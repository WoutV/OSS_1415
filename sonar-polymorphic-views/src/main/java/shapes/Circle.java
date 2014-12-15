package shapes;

import java.awt.Color;

import chartbuilder.ChartBuilder;
/**
 * The class representing Circle objects in the representation of data
 * 
 * @author Maxim
 *
 */
public class Circle extends Shape{

	public Circle(double width, double height, String key, String name,
			Color color) {
		super(width, height, key, name, color);
	}

	@Override
	public void draw(ChartBuilder builder) {
		builder.createCircle(getxPos(), getyPos(), (int) getHeight(), getColor(), getName());
		
	}

}
