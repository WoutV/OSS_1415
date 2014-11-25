package structure;

import generators.Shape;

import java.util.ArrayList;
import java.util.List;

public class ShapeTree {
	
	private ShapeTreeNode root;
	
	private List<ShapeTreeNode> nodes = new ArrayList<ShapeTreeNode>();
	
	public ShapeTree(ShapeTreeNode root){
		root.setLevel(0);
		this.root = root;
		this.nodes.add(root);
	}
	
	/**
	 * Layout will give each node a (x,y)-position. So that a tree can be visualized clearly. 
	 * 
	 * @param leafMargin a margin between children
	 * @param heightMargin a margin between levels
	 */
	public void layout(int leafMargin, int heightMargin){
		resetAllPositions();
		layoutX(leafMargin);
		layoutY(heightMargin);
	}
	
	/**
	 * The the algorithm that will layout the tree's x positions. The tree is sorted bottom-up.
	 * First the bottom level will be sorted. This level isn't sorted relative to other levels.
	 * Second, go from bottom to top. At each level, each node will be positioned in the middle of its children.
	 * Then, each node will be positioned next to the right most point of the subtree to the left.
	 * After every node in a level is repositioned, this method checks the maximum distance between two nodes at the level. All nodes
	 * will then be repositioned according to this maximum distance, so that the middle of every node on a level (bottom level not included) 
	 * is positioned equally far away.
	 * @param leafMargin a minimum margin between nodes.
	 */
	public void layoutX(int leafMargin){
		sortTreeAlphabetic();
		layoutBottom(leafMargin);
		for(int i = getHighestLevel()-1 ; i >= 0 ; i--){
			List<ShapeTreeNode> nodes = getLevel(i);
			List<ShapeTreeNode> checked = new ArrayList<ShapeTreeNode>();
			int whichNode = 0;
			int maxDistance = 0;
			for(ShapeTreeNode node : nodes){
				node.adjustToMiddleOfChildren(); 
				if(!checked.isEmpty()){
					ShapeTreeNode other = checked.get(whichNode-1);
					positionsNodesRelativeTo(leafMargin, other, node);
					int difference = node.getShape().getxPos() - (other.getShape().getxPos());
					if(difference > maxDistance){
						maxDistance = difference;
					}
				}
				checked.add(node);
				whichNode += 1;
			}
			adjustLevelSpacing(i, maxDistance);
		}
	}

	/**
	 * Makes sure that between all consecutive nodes the distance is equal.
	 * @param level the level
	 * @param maxDistance the distance between each node.
	 */
	private void adjustLevelSpacing(int level, int maxDistance) {
		List<ShapeTreeNode> nodes = getLevel(level);
		ShapeTreeNode previous = null;
		for(ShapeTreeNode node : nodes){
			if(previous != null){
				int currentDistance = node.getShape().getxPos() - previous.getShape().getxPos();
				int toMove = maxDistance - currentDistance;
				node.shift(toMove);
			}
			previous = node;
		}
	}

	/**
	 * Position a node's subtree next to the most right edge of the subtree of an other node.
	 * @param leafMargin the distance the two subtrees will be apart from eachother
	 * @param other a node
	 * @param node a node
	 */
	private void positionsNodesRelativeTo(int leafMargin, ShapeTreeNode other, ShapeTreeNode node) {
		int rightX = other.getRightEdgeSubTree();
		int leftX = node.getLeftEdgeSubTree();
		int difference = rightX - leftX;
		node.shift(difference + leafMargin);
	}

	/**
	 * Position the bottem layer of the tree.
	 * @param leafMargin
	 */
	private void layoutBottom(int leafMargin) {
		if(getHighestLevel() >= 1){
			List<ShapeTreeNode> parentsOfBottom = getLevel(getHighestLevel()-1);
			for(ShapeTreeNode parent : parentsOfBottom){
				parent.shakeChildrenX(leafMargin);
			}
		}
		
	}
	/**
	 * Determine all y-positions.
	 * @param heightMargin a margin between levels
	 */
	public void layoutY(int heightMargin){
		int currentHeight = 0;
		for(int i = 0; i <= getHighestLevel(); i++){
			List<ShapeTreeNode> level = getLevel(i);
			double lvlHeight = getMaxHeightOfLvl(i);
			for(ShapeTreeNode node : level){
				int y = (int) currentHeight + (int) node.getShape().getHeight()/2;
				node.getShape().setyPos(y);
			}
			currentHeight += lvlHeight + heightMargin;
		}
	}
	
	/**
	 * Shift all nodes of a tree by x.
	 * @param x the distance to shift
	 */
	public void shiftTree(int x){
		getRoot().shift(x);
	}
	
	
	/**
	 * Sets the right level for every node and resets every node's position to (0,0).
	 */
	public void resetAllPositions(){
		getRoot().doLvls();
		Shape shape = getRoot().getShape();
		shape.setxPos(0);
		shape.setyPos(0);
		for(ShapeTreeNode node : getNodes()){
			node.getShape().setxPos(0);
			node.getShape().setyPos(0);
		}
	}
	
	/**
	 * Will sort a tree. This will call each node and tell them to sort its children.
	 */
	public void sortTreeAlphabetic(){
		for(ShapeTreeNode node : nodes){
			node.sortAlphabetic();
		}
	}
	
	/**
	 * Gets the amount of levels of this tree.
	 * @return the amount of levels
	 */
	public int getHighestLevel(){
		int height = -1;
		for(ShapeTreeNode node : nodes){
			int temp = node.getLevel();
			if(temp>height){
				height = temp;
			}
		}
		return height;
	}
	
	/**
	 * Get all nodes on level x. 
	 * @param x the level
	 * @return
	 */
	public List<ShapeTreeNode> getLevel(int x){
		ArrayList<ShapeTreeNode> list = new ArrayList<ShapeTreeNode>();
		for(ShapeTreeNode node : getNodes()){
			if(node.getLevel() == x){
				list.add(node);
			}
		}
		return list;
	}

	/**
	 * Get a textual representation of a tree.
	 * @return representation in form of a string
	 */
	public String toString(){
		String result = getRoot().getName();
		for(ShapeTreeNode node : getNodes()){
			result = result + "\r\n" + node.toString(); 
		}
		return result;
	}

	/**
	 * Gets the most right edge of the tree.
	 * @return the x-coordinate of the most right edge
	 */
	public int getRightMostPosition(){
		return this.getRoot().getRightEdgeSubTree();
	}
	
	/**
	 * Gets the most left edge of the tree.
	 * @return the x-coordinate of the most left edge
	 */
	public int getLeftMostPositon(){
		return this.getRoot().getLeftEdgeSubTree();
	}
	
	/**
	 * Get the maximum height of level x.
	 * @param x the level
	 * @return the height of level x
	 */
	public double getMaxHeightOfLvl(int x){
		double maxHeight = 0;
		List<ShapeTreeNode> nodes = getLevel(x);
		for(ShapeTreeNode node : nodes){
			double height = node.getShape().getHeight();
			if(height > maxHeight){
				maxHeight = height;
			}
		}
		return maxHeight;
	}
	
	/**
	 * Get the total height of the tree.
	 * @return height of the tree
	 */
	public double getTotalHeight(int heightMargin){
		int temp = 0;
		for(int i = 0; i < getHighestLevel()+1;i++){
			temp += getMaxHeightOfLvl(i) + heightMargin;
		}
		return temp;
	}
	
	/**
	 * Get the root of the tree
	 * @return the root
	 */
	public ShapeTreeNode getRoot() {
		return root;
	}
	
	/**
	 * Set the root of the tree
	 * @param root
	 */
	public void setRoot(ShapeTreeNode root) {
		this.root = root;
	}
	
	/**
	 * Get all the nodes of the tree
	 * @return
	 */
	public List<ShapeTreeNode> getNodes() {
		return nodes;
	}
	
	/**
	 * Set all the nodes of the tree
	 * @param nodes
	 */
	public void setNodes(List<ShapeTreeNode> nodes) {
		this.nodes = nodes;
	}
	
	/**
	 * Add a node to the tree nodes
	 * @param node
	 */
	public void addNode(ShapeTreeNode node){
		nodes.add(node);
	}
	

}