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

import shapes.Shape;
import shapesgenerators.CirclesGenerator;
import utility.MeasureFetcher;
import utility.Util;
import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.WSMetric;

public class CirclesGeneratorTest {
	
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
		paramMap.put("shapemetricorder", "circle");
		paramMap.put("circlecolor", "r255g0b0");
		paramMap.put("circlediam", "12");
		
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		CirclesGenerator cg = new CirclesGenerator(measureFetcher, params);
		Map<String, Shape> shapes = cg.getShapes();
		
		assertTrue(shapes.get("resource1").getName().equals("resource1"));
		assertTrue(shapes.get("resource1").getColor().equals(new Color(255,0,0)));
		assertEquals(12.0,shapes.get("resource1").getHeight(),0.001);
		assertTrue(shapes.get("resource2").getName().equals("resource2"));
		assertTrue(shapes.get("resource2").getColor().equals(new Color(255,0,0)));
		assertEquals(12.0,shapes.get("resource2").getWidth(),0.001);
		assertTrue(shapes.get("resource3").getName().equals("resource3"));
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
		CirclesGenerator cg = new CirclesGenerator(measureFetcher, params);
		Map<String, Shape> shapes = cg.getShapes();
		
		assertTrue(shapes.get("resource1").getName().equals("resource1"));
		assertTrue(shapes.get("resource1").getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_CIRCLECOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_CIRCLEDIAM),shapes.get("resource1").getHeight(),0.001);
		assertTrue(shapes.get("resource2").getName().equals("resource2"));
		assertTrue(shapes.get("resource2").getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_CIRCLECOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_CIRCLEDIAM),shapes.get("resource2").getWidth(),0.001	);
		assertTrue(shapes.get("resource3").getName().equals("resource3"));
		assertTrue(shapes.get("resource3").getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_CIRCLECOLOR)));
	}

}
