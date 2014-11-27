package structureTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import shapes.Box;
import shapes.Shape;
import structure.ShapeTree;
import structure.ShapeTreeNode;

public class ShapeTreeTest {

	private ShapeTreeNode root;
	private ShapeTreeNode node1;
	private ShapeTreeNode node2;
	private ShapeTreeNode node3;
	private ShapeTreeNode node4;
	private ShapeTreeNode node5;
	private Shape box0;
	private Shape box1;
	private Shape box2;
	private Shape box3;
	
	@Before
	public void setUp() throws Exception {
		root = new ShapeTreeNode("root", "rootKey");
		node1 = new ShapeTreeNode("c", "key1");
		node2 = new ShapeTreeNode("a", "key2");
		node3 = new ShapeTreeNode("b", "key3");
		node4 = new ShapeTreeNode("z", "key4");
		node5 = new ShapeTreeNode("y", "key5");
		box0 = new Box();
		box0.setxPos(10);
		box0.setWidth(20);
		box0.setyPos(10);
		box0.setHeight(20);
		box1 = new Box();
		box1.setxPos(0);
		box1.setWidth(20);
		box1.setyPos(50);
		box1.setHeight(20);
		box2 = new Box();
		box2.setxPos(20);
		box2.setWidth(40);
		box2.setyPos(50);
		box2.setHeight(40);
		box3 = new Box();
		box3.setxPos(30);
		box3.setWidth(20);
		box3.setyPos(50);
		box3.setHeight(20);
		root.setShape(box0);
		node1.setShape(box1);
		node2.setShape(box2);
		node3.setShape(box3);
	}

	@Test
	public void testShapeTree() {
		ShapeTree tree = new ShapeTree(root);
		assertEquals(tree.getRoot(), root);
		assertTrue(tree.getNodes().size() == 1);
		assertTrue(tree.getNodes().contains(root));
	}

	@Test
	public void testLayout() {
		//Test is niet noodzakelijk
	}

	@Test
	public void testLayoutX() {
		root.addChild(node1);
		root.addChild(node2);
		node1.addChild(node3);
		node1.setLevel(1);
		node2.setLevel(1);
		node3.setLevel(2);
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node1);
		tree.addNode(node2);
		tree.addNode(node3);
		tree.layoutX(10);
		//adjustTomiddleOfChildren houdt rekening met meeste linkse en meest rechtse kant van kinderen
		assertTrue(root.getShape().getxPos() == 25);
		assertTrue(node1.getShape().getxPos() == 10);
		assertTrue(node2.getShape().getxPos() == 50);
		assertTrue(node3.getShape().getxPos() == 10);
	}

	@Test
	public void testLayoutY() {
		node1.setLevel(1);
		node2.setLevel(1);
		node3.setLevel(2);
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node1);
		tree.addNode(node2);
		tree.addNode(node3);
		tree.layoutY(10);
		assertTrue(root.getShape().getyPos() == 10);
		assertTrue(node1.getShape().getyPos() == 40);
		assertTrue(node2.getShape().getyPos() == 50);		
		assertTrue(node3.getShape().getyPos() == 90);
	}

	@Test
	public void testShiftTree() {
		root.addChild(node1);
		root.addChild(node2);
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node1);
		tree.addNode(node2);
		tree.shiftTree(30);
		assertTrue(root.getShape().getxPos() == 40);
		assertTrue(node1.getShape().getxPos() == 30);
		assertTrue(node2.getShape().getxPos() == 50);
	}

	@Test
	public void testResetAllPositions() {
		root.addChild(node1);
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node1);
		tree.resetAllPositions();
		assertTrue(root.getShape().getxPos() == 0);
		assertTrue(root.getShape().getyPos() == 0);
		assertTrue(node1.getShape().getxPos() == 0);
		assertTrue(node1.getShape().getyPos() == 0);
		
	}

	@Test
	public void testSortAlphabetic() {
		root.addChild(node1);
		root.addChild(node2);
		root.addChild(node3);
		node2.addChild(node4);
		node2.addChild(node5);
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node2);
		tree.addNode(node3);
		tree.addNode(node1);
		tree.addNode(node5);
		tree.addNode(node4);
		tree.sortAlphabetic();
		assertEquals(root.getChildren().get(0), node2);
		assertEquals(root.getChildren().get(1), node3);
		assertEquals(root.getChildren().get(2), node1);
		assertEquals(node2.getChildren().get(0), node5);
		assertEquals(node2.getChildren().get(1), node4);
	}

	@Test
	public void testGetHighestLevel() {
		node1.setLevel(5);
		node2.setLevel(1);
		node3.setLevel(2);
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node1);
		tree.addNode(node2);
		tree.addNode(node3);
		assertTrue(tree.getHighestLevel() == 5);
	}
	
	@Test
	public void testGetHighestLevelOnlyRoot() {
		ShapeTree tree = new ShapeTree(root);
		assertTrue(tree.getHighestLevel() == 0);
	}

	@Test
	public void testGetLevel() {
		node1.setLevel(1);
		node2.setLevel(1);
		node3.setLevel(2);
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node1);
		tree.addNode(node2);
		tree.addNode(node3);
		assertTrue(tree.getLevel(0).size() == 1);
		assertTrue(tree.getLevel(1).size() == 2);
		assertTrue(tree.getLevel(2).size() == 1);
		assertTrue(tree.getLevel(0).contains(root));
		assertTrue(tree.getLevel(1).contains(node1));
		assertTrue(tree.getLevel(1).contains(node2));
		assertTrue(tree.getLevel(2).contains(node3));
	}

/*	@Test
	public void testToString() {
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		ShapeTreeNode node1 = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode node2 = new ShapeTreeNode("node2", "key2");
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node1);
		tree.addNode(node2);
		assertTrue(tree.toString().equals("root\r\nnode1\r\nnode2"));
	}
	
	@Test
	public void testToStringNoOtherNodes() {
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		ShapeTree tree = new ShapeTree(root);
		assertTrue(tree.toString().equals("root"));
	}*/
	
	@Test
	public void testGetRightMostPosition() {
		ShapeTree tree = new ShapeTree(root);
		root.addChild(node1);
		root.addChild(node2);
		assertTrue(tree.getRightMostPosition() == 40);
	}

	@Test
	public void testGetLeftMostPositon() {
		ShapeTree tree = new ShapeTree(root);
		root.addChild(node1);
		root.addChild(node2);
		assertTrue(tree.getLeftMostPositon() == -10);
	}

	@Test
	public void testGetMaxHeightOfLvl() {
		node1.setLevel(1);
		node2.setLevel(1);
		node3.setLevel(2);
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node1);
		tree.addNode(node2);
		tree.addNode(node3);
		assertTrue(tree.getMaxHeightOfLvl(0) == 20);
		assertTrue(tree.getMaxHeightOfLvl(1) == 40);
		assertTrue(tree.getMaxHeightOfLvl(2) == 20);
	}

	@Test
	public void testGetTotalHeight() {
		ShapeTree tree = new ShapeTree(root);
		assertTrue(tree.getTotalHeight(20) == 40);
	}
	
	@Test
	public void testGetTotalHeightMoreLevels() {
		node1.setLevel(1);
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node1);
		assertTrue(tree.getTotalHeight(20) == 80);
	}

//	@Test
//	public void testGetRoot() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetRoot() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetNodes() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetNodes() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testAddNode() {
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node1);
		assertTrue(tree.getNodes().contains(node1));
	}

}
