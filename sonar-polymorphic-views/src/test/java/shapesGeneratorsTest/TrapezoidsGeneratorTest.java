package shapesGeneratorsTest;

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.charts.ChartParameters;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import shapes.Shape;
import shapesgenerators.CirclesGenerator;
import shapesgenerators.TrapsGenerator;
import utility.MeasureFetcher;
import utility.Util;

public class TrapezoidsGeneratorTest {
	
private MeasureFetcher measureFetcher;
	
	@Before
	public void initialize() {
		measureFetcher = createMockBuilder(MeasureFetcher.class).addMockedMethods("getResourceKeysAndNames", "getResourceKeys", "getResourceNames", "getNumberOfResources","getMeasureValues","findMetric").createMock();
		expect(measureFetcher.getResourceKeysAndNames()).andReturn(new HashMap<String,String>(){{put("resource1","resource1");put("resource2", "resource2"); put("resource3","resource3");}}).times(5);
		expect(measureFetcher.getResourceKeys()).andReturn(new ArrayList<String>(){{add("resource1");add("resource2"); add("resource3");}}).times(5);
		expect(measureFetcher.getResourceNames()).andReturn(new ArrayList<String>(){{add("resource1");add("resource2"); add("resource3");}});
		expect(measureFetcher.getNumberOfResources()).andReturn(3);
	}
	
	@Test
	public void testNormalUse() {
		replay(measureFetcher);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("trapcolor", "r255g0b0");
		paramMap.put("trapside1", "12");
		paramMap.put("trapside2", "2");
		paramMap.put("trapside3", "9");
		
		
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		TrapsGenerator bg = new TrapsGenerator(measureFetcher, params);
		Map<String, Shape> shapes = bg.getShapes();
		
		assertTrue(shapes.get("resource1").getName().equals("resource1"));
		assertTrue(shapes.get("resource1").getColor().equals(new Color(255,0,0)));
		assertEquals(9.0,shapes.get("resource1").getHeight(),0.001);
		assertEquals(12.0,shapes.get("resource1").getWidth(),0.001);
		assertTrue(shapes.get("resource2").getName().equals("resource2"));
		assertTrue(shapes.get("resource2").getColor().equals(new Color(255,0,0)));
		assertEquals(12.0,shapes.get("resource2").getWidth(),0.001);
		assertEquals(9.0,shapes.get("resource2").getHeight(),0.001);
		assertTrue(shapes.get("resource3").getName().equals("resource3"));
		assertTrue(shapes.get("resource3").getColor().equals(new Color(255,0,0)));
		assertEquals(12.0,shapes.get("resource3").getWidth(),0.001);
		assertEquals(9.0,shapes.get("resource3").getHeight(),0.001);
	}

	@Test
	public void getShapesDefaultTest() {
		replay(measureFetcher);
		Map<String, String> paramMap = new HashMap<String, String>();
		
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		TrapsGenerator bg = new TrapsGenerator(measureFetcher, params);
		Map<String, Shape> shapes = bg.getShapes();
		
		assertTrue(shapes.get("resource1").getName().equals("resource1"));
		assertTrue(shapes.get("resource1").getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_TRAPCOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_TRAPSIDE2),shapes.get("resource1").getHeight(),0.001);
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_TRAPSIDE1),shapes.get("resource1").getWidth(),0.001);
		assertTrue(shapes.get("resource2").getName().equals("resource2"));
		assertTrue(shapes.get("resource2").getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_TRAPCOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_TRAPSIDE2),shapes.get("resource2").getHeight(),0.001);
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_TRAPSIDE1),shapes.get("resource2").getWidth(),0.001);
		assertTrue(shapes.get("resource3").getName().equals("resource3"));
		assertTrue(shapes.get("resource3").getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_TRAPCOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_TRAPSIDE2),shapes.get("resource3").getHeight(),0.001);
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_TRAPSIDE1),shapes.get("resource3").getWidth(),0.001);
	}

}
