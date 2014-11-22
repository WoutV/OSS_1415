package structure;

import generators.Shape;

import java.util.ArrayList;
import java.util.List;

public class ShapeTreePartition {
	
	private List<ShapeTreeNode> nodes = new ArrayList<ShapeTreeNode>();
	private ShapeTreeNode parent = null;
	private int level;
	private int index;
	private int margin = 2;
	
	public ShapeTreePartition(ShapeTreeNode parent){
		this.parent = parent;
	}
	
	public void positionNodes(){
		int currentx = 0;
		for(ShapeTreeNode node : nodes){
			Shape shape = node.getShape();
			shape.setxPos(currentx);
			currentx += node.getShape().getxPos() + node.getShape().getWidth() + margin;
		}
	}
	
	public void shiftLevel(int x){
		for(ShapeTreeNode node : nodes){
			Shape shape = node.getShape();
			shape.setxPos(shape.getxPos()+x);
		}
	}
	
	public List<ShapeTreeNode> getNodes() {
		return nodes;
	}
	
	public void setNodes(List<ShapeTreeNode> nodes) {
		this.nodes = nodes;
	}
	
	public ShapeTreeNode getParent() {
		return parent;
	}
	
	public void setParent(ShapeTreeNode parent) {
		this.parent = parent;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	
	
}
