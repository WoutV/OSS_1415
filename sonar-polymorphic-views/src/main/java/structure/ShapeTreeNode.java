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
	private ShapeTreeNode parent;
	
	public ShapeTreeNode(String name, String key){
		this.name = name;
		this.key = key;
	}
	
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
	
	public void doLvls(){
		for(ShapeTreeNode node : getChildren()){
			node.setLevel(getLevel()+1);
			node.doLvls();
		}
	}
	
	public String toString(){
		String result = getName();
		for(ShapeTreeNode node : getChildren()){
			result = result + "\r\n" + node.getName(); 
		}
		return result;
	}

	public void adjustToMiddleOfChildren(){
		if(!getChildren().isEmpty()){
			int[] pos = getChildrenPosition();
			int midDistance = (pos[1] - pos[0])/2;
			midDistance = midDistance-((int) (shape.getWidth()/2));
			int x = pos[0] + midDistance;
			shape.setxPos(x);
		}
	}
	
	public int getChildrenWidth(){
		int width = 0;
		for(ShapeTreeNode node : getChildren()){
			width += node.getShape().getWidth();
		}
		return width;
	}
	
	public int[] getChildrenPosition(){
		if(!getChildren().isEmpty()){
			int x1 = getFirstChild().getShape().getxPos();
			Shape shape =  getLastChild().getShape();
			int x2 = shape.getxPos() + (int) shape.getWidth();
			int[] result = {x1,x2};
			return result;
		}
		int[] noChildren = {0, 0};
		return noChildren;
	}
	
	/**
	 * This method gets the width of all children
	 */
	public void shakeChildrenX(int margin) {
		int moved = 0;
		for(ShapeTreeNode node : getChildren()){
			node.getShape().setxPos(moved);
			moved += (int) node.getShape().getWidth() + margin;
		}
	}
	
	public boolean hasChildren(){
		return !getChildren().isEmpty();
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public void setShape(Shape shape) {
		this.shape = shape;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<ShapeTreeNode> getChildren() {
		return children;
	}
	
	public ShapeTreeNode getFirstChild(){
		if(children.isEmpty()){
			return null;
		}
		else{
			return children.get(0);
		}
	}
	
	public ShapeTreeNode getLastChild(){
		if(children.isEmpty()){
			return null;
		}
		else{
			return children.get(children.size()-1);
		}
	}
	
	public void setChildren(List<ShapeTreeNode> children) {
		this.children = children;
	}

	public void addChild(ShapeTreeNode node){
		this.children.add(node);
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ShapeTreeNode getParent() {
		return parent;
	}

	public void setParent(ShapeTreeNode parent) {
		this.parent = parent;
	}
	
	public void shift(int x){
		Shape shape = this.getShape();
		int newx = this.getShape().getxPos()+x;
		shape.setxPos(newx);
		for(ShapeTreeNode child : getChildren()){
			child.shift(x);
		}
	}

	public void positionChildrenAndSelfNextTo(int xcoord, int margin) {
		int x  = this.getChildrenPosition()[0];
		int distance = (xcoord + margin) - x;
		shift(distance);
	}

	public void positionSelfNextTo(int xcoord, int margin) {
		int x = this.getShape().getxPos();
		int distance = (xcoord + margin) - x;
		shift(distance);
	}
	
	public int getRightEdgeSubTree(){
		if(hasChildren()){
			return getLastChild().getRightEdgeSubTree();
		}
		int x = getShape().getxPos() + (int) getShape().getWidth();
		return x;
	}
	
	public int getLeftEdgeSubTree(){
		if(hasChildren()){
			return getFirstChild().getLeftEdgeSubTree();
		}
		return getShape().getxPos();
	}
}
