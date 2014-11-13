package generators;

import java.awt.image.BufferedImage;
import java.util.Map;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;

public class ScatterPlotGenerator extends PolymorphicChartGenerator {
	
	private int width;
	private int height;
	private String xMetric;
	private String yMetric;

	public ScatterPlotGenerator(PolymorphicChartParameters params, SonarFacade sonar) {
		super(params,sonar);
		this.xMetric = params.getXMetric();
		this.yMetric = params.getYMetric();
		parseSize(params.getSize());
		
	}

	@Override
	public BufferedImage generateImage() {
		Map<String,Double> xValues = measureFetcher.getMeasureValues(xMetric);
		Map<String,Double> yValues = measureFetcher.getMeasureValues(yMetric);
		
		
		return builder.getImage();
	}
	
	private void parseSize(String size){
		String[] sizes = size.split("x");
		this.width=Integer.parseInt(sizes[0]);
		this.height=Integer.parseInt(sizes[1]);
	}
}
