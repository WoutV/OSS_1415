package generators;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class contains all "useful" methods used throughout the project, e.g. parsing, ...
 * @author Wout&Thijs
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

	/**
	 * This method scales the given values map.
	 * @param values the array to be scaled
	 * @param a the minimum value of the scaled values
	 * @param b the maximum value of the scaled values
	 * @return the Map with the scaled values and their key
	 */
	public static Map<String,Double> scaleGrey(Map<String,Double> values, double min, double max){
		double factor = 255/(max-min);
		for(Entry<String, Double> entry :values.entrySet()){
			double newValue;
			if (entry.getValue()<=min){
				newValue = 255;
			}
			else if (entry.getValue()>=max){
				newValue =0;
			}
			else{
				newValue = 255-factor*(entry.getValue() -min);
			}
			values.put(entry.getKey(),newValue);
		}
		return values;
	}

	
	/**
	 * Checks if the input is a valid string to be parsed to a color.
	 * @param input (should be of form "r25g26b27")
	 * @return True if valid, False if not.
	 */
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
	
	/**
	 * @param number
	 * @param min
	 * @param max
	 * @return True if number is between min en max
	 */
	public static boolean isBetween(float number, int min, int max){
		return (number <= max && number >= min);
	}
	
	/**
	 * This method parses an input string to an rgb color. If the input is not
	 * of the form rxxxgxxxbxxx. 
	 * 
	 * @param color
	 * @return a color object
	 * @throws IllegalArgumentException
	 */
	public static Color parseColor(String color) throws IllegalArgumentException {
		Integer[] result = new Integer[3];
		try {
			String[] splitted = Util.splitOnDelimiter(color, new String[]{"r","g","b"});
			result[0] = Integer.parseInt(splitted[0]);
			result[1] = Integer.parseInt(splitted[1]);
			result[2] = Integer.parseInt(splitted[2]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException();
		}
		return new Color(result[0], result[1], result[2]);
	}
}
