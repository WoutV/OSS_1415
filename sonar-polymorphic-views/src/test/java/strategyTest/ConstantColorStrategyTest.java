package strategyTest;

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import strategies.ConstantColorStrategy;
import strategies.Strategy;
import utility.MeasureFetcher;

public class ConstantColorStrategyTest {

	private MeasureFetcher measureFetcher;
	@Before
	public void initialize() {
		measureFetcher = createMockBuilder(MeasureFetcher.class).addMockedMethods("getResourceKeys").createMock();
		expect(measureFetcher.getResourceKeys()).andReturn(new ArrayList<String>(){{add("resource1");add("resource2"); add("resource3");}}).anyTimes();
	}
	
	
	@Test
	public void testNormalUse() {
		replay(measureFetcher);
		Color color = new Color(25,25,25);
		Strategy<Color> strat = new ConstantColorStrategy(color,measureFetcher);
		Map<String,Color> result = strat.execute();
		for (int i=1;i<4;i++){
		assertEquals(color,result.get("resource"+i));
		}
	}
	

}
