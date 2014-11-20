package be.kuleuven.cs.oss.polymorphicviews.plugin;

import java.util.HashMap;
import java.util.Map;

import org.sonar.api.charts.ChartParameters;

public class PolymorphicChartParameters {

	public static final String PARAM_RESOURCES = "resources";
	public static final String PARAM_PARENT = "parent";
	public static final String PARAM_TYPE = "type";
	public static final String PARAM_XMETRIC = "xmetric";
	public static final String PARAM_YMETRIC = "ymetric";
	public static final String PARAM_SIZE = "size";
	public static final String PARAM_BOXWIDTH = "boxwidth";
	public static final String PARAM_BOXHEIGHT = "boxheight";
	public static final String PARAM_BOXCOLOR = "boxcolor";
	public static final String PARAM_LOCALE = "locale";

	public static final String DEFAULT_TYPE = "scatter"; 
	public static final String DEFAULT_BOXWIDTH = "13";
	public static final String DEFAULT_BOXHEIGHT = "13";
	public static final String DEFAULT_BOXCOLOR = "r255g255b255"; 
	public static final String DEFAULT_SIZE = "800x800";
	public static final String DEFAULT_RESOURCES = "classes";
	public static String DEFAULT_PARENT; // TODO aanpassen
	public static String DEFAULT_XMETRIC ;
	public static String DEFAULT_YMETRIC;
	private Map<String, String> params = new HashMap<String, String>();
	private static final String[] properties = {"resources","parent","type","xmetric","ymetric","size","boxwidth","boxheight","boxcolor"};

	
	/**
	 * Generates a polymorphicChartParameters object, with the same properties as the given ChartParameters
	 * @param p
	 */
	public PolymorphicChartParameters(ChartParameters p) {
		for(String s : properties) {
			this.params.put(s,p.getValue(s));
		}
		
		
	}

	/**
	 * @return resources
	 */
	public String getResources() {
		String result = getValue(PARAM_RESOURCES,DEFAULT_RESOURCES);
		return result;
	}

	/**
	 * @return parent
	 */
	public String getParent(){
		String result = getValue(PARAM_PARENT, DEFAULT_PARENT);
		return result;
	}
	
	/**
	 * @return xmetric
	 */
	public String getXMetric(){
		String result = getValue(PARAM_XMETRIC, DEFAULT_XMETRIC);
		return result;
	}
	
	/**
	 * @return ymetric
	 */
	public String getYMetric(){
		String result = getValue(PARAM_YMETRIC, DEFAULT_YMETRIC);
		return result;
	}
	
	public String getType(){
		String result = getValue(PARAM_TYPE, DEFAULT_TYPE);
		return result;
	}

	public String getBoxWidth(){
		String result = getValue(PARAM_BOXWIDTH, DEFAULT_BOXWIDTH);
		return result;
	}
	
	public String getSize(){
		String result = getValue(PARAM_SIZE, DEFAULT_SIZE);
		return result;
	}
	
	public String getBoxHeight(){
		String result = getValue(PARAM_BOXHEIGHT, DEFAULT_BOXHEIGHT);
		return result;
	}
	
	public String getBoxColor(){
		String result = getValue(PARAM_BOXCOLOR, DEFAULT_BOXCOLOR);
		return result;
	}

	  /**
	   * Returns the [decoded or not] value of a param from its key or the default value
	   * if id does not exist
	   *
	   * @param key the param ket
	   * @param defaultValue the default value if not exist
	   * @param decode whther the value should be decoded
	   * @return the value of the param
	   */
	  public String getValue(String key, String defaultValue) {
		String val = params.get(key);
	    if (val == null || val.isEmpty()) {
	      val = defaultValue;
	    }
	    return val;
	  }
}
