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
package org.sonar.plugins.core.widgets.issues;

import org.sonar.api.web.*;
import org.sonar.plugins.core.widgets.CoreWidget;

import static org.sonar.api.web.WidgetScope.GLOBAL;

@WidgetCategory({"Filters", "Global", "Issues"})
@WidgetScope(GLOBAL)
@WidgetProperties({
  @WidgetProperty(key = IssueFilterWidget.FILTER_PROPERTY, type = WidgetPropertyType.ISSUE_FILTER, optional = false),
  @WidgetProperty(key = IssueFilterWidget.PAGE_SIZE_PROPERTY, type = WidgetPropertyType.INTEGER, defaultValue = "30"),
  @WidgetProperty(key = IssueFilterWidget.DISPLAY_FILTER_DESCRIPTION, type = WidgetPropertyType.BOOLEAN, defaultValue = "false")
})
public class IssueFilterWidget extends CoreWidget {

  public static final String FILTER_PROPERTY = "filter";
  public static final String PAGE_SIZE_PROPERTY = "numberOfLines";
  public static final String DISPLAY_FILTER_DESCRIPTION = "displayFilterDescription";
  public static final String ID = "issue_filter";

  public IssueFilterWidget() {
    super(ID, "Issue Filter", "/org/sonar/plugins/core/widgets/issues/issue_filter.html.erb");
  }
}
