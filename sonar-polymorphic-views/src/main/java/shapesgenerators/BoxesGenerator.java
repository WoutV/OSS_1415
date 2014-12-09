package shapesgenerators;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import properties.ColorProperty;
import properties.Property;
import properties.ValueProperty;
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
	private Map<String, Shape> shapes;
	private BoxFactory boxFactory;
	
	
	public BoxesGenerator(MeasureFetcher measureFetcher, PolymorphicChartParameters polyParams) {
		Property<Double> width = new ValueProperty(polyParams.getBoxWidth(), PolymorphicChartParameters.DEFAULT_BOXWIDTH, measureFetcher);
		Property<Double> height = new ValueProperty(polyParams.getBoxHeight(), PolymorphicChartParameters.DEFAULT_BOXHEIGHT, measureFetcher);
		Property<Color> color = new ColorProperty(polyParams.getBoxColor(), PolymorphicChartParameters.DEFAULT_BOXCOLOR, measureFetcher);
		Map<String,String> names = measureFetcher.getResourceKeysAndNames();
		this.boxFactory = new BoxFactory();
		initBoxes(width, height,color,names);
	}
	

	/**
	 * This method initializes the list of boxes
	 */
	private void initBoxes(Property<Double> width,Property<Double> height,Property<Color> color, Map<String,String> keysAndNames) {
		Map<String,Double> widthList = Util.scaleMap(width.getMap(),MIN_BOX_SIZE,MAX_BOX_SIZE);
		Map<String,Double> heightList = Util.scaleMap(height.getMap(),MIN_BOX_SIZE,MAX_BOX_SIZE);
		Map<String,Color> colorList = color.getMap();
		this.shapes = new HashMap<String,Shape>();
		int j = 0;
		for(String i : keysAndNames.keySet()) {
			Shape s = boxFactory.createShape
					(heightList.get(i),
							widthList.get(i), 
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
