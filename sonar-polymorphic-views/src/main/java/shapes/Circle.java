package shapes;

import chartbuilder.ChartBuilder;
/**
 * The class representing Circle objects in the representation of data
 * 
 * @author Maxim
 *
 */
public class Circle extends Shape{

	@Override
	public void draw(ChartBuilder builder) {
		builder.createCircle(getxPos(), getyPos(), (int) getHeight(), getColor(), getName());
		
	}

}
