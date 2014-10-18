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
package org.sonar.plugins.core.sensors;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.mockito.Matchers.doubleThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class UnitTestDecoratorTest {

  private UnitTestDecorator decorator;
  private DecoratorContext context;

  @Before
  public void setUp() {
    decorator = new UnitTestDecorator();
    context = mock(DecoratorContext.class);
  }

  @Test
  public void generatesMetrics() {
    assertThat(decorator.generatesMetrics()).hasSize(5);
  }

  @Test
  public void doNotDecorateStaticAnalysis() {
    Project project = mock(Project.class);
    when(project.getAnalysisType()).thenReturn(Project.AnalysisType.STATIC);
    assertThat(decorator.shouldExecuteOnProject(project)).isFalse();

    when(project.getAnalysisType()).thenReturn(Project.AnalysisType.DYNAMIC);
    assertThat(decorator.shouldExecuteOnProject(project)).isTrue();
  }

  @Test
  public void shouldSumChildren() {
    Project project = mock(Project.class);
    mockChildrenMeasures(CoreMetrics.TESTS, 3.0);
    mockChildrenMeasures(CoreMetrics.TEST_ERRORS, 1.0);
    mockChildrenMeasures(CoreMetrics.TEST_FAILURES, 1.0);
    mockChildrenMeasures(CoreMetrics.SKIPPED_TESTS, 1.0);
    mockChildrenMeasures(CoreMetrics.TEST_EXECUTION_TIME, 1.0);

    decorator.decorate(project, context);

    verify(context).saveMeasure(eq(CoreMetrics.TESTS), eq(6.0));
    verify(context).saveMeasure(eq(CoreMetrics.TEST_ERRORS), eq(2.0));
    verify(context).saveMeasure(eq(CoreMetrics.TEST_FAILURES), eq(2.0));
    verify(context).saveMeasure(eq(CoreMetrics.SKIPPED_TESTS), eq(2.0));
    verify(context).saveMeasure(eq(CoreMetrics.TEST_EXECUTION_TIME), eq(2.0));
    verify(context).saveMeasure(eq(CoreMetrics.TEST_SUCCESS_DENSITY), doubleThat(closeTo(33.3, 0.1)));
  }

  private void mockChildrenMeasures(Metric metric, double value) {
    when(context.getChildrenMeasures(metric)).thenReturn(Arrays.asList(new Measure(metric, value), new Measure(metric, value)));
  }

  @Test
  public void doNotDecorateIfTestsMeasureAlreadyExists() {
    Project project = mock(Project.class);
    when(context.getMeasure(CoreMetrics.TESTS)).thenReturn(new Measure());

    decorator.decorate(project, context);

    assertThat(decorator.shouldDecorateResource(project, context)).isFalse();
    verify(context, atLeastOnce()).getMeasure(CoreMetrics.TESTS);
    verifyNoMoreInteractions(context);
  }

}
