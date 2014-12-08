package properties;

import strategies.ConstantStrategy;
import strategies.SingleMetricStrategy;
import utility.MeasureFetcher;
import be.kuleuven.cs.oss.sonarfacade.Metric;

public class ValueProperty extends Property<Double> {
	private final String DEFAULT_VALUE;

	public ValueProperty(String value, String defaultValue,
			MeasureFetcher measureFetcher) {
		super(value);
		this.DEFAULT_VALUE = defaultValue;
		determineStrategy(value, measureFetcher);
	}

	private void determineStrategy(String value, MeasureFetcher measureFetcher) {
		try {
			if (Integer.parseInt(value) <= 0) {
				this.strategy = new ConstantStrategy(DEFAULT_VALUE,measureFetcher);
			} else {
				this.strategy = new ConstantStrategy(value, measureFetcher);
			}
		} catch (NumberFormatException e) {
			Metric metric = measureFetcher.findMetric(value);
			if (metric != null) {
				this.strategy = new SingleMetricStrategy(value, measureFetcher);
			} else {
				this.strategy = new ConstantStrategy(DEFAULT_VALUE,measureFetcher);
			}
		}
	}
}