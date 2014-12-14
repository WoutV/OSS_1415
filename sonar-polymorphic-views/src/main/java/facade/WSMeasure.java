package facade;

/**
 * Sonar web service adapter for the Measure interface.
 * 
 * The class is implemented as a decorator around the Sonar ws-client Measure
 * class.
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 *
 */
public class WSMeasure implements Measure {
	private final org.sonar.wsclient.services.Measure measure;
	private final String resourceKey;
	
	/**
	 * Create an instance of this adapter class for a given Sonar ws-client measure
	 * and resource key.
	 * 
	 * @param measure     The ws-client measure to adapt to the Measure interface.
	 * @param resourceKey The key of the resource to which this measure applies. 
	 */
	public WSMeasure(org.sonar.wsclient.services.Measure measure, String resourceKey) {
		if (measure == null) {
			throw new IllegalArgumentException("measure cannot be null");
		}
		if (resourceKey == null) {
			throw new IllegalArgumentException("resourceKey cannot be null");
		}
		
		this.measure = measure;
		this.resourceKey = resourceKey;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getResourceKey() {
		return this.resourceKey;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMetricKey() {
		return getMeasure().getMetricKey();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double getValue() {
		return getMeasure().getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "WSMeasure[resourceKey: "+getResourceKey()+", " +
				"metricKey: "+getMetricKey()+", " +
				"value: "+getValue()+"]";
	}
	
	protected org.sonar.wsclient.services.Measure getMeasure() {
		return this.measure;
	}

}
