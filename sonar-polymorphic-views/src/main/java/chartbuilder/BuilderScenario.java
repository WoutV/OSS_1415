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
		builder.createCanvas(500,500, BufferedImage.TYPE_INT_RGB);
		builder.createXAxis("Lines of code", 0, 200, -20 , 300);
		builder.createYAxis("Number of methods", 0, 500, 800, 1200);
//		builder.createRectangle(50, 70, 12, 15, Color.blue, "sonar.random");
		builder.createRectangle(250, 250, 50, 50, Color.blue, "sonarTest");
//		builder.createRectangle(200, 500, 20, 15, Color.blue, "sonar Stuff");
//		builder.createRectangle(-20, 500, 50, 70, Color.blue, "sonarqube");
//		builder.createRectangle(200, 0, 90, 90, Color.blue, "visualistation");
		builder.createRectangle(0, 0, 80, 40, Color.blue, "JAVA2DBUILDER");
//		builder.createRectangle(-75, 150, 30, 47, Color.blue, "PolymorphicViewsChart");
		builder.createLine(0, 400, 500, 500);
		File outputfile = new File("img.png");
	    ImageIO.write(builder.getImage(), "png", outputfile);
	}
	
}
