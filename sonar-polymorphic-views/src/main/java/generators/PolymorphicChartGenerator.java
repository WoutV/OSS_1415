package generators;

import java.awt.image.BufferedImage;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;
import chartbuilder.ChartBuilder;
import chartbuilder.Java2DBuilder;

/**
 * @author Thijs&Wout
 *
 */
public abstract class PolymorphicChartGenerator {
	protected ChartBuilder builder;
	protected MeasureFetcher measureFetcher;
	
	
	public PolymorphicChartGenerator(PolymorphicChartParameters params, SonarFacade sonar) {
		this.builder = new Java2DBuilder();
		
		String resourceType = params.getResources();
		String parent = params.getParent();
		this.measureFetcher = new MeasureFetcher(resourceType,parent,sonar);
	}

	public abstract BufferedImage generateImage();
}
