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
package org.sonar.server.plugins;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.picocontainer.Startable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.ServerComponent;
import org.sonar.api.utils.HttpDownloader;
import org.sonar.api.utils.SonarException;
import org.sonar.server.platform.DefaultServerFileSystem;
import org.sonar.updatecenter.common.Release;
import org.sonar.updatecenter.common.Version;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PluginDownloader implements ServerComponent, Startable {

  private static final Logger LOG = LoggerFactory.getLogger(PluginDownloader.class);
  private static final String TMP_SUFFIX = "tmp";

  private final UpdateCenterMatrixFactory updateCenterMatrixFactory;
  private final HttpDownloader downloader;
  private final File downloadDir;

  public PluginDownloader(UpdateCenterMatrixFactory updateCenterMatrixFactory, HttpDownloader downloader, DefaultServerFileSystem fileSystem) {
    this.updateCenterMatrixFactory = updateCenterMatrixFactory;
    this.downloader = downloader;
    this.downloadDir = fileSystem.getDownloadedPluginsDir();
  }

  /**
   * Delete the temporary files remaining from previous downloads
   * @see #downloadRelease(org.sonar.updatecenter.common.Release)
   */
  @Override
  public void start() {
    try {
      FileUtils.forceMkdir(downloadDir);
      Collection<File> tempFiles = FileUtils.listFiles(downloadDir, new String[]{TMP_SUFFIX}, false);
      for (File tempFile : tempFiles) {
        FileUtils.deleteQuietly(tempFile);
      }

    } catch (IOException e) {
      throw new IllegalStateException("Fail to create the directory: " + downloadDir, e);
    }
  }

  @Override
  public void stop() {
  }

  public void cancelDownloads() {
    try {
      if (downloadDir.exists()) {
        FileUtils.cleanDirectory(downloadDir);
      }
    } catch (IOException e) {
      throw new IllegalStateException("Fail to clean the plugin downloads directory: " + downloadDir, e);
    }
  }

  public boolean hasDownloads() {
    return !getDownloads().isEmpty();
  }

  public List<String> getDownloads() {
    List<String> names = new ArrayList<String>();
    List<File> files = (List<File>) FileUtils.listFiles(downloadDir, new String[]{"jar"}, false);
    for (File file : files) {
      names.add(file.getName());
    }
    return names;
  }

  public void download(String pluginKey, Version version) {
    for (Release release : updateCenterMatrixFactory.getUpdateCenter(true).findInstallablePlugins(pluginKey, version)) {
      try {
        downloadRelease(release);

      } catch (Exception e) {
        String message = "Fail to download the plugin (" + release.getArtifact().getKey() + ", version " + release.getVersion().getName() + ") from " + release.getDownloadUrl();
        LOG.warn(message, e);
        throw new SonarException(message, e);
      }
    }
  }

  private void downloadRelease(Release release) throws URISyntaxException, IOException {
    String url = release.getDownloadUrl();
    URI uri = new URI(url);
    if (url.startsWith("file:")) {
      // used for tests
      File file = FileUtils.toFile(uri.toURL());
      FileUtils.copyFileToDirectory(file, downloadDir);
    } else {
      String filename = StringUtils.substringAfterLast(uri.getPath(), "/");
      File targetFile = new File(downloadDir, filename);
      File tempFile = new File(downloadDir, filename + "." + TMP_SUFFIX);
      downloader.download(uri, tempFile);
      FileUtils.moveFile(tempFile, targetFile);
    }
  }
}
