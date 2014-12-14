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

import facade.WSMetric;
import plugin.PolymorphicChartParameters;
import shapes.Shape;
import shapesgenerators.MetricShapesGenerator;
import utility.MeasureFetcher;
import utility.Util;

public class MetricShapeGeneratorTest {
	
private MeasureFetcher measureFetcher;
	
	@Before
	public void initialize() {
		measureFetcher = createMockBuilder(MeasureFetcher.class).addMockedMethods("getResourceKeysAndNames", "getResourceKeys", "getNumberOfResources","getMeasureValues","findMetric").createMock();
		expect(measureFetcher.getResourceKeysAndNames()).andReturn(new HashMap<String, String>(){{put("resource1","resource1");put("resource2","resource2"); put("resource3","resource3"); put("resource4","resource4");}}).anyTimes();
		expect(measureFetcher.getResourceKeys()).andReturn(new ArrayList<String>(){{add("resource1");add("resource2");add("resource3");add("resource4");}}).anyTimes();
		expect(measureFetcher.getNumberOfResources()).andReturn(4).anyTimes();
	}
	
	@Test
	public void testNormalUse() {
		expect(measureFetcher.getMeasureValues("lines")).andReturn(new HashMap<String,Double>(){{put("resource1",20.0);put("resource2",200.0); put("resource3",2000.0);put("resource4",2001.0);}}).anyTimes();
		expect(measureFetcher.findMetric("lines")).andReturn(new WSMetric(new Metric())).anyTimes();
		replay(measureFetcher);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("shapemetricorder", "circle-box-trap-box");
		paramMap.put("shapemetricsplit","20x200x2000");
		paramMap.put("circlecolor", "r200g0b0");
		paramMap.put("circlediam", "15");
		paramMap.put("boxcolor", "r100g0b0");
		paramMap.put("boxheight", "20");
		paramMap.put("boxwidth", "25");
		paramMap.put("trapcolor", "r255g0b0");
		paramMap.put("trapside1", "12");
		paramMap.put("trapside2", "2");
		paramMap.put("trapside3", "9");
		
		
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		MetricShapesGenerator bg = new MetricShapesGenerator(measureFetcher, params);
		Map<String, Shape> shapes = bg.getShapes();
		
		assertTrue(shapes.get("resource1").getName().equals("resource1"));
		assertTrue(shapes.get("resource1").getColor().equals(new Color(200,0,0)));
		assertEquals(15.0,shapes.get("resource1").getHeight(),0.001);
		assertEquals(15.0,shapes.get("resource1").getWidth(),0.001);
		assertTrue(shapes.get("resource2").getName().equals("resource2"));
		assertTrue(shapes.get("resource2").getColor().equals(new Color(100,0,0)));
		assertEquals(25.0,shapes.get("resource2").getWidth(),0.001);
		assertEquals(20.0,shapes.get("resource2").getHeight(),0.001);
		assertTrue(shapes.get("resource3").getName().equals("resource3"));
		assertTrue(shapes.get("resource3").getColor().equals(new Color(255,0,0)));
		assertEquals(12.0,shapes.get("resource3").getWidth(),0.001);
		assertEquals(9.0,shapes.get("resource3").getHeight(),0.001);
		assertTrue(shapes.get("resource4").getName().equals("resource4"));
		assertTrue(shapes.get("resource4").getColor().equals(new Color(100,0,0)));
		assertEquals(25.0,shapes.get("resource4").getWidth(),0.001);
		assertEquals(20.0,shapes.get("resource4").getHeight(),0.001);
	}

	@Test
	public void getShapesDefaultTest() {
		PolymorphicChartParameters.DEFAULT_SHAPEMETRIC = "lines";
		expect(measureFetcher.getMeasureValues("lines")).andReturn(new HashMap<String,Double>(){{put("resource1",20.0);put("resource2",200.0); put("resource3",2000.0);put("resource4",2001.0);}}).anyTimes();
		expect(measureFetcher.findMetric("lines")).andReturn(new WSMetric(new Metric())).anyTimes();
		replay(measureFetcher);
		Map<String, String> paramMap = new HashMap<String, String>();
		
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		MetricShapesGenerator bg = new MetricShapesGenerator(measureFetcher, params);
		Map<String, Shape> shapes = bg.getShapes();
		
		assertTrue(shapes.get("resource1").getName().equals("resource1"));
		assertTrue(shapes.get("resource1").getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_BOXCOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXHEIGHT),shapes.get("resource1").getHeight(),0.001);
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXWIDTH),shapes.get("resource1").getWidth(),0.001);
		assertTrue(shapes.get("resource2").getName().equals("resource2"));
		assertTrue(shapes.get("resource2").getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_CIRCLECOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_CIRCLEDIAM),shapes.get("resource2").getHeight(),0.001);
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_CIRCLEDIAM),shapes.get("resource2").getWidth(),0.001);
		assertTrue(shapes.get("resource3").getName().equals("resource3"));
		assertTrue(shapes.get("resource3").getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_TRAPCOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_TRAPSIDE2),shapes.get("resource3").getHeight(),0.001);
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_TRAPSIDE1),shapes.get("resource3").getWidth(),0.001);
		assertTrue(shapes.get("resource4").getName().equals("resource4"));
		assertTrue(shapes.get("resource4").getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_CIRCLECOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_CIRCLEDIAM),shapes.get("resource4").getHeight(),0.001);
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_CIRCLEDIAM),shapes.get("resource4").getWidth(),0.001);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNoNumberUsed() {
		expect(measureFetcher.getMeasureValues("lines")).andReturn(new HashMap<String,Double>(){{put("resource1",20.0);put("resource2",200.0); put("resource3",2000.0);put("resource4",2001.0);}}).anyTimes();
		expect(measureFetcher.findMetric("lines")).andReturn(new WSMetric(new Metric())).anyTimes();
		replay(measureFetcher);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("shapemetricorder", "circle-box-trap-box");
		paramMap.put("shapemetricsplit","notanumber");
		paramMap.put("circlecolor", "r200g0b0");
		paramMap.put("circlediam", "15");
		paramMap.put("boxcolor", "r100g0b0");
		paramMap.put("boxheight", "20");
		paramMap.put("boxwidth", "25");
		paramMap.put("trapcolor", "r255g0b0");
		paramMap.put("trapside1", "12");
		paramMap.put("trapside2", "2");
		paramMap.put("trapside3", "9");
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		MetricShapesGenerator bg = new MetricShapesGenerator(measureFetcher, params);
		Map<String, Shape> shapes = bg.getShapes();
	}

}
