package generators;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.ResourceQualifier;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;
import chartbuilder.ChartBuilder;
import chartbuilder.Java2DBuilder;

/**
 * @author Thijs&Wout
 *
 */
public abstract class PolymorphicChartGenerator {
	protected ChartBuilder builder;
	protected int boxWidth;
	protected int boxHeight;
	protected int boxColor;
	protected MeasureFetcher measureFetcher;
	protected BoxGenerator boxGenerator;
	protected Box[] boxes;
	public PolymorphicChartGenerator(PolymorphicChartParameters params, SonarFacade sonar) {
		this.builder = new Java2DBuilder();
		
		ResourceQualifier resources = params.getResources();
		String parent = params.getParent();
		this.measureFetcher = new MeasureFetcher(resources,parent,sonar);
		this.boxGenerator = new BoxGenerator(measureFetcher);
		this.boxes = boxGenerator.getBoxes(params);
	}

	public abstract BufferedImage generateImage();
}
