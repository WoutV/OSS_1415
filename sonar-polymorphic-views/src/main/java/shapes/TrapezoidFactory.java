package shapes;

import java.awt.Color;

/**
 * 
 * @author Maxim
 *
 */
public class TrapezoidFactory implements ShapeFactory{

	
	
	public TrapezoidFactory() {
		}


	@Override
	public Trapezoid createShape(double height, double width, 
			String key, String label, Color color) {
		Trapezoid trapezoid = new Trapezoid();
		trapezoid.setHeight1(height);
		trapezoid.setSecondHeight(0.8*height);
		trapezoid.setWidth(width);
		trapezoid.setKey(key);
		trapezoid.setName(label);
		trapezoid.setColor(color);
		return trapezoid;
	}
}
