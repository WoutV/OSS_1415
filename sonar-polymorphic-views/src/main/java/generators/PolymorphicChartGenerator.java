package generators;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.ResourceQualifier;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;
import chartbuilder.ChartBuilder;
import chartbuilder.Java2DBuilder;

/**
 * @author wout
 *
 */
/**
 * @author wout
 *
 */
public abstract class PolymorphicChartGenerator {
	protected ChartBuilder builder;
	protected int boxWidth;
	protected int boxHeight;
	protected int boxColor;
	protected MeasureFetcher measureFetcher;
	protected Box[] boxes;
	public PolymorphicChartGenerator(PolymorphicChartParameters params, SonarFacade sonar) {
		this.builder = new Java2DBuilder();
		
		ResourceQualifier resources = params.getResources();
		String parent = params.getParent();
		this.measureFetcher = new MeasureFetcher(resources,parent,sonar);
		int amount = measureFetcher.getNumberOfResources();
		this.boxes = new Box[amount];
		generateBoxes(params);
		
	}

	public abstract BufferedImage generateImage();
	
	
	/**
	 * This method initiates the boxes that will be drawn on the image. Depending on user input, 
	 * all boxes can have the same dimensions, or their dimensions will be depending on a given
	 * metric value.
	 * @param params
	 * 
	 */
	private void generateBoxes(PolymorphicChartParameters params){
		nameBoxes();
		
		String width = params.getBoxWidth();
		String height = params.getBoxHeight();
		String color = params.getBoxColor();
		
		List<Double> widthList = getBoxDimension(width);
		List<Double> heightList =getBoxDimension(height);
		List<Color> colorList = getBoxColors(color);
		for(int i = 0;i<boxes.length;i++) {
			boxes[i].setHeight(heightList.get(i));
			boxes[i].setWidth(widthList.get(i));
			boxes[i].setColor(colorList.get(i));
		}
		
		
		
	}

	private List<Color> getBoxColors(String color) {
		List<Color> result = new ArrayList<Color>();
		try { 
			Color rgb = parseColor(color);
		} catch (IllegalArgumentException e) {
			//TODO kleur met metrics en overgang en shizzle
		}
		return result;
	}

	private Color parseColor(String color) throws IllegalArgumentException{
		Integer[] result = new Integer[3];
		String[] rgb = color.split("x");
		for(int i = 0;i<rgb.length;i++) {
			try {
				int x = Integer.parseInt(rgb[i]);
				result[i]=x;
			} catch(NumberFormatException e) {
				throw new IllegalArgumentException();
			}
		}
		return new Color(result[0],result[1],result[2]);
	}

	private List<Double> getBoxDimension(String dimension) {
		List<Double> dimensions;
		try{
			Double parsedDimension = Double.parseDouble(dimension);
			dimensions = getBoxSize(parsedDimension);
		} catch(NumberFormatException e) {
			Map<String,Double> values = measureFetcher.getMeasureValues(dimension);
			dimensions = getBoxSize(values);
		}
		return dimensions;
	}

	private List<Double> getBoxSize(Double bwidth) {
		List<Double> result = new ArrayList<Double>();
		for(int i = 0; i<boxes.length; i++) {
			result.add((Double) bwidth);
		}
		return result;
	}

	private void nameBoxes() {
		List<String> names = measureFetcher.getResourceNames();
		int i = 0;
		for(String s : names) {
			boxes[i].setName(s);
			i++;
		}
	}

	
	/**
	 * This method sets the height of all the boxes to the values in the given map. The names in the map
	 * specify which box gets which height.
	 * @param values
	 */
	private List<Double> getBoxSize(Map<String, Double> values) {
		List<Double>result = new ArrayList<Double>();
		for(int i = 0; i<boxes.length; i++) {
			for(String s : values.keySet()) {
				if(boxes[i].getName().equals(s)) {
					result.add(values.get(s));
				}
			}
		}
		return result;
	}
}
