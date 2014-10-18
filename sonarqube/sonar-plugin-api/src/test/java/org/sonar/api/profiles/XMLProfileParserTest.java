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
package org.sonar.api.profiles;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.MetricFinder;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.utils.ValidationMessages;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class XMLProfileParserTest {

  @Test
  public void importProfile() {
    ValidationMessages validation = ValidationMessages.create();
    RulesProfile profile = parse("importProfile.xml", validation);

    assertThat(profile.getLanguage()).isEqualTo("java");
    assertThat(profile.getName()).isEqualTo("sonar way");
    assertThat(validation.hasErrors()).isFalse();
    assertThat(profile).isNotNull();

    assertThat(profile.getActiveRule("checkstyle", "IllegalRegexp").getSeverity()).isEqualTo(RulePriority.CRITICAL);
  }

  @Test
  public void nameAndLanguageShouldBeMandatory() {
    ValidationMessages validation = ValidationMessages.create();
    parse("nameAndLanguageShouldBeMandatory.xml", validation);

    assertThat(validation.getErrors()).hasSize(2);
    assertThat(validation.getErrors().get(0)).contains("");
  }

  @Test
  public void importProfileWithRuleParameters() {
    ValidationMessages validation = ValidationMessages.create();
    RulesProfile profile = parse("importProfileWithRuleParameters.xml", validation);

    assertThat(validation.hasErrors()).isFalse();
    assertThat(validation.hasWarnings()).isFalse();

    ActiveRule rule = profile.getActiveRule("checkstyle", "IllegalRegexp");
    assertThat(rule.getParameter("format")).isEqualTo("foo");
    assertThat(rule.getParameter("message")).isEqualTo("with special characters < > &");
  }

  @Test
  public void importProfileWithUnknownRuleParameter() {
    ValidationMessages validation = ValidationMessages.create();
    RulesProfile profile = parse("importProfileWithUnknownRuleParameter.xml", validation);

    assertThat(validation.getWarnings()).hasSize(1);
    ActiveRule rule = profile.getActiveRule("checkstyle", "IllegalRegexp");
    assertThat(rule.getParameter("unknown")).isNull();
  }

  @Test
  public void importProfileWithAlerts() {
    ValidationMessages validation = ValidationMessages.create();
    RulesProfile profile = parse("importProfileWithAlerts.xml", validation);

    assertThat(profile.getAlerts()).hasSize(2);

    Alert alert = profile.getAlerts().get(0);
    assertThat(alert.getMetric().getKey()).isEqualTo("lines");
    assertThat(alert.getOperator()).isEqualTo(Alert.OPERATOR_SMALLER);
    assertThat(alert.getValueWarning()).isEqualTo("0");
    assertThat(alert.getValueError()).isEqualTo("10");
    assertThat(alert.getPeriod()).isNull();

    alert = profile.getAlerts().get(1);
    assertThat(alert.getMetric().getKey()).isEqualTo("complexity");
    assertThat(alert.getOperator()).isEqualTo(Alert.OPERATOR_GREATER);
    assertThat(alert.getValueWarning()).isEqualTo("10");
    assertThat(alert.getValueError()).isEqualTo("12");
    assertThat(alert.getPeriod()).isEqualTo(1);
  }

  @Test
  public void shouldNotFailWhenNoMetricFinder() {
    ValidationMessages validation = ValidationMessages.create();
    RulesProfile profile = new XMLProfileParser(newRuleFinder(), null)
        .parseResource(getClass().getClassLoader(), getResourcePath("importProfileWithAlerts.xml"), validation);

    assertThat(profile.getAlerts()).isEmpty();
  }

  private RulesProfile parse(String resource, ValidationMessages validation) {
    return new XMLProfileParser(newRuleFinder(), newMetricFinder())
        .parseResource(getClass().getClassLoader(), getResourcePath(resource), validation);
  }

  private String getResourcePath(String resource) {
    return "org/sonar/api/profiles/XMLProfileParserTest/" + resource;
  }

  private MetricFinder newMetricFinder() {
    MetricFinder metricFinder = mock(MetricFinder.class);
    when(metricFinder.findByKey(anyString())).thenAnswer(new Answer<Metric>() {
      public Metric answer(InvocationOnMock iom) throws Throwable {
        return new Metric((String) iom.getArguments()[0]);
      }
    });
    return metricFinder;
  }

  private RuleFinder newRuleFinder() {
    RuleFinder ruleFinder = mock(RuleFinder.class);
    when(ruleFinder.findByKey(anyString(), anyString())).thenAnswer(new Answer<Rule>() {
      public Rule answer(InvocationOnMock iom) throws Throwable {
        Rule rule = Rule.create((String) iom.getArguments()[0], (String) iom.getArguments()[1], (String) iom.getArguments()[1]);
        rule.createParameter("format");
        rule.createParameter("message");
        return rule;
      }
    });
    return ruleFinder;
  }
}
