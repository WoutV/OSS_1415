/*
 * SonarQube Runner - API
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
package org.sonar.runner.api;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentMatcher;
import org.sonar.runner.impl.BatchLauncher;
import org.sonar.runner.impl.InternalProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EmbeddedRunnerTest {

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();

  @Test
  public void should_create() {
    assertThat(EmbeddedRunner.create()).isNotNull().isInstanceOf(EmbeddedRunner.class);
  }

  @Test
  public void test_app() {
    EmbeddedRunner runner = EmbeddedRunner.create().setApp("Eclipse", "3.1");
    assertThat(runner.app()).isEqualTo("Eclipse");
    assertThat(runner.appVersion()).isEqualTo("3.1");
  }

  @Test
  public void should_set_unmasked_packages() {
    EmbeddedRunner runner = EmbeddedRunner.create();
    assertThat(runner.property(InternalProperties.RUNNER_MASK_RULES, null)).isNull();

    runner = EmbeddedRunner.create().setUnmaskedPackages("org.apache.ant", "org.ant");
    assertThat(runner.property(InternalProperties.RUNNER_MASK_RULES, null)).isEqualTo("UNMASK|org.apache.ant.,UNMASK|org.ant.");
  }

  @Test
  public void should_set_mask_rules() {
    EmbeddedRunner runner = EmbeddedRunner.create();
    assertThat(runner.property(InternalProperties.RUNNER_MASK_RULES, null)).isNull();

    runner = EmbeddedRunner.create()
      .unmask("org.slf4j.Logger")
      .mask("org.slf4j.")
      .mask("ch.qos.logback.")
      .unmask("");
    assertThat(runner.property(InternalProperties.RUNNER_MASK_RULES, null)).isEqualTo("UNMASK|org.slf4j.Logger,MASK|org.slf4j.,MASK|ch.qos.logback.,UNMASK|");
  }

  @Test
  public void should_add_extensions() {
    EmbeddedRunner runner = EmbeddedRunner.create();
    assertThat(runner.extensions()).isEmpty();

    FakeExtension fakeExtension = new FakeExtension();
    runner.addExtensions(fakeExtension);
    assertThat(runner.extensions()).containsExactly(fakeExtension);
  }

  @Test
  public void should_set_properties() {
    EmbeddedRunner runner = EmbeddedRunner.create();
    runner.setProperty("sonar.projectKey", "foo");
    runner.addProperties(new Properties() {
      {
        setProperty("sonar.login", "admin");
        setProperty("sonar.password", "gniark");
      }
    });

    assertThat(runner.property("sonar.projectKey", null)).isEqualTo("foo");
    assertThat(runner.property("sonar.login", null)).isEqualTo("admin");
    assertThat(runner.property("sonar.password", null)).isEqualTo("gniark");
    assertThat(runner.property("not.set", "this_is_default")).isEqualTo("this_is_default");
  }

  @Test
  public void should_launch_batch() {
    BatchLauncher batchLauncher = mock(BatchLauncher.class);
    EmbeddedRunner runner = new EmbeddedRunner(batchLauncher);
    final FakeExtension fakeExtension = new FakeExtension();
    runner.addExtensions(fakeExtension);
    runner.setProperty("sonar.projectKey", "foo");
    runner.execute();

    verify(batchLauncher).execute(argThat(new ArgumentMatcher<Properties>() {
      @Override
      public boolean matches(Object o) {
        return "foo".equals(((Properties) o).getProperty("sonar.projectKey"));
      }
    }), argThat(new ArgumentMatcher<List<Object>>() {
      @Override
      public boolean matches(Object o) {
        return ((List) o).contains(fakeExtension);
      }
    }));
  }

  @Test
  public void should_launch_in_simulation_mode() throws IOException {
    File dump = temp.newFile();

    BatchLauncher batchLauncher = mock(BatchLauncher.class);
    EmbeddedRunner runner = new EmbeddedRunner(batchLauncher);
    runner.setProperty("sonar.projectKey", "foo");
    runner.setProperty("sonarRunner.dumpToFile", dump.getAbsolutePath());
    runner.execute();

    Properties props = new Properties();
    props.load(new FileInputStream(dump));

    assertThat(props.getProperty("sonar.projectKey")).isEqualTo("foo");
  }

  static class FakeExtension {
  }
}
