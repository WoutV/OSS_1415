package facade;

/**
 * Interface representing a Sonar dependency.
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 *
 */
public interface Dependency {

	/**
	 * Returns the key of the origin resource of this dependency.
	 * 
	 * @return The key of the origin resource of this dependency.
	 */
	public String getFromResourceKey();

	/**
	 * Returns the key of the destination resource of this dependency.
	 * 
	 * @return The key of the destination resource of this dependency.
	 */
	public String getToResourceKey();

	/**
	 * Returns the type of this dependency.
	 * 
	 * @return The type of this dependency.
	 */
	public DependencyType getType();
}
