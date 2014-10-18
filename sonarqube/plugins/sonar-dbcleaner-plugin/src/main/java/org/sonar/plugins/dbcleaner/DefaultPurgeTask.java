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
package org.sonar.plugins.dbcleaner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.CoreProperties;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Scopes;
import org.sonar.api.utils.TimeUtils;
import org.sonar.core.purge.PurgeConfiguration;
import org.sonar.core.purge.PurgeDao;
import org.sonar.core.purge.PurgeProfiler;
import org.sonar.plugins.dbcleaner.api.DbCleanerConstants;
import org.sonar.plugins.dbcleaner.api.PurgeTask;
import org.sonar.plugins.dbcleaner.period.DefaultPeriodCleaner;

/**
 * @since 2.14
 */
public class DefaultPurgeTask implements PurgeTask {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultPurgeTask.class);

  private PurgeDao purgeDao;
  private Settings settings;
  private DefaultPeriodCleaner periodCleaner;
  private final PurgeProfiler profiler;

  public DefaultPurgeTask(PurgeDao purgeDao, Settings settings, DefaultPeriodCleaner periodCleaner, PurgeProfiler profiler) {
    this.purgeDao = purgeDao;
    this.settings = settings;
    this.periodCleaner = periodCleaner;
    this.profiler = profiler;
  }

  public PurgeTask delete(long resourceId) {
    purgeDao.deleteResourceTree(resourceId);
    return this;
  }

  public PurgeTask purge(long resourceId) {
    long start = System.currentTimeMillis();
    profiler.reset();
    cleanHistoricalData(resourceId);
    doPurge(resourceId);
    if (settings.getBoolean(CoreProperties.PROFILING_LOG_PROPERTY)) {
      long duration = System.currentTimeMillis() - start;
      LOG.info("\n -------- Profiling for purge: " + TimeUtils.formatDuration(duration) + " --------\n");
      profiler.dump(duration, LOG);
      LOG.info("\n -------- End of profiling for purge --------\n");
    }
    return this;
  }

  private void cleanHistoricalData(long resourceId) {
    try {
      periodCleaner.clean(resourceId);
    } catch (Exception e) {
      // purge errors must no fail the batch
      LOG.error("Fail to clean historical data [id=" + resourceId + "]", e);
    }
  }

  private void doPurge(long resourceId) {
    try {
      purgeDao.purge(newConf(resourceId));
    } catch (Exception e) {
      // purge errors must no fail the batch
      LOG.error("Fail to purge data [id=" + resourceId + "]", e);
    }
  }

  private PurgeConfiguration newConf(long resourceId) {
    String[] scopes = new String[] {Scopes.FILE};
    if (settings.getBoolean(DbCleanerConstants.PROPERTY_CLEAN_DIRECTORY)) {
      scopes = new String[] {Scopes.DIRECTORY, Scopes.FILE};
    }
    return new PurgeConfiguration(resourceId, scopes, settings.getInt(DbCleanerConstants.DAYS_BEFORE_DELETING_CLOSED_ISSUES));
  }
}
