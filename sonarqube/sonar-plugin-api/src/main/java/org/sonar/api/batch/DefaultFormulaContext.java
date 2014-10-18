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
package org.sonar.api.batch;

import org.apache.commons.configuration.Configuration;
import org.sonar.api.measures.FormulaContext;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Resource;

/**
 * @since 1.11
 */
public class DefaultFormulaContext implements FormulaContext {

  private Metric metric;
  private DecoratorContext decoratorContext;

  public DefaultFormulaContext(Metric metric) {
    this.metric = metric;
  }

  public Metric getTargetMetric() {
    return metric;
  }

  public Resource getResource() {
    return decoratorContext.getResource();
  }

  /**
   * @deprecated in 3.7. Use {@link org.sonar.api.config.Settings}.
   */
  @Deprecated
  public Configuration getConfiguration() {
    return decoratorContext.getProject().getConfiguration();
  }

  public void setMetric(Metric metric) {
    this.metric = metric;
  }

  public void setDecoratorContext(DecoratorContext decoratorContext) {
    this.decoratorContext = decoratorContext;
  }
}
