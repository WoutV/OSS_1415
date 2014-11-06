package be.kuleuven.cs.oss.sonarfacade;

/**
 * This enumeration lists all possible resource qualifiers.
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 */
public enum ResourceQualifier {
	
	VIEW
	{
		@Override
		public String toString() {
			return "VW";
		}
	},
	SUBVIEW
	{
		@Override
		public String toString() {
			return "SVW";
		}
	},
	LIB
	{
		@Override
		public String toString() {
			return "LIB";
		}
	},
	PROJECT
	{
		@Override
		public String toString() {
			return "TRK";
		}
	},
	MODULE
	{
		@Override
		public String toString() {
			return "BRC";
		}
	},
	PACKAGE
	{
		@Override
		public String toString() {
			return "PAC";
		}
	},
	DIRECTORY
	{
		@Override
		public String toString() {
			return "DIR";
		}
	},
	FILE
	{
		@Override
		public String toString() {
			return "FIL";
		}
	},
	CLASS
	{
		@Override
		public String toString() {
			return "CLA";
		}
	},
	UNIT_TEST_CLASS
	{
		@Override
		public String toString() {
			return "UTS";
		}
	};
	
	public abstract String toString();
	public static ResourceQualifier fromString(String s) {
		ResourceQualifier result;
		switch (s) {
		case "VW":
			result = VIEW;
			break;
		case "SVB":
			result = SUBVIEW;
			break;
		case "LIB":
			result = LIB;
			break;
		case "TRK":
			result = PROJECT;
			break;
		case "BRC":
			result = MODULE;
			break;
		case "PAC":
			result = PACKAGE;
			break;
		case "DIR":
			result = DIRECTORY;
			break;
		case "FIL":
			result = FILE;
			break;
		case "CLA":
			result = CLASS;
			break;
		case "UTS":
			result = UNIT_TEST_CLASS;
			break;
		default:
			throw new IllegalArgumentException("Unknown resource qualifier \""+s+"\"");
		}
		return result;
	}

}
