package generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	public void sortAlphabetic() {
		List<ShapeTreeNode> list = new ArrayList<ShapeTreeNode>();
		int min = Integer.MIN_VALUE;
		ShapeTreeNode current = null;
		int count = 0;
		HashMap<ShapeTreeNode, Integer> map = getStringMap();
		while(count<map.size()){
			for(ShapeTreeNode node : map.keySet()){
				int value = map.get(node);
				if(value < min || min == Integer.MIN_VALUE){
					min = value;
					current = node;
				}
				count++;
			}
			list.add(current);
			map.remove(current);
		}
		children = list;
	}
	
	public HashMap<ShapeTreeNode, Integer> getStringMap(){
		HashMap<ShapeTreeNode, Integer> map = new HashMap<ShapeTreeNode, Integer>();
		map.put(children.get(0),0);
		String referenceString = children.get(0).getName();
		int i = 1;
		while(i < children.size()){
			ShapeTreeNode nextNode = children.get(i);
			map.put(nextNode, referenceString.compareTo(nextNode.getName()));
			i++;
		}
		return map;
	}

}
