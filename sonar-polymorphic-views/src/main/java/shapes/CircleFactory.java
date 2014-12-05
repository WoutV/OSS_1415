package shapes;

import java.awt.Color;


public class CircleFactory implements ShapeFactory{


	public CircleFactory() {
	}
	
	public Circle makeCircle(double height, double width, int x, int y, long key, String label,Color color){
		Circle circle = new Circle();
		circle.setHeight(height);
		circle.setWidth(width);
		circle.setxPos(x);
		circle.setyPos(y);
		circle.setKey(key);
		circle.setName(label);
		circle.setColor(color);
		return circle;
	}

	@Override
	public Shape createShape(double height, double width, int x, int y,
			long key, String label, Color color) {
		Circle circle = new Circle();
		circle.setHeight(height);
		circle.setWidth(width);
		circle.setxPos(x);
		circle.setyPos(y);
		circle.setKey(key);
		circle.setName(label);
		circle.setColor(color);
		return circle;
	}


}
