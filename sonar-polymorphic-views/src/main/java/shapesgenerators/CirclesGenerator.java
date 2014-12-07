package shapesgenerators;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
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
	private Shape[] shapes;
	private CircleFactory circleFactory;
	
	
	public CirclesGenerator(MeasureFetcher measureFetcher, PolymorphicChartParameters polyParams) {
		Property<Double> diameter = new ValueProperty(polyParams.getCircleDiam(), PolymorphicChartParameters.DEFAULT_CIRLCEDIAM, measureFetcher);
		Property<Color> color = new ColorProperty(polyParams.getCircleColor(), PolymorphicChartParameters.DEFAULT_CIRCLECOLOR, measureFetcher);
		List<String> names = measureFetcher.getResourceNames();
		List<String> keys = measureFetcher.getResourceKeys();
		this.circleFactory = new CircleFactory();
		initShapes(diameter, color,names,keys);
		}

	/**
	 * This method initializes the list of shapes
	 */
	private void initShapes(Property<Double> diameter,Property<Color> color, List<String> names, List<String> keyList) {
		List<Double> diamList = Util.scaleList(diameter.getValues(),MIN_SIZE,MAX_SIZE);
		List<Color> colorList = color.getValues();
		this.shapes = new Shape[names.size()];
		for(int i = 0;i<shapes.length;i++) {
			shapes[i] = circleFactory.createShape(diamList.get(i), diamList.get(i), keyList.get(i), names.get(i), colorList.get(i));
		}
	}

	@Override
	public Shape[] getShapes() {
		return this.shapes;
	}
	

}
