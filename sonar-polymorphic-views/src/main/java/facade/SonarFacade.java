package facade;

import java.util.List;

import org.sonar.api.ServerExtension;

/**
 * Interface representing a facade for easy access to the Sonar database.
 * 
 *  @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 *
 */
public interface SonarFacade extends ServerExtension {

	/**
	 * Find the resource with the given key.
	 * 
	 * @param key The key of the resource to find.
	 * @return The resource with the given key, or null if no resource with
	 *         that key exists.
	 */
	public Resource findResource(String key);

	/**
	 * Find all metrics.
	 * 
	 * @return A list of all metrics.
	 */
	public List<Metric> findMetrics();

	/**
	 * Find all projects.
	 * 
	 * @return A list of all projects. 
	 */
	public List<Resource> findProjects();

	/**
	 * Find the metric with the given key.
	 * 
	 * @param key The key of the metric to find.
	 * @return The metric with the given key, or null if no metric with
	 *         that key exists.
	 */
	public Metric findMetric(String key);
	
	/**
	 * Find a measure for a given resource and metric. If multiple measures
	 * exist for the given resource and metric, an arbitrary measure is 
	 * returned.
	 * 
	 * @param resource The resource for which to find a measure. 
	 * @param metric   The metric for which to find a measure.
	 * @return A measure for the given resource and metric, or null if no such
	 *         measure exists.
	 */
	public Measure findMeasure(Resource resource, Metric metric);
	
	/**
	 * Find all measures for a given resource.
	 * 
	 * @param resource The resource for which to find all measures.
	 * @return A list of all measures for the given resource.
	 */
	public List<Measure> findMeasures(Resource resource);

	/**
	 * Find all measures for a given resource and a given list of metrics.
	 * 
	 * @param resource The resource for which to find measures.
	 * @param metrics  The list of metric for which to find measures.
	 * @return A list of all measures for the given resource and list of metrics.
	 */
	public List<Measure> findMeasures(Resource resource, Metric ... metrics);

	/**
	 * Find all classes that are hierarchically below a given ancestor resource.
	 * 
	 * @param ancestor The resource for which to return all classes that are
	 *                 hierarchically lower.
	 * @return A list of all classes that are hierarchically below the given
	 *         ancestor resource.
	 */
	public List<Resource> findClasses(Resource ancestor);

	/**
	 * Find all packages that are hierarchically below a given ancestor resource.
	 * 
	 * @param ancestor The resource for which to return all packages that are
	 *                 hierarchically lower.
	 * @return A list of all packages that are hierarchically below the given
	 *         ancestor resource.
	 */
	public List<Resource> findPackages(Resource ancestor);
	
	/**
	 * Find all incoming dependencies for a given resource.
	 * 
	 * @param resource The resource for which to return all incoming dependencies.
	 * @return A list of all incoming dependencies for the given resource.
	 */
	public List<Dependency> findIncomingDependencies(Resource resource);
	
	/**
	 * Find all outgoing dependencies for a given resource.
	 * 
	 * @param resource The resource for which to return all outgoing dependencies.
	 * @return A list of all outgoing dependencies for the given resource.
	 */
	public List<Dependency> findOutgoingDependencies(Resource resource);
	
	/**
	 * Find all incoming and outgoing dependencies for a given resource.
	 * 
	 * @param resource The resource for which to return all dependencies.
	 * @return A list of all dependencies for the given resource.
	 */
	public List<Dependency> findDependencies(Resource resource);

}
