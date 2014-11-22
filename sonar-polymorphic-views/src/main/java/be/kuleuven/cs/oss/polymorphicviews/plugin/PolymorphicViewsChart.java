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
	 * This method generates a buffered image, based on the given ChartParameters.
	 * @param params arguments passed by end-user in URL
	 * @return the image that should be displayed in the browser
	 */
	@Override
	public BufferedImage generateImage(ChartParameters params) {
		LOG.info("PolymorphicViewsChart generateImage() called!");
		setDefaultParameters();
		PolymorphicChartParameters polyParams = new PolymorphicChartParameters(params);
		String type = polyParams.getType();
		PolymorphicChartGenerator generator = null;
		switch(type) { 
			case "scatter" : generator = new ScatterPlotGenerator(polyParams,sonar);
			break;
			case "syscomp" : generator = new SystemComplexityGenerator(polyParams,sonar);
			break;
			default : generator = new ScatterPlotGenerator(polyParams,sonar);
			break;
		}
		BufferedImage buff = generator.generateImage();
		LOG.info("PolymorphicViewsChart generated!");
		return buff;
	}

	private void setDefaultParameters() {
		String xMetricDefault = sonar.findMetrics().get(0).getKey();
		String yMetricDefault  =  sonar.findMetrics().get(1).getKey();
		String project = sonar.findProjects().get(0).getKey();
		PolymorphicChartParameters.DEFAULT_XMETRIC=xMetricDefault;
		PolymorphicChartParameters.DEFAULT_YMETRIC=yMetricDefault;
		PolymorphicChartParameters.DEFAULT_PARENT=project;
	}
	
	public SonarFacade getSonar() {
		return this.sonar;
	}
}
