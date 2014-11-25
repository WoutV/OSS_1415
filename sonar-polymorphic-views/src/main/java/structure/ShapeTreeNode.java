package structure;

import generators.Shape;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * This class represents a tree used in the system complexity view. This tree consists of one super node and a list of children.
 */
public class ShapeTreeNode {

	private String name;
	private String key;
	private Shape shape;
	private int level;
	private List<ShapeTreeNode> children = new ArrayList<ShapeTreeNode>();

	
	public ShapeTreeNode(String name, String key){
		this.name = name;
		this.key = key;
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
			getShape().setxPos(x);
		}
	}
	
	//TODO Methode ni gebruikt?
	/**
	 * Get the accumulated width of all children.
	 * @return the total width.
	 */
	private int getChildrenWidth(){
		int width = 0;
		for(ShapeTreeNode node : getChildren()){
			width += (int) node.getShape().getWidth();
		}
		return width;
	}
	
	/**
	 * Get the x-position of the left edge of the most left child and the x-position of the right edge of the most right child, will return {0,0} otherwise.
	 * @return {x1,x2} left_edge and right_edge
	 */
	private int[] getChildrenPosition(){
		if(!getChildren().isEmpty()){
			Shape shape1 = getFirstChild().getShape();
			int x1 = shape1.getxPos() - (int) shape1.getWidth()/2;
			Shape shape2 =  getLastChild().getShape();
			int x2 = shape2.getxPos() + (int) shape2.getWidth()/2;
			int[] result = {x1,x2};
			return result;
		}
		int[] noChildren = {0, 0};
		return noChildren;
	}
	
	/**
	 * Will position children next to each other with a margin in between them.
	 * @param margin the margin between consecutive children
	 */
	public void shakeChildrenX(int margin) {
		int moved = 0;
		for(ShapeTreeNode node : getChildren()){
			node.getShape().setxPos(moved + (int) node.getShape().getWidth()/2); 
			moved += (int) node.getShape().getWidth() + margin;
		}
	}
	
	//TODO Gevaarlijk voor nullpointer vermits er geen shape in de constructor wordt meegegeven?
	/**
	 * Will shift a node and its children by an amount x.
	 * @param x the amount to shift
	 */
	public void shift(int x){
		Shape shape = this.getShape();
		int newx = this.getShape().getxPos()+x;
		shape.setxPos(newx);
		for(ShapeTreeNode child : getChildren()){
			child.shift(x);
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
		int x = getShape().getxPos() + (int) (getShape().getWidth()/2);
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
		int x = getShape().getxPos() - (int) (getShape().getWidth()/2);
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
		return name;
	}
	
	/**
	 * Set the name of this node
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
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
		if(children.isEmpty()){
			return null;
		}
		else{
			return children.get(0);
		}
	}
	
	/**
	 * Returns the last child of this node.
	 * @return the last child
	 */
	public ShapeTreeNode getLastChild(){
		if(children.isEmpty()){
			return null;
		}
		else{
			return children.get(children.size()-1);
		}
	}
	
	/**
	 * Sets the list of children of this node.
	 * @param children
	 */
	public void setChildren(List<ShapeTreeNode> children) {
		this.children = children;
	}

	/**
	 * Add a child to this node
	 * @param node the child to add
	 */
	public void addChild(ShapeTreeNode node){
		node.setLevel(this.getLevel()+1);
		this.children.add(node);
	}
	
	/**
	 * Get the key of this node
	 * @return
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
