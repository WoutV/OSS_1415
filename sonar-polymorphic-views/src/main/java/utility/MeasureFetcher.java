package utility;

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
			if(m!=null){
				allValues.put(resource.getKey(), m.getValue());
			}
		}	
		return allValues;
	}
	
	
	/**
	 * @param resourceType 
	 * @param parent - Ancestor of the resources that are returned
	 * @return All the resources of the given type below the given parent.
	 */
	private List<Resource> findResources(String resourceType, Resource parent){
		List<Resource> result = new ArrayList<Resource>();
		switch (resourceType){
		case "packages":
			 result = sonar.findPackages(parent);		
		case "classes":
			 result = sonar.findClasses(parent);
		}
		return result;
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
	
	/**
	 * @return mapp of keys to names for all resources
	 */
	public HashMap<String, String> getResourceKeysAndNames(){
		HashMap<String, String> result = new HashMap<String, String>();
		for(Resource r : resources) {
			result.put(r.getKey(), r.getName());
		}
		return result;
	}
	
	/**
	 * @return First project sonar can find
	 */
	public String getDefaultProject() {
		return sonar.findProjects().get(0).getKey();
	}
	
	/**
	 * @param key
	 * @return Resource for the given key
	 */
	private Resource getResource(String key){
		for(Resource resource : resources){
			if(resource.getKey().equals(key)){
				return resource;
			}
		}
		return null;
	}
	
	/**
	 * @param resourceKey
	 * @return all the outgoing dependencies for the given resourcekey
	 */
	public List<String[]> findOutgoingDependencies(String resourceKey){
		List<Dependency> dependencies = sonar.findOutgoingDependencies(getResource(resourceKey));
		List<String[]> result = new ArrayList<String[]>();
		for(Dependency dependency : dependencies){
			String[] tuple = {dependency.getType().toString() , dependency.getToResourceKey()};
			result.add(tuple);
		}
		return result;
	}
	
	/**
	 * @param resourceKey
	 * @return all the outgoing dependencies for the given resourcekey
	 */
	public List<String[]> findIncomingDependencies(String resourceKey){
		List<Dependency> dependencies = sonar.findIncomingDependencies(getResource(resourceKey));
		List<String[]> result = new ArrayList<String[]>();
		for(Dependency dependency : dependencies){
			String[] tuple = {dependency.getType().toString() , dependency.getFromResourceKey()};
			result.add(tuple);
		}
		return result;
	}

	/**
	 * @param key
	 * @return Metric for the given key
	 */
	public Metric findMetric(String key) {
		return sonar.findMetric(key);
	}

	/**
	 * @return keys from all the resources
	 */
	public List<String> getResourceKeys() {
		List<String> keys = new ArrayList<String>();
		for(Resource r: resources){
			keys.add(r.getKey());
		}
		return keys;
	}
	
}
