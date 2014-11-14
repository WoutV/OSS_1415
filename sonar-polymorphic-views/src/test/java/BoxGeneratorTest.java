import static org.junit.Assert.*;

import generators.Box;
import generators.BoxGenerator;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sonar.api.charts.ChartParameters;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;

public class BoxGeneratorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testGetBoxes() {
		BoxGenerator bg = new BoxGenerator(null);
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("boxwidth", "2");
		inputParams.put("boxheight", "3");
		inputParams.put("boxcolor", "r0g0b0");
		ChartParameters params = new ChartParameters(inputParams);
		PolymorphicChartParameters polyParams = new PolymorphicChartParameters(params.getParams());
		
		Box[] boxes = bg.getBoxes(polyParams.getBoxWidth(),polyParams.getBoxHeight(),polyParams.getBoxColor());
		assertTrue(2 == boxes[0].getWidth());
		assertTrue(2 == boxes[1].getWidth());
		assertTrue(2 == boxes[2].getWidth());
		assertTrue(3 == boxes[0].getHeight());
		assertTrue(3 == boxes[1].getHeight());
		assertTrue(3 == boxes[2].getHeight());
		assertTrue(boxes[0].getColor().equals(new Color(0,0,0)));
		assertTrue(boxes[2].getColor().equals(new Color(0,0,0)));
		assertTrue(boxes[1].getColor().equals(new Color(0,0,0)));
	}
	
	@Test
	public void testDefaultBoxes() {
		BoxGenerator bg = new BoxGenerator(null);
		Map<String, String> inputParams = new HashMap<String, String>();
		ChartParameters params = new ChartParameters(inputParams);
		PolymorphicChartParameters polyParams = new PolymorphicChartParameters(params.getParams());
		
		Box[] boxes = bg.getBoxes(polyParams.getBoxWidth(),polyParams.getBoxHeight(),polyParams.getBoxColor());
		assertTrue(boxes[0].getWidth() == Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXWIDTH));
		assertTrue(boxes[1].getWidth() == Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXWIDTH));
		assertTrue(boxes[2].getWidth() == Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXWIDTH));
		assertTrue(boxes[0].getHeight() == Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXHEIGHT));
		assertTrue(boxes[1].getHeight() == Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXHEIGHT));
		assertTrue(boxes[2].getHeight() == Double.parseDouble(PolymorphicChartParameters.DEFAULT_BOXHEIGHT));
		//TODO nog iets met default color zodat die hier automatisch wordt geladen ipv new color te moeten maken .
		assertTrue(boxes[0].getColor().equals(new Color(255,255,255)));
		assertTrue(boxes[1].getColor().equals(new Color(255,255,255)));
		assertTrue(boxes[2].getColor().equals(new Color(255,255,255)));
	}
	//TODO groot vraagteken: is het mogelijk om te testen dat de juiste metric wordt gebruikt enzo? 
	
	

}
