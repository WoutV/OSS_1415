package generators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.oss.sonarfacade.Measure;
import be.kuleuven.cs.oss.sonarfacade.Metric;
import be.kuleuven.cs.oss.sonarfacade.Resource;
import be.kuleuven.cs.oss.sonarfacade.ResourceQualifier;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;

/**
 * @author Thijs&Wout
 *
 */
public class MeasureFetcher {
	
	private SonarFacade sonar;
	private List<Resource> resources;

	public MeasureFetcher(ResourceQualifier resourceType, String parent, SonarFacade sonar){
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
		Map<String, Double> allValues = new HashMap<String, Double>(); //TODO Checken of resources zelfde naam kunnen hebben -> if so moet op andere manier
		for(Resource resource:resources){
			Measure m = sonar.findMeasure(resource, metric);
			allValues.put(resource.getKey(), m.getValue());
		}
				
		return allValues;
	}
	
	/**
	 * @param resourceType 
	 * @param parent - Ancestor of the resources that are returned
	 * @return All the resources of the given type below the given parent.
	 */
	private List<Resource> findResources(ResourceQualifier resourceType, Resource parent){
		switch (resourceType){
		case PACKAGE:
			 return sonar.findPackages(parent);
		
		case CLASS:
			 return sonar.findClasses(parent);
	
		default:
			return sonar.findPackages(parent);	
		}
	}
}