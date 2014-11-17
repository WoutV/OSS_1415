package generators;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.sonar.api.charts.ChartParameters;

import com.ctc.wstx.dtd.MinimalDTDReader;

import chartbuilder.ChartBuilder;
import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;

public class ScatterPlotGenerator extends PolymorphicChartGenerator {

	private static final int OFFSET = 50;
	private int width;
	private int height;
	private String xMetric;
	private String yMetric;
	private Box[] boxes;

	public ScatterPlotGenerator(PolymorphicChartParameters polyParams, SonarFacade sonar) {
		super(polyParams,sonar);
		this.xMetric = polyParams.getXMetric();
		this.yMetric = polyParams.getYMetric();
		parseSize(polyParams.getSize());
		BoxGenerator boxGenerator = new BoxGenerator(measureFetcher);
		this.boxes = boxGenerator.getShapes(polyParams.getBoxWidth(),polyParams.getBoxHeight(),polyParams.getBoxColor());
	}

	
	@Override
	public BufferedImage generateImage() {
		double maxX = width-OFFSET	;
		double maxY = height-OFFSET;

	    builder.createCanvas(height, width, BufferedImage.TYPE_INT_RGB);
	    builder.createXAxis(xMetric, OFFSET, width, 0,(int) maxX,(int) maxY); 
	    builder.createYAxis(yMetric, OFFSET, height, 0,(int) maxY,OFFSET); 
		Map<String,Double> xValues = addOffset(scale(measureFetcher.getMeasureValues(xMetric),0, maxX));
		Map<String,Double> yValues = addOffset(scale(measureFetcher.getMeasureValues(yMetric),0, maxY));
		buildBoxes(xValues,yValues);
		return builder.getImage();
	}


	private Map<String, Double> addOffset(Map<String, Double> values) {
		for(Entry<String, Double> entry :values.entrySet()){
			double newValue = entry.getValue()+OFFSET;
			values.put(entry.getKey(), newValue);
		}
		return values;
	}


	/**
	 * Scales given the values of a given map so the biggest value is at size
	 * @param values
	 * @param size
	 * @return
	 */
	
	//TODO test schrijven voor deze methode
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

	private void buildBoxes(Map<String, Double> xValues,
			Map<String, Double> yValues) {
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
	
//	private void Map<String,Double> fixOffset(Map<String,>) {
//		
//		
//	}
}
