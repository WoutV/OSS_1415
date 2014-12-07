package be.kuleuven.cs.oss.polymorphicviews.plugin;


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
	public static final String PARAM_SHAPE = "shape";
	public static final String PARAM_CIRLCEDIAM = "circlediam";
	public static final String PARAM_TRAPSIDE1 = "trapside1";
	public static final String PARAM_TRAPSIDE2 = "trapside2";
	public static final String PARAM_TRAPSIDE3 = "trapside3";
	public static final String PARAM_SHAPEMETRIC = "shapemetric";
	public static final String PARAM_SHAPEMETRICORDER = "shapemetricorder";
	public static final String PARAM_SHAPEMETRICSPLIT = "shapemetricsplit";
	
	public static final String DEFAULT_SHAPEMETRICORDER = "box-circle-trap-circle";
	public static final String DEFAULT_SHAPEMETRICSPLIT = "20x200x2000";
	public static final String DEFAULT_TRAPSIDE1 = "13";
	public static final String DEFAULT_TRAPSIDE2 = "13";
	public static final String DEFAULT_TRAPSIDE3 = "10";
	public static final String DEFAULT_CIRLCEDIAM = "13";
	public static final String DEFAULT_SHAPE = "box";
	public static final String DEFAULT_RESOURCES = "classes";
	public static final String DEFAULT_TYPE = "scatter"; 
	public static final String DEFAULT_BOXWIDTH = "13";
	public static final String DEFAULT_BOXHEIGHT = "13";
	public static final String DEFAULT_BOXCOLOR = "r255g255b255"; 
	public static final String DEFAULT_SIZE = "800x800";
	
	public static String DEFAULT_PARENT;
	public static String DEFAULT_XMETRIC;
	public static String DEFAULT_YMETRIC;
	public static String DEFAULT_SHAPEMETRIC = DEFAULT_XMETRIC;
	private ChartParameters chartParameters;

	
	/**
	 * Generates a polymorphicChartParameters object, with the same properties as the given ChartParameters
	 * @param p
	 */
	public PolymorphicChartParameters(ChartParameters p) {
		this.chartParameters = p;
	}

	public String getShapeMetricOrder(){
		String result = chartParameters.getValue(PARAM_SHAPEMETRICORDER, DEFAULT_SHAPEMETRICORDER,false);
		return result;
	}
	
	public String getShapeMetricSplit(){
		String result = chartParameters.getValue(PARAM_SHAPEMETRICSPLIT, DEFAULT_SHAPEMETRICSPLIT,false);
		return result;
	}
	
	public String getShapeMetric(){
		String result = chartParameters.getValue(PARAM_SHAPEMETRIC, DEFAULT_SHAPEMETRIC,false);
		return result;
	}
	/**
	 * @return resources
	 */
	public String getResources() {
		String result = chartParameters.getValue(PARAM_RESOURCES,DEFAULT_RESOURCES,false);
		return result;
	}

	/**
	 * @return parent
	 */
	public String getParent(){
		String result = chartParameters.getValue(PARAM_PARENT, DEFAULT_PARENT,false);
		return result;
	}
	
	public String getShape(){
		String result = chartParameters.getValue(PARAM_SHAPE, DEFAULT_SHAPE,false);
		return result;
	}
	
	public String getCircleDiam(){
		String result = chartParameters.getValue(PARAM_CIRLCEDIAM, DEFAULT_CIRLCEDIAM,false);
		return result;
	}
	
	public String getTrapSide1(){
		String result = chartParameters.getValue(PARAM_TRAPSIDE1, DEFAULT_TRAPSIDE1,false);
		return result;
	}
	
	public String getTrapSide2(){
		String result = chartParameters.getValue(PARAM_TRAPSIDE2, DEFAULT_TRAPSIDE2,false);
		return result;
	}
	
	public String getTrapSide3(){
		String result = chartParameters.getValue(PARAM_TRAPSIDE3, DEFAULT_TRAPSIDE3,false);
		return result;
	}
	/**
	 * @return xmetric
	 */
	public String getXMetric(){
		String result = chartParameters.getValue(PARAM_XMETRIC, DEFAULT_XMETRIC,false);
		return result;
	}
	
	/**
	 * @return ymetric
	 */
	public String getYMetric(){
		String result = chartParameters.getValue(PARAM_YMETRIC, DEFAULT_YMETRIC,false);
		return result;
	}
	
	public String getType(){
		String result = chartParameters.getValue(PARAM_TYPE, DEFAULT_TYPE,false);
		return result;
	}

	public String getBoxWidth(){
		String result = chartParameters.getValue(PARAM_BOXWIDTH, DEFAULT_BOXWIDTH,false);
		return result;
	}
	
	public String getSize(){
		String result = chartParameters.getValue(PARAM_SIZE, DEFAULT_SIZE,false);
		return result;
	}
	
	public String getBoxHeight(){
		String result = chartParameters.getValue(PARAM_BOXHEIGHT, DEFAULT_BOXHEIGHT,false);
		return result;
	}
	
	public String getBoxColor(){
		String result = chartParameters.getValue(PARAM_BOXCOLOR, DEFAULT_BOXCOLOR,false);
		return result;
	}

}