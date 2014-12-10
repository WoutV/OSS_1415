package shapes;

import java.awt.Color;


/**
 * Abstract Factory class
 * @author Maxim
 *
 */
public interface ShapeFactory {


	public Shape createShape(double height, double width, String key, String label ,Color color);



}
