package strategies;

import java.util.HashMap;
import java.util.Map;

import utility.MeasureFetcher;

public class ConstantStrategy implements Strategy<Double> {

	private MeasureFetcher measureFetcher;
	private double value;

	public ConstantStrategy(String value, MeasureFetcher measureFetcher) {
		this.measureFetcher = measureFetcher;
		try{
			this.value = Double.parseDouble(value);}
		catch(Exception e){
			this.value=0.0;
		}
	}

	@Override
	public Map<String, Double> execute() {
		Map<String, Double> result = new HashMap<String, Double>();
		for (String s : measureFetcher.getResourceKeysAndNames().keySet()) {
			result.put(s, this.value);
		}
		return result;
	}
}
