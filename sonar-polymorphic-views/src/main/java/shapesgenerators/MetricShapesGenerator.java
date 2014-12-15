package shapesgenerators;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
 * @author wout&Thijs
 *
 */
public class MetricShapesGenerator implements IShapesGenerator {

	private Map<String,Shape> shapes;
	private ShapeType[] order;
	private double[] thresh;
	private BoxesGenerator bg;
	private CirclesGenerator cg;
	private TrapsGenerator tg;

	/**
	 * @param shapeDeterminingMetric
	 *            - Property which contains metric values that are used to
	 *            determine color
	 * @param thresh
	 *            - String which contains the values that determine which shape
	 *            should be used (format :25x30 if order contains 3 elements
	 * @param order
	 *            - String which contains the order of the shapes separated by a
	 *            '-'
	 */
	public MetricShapesGenerator(MeasureFetcher measureFetcher,
			PolymorphicChartParameters polyParams) {

		this.bg = new BoxesGenerator(measureFetcher, polyParams);
		this.cg = new CirclesGenerator(measureFetcher, polyParams);
		this.tg = new TrapsGenerator(measureFetcher, polyParams);

		Set<String> keys = measureFetcher.getResourceKeysAndNames().keySet();
		Property<Double> shapeDeterminingMetric = new ValueProperty(
				polyParams.getShapeMetric(),
				PolymorphicChartParameters.DEFAULT_SHAPEMETRIC, measureFetcher);

		this.order = convertToShapeType(polyParams.getShapeMetricOrder().split(
				"-"));
		this.thresh = stringArrayToDoubleArray(polyParams.getShapeMetricSplit()
				.split("x"));
		initShapes(shapeDeterminingMetric,keys);

	}

	/**
	 * Initializes all shapes
	 * 
	 * @param shapeDeterminingMetric
	 *            - Metric that determines which shape will be needed
	 */
	private void initShapes(Property<Double> shapeDeterminingMetric,Set<String> keys) {
		Map<String,Double> metric = shapeDeterminingMetric.getMap();
		this.shapes = new HashMap<String,Shape>();
		for (String i : keys) {
			ShapeType type = determineType(metric.get(i));
			IShapesGenerator sg = null;
			if (type.equals(ShapeType.BOX)) {
				sg = bg;
			} else if (type.equals(ShapeType.TRAPEZOID)) {
				sg = tg;
			} else {
				sg = cg;
			}
			shapes.put(i, sg.getShapes().get(i));
		}

	}

	public Map<String,Shape> getShapes() {
		return this.shapes;
	}

	/**
	 * Based on the given value, thresh and order determines which shapetype
	 * is necessary
	 * 
	 * @param value
	 * @return ShapeType determined by the value, order and thresholds
	 */
	private ShapeType determineType(double value) {
		for (int i = 0; i < thresh.length; i++) {
			if (value <= thresh[i]) {
				return order[i];
			}
		}
		return order[order.length - 1];
	}

	/**
	 * @param input
	 *            array of strings which are actually doubles
	 * @return array of converted doubles
	 * @throws IllegalArgumentException
	 *             thrown when the given strings do not represent actual doubles
	 */
	private static double[] stringArrayToDoubleArray(String[] input)
			throws IllegalArgumentException {
		try {
			double[] output = new double[input.length];
			for (int i = 0; i < input.length; i++) {
				output[i] = Double.parseDouble(input[i]);
			}
			return output;
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException(
					"invalid values for shapemetricsplit");
		}
	}

	/**
	 * @param stringTypes
	 *            array of Strings that correspond to a shapetype
	 * @return converted array of shapetypes
	 */
	public ShapeType[] convertToShapeType(String[] stringTypes) {
		ShapeType[] result = new ShapeType[stringTypes.length];
		int i = 0;
		for (String stringType : stringTypes) {
			result[i] = ShapeType.fromString(stringType);
			i++;
		}
		return result;
	}
}
