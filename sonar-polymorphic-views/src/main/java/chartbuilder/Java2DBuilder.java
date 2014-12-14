package chartbuilder;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
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
	private int canvasWidth;
	private int canvasHeight;
	private final static double SCALE = 0.8;
	
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
	 * This method will scale the given x-value considered the scaling and translation of the axes.
	 * 
	 * @param x
	 * @return The scaled and translated x-value.
	 */
	private int scaleX(int x) {
		int result = (int) (x*SCALE+(1-SCALE)/2*getCanvasWidth());
		return result;
	}
	
	/**
	 * This method will scale the given y-value considered the scaling and translation of the axes.
	 * The y value is also fixed to take into account the switch of the y-axis direction.
	 * 
	 * @param y
	 * @return The fixed, scaled and translated y-value.
	 */
	private int scaleY(int y) {
		int result = (int) (fixY(y)*SCALE+(1-SCALE)/2*getCanvasHeight());
		return result;
	}
	
	/**
	 * Sets the canvas of this builder.
	 * @param canvas A canvas for this builder.
	 */
	private void setCanvas(BufferedImage canvas){
		this.canvas = canvas;
	}
	
	@Override
	public BufferedImage getImage() {
		return getCanvas();
	}
	
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

	@Override
	public void createXAxis(String label, int xMin, int xMax, int y, int minLabel, int maxLabel) {
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		double start = xMin + (1-SCALE)/2*getCanvasWidth();
		double stop = xMax - (1-SCALE)/2*getCanvasWidth();
		double height = fixY(y) - (1-SCALE)/2*getCanvasHeight();
		drawHorizontalAxis(start, stop, height);
		int pos = graphics.getFontMetrics().getMaxAscent();
		graphics.setColor(Color.BLACK);
		drawCenteredString(label, (int) (start+(stop-start)/2), (int) (height+pos));
		graphics.drawString(""+minLabel, (int) (start+1), (int) (height+pos));
		graphics.drawString(""+maxLabel, (int) (stop), (int) (height+pos));
	}

	@Override
	public void createYAxis(String label, int yMin, int yMax, int x,  int minLabel, int maxLabel) {
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		double start = yMin + (1-SCALE)/2*getCanvasHeight();
		double stop = yMax - (1-SCALE)/2*getCanvasHeight();
		double width = x + (1-SCALE)/2*getCanvasWidth();
		double newStart = fixY(start);
		double newStop = fixY(stop);
		drawVerticalAxis(newStart, newStop, width);
		int pos = graphics.getFontMetrics().getMaxDescent();
		graphics.setColor(Color.BLACK);
		drawVerticalString(label, (int) (width-pos), (int) (newStart -(newStart-newStop)/2), -Math.PI/2, true);
		drawVerticalString(""+minLabel, (int) (width-pos), (int) (newStart-1), -Math.PI/2, false);
		drawVerticalString(""+maxLabel, (int) (width-pos), (int) (newStop), -Math.PI/2, false);
	}

	@Override
	public void createLine(int x1, int y1, int x2, int y2, LineType lineType) {
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		graphics.setColor(Color.BLACK);
		Stroke style = createLineStyleStroke(lineType);
		graphics.setStroke(style);
		graphics.drawLine(scaleX(x1), scaleY(y1), scaleX(x2), scaleY(y2));
	}
	
	@Override
	public void createRectangle(int xPosition, int yPosition, int height,
			int width, Color color, String label) {
		int newX = scaleX(xPosition);
		int newY = scaleY(yPosition);
		drawRectangle(newX-width/2, newY-height/2, width, height, color);
		drawCenteredString(label, newX, newY-height/2-1);
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
		Line2D line = new Line2D.Double(x, start, x, stop);
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
	 * @param centered True if the string need to be centered to the given y-coordinate, 
	 * 				   False if the string need to start from the given y-coordinate.
	 */
	private void drawVerticalString(String label, int x, int y, double angle, boolean centered) {
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		AffineTransform original = graphics.getTransform();
		graphics.rotate(angle);
		int width = graphics.getFontMetrics().stringWidth(label);
		graphics.setColor(Color.BLACK);
		if (centered) {
			graphics.drawString(label, -y-width/2, x);
		}
		else {
			graphics.drawString(label, -y, x);
		}
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
	
	/**
	 * This method will draw a right-angled trapezoid with its label.
	 * 
	 * @param xPosition The x-coordinate of the center.
	 * @param yPosition The y-coordinate of the center (the middle of the longest vertical side).
	 * @param side1 The length of the left vertical side.
	 * @param side2 The length of bottom side.
	 * @param side3 The length of the right vertical side.
	 * @param color The color of the trapezoid.
	 * @param label The label to be displayed above the trapezoid.
	 */
	@Override
	public void createRightAngledTrapezoid(int xPosition, int yPosition, int side1, int side2, int side3, Color color, String label) {
		xPosition = scaleX(xPosition);
		yPosition = scaleY(yPosition);
		int[] xPoints = {xPosition - side2/2, xPosition - side2/2, xPosition + side2/2, xPosition + side2/2};
		int nPoints = 4;
		int[] yPoints = new int[nPoints];
		int labelY = 0;
		if(side1 > side3){
			int[] ys = {yPosition + side1/2, yPosition - side1/2, yPosition + (side2/2) - (side3/2), yPosition + (side1/2)};
			labelY = yPosition-side1/2 - 1;
			yPoints = ys;
		}
		else{
			int[] ys = {yPosition + side3/2, yPosition + (side2/2) - (side1/2),yPosition - side3/2, yPosition + (side3/2)};
			labelY = yPosition-side3/2 - 1;
			yPoints = ys;
		}
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
		graphics.setColor(color);
		graphics.fillPolygon(xPoints, yPoints, nPoints);
		graphics.setColor(Color.BLACK);
		graphics.drawPolygon(xPoints, yPoints, nPoints);
		drawCenteredString(label, xPosition, labelY);
	}
	
	/**
	 * This method will draw a circle with its label on the current canvas.
	 * 
	 * @param xPosition The x-coordinate of the center.
	 * @param yPosition The y-coordinate of the center.
	 * @param diameter The diameter of the circle.
	 * @param color The color of the circle
	 * @param label The label above the rectangle.
	 */
	@Override
	public void createCircle(int xPosition, int yPosition, int diameter, Color color, String label){
		xPosition = scaleX(xPosition);
		yPosition = scaleY(yPosition);
		BufferedImage img = getCanvas();
		Graphics2D graphics = img.createGraphics();
	    Ellipse2D.Double circle = new Ellipse2D.Double (xPosition-(diameter/2), yPosition-(diameter/2), diameter, diameter);
	    graphics.setColor(color);
	    graphics.fill(circle);
	    graphics.setColor(Color.BLACK);
	    graphics.draw(circle);
	    drawCenteredString(label, xPosition, yPosition - diameter/2 - 1);
	}

	/**
	 * @param lineType
	 * @return corresponding linestylestroke
	 */
	private static Stroke createLineStyleStroke(LineType lineType) {
        float[] style = null;
        if (lineType != null) {
	        switch (lineType) {
			case SOLID:
				break;
			case DASH:
				style = new float[] {20.0f, 20.0f};
				break;
			case DOTDASH:
				style = new float[] {20.0f, 9.0f, 3.0f, 9.0f};
				break;
			default:
	        }
        }
        Stroke result = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, style, 0.0f);
        return result;
    }

}
