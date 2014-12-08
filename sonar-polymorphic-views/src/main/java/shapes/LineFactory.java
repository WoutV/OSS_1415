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

	@Override
	public Shape createShape(double height, double width,
			String key, String label, Color color) {
		Line line = new Line();
		line.setHeight(height);
		line.setWidth(width);
		line.setKey(key);	
		line.setName(label);
		line.setColor(color);
		return line;
	}

}
