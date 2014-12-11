package propertyTest;

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.sonar.wsclient.services.Metric;

import be.kuleuven.cs.oss.sonarfacade.WSMetric;
import properties.ValueProperty;
import utility.MeasureFetcher;

public class ValuePropertyTest {

	private MeasureFetcher measureFetcher;
	@Before
	public void initialize() {
		measureFetcher = createMockBuilder(MeasureFetcher.class).addMockedMethods("getResourceKeysAndNames", "getResourceKeys","getMeasureValues", "findMetric").createMock();
		expect(measureFetcher.getResourceKeysAndNames()).andReturn(new HashMap<String,String>(){{put("resource1","resource1");put("resource2","resource2");}}).anyTimes();
		expect(measureFetcher.getResourceKeys()).andReturn(new ArrayList<String>(){{add("resource1");add("resource2");}}).anyTimes();
	}
	
	
	@Test
	public void testConstantStrategyValue() {
		replay(measureFetcher);
		ValueProperty prop = new ValueProperty("50", "20", measureFetcher);
		assertTrue(prop.getName().equals("50"));
		Map<String, Double> map = prop.getMap();
		assertTrue(map.get("resource1") == 50.0);
		assertTrue(map.get("resource2") == 50.0);
		assertTrue(prop.getValues().contains(50.0));
		assertTrue(prop.getValues().size() == 2);
	}
	
	@Test
	public void testConstantStrategyDefaultValue() {
		replay(measureFetcher);
		ValueProperty prop = new ValueProperty("-50", "20", measureFetcher);
		assertTrue(prop.getName().equals("-50"));
		Map<String, Double> map = prop.getMap();
		assertTrue(map.get("resource1") == 20.0);
		assertTrue(map.get("resource2") == 20.0);
		assertTrue(prop.getValues().contains(20.0));
		assertTrue(prop.getValues().size() == 2);
	}
	
	@Test
	public void testSingleMetricStrategyValue() {
		expect(measureFetcher.getMeasureValues("lines")).andReturn(new HashMap<String,Double>(){{put("resource1",20.0);put("resource2",200.0);}}).anyTimes();
		expect(measureFetcher.findMetric("lines")).andReturn(new WSMetric(new Metric())).anyTimes();
		replay(measureFetcher);
		
		ValueProperty prop = new ValueProperty("lines", "15", measureFetcher);
		assertTrue(prop.getName().equals("lines"));
		Map<String, Double> map = prop.getMap();
		assertTrue(map.get("resource1") == 20.0);
		assertTrue(map.get("resource2") == 200.0);
		assertTrue(prop.getValues().contains(20.0));
		assertTrue(prop.getValues().contains(200.0));
		assertTrue(prop.getValues().size() == 2);
	}
	
	@Test
	public void testSingleMetricStrategyDefaultValue() {
		expect(measureFetcher.getMeasureValues("lines")).andReturn(new HashMap<String,Double>(){{put("resource1",20.0);put("resource2",200.0);}}).anyTimes();
		expect(measureFetcher.findMetric("notvalidmetricvalue")).andReturn(null).anyTimes();
		replay(measureFetcher);
		
		ValueProperty prop = new ValueProperty("notvalidmetricvalue", "15", measureFetcher);
		assertTrue(prop.getName().equals("notvalidmetricvalue"));
		Map<String, Double> map = prop.getMap();
		System.out.println(prop.getValues().get(1));
		assertTrue(map.get("resource1") == 15.0);
		assertTrue(map.get("resource2") == 15.0);
		assertTrue(prop.getValues().contains(15.0));
		assertTrue(prop.getValues().size() == 2);
	}
}