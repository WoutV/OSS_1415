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

package org.sonar.server.issue;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.component.Component;
import org.sonar.api.config.Settings;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.IssueQuery;
import org.sonar.api.issue.IssueQueryResult;
import org.sonar.api.issue.action.Actions;
import org.sonar.api.issue.action.Function;
import org.sonar.api.issue.condition.Condition;
import org.sonar.api.issue.internal.DefaultIssue;
import org.sonar.api.issue.internal.IssueChangeContext;
import org.sonar.core.issue.DefaultIssueQueryResult;
import org.sonar.core.issue.IssueUpdater;
import org.sonar.core.issue.db.IssueStorage;
import org.sonar.core.properties.PropertiesDao;
import org.sonar.core.properties.PropertyDto;
import org.sonar.server.user.UserSession;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ActionServiceTest {

  DefaultIssueFinder finder;
  IssueStorage issueStorage;
  IssueUpdater updater;
  PropertiesDao propertiesDao;
  Settings settings;
  Actions actions;
  ActionService actionService;

  @Before
  public void before() {
    finder = mock(DefaultIssueFinder.class);
    issueStorage = mock(IssueStorage.class);
    updater = mock(IssueUpdater.class);
    propertiesDao = mock(PropertiesDao.class);
    settings = new Settings();
    actions = new Actions();
    actionService = new ActionService(finder, issueStorage, updater, settings, propertiesDao, actions);
  }

  @Test
  public void should_execute_functions() {
    Function function1 = mock(Function.class);
    Function function2 = mock(Function.class);

    Issue issue = new DefaultIssue().setKey("ABCD");
    IssueQueryResult issueQueryResult = mock(DefaultIssueQueryResult.class);
    when(issueQueryResult.first()).thenReturn(issue);

    when(issueQueryResult.project(issue)).thenReturn(mock(Component.class));
    when(finder.find(any(IssueQuery.class))).thenReturn(issueQueryResult);

    actions.add("link-to-jira").setConditions(new AlwaysMatch()).setFunctions(function1, function2);

    assertThat(actionService.execute("ABCD", "link-to-jira", mock(UserSession.class))).isNotNull();

    verify(function1).execute(any(Function.Context.class));
    verify(function2).execute(any(Function.Context.class));
    verifyNoMoreInteractions(function1, function2);
  }

  @Test
  public void should_modify_issue_when_executing_a_function() {
    Function function = new TweetFunction();

    UserSession userSession = mock(UserSession.class);
    when(userSession.login()).thenReturn("arthur");

    DefaultIssue issue = new DefaultIssue().setKey("ABCD");
    IssueQueryResult issueQueryResult = mock(DefaultIssueQueryResult.class);
    when(issueQueryResult.first()).thenReturn(issue);
    when(issueQueryResult.project(issue)).thenReturn(mock(Component.class));

    when(finder.find(any(IssueQuery.class))).thenReturn(issueQueryResult);

    actions.add("link-to-jira").setConditions(new AlwaysMatch()).setFunctions(function);
    assertThat(actionService.execute("ABCD", "link-to-jira", userSession)).isNotNull();

    verify(updater).addComment(eq(issue), eq("New tweet on issue ABCD"), any(IssueChangeContext.class));
    verify(updater).setAttribute(eq(issue), eq("tweet"), eq("tweet sent"), any(IssueChangeContext.class));
  }

  @Test
  public void should_inject_project_settings_when_executing_a_function() {
    Function function = new TweetFunction();

    UserSession userSession = mock(UserSession.class);
    when(userSession.login()).thenReturn("arthur");

    DefaultIssue issue = new DefaultIssue().setKey("ABCD");
    IssueQueryResult issueQueryResult = mock(DefaultIssueQueryResult.class);
    when(issueQueryResult.first()).thenReturn(issue);
    Component project = mock(Component.class);
    when(project.key()).thenReturn("struts");
    when(issueQueryResult.project(issue)).thenReturn(project);

    when(finder.find(any(IssueQuery.class))).thenReturn(issueQueryResult);

    actions.add("link-to-jira").setConditions(new AlwaysMatch()).setFunctions(function);
    assertThat(actionService.execute("ABCD", "link-to-jira", userSession)).isNotNull();

    verify(propertiesDao).selectProjectProperties(eq("struts"));
  }

  @Test
  public void should_not_execute_function_if_issue_not_found() {
    Function function = mock(Function.class);

    IssueQueryResult issueQueryResult = new DefaultIssueQueryResult(Collections.<Issue>emptyList());
    when(finder.find(any(IssueQuery.class))).thenReturn(issueQueryResult);

    actions.add("link-to-jira").setConditions(new AlwaysMatch()).setFunctions(function);
    try {
      actionService.execute("ABCD", "link-to-jira", mock(UserSession.class));
      fail();
    } catch (Exception e) {
      assertThat(e).isInstanceOf(IllegalStateException.class).hasMessage("No issue found");
    }
    verifyZeroInteractions(function);
  }

  @Test
  public void should_not_execute_function_if_action_not_found() {
    Function function = mock(Function.class);

    Issue issue = new DefaultIssue().setKey("ABCD");
    IssueQueryResult issueQueryResult = new DefaultIssueQueryResult(newArrayList(issue));
    when(finder.find(any(IssueQuery.class))).thenReturn(issueQueryResult);

    actions.add("link-to-jira").setConditions(new AlwaysMatch()).setFunctions(function);
    try {
      actionService.execute("ABCD", "tweet", mock(UserSession.class));
      fail();
    } catch (Exception e) {
      assertThat(e).isInstanceOf(IllegalArgumentException.class).hasMessage("Action is not found : tweet");
    }
    verifyZeroInteractions(function);
  }

  @Test
  public void should_not_execute_function_if_action_is_not_supported() {
    Function function = mock(Function.class);

    Issue issue = new DefaultIssue().setKey("ABCD");
    IssueQueryResult issueQueryResult = new DefaultIssueQueryResult(newArrayList(issue));
    when(finder.find(any(IssueQuery.class))).thenReturn(issueQueryResult);

    actions.add("link-to-jira").setConditions(new NeverMatch()).setFunctions(function);
    try {
      actionService.execute("ABCD", "link-to-jira", mock(UserSession.class));
      fail();
    } catch (Exception e) {
      assertThat(e).isInstanceOf(IllegalStateException.class).hasMessage("A condition is not respected");
    }
    verifyZeroInteractions(function);
  }

  @Test
  public void should_list_available_supported_actions() {
    Issue issue = new DefaultIssue().setKey("ABCD");
    IssueQueryResult issueQueryResult = new DefaultIssueQueryResult(newArrayList(issue));
    when(finder.find(any(IssueQuery.class))).thenReturn(issueQueryResult);

    actions.add("link-to-jira").setConditions(new AlwaysMatch());
    actions.add("tweet").setConditions(new NeverMatch());
    assertThat(actionService.listAvailableActions("ABCD")).hasSize(1);
  }

  @Test
  public void should_list_available_actions_throw_exception_if_issue_not_found() {
    IssueQueryResult issueQueryResult = new DefaultIssueQueryResult(Collections.<Issue>emptyList());
    when(finder.find(any(IssueQuery.class))).thenReturn(issueQueryResult);

    actions.add("link-to-jira").setConditions(new AlwaysMatch());
    actions.add("tweet").setConditions(new NeverMatch());
    try {
      actionService.listAvailableActions("ABCD");
      fail();
    } catch (Exception e) {
      assertThat(e).isInstanceOf(IllegalStateException.class).hasMessage("No issue found");
    }
  }

  @Test
  public void should_return_no_action() {
    Issue issue = new DefaultIssue().setKey("ABCD");
    IssueQueryResult issueQueryResult = new DefaultIssueQueryResult(newArrayList(issue));
    when(finder.find(any(IssueQuery.class))).thenReturn(issueQueryResult);

    assertThat(actionService.listAvailableActions("ABCD")).isEmpty();
  }

  @Test
  public void should_get_project_settings(){
    Component project = mock(Component.class);
    when(project.key()).thenReturn("struts");

    // Global property
    settings.appendProperty("sonar.core.version", "3.6");

    // Project property
    List<PropertyDto> projectProperties = newArrayList(new PropertyDto().setKey("sonar.jira.project.key").setValue("STRUTS"));
    when(propertiesDao.selectProjectProperties("struts")).thenReturn(projectProperties);

    Settings result = actionService.getProjectSettings(project);
    assertThat(result).isNotNull();
    assertThat(result.hasKey("sonar.core.version")).isTrue();
    assertThat(result.hasKey("sonar.jira.project.key")).isTrue();
  }

  public class AlwaysMatch implements Condition {
    @Override
    public boolean matches(Issue issue) {
      return true;
    }
  }

  public class NeverMatch implements Condition {
    @Override
    public boolean matches(Issue issue) {
      return false;
    }
  }

  public class TweetFunction implements Function {
    @Override
    public void execute(Context context) {
      context.addComment("New tweet on issue " + context.issue().key());
      context.setAttribute("tweet", "tweet sent");
    }
  }
}
