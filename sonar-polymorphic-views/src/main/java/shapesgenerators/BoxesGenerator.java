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
import shapes.Shape;
import utility.MeasureFetcher;
import utility.Util;
import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;

/**
 * This class is used to generate lists of boxes with a certain width, height and color.
 * @author Thijs&Wout
 *
 */

public class BoxesGenerator implements IShapesGenerator {
	
	private final static double MIN_BOX_SIZE = 5;
	private final static double MAX_BOX_SIZE = 100;
	private Shape[] shapes;
	private BoxFactory boxFactory;
	
	
	public BoxesGenerator(List<String> names, Property<Double> width,Property<Double> height, Property<Color> color, List<String> keyList) {
		int numberOfShapes = names.size();
		this.shapes = new Shape[numberOfShapes];
		this.boxFactory = new BoxFactory();
		initBoxes(width, height,color,names,keyList);
		}

	/**
	 * This method initializes the list of shapes with "empty" boxes
	 */
	private void initBoxes(Property<Double> width,Property<Double> height,Property<Color> color, List<String> names, List<String> keyList) {
		List<Double> widthList = Util.scaleList(width.getValues(),MIN_BOX_SIZE,MAX_BOX_SIZE);
		List<Double> heightList = Util.scaleList(height.getValues(),MIN_BOX_SIZE,MAX_BOX_SIZE);
		List<Color> colorList = color.getValues();
		for(int i = 0;i<shapes.length;i++) {
			shapes[i] = boxFactory.createShape(heightList.get(i), widthList.get(i), keyList.get(i), names.get(i), colorList.get(i));
		}
	}

	@Override
	public Shape[] getShapes() {
		return this.shapes;
	}
	

}
