package generators;

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
	
	public ShapeTreeNode(String name, List<ShapeTreeNode> children, int level){
		this.name=name;
		this.children=children;
		this.level = level;
	}
	
	public ShapeTreeNode(String name, int level){
		this.name = name;
		this.level = level;
	}
	
	public ShapeTreeNode(String name, String key){
		this.name = name;
		this.key = key;
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
			return children.get(children.size());
		}
	}
	
	public void setChildren(List<ShapeTreeNode> children) {
		this.children = children;
	}

	public void addChild(ShapeTreeNode node){
		node.setLevel(this.getLevel()+1);
		this.children.add(node);
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
	
	
	public String toString(){
		String result = getName();
		for(ShapeTreeNode node : getChildren()){
			result = result + "\r\n" + node.getName(); 
		}
		return result;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getChildrenWidth(){
		int width = 0;
		for(ShapeTreeNode node : getChildren()){
			width += node.getShape().getWidth();
		}
		return width;
	}
	
	/**
	 * This method gets the width of all children, and centers them with their parent aka. this node.
	 */
	public void shakeChildrenX() {
		int width = getChildrenWidth();
		int moved = this.getShape().getxPos()-(width/2);
		for(ShapeTreeNode node : getChildren()){
			node.getShape().setxPos(moved);
			moved += -(width/2) + node.getShape().getWidth();
		}
		
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
