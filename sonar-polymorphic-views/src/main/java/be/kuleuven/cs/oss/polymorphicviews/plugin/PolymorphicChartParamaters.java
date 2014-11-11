package be.kuleuven.cs.oss.polymorphicviews.plugin;

import java.util.Map;

import org.sonar.api.charts.ChartParameters;

import be.kuleuven.cs.oss.sonarfacade.ResourceQualifier;

public class PolymorphicChartParamaters extends ChartParameters {

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




	//Constructors
	public PolymorphicChartParamaters(Map<String, String> params) {
		super(params);
	}
	public PolymorphicChartParamaters(String queryString) {
		super(queryString);
	}


	/**
	 * @return resources
	 */
	public ResourceQualifier getResources() {
		ResourceQualifier result = ResourceQualifier.fromString(getValue(PARAM_RESOURCES,DEFAULT_RESOURCES,false));
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

	public int getBoxWidth(){
		String result = getValue(PARAM_BOXWIDTH, DEFAULT_BOXWIDTH, false);
		return Integer.parseInt(result);
	}
	
	public int getBoxHeight(){
		String result = getValue(PARAM_BOXHEIGHT, DEFAULT_BOXHEIGHT, false);
		return Integer.parseInt(result);
	}
	
	//TODO uitbreiden naar grijstinten
	public int getBoxColor(){
		String result = getValue(PARAM_BOXCOLOR, DEFAULT_BOXCOLOR, false);
		return Integer.parseInt(result);
	}
}
