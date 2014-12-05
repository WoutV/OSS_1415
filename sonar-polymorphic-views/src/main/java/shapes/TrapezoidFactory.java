package shapes;

import java.awt.Color;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import properties.Property;
import utility.MeasureFetcher;
import utility.Util;

public class TrapezoidFactory extends ShapeFactory{
	private final static Logger LOG = LoggerFactory.getLogger(BoxGenerator.class);
	private final static double MIN_BOX_SIZE = 5;
	private final static double MAX_BOX_SIZE = 100;
	
	
	public TrapezoidFactory(MeasureFetcher measureFetcher, Property<Double> width,Property<Double> height, Property<Color> color) {
		super(measureFetcher, width,height, color);
		int numberOfShapes = measureFetcher.getNumberOfResources();
		this.shapes = new Shape[numberOfShapes];
		initBoxes(width, height,color);
		}

	/**
	 * This method initializes the list of shapes with "empty" boxes
	 */
	private void initTrapezoids(Property<Double> width,Property<Double> height,Property<Color> color) {
		Trapezoid[] trapezoids =  new Trapezoid[shapes.length];
		for(int i = 0;i<trapezoids.length;i++) {
			trapezoids[i] = new Trapezoid();
		}
		this.shapes = trapezoids;
		nameShapes();
		
		setTrapezoidProperties(width, height, color, trapezoids);
	}

	private void setTrapezoidProperties(Property<Double> width, Property<Double> height, Property<Color> color,Box[] boxes) {
		List<Double> widthList;
			widthList = Util.scaleList(width.getValues(),MIN_BOX_SIZE,MAX_BOX_SIZE);
		List<Double> heightList;
			heightList = Util.scaleList(height.getValues(),MIN_BOX_SIZE,MAX_BOX_SIZE);
		List<Color> colorList = color.getValues();
		for (int i = 0; i < boxes.length-1; i++) {
			boxes[i].setHeight(heightList.get(i));
			boxes[i].setWidth(widthList.get(i));
			boxes[i].setColor(colorList.get(i));
		}
	}
}
