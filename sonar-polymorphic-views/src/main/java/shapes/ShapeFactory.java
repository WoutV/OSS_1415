package shapes;

import java.awt.Color;


/**
 * Abstract Factory class
 * @author Maxim
 *
 */
public interface ShapeFactory {


	/**
	 * Creates a shape based on given arguments
	 * 
	 * @param height
	 * @param width
	 * @param key
	 * @param label
	 * @param color
	 * @return created shape
	 */
	public Shape createShape(double height, double width, String key, String label ,Color color);



}
