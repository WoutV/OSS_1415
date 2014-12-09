package shapesgenerators;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import properties.ColorProperty;
import properties.Property;
import properties.ValueProperty;
import shapes.Box;
import shapes.BoxFactory;
import shapes.CircleFactory;
import shapes.Shape;
import utility.MeasureFetcher;
import utility.Util;
import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;

/**
 * This class is used to generate lists of boxes with a certain width, height and color.
 * @author Thijs&Wout
 *
 */

public class CirclesGenerator implements IShapesGenerator {
	
	private final static double MIN_SIZE = 5;
	private final static double MAX_SIZE = 100;
	private Map<String, Shape> shapes;
	private CircleFactory circleFactory;
	
	
	public CirclesGenerator(MeasureFetcher measureFetcher, PolymorphicChartParameters polyParams) {
		Property<Double> diameter = new ValueProperty(polyParams.getCircleDiam(), PolymorphicChartParameters.DEFAULT_CIRCLEDIAM, measureFetcher);
		Property<Color> color = new ColorProperty(polyParams.getCircleColor(), PolymorphicChartParameters.DEFAULT_CIRCLECOLOR, measureFetcher);
		Map<String,String> keysAndNames = measureFetcher.getResourceKeysAndNames();
		this.circleFactory = new CircleFactory();
		initShapes(diameter, color,keysAndNames);
		}

	/**
	 * This method initializes the list of shapes
	 */
	private void initShapes(Property<Double> diameter,Property<Color> color, Map<String,String> keysAndNames) {
		this.shapes = new HashMap<String, Shape>();
		Map<String,Double> diamList = Util.scaleMap(diameter.getMap(),MIN_SIZE,MAX_SIZE);
		Map<String,Color> colorList = color.getMap();
		int j = 0;
		for(String i : keysAndNames.keySet()) {
			Shape s = circleFactory.createShape
					(diamList.get(i),
							diamList.get(i), 
							i,
							keysAndNames.get(i), 
							colorList.get(i));
			j++;
			shapes.put(i, s);
		}
	}

	@Override
	public Map<String, Shape> getShapes() {
		return this.shapes;
	}
	

}
