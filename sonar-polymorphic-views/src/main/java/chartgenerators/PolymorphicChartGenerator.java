package chartgenerators;

import java.awt.image.BufferedImage;

import shapesgenerators.BoxesGenerator;
import shapesgenerators.CirclesGenerator;
import shapesgenerators.IShapesGenerator;
import shapesgenerators.MetricShapesGenerator;
import shapesgenerators.TrapsGenerator;
import utility.MeasureFetcher;
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
	protected int width;
	protected int height;
	protected IShapesGenerator generator;
	
	
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
		
		String shape = params.getShape();
		this.generator = null;
		switch (shape){
		case "box" : generator =  new BoxesGenerator(measureFetcher,params);
		break;
		case "circle" : generator = new CirclesGenerator(measureFetcher,params);
		break;
		case "trap" : generator = new TrapsGenerator(measureFetcher,params);
		break;
		default : generator = new MetricShapesGenerator(measureFetcher,params);
		break;
		}
	}

	
	/**
	 * This method generates a bufferedimage. It uses the builder to do so. 
	 * @return the generated image
	 */
	public abstract BufferedImage generateImage();


	/**
	 * This method splits a String of form IntegerxInteger into two strings, parses them and sets the width and height of the scatterplot.
	 * @param size
	 */
	protected void parseSize(String size) {		
		String[] sizes = size.split("x");
		this.width=Integer.parseInt(sizes[0]);
		this.height=Integer.parseInt(sizes[1]);
	}
}
