package shapesgenerators;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import plugin.PolymorphicChartParameters;
import properties.ColorProperty;
import properties.Property;
import properties.ValueProperty;
import shapes.Box;
import shapes.Shape;
import utility.MeasureFetcher;
import utility.Util;

/**
 * This class is used to generate lists of boxes with a certain width, height and color.
 * @author Thijs&Wout
 *
 */

public class BoxesGenerator implements IShapesGenerator {
	
	private final static double MIN_BOX_SIZE = 5;
	private final static double MAX_BOX_SIZE = 100;
	private Map<String, Shape> shapes;
	
	/**
	* This method creates a boxesgenerator. It extracts the necessary data from the given parameters and initiates its list of boxes.
	* @param measureFetcher is a connection with the database, used to obtain the names and keys of the resources
	* @param polyParams the user input which contains the values for all properties of the chart
	*/
	public BoxesGenerator(MeasureFetcher measureFetcher, PolymorphicChartParameters polyParams) {
		Property<Double> width = new ValueProperty(polyParams.getBoxWidth(), PolymorphicChartParameters.DEFAULT_BOXWIDTH, measureFetcher);
		Property<Double> height = new ValueProperty(polyParams.getBoxHeight(), PolymorphicChartParameters.DEFAULT_BOXHEIGHT, measureFetcher);
		Property<Color> color = new ColorProperty(polyParams.getBoxColor(), PolymorphicChartParameters.DEFAULT_BOXCOLOR, measureFetcher);
		Map<String,String> names = measureFetcher.getResourceKeysAndNames();
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
		for(String i : keysAndNames.keySet()) {
			Shape s = new Box(widthList.get(i), heightList.get(i), i, keysAndNames.get(i), colorList.get(i));
			shapes.put(i, s);				
		}
	}

	@Override
	public Map<String, Shape> getShapes() {
		return this.shapes;
	}
	

}
