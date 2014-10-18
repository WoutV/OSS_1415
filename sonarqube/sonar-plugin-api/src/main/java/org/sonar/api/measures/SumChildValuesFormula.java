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
package org.sonar.api.measures;

import java.util.Collections;
import java.util.List;

/**
 * @since 1.11
 */
public class SumChildValuesFormula implements Formula {

  private boolean saveZeroIfNoChildValues;

  public SumChildValuesFormula(boolean saveZeroIfNoChildValues) {
    this.saveZeroIfNoChildValues = saveZeroIfNoChildValues;
  }

  public List<Metric> dependsUponMetrics() {
    return Collections.emptyList();
  }

  public Measure calculate(FormulaData data, FormulaContext context) {
    Double sum = MeasureUtils.sum(saveZeroIfNoChildValues, data.getChildrenMeasures(context.getTargetMetric()));
    if (sum != null) {
      return new Measure(context.getTargetMetric(), sum);
    }
    return null;
  }
}
