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

	public Box makeBox(double height, double width, int x, int y, long key, String label, Color color){
		Box box = new Box();
		box.setHeight(height);
		box.setWidth(width);
		box.setxPos(x);
		box.setyPos(y);
		box.setKey(key);
		box.setName(label);
		box.setColor(color);
		return box;
	}

	@Override
	public Shape createShape(double height, double width, int x, int y,
			long key, String label, Color color) {
		Box box = new Box();
		box.setHeight(height);
		box.setWidth(width);
		box.setxPos(x);
		box.setyPos(y);
		box.setKey(key);
		box.setName(label);
		box.setColor(color);
		return box;
	}
}
