package structureTest;

import static org.junit.Assert.*;
import generators.Box;
import generators.Shape;

import java.util.ArrayList;
import java.util.List;

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
		fail("Not yet implemented");
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
		fail("Not yet implemented");
	}

	@Test
	public void testResetAllPositions() {
		fail("Not yet implemented");
	}

	@Test
	public void testSortAlphabetic() {
		fail("Not yet implemented");
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
		System.out.println(tree.toString());
		assertTrue(tree.toString().equals("root\r\nnode1\r\nnode2"));
	}
	
	@Test
	public void testToStringNoOtherNodes() {
		ShapeTreeNode root = new ShapeTreeNode("root", "rootKey");
		ShapeTree tree = new ShapeTree(root);
		System.out.println(tree.toString());
		assertTrue(tree.toString().equals("root"));
	}
	
	@Test
	public void testGetRightMostPosition() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLeftMostPositon() {
		fail("Not yet implemented");
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
