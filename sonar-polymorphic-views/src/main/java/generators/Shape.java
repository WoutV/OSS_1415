package generators;

import java.awt.Color;


/**
 * This class represents a shape that can be used to construct graphs. Each shape has its own name and color.
 * @author Wout
 *
 */
public class Shape {

	private String name;
	private Color color;

	public Shape() {
		super();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}