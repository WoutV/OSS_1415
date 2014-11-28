package strategies;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utility.MeasureFetcher;
import utility.Util;

public class ColorStrategy implements Strategy<Color> {
	
	private String color;
	private String defaultColor;
	private MeasureFetcher mf;

	public ColorStrategy(String color, String defaultColor, MeasureFetcher mf) {
		this.mf = mf;
		this.color = color;
		this.defaultColor = defaultColor;
	}

	@Override
	public Map<String, Color> execute() {
		List<Color> colorList = new ArrayList<Color>();
		int size = mf.getNumberOfResources();
		if(Util.isValidColor(color)){
			Color rgb = Util.parseColor(color);
			colorList = new ArrayList<Color>(Collections.nCopies(size,rgb));
		} 
		else{ //the color parsing is invalid and the string should be of the format "min<float>max<float>key<string>"
			colorList = createGrayScaledList(size);
		}
		return createColorMap(mf, colorList);
	}

	private List<Color> createGrayScaledList(int size) {
		List<Color> colorList;
		try{
			String[] splitted = Util.splitOnDelimiter(color, new String[]{"min","max","key"});
			Double min = Double.parseDouble(splitted[0]);
			Double max = Double.parseDouble(splitted[1]);
			String key = splitted[2];

			colorList = getGrayScaleColors(min, max, key, mf);
		}
		//Given input is not valid
		catch (IllegalArgumentException f){
			Color rgb = Util.parseColor(defaultColor);
			colorList = new ArrayList<Color>(Collections.nCopies(size,rgb));
		}
		return colorList;
	}

	private Map<String, Color> createColorMap(MeasureFetcher measureFetcher,
			List<Color> colorList) {
		Map<String, Color> result = new HashMap<String, Color>();
		int i = 0;
		for (String s : measureFetcher.getResourceNames()) {
			result.put(s, colorList.get(i));
			i++;
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
	private List<Color> getGrayScaleColors(Double min, Double max, String key, MeasureFetcher measureFetcher) throws IllegalArgumentException{
		Map<String, Double> colors = measureFetcher.getMeasureValues(key);
		Map<String, Double> scaledColors = Util.scaleGrey(colors, min, max);
		int size = measureFetcher.getNumberOfResources();
		List<String> names = measureFetcher.getResourceNames();
		List<Color> result = new ArrayList<Color>();
		try{
			for (int i = 0; i < size; i++) {
				String name = names.get(i);
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
}
