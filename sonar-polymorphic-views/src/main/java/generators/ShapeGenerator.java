package generators;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.sonar.api.charts.ChartParameters;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;

public abstract class ShapeGenerator {
	protected MeasureFetcher measureFetcher;

	protected Shape[] shapes;

	protected  PolymorphicChartParameters params;

	public ShapeGenerator(MeasureFetcher measureFetcher, PolymorphicChartParameters params) {
		this.measureFetcher=measureFetcher;
		this.params=params;
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
	 * This method gives all boxes the correct values.
	 * @param width that should be given to all boxes (number or metric)
	 * @param height that should be given to all boxes (number or metric)
	 * @param color that should be given to all boxes (rgb format or grayscale with metric)
	 * @return
	 */
	public Shape[] getShapes() {
		return this.shapes;
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
	protected List<Color> getShapeColors(String color) {
		List<Color> result = new ArrayList<Color>();
		if(Util.isValidColor(color)){
			Color rgb = ShapeGenerator.parseColor(color);
			result = new ArrayList<Color>(Collections.nCopies(shapes.length,rgb));
		} 
		else{ //the color parsing is invalid and the string should be of the format "min<float>max<float>key<string>"
			try{
				String[] splitted = Util.splitOnDelimiter(color, new String[]{"min","max","key"});
				Double min = Double.parseDouble(splitted[0]);
				Double max = Double.parseDouble(splitted[1]);
				String key = splitted[2];

				result = getGrayScaleColors(min, max, key);
			}
			//Given input is not valid
			catch (IllegalArgumentException f){
				Color rgb = ShapeGenerator.parseColor(PolymorphicChartParameters.DEFAULT_BOXCOLOR);
				result = new ArrayList<Color>(Collections.nCopies(shapes.length,rgb));
			}
		}
		return result;
	}

	/**
	 * This method scales a list of metric values to a new list, with the lowest
	 * value of the metric scaled to min, and the highest scaled to max.
	 * 
	 * @param min
	 *            the minimum color value of the list with scaled colors
	 * @param max
	 *            the maximum color value of the list with scaled colors
	 * @param key
	 *            the key that represents the metric that should be scaled
	 * @return a list with the scaled color values
	 */
	private List<Color> getGrayScaleColors(Double min, Double max, String key) throws IllegalArgumentException{
		Map<String, Double> colors = measureFetcher.getMeasureValues(key);
		Map<String, Double> scaledColors = Util.scale(colors, min, max);

		List<Color> result = new ArrayList<Color>();
		try{
			for (int i = 0; i < shapes.length; i++) {
				String name = shapes[i].getName();
				int colorValue = scaledColors.get(name).intValue();
				Color c = new Color(colorValue, colorValue, colorValue);
				result.add(c);
			}
			return result;
		}
		catch(Exception e){
			throw new IllegalArgumentException();
		}
	}

	/**
	 * This method names all the shapes with the correct resource names.
	 */
	protected void nameShapes() {
		List<String> names = measureFetcher.getResourceNames();
		int i = 0;
		for (String s : names) {
			shapes[i].setName(s);
			i++;
		}
	}

}