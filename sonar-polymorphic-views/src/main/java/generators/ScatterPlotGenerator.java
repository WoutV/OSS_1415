package generators;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;


public class ScatterPlotGenerator extends PolymorphicChartGenerator {
	private final static Logger LOG = LoggerFactory.getLogger(BoxGenerator.class);
	private Property<Double> xMetric;
	private Property<Double> yMetric;
	private List<Shape> shapes;

	
	/**
	 * This constructor creates a new scatterplotgenerator object. 
	 * @param polyParams specify the chart that should be built
	 * @param sonar is a facade that can be used to access data about the analyzed project
	 */
	public ScatterPlotGenerator(PolymorphicChartParameters polyParams, SonarFacade sonar) {
		super(polyParams,sonar);
		LOG.info("Creating scatterplot...");
		this.xMetric = new ValueProperty(polyParams.getXMetric(), PolymorphicChartParameters.DEFAULT_BOXWIDTH, measureFetcher);
		this.yMetric = new ValueProperty(polyParams.getYMetric(), PolymorphicChartParameters.DEFAULT_BOXWIDTH, measureFetcher);
		this.shapes = new ArrayList<Shape>();
		
		parseSize(polyParams.getSize());
		
		Property<Double> width = new ValueProperty(polyParams.getBoxWidth(), PolymorphicChartParameters.DEFAULT_BOXWIDTH, measureFetcher);
		Property<Double> height = new ValueProperty(polyParams.getBoxHeight(), PolymorphicChartParameters.DEFAULT_BOXHEIGHT, measureFetcher);
		Property<Color> color = new ColorProperty(polyParams.getBoxColor(), PolymorphicChartParameters.DEFAULT_BOXCOLOR, measureFetcher);
		ShapeGenerator boxGenerator = new BoxGenerator(measureFetcher,width,height,color);
		shapes.addAll(Arrays.asList(boxGenerator.getShapes()));
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
	    builder.createXAxis(xMetric.getName(), 0, width, 0, minX, maxX); 
	    builder.createYAxis(yMetric.getName(), 0, height, 0, minY, maxY);
	    
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
		for(Shape shape : this.shapes){
			Double xValue = xValues.get(shape.getName());
			Double yValue = yValues.get(shape.getName());
			shape.setxPos(xValue.intValue());
			shape.setyPos(yValue.intValue());
			shape.draw(this.builder);
		}
	}
}
