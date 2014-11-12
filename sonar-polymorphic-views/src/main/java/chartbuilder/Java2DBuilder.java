package chartbuilder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
	public void blankImage(int height, int width, int imageType) {
		BufferedImage img = new BufferedImage(width, height, imageType);
		setCanvas(img);
	}

	@Override
	public void createXAxis(String label, int min, int max) {
		// TODO Auto-generated method stub
	}

	@Override
	public void createYAxis(String label, int min, int max) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method will draw a line between two points on the current canvas.
	 * 
	 * @param x1 x-coordinate of start point
	 * @param y1 y-coordinate of start point
	 * @param x2 x-coordinate of end point
	 * @param y2 y-coordinate of end points
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

}
