package shapes;

import chartbuilder.ChartBuilder;
/**
 * 
 * @author Maxim
 *
 */
public class Trapezoid extends Shape{

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

	public void setHeight1(double height) {
		this.height = height;
	}

}
