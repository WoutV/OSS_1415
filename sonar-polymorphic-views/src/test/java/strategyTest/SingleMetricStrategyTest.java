package strategyTest;

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import strategies.SingleMetricStrategy;
import strategies.Strategy;
import utility.MeasureFetcher;

public class SingleMetricStrategyTest {
	private MeasureFetcher measureFetcher;
	@Before
	public void initialize() {
		measureFetcher = createMockBuilder(MeasureFetcher.class).addMockedMethods("getMeasureValues").createMock();
		expect(measureFetcher.getMeasureValues("lines")).andReturn(new HashMap<String,Double>(){{put("resource1",1.0);put("resource2",2.0); put("resource3",3.0);}}).anyTimes();
	}
	
	
	@Test
	public void testNormalUse() {
		replay(measureFetcher);
		Strategy<Double> strat = new SingleMetricStrategy("lines", measureFetcher);
		Map<String,Double> result = strat.execute();
		for (int i=1;i<4;i++){
		assertEquals(i,result.get("resource"+i),0.001);
		}
	}


}
