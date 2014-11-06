package be.kuleuven.cs.oss.sonarfacade;

/**
 * Sonar web service adapter for the Resource interface.
 * 
 * The class is implemented as a decorator around the Sonar ws-client Resource
 * class.
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 *
 */
public class WSResource implements Resource {
	private final org.sonar.wsclient.services.Resource resource;
	
	/**
	 * Create an instance of this adapter class for a given Sonar ws-client resource.
	 * 
	 * @param resource  The ws-client resource to adapt to the Resource interface
	 */
	public WSResource(org.sonar.wsclient.services.Resource resource) {
		if (resource == null) {
			throw new IllegalArgumentException("resource cannot be null");
		}
		
		this.resource = resource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getId() {
		return getResource().getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getKey() {
		return getResource().getKey();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return getResource().getName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResourceQualifier getQualifier() {
		return ResourceQualifier.fromString(getResource().getQualifier());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "WSResource[id: "+getId()+", " +
				"key: "+getKey()+", " +
				"qualifier: "+getQualifier()+"]";
	}
	
	protected org.sonar.wsclient.services.Resource getResource() {
		return this.resource;
	}

}
