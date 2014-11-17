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

	private int width;
	private int height;
	private String xMetric;
	private String yMetric;

	public ScatterPlotGenerator(ChartParameters params, SonarFacade sonar) {
		super(params,sonar);
		this.xMetric = params.getValue("xmetric");
		this.yMetric = params.getValue("ymetric");
		parseSize(params.getValue("size"));
	}

	
	@Override
	public BufferedImage generateImage() {
		double maxX = 0.9*width;
		double maxY = 0.9*height;

	    builder.createCanvas(height, width, BufferedImage.TYPE_INT_RGB);
	    builder.createXAxis(xMetric, 0, 0, (int) maxX,width,0); 
	    builder.createYAxis(yMetric, 0, 0, (int) maxY,height,0); 
		Map<String,Double> xValues = scale(measureFetcher.getMeasureValues(xMetric),0, maxX);
		Map<String,Double> yValues = scale(measureFetcher.getMeasureValues(yMetric),0, maxY);
		buildBoxes(xValues,yValues);
		return builder.getImage();
	}


	/**
	 * Scales given the values of a given map so the biggest value is at size
	 * @param values
	 * @param size
	 * @return
	 */
	
	//TODO test schrijven voor deze methode
	static Map<String,Double> scale(Map<String,Double> values, double min, double max){
		double minimum = Collections.min(values.values(),null);
		double maximum = Collections.max(values.values(),null);
		double totalLength = min+max;		

		for(Entry<String, Double> entry :values.entrySet()){
			double newValue = (maximum - minimum)*(entry.getValue() - min)/(max-min) + minimum;
			values.put(entry.getKey(), newValue);
		}
		return values;
	}

	private void buildBoxes(Map<String, Double> xValues,
			Map<String, Double> yValues) {
		for(Box box : this.boxes){
			String resourceName = box.getName();
			double xValue = xValues.get(resourceName);
			double yValue = yValues.get(resourceName);
			builder.createRectangle((int) xValue, (int) yValue, (int) box.getHeight(),(int) box.getWidth(), box.getColor(), resourceName);
		}

	}

	private void parseSize(String size){
		//TODO fixen met default size!
		if(size == null || size.isEmpty()){
			size="1048x1048";
		}
		
		String[] sizes = size.split("x");
		this.width=Integer.parseInt(sizes[0]);
		this.height=Integer.parseInt(sizes[1]);
	}
}
