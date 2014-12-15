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
	private ShapeTreeNode child1;
	private ShapeTreeNode child2;
	private ShapeTreeNode child3;
	
	@Before
	public void setUp() throws Exception {
		Shape shape = new Box(20,20,"rootKey","root",new Color(25,25,25));
		root = new ShapeTreeNode(shape);
		Shape shape1 = new Box(20,20,"key1","key1",new Color(25,25,25));
		child1 = new ShapeTreeNode(shape1);
		Shape shape2 = new Box(20,20,"key2","key2",new Color(25,25,25));
		child2 = new ShapeTreeNode(shape2);
		Shape shape3 = new Box(20,20,"key3","key3",new Color(25,25,25));
		child3 = new ShapeTreeNode(shape3);
		root.addChild(child1);
		root.addChild(child2);
		root.addChild(child3);
	}

	@Test
	public void testShapeTreeNode() {
		Shape shape = new Box(20,20,"key","newNode",new Color(25,25,25));
		ShapeTreeNode newNode = new ShapeTreeNode(shape);
		assertTrue(newNode.getKey().equals("key"));
	}

	@Test
	public void testSortAlphabetic() {
		root.sortAlphabetic();
		assertEquals(root.getChildren().get(0), child1);
		assertEquals(root.getChildren().get(1), child2);
		assertEquals(root.getChildren().get(2), child3);
	}
	
	@Test
	public void testSortAlphabeticNoChildren() {
		root.sortAlphabetic();
	}

	@Test
	public void testAdjustToMiddleOfChildren() {
		root.adjustToMiddleOfChildren();
		assertTrue(root.getShape().getxPos() == 0);
	}
	
	@Test
	public void testAdjustToMiddleOfChildrenWithOneChild() {
		Shape shape = new Box(20,20,"rootKey","root",new Color(25,25,25));
		ShapeTreeNode master = new ShapeTreeNode(shape);
		Shape shape1 = new Box(20,20,"key1","key2",new Color(25,25,25));
		ShapeTreeNode kid = new ShapeTreeNode(shape1);
		master.addChild(kid);
		master.adjustToMiddleOfChildren();
		assertTrue(master.getShape().getxPos() == 0);
	}
	
	@Test
	public void testAdjustToMiddleOfChildrenWithNoChildren() {
		Shape shape = new Box(20,20,"rootKey","root",new Color(25,25,25));
		ShapeTreeNode master = new ShapeTreeNode(shape);
		master.adjustToMiddleOfChildren();
		assertTrue(master.getShape().getxPos() == 0);
	}

	@Test
	public void testShakeChildrenX() {
		root.shakeChildrenX(10);
		assertEquals(Math.abs(child1.getShape().getxPos() - child2.getShape().getxPos()),30);
		assertEquals(Math.abs(child2.getShape().getxPos() - child3.getShape().getxPos()),30);
		assertEquals(Math.abs(child1.getShape().getxPos() - child3.getShape().getxPos()),60);
	}

	@Test
	public void testShift() {
		root.shift(30);
		root.shakeChildrenX(0);
		assertTrue(root.getShape().getxPos() == 30);
		assertTrue(child1.getShape().getxPos() == 10);
		assertTrue(child2.getShape().getxPos() == 30);
	}
	
	@Test
	public void testShiftNoChildren() {
		Shape shape = new Box(20,20,"rootKey","root",new Color(25,25,25));
		ShapeTreeNode master = new ShapeTreeNode(shape);
		master.shift(30);
		assertTrue(master.getShape().getxPos() == 30);
	}
	
	@Test
	public void testShift2Levels() {
		Shape shape = new Box(20,20,"rootKey","root",new Color(25,25,25));
		ShapeTreeNode master = new ShapeTreeNode(shape);
		Shape shape1 = new Box(20,20,"key1","key2",new Color(25,25,25));
		ShapeTreeNode kid = new ShapeTreeNode(shape1);
		master.addChild(kid);
		kid.addChild(child1);
		assertEquals(master.getShape().getxPos() , 0);
		assertEquals(kid.getShape().getxPos() , 0);
		assertEquals(child1.getShape().getxPos() , 0);
	}

	@Test
	public void testGetRightEdgeSubTree() {
		assertTrue(root.getRightEdge() == 10);
	}
	
	@Test
	public void testGetRightEdgeSubTreeNoChildren() {
		Shape shape = new Box(20,20,"rootKey","root",new Color(25,25,25));
		ShapeTreeNode master = new ShapeTreeNode(shape);
		assertEquals(master.getRightEdge(),10);
	}

	@Test
	public void testGetLeftEdgeSubTree() {
		assertTrue(root.getLeftEdge() == -10);
	}
	
	@Test
	public void testGetLeftEdgeSubTreeNoChildren() {
		Shape shape = new Box(20,20,"rootKey","root",new Color(25,25,25));
		ShapeTreeNode master = new ShapeTreeNode(shape);
		assertEquals(master.getLeftEdge(),-10);
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
		Shape shape = new Box(20,20,"rootKey","root",new Color(25,25,25));
		ShapeTreeNode master = new ShapeTreeNode(shape);
		assertEquals(master.getShape(), shape);
	}


	@Test
	public void testGetName() {
		Shape shape = new Box(20,20,"rootKey","root",new Color(25,25,25));
		ShapeTreeNode master = new ShapeTreeNode(shape);
		assertTrue(master.getName().equals("root"));
	}

	@Test
	public void testGetChildren() {
		Shape shape = new Box(20,20,"rootKey","root",new Color(25,25,25));
		ShapeTreeNode master = new ShapeTreeNode(shape);
		Shape shape1 = new Box(20,20,"key1","key1",new Color(25,25,25));
		ShapeTreeNode kid1 = new ShapeTreeNode(shape1);
		Shape shape2 = new Box(20,20,"key2","key2",new Color(25,25,25));
		ShapeTreeNode kid2 = new ShapeTreeNode(shape2);
		master.addChild(kid1);
		assertTrue(master.getChildren().size() == 1);
		master.addChild(kid2);
		assertTrue(master.getChildren().size() == 2);
		assertTrue(master.getChildren().contains(kid1));
		assertTrue(master.getChildren().contains(kid2));
	}

	@Test
	public void testSetChildren() {
		Shape shape = new Box(20,20,"rootKey","root",new Color(25,25,25));
		ShapeTreeNode master = new ShapeTreeNode(shape);
		Shape shape1 = new Box(20,20,"key1","key1",new Color(25,25,25));
		ShapeTreeNode kid1 = new ShapeTreeNode(shape1);
		Shape shape2 = new Box(20,20,"key2","key2",new Color(25,25,25));
		ShapeTreeNode kid2 = new ShapeTreeNode(shape2);
		List<ShapeTreeNode> children = new ArrayList<ShapeTreeNode>();
		children.add(kid1);
		children.add(kid2);
		master.setChildren(children);
		assertEquals(children, master.getChildren());
	}

	@Test
	public void testAddChild() {
		Shape shape = new Box(20,20,"rootKey","root",new Color(25,25,25));
		ShapeTreeNode master = new ShapeTreeNode(shape);
		Shape shape1 = new Box(20,20,"key1","key1",new Color(25,25,25));
		ShapeTreeNode kid1 = new ShapeTreeNode(shape1);
		master.setLevel(0);
		master.addChild(kid1);
		assertTrue(master.getChildren().contains(kid1));
		assertTrue(master.getChildren().get(0).getLevel() == 1);
	}

	@Test
	public void testGetKey() {
		assertTrue(root.getKey().equals("rootKey"));
	}

}
