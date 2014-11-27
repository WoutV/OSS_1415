package generators;

import java.util.Map;

public interface Strategy<T> {
	public abstract Map<String, T> execute();
}
