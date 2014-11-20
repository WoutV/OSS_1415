package generators;

import java.util.ArrayList;
import java.util.List;

public class ShapeTree {
	
	private ShapeTreeNode startNode;
	
	private List<ShapeTreeNode> nodes = new ArrayList<ShapeTreeNode>();
	
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
		getStartnode().sortAlphabetic();
		for(ShapeTreeNode node : nodes){
			node.sortAlphabetic();
		}
	}
	
	public String toString(){
		String result = getStartnode().getName();
		for(ShapeTreeNode node : getNodes()){
			result = result + "\r\n" + node.toString(); 
		}
		return result;
	}
}
