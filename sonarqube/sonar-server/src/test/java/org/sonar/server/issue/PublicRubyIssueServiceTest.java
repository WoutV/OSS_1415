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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.sonar.api.issue.IssueFinder;
import org.sonar.api.issue.IssueQuery;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.DateUtils;
import org.sonar.api.web.UserRole;

import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

public class PublicRubyIssueServiceTest {

  IssueFinder finder = mock(IssueFinder.class);
  PublicRubyIssueService facade = new PublicRubyIssueService(finder);

  @Test
  public void find_by_issue_keys() throws Exception {
    facade.find("ABCDE");
    verify(finder).find(argThat(new ArgumentMatcher<IssueQuery>() {
      @Override
      public boolean matches(Object o) {
        IssueQuery query = (IssueQuery) o;
        return query.issueKeys().contains("ABCDE") && UserRole.USER.equals(query.requiredRole());
      }
    }));
  }

  @Test
  public void find_by_params() throws Exception {
    facade.find(ImmutableMap.<String, Object>of("issues", Lists.newArrayList("ABCDE")));
    verify(finder).find(argThat(new ArgumentMatcher<IssueQuery>() {
      @Override
      public boolean matches(Object o) {
        IssueQuery query = (IssueQuery) o;
        return query.issueKeys().contains("ABCDE") && UserRole.USER.equals(query.requiredRole());
      }
    }));
  }

  @Test
  public void should_create_query_from_parameters() {
    Map<String, Object> map = newHashMap();
    map.put("issues", newArrayList("ABCDE1234"));
    map.put("severities", newArrayList("MAJOR", "MINOR"));
    map.put("statuses", newArrayList("CLOSED"));
    map.put("resolutions", newArrayList("FALSE-POSITIVE"));
    map.put("resolved", true);
    map.put("components", newArrayList("org.apache"));
    map.put("componentRoots", newArrayList("org.sonar"));
    map.put("reporters", newArrayList("marilyn"));
    map.put("assignees", newArrayList("joanna"));
    map.put("assigned", true);
    map.put("planned", true);
    map.put("createdAfter", "2013-04-16T09:08:24+0200");
    map.put("createdBefore", "2013-04-17T09:08:24+0200");
    map.put("rules", "squid:AvoidCycle,findbugs:NullReference");
    map.put("pageSize", 10l);
    map.put("pageIndex", 50);
    map.put("sort", "CREATION_DATE");
    map.put("asc", true);

    IssueQuery query = new PublicRubyIssueService(finder).toQuery(map);
    assertThat(query.issueKeys()).containsOnly("ABCDE1234");
    assertThat(query.severities()).containsOnly("MAJOR", "MINOR");
    assertThat(query.statuses()).containsOnly("CLOSED");
    assertThat(query.resolutions()).containsOnly("FALSE-POSITIVE");
    assertThat(query.resolved()).isTrue();
    assertThat(query.components()).containsOnly("org.apache");
    assertThat(query.componentRoots()).containsOnly("org.sonar");
    assertThat(query.reporters()).containsOnly("marilyn");
    assertThat(query.assignees()).containsOnly("joanna");
    assertThat(query.assigned()).isTrue();
    assertThat(query.planned()).isTrue();
    assertThat(query.rules()).hasSize(2);
    assertThat(query.createdAfter()).isEqualTo(DateUtils.parseDateTime("2013-04-16T09:08:24+0200"));
    assertThat(query.createdBefore()).isEqualTo(DateUtils.parseDateTime("2013-04-17T09:08:24+0200"));
    assertThat(query.pageSize()).isEqualTo(10);
    assertThat(query.pageIndex()).isEqualTo(50);
    assertThat(query.sort()).isEqualTo(IssueQuery.SORT_BY_CREATION_DATE);
    assertThat(query.asc()).isTrue();
  }

  @Test
  public void should_parse_list_of_rules() {
    assertThat(PublicRubyIssueService.toRules(null)).isNull();
    assertThat(PublicRubyIssueService.toRules("")).isEmpty();
    assertThat(PublicRubyIssueService.toRules("squid:AvoidCycle")).containsOnly(RuleKey.of("squid", "AvoidCycle"));
    assertThat(PublicRubyIssueService.toRules("squid:AvoidCycle,findbugs:NullRef")).containsOnly(RuleKey.of("squid", "AvoidCycle"), RuleKey.of("findbugs", "NullRef"));
    assertThat(PublicRubyIssueService.toRules(asList("squid:AvoidCycle", "findbugs:NullRef"))).containsOnly(RuleKey.of("squid", "AvoidCycle"), RuleKey.of("findbugs", "NullRef"));
  }

  @Test
  public void should_start() throws Exception {
    facade.start();
    // nothing is done
    verifyZeroInteractions(finder);
  }
}
