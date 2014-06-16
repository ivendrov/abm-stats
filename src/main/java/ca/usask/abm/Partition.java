package ca.usask.abm;

/**
 * Describes a number of disjoint subsets of a type T, each
 * associated with an integer identifier in [0 .. maxID]
 * 
 * @author isv452
 *
 */
public abstract class Partition<T> {
	/**
	 * the value returned by toID if there is no ID specified
	 */
	
	public static final int INVALID_ID = -1;
	/**
	 * @return the maximal possible identifier
	 */
	public abstract int maxID();
	
	/**
	 * @param elem the given element
	 * @return the identifier corresponding to the given element, or INVALID_ID if there is no such identifier
	 */
	public abstract int toID(T elem);
	
	/**
	 * @param id an identifier in the range [0, maxID]
	 * @return a text representation of the subset associated with the given identifier
	 * (e.g. for a partition of the real line, this could be "[0, 1)")
	 */
	public abstract String idLabel(int id);
	

	/**
	 * Creates a partition of type U using this existing partition of type T
	 * @param accessor the accessor function
	 * @return the partition resulting conceptually from applying the accessor to each U
	 * @param <U> result partition type
	 */
	public <U> Partition<U> lift(final Function<U,T> accessor){
		final Partition<T> outer = this;
		return new Partition<U>() {
			public int maxID(){ return outer.maxID();}
			public int toID(U b) { return outer.toID(accessor.apply(b));}
			public String idLabel(int id) { return outer.idLabel(id);}
		};
	}
}
