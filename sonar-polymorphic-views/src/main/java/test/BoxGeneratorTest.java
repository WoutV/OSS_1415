package test;

import generators.ScatterPlotGenerator;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class BoxGeneratorTest {

	@Test
	public void test() {
		Map<String,Double> map = new HashMap<String,Double>();
		map.put("-2", -2.0);
		map.put("0",0.0);
		map.put("2",2.0);
		
		map = ScatterPlotGenerator.scale(map,1.0, 3.0	);
		
		assertEquals(1.0, map.get("-2").doubleValue(),0.0001);
		assertEquals(2.0, map.get("0").doubleValue(),0.0001);
		assertEquals(3.0, map.get("2").doubleValue(),0.0001);
		
	}

}
