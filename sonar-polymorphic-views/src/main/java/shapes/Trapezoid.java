package shapes;

import java.awt.Color;

import chartbuilder.ChartBuilder;
/**
 * 
 * @author Maxim
 *
 */
public class Trapezoid extends Shape{

	public Trapezoid(double width, double height, String key, String name,
			Color color) {
		super(width, height, key, name, color);
	}

	public Trapezoid(double width, double height, double height2, String key, String name,
			Color color) {
		super(width, height, key, name, color);
		this.secondHeight=height2;
	}
	
	private double secondHeight;
	
	@Override
	public double getHeight() {
		return Math.max(this.height	, secondHeight);
	}
	
	public void setSecondHeight(double height2){
		this.secondHeight=height2;
	}

	public double getSecondHeight(){
		return secondHeight;
	}
	@Override
	public void draw(ChartBuilder builder) {
		builder.createRightAngledTrapezoid(getxPos(), getyPos(), (int)getHeight(), (int)getWidth(), (int)getSecondHeight(), getColor(), getName());
		
	}

}
