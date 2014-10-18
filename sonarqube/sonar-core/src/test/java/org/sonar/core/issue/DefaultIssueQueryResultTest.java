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

import org.junit.Test;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.internal.DefaultIssue;

import java.util.Arrays;
import java.util.Collections;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;

public class DefaultIssueQueryResultTest {

  @Test(expected = IllegalStateException.class)
  public void first_should_throw_exception_if_no_issues() {
    DefaultIssueQueryResult result = new DefaultIssueQueryResult(Collections.<Issue>emptyList());
    result.first();
  }

  @Test
  public void test_first_issue() {
    DefaultIssueQueryResult result = new DefaultIssueQueryResult(newArrayList((Issue) new DefaultIssue()));
    assertThat(result.first()).isNotNull();

    Issue first = new DefaultIssue();
    Issue second = new DefaultIssue();
    result = new DefaultIssueQueryResult(Arrays.asList(first, second));
    assertThat(result.first()).isSameAs(first);
  }
}
