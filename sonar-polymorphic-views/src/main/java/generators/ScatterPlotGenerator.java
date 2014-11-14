package generators;

import java.awt.image.BufferedImage;
import java.util.Map;

import org.sonar.api.charts.ChartParameters;

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
		Map<String,Double> xValues = measureFetcher.getMeasureValues(xMetric);
		Map<String,Double> yValues = measureFetcher.getMeasureValues(yMetric);
		buildImage(xValues,yValues);
		return builder.getImage();
	}



	private void buildImage(Map<String, Double> xValues,
			Map<String, Double> yValues) {
		builder.createCanvas(height, width, BufferedImage.TYPE_INT_RGB);
		for(Box box : this.boxes){
			String resourceName = box.getName();
			double xValue = xValues.get(resourceName);
			double yValue = yValues.get(resourceName);
			builder.createRectangle((int) xValue, (int) yValue, (int) box.getHeight(),(int) box.getWidth(), box.getColor(), resourceName);
		}

	}

	private void parseSize(String size){
		String[] sizes = size.split("x");
		this.width=Integer.parseInt(sizes[0]);
		this.height=Integer.parseInt(sizes[1]);
	}
}
