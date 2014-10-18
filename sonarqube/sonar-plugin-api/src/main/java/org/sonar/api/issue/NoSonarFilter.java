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

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * Issue filter used to ignore issues created on lines commented with the tag "NOSONAR".
 * <p/>
 * Plugins, via {@link org.sonar.api.BatchExtension}s, must feed this filter by registering the
 * lines that contain "NOSONAR". Note that filters are disabled for the issues reported by
 * end-users from UI or web services.
 *
 * @since 3.6
 */
public class NoSonarFilter implements IssueFilter {

  private final Map<String, Set<Integer>> noSonarLinesByResource = Maps.newHashMap();

  public NoSonarFilter addComponent(String componentKey, Set<Integer> noSonarLines) {
    noSonarLinesByResource.put(componentKey, noSonarLines);
    return this;
  }

  @Override
  public boolean accept(Issue issue) {
    boolean accepted = true;
    if (issue.line() != null) {
      Set<Integer> noSonarLines = noSonarLinesByResource.get(issue.componentKey());
      accepted = noSonarLines == null || !noSonarLines.contains(issue.line());
      if (!accepted && StringUtils.containsIgnoreCase(issue.ruleKey().rule(), "nosonar")) {
        accepted = true;
      }
    }
    return accepted;
  }
}
