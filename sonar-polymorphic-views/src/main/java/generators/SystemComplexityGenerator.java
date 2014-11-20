package generators;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;

public class SystemComplexityGenerator extends PolymorphicChartGenerator {

	//TODO which atributes can be placed in superclass?
	private int width;
	private int height;
	private String boxHeight;
	private String boxWidth;
	private String boxColor;
	private List<Shape> shapes;//The collection of shapes, displayed on the view
	
	
	/** 
	 * This constructor creates a new systemcomplexitygenerator object. 
	 * @param polyParams specify the chart that should be built
	 * @param sonar is a facade that can be used to access data about the analyzed project
	 */
	public SystemComplexityGenerator(PolymorphicChartParameters polyParams, SonarFacade sonar) {
		super(polyParams,sonar);
		
		this.boxHeight = polyParams.getBoxHeight();
		this.boxWidth = polyParams.getBoxWidth();
		this.boxColor = polyParams.getBoxColor();
		
		ShapeGenerator boxGenerator = new BoxGenerator(measureFetcher,polyParams);
		shapes.addAll(Arrays.asList(boxGenerator.getShapes()));
	}

	/**
	 * This method generates the image to be displayed as view
	 * @return the systemcomplexity view as requested as a BuffereredImage
	 */
	@Override
	public BufferedImage generateImage() {
		// TODO Auto-generated method stub
		Map<String,Double> boxHeights = measureFetcher.getMeasureValues(boxHeight);
		Map<String,Double> boxWidths = measureFetcher.getMeasureValues(boxWidth);
	    builder.createCanvas(height, width, BufferedImage.TYPE_INT_RGB);
	    
	    
	    buildShapes(boxWidths,boxHeights);
	    
		return builder.getImage();
	}

	public void buildShapes(Map<String, Double> xValues, Map<String, Double> yValues){
		//TODO horizontal ordering: alphabetical String.compareTo(otherString)
		//TODO vertical ordering: hierarchy + height= max height of yMetric of that row
		for(Shape shape : this.shapes){
			Double xValue = xValues.get(shape.getName());
			Double yValue = yValues.get(shape.getName());
			shape.setxPos(xValue.intValue());
			shape.setyPos(yValue.intValue());
			shape.draw(this.builder);
		}
	}
	
	public void buildLines(){
		for(Shape shape:this.shapes){
			
		}
	}
}
