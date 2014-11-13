package generators;

import java.awt.image.BufferedImage;

import org.sonar.api.charts.ChartParameters;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.ResourceQualifier;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;
import chartbuilder.ChartBuilder;
import chartbuilder.Java2DBuilder;

public abstract class PolymorphicChartGenerator {
	protected ChartBuilder builder;
	protected int boxWidth;
	protected int boxHeight;
	protected int boxColor;
	protected MeasureFetcher measureFetcher;

	public PolymorphicChartGenerator(PolymorphicChartParameters params, SonarFacade sonar) {
		this.builder = new Java2DBuilder();

		ResourceQualifier resources = params.getResources();
		String parent = params.getParent();
		this.measureFetcher = new MeasureFetcher(resources,parent,sonar);
	}

	public abstract BufferedImage generateImage();
	
	private void init(PolymorphicChartParameters params){
		String width = params.getBoxWidth();
		String height = params.getBoxHeight();
		String color = params.getBoxColor();
		
		try{
			boxWidth = Integer.parseInt(width);
		}
		catch (NumberFormatException e){
			
		}
		
		
		
	}
}
