package generators;

import be.kuleuven.cs.oss.sonarfacade.Metric;

public class ValueProperty extends Property<Double> {
	private final String DEFAULT_VALUE;
	
	
	public ValueProperty (String value,String defaultValue, MeasureFetcher measureFetcher) {
		this.DEFAULT_VALUE = defaultValue;
		try {
			Integer.parseInt(value);
			this.strategy= new ConstantStrategy(value, measureFetcher);
		} catch(NumberFormatException e) {
			Metric metric = measureFetcher.findMetric(value);
			if(metric!=null) {
				this.strategy = new SingleMetricStrategy(value, measureFetcher);
			} else { 
				this.strategy = new ConstantStrategy(DEFAULT_VALUE, measureFetcher);
			}
		}
	}
}