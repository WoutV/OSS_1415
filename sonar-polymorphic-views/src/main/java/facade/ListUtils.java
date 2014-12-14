package facade;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that provides a number of convenient methods operating on lists.
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 */
public class ListUtils {

	/**
	 * Inferface that describes a function from type V to type W.
	 * 
	 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
	 *
	 * @param <V>
	 * @param <W>
	 */
	public interface Func<V,W> {
		public abstract W execute(V x);
	}
	
	/**
	 * Maps a function over a list.
	 *  
	 * @param l The list over which to map the function.
	 * @param f The function to map over the list.
	 * @return A new list of the same length as l, containing the mapping of
	 *         the function f over l.
	 */
	public static <V,W> List<W> map(List<V> l, Func<V,W> f) {
		List<W> result = new ArrayList<W>(l.size());
		if (l != null) {
			for (V e : l) {
				result.add(f.execute(e));
			}
		}
		return result;
	}
	
}
