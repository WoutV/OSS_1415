package chartbuilder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
	
	//TODO Interface commentaar en niet deze klasse! 
	protected BufferedImage canvas;
	private int canvasWidth;
	private int canvasHeight;
	private final static double SCALE = 0.9;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	
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
	
	private int getCanvasWidth() {
		return this.canvasWidth;
	}
	
	private int getCanvasHeight() {
		return this.canvasHeight;
	}
	

	/**
	 * @param y The y value in the coordinate system with the y-axis pointing downwards.
	 * @return the y value changed to the x-y-coordinate system with the y-axis pointing upwards.
	 */
	private double fixY(double y) {
		double result = getCanvasHeight() - y;
		return result;
	}
	
	
	/**
	 * Sets the canvas of this builder.
	 * @param canvas A canvas for this builder.
	 */
	private void setCanvas(BufferedImage canvas){
		this.canvas = canvas;
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
	 * Will create a new image and set it as its current canvas.
	 * The width and the height of the canvas are saved.
	 * 
	 * @param height The height of the image in pixels.
	 * @param width	The width of the image in pixels.
	 * @param imageType The type of the image, e.g. BufferedImage.TYPE_INT_RGB.
	 */
	@Override
	public void createCanvas(int height, int width, int imageType) {
		BufferedImage img = new BufferedImage(width, height, imageType);
		Graphics2D graphics = img.createGraphics();
		this.canvasWidth = width;
		this.canvasHeight = height;
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		setCanvas(img);
		
	}

	/**
	 * This method will draw an x-axis, pointed to the right.
	 * The x-axis has a centered label and a minimum and maximum value is showed.
	 * The minimum value will be the start point on the x-axis and the maximum value will be the end point on the x-axis.
	 * The start of the x-axis will always be drawn 10% of the total width of the canvas to the right of the left border of the canvas.
	 * The end of the x-axis will always be drawn 10% of the total width of the canvas to the left of the left border of the canvas.
	 * The x-axis will always be drawn 10% of the total height of the canvas to the top of the bottom border of the canvas.
	 * 
	 * @param label The label shown with the x-axis.
	 * @param xMin The minimum value of the input. 
	 * @param xMax The maximum value of the input. 
	 */
	@Override
	public void createXAxis(String label, int xMin, int xMax) {
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		this.minX = xMin;
		this.maxX = xMax;
		double start = 0.1*getCanvasWidth();
		double stop = 0.9*getCanvasWidth();
		double height = 0.9*getCanvasHeight();
		drawHorizontalAxis(start, stop, height);
		int pos = graphics.getFontMetrics().getMaxAscent();
		graphics.setColor(Color.BLACK);
		drawCenteredString(label, (int) (start+(stop-start)/2), (int) (height+pos));
		graphics.drawString(""+xMin, (int) (start), (int) (height+pos));
		graphics.drawString(""+xMax, (int) (stop), (int) (height+pos));
	}

	/**
	 * This method will draw an y-axis, pointed upwards.
	 * The y-axis has a centered label and a minimum and maximum value is showed.
	 * The minimum value will be the start point on the y-axis and the maximum value will be the end point on the y-axis.
	 * The start of the y-axis will always be drawn 10% of the total height of the canvas to the top of the bottom border of the canvas.
	 * The end of the y-axis will always be drawn 10% of the total height of the canvas to the bottom of the top border of the canvas.
	 * The y-axis will always be drawn 10% of the total width of the canvas to the right of the left border of the canvas.
	 * 
	 * @param label The label shown with the y-axis.
	 * @param yMin The minimum value of the input. 
	 * @param yMax The maximum value of the input. 
	 */
	@Override
	public void createYAxis(String label, int yMin, int yMax) {
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		this.minY = yMin;
		this.maxY = yMax;
		double start = 0.1*getCanvasHeight();
		double stop = 0.9*getCanvasHeight();
		double width = 0.1*getCanvasWidth();
		drawVerticalAxis(start, stop, width);
		int pos = graphics.getFontMetrics().getMaxDescent();
		graphics.setColor(Color.BLACK);
		drawVerticalString(label, (int) (width-pos), (int) (start+(stop-start)/2), -Math.PI/2);
		drawVerticalString(""+yMin, (int) (width-pos), (int) (start+0.99*(stop-start)), -Math.PI/2);
		drawVerticalString(""+yMax, (int) (width-pos), (int) (start+0.01*(stop-start)), -Math.PI/2);
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
		int newY1 = (int) fixY(y1);
		int newY2 = (int) fixY(y2);
		graphics.setColor(Color.BLACK);
		graphics.drawLine(x1, newY1, x2, newY2);
	}

	/**
	 * This method will draw a rectangle with its label on the current canvas, positioned correctly with respect to the axes.
	 * The rectangle has a black border and the label will be centered above the rectangle.
	 * 
	 * Precondition: The current canvas should already have axes.
	 * 
	 * @param xPosition The x-coordinate of the upper left corner.
	 * @param yPosition The y-coordinate of the upper left corner.
	 * @param height The height of the rectangle.
	 * @param width The width of the rectangle.
	 * @param color The color of the rectangle.
	 * @param label The label above the rectangle.
	 */
	@Override
	public void createRectangleFittedToAxes(int xPosition, int yPosition, int height,
			int width, Color color, String label) {
		double startX = 0.1*getCanvasHeight();
		double startY = 0.1*getCanvasWidth();
		
		int newX = (int) (startX + ((xPosition-this.minX)*0.8*getCanvasWidth()/Math.abs(maxX-minX)));
		int newY = (int) fixY(startY + ((yPosition-this.minY)*0.8*getCanvasHeight()/Math.abs(maxY-minY)));
		
		
		drawRectangle(newX-width/2, newY-height/2, width, height, color);
		drawCenteredString(label, newX, newY-height/2 -1);
	}
	
	/**
	 * This method will draw a rectangle with its label on the current canvas.
	 * The rectangle has a black border and the label will be centered above the rectangle.
	 * 
	 * @param xPosition The x-coordinate of the upper left corner.
	 * @param yPosition The y-coordinate of the upper left corner.
	 * @param height The height of the rectangle.
	 * @param width The width of the rectangle.
	 * @param color The color of the rectangle.
	 * @param label The label above the rectangle.
	 */
	@Override
	public void createRectangle(int xPosition, int yPosition, int height,
			int width, Color color, String label) {
		int newY = (int) fixY(yPosition);
		drawRectangle(xPosition-width/2, newY-height/2, width, height, color);
		drawCenteredString(label, xPosition, newY-height/2-1);
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
	private void drawHorizontalAxis(double xStart, double xStop, double y) {
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
	 * @param start The start point of the y-axis.
	 * @param stop The end point of the y-axis. 
	 * @param x The x-position on the canvas where the y-axis will be shown. 
	 */
	private void drawVerticalAxis(double start, double stop, double x) {
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		double newStart = fixY(start);
		double newStop = fixY(stop);
		Line2D line = new Line2D.Double(x, newStart, x, newStop);
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
		graphics.drawString(label, -y, x);
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
	    graphics.rotate(angle);  
	    graphics.draw(arrowHead);
		graphics.fill(arrowHead);
		graphics.setTransform(original);
	}

}
