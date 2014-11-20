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

	String name;
	Shape shape;
	List<ShapeTreeNode> children = new ArrayList<ShapeTreeNode>();
	
	public ShapeTreeNode(String name, List<ShapeTreeNode> children){
		this.name=name;
		this.children=children;
	}
	
	public ShapeTreeNode(String name){
		this.name = name;
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
	
	public void setChildren(List<ShapeTreeNode> children) {
		this.children = children;
	}

	public void addChild(ShapeTreeNode node){
		this.children.add(node);
	}
	
//	public void sortAlphabetic() {
//		if(!getChildren().isEmpty()){
//			List<ShapeTreeNode> list = new ArrayList<ShapeTreeNode>();
//			int min = Integer.MIN_VALUE;
//			ShapeTreeNode current = null;
//			int count = 0;
//			HashMap<ShapeTreeNode, Integer> map = getStringMap();
//			int size = map.size();
//			while(count<size){
//				for(ShapeTreeNode node : map.keySet()){
//					int value = map.get(node);
//					if(value < min || min == Integer.MIN_VALUE){
//						min = value;
//						current = node;
//					}
//					count++;
//				}
//				list.add(current);
//				map.remove(current);
//			}
//			children = list;
//		}
//	}
	
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
	
//	public HashMap<ShapeTreeNode, Integer> getStringMap(){
//		HashMap<ShapeTreeNode, Integer> map = new HashMap<ShapeTreeNode, Integer>();
//		map.put(children.get(0),0);
//		String referenceString = children.get(0).getName();
//		int i = 1;
//		while(i < children.size()){
//			ShapeTreeNode nextNode = children.get(i);
//			map.put(nextNode, referenceString.compareTo(nextNode.getName()));
//			i++;
//		}
//		return map;
//	}
	
	public String toString(){
		String result = getName();
		for(ShapeTreeNode node : getChildren()){
			result = result + "\r\n" + node.getName(); 
		}
		return result;
	}
}
