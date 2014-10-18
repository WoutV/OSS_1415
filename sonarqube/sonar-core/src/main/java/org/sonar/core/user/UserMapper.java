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
package org.sonar.core.user;

import org.apache.ibatis.annotations.Param;
import org.sonar.api.user.UserQuery;

import javax.annotation.CheckForNull;

import java.util.List;

public interface UserMapper {

  @CheckForNull
  UserDto selectUser(long userId);

  /**
   * Select user by login. Note that disabled users are ignored.
   */
  @CheckForNull
  UserDto selectUserByLogin(String login);

  List<UserDto> selectUsersByLogins(@Param("logins") List<String> logins);

  List<UserDto> selectUsers(UserQuery query);

  @CheckForNull
  GroupDto selectGroupByName(String name);

  void removeUserFromGroups(long userId);

  void deleteUserActiveDashboards(long userId);

  void deleteUserDashboards(long userId);

  void deleteUserIssueFilters(String login);

  void deleteUserIssueFilterFavourites(String login);

  void deleteUserMeasureFilters(long userId);

  void deleteUserMeasureFilterFavourites(long userId);

  void deleteUserProperties(long userId);

  void deleteUserRoles(long userId);

  void deactivateUser(long userId);
}
