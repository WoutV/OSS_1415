package generators;

import java.util.ArrayList;
import java.util.List;

public class BoxTreeNode {

	String name;
	Shape shape;
	List<BoxTreeNode> children = new ArrayList<BoxTreeNode>();
	
	public BoxTreeNode(String name, List<BoxTreeNode> children){
		this.name=name;
		this.children=children;
	}
	
	public BoxTreeNode(String name){
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
	public List<BoxTreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<BoxTreeNode> children) {
		this.children = children;
	}

}
