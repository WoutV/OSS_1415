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
package org.sonar.batch.phases;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.BatchExtensionDictionnary;
import org.sonar.api.batch.PostJob;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.batch.events.EventBus;
import org.sonar.batch.scan.DeprecatedJsonReport;
import org.sonar.batch.scan.filesystem.DefaultModuleFileSystem;
import org.sonar.batch.scan.maven.MavenPluginExecutor;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostJobsExecutorTest {
  PostJobsExecutor executor;

  Project project = new Project("project");
  BatchExtensionDictionnary selector = mock(BatchExtensionDictionnary.class);
  MavenPluginExecutor mavenPluginExecutor = mock(MavenPluginExecutor.class);
  DeprecatedJsonReport localModeExporter = mock(DeprecatedJsonReport.class);
  PostJob job1 = mock(PostJob.class);
  PostJob job2 = mock(PostJob.class);
  SensorContext context = mock(SensorContext.class);

  @Before
  public void setUp() {
    executor = new PostJobsExecutor(selector, project, mock(DefaultModuleFileSystem.class), mavenPluginExecutor, localModeExporter, mock(EventBus.class));
  }

  @Test
  public void should_execute_post_jobs() {
    when(selector.select(PostJob.class, project, true)).thenReturn(Arrays.asList(job1, job2));

    executor.execute(context);

    verify(job1).executeOn(project, context);
    verify(job2).executeOn(project, context);

  }

  @Test
  public void should_export_local_mode_results() {
    executor.execute(context);

    verify(localModeExporter).execute();
  }
}
