package generators;

import org.sonar.api.charts.ChartParameters;

import chartbuilder.ChartBuilder;

public abstract class PolymorphicChartGenerator {
	protected ChartBuilder builder;
	protected ChartParameters params;
	
	public PolymorphicChartGenerator(ChartParameters params) {
		this.params = params;
	}
}
