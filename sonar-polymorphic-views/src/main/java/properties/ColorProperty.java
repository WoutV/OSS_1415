package properties;

import java.awt.Color;

import strategies.ConstantColorStrategy;
import strategies.MetricColorStrategy;
import utility.MeasureFetcher;
import utility.Util;

public class ColorProperty extends Property<Color> {
private final String DEFAULT_VALUE;
	
	
	public ColorProperty (String value,String defaultValue, MeasureFetcher measureFetcher) {
		super(value);
		this.DEFAULT_VALUE = defaultValue;
		try{ 
			Color color = Util.parseColor(value);
			this.strategy = new ConstantColorStrategy(color, measureFetcher);
		}
		catch(Exception e){
			this.strategy = new MetricColorStrategy(value, defaultValue, measureFetcher);
		}
	}
}
