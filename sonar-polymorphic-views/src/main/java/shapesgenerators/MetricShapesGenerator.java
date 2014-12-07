package shapesgenerators;


import java.util.List;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import properties.Property;
import properties.ValueProperty;
import shapes.Shape;
import shapes.ShapeType;
import utility.MeasureFetcher;

/**
 * This class is used to generate lists of shapes, based on a set of given
 * properties. Each list returned is used to draw a polymorphic represenation of
 * the analyzed project.
 * 
 * @author wout
 *
 */
public class MetricShapesGenerator implements IShapesGenerator{


	private Shape[] shapes;
	private ShapeType[] order;
	private double[] thresh;
	private BoxesGenerator bg;
	private CirclesGenerator cg;
	private TrapsGenerator tg;

	/**
	 * @param shapeDeterminingMetric - Property which contains metric values that are used to determine color
	 * @param thresh - String which contains the values that determine which shape should be used (format :25x30 if order contains 3 elements
	 * @param order - String which contains the order of the shapes separated by a '-'
	 */
	public MetricShapesGenerator(MeasureFetcher measureFetcher, PolymorphicChartParameters polyParams) {
		
		this.bg = new BoxesGenerator(measureFetcher, polyParams);
		this.cg = new CirclesGenerator(measureFetcher, polyParams);
		this.tg = new TrapsGenerator(measureFetcher, polyParams);
	
		Property<Double> shapeDeterminingMetric = new ValueProperty(polyParams.getShapeMetric(),PolymorphicChartParameters.DEFAULT_SHAPEMETRIC, measureFetcher);
		
		this.order = convertToShapeType(polyParams.getShapeMetricOrder().split("-"));
		this.thresh = stringArrayToIntArray(polyParams.getShapeMetricSplit().split("x"));
		initShapes(shapeDeterminingMetric);
		
	}
	private void initShapes(Property<Double> shapeDeterminingMetric){
		List<Double> metric = shapeDeterminingMetric.getValues();
		this.shapes = new Shape[metric.size()];
		for(int i = 0;i<metric.size();i++) {
			ShapeType type = determineType (metric.get(i));
			IShapesGenerator sg = null;
			if(type.equals(ShapeType.BOX)){
				sg = bg;
			}
			else if (type.equals(ShapeType.TRAPEZOID)){
				sg = tg;
			}
			else{
				sg = cg;
			}
			shapes[i] = sg.getShapes()[i];
		}
		
	}

	public Shape[] getShapes () {
		return this.shapes;
	}

	private ShapeType determineType (double value){
		for (int i=0;i<thresh.length;i++){
			if(value <= thresh[i]){
				System.out.println("Value "+value+"below "+ thresh[i] + "type : " + order[i]);
				return order[i];
			}
		}
		System.out.println("Value "+value+"above "+ thresh[thresh.length-1] + "type : " + order[order.length-1]);

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
