package be.kuleuven.cs.oss.polymorphicviews.plugin;

import java.awt.image.BufferedImage;
import java.io.File;

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
		//TODO
		File img = new File("poesje.jpg");

		BufferedImage buffImg = new BufferedImage(240, 240, BufferedImage.TYPE_INT_ARGB);
		return buffImg;
	}
	
	public SonarFacade getSonar() {
		return this.sonar;
	}
}
