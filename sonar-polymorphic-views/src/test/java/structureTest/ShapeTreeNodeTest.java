package structureTest;

import static org.junit.Assert.*;
import generators.Box;
import generators.Shape;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import structure.ShapeTreeNode;

public class ShapeTreeNodeTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testShapeTreeNode() {
		ShapeTreeNode newNode = new ShapeTreeNode("node", "key");
		assertTrue(newNode.getName().equals("node"));
		assertTrue(newNode.getKey().equals("key"));
	}

	@Test
	public void testSortAlphabetic() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode child1 = new ShapeTreeNode("a", "key2");
		ShapeTreeNode child2 = new ShapeTreeNode("b", "key3");
		ShapeTreeNode child3 = new ShapeTreeNode("c", "key3");
		root.addChild(child3);
		root.addChild(child1);
		root.addChild(child2);
		root.sortAlphabetic();
		assertEquals(root.getChildren().get(0), child1);
		assertEquals(root.getChildren().get(1), child2);
		assertEquals(root.getChildren().get(2), child3);
	}
	
	@Test
	public void testSortAlphabeticNoChildren() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		root.sortAlphabetic();
	}
	
	@Test
	public void testDoLvls() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode child1 = new ShapeTreeNode("node2", "key2");
		ShapeTreeNode child2 = new ShapeTreeNode("node3", "key3");
		root.setLevel(1);
		root.addChild(child1);
		root.addChild(child2);
		root.doLvls();
		assertTrue(child1.getLevel() == 2);
		assertTrue(child2.getLevel() == 2);
	}
	
	@Test
	public void testDoLvlsNoChildren() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		root.doLvls();
	}
	
	@Test
	public void testDoLvls2levelsChildren() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode child1 = new ShapeTreeNode("node2", "key2");
		ShapeTreeNode child2 = new ShapeTreeNode("node3", "key3");
		root.setLevel(1);
		root.addChild(child1);
		child1.addChild(child2);
		root.doLvls();
		assertTrue(child1.getLevel() == 2);
		assertTrue(child2.getLevel() == 3);
	}

	@Test
	public void testToString() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode child1 = new ShapeTreeNode("node2", "key2");
		ShapeTreeNode child2 = new ShapeTreeNode("node3", "key3");
		root.addChild(child1);
		root.addChild(child2);
		assertTrue(root.toString().equals("node1\r\nnode2\r\nnode3"));
	}
	
	@Test
	public void testToStringNoChildren() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		assertTrue(root.toString().equals("node1"));
	}

	@Test
	public void testAdjustToMiddleOfChildren() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode child1 = new ShapeTreeNode("node2", "key2");
		ShapeTreeNode child2 = new ShapeTreeNode("node3", "key3");
		Shape box0 = new Box();
		box0.setxPos(20);
		box0.setWidth(20);
		root.setShape(box0);
		Shape box1 = new Box();
		box1.setxPos(20);
		box1.setWidth(20);
		child1.setShape(box1);
		Shape box2 = new Box();
		box2.setxPos(100);
		box2.setWidth(20);
		child2.setShape(box2);
		root.addChild(child1);
		root.addChild(child2);
		root.adjustToMiddleOfChildren();
		assertTrue(root.getShape().getxPos() == 60);
	}
	
	@Test
	public void testAdjustToMiddleOfChildrenWithOneChild() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode child1 = new ShapeTreeNode("node2", "key2");
		Shape box0 = new Box();
		box0.setxPos(20);
		box0.setWidth(20);
		root.setShape(box0);
		Shape box1 = new Box();
		box1.setxPos(50);
		box1.setWidth(20);
		child1.setShape(box1);
		root.addChild(child1);
		root.adjustToMiddleOfChildren();
		assertTrue(root.getShape().getxPos() == 50);
	}
	
	@Test
	public void testAdjustToMiddleOfChildrenWithNoChildren() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		Shape box0 = new Box();
		box0.setxPos(20);
		box0.setWidth(20);
		root.setShape(box0);
		root.adjustToMiddleOfChildren();
		assertTrue(root.getShape().getxPos() == 20);
	}

	@Test
	public void testShakeChildrenX() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode child1 = new ShapeTreeNode("node2", "key2");
		ShapeTreeNode child2 = new ShapeTreeNode("node3", "key3");
		ShapeTreeNode child3 = new ShapeTreeNode("node4", "key4");
		Shape box1 = new Box();
		box1.setWidth(20);
		child1.setShape(box1);
		Shape box2 = new Box();
		box2.setWidth(10);
		child2.setShape(box2);
		Shape box3 = new Box();
		box3.setWidth(30);
		child3.setShape(box3);
		root.addChild(child1);
		root.addChild(child2);
		root.addChild(child3);
		root.shakeChildrenX(10);
		assertTrue(Math.abs(child1.getShape().getxPos() - child2.getShape().getxPos()) == 25);
		assertTrue(Math.abs(child2.getShape().getxPos() - child3.getShape().getxPos()) == 30);
		assertTrue(Math.abs(child1.getShape().getxPos() - child3.getShape().getxPos()) == 55);
	}

	@Test
	public void testShift() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode child1 = new ShapeTreeNode("node2", "key2");
		ShapeTreeNode child2 = new ShapeTreeNode("node3", "key3");
		Shape box0 = new Box();
		box0.setxPos(10);
		box0.setWidth(40);
		root.setShape(box0);
		Shape box1 = new Box();
		box1.setxPos(50);
		box1.setWidth(20);
		child1.setShape(box1);
		Shape box2 = new Box();
		box2.setxPos(100);
		box2.setWidth(10);
		child2.setShape(box2);
		root.addChild(child1);
		root.addChild(child2);
		root.shift(30);
		assertTrue(root.getShape().getxPos() == 40);
		assertTrue(child1.getShape().getxPos() == 80);
		assertTrue(child2.getShape().getxPos() == 130);
	}
	
	@Test
	public void testShiftNoChildren() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		Shape box0 = new Box();
		box0.setxPos(10);
		box0.setWidth(40);
		root.setShape(box0);
		root.shift(30);
		assertTrue(root.getShape().getxPos() == 40);
	}
	
	@Test
	public void testShift2Levels() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode child1 = new ShapeTreeNode("node2", "key2");
		ShapeTreeNode child2 = new ShapeTreeNode("node3", "key3");
		Shape box0 = new Box();
		box0.setxPos(10);
		box0.setWidth(40);
		root.setShape(box0);
		Shape box1 = new Box();
		box1.setxPos(50);
		box1.setWidth(20);
		child1.setShape(box1);
		Shape box2 = new Box();
		box2.setxPos(100);
		box2.setWidth(10);
		child2.setShape(box2);
		root.addChild(child1);
		child1.addChild(child2);
		root.shift(30);
		assertTrue(root.getShape().getxPos() == 40);
		assertTrue(child1.getShape().getxPos() == 80);
		assertTrue(child2.getShape().getxPos() == 130);
	}

	@Test
	public void testGetRightEdgeSubTree() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode child1 = new ShapeTreeNode("node2", "key2");
		Shape box1 = new Box();
		box1.setxPos(50);
		box1.setWidth(20);
		child1.setShape(box1);
		ShapeTreeNode child2 = new ShapeTreeNode("node3", "key3");
		Shape box2 = new Box();
		box2.setxPos(100);
		box2.setWidth(10);
		child2.setShape(box2);
		root.addChild(child1);
		root.addChild(child2);
		assertTrue(root.getRightEdgeSubTree() == 105);
	}
	
	//TODO
	@Test
	public void testGetRightEdgeSubTreeNoChildren() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		assertNull(root.getRightEdgeSubTree());
	}

	@Test
	public void testGetLeftEdgeSubTree() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode child1 = new ShapeTreeNode("node2", "key2");
		Shape box1 = new Box();
		box1.setxPos(50);
		box1.setWidth(20);
		child1.setShape(box1);
		ShapeTreeNode child2 = new ShapeTreeNode("node3", "key3");
		Shape box2 = new Box();
		box2.setxPos(100);
		box2.setWidth(10);
		child2.setShape(box2);
		root.addChild(child1);
		root.addChild(child2);
		assertTrue(root.getLeftEdgeSubTree() == 40);
	}
	
	//TODO
	@Test
	public void testGetLeftEdgeSubTreeNoChildren() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		assertNull(root.getLeftEdgeSubTree());
	}

	@Test
	public void testGetLevel() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		root.setLevel(2);
		assertTrue(root.getLevel() == 2);
	}

	@Test
	public void testSetLevel() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		root.setLevel(0);
		assertTrue(root.getLevel() == 0);
		root.setLevel(2);
		assertTrue(root.getLevel() == 2);
	}

	@Test
	public void testGetShape() {
		Shape s = new Box();
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		root.setShape(s);
		assertEquals(root.getShape(), s);
	}

	@Test
	public void testSetShape() {
		Shape s = new Box();
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		root.setShape(s);
		assertEquals(root.getShape(), s);
	}

	@Test
	public void testGetName() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		assertTrue(root.getName().equals("node1"));
	}

	@Test
	public void testSetName() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		root.setName("newNode1");
		assertTrue(root.getName().equals("newNode1"));
	}

	@Test
	public void testGetChildren() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode child1 = new ShapeTreeNode("node2", "key2");
		ShapeTreeNode child2 = new ShapeTreeNode("node3", "key3");
		root.addChild(child1);
		assertTrue(root.getChildren().size() == 1);
		root.addChild(child2);
		assertTrue(root.getChildren().size() == 2);
		assertTrue(root.getChildren().contains(child1));
		assertTrue(root.getChildren().contains(child2));
	}

	@Test
	public void testGetFirstChild() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode child1 = new ShapeTreeNode("node2", "key2");
		ShapeTreeNode child2 = new ShapeTreeNode("node3", "key3");
		root.addChild(child1);
		root.addChild(child2);
		assertEquals(root.getFirstChild(), child1);
	}
	
	@Test
	public void testGetFirstChildNullChilds() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		assertNull(root.getFirstChild());
	}

	@Test
	public void testSetChildren() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode child1 = new ShapeTreeNode("node2", "key2");
		ShapeTreeNode child2 = new ShapeTreeNode("node3", "key3");
		List<ShapeTreeNode> children = new ArrayList<ShapeTreeNode>();
		children.add(child1);
		children.add(child2);
		root.setChildren(children);
		assertEquals(children, root.getChildren());
	}
	
	@Test
	public void testGetLastChildNullChilds() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		assertNull(root.getLastChild());
	}

	@Test
	public void testAddChild() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		ShapeTreeNode child1 = new ShapeTreeNode("node2", "key2");
		root.addChild(child1);
		assertTrue(root.getChildren().contains(child1));
	}

	@Test
	public void testGetKey() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		assertTrue(root.getKey().equals("key1"));
	}

	@Test
	public void testSetKey() {
		ShapeTreeNode root = new ShapeTreeNode("node1", "key1");
		root.setKey("newKey1");
		assertTrue(root.getKey().equals("newKey1"));
	}

}
