package generators;

import java.awt.image.BufferedImage;

import org.sonar.api.charts.ChartParameters;

public class ScatterPlotGenerator extends PolymorphicChartGenerator {

	public ScatterPlotGenerator(PolymorphicChartParameters params) {
		super(params);
	}

	@Override
	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		return builder.getImage();
	}
	
}
