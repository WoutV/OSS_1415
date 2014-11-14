package generators;

import java.awt.image.BufferedImage;

import org.sonar.api.charts.ChartParameters;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;

public class SystemComplexityGenerator extends PolymorphicChartGenerator {

	public SystemComplexityGenerator(ChartParameters params, SonarFacade sonar) {
		super(params,sonar);
	}

	@Override
	public BufferedImage generateImage() {
		// TODO Auto-generated method stub
		return builder.getImage();
	}

}
