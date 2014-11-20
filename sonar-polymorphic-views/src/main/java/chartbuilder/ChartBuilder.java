package chartbuilder;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface ChartBuilder {
	
	/**
	 * Returns the canvas.
	 * 
	 * @return the current canvas
	 */
	public BufferedImage getImage();
	
	/**
	 * Will create a new image and set it as its current canvas.
	 * The width and the height of the canvas are saved.
	 * 
	 * @param height The height of the image in pixels.
	 * @param width	The width of the image in pixels.
	 * @param imageType The type of the image, e.g. BufferedImage.TYPE_INT_RGB.
	 */
	public void createCanvas(int height, int width, int imageType);
	
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
	 * 
	 * TODO commentaar aanpassen
	 */
	public void createXAxis(String label, int xMin, int xMax, int minLabel, int maxLabel);
	
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
	 * 
	 *TODO commentaar aanpassen
	 */
	public void createYAxis(String label, int yMin, int yMax, int minLabel, int maxLabel);
	
	/**
	 * This method will draw a line between two points on the current canvas.
	 * 
	 * @param x1 x-coordinate of start point.
	 * @param y1 y-coordinate of start point.
	 * @param x2 x-coordinate of end point.
	 * @param y2 y-coordinate of end point.
	 */
	public void createLine(int x1, int y1, int x2, int y2);
	
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

	public void createRectangle(int xPosition, int yPosition, int height, int width, Color color, String label);

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
	public void createRectangleFittedToAxes(int xPosition, int yPosition, int height,
			int width, Color color, String label);

}
