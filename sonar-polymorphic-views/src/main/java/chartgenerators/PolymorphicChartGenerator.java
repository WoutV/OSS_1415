package chartgenerators;

import java.awt.image.BufferedImage;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import shapesgenerators.BoxesGenerator;
import shapesgenerators.CirclesGenerator;
import shapesgenerators.IShapesGenerator;
import shapesgenerators.MetricShapesGenerator;
import shapesgenerators.TrapsGenerator;
import utility.MeasureFetcher;
import chartbuilder.ChartBuilder;
import chartbuilder.Java2DBuilder;
import facade.SonarFacade;

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

	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}
	
	/**
	 * This method generates a bufferedimage. It should use the builder to do so.
	 * @return the generated image
	 */
	public abstract BufferedImage generateImage();


}
