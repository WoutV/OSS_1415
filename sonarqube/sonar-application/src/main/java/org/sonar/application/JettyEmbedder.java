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
package org.sonar.application;

import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ShutdownHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class JettyEmbedder {

  private final Server server;
  private final String host;
  private final int port;
  private final String contextPath;
  private final Properties configuration;

  public JettyEmbedder(String host, int port, String contextPath, Properties configuration) throws Exception {
    this.host = host.trim();
    this.port = port;
    this.contextPath = contextPath;
    this.configuration = configuration;
    server = new Server();
    configureProgrammatically();
  }

  /**
   * for tests
   */
  JettyEmbedder(String host, int port) throws Exception {
    this(host, port, null, new Properties());
  }

  public void start() throws Exception {
    server.start();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        try {
          server.stop();
        } catch (Exception e) {
          System.err.println("Can not stop the Jetty server");
          e.printStackTrace();
        }
      }
    });
  }

  private Server configureProgrammatically() throws URISyntaxException {
    configureServer();

    List<Handler> handlers = new ArrayList<Handler>();

    String shutdownCookie = System.getProperty("sonar.shutdownToken");
    if (shutdownCookie != null && !"".equals(shutdownCookie)) {
      System.out.println("Registering shutdown handler");
      ShutdownHandler shutdownHandler = new ShutdownHandler(server, shutdownCookie);
      shutdownHandler.setExitJvm(true);
      handlers.add(shutdownHandler);
    }

    WebAppContext context = new WebAppContext(getPath("/war/sonar-server"), contextPath);
    // Set up the path to the custom webdefault.xml (SONAR-3962).
    context.setDefaultsDescriptor("/org/sonar/application/webdefault.xml");
    handlers.add(context);

    String filenamePattern = configuration.getProperty("sonar.web.jettyRequestLogs");
    if (filenamePattern != null) {
      handlers.add(configureRequestLogHandler(filenamePattern));
    }

    HandlerCollection handler = new HandlerCollection();
    handler.setHandlers(handlers.toArray(new Handler[handlers.size()]));
    server.setHandler(handler);

    return server;
  }

  private RequestLogHandler configureRequestLogHandler(String filenamePattern) {
    RequestLogHandler requestLogHandler = new RequestLogHandler();
    NCSARequestLog requestLog = new NCSARequestLog(filenamePattern);
    requestLog.setRetainDays(7);
    requestLog.setAppend(true);
    requestLog.setExtended(true);
    requestLog.setLogTimeZone("GMT");
    requestLogHandler.setRequestLog(requestLog);
    return requestLogHandler;
  }

  private void configureServer() {
    QueuedThreadPool threadPool = new QueuedThreadPool();
    threadPool.setMinThreads(getIntProperty("sonar.web.jetty.threads.min", 5));
    threadPool.setMaxThreads(getIntProperty("sonar.web.jetty.threads.max", 50));
    server.setThreadPool(threadPool);
    SelectChannelConnector connector = new SelectChannelConnector();
    connector.setHost(host);
    connector.setPort(port);
    connector.setStatsOn(false);
    connector.setAcceptors(2);
    connector.setConfidentialPort(8443);
    server.addConnector(connector);
    server.setStopAtShutdown(true);
    server.setSendServerVersion(false);
    server.setSendDateHeader(true);
    server.setGracefulShutdown(1000);
  }

  private int getIntProperty(String name, int defaultValue) {
    String value = configuration.getProperty(name);
    if (null == value) {
      return defaultValue;
    }

    return Integer.parseInt(value);
  }

  final String getPluginsClasspath(String pluginsPathFromClassloader) throws URISyntaxException, IOException {
    URL resource = getClass().getResource(pluginsPathFromClassloader);
    if (resource == null) {
      return null;
    }

    List<String> paths = new ArrayList<String>();

    File pluginsDir = new File(resource.toURI());
    paths.add(pluginsDir.getCanonicalPath() + System.getProperty("file.separator"));

    Collection<File> files = FileUtils.listFiles(pluginsDir, new String[] {"jar"}, false);
    for (File file : files) {
      paths.add(file.getCanonicalPath());
    }

    return join(paths, ",");
  }

  private String join(List<String> paths, String separator) {
    StringBuilder sb = new StringBuilder();
    for (String path : paths) {
      if (sb.length() > 0) {
        sb.append(separator);
      }
      sb.append(path);
    }
    return sb.toString();
  }

  private String getPath(String resourcePath) throws URISyntaxException {
    URL resource = getClass().getResource(resourcePath);
    if (resource != null) {
      return resource.toURI().toString();
    }
    return null;
  }

  Server getServer() {
    return server;
  }

  @Override
  public String toString() {
    return new StringBuilder().append("http://").append(host).append(":").append(port).append(contextPath).toString();
  }
}
