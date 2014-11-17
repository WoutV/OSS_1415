package generators;

import java.awt.image.BufferedImage;

import org.sonar.api.charts.ChartParameters;

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
	protected ShapeGenerator shapeGenerator;
	protected Box[] boxes;
	
	public PolymorphicChartGenerator(ChartParameters params, SonarFacade sonar) {
		this.builder = new Java2DBuilder();
		
		String resourceType = params.getValue("resources");
		String parent = params.getValue("parent");
		this.measureFetcher = new MeasureFetcher(resourceType,parent,sonar);
		this.shapeGenerator = new BoxGenerator(measureFetcher);
		//TODO ik heb hier ne cast gedaan naar ne array me boxes. Volgens mij is da niet ideaal
		this.boxes = (Box[]) shapeGenerator.getShapes(params.getValue("boxwidth"),params.getValue("boxheight"),params.getValue("boxcolor"));
	}

	public abstract BufferedImage generateImage();
}
