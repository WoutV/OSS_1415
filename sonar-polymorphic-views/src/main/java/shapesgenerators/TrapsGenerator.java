package shapesgenerators;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import plugin.PolymorphicChartParameters;
import properties.ColorProperty;
import properties.Property;
import properties.ValueProperty;
import shapes.Shape;
import shapes.Trapezoid;
import utility.MeasureFetcher;
import utility.Util;

/**
 * This class is used to generate lists of boxes with a certain width, height and color.
 * @author Thijs&Wout
 *
 */

public class TrapsGenerator implements IShapesGenerator {
	
	private final static double MIN_SIZE = 5;
	private final static double MAX_SIZE = 100;
	private Map<String,Shape> shapes;
	
	
	public TrapsGenerator(MeasureFetcher measureFetcher, PolymorphicChartParameters polyParams) {
		Property<Double> width = new ValueProperty(polyParams.getTrapSide1(), PolymorphicChartParameters.DEFAULT_TRAPSIDE1, measureFetcher);
		Property<Double> height = new ValueProperty(polyParams.getTrapSide2(), PolymorphicChartParameters.DEFAULT_TRAPSIDE2, measureFetcher);
		Property<Double> height2 = new ValueProperty(polyParams.getTrapSide3(), PolymorphicChartParameters.DEFAULT_TRAPSIDE3, measureFetcher);
		Property<Color> color = new ColorProperty(polyParams.getTrapColor(), PolymorphicChartParameters.DEFAULT_TRAPCOLOR, measureFetcher);
		Map<String,String> keysAndNames = measureFetcher.getResourceKeysAndNames();
		initShapes(width, height, height2, color,keysAndNames);
		}

	/**
	 * This method initializes the list of shapes
	 */
	private void initShapes(Property<Double> width,Property<Double> height,Property<Double> height2,Property<Color> color, Map<String,String> keys) {
		Map<String,Double> widthList = Util.scaleMap(width.getMap(),MIN_SIZE,MAX_SIZE);
		Map<String,Double> heightList = Util.scaleMap(height.getMap(),MIN_SIZE,MAX_SIZE);
		Map<String,Double> height2List = Util.scaleMap(height2.getMap(),MIN_SIZE,MAX_SIZE);
		Map<String,Color> colorList = color.getMap();
		this.shapes = new HashMap<String,Shape>();
		for(String i: keys.keySet()) {
			Shape s = new Trapezoid(widthList.get(i), heightList.get(i), height2List.get(i), i, keys.get(i), colorList.get(i));
			shapes.put(i, s);
		}
	}

	@Override
	public Map<String,Shape> getShapes() {
		return this.shapes;
	}
	

}
