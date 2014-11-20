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
	
	public List<BoxTree> getDependencyTrees(){
		List<BoxTree> dependencyTrees= new ArrayList<BoxTree>();
		for(Resource resource: resources){ //loop over main nodes and build a tree for each main node
			BoxTreeNode master = new BoxTreeNode(resource.getName());//create master node
			BoxTree tree = new BoxTree(master);//create tree for master node
			tree = fillTree(resource, tree);//createTree recursively
			dependencyTrees.add(tree);
		}
		return dependencyTrees;
	}
	
	public BoxTree fillTree(Resource resource, BoxTree tree){
		List<Dependency> dependencies= sonar.findOutgoingDependencies(resource); //get dependencies for a resource
		for(Dependency dependency: dependencies){//loop over all found resources
			String toResourceKey = dependency.getToResourceKey();//get the key of every resource of a dependency
			Resource res = sonar.findResource(toResourceKey);//find the resource with the key
			resources.add(res);// at this resource to resources
			BoxTreeNode node = new BoxTreeNode(res.getName());//create node for resource
			tree.addNode(node);//add resource to tree
			tree = fillTree(res, tree);//and do whole thing again for each resource
		}
		return tree; // return tree
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
