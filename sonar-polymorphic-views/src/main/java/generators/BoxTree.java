package generators;

import java.util.List;

public class BoxTree {
	BoxTreeNode startNode;
	List<BoxTreeNode> nodes;
	
	public BoxTree(BoxTreeNode startNode, List<BoxTreeNode> nodes){
		this.startNode=startNode;
		this.nodes=nodes;
	}
	
	public BoxTree(BoxTreeNode startNode){
		this.startNode = startNode;
	}
	
	public BoxTreeNode getStartnode() {
		return startNode;
	}
	
	public void setStartnode(BoxTreeNode startNode) {
		this.startNode = startNode;
	}
	
	public List<BoxTreeNode> getNodes() {
		return nodes;
	}
	
	public void setNodes(List<BoxTreeNode> nodes) {
		this.nodes = nodes;
	}
	
	public void addNode(BoxTreeNode node){
		nodes.add(node);
	}
	
	
}
