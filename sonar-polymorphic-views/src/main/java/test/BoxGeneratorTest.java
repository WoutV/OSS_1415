package test;

import static org.junit.Assert.assertTrue;
import generators.Box;
import generators.BoxGenerator;
import generators.DummyMeasureFetcher;
import generators.Shape;
import generators.Util;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.sonar.api.charts.ChartParameters;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;

public class BoxGeneratorTest {

	@Test
	public void testNormalUse() {
		DummyMeasureFetcher dummyFetcher = new DummyMeasureFetcher(null, null, null);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("boxcolor", "r255g0b0");
		paramMap.put("boxheight", "12.0");
		paramMap.put("boxwidth", "8.0");
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		BoxGenerator bg = new BoxGenerator(dummyFetcher, params);
		Shape[] shapes = bg.getShapes();
		assertTrue(shapes[0].getName().equals("resource1"));
		assertTrue(shapes[0].getColor().equals(new Color(255,0,0)));
		assertEquals(12.0,shapes[0].getHeight(),0.001);
		assertTrue(shapes[1].getName().equals("resource2"));
		assertTrue(shapes[1].getColor().equals(new Color(255,0,0)));
		assertEquals(8.0,shapes[1].getWidth(),0.001);
		assertTrue(shapes[2].getName().equals("resource3"));
		assertTrue(shapes[2].getColor().equals(new Color(255,0,0)));
	}
	
	@Test
	public void testDefault() {
		DummyMeasureFetcher dummyFetcher = new DummyMeasureFetcher(null, null, null);
		Map<String, String> paramMap = new HashMap<String, String>();
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		BoxGenerator bg = new BoxGenerator(dummyFetcher, params);
		Shape[] shapes = bg.getShapes();
		assertTrue(shapes[0].getName().equals("resource1"));
		assertTrue(shapes[0].getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_BOXCOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXHEIGHT),shapes[0].getHeight(),0.001);
		assertTrue(shapes[1].getName().equals("resource2"));
		assertTrue(shapes[1].getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_BOXCOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXWIDTH),shapes[1].getWidth(),0.001	);
		assertTrue(shapes[2].getName().equals("resource3"));
		assertTrue(shapes[2].getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_BOXCOLOR)));
	}

}
