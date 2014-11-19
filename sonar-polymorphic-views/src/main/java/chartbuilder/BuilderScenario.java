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
		builder.createXAxis("Lines of code", -20, 200);
		builder.createYAxis("Number of methods", 0, 500);
		builder.createRectangleFittedToAxes(50, 70, 12, 15, Color.blue, "sonar.random");
		builder.createRectangleFittedToAxes(100, 50, 12, 15, Color.blue, "sonarTest");
		builder.createRectangleFittedToAxes(200, 500, 20, 15, Color.blue, "sonar Stuff");
		builder.createRectangleFittedToAxes(-20, 500, 50, 70, Color.blue, "sonarqube");
		builder.createRectangleFittedToAxes(200, 0, 90, 90, Color.blue, "visualistation");
		builder.createRectangleFittedToAxes(0, 0, 80, 40, Color.blue, "JAVA2DBUILDER");
		builder.createRectangleFittedToAxes(-75, 150, 30, 47, Color.blue, "PolymorphicViewsChart");
		File outputfile = new File("img.png");
	    ImageIO.write(builder.getImage(), "png", outputfile);
	}
	
}
