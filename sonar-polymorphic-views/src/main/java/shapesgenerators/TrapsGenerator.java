package shapesgenerators;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import properties.Property;

import shapes.Shape;
import shapes.TrapezoidFactory;
import utility.Util;

/**
 * This class is used to generate lists of boxes with a certain width, height and color.
 * @author Thijs&Wout
 *
 */

public class TrapsGenerator implements IShapesGenerator {
	
	private final static double MIN_SIZE = 5;
	private final static double MAX_SIZE = 100;
	private Shape[] shapes;
	private TrapezoidFactory trapFactory;
	
	
	public TrapsGenerator(List<String> names, Property<Double> width,Property<Double> height1, Property<Double> height2,Property<Color> color, List<String> keyList) {
		int numberOfShapes = names.size();
		this.shapes = new Shape[numberOfShapes];
		this.trapFactory = new TrapezoidFactory();
		initShapes(width, height1, height2, color,names,keyList);
		}

	/**
	 * This method initializes the list of shapes with "empty" boxes
	 */
	private void initShapes(Property<Double> width,Property<Double> height,Property<Double> height2,Property<Color> color, List<String> names, List<String> keyList) {
		List<Double> widthList = Util.scaleList(width.getValues(),MIN_SIZE,MAX_SIZE);
		List<Double> heightList = Util.scaleList(height.getValues(),MIN_SIZE,MAX_SIZE);
		List<Double> height2List = Util.scaleList(height2.getValues(),MIN_SIZE,MAX_SIZE);
		List<Color> colorList = color.getValues();
		for(int i = 0;i<shapes.length;i++) {
			shapes[i] = trapFactory.makeTrapezoid(heightList.get(i),widthList.get(i), height2List.get(i), keyList.get(i), names.get(i), colorList.get(i));
		}
	}

	@Override
	public Shape[] getShapes() {
		return this.shapes;
	}
	

}
