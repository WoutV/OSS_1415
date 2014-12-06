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
			return "BOX";
		}
	},
	
	TRAPEZOID
	{
		@Override
		public String toString() {
			return "TRAPEZOID";
		}
	},
	
	CIRCLE{
		@Override
		public String toString() {
			return "CIRCLE";
		}
	};
	
	public abstract String toString();
	public static ShapeType fromString(String s) {
		ShapeType result;
		switch (s) {
		case "BOX":
			result = ShapeType.BOX;
			break;
		case "TRAPEZOID":
			result = ShapeType.TRAPEZOID;
			break;
		case "CIRCLE":
			result = ShapeType.CIRCLE;
			break;
		default:
			throw new IllegalArgumentException("Unknown dependency type \""+s+"\"");
		}
		return result;
	}
	
}
