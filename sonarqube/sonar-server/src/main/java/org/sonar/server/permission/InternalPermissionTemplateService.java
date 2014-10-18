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

import com.google.common.collect.Lists;
import org.h2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.ServerComponent;
import org.sonar.core.permission.PermissionDao;
import org.sonar.core.permission.PermissionTemplateDto;
import org.sonar.core.resource.ResourceDao;
import org.sonar.core.resource.ResourceDto;
import org.sonar.core.resource.ResourceQuery;
import org.sonar.core.user.UserDao;
import org.sonar.server.exceptions.BadRequestException;
import org.sonar.server.exceptions.ServerErrorException;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import java.util.List;

/**
 * Used by ruby code <pre>Internal.permission_templates</pre>
 */
public class InternalPermissionTemplateService implements ServerComponent {

  private static final Logger LOG = LoggerFactory.getLogger(InternalPermissionTemplateService.class);

  private final PermissionDao permissionDao;
  private final UserDao userDao;
  private final ResourceDao resourceDao;

  public InternalPermissionTemplateService(PermissionDao permissionDao, UserDao userDao, ResourceDao resourceDao) {
    this.permissionDao = permissionDao;
    this.userDao = userDao;
    this.resourceDao = resourceDao;
  }

  @CheckForNull
  public PermissionTemplate selectPermissionTemplate(String templateName) {
    PermissionTemplateUpdater.checkSystemAdminUser();
    PermissionTemplateDto permissionTemplateDto = permissionDao.selectPermissionTemplate(templateName);
    return PermissionTemplate.create(permissionTemplateDto);
  }

  public List<PermissionTemplate> selectAllPermissionTemplates() {
    return selectAllPermissionTemplates(null);
  }

  public List<PermissionTemplate> selectAllPermissionTemplates(@Nullable String componentKey) {
    PermissionTemplateUpdater.checkProjectAdminUser(getComponentId(componentKey));
    List<PermissionTemplate> permissionTemplates = Lists.newArrayList();
    List<PermissionTemplateDto> permissionTemplateDtos = permissionDao.selectAllPermissionTemplates();
    if (permissionTemplateDtos != null) {
      for (PermissionTemplateDto permissionTemplateDto : permissionTemplateDtos) {
        permissionTemplates.add(PermissionTemplate.create(permissionTemplateDto));
      }
    }
    return permissionTemplates;
  }

  public PermissionTemplate createPermissionTemplate(String name, @Nullable String description) {
    PermissionTemplateUpdater.checkSystemAdminUser();
    validateTemplateName(null, name);
    PermissionTemplateDto permissionTemplateDto = permissionDao.createPermissionTemplate(name, description);
    if (permissionTemplateDto.getId() == null) {
      String errorMsg = "Template creation failed";
      LOG.error(errorMsg);
      throw new ServerErrorException(errorMsg);
    }
    return PermissionTemplate.create(permissionTemplateDto);
  }

  public void updatePermissionTemplate(Long templateId, String newName, @Nullable String newDescription) {
    PermissionTemplateUpdater.checkSystemAdminUser();
    validateTemplateName(templateId, newName);
    permissionDao.updatePermissionTemplate(templateId, newName, newDescription);
  }

  public void deletePermissionTemplate(Long templateId) {
    PermissionTemplateUpdater.checkSystemAdminUser();
    permissionDao.deletePermissionTemplate(templateId);
  }

  public void addUserPermission(String templateName, String permission, String userLogin) {
    PermissionTemplateUpdater updater = new PermissionTemplateUpdater(templateName, permission, userLogin, permissionDao, userDao) {
      @Override
      protected void doExecute(Long templateId, String permission) {
        Long userId = getUserId();
        permissionDao.addUserPermission(templateId, userId, permission);
      }
    };
    updater.executeUpdate();
  }

  public void removeUserPermission(String templateName, String permission, String userLogin) {
    PermissionTemplateUpdater updater = new PermissionTemplateUpdater(templateName, permission, userLogin, permissionDao, userDao) {
      @Override
      protected void doExecute(Long templateId, String permission) {
        Long userId = getUserId();
        permissionDao.removeUserPermission(templateId, userId, permission);
      }
    };
    updater.executeUpdate();
  }

  public void addGroupPermission(String templateName, String permission, String groupName) {
    PermissionTemplateUpdater updater = new PermissionTemplateUpdater(templateName, permission, groupName, permissionDao, userDao) {
      @Override
      protected void doExecute(Long templateId, String permission) {
        Long groupId = getGroupId();
        permissionDao.addGroupPermission(templateId, groupId, permission);
      }
    };
    updater.executeUpdate();
  }

  public void removeGroupPermission(String templateName, String permission, String groupName) {
    PermissionTemplateUpdater updater = new PermissionTemplateUpdater(templateName, permission, groupName, permissionDao, userDao) {
      @Override
      protected void doExecute(Long templateId, String permission) {
        Long groupId = getGroupId();
        permissionDao.removeGroupPermission(templateId, groupId, permission);
      }
    };
    updater.executeUpdate();
  }

  private void validateTemplateName(Long templateId, String templateName) {
    if (StringUtils.isNullOrEmpty(templateName)) {
      String errorMsg = "Name can't be blank";
      throw new BadRequestException(errorMsg);
    }
    List<PermissionTemplateDto> existingTemplates = permissionDao.selectAllPermissionTemplates();
    if (existingTemplates != null) {
      for (PermissionTemplateDto existingTemplate : existingTemplates) {
        if ((templateId == null || !existingTemplate.getId().equals(templateId)) && (existingTemplate.getName().equals(templateName))) {
          String errorMsg = "A template with that name already exists";
          throw new BadRequestException(errorMsg);
        }
      }
    }
  }

  @Nullable
  private Long getComponentId(String componentKey) {
    if (componentKey == null) {
      return null;
    } else {
      ResourceDto resourceDto = resourceDao.getResource(ResourceQuery.create().setKey(componentKey));
      if (resourceDto == null) {
        throw new BadRequestException("Project does not exists.");
      }
      return resourceDto.getId();
    }
  }
}
