package shapesTest;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import shapes.Circle;
import shapes.CircleFactory;
import shapes.Shape;

public class CircleFactoryTest {

	@Test
	public void testCreateShape() {
		CircleFactory cf = new CircleFactory();
		Shape shape = cf.createShape(20.0, 20.0, "key", "label", new Color(255,0,0));
		assertTrue(shape.getHeight() == 20.0);
		assertTrue(shape.getWidth() == 20.0);
		assertTrue(shape.getKey().equals("key"));
		assertTrue(shape.getName().equals("label"));
		assertTrue(shape.getColor().equals(new Color(255,0,0)));
	}

}
