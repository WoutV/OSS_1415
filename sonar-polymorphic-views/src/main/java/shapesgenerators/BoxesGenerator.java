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
	
	
	public BoxesGenerator(MeasureFetcher measureFetcher, PolymorphicChartParameters polyParams) {
		Property<Double> width = new ValueProperty(polyParams.getBoxWidth(), PolymorphicChartParameters.DEFAULT_BOXWIDTH, measureFetcher);
		Property<Double> height = new ValueProperty(polyParams.getBoxHeight(), PolymorphicChartParameters.DEFAULT_BOXHEIGHT, measureFetcher);
		Property<Color> color = new ColorProperty(polyParams.getBoxColor(), PolymorphicChartParameters.DEFAULT_BOXCOLOR, measureFetcher);
		List<String> names = measureFetcher.getResourceNames();
		List<String> keys = measureFetcher.getResourceKeys();
		int numberOfShapes = names.size();
		this.shapes = new Shape[numberOfShapes];
		this.boxFactory = new BoxFactory();
		initBoxes(width, height,color,names,keys);
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
