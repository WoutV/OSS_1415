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
package org.sonar.api.issue;

import org.sonar.api.component.Component;
import org.sonar.api.rules.Rule;
import org.sonar.api.user.User;
import org.sonar.api.utils.Paging;

import javax.annotation.CheckForNull;

import java.util.Collection;
import java.util.List;

/**
 * @since 3.6
 */
public interface IssueQueryResult {
  /**
   * Non-null paginated list of issues.
   */
  List<Issue> issues();

  /**
   * Returns the first issue in the list.
   * @throws IllegalStateException if the list is empty.
   */
  Issue first();

  /**
   * Returns the rule associated to the given issue.
   */
  Rule rule(Issue issue);

  /**
   * The rules involved in the paginated {@link #issues()}.
   */
  Collection<Rule> rules();

  Component component(Issue issue);

  /**
   * The components involved in the paginated {@link #issues()}.
   */
  Collection<Component> components();

  Component project(Issue issue);

  /**
   * The projects involved in the paginated {@link #issues()}.
   */
  Collection<Component> projects();

  @CheckForNull
  ActionPlan actionPlan(Issue issue);

  /**
   * The action plans involved in the paginated {@link #issues()}.
   */
  Collection<ActionPlan> actionPlans();

  /**
   * The users involved in the paginated {@link #issues()}, for example people who added a comment, reported an issue
   * or are assigned to issues.
   */
  Collection<User> users();

  /**
   * Returns the user with the given login. Users that are not returned by {@link #users()} are ignored.
   */
  @CheckForNull
  User user(String login);

  /**
   * Non-null data about paging of issues
   */
  Paging paging();

  /**
   * True if too many issues have been found. In this case results are truncated.
   */
  boolean maxResultsReached();
}
