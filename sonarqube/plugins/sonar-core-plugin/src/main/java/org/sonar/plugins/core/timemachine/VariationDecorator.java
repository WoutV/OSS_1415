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
package org.sonar.plugins.core.timemachine;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.*;
import org.sonar.api.measures.*;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Scopes;
import org.sonar.batch.components.PastMeasuresLoader;
import org.sonar.batch.components.PastSnapshot;
import org.sonar.batch.components.TimeMachineConfiguration;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@DependedUpon(DecoratorBarriers.END_OF_TIME_MACHINE)
public class VariationDecorator implements Decorator {

  private List<PastSnapshot> projectPastSnapshots;
  private MetricFinder metricFinder;
  private PastMeasuresLoader pastMeasuresLoader;


  public VariationDecorator(PastMeasuresLoader pastMeasuresLoader, MetricFinder metricFinder, TimeMachineConfiguration configuration) {
    this(pastMeasuresLoader, metricFinder, configuration.getProjectPastSnapshots());
  }

  VariationDecorator(PastMeasuresLoader pastMeasuresLoader, MetricFinder metricFinder, List<PastSnapshot> projectPastSnapshots) {
    this.pastMeasuresLoader = pastMeasuresLoader;
    this.projectPastSnapshots = projectPastSnapshots;
    this.metricFinder = metricFinder;
  }

  public boolean shouldExecuteOnProject(Project project) {
    return true;
  }

  @DependsUpon
  public Collection<Metric> dependsUponMetrics() {
    return pastMeasuresLoader.getMetrics();
  }

  public void decorate(Resource resource, DecoratorContext context) {
    for (PastSnapshot projectPastSnapshot : projectPastSnapshots) {
      if (shouldComputeVariation(resource)) {
        computeVariation(resource, context, projectPastSnapshot);
      }
    }
  }

  boolean shouldComputeVariation(Resource resource) {
    if (Scopes.FILE.equals(resource.getScope()) && !Qualifiers.UNIT_TEST_FILE.equals(resource.getQualifier())) {
      return false;
    }

    // measures on files are currently purged, so past measures are not available on files
    return StringUtils.equals(Scopes.PROJECT, resource.getScope()) || StringUtils.equals(Scopes.DIRECTORY, resource.getScope());
  }

  private void computeVariation(Resource resource, DecoratorContext context, PastSnapshot pastSnapshot) {
    List<Object[]> pastMeasures = pastMeasuresLoader.getPastMeasures(resource, pastSnapshot);
    compareWithPastMeasures(context, pastSnapshot.getIndex(), pastMeasures);
  }

  void compareWithPastMeasures(DecoratorContext context, int index, List<Object[]> pastMeasures) {
    Map<MeasureKey, Object[]> pastMeasuresByKey = Maps.newHashMap();
    for (Object[] pastMeasure : pastMeasures) {
      pastMeasuresByKey.put(new MeasureKey(pastMeasure), pastMeasure);
    }

    // for each measure, search equivalent past measure
    for (Measure measure : context.getMeasures(MeasuresFilters.all())) {
      // compare with past measure
      Integer metricId = measure.getMetric().getId() != null ? measure.getMetric().getId() : metricFinder.findByKey(measure.getMetric().getKey()).getId();
      Integer characteristicId = measure.getCharacteristic() != null ? measure.getCharacteristic().getId() : null;
      Integer personId = measure.getPersonId();
      Integer ruleId = measure instanceof RuleMeasure ? ((RuleMeasure) measure).getRule().getId() : null;

      Object[] pastMeasure = pastMeasuresByKey.get(new MeasureKey(metricId, characteristicId, personId, ruleId));
      if (updateVariation(measure, pastMeasure, index)) {
        context.saveMeasure(measure);
      }
    }
  }

  boolean updateVariation(Measure measure, Object[] pastMeasure, int index) {
    if (pastMeasure != null && PastMeasuresLoader.hasValue(pastMeasure) && measure.getValue() != null) {
      double variation = measure.getValue() - PastMeasuresLoader.getValue(pastMeasure);
      measure.setVariation(index, variation);
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

  static final class MeasureKey {
    int metricId;
    Integer characteristicId;
    Integer personId;
    Integer ruleId;

    MeasureKey(Object[] pastFields) {
      metricId = PastMeasuresLoader.getMetricId(pastFields);
      characteristicId = PastMeasuresLoader.getCharacteristicId(pastFields);
      personId = PastMeasuresLoader.getPersonId(pastFields);
      ruleId = PastMeasuresLoader.getRuleId(pastFields);
    }

    MeasureKey(int metricId, Integer characteristicId, Integer personId, Integer ruleId) {
      this.metricId = metricId;
      this.characteristicId = characteristicId;
      this.personId = personId;
      this.ruleId = ruleId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      MeasureKey that = (MeasureKey) o;
      if (metricId != that.metricId) {
        return false;
      }
      if (characteristicId != null ? !characteristicId.equals(that.characteristicId) : that.characteristicId != null) {
        return false;
      }
      if (personId != null ? !personId.equals(that.personId) : that.personId != null) {
        return false;
      }
      if (ruleId != null ? !ruleId.equals(that.ruleId) : that.ruleId != null) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = metricId;
      result = 31 * result + (characteristicId != null ? characteristicId.hashCode() : 0);
      result = 31 * result + (personId != null ? personId.hashCode() : 0);
      result = 31 * result + (ruleId != null ? ruleId.hashCode() : 0);
      return result;
    }
  }
}
