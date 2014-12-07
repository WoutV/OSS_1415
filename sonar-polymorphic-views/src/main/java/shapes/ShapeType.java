package shapes;


/**
 * This enumeration lists all types of dependencies between resources.
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 *
 */
public enum ShapeType {
	
	BOX
	{
		@Override
		public String toString() {
			return "box";
		}
	},
	
	TRAPEZOID
	{
		@Override
		public String toString() {
			return "trap";
		}
	},
	
	CIRCLE{
		@Override
		public String toString() {
			return "circle";
		}
	};
	
	public abstract String toString();
	public static ShapeType fromString(String s) {
		ShapeType result;
		switch (s) {
		case "box":
			result = ShapeType.BOX;
			break;
		case "trap":
			result = ShapeType.TRAPEZOID;
			break;
		case "circle":
			result = ShapeType.CIRCLE;
			break;
		default:
			throw new IllegalArgumentException("Unknown shapetype type \""+s+"\"");
		}
		return result;
	}
	
}
