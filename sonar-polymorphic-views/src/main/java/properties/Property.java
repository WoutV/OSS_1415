package properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import strategies.Strategy;

public abstract class Property<T> {
	protected Strategy<T> strategy;
	protected final String value;
	public Property(String value){
		this.value=value;
	}
	public List<T> getValues() {
		return new ArrayList<T>(strategy.execute().values());
	}
	public Map<String,T> getMap(){
		return strategy.execute();
	}
	public String getName() {
		return this.value;
	}
}