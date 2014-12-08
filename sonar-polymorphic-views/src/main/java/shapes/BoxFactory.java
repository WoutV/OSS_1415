package shapes;

import java.awt.Color;


/**
 * This class is used to create lists of boxes with a certain width, height and color.
 * @author Maxim&Thijs&Wout
 *
 */
public class BoxFactory implements ShapeFactory {

	
	
	public BoxFactory() {

		}

	@Override
	public Shape createShape(double height, double width,
			String key, String label, Color color) {
		Box box = new Box();
		box.setHeight(height);
		box.setWidth(width);
		box.setKey(key);
		box.setName(label);
		box.setColor(color);
		return box;
	}
}
