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
package org.sonar.batch.scan;

import com.google.common.collect.Lists;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.skyscreamer.jsonassert.JSONAssert;
import org.sonar.api.CoreProperties;
import org.sonar.api.config.Settings;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.internal.DefaultIssue;
import org.sonar.api.platform.Server;
import org.sonar.api.resources.Resource;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rules.Rule;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.batch.events.EventBus;
import org.sonar.batch.issue.IssueCache;
import org.sonar.core.i18n.RuleI18nManager;
import org.sonar.test.TestUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Locale;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonReportTest {

  @org.junit.Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  JsonReport jsonReport;
  Resource resource = mock(Resource.class);
  ModuleFileSystem fileSystem = mock(ModuleFileSystem.class);
  Server server = mock(Server.class);
  RuleI18nManager ruleI18nManager = mock(RuleI18nManager.class);
  Settings settings;
  IssueCache issueCache = mock(IssueCache.class);

  @Before
  public void setUp() {
    when(resource.getEffectiveKey()).thenReturn("Action.java");
    when(server.getVersion()).thenReturn("3.6");

    settings = new Settings();
    settings.setProperty(CoreProperties.DRY_RUN, true);
    jsonReport = new JsonReport(settings, fileSystem, server, ruleI18nManager, issueCache, mock(EventBus.class));
  }

  @Test
  public void should_write_json() throws Exception {
    DefaultIssue issue = new DefaultIssue()
        .setKey("200")
        .setComponentKey("struts:org.apache.struts.Action")
        .setRuleKey(RuleKey.of("squid", "AvoidCycles"))
        .setMessage("There are 2 cycles")
        .setSeverity("MINOR")
        .setStatus(Issue.STATUS_OPEN)
        .setResolution(null)
        .setLine(1)
        .setEffortToFix(3.14)
        .setReporter("julien")
        .setAssignee("simon")
        .setCreationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2013-04-24"))
          .setUpdateDate(new SimpleDateFormat("yyyy-MM-dd").parse("2013-04-25"))
          .setNew(false);
    when(ruleI18nManager.getName("squid", "AvoidCycles", Locale.getDefault())).thenReturn("Avoid Cycles");
    when(jsonReport.getIssues()).thenReturn(Lists.<DefaultIssue> newArrayList(issue));

    StringWriter writer = new StringWriter();
    jsonReport.writeJson(writer);

    JSONAssert.assertEquals(TestUtils.getResourceContent("/org/sonar/batch/scan/JsonReportTest/report.json"),
        writer.toString(), false);
  }

  @Test
  public void should_exclude_resolved_issues() throws Exception {
    DefaultIssue issue = new DefaultIssue()
        .setKey("200")
        .setComponentKey("struts:org.apache.struts.Action")
        .setRuleKey(RuleKey.of("squid", "AvoidCycles"))
        .setStatus(Issue.STATUS_CLOSED)
        .setResolution(Issue.RESOLUTION_FIXED)
        .setCreationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2013-04-24"))
        .setUpdateDate(new SimpleDateFormat("yyyy-MM-dd").parse("2013-04-25"))
        .setCloseDate(new SimpleDateFormat("yyyy-MM-dd").parse("2013-04-26"))
        .setNew(false);
    when(ruleI18nManager.getName("squid", "AvoidCycles", Locale.getDefault())).thenReturn("Avoid Cycles");
    when(jsonReport.getIssues()).thenReturn(Lists.<DefaultIssue> newArrayList(issue));

    StringWriter writer = new StringWriter();
    jsonReport.writeJson(writer);

    JSONAssert.assertEquals(TestUtils.getResourceContent("/org/sonar/batch/scan/JsonReportTest/report-without-resolved-issues.json"),
        writer.toString(), false);
  }

  @Test
  public void should_ignore_components_without_issue() throws JSONException {
    when(jsonReport.getIssues()).thenReturn(Collections.<DefaultIssue> emptyList());

    StringWriter writer = new StringWriter();
    jsonReport.writeJson(writer);

    JSONAssert.assertEquals("{\"version\":\"3.6\"}", writer.toString(), false);
  }

  @Test
  public void should_export_issues_to_file() throws IOException {
    File sonarDirectory = temporaryFolder.newFolder("sonar");

    Rule rule = Rule.create("squid", "AvoidCycles");
    when(ruleI18nManager.getName(rule, Locale.getDefault())).thenReturn("Avoid Cycles");
    when(jsonReport.getIssues()).thenReturn(Collections.<DefaultIssue> emptyList());

    settings.setProperty("sonar.report.export.path", "output.json");
    when(fileSystem.workingDir()).thenReturn(sonarDirectory);

    jsonReport.execute();

    assertThat(new File(sonarDirectory, "output.json")).exists();
  }

}
