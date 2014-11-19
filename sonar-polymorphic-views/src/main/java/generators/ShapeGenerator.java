package generators;

import java.awt.Color;

public abstract class ShapeGenerator {
	protected MeasureFetcher measureFetcher;
	
	public ShapeGenerator(MeasureFetcher measureFetcher) {
		this.measureFetcher=measureFetcher;
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
}