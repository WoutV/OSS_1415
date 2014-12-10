package shapesTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import shapes.ShapeType;

public class ShapeTypeTest {

	@Test
	public void testFromStringDefault() {
		assertEquals(ShapeType.BOX,ShapeType.fromString("box"));
		assertEquals(ShapeType.TRAPEZOID,ShapeType.fromString("trap"));
		assertEquals(ShapeType.CIRCLE,ShapeType.fromString("circle"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFromStringException() {
		ShapeType.fromString("noShape");
	}


}
