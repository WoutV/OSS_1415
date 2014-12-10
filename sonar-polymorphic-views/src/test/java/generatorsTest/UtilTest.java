package generatorsTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utility.Util;

public class UtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testParseColor(){
		String color = "r25g25b25";
		Color c = new Color(25,25,25);
		assertEquals(c,Util.parseColor(color));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testParseColorFaultyInput(){
		String color = "rtestg25b25";
		Util.parseColor(color);
		
	}
	
	@Test
	public void testIsValidColor() {
		String valid = "r1g1b1";
		String negative = "r-245g3b9";
		String black = "r0g0b0";
		String white = "r255g255b255";
		String tooBig = "r355g2b99";
		String word = "color";
		String test1 = "r20g500b200";
		String test2 = "r20g200b500";
		
		assertTrue(Util.isValidColor(valid));	
		assertTrue(Util.isValidColor(black));	
		assertTrue(Util.isValidColor(white));
		assertFalse(Util.isValidColor(tooBig));
		assertFalse(Util.isValidColor(word));
		assertFalse(Util.isValidColor(negative));
		assertFalse(Util.isValidColor(test1));
		assertFalse(Util.isValidColor(test2));


	}
	
	@Test
	public void scaleGreyTest(){
		Map<String,Double> map = new HashMap<String,Double>();
		map.put("test1", 0.0);
		map.put("test2", 1.0);
		map.put("test3", 2.0);
		map.put("test4",1.5);
		Map<String,Double> result = Util.scaleGrey(map, 1, 2);
		assertEquals(255.0,result.get("test1"),0.001);
		assertEquals(255.0,result.get("test2"),0.001);
		assertEquals(0.0,result.get("test3"),0.001);
		assertEquals(127.5,result.get("test4"),0.001);
	}
	@Test
	public void scaleMapTest() {
		Map<String,Double> map = new HashMap<String,Double>();
		map.put("-2", -2.0);
		map.put("0",0.0);
		map.put("2",2.0);
		
		map = Util.scaleMap(map,1.0, 3.0);
		
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