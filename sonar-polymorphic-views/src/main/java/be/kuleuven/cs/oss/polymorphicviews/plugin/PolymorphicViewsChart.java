package be.kuleuven.cs.oss.polymorphicviews.plugin;


import java.awt.image.BufferedImage;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.charts.Chart;
import org.sonar.api.charts.ChartParameters;

import chartgenerators.PolymorphicChartGenerator;
import chartgenerators.ScatterPlotGenerator;
import chartgenerators.SystemComplexityGenerator;
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
		try{
		setDefaultParameters();
		PolymorphicChartParameters polyParams = new PolymorphicChartParameters(params);
		String type = polyParams.getType();
		PolymorphicChartGenerator generator = null;
		if (type.equals("scatter")){
			generator = new ScatterPlotGenerator(polyParams,sonar);
		}
		else if (type.equals("syscomp") && polyParams.getResources().equals("classes")){
			generator = new SystemComplexityGenerator(polyParams,sonar);
		}
		else{
			generator = new ScatterPlotGenerator(polyParams,sonar);
		}
			BufferedImage buff = generator.generateImage();
			LOG.info("PolymorphicViewsChart generated!");
			return buff;}
		catch(Exception e)
		{
			System.out.println("Error occured generating the chart: \n");
			e.printStackTrace();
			Log.error("An error occured generating the chart" + e);
			return null;
		}
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
