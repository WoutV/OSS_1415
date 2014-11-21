package generatorsTest;

import generators.MeasureFetcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.oss.sonarfacade.SonarFacade;

import com.google.common.collect.Lists;

public class DummyMeasureFetcher extends MeasureFetcher {

	public DummyMeasureFetcher(String resourceType, String parent,
			SonarFacade sonar) {
		super(resourceType, parent, sonar);
	}
	
	@Override
	public int getNumberOfResources() {
		return 3;
	}
	
	@Override
	public List<String> getResourceNames() {
		return Lists.newArrayList("resource1", "resource2", "resource3");
	}
	
	@Override
	public Map<String, Double> getMeasureValues(String met) {
		Map<String,Double> result = new HashMap<String, Double>();
		if(met.equals("lines")) {
			result.put("resource1", 15.0);
			result.put("resource2", 10.0);
			result.put("resource3", 9.0);
		} else if (met.equals("commentlines")){
			result.put("resource1", 8.0);
			result.put("resource2", 5.0);
			result.put("resource3", 3.0);
		}
		return result;
	}

}
