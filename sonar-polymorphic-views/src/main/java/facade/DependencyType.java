package facade;

/**
 * This enumeration lists all types of dependencies between resources.
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 *
 */
public enum DependencyType {
	
	USES
	{
		@Override
		public String toString() {
			return "USES";
		}
	},
	
	IMPLEMENTS
	{
		@Override
		public String toString() {
			return "IMPLEMENTS";
		}
	},
	
	EXTENDS{
		@Override
		public String toString() {
			return "EXTENDS";
		}
	};
	
	public abstract String toString();
	public static DependencyType fromString(String s) {
		DependencyType result;
		switch (s) {
		case "USES":
			result = DependencyType.USES;
			break;
		case "IMPLEMENTS":
			result = DependencyType.IMPLEMENTS;
			break;
		case "EXTENDS":
			result = DependencyType.EXTENDS;
			break;
		default:
			throw new IllegalArgumentException("Unknown dependency type \""+s+"\"");
		}
		return result;
	}
	
}
