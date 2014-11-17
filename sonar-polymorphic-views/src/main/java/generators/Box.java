package generators;



/**
 * This class represents boxes that can be used to draw a graph. Each box has its own dimensions.
 * @author Wout
 *
 */
public class Box extends Shape {
	private double width;
	private double height;
	
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
}
