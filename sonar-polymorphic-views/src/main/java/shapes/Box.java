package shapes;

import chartbuilder.ChartBuilder;

/**
 * This class represents boxes that can be used to draw a graph. Each box has its own dimensions.
 * @author Wout
 *
 */
public class Box extends Shape {
	

	@Override
	public void draw(ChartBuilder builder) {
		builder.createRectangle(this.getxPos(),this.getyPos(), (int) this.getHeight(),(int) this.getWidth(), this.getColor(), this.getName());
	}
}
