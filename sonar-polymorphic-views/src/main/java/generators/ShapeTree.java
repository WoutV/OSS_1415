package generators;

import java.util.ArrayList;
import java.util.List;

public class ShapeTree {
	
	private ShapeTreeNode root;
	
	private List<ShapeTreeNode> nodes = new ArrayList<ShapeTreeNode>();
	
	public ShapeTree(ShapeTreeNode root, List<ShapeTreeNode> nodes){
		this.root=root;
		this.nodes=nodes;
	}
	
	public ShapeTree(ShapeTreeNode root){
		root.setLevel(0);
		this.root = root;
	}
	
	public ShapeTreeNode getRoot() {
		return root;
	}
	
	public void setRoot(ShapeTreeNode root) {
		this.root = root;
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
		getRoot().sortAlphabetic();
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
		String result = getRoot().getName();
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
	
//	private boolean checkLevel(int level) {
//		List<ShapeTreeNode> nodes = getXthLvl(level-1);
//		for(ShapeTreeNode node_i : nodes){
//			for(ShapeTreeNode node_j : nodes){
//				if(node_i == node_j){ 
//					//do nothing
//				}
//				else{
//					Shape shape_i = node_i.getFirstChild().getShape();
//					int left_i = shape_i.getxPos();
//					int right_i = left_i + (int) shape_i.getWidth();
//					int right_j = node_j.getFirstChild().getShape().getxPos();
//					if(left_i < )
//						
//					}
//				}
//				
//			}
//		}
//		return false;
//		
//	}
	
//	/**
//	 * This method will shake the tree, giving each group of classes that belong together positions, so that no parts of the tree overlap and are all beautifully spread out.
//	 */
//	public void shake() {
//		shakeX();
//		//shakeY();	
//	}
//	
//	public void shakeX(){
//		Shape shape = getRoot().getShape();          
//		shape.setxPos((int) -shape.getWidth()/2); //Root centered to 0
//		getRoot().shakeChildrenX(); //shake kids of root
//		//checkLevel(1); // check first level
//	}

	
}
