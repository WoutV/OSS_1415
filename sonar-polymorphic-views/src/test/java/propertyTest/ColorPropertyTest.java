package propertyTest;

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import properties.ColorProperty;
import utility.MeasureFetcher;

public class ColorPropertyTest {

	private MeasureFetcher measureFetcher;
	@Before
	public void initialize() {
		measureFetcher = createMockBuilder(MeasureFetcher.class).addMockedMethods("getResourceKeysAndNames", "getResourceKeys").createMock();
		expect(measureFetcher.getResourceKeysAndNames()).andReturn(new HashMap<String,String>(){{put("resource1","resource1");put("resource2","resource2");}}).anyTimes();
		expect(measureFetcher.getResourceKeys()).andReturn(new ArrayList<String>(){{add("resource1");add("resource2");}}).anyTimes();
		replay(measureFetcher);
	}
	
	
	@Test
	public void testNormalUse() {
		ColorProperty prop = new ColorProperty("r100g100b100", "r255g0b0", measureFetcher);
		assertTrue(prop.getName().equals("r100g100b100"));
		Map<String, Color> map = prop.getMap();
		assertTrue(map.get("resource1").equals(new Color(100,100,100)));
		assertTrue(map.get("resource2").equals(new Color(100,100,100)));
		assertTrue(prop.getValues().contains(new Color(100,100,100)));
		assertTrue(prop.getValues().size() == 2);
	}
	
	@Test
	public void testInvalidValue() {
		ColorProperty prop = new ColorProperty("NoColor", "r255g0b0", measureFetcher);
		assertTrue(prop.getName().equals("NoColor"));
		Map<String, Color> map = prop.getMap();
		assertTrue(map.get("resource1").equals(new Color(255,0,0)));
		assertTrue(map.get("resource2").equals(new Color(255,0,0)));
		assertTrue(prop.getValues().contains(new Color(255,0,0)));
		assertTrue(prop.getValues().size() == 2);
		
	}

}
