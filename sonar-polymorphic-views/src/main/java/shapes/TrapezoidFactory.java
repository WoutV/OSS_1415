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

	
	public Trapezoid makeTrapezoid(double height, double width, double secondHeight,String key, String label, Color color){
		Trapezoid trapezoid = new Trapezoid();
		trapezoid.setHeight(height);
		trapezoid.setWidth(width);
		trapezoid.setSecondHeight(secondHeight);
		trapezoid.setKey(key);
		trapezoid.setName(label);
		trapezoid.setColor(color);
		return trapezoid;
	}


	@Override
	public Shape createShape(double height, double width, 
			String key, String label, Color color) {
		Trapezoid trapezoid = new Trapezoid();
		trapezoid.setHeight(height);
		trapezoid.setSecondHeight(0.8*height);
		trapezoid.setWidth(width);
		trapezoid.setKey(key);
		trapezoid.setName(label);
		trapezoid.setColor(color);
		return trapezoid;
	}
}
