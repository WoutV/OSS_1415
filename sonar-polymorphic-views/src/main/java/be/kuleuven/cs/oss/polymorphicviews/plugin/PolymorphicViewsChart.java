package be.kuleuven.cs.oss.polymorphicviews.plugin;

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

	@Override
	public BufferedImage generateImage(ChartParameters params) {
		LOG.info("PolymorphicViewsChart generateImage() called!");
		// TODO: implement
		// moet een chart maken (roept hier normaal duizend andere klasses aan)
		return null;
	}
	
	public SonarFacade getSonar() {
		return this.sonar;
	}
}
