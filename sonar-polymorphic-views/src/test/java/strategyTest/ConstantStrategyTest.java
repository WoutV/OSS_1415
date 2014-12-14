package strategyTest;

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.easymock.internal.ReplayState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import strategies.ConstantStrategy;
import strategies.Strategy;
import utility.MeasureFetcher;

@RunWith(JUnit4.class)
public class ConstantStrategyTest {

	private MeasureFetcher measureFetcher;
	@Before
	public void initialize() {
		measureFetcher = createMockBuilder(MeasureFetcher.class).addMockedMethods("getResourceKeysAndNames").createMock();
		expect(measureFetcher.getResourceKeysAndNames()).andReturn(new HashMap<String,String>(){{put("resource1","resource1");put("resource2","resource2"); put("resource3","resource3");}}).anyTimes();
		replay(measureFetcher);
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
