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

package org.sonar.server.permission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.ServerComponent;
import org.sonar.api.security.DefaultGroups;
import org.sonar.api.web.UserRole;
import org.sonar.core.permission.ComponentPermissionFacade;
import org.sonar.core.permission.Permission;
import org.sonar.core.user.*;
import org.sonar.server.exceptions.BadRequestException;
import org.sonar.server.exceptions.ForbiddenException;
import org.sonar.server.user.UserSession;

import javax.annotation.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Used by ruby code <pre>Internal.permissions</pre>
 */
public class InternalPermissionService implements ServerComponent {

  private static final Logger LOG = LoggerFactory.getLogger(InternalPermissionService.class);

  private static final String ADD = "add";
  private static final String REMOVE = "remove";

  private final RoleDao roleDao;
  private final UserDao userDao;
  private final ComponentPermissionFacade permissionFacade;

  public InternalPermissionService(RoleDao roleDao, UserDao userDao, ComponentPermissionFacade permissionFacade) {
    this.roleDao = roleDao;
    this.userDao = userDao;
    this.permissionFacade = permissionFacade;
  }

  public void addPermission(final Map<String, Object> params) {
    changePermission(ADD, params);
  }

  public void removePermission(Map<String, Object> params) {
    changePermission(REMOVE, params);
  }

  public void applyPermissionTemplate(Map<String, Object> params) {
    UserSession.get().checkLoggedIn();
    ApplyPermissionTemplateQuery query = ApplyPermissionTemplateQuery.buildFromParams(params);
    query.validate();

    // If only one project is selected, check user has admin permission on it, otherwise we are in the case of a bulk change and only system admin has permission to do it
    if (query.getSelectedComponents().size() == 1) {
      checkProjectAdminPermission(Long.parseLong(query.getSelectedComponents().get(0)));
    } else {
      checkProjectAdminPermission(null);
    }

    for (String component : query.getSelectedComponents()) {
      applyPermissionTemplate(query.getTemplateKey(), component);
    }
  }

  private void applyPermissionTemplate(String templateKey, String componentId) {
    permissionFacade.applyPermissionTemplate(templateKey, Long.parseLong(componentId));
  }

  private void changePermission(String permissionChange, Map<String, Object> params) {
    UserSession.get().checkLoggedIn();
    UserSession.get().checkGlobalPermission(Permission.SYSTEM_ADMIN);
    PermissionChangeQuery permissionChangeQuery = PermissionChangeQuery.buildFromParams(params);
    permissionChangeQuery.validate();
    applyPermissionChange(permissionChange, permissionChangeQuery);
  }

  private void applyPermissionChange(String operation, PermissionChangeQuery permissionChangeQuery) {
    if (permissionChangeQuery.targetsUser()) {
      applyUserPermissionChange(operation, permissionChangeQuery);
    } else {
      applyGroupPermissionChange(operation, permissionChangeQuery);
    }
  }

  private void applyGroupPermissionChange(String operation, PermissionChangeQuery permissionChangeQuery) {
    List<String> existingPermissions = roleDao.selectGroupPermissions(permissionChangeQuery.getGroup());
    if (shouldSkipPermissionChange(operation, existingPermissions, permissionChangeQuery.getRole())) {
      LOG.info("Skipping permission change '{} {}' for group {} as it matches the current permission scheme",
          new String[] {operation, permissionChangeQuery.getRole(), permissionChangeQuery.getGroup()});
    } else {
      Long targetedGroup = getTargetedGroup(permissionChangeQuery.getGroup());
      GroupRoleDto groupRole = new GroupRoleDto().setRole(permissionChangeQuery.getRole()).setGroupId(targetedGroup);
      if (ADD.equals(operation)) {
        roleDao.insertGroupRole(groupRole);
      } else {
        roleDao.deleteGroupRole(groupRole);
      }
    }
  }

  private void applyUserPermissionChange(String operation, PermissionChangeQuery permissionChangeQuery) {
    List<String> existingPermissions = roleDao.selectUserPermissions(permissionChangeQuery.getUser());
    if (shouldSkipPermissionChange(operation, existingPermissions, permissionChangeQuery.getRole())) {
      LOG.info("Skipping permission change '{} {}' for user {} as it matches the current permission scheme",
          new String[] {operation, permissionChangeQuery.getRole(), permissionChangeQuery.getUser()});
    } else {
      Long targetedUser = getTargetedUser(permissionChangeQuery.getUser());
      UserRoleDto userRole = new UserRoleDto().setRole(permissionChangeQuery.getRole()).setUserId(targetedUser);
      if (ADD.equals(operation)) {
        roleDao.insertUserRole(userRole);
      } else {
        roleDao.deleteUserRole(userRole);
      }
    }
  }

  private Long getTargetedUser(String userLogin) {
    UserDto user = userDao.selectActiveUserByLogin(userLogin);
    if(user == null) {
      throw new BadRequestException("User " + userLogin + " does not exist");
    }
    return user.getId();
  }

  private Long getTargetedGroup(String group) {
    if (DefaultGroups.isAnyone(group)) {
      return null;
    } else {
      GroupDto groupDto = userDao.selectGroupByName(group);
      if(groupDto == null) {
        throw new BadRequestException("Group " + group + " does not exist");
      }
      return groupDto.getId();
    }
  }

  private boolean shouldSkipPermissionChange(String operation, List<String> existingPermissions, String role) {
    return (ADD.equals(operation) && existingPermissions.contains(role)) ||
      (REMOVE.equals(operation) && !existingPermissions.contains(role));
  }

  private void checkProjectAdminPermission(@Nullable Long componentId) {
    if (componentId == null) {
      UserSession.get().checkGlobalPermission(Permission.SYSTEM_ADMIN);
    } else {
      if (!UserSession.get().hasGlobalPermission(Permission.SYSTEM_ADMIN) && !UserSession.get().hasProjectPermission(UserRole.ADMIN, componentId)) {
        throw new ForbiddenException("Insufficient privileges");
      }
    }
  }
}
