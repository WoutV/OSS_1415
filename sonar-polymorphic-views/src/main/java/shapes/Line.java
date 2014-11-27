package shapes;

import chartbuilder.ChartBuilder;

public class Line extends Shape{


	@Override
	public void draw(ChartBuilder builder) {
		int secondX=(int)this.getWidth()+this.getxPos();
		int secondY=(int)this.getHeight()+this.getyPos();
		builder.createLine(this.getxPos(),this.getyPos(), secondX, secondY, null);
	}
}
