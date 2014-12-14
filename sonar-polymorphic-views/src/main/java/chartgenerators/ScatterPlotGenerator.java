package chartgenerators;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facade.SonarFacade;
import plugin.PolymorphicChartParameters;
import properties.Property;
import properties.ValueProperty;
import shapes.Shape;
import shapesgenerators.BoxesGenerator;
import utility.Util;


public class ScatterPlotGenerator extends PolymorphicChartGenerator {
	private final static Logger LOG = LoggerFactory.getLogger(BoxesGenerator.class);
	private Property<Double> xMetric;
	private Property<Double> yMetric;
	private Map<String, Shape> shapes;
	
	/**
	 * This constructor creates a new scatterplotgenerator object. 
	 * @param polyParams specify the chart that should be built
	 * @param sonar is a facade that can be used to access data about the analyzed project
	 */
	public ScatterPlotGenerator(PolymorphicChartParameters polyParams, SonarFacade sonar) {
		super(polyParams,sonar);
		LOG.info("Creating scatterplot...");
		this.xMetric = new ValueProperty(polyParams.getXMetric(), PolymorphicChartParameters.DEFAULT_XMETRIC, measureFetcher);
		this.yMetric = new ValueProperty(polyParams.getYMetric(), PolymorphicChartParameters.DEFAULT_YMETRIC, measureFetcher);
		this.shapes = new HashMap<String, Shape>();
		
		parseSize(polyParams.getSize());
		shapes = generator.getShapes();
	}

	@Override
	public BufferedImage generateImage() {
		Map<String,Double> xPositions = xMetric.getMap();
		Map<String,Double> yPositions = yMetric.getMap();
		
		int minX = Collections.min(xPositions.values(),null).intValue();
		int maxX = Collections.max(xPositions.values(),null).intValue();
		int minY = Collections.min(yPositions.values(),null).intValue();
		int maxY = Collections.max(yPositions.values(),null).intValue();
		
		xPositions=Util.scaleMap(xPositions, 0, width);
		yPositions=Util.scaleMap(yPositions, 0, height);

		Log.info("Creating axes...");
	    builder.createCanvas(height, width, BufferedImage.TYPE_INT_RGB);
	    builder.createXAxis(xMetric.getPropertyValue(), 0, width, 0, minX, maxX); 
	    builder.createYAxis(yMetric.getPropertyValue(), 0, height, 0, minY, maxY);
	    
	    Log.info("Building shapes...");
		buildShapes(xPositions,yPositions);
		return builder.getImage();
	}
	
	/**
	 * This method builds all the boxes used by the scatterplot
	 * @param xValues values for x position of the boxes
	 * @param yValues values for y position of the boxes
	 */
	private void buildShapes(Map<String, Double> xValues, Map<String, Double> yValues) {
		for(Shape shape : this.shapes.values()){
			Double xValue = xValues.get(shape.getKey());
			Double yValue = yValues.get(shape.getKey());
			shape.setxPos(xValue.intValue());
			shape.setyPos(yValue.intValue());
			shape.draw(this.builder);
		}
	}
	

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
