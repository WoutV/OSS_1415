package shapes;

import java.awt.Color;

import chartbuilder.ChartBuilder;

/**
 * This class represents boxes that can be used to draw a graph. Each box has its own dimensions.
 * @author Wout
 *
 */
public class Box extends Shape {
	

	public Box(double width, double height, String key, String name, Color color) {
		super(width, height, key, name, color);
	}

	@Override
	public void draw(ChartBuilder builder) {
		builder.createRectangle(this.getxPos(),this.getyPos(), (int) this.getHeight(),(int) this.getWidth(), this.getColor(), this.getName());
	}
}
