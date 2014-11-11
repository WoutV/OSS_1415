package generators;

import java.awt.image.BufferedImage;

import org.sonar.api.charts.ChartParameters;

import chartbuilder.ChartBuilder;

public abstract class PolymorphicChartGenerator {
	protected ChartBuilder builder;
	protected ChartParameters params;
	
	public PolymorphicChartGenerator(PolymorphicChartParameters params) {
		this.params = params;
		this.builder = new Java2DBuilder();
	}
	
	public abstract BufferedImage getImage();
}
