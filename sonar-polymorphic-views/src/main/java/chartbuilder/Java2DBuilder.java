package chartbuilder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 * This class is only responsible for creating a chart image itself for the polymorphic views plugin.
 * It will create a blank canvas and add all necessary components one by one. The whole layout is
 * beforehand calculated and will only be painted here.
 * 
 * @author Maxim D'Hollander, Simon Dierckx, Yannick Laevaert
 * 
 */
public class Java2DBuilder implements ChartBuilder {
	
	protected BufferedImage canvas;
	
	/**
	 * Creates an instance of this class.
	 */
	public Java2DBuilder(){
		
	}
	
	/**
	 * Returns the canvas of this builder.
	 * 
	 * @return the current canvas
	 */
	private BufferedImage getCanvas(){
		return this.canvas;
	}
	
	/**
	 * Sets the canvas of this builder.
	 * @param canvas A canvas for this builder.
	 */
	private void setCanvas(BufferedImage canvas){
		this.canvas = canvas;
	}
	
	/**
	 * Will create a new image and set it as its current canvas.
	 * 
	 * @param height The height of the image in pixels.
	 * @param width	The width of the image in pixels.
	 * @param imageType The type of the image, e.g. BufferedImage.TYPE_INT_RGB.
	 */
	@Override
	public void createCanvas(int height, int width, int imageType) {
		BufferedImage img = new BufferedImage(width, height, imageType);
		Graphics2D graphics = img.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		setCanvas(img);
	}

	/**
	 * This method will draw an x-axis, pointed to the right.
	 * The x-axis has a centered label and a minimum and maximum value is showed.
	 * 
	 * @param label The label shown with the x-axis.
	 * @param xStart The start point of the x-axis.
	 * @param xStop The end point of the x-axis .
	 * @param xMin The minimum value of the input.
	 * @param xMax The maximum value of the input.
	 * @param y The y-position on the canvas where the x-axis will be shown.
	 */
	@Override
	public void createXAxis(String label, int xStart, int xStop,
			int xMin, int xMax, int y) {
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		drawHorizontalAxis(xStart, xStop, y);
		int pos = graphics.getFontMetrics().getMaxAscent();
		drawCenteredString(label, xStart+(xStop-xStart)/2, y+pos);
		drawCenteredString(""+xMin, xMin, y+pos);
		drawCenteredString(""+xMax, xMax, y+pos);
	}

	/**
	 * This method will draw an y-axis, pointed upwards.
	 * The y-axis has a centered label and a minimum and maximum value is showed.
	 * 
	 * @param label The label shown with the y-axis.
	 * @param yStart The start point of the y-axis.
	 * @param yStop The end point of the y-axis. 
	 * @param yMin The minimum value of the input.
	 * @param yMax The maximum value of the input.
	 * @param x The x-position on the canvas where the y-axis will be shown. 
	 */
	@Override
	public void createYAxis(String label, int yStart, int yStop,
			int yMin, int yMax, int x) {
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		drawVerticalAxis(yStart, yStop, x);
		int pos = graphics.getFontMetrics().getMaxDescent();
		drawVerticalString(label, x-pos, yStart+(yStop-yStart)/2, -Math.PI/2);
		drawVerticalString(""+yMin, x-pos, yStop-yMin, -Math.PI/2);
		drawVerticalString(""+yMax, x-pos, yStop-yMax, -Math.PI/2);
	}

	/**
	 * This method will draw a line between two points on the current canvas.
	 * 
	 * @param x1 x-coordinate of start point.
	 * @param y1 y-coordinate of start point.
	 * @param x2 x-coordinate of end point.
	 * @param y2 y-coordinate of end point.
	 */
	@Override
	public void createLine(int x1, int y1, int x2, int y2) {
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		graphics.setColor(Color.BLACK);
		graphics.drawLine(x1, y1, x2, y2);
	}

	/**
	 * This method will draw a rectangle with its label on the current canvas.
	 * The rectangle has a black border and the label will be centered above the rectangle.
	 * 
	 * @param xPosition The x-coordinate of the upper left corner.
	 * @param yPosition The y-coordinate of the upper left corner.
	 * @param height The height of the rectangle.
	 * @param color The color of the rectangle.
	 * @param label The label above the rectangle.
	 */
	@Override
	public void createRectangle(int xPosition, int yPosition, int height,
			int width, Color color, String label) {
		drawRectangle(xPosition, yPosition, width, height, color);
		drawCenteredString(label, xPosition + (width/2) , yPosition-1);
	}

	/**
	 * Returns the canvas.
	 * 
	 * @return the current canvas
	 */
	@Override
	public BufferedImage getImage() {
		return getCanvas();
	}
	
	/**
	 * This method will draw a string centered around the specified position.
	 * 
	 * @param label The string to be displayed.
	 * @param x The x-coordinate of the center.
	 * @param y The y-coordinate of the center.
	 */
	private void drawCenteredString(String label, int x, int y) {
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		int width = graphics.getFontMetrics().stringWidth(label);
		graphics.setColor(Color.BLACK);
		graphics.drawString(label, x-(width/2), y);
	}

	/**
	 * This method will draw a rectangle with a black border.
	 * 
	 * @param xPosition The x-coordinate of the upper left corner of the rectangle.
	 * @param yPosition The y-coordinate of the upper left corner of the rectangle.
	 * @param width The width of the rectangle.
	 * @param height The height of the rectangle.
	 * @param color The color of the rectangle.
	 */
	private void drawRectangle(int xPosition, int yPosition, int width,
			int height, Color color) {
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		Rectangle rect = new Rectangle(xPosition, yPosition, width, height);
		graphics.setColor(color);
		graphics.fill(rect);
		graphics.setColor(Color.BLACK);
		graphics.draw(rect);
	}
	
	/**
	 * This method will draw an horizontal axis, pointed to the right.
	 * 
	 * @param xStart The start point of the x-axis.
	 * @param xStop The end point of the x-axis.
	 * @param y The y-position on the canvas where the x-axis will be shown.
	 */
	public void drawHorizontalAxis(int xStart, int xStop, int y) {
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		Line2D line = new Line2D.Double(xStart, y, xStop, y);
		graphics.setColor(Color.BLACK);
		graphics.draw(line);
		drawArrowHead(line);
	}
	
	/**
	 * This method will draw a vertical axis, pointed upwards.
	 * 
	 * @param yStart The start point of the y-axis.
	 * @param yStop The end point of the y-axis. 
	 * @param x The x-position on the canvas where the y-axis will be shown. 
	 */
	public void drawVerticalAxis(int yStart, int yStop, int x) {
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		Line2D line = new Line2D.Double(x, yStop, x, yStart);
		graphics.setColor(Color.BLACK);
		graphics.draw(line);
		drawArrowHead(line);
	}
	
	/**
	 * This method will draw a string that is rotated.
	 * 
	 * @param label The string to be displayed.
	 * @param x The x-coordinate of the center.
	 * @param y The y-coordinate of the center.
	 * @param angle The angle, in radians, the string will be rotated over to the right.
	 */
	private void drawVerticalString(String label, int x, int y, double angle) {
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		AffineTransform original = graphics.getTransform();
		graphics.rotate(angle);
		int width = graphics.getFontMetrics().stringWidth(label);
		graphics.setColor(Color.BLACK);
		graphics.drawString(label, -y-(width/2), x);
		graphics.setTransform(original);
	}
	
	/**
	 * This method will draw an arrowhead on the end of a line, pointing away of the line. 
	 * 
	 * @param line The line on which the arrowhead need to be drawn.
	 */
	private void drawArrowHead(Line2D line) {  
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		graphics.setColor(Color.BLACK);
		AffineTransform original = graphics.getTransform();
		Polygon arrowHead = new Polygon();  
		arrowHead.addPoint(5,0);
		arrowHead.addPoint( -5, 5);
		arrowHead.addPoint( -5,-5);
	    double angle = Math.atan2(line.getY2()-line.getY1(), line.getX2()-line.getX1());
	    graphics.translate(line.getX2(), line.getY2());
	    System.out.println("" + line.getX2());
	    System.out.println("" + line.getY2());
	    System.out.println("" + angle);
	    graphics.rotate(angle);  
	    graphics.draw(arrowHead);
		graphics.fill(arrowHead);
		graphics.setTransform(original);
	}

}
