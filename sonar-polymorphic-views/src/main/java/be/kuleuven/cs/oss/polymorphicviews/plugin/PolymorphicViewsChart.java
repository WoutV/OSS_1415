package be.kuleuven.cs.oss.polymorphicviews.plugin;

import generators.PolymorphicChartGenerator;
import generators.ScatterPlotGenerator;
import generators.SystemComplexityGenerator;

import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.charts.Chart;
import org.sonar.api.charts.ChartParameters;

import be.kuleuven.cs.oss.sonarfacade.SonarFacade;

/**
 * Binding to the Sonar charts servlet
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 */
public class PolymorphicViewsChart implements Chart {
	private final static Logger LOG = LoggerFactory.getLogger(PolymorphicViewsChart.class);
	private final SonarFacade sonar;
	
	public PolymorphicViewsChart(SonarFacade sonar) {
		this.sonar = sonar;
	}
	
	@Override
	public String getKey() {
		return "polymorphic";
	}

	/**
	 * @param params arguments passed by end-user in URL
	 * @return the image that should be displayed in the browser
	 */
	@Override
	public BufferedImage generateImage(ChartParameters params) {
		PolymorphicChartParameters polyParams = (PolymorphicChartParameters) params;
		LOG.info("PolymorphicViewsChart generateImage() called!");
		String type = polyParams.getType();
		PolymorphicChartGenerator generator = null;
		switch(type) { 
			case "scatter" : generator = new ScatterPlotGenerator(polyParams);
			break;
			case "syscomp" : generator = new SystemComplexityGenerator(polyParams);
			break;
		}
		//BufferedImage buff = generator.getImage();
		//TODO return statement
		return null;
	}
	
	public SonarFacade getSonar() {
		return this.sonar;
	}
}
