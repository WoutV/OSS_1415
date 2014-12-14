package strategies;

import java.util.Map;

public interface Strategy<T> {
	/**
	 * executes this strategy
	 * @return map of keys to a value for all resources
	 */
	public abstract Map<String, T> execute();
}
