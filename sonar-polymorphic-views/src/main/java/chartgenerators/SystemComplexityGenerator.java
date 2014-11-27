package chartgenerators;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import properties.ColorProperty;
import properties.Property;
import properties.ValueProperty;
import shapes.BoxGenerator;
import shapes.Line;
import shapes.Shape;
import shapes.ShapeGenerator;
import structure.ShapeTree;
import structure.ShapeTreeNode;
import be.kuleuven.cs.oss.polymorphicviews.plugin.PolymorphicChartParameters;
import be.kuleuven.cs.oss.sonarfacade.DependencyType;
import be.kuleuven.cs.oss.sonarfacade.SonarFacade;


public class SystemComplexityGenerator extends PolymorphicChartGenerator {

	private List<Shape> shapes = new ArrayList<Shape>();//The collection of shapes, displayed on the view
	private List<ShapeTree> dependencyTrees;
	private final static int LEAF_MARGIN = 20;
	private final static int TREE_MARGIN = 25;
	private final static int HEIGHT_MARGIN = 50;
	
	
	/** 
	 * This constructor creates a new system complexity generator object. 
	 * @param polyParams specify the chart that should be built
	 * @param sonar is a facade that can be used to access data about the analyzed project
	 */
	public SystemComplexityGenerator(PolymorphicChartParameters polyParams, SonarFacade sonar) {
		super(polyParams,sonar);
		
		this.dependencyTrees = buildTrees();
		
		Property<Double> width = new ValueProperty(polyParams.getBoxWidth(), PolymorphicChartParameters.DEFAULT_BOXWIDTH, measureFetcher);
		Property<Double> height = new ValueProperty(polyParams.getBoxHeight(), PolymorphicChartParameters.DEFAULT_BOXHEIGHT, measureFetcher);
		Property<Color> color = new ColorProperty(polyParams.getBoxColor(), PolymorphicChartParameters.DEFAULT_BOXCOLOR, measureFetcher);
		ShapeGenerator boxGenerator = new BoxGenerator(measureFetcher,width,height,color);
		
		shapes.addAll(Arrays.asList(boxGenerator.getShapes()));
		addShapesToTree();
	}

	/**
	 * This method generates the image to be dsisplayed as view
	 * @return the systemcomplexity view as requested as a BuffereredImage
	 */
	@Override
	public BufferedImage generateImage() {  
	    getPositions();
	    
	    setSize();
	    builder.createCanvas(height, width, BufferedImage.TYPE_INT_RGB);
	    
	    flipY();
	    createLines();
	    
	    for(Shape shape : this.shapes){
			shape.draw(this.builder);
		}

		return builder.getImage();
	}
	

	/**
	 * This methods is responsible for building the different trees, representing a hierarchy of classes.
	 * First, it will create a node for every resource.
	 * Secondly, it will isolate the nodes that have no outgoingDependencies (nodes that do not extend another class)
	 * , these nodes will become the root of a tree.
	 * Thirdly, the trees will be filled 'depth-first'-like with the remaining nodes.
	 * 
	 * @return List of all trees
	 */
	public List<ShapeTree> buildTrees(){
		HashMap<String, String> map = measureFetcher.getResourceKeysAndNames();
		
		List<ShapeTreeNode> nodes = generateNodes(map);
		List<ShapeTree> dependencyTrees = findRoots(nodes); 			
		
		for(ShapeTree ent : dependencyTrees){
			ShapeTreeNode root = ent.getRoot(); 
			dig(nodes, ent, root);
		}
		return dependencyTrees;
	}
	
	/**
	 * This method will generate a node for every resource. The node will contain the key and the name.
	 * Key is needed later because the relations between the nodes are not established yet.
	 * Since resources can have the same name but not the same key, 
	 * this makes sure that no node gets added to two nodes with the same name.
	 * 
	 * @param map containing names and keys of all resources.
	 * @return a node for every resource
	 */
	private List<ShapeTreeNode> generateNodes(HashMap<String, String> map) {
		List<ShapeTreeNode> nodes = new ArrayList<ShapeTreeNode>();
		for(String key : map.keySet()){
			ShapeTreeNode node = new ShapeTreeNode(map.get(key), key);
			nodes.add(node);
		}
		return nodes;
	}
	
	/**
	 * This method will isolate the nodes that have no outgoingDepencies (nodes that do not extend another class).
	 * These nodes will become the root of a tree.
	 * @param dependencyTrees 
	 * @param nodes
	 */
	private List<ShapeTree>  findRoots(List<ShapeTreeNode> nodes) {
		List<ShapeTree> dependencyTrees = new ArrayList<ShapeTree>();
		for(ShapeTreeNode leaf : nodes){
			List<String[]> outgoingDependencies = measureFetcher.findOutgoingDependencies(leaf.getKey());
			boolean onlyUses = true;
			for(String[] dependency: outgoingDependencies){
				if(!dependency[0].equals(DependencyType.USES.toString())){
					onlyUses = false;
				}
			}
			if(onlyUses || outgoingDependencies.isEmpty()){
				ShapeTree tree = new ShapeTree(leaf);
				dependencyTrees.add(tree);
			}
		}
		return dependencyTrees;
	}
	
	/**
	 * This will lookup the children of a node and add them to the node.
	 * This method is called recursively and will fill a tree 'depth-first'-like.
	 * @param nodes List of all nodes
	 * @param ent A tree
	 * @param parent A node of the tree
	 */
	private void dig(List<ShapeTreeNode> nodes, ShapeTree ent, ShapeTreeNode parent) {
		List<String[]> incommingDependencies = measureFetcher.findIncomingDependencies(parent.getKey());
		for(String[] dependency: incommingDependencies){
			if(!dependency[0].equals(DependencyType.USES.toString())){
				String key = dependency[1];
				for(ShapeTreeNode node : nodes){
					if(node.getKey().equals(key)){
						ent.addNode(node);
						parent.addChild(node);
						dig(nodes, ent, node);
					}
				}
			}
		}
	}
	
	/**
	 * This method will instantiate all x,y positions in all trees and set the total width of all trees.
	 */
	public void getPositions(){
		for(ShapeTree tree : dependencyTrees){
			tree.layout(LEAF_MARGIN, HEIGHT_MARGIN);
		}
		int lastTreePos = 0;
		for(ShapeTree tree: dependencyTrees){
			int leftMost = tree.getLeftMostPositon();
			int distance = lastTreePos - leftMost;
			tree.shiftTree(distance + TREE_MARGIN);
			lastTreePos = tree.getRightMostPosition();
		}
	}
	
	/**
	 * This method will add each shape to a node
	 */
	private void addShapesToTree(){
		for(ShapeTree shapeTree: dependencyTrees){
			for(ShapeTreeNode node : shapeTree.getNodes()){
				Shape shape = getShapeBy(node.getName());
				node.setShape(shape);
			}
		}
	}
	
	/**
	 * Get a shape by name.
	 * @param name the name of the shape
	 * @return the shape with the given name
	 */
	private Shape getShapeBy(String name){
		for(Shape shape : shapes){
			if(shape.getName().equals(name)){
				return shape;
			}
		}
		return null;
	}
	
	/**
	 * Will create a lines for every parent and its children.
	 */
	public void createLines(){
		for(ShapeTree shapeTree:dependencyTrees){
			int height = shapeTree.getHighestLevel();
			for(int i=0; i<height;i++){
				List<ShapeTreeNode> nodes = shapeTree.getLevel(i);
				for(ShapeTreeNode root: nodes){
					for(ShapeTreeNode node: root.getChildren()){
						createLine(root, node);
					}
				}

			}	
		}
	}
	
	/**
	 * Will create a line shape between to shapes and add it to shapes.
	 * @param parent The parent
	 * @param child The child
	 */
	public void createLine(ShapeTreeNode parent, ShapeTreeNode child){
		Shape parentShape = parent.getShape();
		int x1 = parentShape.getxPos();
		int y1 = parentShape.getyPos() - (int) parentShape.getHeight()/2;
		Shape childShape = child.getShape();
		int x2 = childShape.getxPos();
		int y2 = childShape.getyPos() + (int)childShape.getHeight()/2;
		Shape line= new Line();
		line.setxPos(x1);
		line.setyPos(y1);
		line.setWidth(x2-x1);
		line.setHeight(y2-y1);
		
		shapes.add(line);
	}
	
	/**
	 * Will flip the trees, so that they are not not upside down 
	 */
	private void flipY() {
		for(ShapeTree tree : dependencyTrees){
			for(ShapeTreeNode node : tree.getNodes()){
				Shape shape = node.getShape();
				shape.setyPos(this.height - shape.getyPos());
			}
		}
	}
	
	/**
	 * Set height and width of the plot.
	 */
	private void setSize() {
		int x = 0;
		if(!dependencyTrees.isEmpty()){
			ShapeTree tree = this.dependencyTrees.get(this.dependencyTrees.size()-1);
			x = tree.getRightMostPosition() + 2 * TREE_MARGIN;
		}
		this.width = x;
		int maxHeight = 0;
		for(ShapeTree tree: dependencyTrees){
			int height = (int) tree.getTotalHeight(HEIGHT_MARGIN) + 4 * HEIGHT_MARGIN;
			if(height > maxHeight){
				maxHeight = height;
			}
		}
		this.height = maxHeight;
	}
}
