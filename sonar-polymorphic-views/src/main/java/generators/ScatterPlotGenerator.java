package generators;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;

public class ScatterPlotGenerator extends PolymorphicChartGenerator {

	private int width;
	private int height;
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
		
		this.xMetric = polyParams.getXMetric();
		this.yMetric = polyParams.getYMetric();
		this.shapes = new ArrayList<Shape>();
	
		parseSize(polyParams.getSize());
		
		ShapeGenerator boxGenerator = new BoxGenerator(measureFetcher,polyParams);
		System.out.println(boxGenerator.getShapes());
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

	    builder.createCanvas(height, width, BufferedImage.TYPE_INT_RGB);
	    builder.createXAxis(xMetric, minX, maxX); 
	    builder.createYAxis(yMetric, minY, maxY);
	    
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

	private void parseSize(String size){		
		String[] sizes = size.split("x");
		this.width=Integer.parseInt(sizes[0]);
		this.height=Integer.parseInt(sizes[1]);
	}
}
