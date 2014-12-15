package shapesTest;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.Test;

import shapes.Box;
import shapes.Circle;
import shapes.Line;
import shapes.Shape;
import shapes.Trapezoid;
import chartbuilder.ChartBuilder;
import chartbuilder.Java2DBuilder;

public class ShapeTest {

	@Test
	public void testDrawing(){
		ChartBuilder builder = new Java2DBuilder();
		builder.createCanvas(600,600, BufferedImage.TYPE_INT_RGB);
		
		List<Shape> shapes = new ArrayList<Shape>();
		shapes.add(new Box(20,20,"key1","name1",Color.BLUE));
		shapes.add(new Trapezoid(40,70,50,"key2","name2",Color.RED));
		shapes.add(new Circle(40,40,"key3","name3",Color.PINK));
		shapes.add(new Line(20,70,"key4","name4",Color.BLACK));
		
		int i = 2;
		for(Shape shape : shapes){
			shape.setxPos(i);
			shape.setyPos(i);
			i+=20;
			shape.draw(builder);
		}
		File outputfile = new File("shapesExample.png");
	    try {
			ImageIO.write(builder.getImage(), "png", outputfile);
		} catch (IOException e) {
			System.out.println("fout");
		}
	}

}
