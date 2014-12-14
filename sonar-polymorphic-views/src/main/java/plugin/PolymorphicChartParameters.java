package plugin;


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
	public static final String PARAM_CIRCLECOLOR = "circlecolor";
	public static final String PARAM_TRAPCOLOR = "trapcolor";
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
	public static final String DEFAULT_TRAPSIDE1 = "14";
	public static final String DEFAULT_TRAPSIDE2 = "15";
	public static final String DEFAULT_TRAPSIDE3 = "9";
	public static final String DEFAULT_CIRCLEDIAM = "12";
	public static final String DEFAULT_SHAPE = "box";
	public static final String DEFAULT_RESOURCES = "classes";
	public static final String DEFAULT_TYPE = "scatter"; 
	public static final String DEFAULT_BOXWIDTH = "13";
	public static final String DEFAULT_BOXHEIGHT = "13";
	public static final String DEFAULT_BOXCOLOR = "r255g0b0";
	public static final String DEFAULT_TRAPCOLOR = "r0g0b255"; 
	public static final String DEFAULT_CIRCLECOLOR = "r255g255b0"; 
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
		return chartParameters.getValue(PARAM_SHAPEMETRICORDER, DEFAULT_SHAPEMETRICORDER,false);
	}
	
	public String getShapeMetricSplit(){
		return chartParameters.getValue(PARAM_SHAPEMETRICSPLIT, DEFAULT_SHAPEMETRICSPLIT,false);
	}
	
	public String getShapeMetric(){
		return chartParameters.getValue(PARAM_SHAPEMETRIC, DEFAULT_SHAPEMETRIC,false);
	}
	/**
	 * @return resources
	 */
	public String getResources() {
		return chartParameters.getValue(PARAM_RESOURCES,DEFAULT_RESOURCES,false);
	}

	/**
	 * @return parent
	 */
	public String getParent(){
		return chartParameters.getValue(PARAM_PARENT, DEFAULT_PARENT,false);
	}
	
	public String getShape(){
		return chartParameters.getValue(PARAM_SHAPE, DEFAULT_SHAPE,false);
	}
	
	public String getCircleDiam(){
		return chartParameters.getValue(PARAM_CIRLCEDIAM, DEFAULT_CIRCLEDIAM,false);
	}
	
	public String getTrapSide1(){
		return chartParameters.getValue(PARAM_TRAPSIDE1, DEFAULT_TRAPSIDE1,false);
	}
	
	public String getTrapSide2(){
		return chartParameters.getValue(PARAM_TRAPSIDE2, DEFAULT_TRAPSIDE2,false);
	}
	
	public String getTrapSide3(){
		return chartParameters.getValue(PARAM_TRAPSIDE3, DEFAULT_TRAPSIDE3,false);
	}
	/**
	 * @return xmetric
	 */
	public String getXMetric(){
		return chartParameters.getValue(PARAM_XMETRIC, DEFAULT_XMETRIC,false);
	}
	
	/**
	 * @return ymetric
	 */
	public String getYMetric(){
		return chartParameters.getValue(PARAM_YMETRIC, DEFAULT_YMETRIC,false);
	}
	
	public String getType(){
		return chartParameters.getValue(PARAM_TYPE, DEFAULT_TYPE,false);
	}

	public String getBoxWidth(){
		return chartParameters.getValue(PARAM_BOXWIDTH, DEFAULT_BOXWIDTH,false);
	}
	
	public String getSize(){
		return chartParameters.getValue(PARAM_SIZE, DEFAULT_SIZE,false);
	}
	
	public String getBoxHeight(){
		return chartParameters.getValue(PARAM_BOXHEIGHT, DEFAULT_BOXHEIGHT,false);
	}
	
	public String getBoxColor(){
		return chartParameters.getValue(PARAM_BOXCOLOR, DEFAULT_BOXCOLOR,false);
	}
	
	public String getTrapColor(){
		return chartParameters.getValue(PARAM_TRAPCOLOR, DEFAULT_TRAPCOLOR,false);
	}
	
	public String getCircleColor(){
		return chartParameters.getValue(PARAM_CIRCLECOLOR, DEFAULT_CIRCLECOLOR,false);
	}

}