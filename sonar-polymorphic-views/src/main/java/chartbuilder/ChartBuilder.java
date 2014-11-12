package chartbuilder;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface ChartBuilder {
	
	public void blankImage(int height, int width, int imageType);
	
	public void createXAxis(String label, int xStart, int xStop, int xMin, int xMax, int y);
	
	public void createYAxis(String label, int yStart, int yStop, int yMin, int yMax, int x);
	
	public void createLine(int x1, int y1, int x2, int y2);
	
	public void createRectangle(int xPosition, int yPosition, int height, int width, Color color, String label);
	
	public BufferedImage getImage();

}
