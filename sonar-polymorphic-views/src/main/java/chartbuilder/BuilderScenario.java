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
		builder.createCanvas(512,512, BufferedImage.TYPE_INT_RGB);
		builder.createXAxis("Lines of code", 50, 462, 80, 400, 462);
		builder.createYAxis("Number of methods", 50, 462, 50, 100, 50);
		builder.createRectangle(50, 70, 12, 15, Color.blue, "sonar.random");
		builder.createRectangle(100, 50, 12, 15, Color.blue, "sonarTest");
		builder.createRectangle(200, 222, 20, 15, Color.blue, "sonar Stuff");
		builder.createRectangle(80, 300, 50, 70, Color.blue, "sonarqube");
		builder.createRectangle(350, 200, 90, 90, Color.blue, "visualistation");
		builder.createRectangle(350, 190, 80, 40, Color.blue, "JAVA2DBUILDER");
		builder.createRectangle(200, 150, 30, 47, Color.blue, "PolymorphicViewsChart");
		File outputfile = new File("img.png");
	    ImageIO.write(builder.getImage(), "png", outputfile);
	}
	
}
