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
package org.sonar.core.issue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.sonar.api.component.Component;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.internal.DefaultIssue;
import org.sonar.api.issue.internal.IssueChangeContext;
import org.sonar.api.notifications.Notification;
import org.sonar.api.notifications.NotificationManager;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.DateUtils;
import org.sonar.core.component.ResourceComponent;
import org.sonar.core.i18n.RuleI18nManager;

import java.util.Arrays;
import java.util.Date;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class IssueNotificationsTest {

  @Mock
  NotificationManager manager;

  @Mock
  RuleI18nManager ruleI18n;

  IssueNotifications issueNotifications;

  @Before
  public void setUp() throws Exception {
    issueNotifications = new IssueNotifications(manager, ruleI18n);
  }

  @Test
  public void should_send_new_issues() throws Exception {
    Date date = DateUtils.parseDateTime("2013-05-18T13:00:03+0200");
    Project project = new Project("struts").setAnalysisDate(date);
    Notification notification = issueNotifications.sendNewIssues(project, 42);

    assertThat(notification.getFieldValue("count")).isEqualTo("42");
    assertThat(DateUtils.parseDateTime(notification.getFieldValue("projectDate"))).isEqualTo(date);
    Mockito.verify(manager).scheduleForSending(notification);
  }

  @Test
  public void should_send_changes() throws Exception {
    IssueChangeContext context = IssueChangeContext.createScan(new Date());
    DefaultIssue issue = new DefaultIssue()
      .setMessage("the message")
      .setKey("ABCDE")
      .setAssignee("freddy")
      .setFieldChange(context, "resolution", null, "FIXED")
      .setFieldChange(context, "status", "OPEN", "RESOLVED")
      .setFieldChange(context, "assignee", "simon", null)
      .setSendNotifications(true)
      .setComponentKey("struts:Action")
      .setProjectKey("struts");
    DefaultIssueQueryResult queryResult = new DefaultIssueQueryResult(Arrays.<Issue> asList(issue));
    queryResult.addProjects(Arrays.<Component> asList(new Project("struts")));

    Notification notification = issueNotifications.sendChanges(issue, context, queryResult).get(0);

    assertThat(notification.getFieldValue("message")).isEqualTo("the message");
    assertThat(notification.getFieldValue("key")).isEqualTo("ABCDE");
    assertThat(notification.getFieldValue("componentKey")).isEqualTo("struts:Action");
    assertThat(notification.getFieldValue("componentName")).isNull();
    assertThat(notification.getFieldValue("old.resolution")).isNull();
    assertThat(notification.getFieldValue("new.resolution")).isEqualTo("FIXED");
    assertThat(notification.getFieldValue("old.status")).isEqualTo("OPEN");
    assertThat(notification.getFieldValue("new.status")).isEqualTo("RESOLVED");
    assertThat(notification.getFieldValue("old.assignee")).isEqualTo("simon");
    assertThat(notification.getFieldValue("new.assignee")).isNull();
    Mockito.verify(manager).scheduleForSending(eq(Arrays.asList(notification)));
  }

  @Test
  public void should_send_changes_with_comment() throws Exception {
    IssueChangeContext context = IssueChangeContext.createScan(new Date());
    DefaultIssue issue = new DefaultIssue()
      .setMessage("the message")
      .setKey("ABCDE")
      .setAssignee("freddy")
      .setComponentKey("struts:Action")
      .setProjectKey("struts");
    DefaultIssueQueryResult queryResult = new DefaultIssueQueryResult(Arrays.<Issue> asList(issue));
    queryResult.addProjects(Arrays.<Component> asList(new Project("struts")));

    Notification notification = issueNotifications.sendChanges(issue, context, queryResult, "I don't know how to fix it?");

    assertThat(notification.getFieldValue("message")).isEqualTo("the message");
    assertThat(notification.getFieldValue("key")).isEqualTo("ABCDE");
    assertThat(notification.getFieldValue("comment")).isEqualTo("I don't know how to fix it?");
    Mockito.verify(manager).scheduleForSending(notification);
  }

  @Test
  public void should_send_changes_with_component_name() throws Exception {
    IssueChangeContext context = IssueChangeContext.createScan(new Date());
    DefaultIssue issue = new DefaultIssue()
      .setMessage("the message")
      .setKey("ABCDE")
      .setAssignee("freddy")
      .setFieldChange(context, "resolution", null, "FIXED")
      .setSendNotifications(true)
      .setComponentKey("struts:Action")
      .setProjectKey("struts");
    DefaultIssueQueryResult queryResult = new DefaultIssueQueryResult(Arrays.<Issue> asList(issue));
    queryResult.addProjects(Arrays.<Component> asList(new Project("struts")));
    queryResult.addComponents(Arrays.<Component> asList(new ResourceComponent(new File("struts:Action").setEffectiveKey("struts:Action"))));

    Notification notification = issueNotifications.sendChanges(issue, context, queryResult).get(0);

    assertThat(notification.getFieldValue("message")).isEqualTo("the message");
    assertThat(notification.getFieldValue("key")).isEqualTo("ABCDE");
    assertThat(notification.getFieldValue("componentKey")).isEqualTo("struts:Action");
    assertThat(notification.getFieldValue("componentName")).isEqualTo("struts:Action");
    assertThat(notification.getFieldValue("old.resolution")).isNull();
    assertThat(notification.getFieldValue("new.resolution")).isEqualTo("FIXED");
    Mockito.verify(manager).scheduleForSending(eq(Arrays.asList(notification)));
  }

  @Test
  public void should_not_send_changes_if_no_diffs() throws Exception {
    IssueChangeContext context = IssueChangeContext.createScan(new Date());
    DefaultIssue issue = new DefaultIssue()
      .setMessage("the message")
      .setKey("ABCDE")
      .setComponentKey("struts:Action")
      .setProjectKey("struts");
    DefaultIssueQueryResult queryResult = new DefaultIssueQueryResult(Arrays.<Issue> asList(issue));
    queryResult.addProjects(Arrays.<Component> asList(new Project("struts")));

    Notification notification = issueNotifications.sendChanges(issue, context, queryResult, null);

    assertThat(notification).isNull();
    Mockito.verifyZeroInteractions(manager);
  }
}
