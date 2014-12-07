package shapesgenerators;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import properties.Property;
import shapes.Shape;
import shapes.ShapeType;
import utility.Util;

/**
 * This class is used to generate lists of shapes, based on a set of given
 * properties. Each list returned is used to draw a polymorphic represenation of
 * the analyzed project.
 * 
 * @author wout
 *
 */
public class ShapeListGenerator {
	private final static double MIN_SIZE = 5;
	private final static double MAX_SIZE = 100;

	/**
	 * @param shapeWidth - Property voor breedte shape
	 * @param shapeHeight - Property voor bepalen hoogte
	 * @param colorProperty - Property voor bepalen kleur
	 * @param shapeDeterminingMetric - Property which contains metric values that are used to determine color
	 * @param thresh - String which contains the values that determine which shape should be used (format :25x30 if order contains 3 elements
	 * @param order - String which containts the order of the shapes separated by a '-'
	 */
	public ShapeListGenerator() {
		//TODO aanpassen
	}

	public List<Shape> getShapes () {
		List<Shape> result = new ArrayList<Shape>();

		//TODO lijst vullen met juiste shapes
		//TODO gebruik maken van factory. Maar die kan enkel shapes maken.

		return result;
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
