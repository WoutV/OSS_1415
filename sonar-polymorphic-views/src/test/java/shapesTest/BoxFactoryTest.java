package shapesTest;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import shapes.BoxFactory;
import shapes.Shape;

public class BoxFactoryTest {

	@Test
	public void testCreateShape() {
		BoxFactory bf = new BoxFactory();
		Shape shape = bf.createShape(20.0, 15.0, "key", "label", new Color(255,0,0));
		assertTrue(shape.getHeight() == 20.0);
		assertTrue(shape.getWidth() == 15.0);
		assertTrue(shape.getKey().equals("key"));
		assertTrue(shape.getName().equals("label"));
		assertTrue(shape.getColor().equals(new Color(255,0,0)));
	}

}
