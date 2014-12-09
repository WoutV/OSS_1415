package shapesTest;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import shapes.LineFactory;
import shapes.Shape;

public class LineFactoryTest {

	@Test
	public void testCreateShape() {
		LineFactory lf = new LineFactory();
		Shape shape = lf.createShape(20.0, 15.0, "key", "label", new Color(255,0,0));
		assertTrue(shape.getHeight() == 20.0);
		assertTrue(shape.getWidth() == 15.0);
		assertTrue(shape.getKey().equals("key"));
		assertTrue(shape.getName().equals("label"));
		assertTrue(shape.getColor().equals(new Color(255,0,0)));
	}

}
