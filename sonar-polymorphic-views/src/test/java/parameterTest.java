import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.sonar.api.charts.ChartParameters;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicViewsChart;

public class parameterTest {

	@Test
	public void testCasting() {

		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("width", "2");
		inputParams.put("height", "3");
		ChartParameters params = new ChartParameters(inputParams);
		PolymorphicViewsChart chart = new PolymorphicViewsChart(null);
		BufferedImage img = chart.generateImage(params);
		assertEquals(null, img);
	}

}
