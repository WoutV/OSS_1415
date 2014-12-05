package shapes;

import chartbuilder.ChartBuilder;
/**
 * 
 * @author Maxim
 *
 */
public class Trapezoid extends Shape{

	private double secondHeight;
	
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
