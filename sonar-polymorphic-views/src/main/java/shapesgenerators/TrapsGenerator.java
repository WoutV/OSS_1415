package shapesgenerators;

import java.awt.Color;
import java.util.List;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import properties.ColorProperty;
import properties.Property;
import properties.ValueProperty;
import shapes.Shape;
import shapes.TrapezoidFactory;
import utility.MeasureFetcher;
import utility.Util;

/**
 * This class is used to generate lists of boxes with a certain width, height and color.
 * @author Thijs&Wout
 *
 */

public class TrapsGenerator implements IShapesGenerator {
	
	private final static double MIN_SIZE = 5;
	private final static double MAX_SIZE = 100;
	private Shape[] shapes;
	private TrapezoidFactory trapFactory;
	
	
	public TrapsGenerator(MeasureFetcher measureFetcher, PolymorphicChartParameters polyParams) {
		Property<Double> width = new ValueProperty(polyParams.getTrapSide1(), PolymorphicChartParameters.DEFAULT_TRAPSIDE1, measureFetcher);
		Property<Double> height = new ValueProperty(polyParams.getTrapSide2(), PolymorphicChartParameters.DEFAULT_TRAPSIDE2, measureFetcher);
		Property<Double> height2 = new ValueProperty(polyParams.getTrapSide3(), PolymorphicChartParameters.DEFAULT_TRAPSIDE3, measureFetcher);
		Property<Color> color = new ColorProperty(polyParams.getBoxColor(), PolymorphicChartParameters.DEFAULT_BOXCOLOR, measureFetcher);
		List<String> names = measureFetcher.getResourceNames();
		List<String> keys = measureFetcher.getResourceKeys();
		int numberOfShapes = names.size();
		this.shapes = new Shape[numberOfShapes];
		this.trapFactory = new TrapezoidFactory();
		initShapes(width, height, height2, color,names,keys);
		}

	/**
	 * This method initializes the list of shapes with "empty" boxes
	 */
	private void initShapes(Property<Double> width,Property<Double> height,Property<Double> height2,Property<Color> color, List<String> names, List<String> keyList) {
		List<Double> widthList = Util.scaleList(width.getValues(),MIN_SIZE,MAX_SIZE);
		List<Double> heightList = Util.scaleList(height.getValues(),MIN_SIZE,MAX_SIZE);
		List<Double> height2List = Util.scaleList(height2.getValues(),MIN_SIZE,MAX_SIZE);
		List<Color> colorList = color.getValues();
		for(int i = 0;i<shapes.length;i++) {
			shapes[i] = trapFactory.makeTrapezoid(heightList.get(i),widthList.get(i), height2List.get(i), keyList.get(i), names.get(i), colorList.get(i));
		}
	}

	@Override
	public Shape[] getShapes() {
		return this.shapes;
	}
	

}