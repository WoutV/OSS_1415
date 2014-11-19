package generators;

import java.awt.image.BufferedImage;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;
import chartbuilder.ChartBuilder;
import chartbuilder.Java2DBuilder;

/**
 * This class is an abstract superclass for generators that make polymorphic charts. Every generator has a builder,
 * to draw the chart, and a measurefetcher which serves as communication with the database.
 * @author Thijs&Wout
 *
 */
public abstract class PolymorphicChartGenerator {
	protected ChartBuilder builder;
	protected MeasureFetcher measureFetcher;
	
	
	/**
	 * Create a new PolymorphicChartGenerator
	 * @param params the parameters to be used to create the generator, specifies the resources, parent, type , ... 
	 * @param sonar the provided sonarfacade that serves as connection with the database
	 */
	public PolymorphicChartGenerator(PolymorphicChartParameters params, SonarFacade sonar) {
		this.builder = new Java2DBuilder();
		
		String resourceType = params.getResources();
		String parent = params.getParent();
		this.measureFetcher = new MeasureFetcher(resourceType,parent,sonar);
	}

	
	/**
	 * This method generates a bufferedimage. It uses the builder to do so. 
	 * @return the generated image
	 */
	public abstract BufferedImage generateImage();
}
