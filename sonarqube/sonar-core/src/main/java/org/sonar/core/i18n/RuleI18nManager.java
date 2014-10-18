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
package org.sonar.core.i18n;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.BatchExtension;
import org.sonar.api.ServerExtension;
import org.sonar.api.i18n.RuleI18n;
import org.sonar.api.rules.Rule;

import javax.annotation.CheckForNull;

import java.util.List;
import java.util.Locale;

public class RuleI18nManager implements RuleI18n, ServerExtension, BatchExtension {

  private static final String NAME_SUFFIX = ".name";
  private static final String RULE_PREFIX = "rule.";

  private I18nManager i18nManager;
  private RuleKey[] ruleKeys;

  public RuleI18nManager(I18nManager i18nManager) {
    this.i18nManager = i18nManager;
  }

  public void start() {
    List<RuleKey> list = Lists.newArrayList();
    for (String propertyKey : i18nManager.getPropertyKeys()) {
      if (isRuleProperty(propertyKey)) {
        list.add(extractRuleKey(propertyKey));
      }
    }
    this.ruleKeys = list.toArray(new RuleKey[list.size()]);
  }

  @CheckForNull
  public String getName(String repositoryKey, String ruleKey, Locale locale) {
    return message(repositoryKey, ruleKey, locale, NAME_SUFFIX);
  }

  @CheckForNull
  public String getName(Rule rule, Locale locale) {
    String name = message(rule.getRepositoryKey(), rule.getKey(), locale, NAME_SUFFIX);
    return name != null ? name : rule.getName();
  }

  public String getDescription(String repositoryKey, String ruleKey, Locale locale) {
    String relatedProperty = new StringBuilder().append(RULE_PREFIX).append(repositoryKey).append(".").append(ruleKey).append(NAME_SUFFIX).toString();

    Locale localeWithoutCountry = locale.getCountry() == null ? locale : new Locale(locale.getLanguage());
    String ruleDescriptionFilePath = "rules/" + repositoryKey + "/" + ruleKey + ".html";
    String description = i18nManager.messageFromFile(localeWithoutCountry, ruleDescriptionFilePath, relatedProperty, true);
    if (description == null) {
      // Following line is to ensure backward compatibility (SONAR-3319)
      description = lookUpDescriptionInFormerLocation(ruleKey, relatedProperty, localeWithoutCountry);
      if (description == null && !"en".equals(localeWithoutCountry.getLanguage())) {
        // nothing was found, let's get the value of the default bundle
        description = i18nManager.messageFromFile(Locale.ENGLISH, ruleDescriptionFilePath, relatedProperty, true);
      }
    }
    return description;
  }

  /*
   * Method used to ensure backward compatibility for language plugins that store HTML rule description files in the former
   * location (which was used prior to Sonar 3.0).
   * 
   * See http://jira.codehaus.org/browse/SONAR-3319
   */
  private String lookUpDescriptionInFormerLocation(String ruleKey, String relatedProperty, Locale localeWithoutCountry) {
    String description = i18nManager.messageFromFile(localeWithoutCountry, ruleKey + ".html", relatedProperty, true);
    if (description == null && !"en".equals(localeWithoutCountry.getLanguage())) {
      // nothing was found, let's get the value of the default bundle
      description = i18nManager.messageFromFile(Locale.ENGLISH, ruleKey + ".html", relatedProperty, true);
    }
    return description;
  }

  @CheckForNull
  public String getParamDescription(String repositoryKey, String ruleKey, String paramKey, Locale locale) {
    return message(repositoryKey, ruleKey, locale, ".param." + paramKey);
  }

  @CheckForNull
  String message(String repositoryKey, String ruleKey, Locale locale, String suffix) {
    String propertyKey = new StringBuilder().append(RULE_PREFIX).append(repositoryKey).append(".").append(ruleKey).append(suffix).toString();
    return i18nManager.message(locale, propertyKey, null);
  }

  public List<RuleKey> searchNames(String search, Locale locale) {
    List<RuleKey> result = Lists.newArrayList();
    for (RuleKey ruleKey : ruleKeys) {
      String name = i18nManager.message(locale, ruleKey.getNameProperty(), null);
      if (name != null && StringUtils.indexOfIgnoreCase(name, search) >= 0) {
        result.add(ruleKey);
      }
    }
    return result;
  }

  RuleKey[] getRuleKeys() {
    return ruleKeys;
  }

  static RuleKey extractRuleKey(String propertyKey) {
    String s = StringUtils.substringBetween(propertyKey, RULE_PREFIX, NAME_SUFFIX);
    String ruleKey = StringUtils.substringAfter(s, ".");
    String repository = StringUtils.substringBefore(s, ".");
    return new RuleKey(repository, ruleKey);
  }

  static boolean isRuleProperty(String propertyKey) {
    return StringUtils.startsWith(propertyKey, RULE_PREFIX) && StringUtils.endsWith(propertyKey, NAME_SUFFIX) && !propertyKey.contains(".param.");
  }

  public static class RuleKey {
    private String repositoryKey;
    private String key;

    RuleKey(String repositoryKey, String key) {
      this.repositoryKey = repositoryKey;
      this.key = key;
    }

    public String getRepositoryKey() {
      return repositoryKey;
    }

    public String getKey() {
      return key;
    }

    public String getNameProperty() {
      return new StringBuilder().append(RULE_PREFIX).append(repositoryKey).append(".").append(key).append(NAME_SUFFIX).toString();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      RuleKey ruleKey = (RuleKey) o;
      if (!key.equals(ruleKey.key)) {
        return false;
      }
      if (!repositoryKey.equals(ruleKey.repositoryKey)) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = repositoryKey.hashCode();
      result = 31 * result + key.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return new StringBuilder().append(repositoryKey).append(":").append(key).toString();
    }
  }
}
