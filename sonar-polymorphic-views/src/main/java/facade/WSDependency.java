package facade;

/**
 * Sonar web service adapter for the Dependency interface.
 * 
 * The class is implemented as a decorator around the Sonar ws-client Dependency
 * class.
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 *
 */
public class WSDependency implements Dependency {
	private final org.sonar.wsclient.services.Dependency dependency;
	
	/**
	 * Create an instance of this adapter class for a given Sonar ws-client
	 * dependency.
	 * 
	 * @param dependency The ws-client dependency to adapt to the Dependency
	 *                   interface.
	 */
	public WSDependency(org.sonar.wsclient.services.Dependency dependency) {
		if (dependency == null) {
			throw new IllegalArgumentException("dependency cannot be null");
		}
		
		this.dependency = dependency;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFromResourceKey() {
		return getDependency().getFromKey();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getToResourceKey() {
		return getDependency().getToKey();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DependencyType getType() {
		return DependencyType.fromString(getDependency().getUsage());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "WSDependency[fromResourceKey: "+getFromResourceKey()+", " +
				"toResourceKey: "+getToResourceKey()+", "+
				"type: "+getType()+"]";
	}
	
	protected org.sonar.wsclient.services.Dependency getDependency() {
		return this.dependency;
	}

}
