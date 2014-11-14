package be.kuleuven.cs.oss.polymorphicviews.plugin;

import java.util.Map;

import org.sonar.api.charts.ChartParameters;

public class PolymorphicChartParameters extends ChartParameters {

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

	public static final String DEFAULT_RESOURCES = "CLA";
	public static final String DEFAULT_TYPE = "scatter"; 
	public static final String DEFAULT_BOXWIDTH = "20"; //TODO AANPASSSEN
	public static final String DEFAULT_BOXHEIGHT = "20"; // TODO aanpassen
	public static final String DEFAULT_BOXCOLOR = "255255255";
	public static final String DEFAULT_SIZE = "480x480"; //TODO aanpassen




	//Constructors
	public PolymorphicChartParameters(Map<String, String> params) {
		super(params);
	}
	public PolymorphicChartParameters(String queryString) {
		super(queryString);
	}


	/**
	 * @return resources
	 */
	public String getResources() {
		String result = getValue(PARAM_RESOURCES,DEFAULT_RESOURCES,false);
		return result;
	}

	/**
	 * @return parent
	 * TODO default toevoegen
	 */
	public String getParent(){
		String result = getValue(PARAM_PARENT);
		return result;
	}
	
	/**
	 * @return
	 * TODO default toevoegen
	 */
	public String getXMetric(){
		String result = getValue(PARAM_XMETRIC);
		return result;
	}
	
	public String getYMetric(){
		String result = getValue(PARAM_YMETRIC);
		return result;
	}
	
	public String getType(){
		String result = getValue(PARAM_TYPE, DEFAULT_TYPE, false);
		return result;
	}
	
	public int getWidth(){
		String width = getValues(PARAM_SIZE, "x")[0];
		return Integer.parseInt(width);
	}
	
	public int getHeigth(){
		String heigth = getValues(PARAM_SIZE, "x")[1];
		return Integer.parseInt(heigth);
	}

	public String getBoxWidth(){
		String result = getValue(PARAM_BOXWIDTH, DEFAULT_BOXWIDTH, false);
		return result;
	}
	
	public String getSize(){
		String result = getValue(PARAM_SIZE, DEFAULT_SIZE,false);
		return result;
	}
	
	public String getBoxHeight(){
		String result = getValue(PARAM_BOXHEIGHT, DEFAULT_BOXHEIGHT, false);
		return result;
	}
	
	public String getBoxColor(){
		String result = getValue(PARAM_BOXCOLOR, DEFAULT_BOXCOLOR, false);
		return result;
	}
}
