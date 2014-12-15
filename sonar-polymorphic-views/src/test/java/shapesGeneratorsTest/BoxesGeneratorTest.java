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
import org.sonar.wsclient.services.Metric;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import facade.WSMetric;
import shapes.Shape;
import shapesgenerators.BoxesGenerator;
import utility.MeasureFetcher;
import utility.Util;

public class BoxesGeneratorTest {
	
	private MeasureFetcher measureFetcher;
	 
	@Before
	public void initialize() {
		measureFetcher = createMockBuilder(MeasureFetcher.class).addMockedMethods("getResourceKeysAndNames", "getResourceKeys", "getNumberOfResources","getMeasureValues","findMetric").createMock();
		expect(measureFetcher.getResourceKeysAndNames()).andReturn(new HashMap<String,String>(){{put("resource1","resource1");put("resource2","resource2"); put("resource3","resource3");}}).anyTimes();
		expect(measureFetcher.getResourceKeys()).andReturn(new ArrayList<String>(){{add("resource1");add("resource2");add("resource3");}}).anyTimes();
		expect(measureFetcher.getNumberOfResources()).andReturn(3);
	}
	  

	@Test
	public void testNormalUse() {
		expect(measureFetcher.getMeasureValues("lines")).andReturn(new HashMap<String,Double>(){{put("resource1",20.0);put("resource2",200.0); put("resource3",2000.0);}}).anyTimes();
		expect(measureFetcher.findMetric("lines")).andReturn(new WSMetric(new Metric())).anyTimes();
		replay(measureFetcher);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("shapemetricorder", "box");
		paramMap.put("boxcolor", "r255g0b0");
		paramMap.put("boxheight", "12");
		paramMap.put("boxwidth", "8");
		
		
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		BoxesGenerator bg = new BoxesGenerator(measureFetcher, params);
		Map<String, Shape> shapes = bg.getShapes();
		
		assertEquals("resource1",shapes.get("resource1").getName());
		assertTrue(shapes.get("resource1").getColor().equals(new Color(255,0,0)));
		assertEquals(12.0,shapes.get("resource1").getHeight(),0.001);
		assertEquals("resource2",shapes.get("resource2").getName());
		assertTrue(shapes.get("resource2").getColor().equals(new Color(255,0,0)));
		assertEquals(8.0,shapes.get("resource2").getWidth(),0.001);
		assertEquals("resource3",shapes.get("resource3").getName());;
		assertTrue(shapes.get("resource3").getColor().equals(new Color(255,0,0)));
	}

	@Test
	public void getShapesDefaultTest() {
		expect(measureFetcher.getMeasureValues("lines")).andReturn(new HashMap<String,Double>(){{put("resource1",20.0);put("resource2",200.0); put("resource3",2000.0);}}).anyTimes();
		expect(measureFetcher.findMetric("lines")).andReturn(new WSMetric(new Metric())).anyTimes();
		replay(measureFetcher);
		Map<String, String> paramMap = new HashMap<String, String>();
		
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		BoxesGenerator bg = new BoxesGenerator(measureFetcher, params);
		Map<String, Shape> shapes = bg.getShapes();
		
		assertTrue(shapes.get("resource3").getName().equals("resource3"));
		assertTrue(shapes.get("resource3").getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_BOXCOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXHEIGHT),shapes.get("resource3").getHeight(),0.001);
		assertTrue(shapes.get("resource2").getName().equals("resource2"));
		assertTrue(shapes.get("resource2").getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_BOXCOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXWIDTH),shapes.get("resource2").getWidth(),0.001	);
		assertTrue(shapes.get("resource1").getName().equals("resource1"));
		assertTrue(shapes.get("resource1").getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_BOXCOLOR)));
	}
	
	@Test
	public void testInvalidMetrics() {
		expect(measureFetcher.findMetric("width")).andReturn(null);
		expect(measureFetcher.findMetric("height")).andReturn(null);
		expect(measureFetcher.findMetric("color")).andReturn(null);
		replay(measureFetcher);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("boxcolor", "color");
		paramMap.put("boxheight", "height");
		paramMap.put("boxwidth", "width");
		
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		BoxesGenerator bg = new BoxesGenerator(measureFetcher, params);
		Map<String, Shape> shapes = bg.getShapes();
		
		assertTrue(shapes.get("resource1").getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_BOXCOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXHEIGHT),shapes.get("resource1").getHeight(),0.001);
	}
	
	@Test
	public void testInvalidInput() {
		replay(measureFetcher);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("boxcolor", "r290g1000b4");
		paramMap.put("boxheight", "-150");
		paramMap.put("boxwidth", "-50");
		
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		BoxesGenerator bg = new BoxesGenerator(measureFetcher, params);
		Map<String, Shape> shapes = bg.getShapes();
		
		assertTrue(shapes.get("resource1").getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_BOXCOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXHEIGHT),shapes.get("resource1").getHeight(),0.001);
	}

}
