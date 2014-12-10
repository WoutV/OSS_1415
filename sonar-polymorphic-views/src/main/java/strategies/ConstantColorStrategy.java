package strategies;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import utility.MeasureFetcher;

public class ConstantColorStrategy implements Strategy<Color> {
	
	private Color color;
	private MeasureFetcher mf;

	public ConstantColorStrategy(Color color,MeasureFetcher mf) {
		this.mf = mf;
		this.color = color;
	}

	@Override
	public Map<String, Color> execute() {
		Map<String,Color> colorMap = new HashMap<String,Color>();
			for(String key : mf.getResourceKeys()){
				colorMap.put(key, color);
			}
		return colorMap;
	}

	
}
