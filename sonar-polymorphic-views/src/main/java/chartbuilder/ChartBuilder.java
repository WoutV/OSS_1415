package chartbuilder;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface ChartBuilder {
	
	public void blankImage(int height, int width, int imageType);
	
	public void createXAxis(String name, int min, int max);
	
	public void createYAxis(String name, int min, int max);
	
	public void createLine(int x1, int y1, int x2, int y2);
	
	public void createRectangle(int xPosition, int yPosition, int height, int width, Color color, String label);
	
	public BufferedImage getImage();

	
}
