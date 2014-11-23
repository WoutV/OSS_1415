package generators;

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
	private String xMetric;
	private String yMetric;
	private List<Shape> shapes;

	
	/**
	 * This constructor creates a new scatterplotgenerator object. 
	 * @param polyParams specify the chart that should be built
	 * @param sonar is a facade that can be used to access data about the analyzed project
	 */
	public ScatterPlotGenerator(PolymorphicChartParameters polyParams, SonarFacade sonar) {
		super(polyParams,sonar);
		LOG.info("Creating scatterplot...");
		this.xMetric = polyParams.getXMetric();
		this.yMetric = polyParams.getYMetric();
		this.shapes = new ArrayList<Shape>();
	
		parseSize(polyParams.getSize());
		
		ShapeGenerator boxGenerator = new BoxGenerator(measureFetcher,polyParams);
		shapes.addAll(Arrays.asList(boxGenerator.getShapes()));
	}

	@Override
	public BufferedImage generateImage() {
		Map<String,Double> xPositions = measureFetcher.getMeasureValues(xMetric);
		Map<String,Double> yPositions = measureFetcher.getMeasureValues(yMetric);
		
		int minX = Collections.min(xPositions.values(),null).intValue();
		int maxX = Collections.max(xPositions.values(),null).intValue();
		int minY = Collections.min(yPositions.values(),null).intValue();
		int maxY = Collections.max(yPositions.values(),null).intValue();
		
		xPositions=Util.scale(xPositions, 0, width);
		yPositions=Util.scale(yPositions, 0, height);

		Log.info("Creating axes...");
	    builder.createCanvas(height, width, BufferedImage.TYPE_INT_RGB);
	    builder.createXAxis(xMetric, 0, width, minX, maxX); 
	    builder.createYAxis(yMetric, 0, height, minY, maxY);
	    
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
