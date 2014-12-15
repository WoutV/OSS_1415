package shapes;

import java.awt.Color;

import chartbuilder.ChartBuilder;


/**
 * This class represents a shape that can be used to construct graphs. Each shape has its own name and color.
 * @author Wout
 *
 */
public abstract class Shape {
	
	public Shape(double width, double height, String key, String name, Color color){
		this.width = width;
		this.height = height;
		this.key=key;
		this.name=name;
		this.color=color;
	}
	private int xPos;
	private int yPos;
	private String name;
	private Color color;
	private double width;
	protected double height;
	private String key;
	
	public String getKey() {
		return key;
	}
	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
	
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
	
	public String getName() {
		return name;
	}


	public Color getColor() {
		return color;
	}

	public abstract void draw(ChartBuilder builder);
}