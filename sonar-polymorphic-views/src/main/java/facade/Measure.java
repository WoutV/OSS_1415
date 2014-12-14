package facade;

/**
 * Interface representing a Sonar measurement.
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 *
 */
public interface Measure {
	
	/**
	 * Returns the key of the resource to which this measure applies.
	 * 
	 * @return The key of the resource to which this measure applies.
	 */
	public String getResourceKey();
	
	/**
	 * Returns the key of the metric of which this measure was taken.
	 * 
	 * @return The key of the metric of which this measure was taken.
	 */
	public String getMetricKey();

	/**
	 * Returns the value of this measure.
	 * 
	 * @return The value of this measure.
	 */
	public Double getValue();
}
