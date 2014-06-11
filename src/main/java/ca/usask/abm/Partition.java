package ca.usask.abm;

/**
 * Describes a number of disjoint subsets of a type T, each
 * associated with an integer identifier in [0 .. maxID]
 * 
 * @author isv452
 *
 */
public interface Partition<T> {
	/**
	 * @return the maximal possible identifier
	 */
	int maxID();
	
	/**
	 * @return the identifier corresponding to the given element, or (-1) if there is no such identifier
	 */
	int toID(T elem);
	
	/**
	 * @return a text representation of the subset associated with the given identifier
	 * (e.g. for a partition of the real line, this could be "[0, 1)")
	 */
	String idLabel(int id);
}
