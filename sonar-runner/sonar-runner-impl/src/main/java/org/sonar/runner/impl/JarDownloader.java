/*
 * SonarQube Runner - Implementation
 * Copyright (C) 2011 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.runner.impl;

import java.io.File;
import java.util.List;

class JarDownloader {
  private final ServerConnection serverConnection;
  private final ServerVersion serverVersion;

  JarDownloader(ServerConnection conn, ServerVersion version) {
    this.serverConnection = conn;
    this.serverVersion = version;
  }

  List<File> checkVersionAndDownload() {
    List<File> jarFiles;
    if (serverVersion.is37Compatible()) {
      jarFiles = download();
    } else {
      throw new IllegalStateException("SonarQube " + serverVersion.version()
        + " is not supported. Please upgrade SonarQube to version 3.7 or more.");
    }
    return jarFiles;
  }

  List<File> download() {
    return new Jars(serverConnection, new JarExtractor()).download();
  }
}
