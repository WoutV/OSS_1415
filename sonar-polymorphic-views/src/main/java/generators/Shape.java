package generators;

import java.awt.Color;

import chartbuilder.ChartBuilder;


/**
 * This class represents a shape that can be used to construct graphs. Each shape has its own name and color.
 * @author Wout
 *
 */
public class Shape {
	
	//TODO Line moet ook overerven van shape denk ik . dan kunt ge da veel gemakkelijker tekenen, gewoon overerven van draw en buildermeegeven
	
	private int xPos;
	private int yPos;
	private String name;
	private Color color;

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

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

	public void draw(ChartBuilder builder) {
		// subclass should provide implementation
	}
}