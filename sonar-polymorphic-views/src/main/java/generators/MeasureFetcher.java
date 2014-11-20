package generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.oss.sonarfacade.Dependency;
import be.kuleuven.cs.oss.sonarfacade.Measure;
import be.kuleuven.cs.oss.sonarfacade.Metric;
import be.kuleuven.cs.oss.sonarfacade.Resource;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;

/**
 * @author Thijs&Wout
 *
 */
public class MeasureFetcher {
	
	private SonarFacade sonar;
	private List<Resource> resources;

	public MeasureFetcher(String resourceType, String parent, SonarFacade sonar){
		this.sonar = sonar;
		Resource ancestor = sonar.findResource(parent);
		this.resources = findResources(resourceType,ancestor);
	}
	
	/**
	 * @param metric = the metric key for the metric for which measures must be found
	 * @return values for the measures of the given metric
	 */
	public Map<String, Double> getMeasureValues(String met){		
		Metric metric = sonar.findMetric(met);		
		Map<String, Double> allValues = new HashMap<String, Double>();
		for(Resource resource:resources){
			Measure m = sonar.findMeasure(resource, metric);
			allValues.put(resource.getName(), m.getValue());
		}
				
		return allValues;
	}
	
	public BoxTree getDependencyTree(){
		BoxTree dependencyTree;
		for(Resource resource: resources){
			List<Dependency> dependencies= sonar.findOutgoingDependencies(resource);
			//for()
		}
		return null;
	}
	
	
	/**
	 * @param resourceType 
	 * @param parent - Ancestor of the resources that are returned
	 * @return All the resources of the given type below the given parent.
	 */
	private List<Resource> findResources(String resourceType, Resource parent){
		switch (resourceType){
		case "packages":
			 return sonar.findPackages(parent);
		
		case "classes":
			 return sonar.findClasses(parent);
		}
		return null;
	}

	public int getNumberOfResources() {
		return this.resources.size();
	}
	
	/**
	 * @return  a list of all the resourcenames
	 */
	public List<String> getResourceNames() {
		List<String> result = new ArrayList<String>();
		for(Resource r : resources) {
			result.add(r.getName());
		}
		return result;
	}
	
	public String getDefaultProject() {
		return sonar.findProjects().get(0).getKey();
	}
	
	
}
