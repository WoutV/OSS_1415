package generatorsTest;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import generators.BoxGenerator;
import generators.MeasureFetcher;
import generators.Shape;
import generators.Util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.charts.ChartParameters;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;

public class BoxGeneratorTest {
	
	private MeasureFetcher measureFetcher;
	
	@Before
	  public void setUp() throws Exception {
//	    measureFetcher = createMockBuilder(MeasureFetcher.class)
//	    		.withConstructor(String.class, String.class, SonarFacade.class)
//	    		.withArgs("", "", null)
//	    		.addMockedMethod("getNumberOfResources", int.class)
//	    		.createNiceMock();
		measureFetcher = EasyMock.createNiceMock(MeasureFetcher.class);
		expect(measureFetcher.getNumberOfResources()).andReturn(3);
		expect(measureFetcher.getResourceNames()).andReturn(new ArrayList<String>(){{add("resource1");add("resource2"); add("resource3");}});
		expect(measureFetcher.getMeasureValues("lines")).andReturn(new HashMap<String,Double>());
		expect(measureFetcher.getMeasureValues("commentlines")).andReturn(new HashMap<String,Double>());
		expect(measureFetcher.getMeasureValues("width")).andReturn(new HashMap<String,Double>());
		expect(measureFetcher.getMeasureValues("height")).andReturn(new HashMap<String,Double>());
		expect(measureFetcher.getMeasureValues("color")).andReturn(new HashMap<String,Double>());
		//expect(measureFetcher.getMeasureValues("width")).andReturn(new HashMap<String,Double>(){{put("resource1",null); put("resource2",null);put("resource3",null);}});
		//expect(measureFetcher.getMeasureValues("height")).andReturn(new HashMap<String,Double>(){{put("resource1",null); put("resource2",null);put("resource3",null);}});
		replay(measureFetcher);
	}
	  

	@Test
	public void testNormalUse() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("boxcolor", "r255g0b0");
		paramMap.put("boxheight", "12.0");
		paramMap.put("boxwidth", "8.0");
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		BoxGenerator bg = new BoxGenerator(measureFetcher, params);
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
		Map<String, String> paramMap = new HashMap<String, String>();
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		BoxGenerator bg = new BoxGenerator(measureFetcher, params);
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
	
	@Test
	public void testInvalidInput() {
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("boxcolor", "color");
		paramMap.put("boxheight", "height");
		paramMap.put("boxwidth", "width");
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		BoxGenerator bg = new BoxGenerator(measureFetcher, params);
		Shape[] shapes = bg.getShapes();
		assertTrue(shapes[0].getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_BOXCOLOR)));
		assertEquals(Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXHEIGHT),shapes[0].getHeight(),0.001);
	}
	
	@Test
	public void testNegativeInput() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("boxcolor", "r-20g30b253");
		paramMap.put("boxheight", "-25");
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		BoxGenerator bg = new BoxGenerator(measureFetcher, params);
		Shape[] shapes = bg.getShapes();
		assertTrue(shapes[0].getColor().equals(Util.parseColor(PolymorphicChartParameters.DEFAULT_BOXCOLOR)));
		assertEquals(25.0,shapes[0].getHeight(),0.001);
	}
	

	@Test
	public void testGrayScaleInput() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("boxcolor", "min0max255keylines");
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		BoxGenerator bg = new BoxGenerator(measureFetcher, params);
		Shape[] shapes = bg.getShapes();
		assertEquals(shapes[0].getColor(),new Color(255,255,255));
		assertEquals(shapes[1].getColor(),new Color(42,42,42));
		assertEquals(shapes[2].getColor(),new Color(0,0,0));
	}
	
	@Test
	public void testGrayScale2Input() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("boxcolor", "min0max1keylines");
		ChartParameters p = new ChartParameters(paramMap); 
		PolymorphicChartParameters params = new PolymorphicChartParameters(p);
		BoxGenerator bg = new BoxGenerator(measureFetcher, params);
		Shape[] shapes = bg.getShapes();
		assertEquals(shapes[0].getColor(),new Color(255,255,255));
		assertEquals(shapes[1].getColor(),new Color(42,42,42));
		assertEquals(shapes[2].getColor(),new Color(0,0,0));
	}
	
	

}
