package structureTest;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import shapes.Box;
import shapes.Shape;
import structure.ShapeTreeNode;

public class ShapeTreeNodeTest {

	private ShapeTreeNode root;
	
	@Before
	public void setUp() throws Exception {
		root = new ShapeTreeNode("rootKey");
	}

	@Test
	public void testShapeTreeNode() {
		ShapeTreeNode newNode = new ShapeTreeNode("key");
		assertTrue(newNode.getKey().equals("key"));
	}

	@Test
	public void testSortAlphabetic() {
		ShapeTreeNode child1 = new ShapeTreeNode("key1");
		Shape shape1 = new Box(20,20,"key1","key1",new Color(25,25,25));
		child1.setShape(shape1);
		ShapeTreeNode child2 = new ShapeTreeNode("key2");
		Shape shape2 = new Box(20,20,"key2","key2",new Color(25,25,25));
		child2.setShape(shape2);
		ShapeTreeNode child3 = new ShapeTreeNode("key3");
		Shape shape3 = new Box(20,20,"key3","key3",new Color(25,25,25));
		child3.setShape(shape3);
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
		ShapeTreeNode root = new ShapeTreeNode("key1");
		root.sortAlphabetic();
	}

	@Test
	public void testAdjustToMiddleOfChildren() {
		ShapeTreeNode child1 = new ShapeTreeNode("key1");
		ShapeTreeNode child2 = new ShapeTreeNode("key2");
		Shape box0 = new Box(20,20,"box0","box0",new Color(25,25,25));;
		box0.setxPos(20);
		root.setShape(box0);
		Shape box1 = new Box(20,20,"box1","box1",new Color(25,25,25));
		box1.setxPos(20);
		child1.setShape(box1);
		Shape box2 = new Box(20,20,"box2","box2",new Color(25,25,25));
		box2.setxPos(100);
		child2.setShape(box2);
		root.addChild(child1);
		root.addChild(child2);
		root.adjustToMiddleOfChildren();
		assertTrue(root.getShape().getxPos() == 60);
	}
	
	@Test
	public void testAdjustToMiddleOfChildrenWithOneChild() {
		ShapeTreeNode child1 = new ShapeTreeNode("key1");
		Shape box0 = new Box(20,20,"box0","box0",new Color(25,25,25));
		box0.setxPos(20);
		root.setShape(box0);
		Shape box1 = new Box(20,20,"box1","box1",new Color(25,25,25));
		box1.setxPos(50);
		child1.setShape(box1);
		root.addChild(child1);
		root.adjustToMiddleOfChildren();
		assertTrue(root.getShape().getxPos() == 50);
	}
	
	@Test
	public void testAdjustToMiddleOfChildrenWithNoChildren() {
		Shape box0 = new Box(20,20,"box0","box0",new Color(25,25,25));
		box0.setxPos(20);
		root.setShape(box0);
		root.adjustToMiddleOfChildren();
		assertTrue(root.getShape().getxPos() == 20);
	}

	@Test
	public void testShakeChildrenX() {
		ShapeTreeNode child1 = new ShapeTreeNode("key1");
		ShapeTreeNode child2 = new ShapeTreeNode("key2");
		ShapeTreeNode child3 = new ShapeTreeNode("key3");
		Shape box1 = new Box(20,20,"box1","box1",new Color(25,25,25));
		child1.setShape(box1);
		Shape box2 = new Box(10,20,"box2","box2",new Color(25,25,25));
		child2.setShape(box2);
		Shape box3 = new Box(30,20,"box3","box3",new Color(25,25,25));
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
		ShapeTreeNode child1 = new ShapeTreeNode("key1");
		ShapeTreeNode child2 = new ShapeTreeNode("key2");
		Shape box0 = new Box(40,20,"box0","box0",new Color(25,25,25));
		box0.setxPos(10);
		root.setShape(box0);
		Shape box1 = new Box(20,20,"box1","box1",new Color(25,25,25));
		box1.setxPos(50);
		child1.setShape(box1);
		Shape box2 = new Box(10,20,"box2","box2",new Color(25,25,25));
		box2.setxPos(100);
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
		Shape box0 = new Box(40,20,"box0","box0",new Color(25,25,25));
		box0.setxPos(10);
		root.setShape(box0);
		root.shift(30);
		assertTrue(root.getShape().getxPos() == 40);
	}
	
	@Test
	public void testShift2Levels() {
		ShapeTreeNode child1 = new ShapeTreeNode("key1");
		ShapeTreeNode child2 = new ShapeTreeNode("key2");
		Shape box0 = new Box(40,20,"box0","box0",new Color(25,25,25));
		box0.setxPos(10);
		root.setShape(box0);
		Shape box1 = new Box(20,20,"box1","box1",new Color(25,25,25));
		box1.setxPos(50);
		child1.setShape(box1);
		Shape box2 = new Box(10,20,"box2","box2",new Color(25,25,25));
		box2.setxPos(100);
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
		ShapeTreeNode child1 = new ShapeTreeNode("key1");
		ShapeTreeNode child2 = new ShapeTreeNode("key2");
		Shape box1 = new Box(20,20,"box0","box0",new Color(25,25,25));
		box1.setxPos(50);
		child1.setShape(box1);
		Shape box2 = new Box(10,20,"box0","box0",new Color(25,25,25));
		box2.setxPos(100);
		child2.setShape(box2);
		root.addChild(child1);
		root.addChild(child2);
		assertTrue(root.getRightEdge() == 105);
	}
	
	@Test
	public void testGetRightEdgeSubTreeNoChildren() {
		Shape box1 = new Box(20,20,"box1","box1",new Color(25,25,25));
		box1.setxPos(50);
		root.setShape(box1);
		assertTrue(root.getRightEdge() == 60);
	}

	@Test
	public void testGetLeftEdgeSubTree() {
		ShapeTreeNode child1 = new ShapeTreeNode("key1");
		ShapeTreeNode child2 = new ShapeTreeNode("key2");
		Shape box1 = new Box(20,20,"box1","box1",new Color(25,25,25));
		box1.setxPos(50);
		child1.setShape(box1);
		Shape box2 = new Box(10,20,"box2","box2",new Color(25,25,25));
		box2.setxPos(100);
		child2.setShape(box2);
		root.addChild(child1);
		root.addChild(child2);
		assertTrue(root.getLeftEdge() == 40);
	}
	
	@Test
	public void testGetLeftEdgeSubTreeNoChildren() {
		Shape box1 = new Box(20,20,"box1","box1",new Color(25,25,25));
		box1.setxPos(50);
		root.setShape(box1);
		assertTrue(root.getLeftEdge() == 40);
	}

	@Test
	public void testGetLevel() {
		root.setLevel(2);
		assertTrue(root.getLevel() == 2);
	}

	@Test
	public void testSetLevel() {
		root.setLevel(0);
		assertTrue(root.getLevel() == 0);
		root.setLevel(2);
		assertTrue(root.getLevel() == 2);
	}

	@Test
	public void testGetShape() {
		Shape s = new Box(40,20,"box0","box0",new Color(25,25,25));
		root.setShape(s);
		assertEquals(root.getShape(), s);
	}

	@Test
	public void testSetShape() {
		Shape s = new Box(40,20,"box0","box0",new Color(25,25,25));
		root.setShape(s);
		assertEquals(root.getShape(), s);
	}

	@Test
	public void testGetName() {
		Shape s = new Box(40,20,"root","root",new Color(25,25,25));
		root.setShape(s);
		assertTrue(root.getName().equals("root"));
	}

	@Test
	public void testGetChildren() {
		ShapeTreeNode child1 = new ShapeTreeNode("key1");
		ShapeTreeNode child2 = new ShapeTreeNode("key2");
		root.addChild(child1);
		assertTrue(root.getChildren().size() == 1);
		root.addChild(child2);
		assertTrue(root.getChildren().size() == 2);
		assertTrue(root.getChildren().contains(child1));
		assertTrue(root.getChildren().contains(child2));
	}

	@Test
	public void testGetFirstChild() {
		ShapeTreeNode child1 = new ShapeTreeNode("key1");
		ShapeTreeNode child2 = new ShapeTreeNode("key2");
		root.addChild(child1);
		root.addChild(child2);
		assertEquals(root.getFirstChild(), child1);
	}
	
	@Test
	public void testGetFirstChildNullChilds() {
		assertNull(root.getFirstChild());
	}

	@Test
	public void testSetChildren() {
		ShapeTreeNode child1 = new ShapeTreeNode("key1");
		ShapeTreeNode child2 = new ShapeTreeNode("key2");
		List<ShapeTreeNode> children = new ArrayList<ShapeTreeNode>();
		children.add(child1);
		children.add(child2);
		root.setChildren(children);
		assertEquals(children, root.getChildren());
	}
	
	@Test
	public void testGetLastChildNullChilds() {
		assertNull(root.getLastChild());
	}

	@Test
	public void testAddChild() {
		ShapeTreeNode child1 = new ShapeTreeNode("key1");
		root.setLevel(0);
		root.addChild(child1);
		assertTrue(root.getChildren().contains(child1));
		assertTrue(root.getChildren().get(0).getLevel() == 1);
	}

	@Test
	public void testGetKey() {
		assertTrue(root.getKey().equals("rootKey"));
	}

}
