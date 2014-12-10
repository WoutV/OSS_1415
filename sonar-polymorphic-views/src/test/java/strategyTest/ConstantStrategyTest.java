package strategyTest;

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import strategies.ConstantStrategy;
import strategies.Strategy;
import utility.MeasureFetcher;

public class ConstantStrategyTest {

	private MeasureFetcher measureFetcher;
	@Before
	public void initialize() {
		measureFetcher = createMockBuilder(MeasureFetcher.class).addMockedMethods("getResourceKeysAndNames").createMock();
		expect(measureFetcher.getResourceKeysAndNames()).andReturn(new HashMap<String,String>(){{put("resource1","resource1");put("resource2","resource2"); put("resource3","resource3");}}).anyTimes();
	}
	
	
	@Test
	public void testNormalUse() {
		Strategy<Double> strat = new ConstantStrategy("10",measureFetcher);
		Map<String,Double> result = strat.execute();
		for (int i=1;i<4;i++){
		assertEquals(10,result.get("resource"+i),0.001);
		}
	}
	
	@Test
	public void testInvalidValue() {
		Strategy<Double> strat = new ConstantStrategy("test",measureFetcher);
		Map<String,Double> result = strat.execute();
		for (int i=1;i<4;i++){
		assertEquals(0,result.get("resource"+i),0.001);
		}
	}

}
