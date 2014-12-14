package properties;

import java.util.Map;

import strategies.Strategy;

public abstract class Property<T> {
	protected Strategy<T> strategy;
	protected final String value;
	
	public Property(String value){
		this.value=value;
	}
	/**
	 * Returns map containing keys and corresponding values of the property for all resources
	 * @return
	 */
	public Map<String,T> getMap(){
		return strategy.execute();
	}
	/**
	 * @return given value of this property
	 */
	public String getPropertyValue() {
		return this.value;
	}
}