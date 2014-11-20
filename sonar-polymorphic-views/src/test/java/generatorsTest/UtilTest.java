package generatorsTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import generators.Util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIsValidColorCorrectcase() {
		String valid = "r1g1b1";
		String negative = "r-245g3b9";
		String black = "r0g0b0";
		String white = "r255g255b255";
		String tooBig = "r355g2b99";
		String word = "color";
		
		assertTrue(Util.isValidColor(valid));	
		assertTrue(Util.isValidColor(black));	
		assertTrue(Util.isValidColor(white));
		assertFalse(Util.isValidColor(tooBig));
		assertFalse(Util.isValidColor(word));
		assertFalse(Util.isValidColor(negative));
	}
	
	@Test
	public void scaleTest() {
		Map<String,Double> map = new HashMap<String,Double>();
		map.put("-2", -2.0);
		map.put("0",0.0);
		map.put("2",2.0);
		
		map = Util.scale(map,1.0, 3.0);
		
		assertEquals(1.0, map.get("-2").doubleValue(),0.0001);
		assertEquals(2.0, map.get("0").doubleValue(),0.0001);
		assertEquals(3.0, map.get("2").doubleValue(),0.0001);
		
	}

	@Test
	public void splitOnDelimiterTestNormal() {		
		String test = "abcdef";
		String[] delimiters = {"a","c","e"};
		String result[] = Util.splitOnDelimiter(test, delimiters);
		
		assertEquals("b",result[0]);
		assertEquals("d",result[1]);
		assertEquals("f",result[2]);		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void splitOnDelimiterTestWrongDelimiter() {		
		String test = "abcdef";
		String[] delimiters = {"x"};
		String result[] = Util.splitOnDelimiter(test, delimiters);
	}

	@Test
	public void testIsBetweenNormal() {
		assertTrue(Util.isBetween(15, 12, 16));
		assertFalse(Util.isBetween(1,2,3));
	}

}
