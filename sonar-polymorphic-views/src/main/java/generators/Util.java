package generators;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class contains all "useful" methods used throughout the project, e.g. parsing, ...
 * @author wout
 *
 */
public class Util {
	/**
	 * This method splits the input based on an array of delimiters. E.g. r234g234b234 with delimiters [r,g,b] 
	 * has as result: ["234","234","234"].
	 * @param input the string that should be split
	 * @param delimiters the places where the string should be split
	 * @return an array with the parts of the splitted string
	 */
	//TODO test this method
	public static String[] splitOnDelimiter(String input, String[] delimiters) throws IllegalArgumentException{
		try{
		String[] result = new String[delimiters.length];
		for(int i = 0;i<delimiters.length-1;i++) {
			result[i] = input.split(delimiters[i])[1].split(delimiters[i+1])[0];
		}
		result[delimiters.length-1] = input.split(delimiters[delimiters.length-1])[1];
		return result;}
		catch(Exception f){
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * This method scales the given values map.
	 * @param values the array to be scaled
	 * @param a the minimum value of the scaled values
	 * @param b the maximum value of the scaled values
	 * @return the Map with the scaled values and their key
	 */
	public static Map<String,Double> scale(Map<String,Double> values, double a, double b){
		double min = Collections.min(values.values(),null);
		double max = Collections.max(values.values(),null);
		double factor = (b-a)/(max-min);

		for(Entry<String, Double> entry :values.entrySet()){
			double newValue = factor*(entry.getValue()-min)+(a);
			values.put(entry.getKey(), newValue);
		}
		
		return values;
	}

	public static boolean isValidColor(String input){
		try{
		
			String[] res = Util.splitOnDelimiter(input, new String[] {"r","g","b"});
			float r = Float.parseFloat(res[0]);
			float g = Float.parseFloat(res[1]);
			float b = Float.parseFloat(res[2]);
			
			return (isBetween(r,0,255)&&isBetween(g,0,255)&&isBetween(b,0,255));
			
		}
		catch(Exception e){
			return false;
		}
	}
	
	public static boolean isBetween(float number, int min, int max){
		return (number <= max && number >= min);
	}
}
