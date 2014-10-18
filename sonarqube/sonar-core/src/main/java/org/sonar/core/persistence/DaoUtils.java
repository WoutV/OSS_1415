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
package org.sonar.core.persistence;

import org.sonar.core.notification.db.NotificationQueueDao;

import com.google.common.collect.ImmutableList;
import org.sonar.core.dashboard.ActiveDashboardDao;
import org.sonar.core.dashboard.DashboardDao;
import org.sonar.core.duplication.DuplicationDao;
import org.sonar.core.graph.jdbc.GraphDao;
import org.sonar.core.issue.db.*;
import org.sonar.core.measure.MeasureFilterDao;
import org.sonar.core.permission.PermissionDao;
import org.sonar.core.properties.PropertiesDao;
import org.sonar.core.purge.PurgeDao;
import org.sonar.core.resource.ResourceDao;
import org.sonar.core.resource.ResourceIndexerDao;
import org.sonar.core.resource.ResourceKeyUpdaterDao;
import org.sonar.core.rule.RuleDao;
import org.sonar.core.source.jdbc.SnapshotDataDao;
import org.sonar.core.template.LoadedTemplateDao;
import org.sonar.core.user.AuthorDao;
import org.sonar.core.user.AuthorizationDao;
import org.sonar.core.user.RoleDao;
import org.sonar.core.user.UserDao;

import java.util.List;

public final class DaoUtils {

  private DaoUtils() {
  }

  @SuppressWarnings("unchecked")
  public static List<Class<?>> getDaoClasses() {
    return ImmutableList.of(
      ActionPlanDao.class,
      ActionPlanStatsDao.class,
      ActiveDashboardDao.class,
      AuthorDao.class,
      AuthorizationDao.class,
      DashboardDao.class,
      DuplicationDao.class,
      GraphDao.class,
      IssueDao.class,
      IssueStatsDao.class,
      IssueChangeDao.class,
      IssueFilterDao.class,
      IssueFilterFavouriteDao.class,
      LoadedTemplateDao.class,
      MeasureFilterDao.class,
      NotificationQueueDao.class,
      PermissionDao.class,
      PropertiesDao.class,
      PurgeDao.class,
      ResourceIndexerDao.class,
      ResourceDao.class,
      ResourceKeyUpdaterDao.class,
      RoleDao.class,
      RuleDao.class,
      SemaphoreDao.class,
      SnapshotDataDao.class,
      UserDao.class
    );
  }
}
