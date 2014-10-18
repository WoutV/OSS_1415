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

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.server.platform.Platform;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StaticResourcesServlet extends HttpServlet {

  private static final Logger LOG = LoggerFactory.getLogger(StaticResourcesServlet.class);
  private static final long serialVersionUID = -2577454614650178426L;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String pluginKey = getPluginKey(request);
    String resource = getResourcePath(request);

    DefaultServerPluginRepository pluginRepository = Platform.getInstance().getContainer().getComponentByType(DefaultServerPluginRepository.class);
    ClassLoader classLoader = pluginRepository.getClassLoader(pluginKey);
    if (classLoader == null) {
      LOG.error("Plugin not found: " + pluginKey);
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    InputStream in = null;
    OutputStream out = null;
    try {
      in = classLoader.getResourceAsStream(resource);
      if (in != null) {
        out = response.getOutputStream();
        IOUtils.copy(in, out);
        completeContentType(response, resource);

      } else {
        LOG.error("Unable to find resource '" + resource + "' in plugin '" + pluginKey + "'");
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
    } catch (Exception e) {
      LOG.error("Unable to load static resource '" + resource + "' from plugin '" + pluginKey + "'", e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    } finally {
      IOUtils.closeQuietly(in);
      IOUtils.closeQuietly(out);
    }
  }

  /**
   * @return part of request URL after servlet path
   */
  protected String getPluginKeyAndResourcePath(HttpServletRequest request) {
    return StringUtils.substringAfter(request.getRequestURI(), request.getContextPath() + request.getServletPath() + "/");
  }

  protected String getPluginKey(HttpServletRequest request) {
    return StringUtils.substringBefore(getPluginKeyAndResourcePath(request), "/");
  }

  /**
   * Note that returned value should not have a leading "/" - see {@link Class#resolveName(String)}.
   */
  protected String getResourcePath(HttpServletRequest request) {
    return "static/" + StringUtils.substringAfter(getPluginKeyAndResourcePath(request), "/");
  }

  @VisibleForTesting
  void completeContentType(HttpServletResponse response, String filename) {
    response.setContentType(MimeTypes.getByFilename(filename));
  }
}
