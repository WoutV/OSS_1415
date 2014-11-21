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
	
	public int getHeight(){
		int height = -1;
		for(ShapeTreeNode node : nodes){
			int temp = node.getLevel();
			if(temp>height){
				height = temp;
			}
		}
		return height;
	}
	public List<ShapeTreeNode> getXthLvl(int x){
		ArrayList<ShapeTreeNode> list = new ArrayList<ShapeTreeNode>();
		for(ShapeTreeNode node : getNodes()){
			if(node.getLevel() == x){
				list.add(node);
			}
		}
		return list;
	}
	
	public double getLvlWidth(int x){
		List<ShapeTreeNode> nodes = getXthLvl(x);
		double width = 0;
		for(ShapeTreeNode node : nodes){
			width+=node.getShape().getWidth();
		}
		return width;
	}
	
	public double getLvlWidthMargin(int x, int margin){
		double width = getLvlWidth(x);
		if(margin == 0){
			return width;
		}
		else{
			width += (getXthLvl(x).size()-1) * margin;
			return width;
		}
	}
	
	public String toString(){
		String result = getStartnode().getName();
		for(ShapeTreeNode node : getNodes()){
			result = result + "\r\n" + node.toString(); 
		}
		return result;
	}
	
	public double getMaxWidth(int margin){
		int height = getHeight();
		double maxWidth = 0;
		for(int i = 0; i < height+1; i++){
			double width = getLvlWidthMargin(i, margin);
			if(width > maxWidth){
				maxWidth = width;
			}
		}
		return maxWidth;
	}
	
	public double getMaxHeightOfLvl(int x){
		double maxHeight = 0;
		List<ShapeTreeNode> nodes = getXthLvl(x);
		for(ShapeTreeNode node : nodes){
			double height = node.getShape().getHeight();
			if(height > maxHeight){
				maxHeight = height;
			}
		}
		return maxHeight;
	}
}
