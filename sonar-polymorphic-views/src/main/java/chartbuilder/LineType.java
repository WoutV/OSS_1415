package chartbuilder;

import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 * This enumeration lists all types of lines that can be drawn.
 */
public enum LineType {
	
	SOLID
	{
		@Override
		public String toString() {
			return "solid";
		}
	},
	
	DASH
	{
		@Override
		public String toString() {
			return "dash";
		}
	},
	
	DOTDASH
	{
		@Override
		public String toString() {
			return "dotdash";
		}
	};
	
	public abstract String toString();
	

	
}