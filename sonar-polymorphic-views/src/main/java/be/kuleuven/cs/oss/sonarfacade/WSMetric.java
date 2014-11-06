package be.kuleuven.cs.oss.sonarfacade;

/**
 * Sonar web service adapter for the Metric interface.
 * 
 * The class is implemented as a decorator around the Sonar ws-client Metric
 * class.
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 *
 */
public class WSMetric implements Metric {
	private final org.sonar.wsclient.services.Metric metric;
	
	/**
	 * Create an instance of this adapter class for a given Sonar ws-client metric.
	 * 
	 * @param metric  The ws-client metric to adapt to the Metric interface
	 */
	public WSMetric(org.sonar.wsclient.services.Metric metric) {
		if (metric == null) {
			throw new IllegalArgumentException("metric cannot be null");
		}
		
		this.metric = metric;
	}
	// comment derp derp
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getKey() {
		return getMetric().getKey();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return getMetric().getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return getMetric().getDescription();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "WSMetric[key: "+getKey()+", " +
				"name: "+getName()+", " +
				"description: "+getDescription()+"]";
	}
	
	protected org.sonar.wsclient.services.Metric getMetric() {
		return this.metric;
	}
}
