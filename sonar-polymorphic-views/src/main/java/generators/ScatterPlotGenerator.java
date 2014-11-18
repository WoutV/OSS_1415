package generators;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;

public class ScatterPlotGenerator extends PolymorphicChartGenerator {

	private int width;
	private int height;
	private String xMetric;
	private String yMetric;
	private Box[] boxes;

	
	/**
	 * This constructor creates a new scatterplotgenerator object. 
	 * @param polyParams specify the chart that should be built
	 * @param sonar is a facade that can be used to access data about the analyzed project
	 */
	public ScatterPlotGenerator(PolymorphicChartParameters polyParams, SonarFacade sonar) {
		super(polyParams,sonar);
		
		this.xMetric = polyParams.getXMetric();
		this.yMetric = polyParams.getYMetric();
		
		parseSize(polyParams.getSize());
		
		BoxGenerator boxGenerator = new BoxGenerator(measureFetcher);
		this.boxes = boxGenerator.getBoxes(polyParams.getBoxWidth(),polyParams.getBoxHeight(),polyParams.getBoxColor());
	}

	@Override
	public BufferedImage generateImage() {
		Map<String,Double> xValues = measureFetcher.getMeasureValues(xMetric);
		Map<String,Double> yValues = measureFetcher.getMeasureValues(yMetric);
		
		int minX = Collections.min(xValues.values(),null).intValue();
		int maxX = Collections.max(xValues.values(),null).intValue();
		int minY = Collections.min(yValues.values(),null).intValue();
		int maxY = Collections.max(yValues.values(),null).intValue();
		
		xValues=scale(xValues, 0, width);
		yValues=scale(yValues, 0, height);

	    builder.createCanvas(height, width, BufferedImage.TYPE_INT_RGB);
	    builder.createXAxis(xMetric, 0, width, minX, maxX, 0); 
	    builder.createYAxis(yMetric, 0, height,minY, maxY, 0); 
		buildBoxes(xValues,yValues);
		
		return builder.getImage();
	}
	
	/**
	 * This method scales the given values map.
	 * @param values the array to be scaled
	 * @param a the minimum value of the scaled values
	 * @param b the maximum value of the scaled values
	 * @return the Map with the scaled values and their key
	 */
	static Map<String,Double> scale(Map<String,Double> values, double a, double b){
		double min = Collections.min(values.values(),null);
		double max = Collections.max(values.values(),null);
		double factor = (b-a)/(max-min);

		for(Entry<String, Double> entry :values.entrySet()){
			double newValue = factor*entry.getValue()+(a);
			values.put(entry.getKey(), newValue);
		}
		
		return values;
	}

	/**
	 * This method builds all the boxes used by the scatterplot
	 * @param xValues values for x position of the boxes
	 * @param yValues values for y position of the boxes
	 */
	private void buildBoxes(Map<String, Double> xValues, Map<String, Double> yValues) {
		for(Box shape : this.boxes){
			String resourceName = shape.getName();
			double xValue = xValues.get(resourceName);
			double yValue = yValues.get(resourceName);
			builder.createRectangle((int) xValue, (int) yValue, (int) shape.getHeight(),(int) shape.getWidth(), shape.getColor(), resourceName);
		}

	}

	private void parseSize(String size){		
		String[] sizes = size.split("x");
		this.width=Integer.parseInt(sizes[0]);
		this.height=Integer.parseInt(sizes[1]);
	}
}
