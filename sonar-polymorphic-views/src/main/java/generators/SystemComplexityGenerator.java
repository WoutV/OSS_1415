package generators;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;



public class SystemComplexityGenerator extends PolymorphicChartGenerator {

	//TODO which attributes can be placed in superclass?
	private int width = 800;//TODO make self-generated
	private int height = 800;
	private String boxHeight;
	private String boxWidth;
	private List<Shape> shapes = new ArrayList<Shape>();//The collection of shapes, displayed on the view
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
		
		this.dependencyTrees = buildTrees();
		ShapeGenerator boxGenerator = new BoxGenerator(measureFetcher,polyParams);
		
		shapes.addAll(Arrays.asList(boxGenerator.getShapes()));
		addShapesToTree();
	}

	/**
	 * This method generates the image to be dsisplayed as view
	 * @return the systemcomplexity view as requested as a BuffereredImage
	 */
	@Override
	public BufferedImage generateImage() {
	    builder.createCanvas(height, width, BufferedImage.TYPE_INT_RGB);
	    
	    resetAllPositions();
	    //shakeTrees();
	    getXPositions();
	    getYPositions(); 
	    createLines();
	    
	    for(Shape shape : this.shapes){
			shape.draw(this.builder);
		}

		return builder.getImage();
	}


	private void resetAllPositions() {
		for(ShapeTree tree : dependencyTrees){
			Shape shape = tree.getRoot().getShape();
			shape.setxPos(0);
			shape.setyPos(0);
			for(ShapeTreeNode node : tree.getNodes()){
				node.getShape().setxPos(0);
				node.getShape().setyPos(0);
			}
		}
		
	}


	public List<ShapeTree> buildTrees(){
		List<ShapeTree> dependencyTrees= new ArrayList<ShapeTree>();
		HashMap<String, String> map = measureFetcher.getResourceKeysAndNames();
		
		List<ShapeTreeNode> nodes = generateNodes(map); //create all nodes
		createRoots(dependencyTrees, nodes); 			//get roots for trees, aka trees with no incomingDependencies unless "uses".
		
		for(ShapeTree ent : dependencyTrees){ 			//get rest of trees, by adding for each root its children and their children etc...
			ShapeTreeNode root = ent.getRoot(); 
			dig(map, nodes, ent, root);
		}
		return dependencyTrees;
		
	}

	private void createRoots(List<ShapeTree> dependencyTrees,
			List<ShapeTreeNode> nodes) {
		for(ShapeTreeNode leaf : nodes){
			System.out.println("ENTER");
			List<String[]> incomingDependencies = measureFetcher.findIncomingDependencies(leaf.getKey());
			boolean isRoot = true;
			for(String[] dependency: incomingDependencies){
//				if(!dependency[0].equals("USES")){
//					isRoot = false; //TODO implement "uses" correctly
//				}
			}
			if(incomingDependencies.isEmpty()){
				isRoot = false;
			}
			if(isRoot){ //create new tree with this root
				ShapeTree tree = new ShapeTree(leaf);
				dependencyTrees.add(tree);
			}
		}
	}

	private List<ShapeTreeNode> generateNodes(HashMap<String, String> map) {
		List<ShapeTreeNode> nodes = new ArrayList<ShapeTreeNode>();
		for(String key : map.keySet()){
			ShapeTreeNode node = new ShapeTreeNode(map.get(key), key);
			nodes.add(node);
		}
		return nodes;
	}

	private void dig(HashMap<String, String> map, List<ShapeTreeNode> nodes,ShapeTree ent, ShapeTreeNode root) {
		List<String[]> outgoingDependencies = measureFetcher.findOutgoingDependencies(root.getKey());
		for(String[] dependency: outgoingDependencies){
			System.out.println("OutDependency for "+root.getName()+": "+dependency);
			if(!dependency[0].equals("USES")){
				String key = dependency[1];
				String name = map.get(key);
				for(ShapeTreeNode node : nodes){
					if(node.getName().equals(name)){
						ent.addNode(node);
						root.addChild(node);
						dig(map, nodes, ent, node);
					}
				}
			}
		}
	}
	
	public void getXPositions(){
		System.out.println("CHECK2");
		for(ShapeTree tree : dependencyTrees){
			Shape master = getShapeBy(tree.getRoot().getName());
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

	public void getYPositions(){
		for(ShapeTree tree : dependencyTrees){
			Shape master = getShapeBy(tree.getRoot().getName());
			master.setyPos(0);
			layoutY(tree);
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
		for(int i = 1; i < tree.getHeight()+1; i++){
			List<ShapeTreeNode> level = tree.getXthLvl(i);
			double lvlHeight = tree.getMaxHeightOfLvl(i-1);
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
		nodes.add(tree.getRoot());
		for(ShapeTreeNode node : nodes){
			Shape shape = node.getShape();
			shape.setxPos(shape.getxPos()+x);
		}
	}
	
	private void addShapesToTree(){
		for(ShapeTree shapeTree: dependencyTrees){
			shapeTree.getRoot().setShape(getShapeBy(shapeTree.getRoot().getName()));
			for(ShapeTreeNode node : shapeTree.getNodes()){
				Shape shape = getShapeBy(node.getName());
				node.setShape(shape);
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
	
//	/**
//	 * This method will shake each trees, this will make sure that no shapes of each tree would overlap.
//	 */
//	private void shakeTrees() {
//		for(ShapeTree tree : dependencyTrees){
//			tree.shake();
//		}
//	}
}
