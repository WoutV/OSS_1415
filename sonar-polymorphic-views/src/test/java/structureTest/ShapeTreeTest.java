package structureTest;

import static org.junit.Assert.*;
import generators.Box;
import generators.Shape;

import org.junit.Before;
import org.junit.Test;

import structure.ShapeTree;
import structure.ShapeTreeNode;

public class ShapeTreeTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testShapeTree() {
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		ShapeTree tree = new ShapeTree(root);
		assertEquals(tree.getRoot(), root);
		assertTrue(tree.getNodes().size() == 1);
		assertTrue(tree.getNodes().contains(root));
	}

	@Test
	public void testLayout() {
		fail("Not yet implemented");
	}

	@Test
	public void testLayoutX() {
		fail("Not yet implemented");
	}

	@Test
	public void testLayoutY() {
		fail("Not yet implemented");
	}

	@Test
	public void testShiftTree() {
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		ShapeTreeNode node1 = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode node2 = new ShapeTreeNode("node2", "key2");
		root.addChild(node1);
		root.addChild(node2);
		Shape box0 = new Box();
		box0.setxPos(50);
		Shape box1 = new Box();
		box1.setxPos(20);
		Shape box2 = new Box();
		box2.setxPos(70);
		root.setShape(box0);
		node1.setShape(box1);
		node2.setShape(box2);
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node1);
		tree.addNode(node2);
		tree.shiftTree(30);
		assertTrue(root.getShape().getxPos() == 80);
		assertTrue(node1.getShape().getxPos() == 50);
		assertTrue(node2.getShape().getxPos() == 100);
	}

	@Test
	public void testResetAllPositions() {
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		ShapeTreeNode node1 = new ShapeTreeNode("node1", "key1");
		root.addChild(node1);
		Shape box0 = new Box();
		box0.setxPos(50);
		box0.setyPos(10);
		Shape box1 = new Box();
		box1.setxPos(20);
		box1.setyPos(50);
		root.setShape(box0);
		node1.setShape(box1);
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node1);
		tree.resetAllPositions();
		assertTrue(root.getShape().getxPos() == 0);
		assertTrue(root.getShape().getyPos() == 0);
		assertTrue(node1.getShape().getxPos() == 0);
		assertTrue(node1.getShape().getyPos() == 0);
		
	}

	//TODO
	@Test
	public void testSortAlphabetic() {
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		ShapeTreeNode node1 = new ShapeTreeNode("c", "key1");
		ShapeTreeNode node2 = new ShapeTreeNode("a", "key2");
		ShapeTreeNode node3 = new ShapeTreeNode("b", "key3");
		ShapeTreeNode node4 = new ShapeTreeNode("z", "key4");
		ShapeTreeNode node5 = new ShapeTreeNode("y", "key5");
		root.addChild(node1);
		root.addChild(node2);
		root.addChild(node3);
		node2.addChild(node4);
		node2.addChild(node5);
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node1);
		tree.addNode(node2);
		tree.addNode(node3);
		tree.addNode(node4);
		tree.addNode(node5);
		tree.sortAlphabetic();
		assertEquals(tree.getNodes().get(0), root);
		assertEquals(tree.getNodes().get(1), node2);
		assertEquals(tree.getNodes().get(2), node5);
		assertEquals(tree.getNodes().get(3), node4);
		assertEquals(tree.getNodes().get(4), node3);
		assertEquals(tree.getNodes().get(5), node1);
	}

	@Test
	public void testGetHighestLevel() {
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		ShapeTreeNode node1 = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode node2 = new ShapeTreeNode("node2", "key2");
		ShapeTreeNode node3 = new ShapeTreeNode("node3", "key3");
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
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		ShapeTree tree = new ShapeTree(root);
		assertTrue(tree.getHighestLevel() == 0);
	}

	@Test
	public void testGetLevel() {
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		ShapeTreeNode node1 = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode node2 = new ShapeTreeNode("node2", "key2");
		ShapeTreeNode node3 = new ShapeTreeNode("node3", "key3");
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
	}
	
	@Test
	public void testGetRightMostPosition() {
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		ShapeTreeNode node1 = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode node2 = new ShapeTreeNode("node2", "key2");
		node1.setLevel(1);
		Shape box0 = new Box();
		box0.setWidth(10);
		box0.setxPos(10);
		Shape box1 = new Box();
		box1.setWidth(20);
		box1.setxPos(0);
		Shape box2 = new Box();
		box2.setWidth(40);
		box2.setxPos(30);
		root.setShape(box0);
		node1.setShape(box1);
		node2.setShape(box2);
		ShapeTree tree = new ShapeTree(root);
		root.addChild(node1);
		root.addChild(node2);
		assertTrue(tree.getRightMostPosition() == 50);
	}

	@Test
	public void testGetLeftMostPositon() {
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		ShapeTreeNode node1 = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode node2 = new ShapeTreeNode("node2", "key2");
		node1.setLevel(1);
		Shape box0 = new Box();
		box0.setWidth(10);
		box0.setxPos(10);
		Shape box1 = new Box();
		box1.setWidth(20);
		box1.setxPos(0);
		Shape box2 = new Box();
		box2.setWidth(40);
		box2.setxPos(30);
		root.setShape(box0);
		node1.setShape(box1);
		node2.setShape(box2);
		ShapeTree tree = new ShapeTree(root);
		root.addChild(node1);
		root.addChild(node2);
		assertTrue(tree.getLeftMostPositon() == -10);
	}

	@Test
	public void testGetMaxHeightOfLvl() {
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		ShapeTreeNode node1 = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode node2 = new ShapeTreeNode("node2", "key2");
		ShapeTreeNode node3 = new ShapeTreeNode("node3", "key3");
		node1.setLevel(1);
		node2.setLevel(1);
		node3.setLevel(2);
		Shape box1 = new Box();
		box1.setHeight(20);
		Shape box2 = new Box();
		box2.setHeight(40);
		root.setShape(box2);
		node1.setShape(box1);
		node2.setShape(box2);
		node3.setShape(box1);
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node1);
		tree.addNode(node2);
		tree.addNode(node3);
		assertTrue(tree.getMaxHeightOfLvl(0) == 40);
		assertTrue(tree.getMaxHeightOfLvl(1) == 40);
		assertTrue(tree.getMaxHeightOfLvl(2) == 20);
	}

	@Test
	public void testGetTotalHeight() {
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		Shape box = new Box();
		box.setHeight(50);
		root.setShape(box);
		ShapeTree tree = new ShapeTree(root);
		assertTrue(tree.getTotalHeight(20) == 70);
	}
	
	@Test
	public void testGetTotalHeightMoreLevels() {
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		ShapeTreeNode node1 = new ShapeTreeNode("node1", "key1");
		Shape box = new Box();
		box.setHeight(50);
		root.setShape(box);
		node1.setShape(box);
		node1.setLevel(1);
		ShapeTree tree = new ShapeTree(root);
		tree.addNode(node1);
		assertTrue(tree.getTotalHeight(20) == 140);
	}

	@Test
	public void testGetRoot() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetRoot() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNodes() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetNodes() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNode() {
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		ShapeTree tree = new ShapeTree(root);
		ShapeTreeNode node1 = new ShapeTreeNode("node1", "key1");
		tree.addNode(node1);
		assertTrue(tree.getNodes().contains(node1));
	}

}
