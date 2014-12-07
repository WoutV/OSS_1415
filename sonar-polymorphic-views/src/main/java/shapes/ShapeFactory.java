package shapes;

import java.awt.Color;


/**
 * Abstract Factory class
 * @author Maxim
 *
 */
public interface ShapeFactory {


	public Shape createShape(double height, double width, String key, String label ,Color color)
	;//{
//		Shape shape = new Shape();
//		shape.setHeight(height);
//		shape.setWidth(width);
//		shape.setxPos(x);
//		shape.setyPos(y);
//		shape.setKey(key);
//		shape.setName(label);
//		return shape;
//	}



}
