package strategies;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utility.MeasureFetcher;
import utility.Util;

public class MetricColorStrategy implements Strategy<Color> {

	private String color;
	private MeasureFetcher mf;
	private String defaultColor;

	public MetricColorStrategy(String color, String defaultColor, MeasureFetcher mf) {
		this.mf = mf;
		this.color = color;
		this.defaultColor = defaultColor;
	}

	@Override
	public Map<String, Color> execute() {
		Map<String,Color> colorMap = new HashMap<String,Color>();
		colorMap= createGrayScaledList();
		return colorMap;
	}

	private Map<String,Color> createGrayScaledList() {
		Map<String,Color> colorMap = new HashMap<String,Color>();
		try{
			String[] splitted = Util.splitOnDelimiter(color, new String[]{"min","max","key"});
			Double min = Double.parseDouble(splitted[0]);
			Double max = Double.parseDouble(splitted[1]);
			String key = splitted[2];

			colorMap = getGrayScaleColors(min, max, key, mf);
		}
		//Given input is not valid
		catch (IllegalArgumentException f){
			Color rgb = Util.parseColor(defaultColor);
			for(String key : mf.getResourceKeys()){
				colorMap.put(key, rgb);
			}
		}
		return colorMap;
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
	private Map<String,Color> getGrayScaleColors(Double min, Double max, String key, MeasureFetcher measureFetcher) throws IllegalArgumentException{
		try{
			Map<String, Double> colors = measureFetcher.getMeasureValues(key);
			Map<String, Double> scaledColors = Util.scaleGrey(colors, min, max);
			List<String> keys = measureFetcher.getResourceKeys();
			Map<String,Color> result = new HashMap<String,Color>();
			for (String i : keys) {
				int colorValue = scaledColors.get(i).intValue();
				Color c = new Color(colorValue, colorValue, colorValue);
				result.put(i,c);
			}
			return result;
		}
		catch(Exception e){
			throw new IllegalArgumentException();
		}
	}
}
