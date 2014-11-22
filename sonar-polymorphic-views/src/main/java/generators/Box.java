package generators;

import chartbuilder.ChartBuilder;

/**
 * This class represents boxes that can be used to draw a graph.
 *
 */
public class Box extends Shape {
	

	@Override
	public void draw(ChartBuilder builder) {
		builder.createRectangle(this.getxPos(),this.getyPos(), (int) this.getHeight(),(int) this.getWidth(), this.getColor(), this.getName());
	}
}
