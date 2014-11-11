package generators;

import java.awt.image.BufferedImage;

import org.sonar.api.charts.ChartParameters;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;

public class ScatterPlotGenerator extends PolymorphicChartGenerator {

	public ScatterPlotGenerator(PolymorphicChartParameters params) {
		super(params);
	}

	@Override
	public BufferedImage generateImage() {
		// TODO Auto-generated method stub
		return builder.getImage();
	}
	
}
