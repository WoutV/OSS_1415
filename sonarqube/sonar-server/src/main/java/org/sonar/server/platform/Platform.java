/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2013 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.platform;

import org.apache.commons.configuration.BaseConfiguration;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.EmailSettings;
import org.sonar.api.issue.action.Actions;
import org.sonar.api.platform.ComponentContainer;
import org.sonar.api.platform.Server;
import org.sonar.api.profiles.AnnotationProfileParser;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.profiles.XMLProfileSerializer;
import org.sonar.api.resources.Languages;
import org.sonar.api.resources.ResourceTypes;
import org.sonar.api.rules.AnnotationRuleParser;
import org.sonar.api.rules.XMLRuleParser;
import org.sonar.api.utils.HttpDownloader;
import org.sonar.api.utils.TimeProfiler;
import org.sonar.api.utils.UriReader;
import org.sonar.core.component.SnapshotPerspectives;
import org.sonar.core.config.Logback;
import org.sonar.core.dryrun.DryRunCache;
import org.sonar.core.i18n.GwtI18n;
import org.sonar.core.i18n.I18nManager;
import org.sonar.core.i18n.RuleI18nManager;
import org.sonar.core.issue.IssueFilterSerializer;
import org.sonar.core.issue.IssueNotifications;
import org.sonar.core.issue.IssueUpdater;
import org.sonar.core.issue.workflow.FunctionExecutor;
import org.sonar.core.issue.workflow.IssueWorkflow;
import org.sonar.core.measure.MeasureFilterEngine;
import org.sonar.core.measure.MeasureFilterExecutor;
import org.sonar.core.measure.MeasureFilterFactory;
import org.sonar.core.metric.DefaultMetricFinder;
import org.sonar.core.notification.DefaultNotificationManager;
import org.sonar.core.permission.ComponentPermissionFacade;
import org.sonar.core.persistence.DaoUtils;
import org.sonar.core.persistence.DatabaseVersion;
import org.sonar.core.persistence.DefaultDatabase;
import org.sonar.core.persistence.DryRunDatabaseFactory;
import org.sonar.core.persistence.MyBatis;
import org.sonar.core.persistence.SemaphoreUpdater;
import org.sonar.core.persistence.SemaphoresImpl;
import org.sonar.core.purge.PurgeProfiler;
import org.sonar.core.qualitymodel.DefaultModelFinder;
import org.sonar.core.resource.DefaultResourcePermissions;
import org.sonar.core.rule.DefaultRuleFinder;
import org.sonar.core.source.HtmlSourceDecorator;
import org.sonar.core.test.TestPlanPerspectiveLoader;
import org.sonar.core.test.TestablePerspectiveLoader;
import org.sonar.core.timemachine.Periods;
import org.sonar.core.user.DefaultUserFinder;
import org.sonar.core.user.HibernateUserFinder;
import org.sonar.jpa.dao.MeasuresDao;
import org.sonar.jpa.dao.ProfilesDao;
import org.sonar.jpa.dao.RulesDao;
import org.sonar.jpa.session.DatabaseSessionFactory;
import org.sonar.jpa.session.DatabaseSessionProvider;
import org.sonar.jpa.session.DefaultDatabaseConnector;
import org.sonar.jpa.session.ThreadLocalDatabaseSessionFactory;
import org.sonar.server.charts.ChartFactory;
import org.sonar.server.component.DefaultComponentFinder;
import org.sonar.server.component.DefaultRubyComponentService;
import org.sonar.server.configuration.Backup;
import org.sonar.server.configuration.ProfilesManager;
import org.sonar.server.db.migrations.DatabaseMigration;
import org.sonar.server.db.migrations.DatabaseMigrations;
import org.sonar.server.db.migrations.DatabaseMigrator;
import org.sonar.server.db.EmbeddedDatabaseFactory;
import org.sonar.server.issue.ActionPlanService;
import org.sonar.server.issue.ActionService;
import org.sonar.server.issue.AssignAction;
import org.sonar.server.issue.CommentAction;
import org.sonar.server.issue.DefaultIssueFinder;
import org.sonar.server.issue.InternalRubyIssueService;
import org.sonar.server.issue.IssueBulkChangeService;
import org.sonar.server.issue.IssueChangelogService;
import org.sonar.server.issue.IssueCommentService;
import org.sonar.server.issue.IssueFilterService;
import org.sonar.server.issue.IssueService;
import org.sonar.server.issue.IssueStatsFinder;
import org.sonar.server.issue.PlanAction;
import org.sonar.server.issue.PublicRubyIssueService;
import org.sonar.server.issue.ServerIssueStorage;
import org.sonar.server.issue.SetSeverityAction;
import org.sonar.server.issue.TransitionAction;
import org.sonar.server.notifications.NotificationCenter;
import org.sonar.server.notifications.NotificationService;
import org.sonar.server.permission.InternalPermissionService;
import org.sonar.server.permission.InternalPermissionTemplateService;
import org.sonar.server.plugins.ApplicationDeployer;
import org.sonar.server.plugins.DefaultServerPluginRepository;
import org.sonar.server.plugins.InstalledPluginReferentialFactory;
import org.sonar.server.plugins.PluginDeployer;
import org.sonar.server.plugins.PluginDownloader;
import org.sonar.server.plugins.ServerExtensionInstaller;
import org.sonar.server.plugins.UpdateCenterClient;
import org.sonar.server.plugins.UpdateCenterMatrixFactory;
import org.sonar.server.qualitymodel.DefaultModelManager;
import org.sonar.server.rule.RubyRuleService;
import org.sonar.server.rules.ProfilesConsole;
import org.sonar.server.rules.RulesConsole;
import org.sonar.server.startup.CleanDryRunCache;
import org.sonar.server.startup.DeleteDeprecatedMeasures;
import org.sonar.server.startup.GenerateBootstrapIndex;
import org.sonar.server.startup.GeneratePluginIndex;
import org.sonar.server.startup.GwtPublisher;
import org.sonar.server.startup.JdbcDriverDeployer;
import org.sonar.server.startup.LogServerId;
import org.sonar.server.startup.RegisterMetrics;
import org.sonar.server.startup.RegisterNewDashboards;
import org.sonar.server.startup.RegisterNewMeasureFilters;
import org.sonar.server.startup.RegisterNewProfiles;
import org.sonar.server.startup.RegisterPermissionTemplates;
import org.sonar.server.startup.RegisterQualityModels;
import org.sonar.server.startup.RegisterRules;
import org.sonar.server.startup.RegisterServletFilters;
import org.sonar.server.startup.RenameDeprecatedPropertyKeys;
import org.sonar.server.startup.ServerMetadataPersister;
import org.sonar.server.text.MacroInterpreter;
import org.sonar.server.text.RubyTextService;
import org.sonar.server.ui.CodeColorizers;
import org.sonar.server.ui.JRubyI18n;
import org.sonar.server.ui.PageDecorations;
import org.sonar.server.ui.SecurityRealmFactory;
import org.sonar.server.ui.Views;
import org.sonar.server.user.DefaultUserService;
import org.sonar.server.user.NewUserNotifier;

import javax.servlet.ServletContext;

/**
 * @since 2.2
 */
public final class Platform {

  private static final Platform INSTANCE = new Platform();
  // level 1 : only database connectors
  private ComponentContainer rootContainer;
  // level 2 : level 1 + core components
  private ComponentContainer coreContainer;
  // level 3 : level 2 + plugin extensions + core components that depend on plugin extensions
  private ComponentContainer servicesContainer;
  private boolean connected = false;
  private boolean started = false;

  private Platform() {
  }

  public static Platform getInstance() {
    return INSTANCE;
  }

  /**
   * shortcut for ruby code
   */
  public static Server getServer() {
    return (Server) getInstance().getComponent(Server.class);
  }

  /**
   * Used by ruby code
   */
  public static <T> T component(Class<T> type) {
    return getInstance().getContainer().getComponentByType(type);
  }

  public void init(ServletContext servletContext) {
    if (!connected) {
      try {
        startDatabaseConnectors(servletContext);
        connected = true;

      } catch (RuntimeException e) {
        // full stacktrace is lost by jruby. It must be logged now.
        LoggerFactory.getLogger(getClass()).error(e.getMessage(), e);
        throw e;
      }
    }
  }

  public void start() {
    if (!started && getDatabaseStatus() == DatabaseVersion.Status.UP_TO_DATE) {
      try {
        TimeProfiler profiler = new TimeProfiler().start("Start components");
        startCoreComponents();
        startServiceComponents();
        executeStartupTasks();
        started = true;
        profiler.stop();
      } catch (RuntimeException e) {
        // full stacktrace is lost by jruby. It must be logged now.
        LoggerFactory.getLogger(getClass()).error(e.getMessage(), e);
        throw e;
      }
    }
  }

  private void startDatabaseConnectors(ServletContext servletContext) {
    rootContainer = new ComponentContainer();
    rootContainer.addSingleton(servletContext);
    rootContainer.addSingleton(new BaseConfiguration());
    rootContainer.addSingleton(ServerSettings.class);
    rootContainer.addSingleton(ServerImpl.class);
    rootContainer.addSingleton(Logback.class);
    rootContainer.addSingleton(EmbeddedDatabaseFactory.class);
    rootContainer.addSingleton(DefaultDatabase.class);
    rootContainer.addSingleton(MyBatis.class);
    rootContainer.addSingleton(DefaultServerUpgradeStatus.class);
    rootContainer.addSingleton(DatabaseServerCompatibility.class);
    for (Class<? extends DatabaseMigration> migrationClass : DatabaseMigrations.CLASSES) {
      rootContainer.addSingleton(migrationClass);
    }
    rootContainer.addSingleton(DatabaseMigrator.class);
    rootContainer.addSingleton(DatabaseVersion.class);
    for (Class daoClass : DaoUtils.getDaoClasses()) {
      rootContainer.addSingleton(daoClass);
    }
    rootContainer.addSingleton(PurgeProfiler.class);
    rootContainer.addSingleton(PluginDeployer.class);
    rootContainer.addSingleton(InstalledPluginReferentialFactory.class);
    rootContainer.addSingleton(DefaultServerPluginRepository.class);

    rootContainer.addSingleton(DefaultServerFileSystem.class);
    rootContainer.addSingleton(ApplicationDeployer.class);
    rootContainer.addSingleton(JRubyI18n.class);
    rootContainer.addSingleton(I18nManager.class);
    rootContainer.addSingleton(RuleI18nManager.class);
    rootContainer.addSingleton(GwtI18n.class);
    rootContainer.addSingleton(DryRunDatabaseFactory.class);
    rootContainer.addSingleton(SemaphoreUpdater.class);
    rootContainer.addSingleton(SemaphoresImpl.class);
    rootContainer.startComponents();
  }

  private DatabaseVersion.Status getDatabaseStatus() {
    DatabaseVersion version = getContainer().getComponentByType(DatabaseVersion.class);
    return version.getStatus();
  }

  private void startCoreComponents() {
    coreContainer = rootContainer.createChild();
    coreContainer.addSingleton(PersistentSettings.class);
    coreContainer.addSingleton(DefaultDatabaseConnector.class);
    coreContainer.addSingleton(ServerExtensionInstaller.class);
    coreContainer.addSingleton(ThreadLocalDatabaseSessionFactory.class);
    coreContainer.addPicoAdapter(new DatabaseSessionProvider());
    coreContainer.addSingleton(ServerMetadataPersister.class);
    coreContainer.startComponents();
  }

  /**
   * plugin extensions + all the components that depend on plugin extensions
   */
  private void startServiceComponents() {
    servicesContainer = coreContainer.createChild();

    servicesContainer.addSingleton(HttpDownloader.class);
    servicesContainer.addSingleton(UriReader.class);
    servicesContainer.addSingleton(UpdateCenterClient.class);
    servicesContainer.addSingleton(UpdateCenterMatrixFactory.class);
    servicesContainer.addSingleton(PluginDownloader.class);
    servicesContainer.addSingleton(ServerIdGenerator.class);
    // depends on plugins
    servicesContainer.addSingleton(DefaultModelFinder.class);
    servicesContainer.addSingleton(DefaultModelManager.class);
    servicesContainer.addSingleton(ChartFactory.class);
    servicesContainer.addSingleton(Languages.class);
    servicesContainer.addSingleton(Views.class);
    servicesContainer.addSingleton(CodeColorizers.class);
    servicesContainer.addComponent(RulesDao.class, false);
    servicesContainer.addComponent(MeasuresDao.class, false);
    servicesContainer.addComponent(org.sonar.api.database.daos.MeasuresDao.class, false);
    servicesContainer.addComponent(ProfilesDao.class, false);
    servicesContainer.addComponent(ProfilesManager.class, false);
    servicesContainer.addComponent(Backup.class, false);
    servicesContainer.addSingleton(SecurityRealmFactory.class);
    servicesContainer.addSingleton(ServerLifecycleNotifier.class);
    servicesContainer.addSingleton(AnnotationProfileParser.class);
    servicesContainer.addSingleton(XMLProfileParser.class);
    servicesContainer.addSingleton(XMLProfileSerializer.class);
    servicesContainer.addSingleton(AnnotationRuleParser.class);
    servicesContainer.addSingleton(XMLRuleParser.class);
    servicesContainer.addSingleton(DefaultRuleFinder.class);
    servicesContainer.addSingleton(DefaultMetricFinder.class);
    servicesContainer.addSingleton(ProfilesConsole.class);
    servicesContainer.addSingleton(RulesConsole.class);
    servicesContainer.addSingleton(ResourceTypes.class);
    servicesContainer.addSingleton(SettingsChangeNotifier.class);
    servicesContainer.addSingleton(PageDecorations.class);
    servicesContainer.addSingleton(MeasureFilterFactory.class);
    servicesContainer.addSingleton(MeasureFilterExecutor.class);
    servicesContainer.addSingleton(MeasureFilterEngine.class);
    servicesContainer.addSingleton(DryRunDatabaseFactory.class);
    servicesContainer.addSingleton(DryRunCache.class);
    servicesContainer.addSingleton(DefaultResourcePermissions.class);
    servicesContainer.addSingleton(Periods.class);

    // users
    servicesContainer.addSingleton(HibernateUserFinder.class);
    servicesContainer.addSingleton(NewUserNotifier.class);
    servicesContainer.addSingleton(DefaultUserFinder.class);
    servicesContainer.addSingleton(DefaultUserService.class);
    servicesContainer.addSingleton(ComponentPermissionFacade.class);
    servicesContainer.addSingleton(InternalPermissionService.class);
    servicesContainer.addSingleton(InternalPermissionTemplateService.class);

    // components
    servicesContainer.addSingleton(DefaultComponentFinder.class);
    servicesContainer.addSingleton(DefaultRubyComponentService.class);

    // issues
    servicesContainer.addSingleton(ServerIssueStorage.class);
    servicesContainer.addSingleton(IssueUpdater.class);
    servicesContainer.addSingleton(FunctionExecutor.class);
    servicesContainer.addSingleton(IssueWorkflow.class);
    servicesContainer.addSingleton(IssueService.class);
    servicesContainer.addSingleton(IssueCommentService.class);
    servicesContainer.addSingleton(DefaultIssueFinder.class);
    servicesContainer.addSingleton(IssueStatsFinder.class);
    servicesContainer.addSingleton(PublicRubyIssueService.class);
    servicesContainer.addSingleton(InternalRubyIssueService.class);
    servicesContainer.addSingleton(ActionPlanService.class);
    servicesContainer.addSingleton(IssueChangelogService.class);
    servicesContainer.addSingleton(IssueNotifications.class);
    servicesContainer.addSingleton(ActionService.class);
    servicesContainer.addSingleton(Actions.class);
    servicesContainer.addSingleton(IssueFilterSerializer.class);
    servicesContainer.addSingleton(IssueFilterService.class);
    servicesContainer.addSingleton(IssueBulkChangeService.class);
    // issues actions
    servicesContainer.addSingleton(AssignAction.class);
    servicesContainer.addSingleton(PlanAction.class);
    servicesContainer.addSingleton(SetSeverityAction.class);
    servicesContainer.addSingleton(CommentAction.class);
    servicesContainer.addSingleton(TransitionAction.class);

    // rules
    servicesContainer.addSingleton(RubyRuleService.class);

    // text
    servicesContainer.addSingleton(MacroInterpreter.class);
    servicesContainer.addSingleton(RubyTextService.class);

    // Notifications
    servicesContainer.addSingleton(EmailSettings.class);
    servicesContainer.addSingleton(NotificationService.class);
    servicesContainer.addSingleton(NotificationCenter.class);
    servicesContainer.addSingleton(DefaultNotificationManager.class);

    // graphs and perspective related classes
    servicesContainer.addSingleton(TestablePerspectiveLoader.class);
    servicesContainer.addSingleton(TestPlanPerspectiveLoader.class);
    servicesContainer.addSingleton(SnapshotPerspectives.class);
    servicesContainer.addSingleton(HtmlSourceDecorator.class);

    ServerExtensionInstaller extensionRegistrar = servicesContainer.getComponentByType(ServerExtensionInstaller.class);
    extensionRegistrar.registerExtensions(servicesContainer);

    servicesContainer.startComponents();
  }

  private void executeStartupTasks() {
    ComponentContainer startupContainer = servicesContainer.createChild();
    startupContainer.addSingleton(GwtPublisher.class);
    startupContainer.addSingleton(RegisterMetrics.class);
    startupContainer.addSingleton(RegisterRules.class);
    startupContainer.addSingleton(RegisterNewProfiles.class);
    startupContainer.addSingleton(JdbcDriverDeployer.class);
    startupContainer.addSingleton(RegisterQualityModels.class);
    startupContainer.addSingleton(DeleteDeprecatedMeasures.class);
    startupContainer.addSingleton(GeneratePluginIndex.class);
    startupContainer.addSingleton(GenerateBootstrapIndex.class);
    startupContainer.addSingleton(RegisterNewMeasureFilters.class);
    startupContainer.addSingleton(RegisterNewDashboards.class);
    startupContainer.addSingleton(RegisterPermissionTemplates.class);
    startupContainer.addSingleton(RenameDeprecatedPropertyKeys.class);
    startupContainer.addSingleton(LogServerId.class);
    startupContainer.addSingleton(RegisterServletFilters.class);
    startupContainer.addSingleton(CleanDryRunCache.class);
    startupContainer.startComponents();

    startupContainer.getComponentByType(ServerLifecycleNotifier.class).notifyStart();

    // Do not put the following statements in a finally block.
    // It would hide the possible exception raised during startup
    // See SONAR-3107
    startupContainer.stopComponents();
    servicesContainer.removeChild();
    servicesContainer.getComponentByType(DatabaseSessionFactory.class).clear();
  }

  public void stop() {
    if (rootContainer != null) {
      try {
        TimeProfiler profiler = new TimeProfiler().start("Stop sonar");
        rootContainer.stopComponents();
        rootContainer = null;
        connected = false;
        started = false;
        profiler.stop();
      } catch (Exception e) {
        LoggerFactory.getLogger(getClass()).debug("Fail to stop SonarQube - ignored", e);
      }
    }
  }

  public ComponentContainer getContainer() {
    if (servicesContainer != null) {
      return servicesContainer;
    }
    if (coreContainer != null) {
      return coreContainer;
    }
    return rootContainer;
  }

  public Object getComponent(Object key) {
    return getContainer().getComponentByKey(key);
  }
}
