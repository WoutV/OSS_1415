package generators;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;

/**
 * This class is used to generate lists of boxes with a certain width, height and color.
 * @author Thijs&Wout
 *
 */

public class BoxGenerator {
	private final static Logger LOG = LoggerFactory.getLogger(BoxGenerator.class);
	private MeasureFetcher measureFetcher;
	private Box[] boxes;
	
	public BoxGenerator(MeasureFetcher measureFetcher) {
		this.measureFetcher = measureFetcher;
		//should never be null
		//the null check is for testing only
		//if(!measureFetcher.equals(null)) {
			int numberOfShapes = measureFetcher.getNumberOfResources();
			this.boxes= new Box[numberOfShapes];
			initBoxes();
			nameShapes();
		//}
		
	}
	
	/**
	 * This method initializes the list of shapes with "empty" boxes
	 */
	private void initBoxes() {
		for(int i = 0;i<boxes.length;i++) {
			boxes[i] = new Box();
		}
	}

	
	/**
	 * This method gives all boxes the correct values.
	 * @param width that should be given to all boxes (number or metric)
	 * @param height that should be given to all boxes (number or metric)
	 * @param color that should be given to all boxes (rgb format or grayscale with metric)
	 * @return
	 */
	public Box[] getBoxes(String width, String height, String color) {
		List<Double> widthList = getShapeDimension(width);
		List<Double> heightList = getShapeDimension(height);
		List<Color> colorList = getShapeColors(color);
		for (int i = 0; i < boxes.length; i++) {
			boxes[i].setHeight(heightList.get(i));
			boxes[i].setWidth(widthList.get(i));
			boxes[i].setColor(colorList.get(i));
		}
		return boxes;
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
	private List<Double> getShapeDimension(String dimension) {
		List<Double> dimensions;
		try {
			Double parsedDimension = Double.parseDouble(dimension);
			dimensions = new ArrayList<Double>(Collections.nCopies(boxes.length, parsedDimension));
		} catch (NumberFormatException e) {
			Map<String, Double> values = measureFetcher.getMeasureValues(dimension);
			dimensions = getShapeSize(values);
		}
		return dimensions;
	}

	/**
	 * This method returns a list of double-typed values for a certain property
	 * of a set of shapes. The values are in the same order as the shapes in the
	 * shapes array.
	 * 
	 * @param values
	 */
	private List<Double> getShapeSize(Map<String, Double> values) {
		List<Double> result = new ArrayList<Double>();
		for (int i = 0; i < boxes.length; i++) {
			for (String s : values.keySet()) {
				if (boxes[i].getName().equals(s)) {
					result.add(values.get(s));
				}
			}
		}
		return result;
	}

	/**
	 * TODO aanpa This method returns a list of colors, one for each shape. If
	 * the input is in RGB format, the color is the same for all shapes. If the
	 * format "min<float>max<float>key<string>" is used as input, the color of
	 * each box is dependent on a specific measure of the box. Else, the color
	 * will be set to default color
	 * 
	 * @param color
	 *            the color/metric to be used
	 * @return a list with a color for each box
	 */
	private List<Color> getShapeColors(String color) {
		List<Color> result = new ArrayList<Color>();
		try {
			Color rgb = parseColor(color);
			result = new ArrayList<Color>(Collections.nCopies(boxes.length,
					rgb));

		} catch (Exception e) {
			String[] splitted = Util.splitOnDelimiter(color, new String[]{"min","max","key"});
			Double min = Double.parseDouble(splitted[0]);
			Double max = Double.parseDouble(splitted[1]);
			String key = splitted[2];
			
			result = getGrayScaleColors(min, max, key);
		}
		return result;
	}

	private List<Color> getGrayScaleColors(Double min, Double max, String key) {
		Map<String, Double> colors = measureFetcher.getMeasureValues(key);
		Map<String, Double> scaledColors = ScatterPlotGenerator.scale(colors, min, max);
		
		List<Color> result = new ArrayList<Color>();

		for (int i = 0; i < boxes.length; i++) {
			String name = boxes[i].getName();
			int colorValue = scaledColors.get(name).intValue();
			Color c = new Color(colorValue, colorValue, colorValue);
			result.add(c);
		}
		return result;
	}

	/**
	 * This method parses an input string to an rgb color. If the input is not
	 * of the form rxxxgxxxbxxx, it should be of the form
	 * min<float>max<float>key<string>. In the first case, the color will be the
	 * same for all shapes, in the second case, the color will be depending on a
	 * specific metric.
	 * 
	 * @param color
	 * @return
	 * @throws IllegalArgumentException
	 */
	static Color parseColor(String color) throws IllegalArgumentException {
		Integer[] result = new Integer[3];
		try {
			String[] splitted = Util.splitOnDelimiter(color, new String[]{"r","g","b"});
			result[0] = Integer.parseInt(splitted[0]);
			result[1] = Integer.parseInt(splitted[1]);
			result[2] = Integer.parseInt(splitted[2]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException();
		}
		return new Color(result[0], result[1], result[2]);
	}

	/**
	 * This method names all the shapes with the correct resource names.
	 */
	protected void nameShapes() {
		List<String> names = measureFetcher.getResourceNames();
		int i = 0;
		for (String s : names) {
			boxes[i].setName(s);
			i++;
		}
	}
}
