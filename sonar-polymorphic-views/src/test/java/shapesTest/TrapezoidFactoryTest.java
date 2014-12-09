package shapesTest;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import shapes.Trapezoid;
import shapes.TrapezoidFactory;
import shapes.Shape;

public class TrapezoidFactoryTest {

	@Test
	public void testCreateShape() {
		TrapezoidFactory tf = new TrapezoidFactory();
		Shape shape = tf.createShape(20.0, 15.0, "key", "label", new Color(255,0,0));
		assertTrue(shape.getHeight() == 20.0);
		assertTrue(shape.getWidth() == 15.0);
		assertTrue(((Trapezoid) shape).getSecondHeight() == 16.0);
		assertTrue(shape.getKey().equals("key"));
		assertTrue(shape.getName().equals("label"));
		assertTrue(shape.getColor().equals(new Color(255,0,0)));
	}
}
