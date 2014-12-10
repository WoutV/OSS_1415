package strategyTest;

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import strategies.MetricColorStrategy;
import strategies.Strategy;
import utility.MeasureFetcher;

public class MetricColorStrategyTest {
		private MeasureFetcher measureFetcher;
		@Before
		public void initialize() {
			measureFetcher = createMockBuilder(MeasureFetcher.class).addMockedMethods("getMeasureValues","getResourceKeys").createMock();
			expect(measureFetcher.getMeasureValues("lines")).andReturn(new HashMap<String,Double>(){{put("resource1",1.0);put("resource2",2.0); put("resource3",3.0);}}).anyTimes();
			expect(measureFetcher.getResourceKeys()).andReturn(new ArrayList<String>(){{add("resource1");add("resource2"); add("resource3");}}).anyTimes();
			expect(measureFetcher.getMeasureValues("test")).andReturn(new HashMap<String,Double>()).anyTimes();
		}
		
		
		@Test
		public void testNormalUse() {
			replay(measureFetcher);
			Strategy<Color> strat = new MetricColorStrategy("min1max2keylines","r25g25b25", measureFetcher);
			Map<String, Color> result = strat.execute();
			Color color1 = new Color(255,255,255);
			Color color2 = new Color(0,0,0);
			
			assertEquals(color1,result.get("resource1"));
			assertEquals(color2,result.get("resource2"));
			assertEquals(color2,result.get("resource3"));
			
		}
		
		@Test
		public void testInvalidInput() {
			replay(measureFetcher);
			Strategy<Color> strat = new MetricColorStrategy("test","r25g25b25", measureFetcher);
			Map<String, Color> result = strat.execute();
			Color color = new Color(25,25,25);
			
			assertEquals(color,result.get("resource1"));
			assertEquals(color,result.get("resource2"));
			assertEquals(color,result.get("resource3"));			
		}

		@Test
		public void testFaultyKey() {
			replay(measureFetcher);
			Strategy<Color> strat = new MetricColorStrategy("min1max2keytest","r25g25b25", measureFetcher);
			Map<String, Color> result = strat.execute();
			Color color = new Color(25,25,25);
			
			assertEquals(color,result.get("resource1"));
			assertEquals(color,result.get("resource2"));
			assertEquals(color,result.get("resource3"));			
		}
}
