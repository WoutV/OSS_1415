package generators;

import java.awt.image.BufferedImage;

import org.sonar.api.charts.ChartParameters;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.ResourceQualifier;
import chartbuilder.ChartBuilder;
import chartbuilder.Java2DBuilder;

public abstract class PolymorphicChartGenerator {
	protected ChartBuilder builder;
	protected ChartParameters params;
	private ResourceQualifier resources;
	private String parent;
	private int boxWidth;
	private int boxHeight;
	private int boxColor;
	
	public PolymorphicChartGenerator(PolymorphicChartParameters params) {
		this.params = params;
		this.resources = params.getResources();
		this.parent = params.getParent();
		this.boxWidth = params.getBoxWidth();
		this.boxHeight = params.getBoxHeight();
		this.boxColor = params.getBoxColor();
		this.builder = new Java2DBuilder();
	}
	
	public abstract BufferedImage generateImage();
}
