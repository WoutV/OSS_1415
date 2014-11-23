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
	public static LineType fromString(String s) {
		LineType result;
		switch (s) {
		case "solid":
			result = LineType.SOLID;
			break;
		case "dash":
			result = LineType.DASH;
			break;
		case "dotdash":
			result = LineType.DOTDASH;
			break;
		default:
			result = LineType.SOLID;
		}
        return result;
	}
	
	public static Stroke createLineStyleStroke(String lineType) {
        float[] style = null;
        if (lineType != null) {
	        switch (lineType) {
			case "solid":
				break;
			case "dash":
				style = new float[] {20.0f, 20.0f};
				break;
			case "dotdash":
				style = new float[] {20.0f, 9.0f, 3.0f, 9.0f};
				break;
			default:
	        }
        }
        Stroke result = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, style, 0.0f);
        return result;
    }
	
}