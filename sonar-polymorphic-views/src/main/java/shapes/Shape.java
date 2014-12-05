package shapes;

import java.awt.Color;

import chartbuilder.ChartBuilder;


/**
 * This class represents a shape that can be used to construct graphs. Each shape has its own name and color.
 * @author Wout
 *
 */
public abstract class Shape {
	
	private int xPos;
	private int yPos;
	private String name;
	private Color color;
	private double width;
	private double height;
	private long key;
	
	public long getKey() {
		return key;
	}
	public void setKey(long key) {
		this.key = key;
	}
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

	public abstract void draw(ChartBuilder builder);
}