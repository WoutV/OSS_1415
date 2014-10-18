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
package org.sonar.batch.scan.filesystem;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.sonar.api.scan.filesystem.FileSystemFilter;
import org.sonar.api.scan.filesystem.FileType;

import java.io.File;
import java.util.Set;

/**
 * @since 3.5
 */
class WhiteListFileFilter implements FileSystemFilter {
  private final FileType fileType;
  private final Set<File> files;

  WhiteListFileFilter(FileType fileType, Set<File> files) {
    Preconditions.checkNotNull(fileType);
    Preconditions.checkNotNull(files);
    this.fileType = fileType;
    this.files = files;
  }

  public boolean accept(File file, Context context) {
    return !context.type().equals(fileType) || files.contains(file);
  }

  @Override
  public String toString() {
    return StringUtils.capitalize(fileType.name().toLowerCase()) + " files: " + SystemUtils.LINE_SEPARATOR +
      Joiner.on(SystemUtils.LINE_SEPARATOR).join(files);
  }
}
