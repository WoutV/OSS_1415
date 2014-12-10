package shapes;

import java.awt.Color;


public class CircleFactory implements ShapeFactory{


	public CircleFactory() {
	}

	@Override
	public Shape createShape(double height, double width,
			String key, String label, Color color) {
		Circle circle = new Circle();
		circle.setHeight(height);
		circle.setWidth(width);
		circle.setKey(key);
		circle.setName(label);
		circle.setColor(color);
		return circle;
	}
}
