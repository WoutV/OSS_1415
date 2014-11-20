package generators;

import java.util.List;

public class ShapeTree {
	ShapeTreeNode startNode;
	List<ShapeTreeNode> nodes;
	
	public ShapeTree(ShapeTreeNode startNode, List<ShapeTreeNode> nodes){
		this.startNode=startNode;
		this.nodes=nodes;
	}
	
	public ShapeTree(ShapeTreeNode startNode){
		this.startNode = startNode;
	}
	
	public ShapeTreeNode getStartnode() {
		return startNode;
	}
	
	public void setStartnode(ShapeTreeNode startNode) {
		this.startNode = startNode;
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
	
	public void sortTreeAlphabetic(){
		for(ShapeTreeNode node : nodes){
			node.sortAlphabetic();
		}
	}
}
