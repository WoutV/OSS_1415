package chartbuilderTest;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import chartbuilder.Java2DBuilder;
import chartbuilder.LineType;

@RunWith(JUnit4.class)
public class BuilderScenario {
	
	public Java2DBuilder builder;
	
	public BuilderScenario(){
		builder = new Java2DBuilder();
	}
	

	@Test
	public void testScenarioScatterPlot() throws IOException{
		builder.createCanvas(600,600, BufferedImage.TYPE_INT_RGB);
		builder.createXAxis("Lines of code", 0, 600, 0, 0 , 2000);
		builder.createYAxis("Number of methods", 0, 600, 0, 0, 50);
		builder.createRectangle(300, 60, 80, 40, Color.blue, "JAVA2DBUILDER");
		builder.createCircle(50, 50, 50, Color.green, "CirclesGenerator");
		builder.createRightAngledTrapezoid(400, 500, 80, 30, 80, Color.red, "TrapsGenerator");
		File outputfile = new File("scatterexample.png");
	    ImageIO.write(builder.getImage(), "png", outputfile);
	}
	
	@Test
	public void testScenarioSystemComplexityView() throws IOException{
		builder.createCanvas(600,600, BufferedImage.TYPE_INT_RGB);
		builder.createLine(300, 600, 50, 300, LineType.SOLID);
		builder.createLine(300, 600, 200, 300, LineType.SOLID);
		builder.createLine(300, 600, 350, 300, null);
		builder.createLine(300, 600, 500, 300, null);
		builder.createRectangle(300, 600, 80, 100, Color.blue, "Shape");
		builder.createCircle(50, 300, 50, Color.green, "Circle");
		builder.createRightAngledTrapezoid(350, 300, 100, 30, 80, Color.orange, "Trapezoid");
		builder.createRectangle(200, 300, 130, 70, Color.red, "Box");
		builder.createRectangle(500, 300, 60, 60, Color.yellow, "Line");
		File outputfile = new File("syscomexample.png");
	    ImageIO.write(builder.getImage(), "png", outputfile);
	}
}
