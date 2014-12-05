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

	
	public Trapezoid makeTrapezoid(double height, double width, double secondHeight, int x, int y, long key, String label, Color color){
		Trapezoid trapezoid = new Trapezoid();
		trapezoid.setHeight(height);
		trapezoid.setWidth(width);
		trapezoid.setSecondHeight(secondHeight);
		trapezoid.setxPos(x);
		trapezoid.setyPos(y);
		trapezoid.setKey(key);
		trapezoid.setName(label);
		trapezoid.setColor(color);
		return trapezoid;
	}


	@Override
	public Shape createShape(double height, double width, int x, int y,
			long key, String label, Color color) {
		Trapezoid trapezoid = new Trapezoid();
		trapezoid.setHeight(height);
		trapezoid.setWidth(width);
		trapezoid.setxPos(x);
		trapezoid.setyPos(y);
		trapezoid.setKey(key);
		trapezoid.setName(label);
		trapezoid.setColor(color);
		return null;
	}
}
