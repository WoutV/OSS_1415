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
	}
	
	/**
	 * Layout will give each node a (x,y)-position. So that a tree can be visualized clearly. 
	 * 
	 * @param leafMargin a margin between children
	 * @param heightMargin a margin between levels
	 */
	public void layout(int leafMargin, int heightMargin){
		layoutX(leafMargin);
		layoutY(heightMargin);
	}
	
	/**
	 * Determine all x-positions.
	 * @param leafMargin a margin between children
	 */
	public void layoutX(int leafMargin) {
		getRoot().getShape().setxPos(0);
		int height = getAmountOfLevels();
		for(int i = 1; i < height+1 ; i++){
			List<ShapeTreeNode> level = getLevel(i);
			double lvlWidth = getLvlWidthMargin(i, leafMargin);
			double tempX = 0 - lvlWidth/2;
			for(ShapeTreeNode node : level){
				node.getShape().setxPos((int) tempX);
				tempX += node.getShape().getWidth() + leafMargin;
			}
		}
	}
	
	/**
	 * Determine all y-positions.
	 * @param heightMargin a margin between levels
	 */
	public void layoutY(int heightMargin){
		getRoot().getShape().setyPos(0);
		int tempY = 0;
		for(int i = 1; i < getAmountOfLevels()+1; i++){
			List<ShapeTreeNode> level = getLevel(i);
			double lvlHeight = getMaxHeightOfLvl(i-1);
			for(ShapeTreeNode node : level){
				node.getShape().setyPos((int) tempY + heightMargin);
			}
			tempY += lvlHeight + heightMargin;
		}
	}
	
	/**
	 * Shift all nodes of a tree by x.
	 * @param x the distance to shift
	 */
	public void shiftTree(int x){
		List<ShapeTreeNode> nodes = getNodes();
		nodes.add(getRoot());
		for(ShapeTreeNode node : nodes){
			Shape shape = node.getShape();
			shape.setxPos(shape.getxPos()+x);
		}
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
		getRoot().sortAlphabetic();
		for(ShapeTreeNode node : nodes){
			node.sortAlphabetic();
		}
	}
	
	/**
	 * Gets the amount of levels of this tree.
	 * @return the amount of levels
	 */
	public int getAmountOfLevels(){
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
	 * Get the width of level x including a margin.
	 * @param x
	 * @param margin
	 * @return
	 */
	public double getLvlWidthMargin(int x, int margin){
		List<ShapeTreeNode> nodes = getLevel(x);
		double width = 0;
		for(ShapeTreeNode node : nodes){
			width+=node.getShape().getWidth();
		}
		return width += (getLevel(x).size()-1) * margin;
	}
	
	/**
	 * Get a representation of a tree.
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
	 * Get the maximum width of a tree.
	 * @param margin the margin between the each node.
	 * @return The width of the level with the biggest width of the tree.
	 */
	public double getMaxWidth(int margin){
		int height = getAmountOfLevels();
		double maxWidth = 0;
		for(int i = 0; i < height+1; i++){
			double width = getLvlWidthMargin(i, margin);
			if(width > maxWidth){
				maxWidth = width;
			}
		}
		return maxWidth;
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
		for(int i = 0; i < getAmountOfLevels()+1;i++){
			temp += getMaxHeightOfLvl(i);
		}
		temp += getAmountOfLevels()-1 * heightMargin;
		return temp;
	}
	
	public ShapeTreeNode getRoot() {
		return root;
	}
	
	public void setRoot(ShapeTreeNode root) {
		this.root = root;
	}
	
	public List<ShapeTreeNode> getNodes() {
		return nodes;
	}
	
	public void setNodes(List<ShapeTreeNode> nodes) {
		this.nodes = nodes;
	}
	
	public void addNode(ShapeTreeNode node){
		nodes.add(node);
	}
	
	//new method
	public void layoutA(){
		for(int currentLevel = 2; currentLevel <= getAmountOfLevels(); currentLevel++){
			//get nodes of one level higher
			List<ShapeTreeNode> nodes = getLevel(currentLevel-1);
			ShapeTreeNode previous = null;
			
			for(ShapeTreeNode node : nodes){
				if(previous != null){
					fixOverlap();
				}
				previous = node;
			}
		}
	}
	
	public void shake(){
		root.getShape().setxPos(0);
		root.shakeChildrenX();
		for(ShapeTreeNode node : nodes){
			node.shakeChildrenX();
		}
	}
	
	private void fixOverlap() {
		// TODO Auto-generated method stub
		
	}
}