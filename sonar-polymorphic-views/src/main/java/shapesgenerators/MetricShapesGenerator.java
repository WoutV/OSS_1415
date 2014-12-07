package shapesgenerators;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import properties.ColorProperty;
import properties.Property;
import properties.ValueProperty;
import shapes.BoxFactory;
import shapes.CircleFactory;
import shapes.Shape;
import shapes.ShapeFactory;
import shapes.ShapeType;
import shapes.TrapezoidFactory;
import utility.MeasureFetcher;
import utility.Util;

/**
 * This class is used to generate lists of shapes, based on a set of given
 * properties. Each list returned is used to draw a polymorphic represenation of
 * the analyzed project.
 * 
 * @author wout
 *
 */
public class MetricShapesGenerator implements IShapesGenerator{

	private ShapeFactory trapFactory;
	private ShapeFactory circleFactory;
	private ShapeFactory boxFactory;
	private Shape[] shapes;
	private ShapeType[] order;
	private double[] thresh;
	private final static double MIN_SIZE = 5;
	private final static double MAX_SIZE = 100;

	/**
	 * @param shapeDeterminingMetric - Property which contains metric values that are used to determine color
	 * @param thresh - String which contains the values that determine which shape should be used (format :25x30 if order contains 3 elements
	 * @param order - String which contains the order of the shapes separated by a '-'
	 */
	public MetricShapesGenerator(MeasureFetcher measureFetcher, PolymorphicChartParameters polyParams) {
		//Take default values here
		Property<Double> width = new ValueProperty(polyParams.getBoxWidth(), PolymorphicChartParameters.DEFAULT_BOXWIDTH, measureFetcher);
		Property<Double> height = new ValueProperty(polyParams.getBoxHeight(), PolymorphicChartParameters.DEFAULT_BOXHEIGHT, measureFetcher);
		Property<Color> color = new ColorProperty(polyParams.getBoxColor(), PolymorphicChartParameters.DEFAULT_BOXCOLOR, measureFetcher);
		
		Property<Double> shapeDeterminingMetric = new ValueProperty(polyParams.getShapeMetric(),PolymorphicChartParameters.DEFAULT_SHAPEMETRIC, measureFetcher);
		List<String> names = measureFetcher.getResourceNames();
		List<String> keys = measureFetcher.getResourceKeys();
		
		this.order = convertToShapeType(polyParams.getShapeMetricOrder().split("-"));
		this.thresh = stringArrayToIntArray(polyParams.getShapeMetricSplit().split("x"));
		this.trapFactory = new TrapezoidFactory();
		this.circleFactory = new CircleFactory();
		this.boxFactory = new BoxFactory();
		initShapes(height, width, color, names, shapeDeterminingMetric, keys);
		
	}
    // TODO hier veel argumenten
	private void initShapes(Property<Double> height, Property<Double> width, Property<Color> color, List<String> names, Property<Double> shapeDeterminingMetric, List<String> keys){
		List<Double> widthList = Util.scaleList(width.getValues(),MIN_SIZE,MAX_SIZE);
		List<Double> heightList = Util.scaleList(height.getValues(),MIN_SIZE,MAX_SIZE);
		List<Color> colorList = color.getValues();
		List<Double> metric = shapeDeterminingMetric.getValues();
		for(int i = 0;i<shapes.length;i++) {
			ShapeType type = determineType (metric.get(i));
			ShapeFactory sf = null;
			if(type.equals(ShapeType.BOX)){
				sf = this.boxFactory;
			}
			else if (type.equals(ShapeType.TRAPEZOID)){
				sf = this.trapFactory;
			}
			else{
				sf = this.circleFactory;
			}
			shapes[i] = sf.createShape(heightList.get(i), widthList.get(i), keys.get(i), names.get(i), colorList.get(i));
		}
		
	}
		
	public Shape[] getShapes () {
		return this.shapes;
	}

	private ShapeType determineType (double value){
		for (int i=0;i<thresh.length;i++){
			if(value <= thresh[i]){
				return order[i];
			}
		}
		return order[order.length-1];
	}
	
	
	private static double[] stringArrayToIntArray(String[] input) throws IllegalArgumentException{
		try{
			double[] output = new double[input.length];
			for(int i=0;i<input.length;i++){
				output[i] = Double.parseDouble(input[i]);
			}
			return output;
		}
		catch(NumberFormatException nfe){
			throw new IllegalArgumentException("invalid values for shapemetricsplit");
		}
	}

	public ShapeType[] convertToShapeType (String[] stringTypes){
		ShapeType[] result = new ShapeType[stringTypes.length];
		int i = 0;
		for (String stringType: stringTypes){
			result[i] = ShapeType.fromString(stringType);
			i++;
		}
		return result;
	}
}
