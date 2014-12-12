package structure;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import shapes.Shape;

/**
 * This class represents a node in a ShapeTree. A node is on a certain level and has zero or more children and contains a shape.
 */
public class ShapeTreeNode {

	private String key;
	private Shape shape;
	private int level;
	private List<ShapeTreeNode> children = new ArrayList<ShapeTreeNode>();

	public ShapeTreeNode(String key){
		this.key = key;
	}
	
	/**
	 * Get the x-position.
	 * @return x The x-coordinate.
	 */
	public int getX(){
		return getShape().getxPos();
	}
	
	/**
	 * Set the x-position.
	 * @param x The x-position.
	 */
	public void setX(int x){
		getShape().setxPos(x);
	}
	
	/**
	 * Get the y-position.
	 * @return y The y-coordinate.
	 */
	public int getY(){
		return getShape().getyPos();
	}
	
	/**
	 * Set the y-position.
	 * @param y The new y-coordinate.
	 */
	public void setY(int y){
		getShape().setyPos(y);
	}
	
	/**
	 * Get the width.
	 * @return width The width of the node.
	 */
	public int getWidth(){
		return (int) getShape().getWidth();
	}
	
	/**
	 * Get the height.
	 * @return height The height of the node.
	 */
	public int getHeight(){
		return (int) getShape().getHeight();
	}
	
	/**
	 * Tells a node to sort its children. Uses a TreeSet which has the property that it sorts strings alphabetically when added.
	 */
	public void sortAlphabetic() {
		if(!getChildren().isEmpty()){
			HashMap<String, ShapeTreeNode> map = new HashMap<String, ShapeTreeNode>();
			for(ShapeTreeNode node : getChildren()){
				map.put(node.getName(), node);
			}
			Collection<String> sorted = new TreeSet<String>(Collator.getInstance());
			for(ShapeTreeNode node : getChildren()){
				sorted.add(node.getName());
			}
			ArrayList<ShapeTreeNode> list = new ArrayList<ShapeTreeNode>();
			for(String node : sorted){
				list.add(map.get(node));
			}
			setChildren(list);
		}
	}
	
	/**
	 * Representation of a node and its children as a string.
	 */
	public String toString(){
		String result = getName();
		for(ShapeTreeNode node : getChildren()){
			result = result + "\r\n" + node.getName(); 
		}
		return result;
	}

	/**
	 * Will set the x-position equal to the middle of its children.
	 */
	public void adjustToMiddleOfChildren(){
		if(!getChildren().isEmpty()){
			int[] pos = getChildrenPosition();
			int midDistance = (pos[1] - pos[0])/2;
			int x = pos[0] + midDistance;
			setX(x);
		}
	}

	
	/**
	 * Get the x-position of the left edge of the most left child and the x-position of the right edge of the most right child, will return {0,0} otherwise.
	 * @return {x1,x2} left_edge and right_edge
	 */
	private int[] getChildrenPosition(){
		ShapeTreeNode node1 = getFirstChild();
		if(node1!=null){
			int x1 = node1.getX() - node1.getWidth()/2;
			ShapeTreeNode node2 =  getLastChild();
			int x2 = node2.getX() + node2.getWidth()/2;
			int[] result = {x1,x2};
			return result;
		}
		else{
			int[] result = {0,0};
			return result;
		}
	}
	
	/**
	 * Will position children next to each other with a margin in between them.
	 * @param margin the margin between consecutive children
	 */
	public void shakeChildrenX(int margin) {
		int moved = 0;
		for(ShapeTreeNode node : getChildren()){
			node.setX(moved + node.getWidth()/2); 
			moved += node.getWidth() + margin;
		}
	}
	
	/**
	 * Will shift a node and its children by an amount x.
	 * @param x the amount to shift
	 */
	public void shift(int x){
		if(getShape()!=null){
			int newx = getX()+x;
			setX(newx);
			for(ShapeTreeNode child : getChildren()){
				child.shift(x);
			}
		}
	}
	
	/**
	 * Get the most right edge.
	 * @return the x-coordinate of the most right edge
	 */
	public int getRightEdge(){
		if(hasChildren()){
			return getLastChild().getRightEdge();
		}
		int x = getX() + getWidth()/2;
		return x;
	}
	
	/**
	 * Get the most left edge.
	 * @return the x-coordinate of the most left edge
	 */
	public int getLeftEdge(){
		if(hasChildren()){
			return getFirstChild().getLeftEdge();
		}
		int x = getX() - getWidth()/2;
		return x;
	}
	
	/**
	 * Checks if a node has children or not.
	 * @return true if it has children, false if not
	 */
	private boolean hasChildren(){
		return !getChildren().isEmpty();
	}
	
	/**
	 * Get the level of this node.
	 * @return the level of the node
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Set the level of this node
	 * @param level the level
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * Get the shape of this node
	 * @return the shape
	 */
	public Shape getShape() {
		return shape;
	}
	
	/**
	 * Set the shape of this node
	 * @param shape the shape
	 */
	public void setShape(Shape shape) {
		this.shape = shape;
	}
	
	/**
	 * Get the name of this node
	 * @return the name
	 */
	public String getName() {
		return getShape().getName();
	}
	
	/**
	 * Return the list of children.
	 * @return list of children
	 */
	public List<ShapeTreeNode> getChildren() {
		return children;
	}
	
	/**
	 * Returns the first child of this node.
	 * @return the first child
	 */
	public ShapeTreeNode getFirstChild(){
		if(getChildren().isEmpty()){
			return null;
		}
		else{
			return getChildren().get(0);
		}
	}
	
	/**
	 * Returns the last child of this node.
	 * @return the last child
	 */
	public ShapeTreeNode getLastChild(){
		if(getChildren().isEmpty()){
			return null;
		}
		else{
			return getChildren().get(getChildren().size()-1);
		}
	}
	
	/**
	 * Sets the list of children of this node.
	 * @param children The new children of this node.
	 */
	public void setChildren(List<ShapeTreeNode> children) {
		this.children = children;
	}

	/**
	 * Add a child to this node
	 * @param node The child to add.
	 */
	public void addChild(ShapeTreeNode node){
		node.setLevel(this.getLevel()+1);
		this.children.add(node);
	}
	
	/**
	 * Get the key of this node
	 * @return key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Set the key of this node
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}	
}
