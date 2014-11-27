package generators;

import java.util.Map;

public class SingleMetricStrategy implements Strategy<Double> {

	private String metricKey;
	private MeasureFetcher measureFetcher;

	public SingleMetricStrategy(String metricKey, MeasureFetcher measureFetcher) {
		this.measureFetcher = measureFetcher;
		this.metricKey = metricKey;
	}

	@Override
	public Map<String,Double> execute() {
		return measureFetcher.getMeasureValues(this.metricKey);
	}
}
