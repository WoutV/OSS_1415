package chartbuilder;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class BuilderScenario {
	
	public Java2DBuilder builder;
	
	public BuilderScenario(){
		builder = new Java2DBuilder();
	}
	
	public static void main(String[] args) throws IOException{
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		BuilderScenario scene = new BuilderScenario();
		scene.testScenarioA();
	}
	
	public void testScenarioA() throws IOException{
		builder.createCanvas(600,600, BufferedImage.TYPE_INT_RGB);
		builder.createXAxis("Lines of code", 0, 600, -20 , 300);
		builder.createYAxis("Number of methods", 0, 600, 800, 1200);
//		builder.createXAxis("Lines of code", 300, 600, -20 , 300);
//		builder.createYAxis("Number of methods", 300, 600, 800, 1200);
		builder.createRectangle(300, 60, 80, 40, Color.blue, "JAVA2DBUILDER");
		builder.createRectangle(500, 150, 30, 47, Color.blue, "PolymorphicViewsChart");
		File outputfile = new File("img.png");
	    ImageIO.write(builder.getImage(), "png", outputfile);
	}
	
}
