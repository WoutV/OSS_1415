package shapes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import properties.Property;
import utility.MeasureFetcher;
import utility.Util;
import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;

public abstract class ShapeGenerator {
	protected MeasureFetcher measureFetcher;
	protected Shape[] shapes;
	protected  PolymorphicChartParameters params;
	private Property<Double> width;
	private Property<Double> height;
	private Property<Color> color;

	public ShapeGenerator(MeasureFetcher measureFetcher, Property<Double> width, Property<Double> height, Property<Color> color) {
		this.measureFetcher=measureFetcher;
		this.width = width;
		this.height = height;
		this.color = color;
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
	 * This method returns a list of colors, one for each shape. If
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
			Color rgb = Util.parseColor(color);
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
				Color rgb = Util.parseColor(PolymorphicChartParameters.DEFAULT_BOXCOLOR);
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
		//TODO hoe werkt dees eigenlijk. vage opgave
		Map<String, Double> colors = measureFetcher.getMeasureValues(key);
		Map<String, Double> scaledColors = Util.scaleGrey(colors, min, max);

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