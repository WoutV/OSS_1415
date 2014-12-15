package shapesgenerators;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import plugin.PolymorphicChartParameters;
import properties.ColorProperty;
import properties.Property;
import properties.ValueProperty;
import shapes.Circle;
import shapes.Shape;
import utility.MeasureFetcher;
import utility.Util;

/**
 * This class is used to generate lists of boxes with a certain width, height and color.
 * @author Thijs&Wout
 *
 */

public class CirclesGenerator implements IShapesGenerator {
	
	private final static double MIN_SIZE = 5;
	private final static double MAX_SIZE = 100;
	private Map<String, Shape> shapes;
	
	/**
	* This method creates a circlegenerator. It extracts the necessary data from the given parameters and initiates its list of circles.
	* @param measureFetcher is a connection with the database, used to obtain the names and keys of the resources
	* @param polyParams the user input which contains the values for all properties of the chart
	*/
	public CirclesGenerator(MeasureFetcher measureFetcher, PolymorphicChartParameters polyParams) {
		Property<Double> diameter = new ValueProperty(polyParams.getCircleDiam(), PolymorphicChartParameters.DEFAULT_CIRCLEDIAM, measureFetcher);
		Property<Color> color = new ColorProperty(polyParams.getCircleColor(), PolymorphicChartParameters.DEFAULT_CIRCLECOLOR, measureFetcher);
		Map<String,String> keysAndNames = measureFetcher.getResourceKeysAndNames();
		initShapes(diameter, color,keysAndNames);
		}

	/**
	 * This method initializes the list of shapes, based on some given properties.
	 */
	private void initShapes(Property<Double> diameter,Property<Color> color, Map<String,String> keysAndNames) {
		this.shapes = new HashMap<String, Shape>();
		Map<String,Double> diamList = Util.scaleMap(diameter.getMap(),MIN_SIZE,MAX_SIZE);
		Map<String,Color> colorList = color.getMap();
		for(String i : keysAndNames.keySet()) {
			Shape s = new Circle(diamList.get(i), diamList.get(i), i, keysAndNames.get(i), colorList.get(i));
			shapes.put(i, s);
		}
	}

	@Override
	public Map<String, Shape> getShapes() {
		return this.shapes;
	}
}
