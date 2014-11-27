package properties;

import java.awt.Color;

import strategies.ColorStrategy;
import utility.MeasureFetcher;

public class ColorProperty extends Property<Color> {
private final String DEFAULT_VALUE;
	
	
	public ColorProperty (String value,String defaultValue, MeasureFetcher measureFetcher) {
		this.DEFAULT_VALUE = defaultValue;
		this.strategy = new ColorStrategy(value, defaultValue, measureFetcher);
	}
}
