package generators;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;

public class SystemComplexityGenerator extends PolymorphicChartGenerator {

	//TODO which attributes can be placed in superclass?
	private int width;//TODO make self-generated
	private int height;
	private String boxHeight;
	private String boxWidth;
	private List<Shape> shapes;//The collection of shapes, displayed on the view
	private List<ShapeTree> dependencyTrees;
	private int leafMargin = 1;
	private int treeMargin = 5;
	private int heightMargin = 5;
	
	
	/** 
	 * This constructor creates a new system complexity generator object. 
	 * @param polyParams specify the chart that should be built
	 * @param sonar is a facade that can be used to access data about the analyzed project
	 */
	public SystemComplexityGenerator(PolymorphicChartParameters polyParams, SonarFacade sonar) {
		super(polyParams,sonar);
		
		this.boxHeight = polyParams.getBoxHeight();
		this.boxWidth = polyParams.getBoxWidth();
		this.dependencyTrees= measureFetcher.getDependencyTrees();
		ShapeGenerator boxGenerator = new BoxGenerator(measureFetcher,polyParams);
		shapes.addAll(Arrays.asList(boxGenerator.getShapes()));
		addShapesToTree();
	}

	
	/**
	 * This method generates the image to be displayed as view
	 * @return the system complexity view as requested as a BuffereredImage
	 */
	@Override
	public BufferedImage generateImage() {

	
	    builder.createCanvas(height, width, BufferedImage.TYPE_INT_RGB);
	    
	    getXPositions();
	    getYPositions();    
	    createLines();
	    
	    for(Shape shape : this.shapes){
			shape.draw(this.builder);
		}

		return builder.getImage();
	}
	
	public void getXPositions(){
		for(ShapeTree tree : dependencyTrees){
			Shape master = getShapeBy(tree.getStartnode().getName());
			master.setxPos(0);
			layoutX(tree);
		}
		int tempx = 0;
		for(ShapeTree tree: dependencyTrees){
			int width = (int) tree.getMaxWidth(leafMargin);
			shiftTree(tempx + width/2, tree);
			tempx += width+treeMargin;
		}
	}

	private void layoutX(ShapeTree tree) {
		int height = tree.getHeight();
		for(int i = 1; i < height+1 ; i++){
			List<ShapeTreeNode> level = tree.getXthLvl(i);
			double lvlWidth = tree.getLvlWidthMargin(i, leafMargin);
			double tempX = 0 - lvlWidth/2;
			for(ShapeTreeNode node : level){
				node.getShape().setxPos((int) tempX);
				tempX += node.getShape().getWidth() + leafMargin;
			}
		}
	}
	
	private void layoutY(ShapeTree tree){
		int tempY = 0;
		for(int i = 0; i < tree.getHeight()+1; i++){
			List<ShapeTreeNode> level = tree.getXthLvl(i);
			double lvlHeight = tree.getMaxHeightOfLvl(i);
			for(ShapeTreeNode node : level){
				node.getShape().setyPos((int) tempY + heightMargin);
				tempY += lvlHeight + heightMargin;
			}
		}
	}
	/**
	 * Move whole tree by x.
	 * @param x
	 */
	private void shiftTree(int x, ShapeTree tree){
		List<ShapeTreeNode> nodes = tree.getNodes();
		nodes.add(tree.getStartnode());
		for(ShapeTreeNode node : nodes){
			Shape shape = node.getShape();
			shape.setxPos(shape.getxPos()+x);
		}
	}
	
	private void addShapesToTree(){
		for(ShapeTree shapeTree: dependencyTrees){
			for(ShapeTreeNode node : shapeTree.getNodes()){
				node.setShape(getShapeBy(node.getName()));
			}
		}
	}

	private Shape getShapeBy(String name){
		for(Shape shape : shapes){
			if(shape.getName().equals(name)){
				return shape;
			}
		}
		return null;
	}
	
	public void getYPositions(){
		for(ShapeTree tree : dependencyTrees){
			layoutY(tree);
		}
	}
	
	public void createLines(){
		for(ShapeTree shapeTree:dependencyTrees){
			int height = shapeTree.getHeight();
			for(int i=0; i<height;i++){
				List<ShapeTreeNode> nodes = shapeTree.getXthLvl(i);
				for(ShapeTreeNode root: nodes){
					for(ShapeTreeNode node: root.getChildren()){
						createLine(root, node);
					}
				}

			}	
		}
	}
	
	public void createLine(ShapeTreeNode node1, ShapeTreeNode node2){
		Shape shape1 = node1.getShape();
		int x1 = shape1.getxPos() + (int)shape1.getWidth()/2;
		int y1 = shape1.getyPos() + (int)shape1.getHeight();
		Shape shape2 = node2.getShape();
		int x2 = shape2.getxPos() + (int)shape1.getWidth()/2;
		int y2 = shape2.getyPos();
		Shape line= new Line();
		line.setxPos(x1);
		line.setyPos(y1);
		line.setWidth(x2-x1);
		line.setHeight(y2-y1);
		
		shapes.add(line);
	}
	
}
