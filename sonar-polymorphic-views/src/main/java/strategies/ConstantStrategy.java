package strategies;

import java.util.HashMap;
import java.util.Map;

import utility.MeasureFetcher;

public class ConstantStrategy implements Strategy<Double> {

	private MeasureFetcher measureFetcher;
	private double value;

	public ConstantStrategy(String value, MeasureFetcher measureFetcher) {
		this.measureFetcher = measureFetcher;
		this.value = Double.parseDouble(value);
	}

	@Override
	public Map<String, Double> execute() {
		Map<String, Double> result = new HashMap<String, Double>();
		for (String s : measureFetcher.getResourceNames()) {
			result.put(s, this.value);
		}
		return result;
	}
}
