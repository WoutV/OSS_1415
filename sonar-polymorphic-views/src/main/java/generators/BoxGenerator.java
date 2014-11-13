package generators;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;

/**
 * This class is used to generate lists of boxes with a certain width, height and color.
 * @author Thijs&Wout
 *
 */
public class BoxGenerator {
	private Box[] boxes = new Box[3];
	private MeasureFetcher measureFetcher;

	public BoxGenerator(MeasureFetcher measureFetcher) {
		this.measureFetcher = measureFetcher;
		initBoxes();
		if(measureFetcher!=null) {
			nameBoxes();
		}
	}

	private void initBoxes() {
		for(int i = 0;i<boxes.length;i++) {
			boxes[i] = new Box();
		}
	}

	/**
	 * This method generates a list of boxes with size and color defined by the
	 * params input. Size and/or color may be either a fixed value, or a value
	 * defined by a given metric.
	 * 
	 * @param params
	 *            defines the sizes and colors of the boxes
	 * @return an array of boxes with the given dimension/color
	 */
	public Box[] getBoxes(PolymorphicChartParameters params) {
		
		String width = params.getBoxWidth();
		String height = params.getBoxHeight();
		String color = params.getBoxColor();

		List<Double> widthList = getBoxDimension(width);
		List<Double> heightList = getBoxDimension(height);
		List<Color> colorList = getBoxColors(color);
		for (int i = 0; i < boxes.length; i++) {
			boxes[i].setHeight(heightList.get(i));
			boxes[i].setWidth(widthList.get(i));
			boxes[i].setColor(colorList.get(i));
		}
		return boxes;
	}

	/**
	 * This method returns a list of colors, one for each box. If the input is
	 * in RGB format, the color is the same for all boxes. If the format
	 * "min<float>max<float>key<string>" is used as input, the color of each box
	 * is dependent on a specific measure of the box.
	 * 
	 * @param color
	 *            the color/metric to be used
	 * @return a list with a color for each box
	 */
	private List<Color> getBoxColors(String color) {
		List<Color> result = new ArrayList<Color>();
		try {
			Color rgb = parseColor(color);
			result = new ArrayList<Color>(Collections.nCopies(
					boxes.length, rgb));
			
		} catch (IllegalArgumentException e) {
			// TODO kleur met metrics en overgang en shizzle
		}
		return result;
	}

	/**
	 * This method parses an input string to an rgb color. If the input is not
	 * of the form rxxxgxxxbxxx, it should be of the form
	 * min<float>max<float>key<string>. In the first case, the color will be the
	 * same for all boxes, in the second case, the color will be depending on a
	 * specific metric.
	 * 
	 * @param color
	 * @return
	 * @throws IllegalArgumentException
	 */
	private Color parseColor(String color) throws IllegalArgumentException {
		Integer[] result = new Integer[3];
			try {
				result[0] = Integer.parseInt(color.split("r")[1].split("g")[0]);
				
				result[1] = Integer.parseInt(color.split("g")[1].split("b")[0]);
				result[2] = Integer.parseInt(color.split("b")[1]);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException();
			}
		return new Color(result[0], result[1], result[2]);
	}

	/**
	 * This method returns a list with the needed dimensions of the boxes
	 * (either width or height). If the input can be parsed to an int, all boxes
	 * have the same dimension, if the input is a string, the dimension of the
	 * boxes is dependent on the metric with the input as its key.
	 * 
	 * @param dimension
	 *            either the dimension, or the string defining the metric
	 *            needed.
	 * @return a list with the width/height of all the boxes
	 */
	private List<Double> getBoxDimension(String dimension) {
		List<Double> dimensions;
		try {
			Double parsedDimension = Double.parseDouble(dimension);
			dimensions = new ArrayList<Double>(Collections.nCopies(boxes.length, parsedDimension));
		} catch (NumberFormatException e) {
			Map<String, Double> values = measureFetcher
					.getMeasureValues(dimension);
			dimensions = getBoxSize(values);
		}
		return dimensions;
	}

	/**
	 * This method names all the boxes with the correct resource names.
	 */
	private void nameBoxes() {
		List<String> names = measureFetcher.getResourceNames();
		int i = 0;
		for (String s : names) {
			boxes[i].setName(s);
			i++;
		}
	}

	/**
	 * This method sets the height of all the boxes to the values in the given
	 * map. The names in the map specify which box gets which height.
	 * 
	 * @param values
	 */
	private List<Double> getBoxSize(Map<String, Double> values) {
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
}
