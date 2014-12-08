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
import shapesgenerators.MetricShapesGenerator;
import utility.MeasureFetcher;
import utility.Util;
import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.WSMetric;

public class MetricShapeGeneratorTest {
	
private MeasureFetcher measureFetcher;
	
	@Before
	public void initialize() {
		measureFetcher = createMockBuilder(MeasureFetcher.class).addMockedMethods("getResourceKeys", "getResourceNames", "getNumberOfResources","getMeasureValues","findMetric").createMock();
		expect(measureFetcher.getResourceKeys()).andReturn(new ArrayList<String>(){{add("resource1");add("resource2"); add("resource3");add("resource4");}}).anyTimes();
		expect(measureFetcher.getResourceNames()).andReturn(new ArrayList<String>(){{add("resource1");add("resource2"); add("resource3");add("resource4");}}).anyTimes();
		expect(measureFetcher.getNumberOfResources()).andReturn(4).anyTimes();
	}
	
	@Test
	public void testNormalUse() {
		expect(measureFetcher.getMeasureValues("lines")).andReturn(new HashMap<String,Double>(){{put("resource1",20.0);put("resource2",200.0); put("resource3",2000.0);put("resource4",2001.0);}}).anyTimes();
		expect(measureFetcher.findMetric("lines")).andReturn(new WSMetric(new Metric())).anyTimes();
		replay(measureFetcher);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("trapcolor", "r255g0b0");
		paramMap.put("trapside1", "12");
		paramMap.put("trapside2", "2");
		paramMap.put("trapside3", "9");
		
		
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		MetricShapesGenerator bg = new MetricShapesGenerator(measureFetcher, params);
		Shape[] shapes = bg.getShapes();
		
		assertTrue(shapes[0].getName().equals("resource1"));
		assertTrue(shapes[0].getColor().equals(new Color(255,0,0)));
		assertEquals(9.0,shapes[0].getHeight(),0.001);
		assertEquals(12.0,shapes[0].getWidth(),0.001);
		assertTrue(shapes[1].getName().equals("resource2"));
		assertTrue(shapes[1].getColor().equals(new Color(255,0,0)));
		assertEquals(12.0,shapes[1].getWidth(),0.001);
		assertTrue(shapes[2].getName().equals("resource3"));
		assertTrue(shapes[2].getColor().equals(new Color(255,0,0)));
	}

	@Test
//	public static final String DEFAULT_SHAPEMETRICORDER = "box-circle-trap-circle";
//	public static final String DEFAULT_SHAPEMETRICSPLIT = "20x200x2000";
	
	public void getShapesDefaultTest() {
		PolymorphicChartParameters.DEFAULT_SHAPEMETRIC = "lines";
		expect(measureFetcher.getMeasureValues("lines")).andReturn(new HashMap<String,Double>(){{put("resource1",20.0);put("resource2",200.0); put("resource3",2000.0);put("resource4",2001.0);}}).anyTimes();
		expect(measureFetcher.findMetric("lines")).andReturn(new WSMetric(new Metric())).anyTimes();
		replay(measureFetcher);
		Map<String, String> paramMap = new HashMap<String, String>();
		
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		MetricShapesGenerator bg = new MetricShapesGenerator(measureFetcher, params);
		Shape[] shapes = bg.getShapes();
		
		assertTrue(shapes[0].getName().equals("resource1"));
		assertTrue(shapes[0].getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_BOXCOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXHEIGHT),shapes[0].getHeight(),0.001);
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXWIDTH),shapes[0].getWidth(),0.001);
		assertTrue(shapes[1].getName().equals("resource2"));
		assertTrue(shapes[1].getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_CIRCLECOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_CIRCLEDIAM),shapes[1].getHeight(),0.001);
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_CIRCLEDIAM),shapes[1].getWidth(),0.001);
		assertTrue(shapes[2].getName().equals("resource3"));
		assertTrue(shapes[2].getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_TRAPCOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_TRAPSIDE2),shapes[2].getHeight(),0.001);
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_TRAPSIDE1),shapes[2].getWidth(),0.001);
		assertTrue(shapes[3].getName().equals("resource4"));
		assertTrue(shapes[3].getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_CIRCLECOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_CIRCLEDIAM),shapes[3].getHeight(),0.001);
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_CIRCLEDIAM),shapes[3].getWidth(),0.001);
	}

}
