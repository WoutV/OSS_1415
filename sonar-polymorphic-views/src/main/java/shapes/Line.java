package shapes;

import java.awt.Color;

import chartbuilder.ChartBuilder;
/**
 * 
 * @author Maxim
 *
 */
public class Line extends Shape{


	public Line(double width, double height, String key, String name,
			Color color) {
		super(width, height, key, name, color);
	}

	@Override
	public void draw(ChartBuilder builder) {
		int secondX=(int)this.getWidth()+this.getxPos();
		int secondY=(int)this.getHeight()+this.getyPos();
		builder.createLine(this.getxPos(),this.getyPos(), secondX, secondY, null);
	}
}
