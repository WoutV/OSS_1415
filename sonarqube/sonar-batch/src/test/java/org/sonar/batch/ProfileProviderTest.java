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
package org.sonar.batch;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sonar.api.config.Settings;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Languages;
import org.sonar.api.resources.Project;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ProfileProviderTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private Project javaProject;

  @Before
  public void setUp() {
    javaProject = newProject("java");
  }

  @Test
  public void shouldProvideProfile() {
    ProfileProvider provider = new ProfileProvider();
    ProfileLoader loader = mock(ProfileLoader.class);
    RulesProfile profile = RulesProfile.create();
    when(loader.load(eq(javaProject), any(Settings.class))).thenReturn(profile);

    assertThat(provider.provide(javaProject, loader, new Settings(), mock(Languages.class)), is(profile));
    assertThat(provider.provide(javaProject, loader, new Settings(), mock(Languages.class)), is(profile));
    verify(loader).load(eq(javaProject), any(Settings.class));
    verifyNoMoreInteractions(loader);
  }

  private Project newProject(String language) {
    PropertiesConfiguration configuration = new PropertiesConfiguration();
    configuration.setProperty("sonar.language", language);
    return new Project("project").setConfiguration(configuration);
  }
}
