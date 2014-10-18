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
package org.sonar.batch.bootstrap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.sonar.api.BatchExtension;
import org.sonar.api.Plugin;
import org.sonar.api.SonarPlugin;
import org.sonar.api.platform.PluginMetadata;
import org.sonar.core.config.Logback;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class BootstrapContainerTest {
  @Test
  public void should_add_components() {
    BootstrapContainer container = BootstrapContainer.create(Collections.emptyList());
    container.doBeforeStart();

    assertThat(container.getComponentByType(Logback.class)).isNotNull();
    assertThat(container.getComponentByType(TempDirectories.class)).isNotNull();
  }

  @Test
  public void should_add_bootstrap_extensions() {
    BootstrapContainer container = BootstrapContainer.create(Lists.newArrayList(Foo.class, new Bar()));
    container.doBeforeStart();

    assertThat(container.getComponentByType(Foo.class)).isNotNull();
    assertThat(container.getComponentByType(Bar.class)).isNotNull();
  }

  @Test
  public void should_install_plugins() {
    PluginMetadata metadata = mock(PluginMetadata.class);
    FakePlugin plugin = new FakePlugin();
    BatchPluginRepository pluginRepository = mock(BatchPluginRepository.class);
    when(pluginRepository.getPluginsByMetadata()).thenReturn(ImmutableMap.<PluginMetadata, Plugin> of(
        metadata, plugin
        ));

    BootstrapContainer container = spy(BootstrapContainer.create(Lists.<Object> newArrayList(pluginRepository)));
    doNothing().when(container).executeTask();
    container.doAfterStart();

    assertThat(container.getComponentsByType(Plugin.class)).containsOnly(plugin);
  }

  public static class Foo implements BatchExtension {

  }

  public static class Bar implements BatchExtension {

  }

  public static class FakePlugin extends SonarPlugin {

    public List getExtensions() {
      return Arrays.asList(Foo.class, Bar.class);
    }
  }
}
