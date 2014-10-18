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
package org.sonar.server.ui;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.CoreProperties;
import org.sonar.api.config.Settings;
import org.sonar.api.security.LoginPasswordAuthenticator;
import org.sonar.api.security.SecurityRealm;
import org.sonar.api.utils.SonarException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SecurityRealmFactoryTest {

  private Settings settings;

  @Before
  public void setUp() {
    settings = new Settings();
  }

  /**
   * Typical usage.
   */
  @Test
  public void should_select_realm_and_start() {
    SecurityRealm realm = spy(new FakeRealm());
    settings.setProperty(CoreProperties.CORE_AUTHENTICATOR_REALM, realm.getName());

    SecurityRealmFactory factory = new SecurityRealmFactory(settings, new SecurityRealm[]{realm});
    factory.start();
    assertThat(factory.getRealm(), is(realm));
    verify(realm).init();
  }

  @Test
  public void do_not_fail_if_no_realms() {
    SecurityRealmFactory factory = new SecurityRealmFactory(settings);
    factory.start();
    assertThat(factory.getRealm(), nullValue());
  }

  @Test
  public void realm_not_found() {
    settings.setProperty(CoreProperties.CORE_AUTHENTICATOR_REALM, "Fake");

    try {
      new SecurityRealmFactory(settings);
      fail();
    } catch (SonarException e) {
      assertThat(e.getMessage(), containsString("Realm 'Fake' not found."));
    }
  }

  @Test
  public void should_provide_compatibility_for_authenticator() {
    settings.setProperty(CoreProperties.CORE_AUTHENTICATOR_CLASS, FakeAuthenticator.class.getName());
    LoginPasswordAuthenticator authenticator = new FakeAuthenticator();

    SecurityRealmFactory factory = new SecurityRealmFactory(settings, new LoginPasswordAuthenticator[]{authenticator});
    SecurityRealm realm = factory.getRealm();
    assertThat(realm, instanceOf(CompatibilityRealm.class));
  }

  @Test
  public void should_take_precedence_over_authenticator() {
    SecurityRealm realm = new FakeRealm();
    settings.setProperty(CoreProperties.CORE_AUTHENTICATOR_REALM, realm.getName());
    LoginPasswordAuthenticator authenticator = new FakeAuthenticator();
    settings.setProperty(CoreProperties.CORE_AUTHENTICATOR_CLASS, FakeAuthenticator.class.getName());

    SecurityRealmFactory factory = new SecurityRealmFactory(settings, new SecurityRealm[]{realm},
        new LoginPasswordAuthenticator[]{authenticator});
    assertThat(factory.getRealm(), is(realm));
  }

  @Test
  public void authenticator_not_found() {
    settings.setProperty(CoreProperties.CORE_AUTHENTICATOR_CLASS, "Fake");

    try {
      new SecurityRealmFactory(settings);
      fail();
    } catch (SonarException e) {
      assertThat(e.getMessage(), containsString("Authenticator 'Fake' not found."));
    }
  }

  @Test
  public void ignore_startup_failure() {
    SecurityRealm realm = spy(new AlwaysFailsRealm());
    settings.setProperty(CoreProperties.CORE_AUTHENTICATOR_REALM, realm.getName());
    settings.setProperty(CoreProperties.CORE_AUTHENTICATOR_IGNORE_STARTUP_FAILURE, true);

    new SecurityRealmFactory(settings, new SecurityRealm[]{realm}).start();
    verify(realm).init();
  }

  @Test
  public void should_fail() {
    SecurityRealm realm = spy(new AlwaysFailsRealm());
    settings.setProperty(CoreProperties.CORE_AUTHENTICATOR_REALM, realm.getName());

    try {
      new SecurityRealmFactory(settings, new SecurityRealm[]{realm}).start();
      fail();
    } catch (SonarException e) {
      assertThat(e.getCause(), instanceOf(IllegalStateException.class));
      assertThat(e.getMessage(), containsString("Security realm fails to start"));
    }
  }

  private static class AlwaysFailsRealm extends FakeRealm {
    @Override
    public void init() {
      throw new IllegalStateException();
    }
  }

  private static class FakeRealm extends SecurityRealm {
    @Override
    public LoginPasswordAuthenticator getLoginPasswordAuthenticator() {
      return null;
    }
  }

  private static class FakeAuthenticator implements LoginPasswordAuthenticator {
    public void init() {
    }

    public boolean authenticate(String login, String password) {
      return false;
    }
  }

}
