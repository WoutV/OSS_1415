package generators;

import java.awt.Color;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;

/**
 * This class is used to generate lists of boxes with a certain width, height and color.
 * @author Thijs&Wout
 *
 */

public class BoxGenerator extends ShapeGenerator {
	private final static Logger LOG = LoggerFactory.getLogger(BoxGenerator.class);
	private Box[] shapes;
	

	public BoxGenerator(MeasureFetcher measureFetcher) {
		super(measureFetcher);
		if(measureFetcher.equals(null)) {
		int numberOfShapes = measureFetcher.getNumberOfResources();
		this.shapes= new Box[numberOfShapes];
		nameBoxes();
		}
		initBoxes();
	}

	
	/**
	 * This method initializes the list of shapes with "empty" boxes
	 */
	private void initBoxes() {
		for(int i = 0;i<shapes.length;i++) {
			shapes[i] = new Box();
		}
	}

	/* (non-Javadoc)
	 * @see generators.ShapeGenerator#getBoxes(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Box[] getShapes(String width, String height, String color) {
//		TODO ik denk dat dit de bedoeling was ?
		if(width == null){
			width = PolymorphicChartParameters.DEFAULT_BOXWIDTH;
		}
		if(height == null){
			height = PolymorphicChartParameters.DEFAULT_BOXHEIGHT;
		}
		if(color == null){
			color = PolymorphicChartParameters.DEFAULT_BOXCOLOR;
		}
		List<Double> widthList = getShapeDimension(width);
		List<Double> heightList = getShapeDimension(height);
		List<Color> colorList = getShapeColors(color);
		for (int i = 0; i < shapes.length; i++) {
			shapes[i].setHeight(heightList.get(i));
			shapes[i].setWidth(widthList.get(i));
			shapes[i].setColor(colorList.get(i));
		}
		return shapes;
	}

	

	/**
	 * This method names all the boxes with the correct resource names.
	 */
	private void nameBoxes() {
		List<String> names = measureFetcher.getResourceNames();
		int i = 0;
		for (String s : names) {
			shapes[i].setName(s);
			i++;
		}
	}
}
