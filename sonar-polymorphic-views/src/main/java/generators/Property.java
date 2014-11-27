package generators;

import java.util.ArrayList;
import java.util.List;

public abstract class Property<T> {
	protected Strategy<T> strategy;
	public List<T> getValues() {
		return new ArrayList<T>(strategy.execute().values());
	}
}