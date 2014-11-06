package be.kuleuven.cs.oss.sonarfacade;

/**
 * Interface representing a Sonar metric.
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 *
 */
public interface Metric {
	
	/**
	 * Returns the key of this metric.
	 * 
	 * @return The key of this metric.
	 */
	public String getKey();

	/**
	 * Returns the name of this metric.
	 * 
	 * @return The name of this metric.
	 */
	public String getName();

	/**
	 * Returns the description of this metric.
	 * 
	 * @return The description of this metric.
	 */
	public String getDescription();
}
