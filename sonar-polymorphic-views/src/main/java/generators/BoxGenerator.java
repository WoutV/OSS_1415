package generators;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;

/**
 * This class is used to generate lists of boxes with a certain width, height and color.
 * @author Thijs&Wout
 *
 */

public class BoxGenerator extends ShapeGenerator {
	private final static Logger LOG = LoggerFactory.getLogger(BoxGenerator.class);
	
	public BoxGenerator(MeasureFetcher measureFetcher, PolymorphicChartParameters params) {
		super(measureFetcher, params);
		int numberOfShapes = measureFetcher.getNumberOfResources();
		this.shapes = new Shape[numberOfShapes];
		initBoxes(params.getBoxWidth(), params.getBoxHeight(),params.getBoxColor());
	}
	
	/**
	 * This method initializes the list of shapes with "empty" boxes
	 */
	private void initBoxes(String width,String height,String color) {
		Box[] boxes =  new Box[shapes.length];
		for(int i = 0;i<boxes.length;i++) {
			boxes[i] = new Box();
		}
		this.shapes = boxes;
		nameShapes();
		
		setBoxProperties(width, height, color, boxes);
	}

	private void setBoxProperties(String width, String height, String color,Box[] boxes) {
		List<Double> widthList;
		try {
			widthList = getShapeDimension(width);
		} catch (IllegalArgumentException e) {
			widthList = getShapeDimension(PolymorphicChartParameters.DEFAULT_BOXWIDTH);
		}
		List<Double> heightList;
		try {
			heightList = getShapeDimension(height);
		} catch (IllegalArgumentException e) {
			heightList = getShapeDimension(PolymorphicChartParameters.DEFAULT_BOXHEIGHT);
		}
		List<Color> colorList = getShapeColors(color);
		for (int i = 0; i < boxes.length; i++) {
			boxes[i].setHeight(heightList.get(i));
			boxes[i].setWidth(widthList.get(i));
			boxes[i].setColor(colorList.get(i));
		}
	}


	/**
	 * This method returns a list with the needed dimensions of the shapes. If
	 * the input can be parsed to an int, all shapes have the same dimension, if
	 * the input is a string, the dimension of the boxes is dependent on the
	 * metric with the input as its key.
	 * 
	 * @param dimension
	 *            either the dimension, or the string defining the metric
	 *            needed.
	 * @return a list with the width/height of all the boxes
	 */
	private List<Double> getShapeDimension(String dimension) throws IllegalArgumentException {
		List<Double> dimensions;
		try {
			Double parsedDimension = Math.abs(Double.parseDouble(dimension));
			dimensions = new ArrayList<Double>(Collections.nCopies(shapes.length, parsedDimension));
		} catch (NumberFormatException e) {
				Map<String, Double> values = measureFetcher.getMeasureValues(dimension);
				dimensions = getShapeSize(values);
				if(dimensions.isEmpty()) {
					throw new IllegalArgumentException();
				}
		}
		return dimensions;
	}

	/**
	 * This method returns a list of double-typed values for a certain property
	 * of a set of shapes. The values are in the same order as the shapes in the
	 * shapes array.
	 * 
	 * @param values
	 * @return the values, ordered by shape name
	 */
	private List<Double> getShapeSize(Map<String, Double> values) {
		List<Double> result = new ArrayList<Double>();
		for (int i = 0; i < shapes.length; i++) {
			for (String s : values.keySet()) {
				if (shapes[i].getName().equals(s)) {
					result.add(values.get(s));
				}
			}
		}
		return result;
	}
}
