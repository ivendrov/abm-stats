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
	 * the value returned by toID if there is no ID specified
	 */
	
	public static final int INVALID_ID = -1;
	/**
	 * @return the maximal possible identifier
	 */
	int maxID();
	
	/**
	 * @param elem the given element
	 * @return the identifier corresponding to the given element, or INVALID_ID if there is no such identifier
	 */
	int toID(T elem);
	
	/**
	 * @param id an identifier in the range [0, maxID]
	 * @return a text representation of the subset associated with the given identifier
	 * (e.g. for a partition of the real line, this could be "[0, 1)")
	 */
	String idLabel(int id);
}
