package generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
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
		if(sonar != null) {
			Resource ancestor = sonar.findResource(parent);
			this.resources = findResources(resourceType,ancestor);
		}
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
	
	
	/**
	 * @param resourceType 
	 * @param parent - Ancestor of the resources that are returned
	 * @return All the resources of the given type below the given parent.
	 */
	private List<Resource> findResources(String resourceType, Resource parent){
		switch (resourceType){
		case "packages":
			 List<Resource> packages = sonar.findPackages(parent);
			 return packages;
		
		case "classes":
			 List<Resource> classes = sonar.findClasses(parent);
			 return classes;
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
	
	public HashMap<String, String> getResourceKeysAndNames(){
		HashMap<String, String> result = new HashMap<String, String>();
		for(Resource r : resources) {
			result.put(r.getKey(), r.getName());
		}
		return result;
	}
	
	public String getDefaultProject() {
		return sonar.findProjects().get(0).getKey();
	}
	
	private Resource getResource(String key){
		for(Resource resource : resources){
			if(resource.getKey().equals(key)){
				return resource;
			}
		}
		return null;
	}
	
	public List<String[]> findOutgoingDependencies(String resourceKey){
		List<Dependency> dependencies = sonar.findOutgoingDependencies(getResource(resourceKey)); //TODO fix, not getting anything
		List<String[]> result = new ArrayList<String[]>();
		for(Dependency dependency : dependencies){
			String[] tuple = {dependency.getType().toString() , dependency.getToResourceKey()};
			result.add(tuple);
		}
		return result;
	}
	
	public List<String[]> findIncomingDependencies(String resourceKey){
		List<Dependency> dependencies = sonar.findIncomingDependencies(getResource(resourceKey)); //TODO fix, not getting anything
		List<String[]> result = new ArrayList<String[]>();
		for(Dependency dependency : dependencies){
			String[] tuple = {dependency.getType().toString() , dependency.getFromResourceKey()};
			result.add(tuple);
		}
		return result;
	}
	
}
