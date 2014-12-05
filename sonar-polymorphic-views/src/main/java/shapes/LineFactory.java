package shapes;

import java.awt.Color;


/**
 * 
 * @author Maxim
 *
 */
public class LineFactory implements ShapeFactory{

	public LineFactory() {	
	}
	
	public Line makeLine(double height, double width, int x, int y, String label,Color color){
		Line line = new Line();
		line.setHeight(height);
		line.setWidth(width);
		line.setxPos(x);
		line.setyPos(y);
		line.setName(label);
		line.setColor(color);
		return line;
	}

	@Override
	public Shape createShape(double height, double width, int x, int y,
			long key, String label, Color color) {
		Line line = new Line();
		line.setHeight(height);
		line.setWidth(width);
		line.setxPos(x);
		line.setyPos(y);
		line.setName(label);
		line.setColor(color);
		return line;
	}

}
