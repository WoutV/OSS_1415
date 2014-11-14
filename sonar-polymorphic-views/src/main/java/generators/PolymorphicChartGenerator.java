package generators;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sonar.api.charts.ChartParameters;

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
	protected MeasureFetcher measureFetcher;
	protected BoxGenerator boxGenerator;
	protected Box[] boxes;
	public PolymorphicChartGenerator(ChartParameters params, SonarFacade sonar) {
		this.builder = new Java2DBuilder();
		
		String resourceType = params.getValue("resources");
		String parent = params.getValue("parent");
		this.measureFetcher = new MeasureFetcher(resourceType,parent,sonar);
		this.boxGenerator = new BoxGenerator(measureFetcher);
		this.boxes = boxGenerator.getBoxes(params.getValue("boxwidth"),params.getValue("boxheight"),params.getValue("boxcolor"));
	}

	public abstract BufferedImage generateImage();
}
