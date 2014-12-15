package structureTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

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
	private Shape box4;
	private Shape box5;
	
	@Before
	public void setUp() throws Exception {
		box0 = new Box(20,20,"root","root",new Color(25,25,25));
		box0.setxPos(10);
		box0.setyPos(10);
		box1 = new Box(20,20,"node1","node1",new Color(25,25,25));
		box1.setxPos(0);
		box1.setyPos(50);
		box2 = new Box(40,40,"node2","node2",new Color(25,25,25));
		box2.setxPos(20);
		box2.setyPos(50);
		box3 = new Box(20,20,"node3","node3",new Color(25,25,25));
		box3.setxPos(30);
		box3.setyPos(50);
		box4 = new Box(20,20,"node4","node4",new Color(25,25,25));
		box4.setxPos(30);
		box4.setyPos(50);
		box5 = new Box(20,20,"node5","node5",new Color(25,25,25));
		box5.setxPos(30);
		box5.setyPos(50);
		root = new ShapeTreeNode(box0);
		node1 = new ShapeTreeNode(box1);
		node2 = new ShapeTreeNode(box2);
		node3 = new ShapeTreeNode(box3);
		node4 = new ShapeTreeNode(box4);
		node5 = new ShapeTreeNode(box5);
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
		assertTrue(root.getShape().getxPos() == 35);
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
		assertEquals(root.getChildren().get(0), node1);
		assertEquals(root.getChildren().get(1), node2);
		assertEquals(root.getChildren().get(2), node3);
		assertEquals(node2.getChildren().get(0), node4);
		assertEquals(node2.getChildren().get(1), node5);
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
