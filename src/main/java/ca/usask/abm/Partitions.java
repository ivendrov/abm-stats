package ca.usask.abm;

/**
 * Consists entirely of static methods used to create and combine common types of partitions
 * @author isv452
 *
 */
public class Partitions {
	
	/**
	 * Creates a partition of type B using an existing partition of type A
	 * @param partition the existing partition
	 * @param accessor the accessor function
	 * @return the partition resulting conceptually from applying the accessor to each element of B
	 */
	public static <A, B> Partition<B> lift(final Partition<A> partition, final Function<B,A> accessor){
		return new Partition<B>() {
			public int maxID(){ return partition.maxID();}
			public int toID(B b) { return partition.toID(accessor.apply(b));}
			public String idLabel(int id) { return partition.idLabel(id);}
		};
	}
	
	/**
	 * Creates a partition from an (inclusive) range of integers, with each integer associated with a separate id
	 * @param min the minimum integer of the range
	 * @param max the maximum integer of the range
	 * @return the resulting partition object
	 */
	public static Partition<Integer> intRange(int min, int max) {
		return new Partition<Integer>(){
			public int maxID() { return max; }
			public int toID(Integer x) { 
				if (x > min && x <= max) return x - min;
				else return -1;
			}
			public String idLabel(int id) { return String.format("%d", id + min);}
		};
	}
}
