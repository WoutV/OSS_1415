package be.kuleuven.cs.oss.sonarfacade;

/**
 * Interface representing a Sonar resource.
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 *
 */
public interface Resource {
	
	/**
	 * Returns the integer identifier of this resource.
	 * 
	 * @return The integer identifier of this resource.
	 */
	public Integer getId();

	/**
	 * Returns the key of this resource.
	 * 
	 * @return The key of this resource.
	 */
	public String getKey();
	
	/**
	 * Returns the name of this resource.
	 * @return The name of this resource.
	 */
	public String getName();

	/**
	 * Returns the qualifier of this resource.
	 * 
	 * @return The qualifier of this resource.
	 */
	public ResourceQualifier getQualifier();
}
