package shapesgenerators;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import properties.Property;
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
	
	
	public CirclesGenerator(List<String> names, Property<Double> diameter, Property<Color> color, List<String> keyList) {
		int numberOfShapes = names.size();
		this.shapes = new Shape[numberOfShapes];
		this.circleFactory = new CircleFactory();
		initShapes(diameter, color,names,keyList);
		}

	/**
	 * This method initializes the list of shapes
	 */
	private void initShapes(Property<Double> diameter,Property<Color> color, List<String> names, List<String> keyList) {
		List<Double> diamList = Util.scaleList(diameter.getValues(),MIN_SIZE,MAX_SIZE);
		List<Color> colorList = color.getValues();
		for(int i = 0;i<shapes.length;i++) {
			shapes[i] = circleFactory.createShape(diamList.get(i), diamList.get(i), keyList.get(i), names.get(i), colorList.get(i));
		}
	}

	@Override
	public Shape[] getShapes() {
		return this.shapes;
	}
	

}
