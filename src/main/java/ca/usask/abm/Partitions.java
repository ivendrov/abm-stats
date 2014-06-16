package ca.usask.abm;

import java.util.ArrayList;
import java.util.Collection;

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
	 * @param <A> existing partition type
	 * @param <B> result partition type
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
	public static Partition<Integer> intRange(final int min, final int max) {
		return new Partition<Integer>(){
			public int maxID() { return max; }
			public int toID(Integer x) { 
				if (x > min && x <= max) return x - min;
				else return Partition.INVALID_ID;
			}
			public String idLabel(int id) { return String.format("%d", id + min);}
		};
	}

	/**
	 * Partitions an interval on the real line into a set of half-open intervals of given step size
	 * @param min the minimum real of the range
	 * @param step the step size
	 * @param max the range maximum
	 * @return the resulting partition object
	 */
	 public static Partition<Double> range(final double min, final double step, final double max) { 
		 return new Partition<Double>() {
		    private Double eps = 1e-8;
		    @Override
		    public int toID(Double x){
		      if (x > max || x < min) return Partition.INVALID_ID;
		      else { 
		        double basic = (x - min) / step; // the number of steps needed to get there
		        int rounded = (int) Math.floor(basic - eps); // the interval number
		        return Math.max(0, rounded); // make sure the answer is nonnegative
		      }
		    }  
		    public int maxID() { return toID(max);}
		    
		    public String idLabel(int id) {
		        double lowerBound = min + step * id;
		        double upperBound = lowerBound + step;
		        if (id == maxID())
		        	return String.format("[%f, %f]", lowerBound, upperBound);
		        else 
		        	return String.format("[%f, %f)", lowerBound, upperBound);
		    }    
		 };
	 }
	 
	 /**
	  * Splits the real line into a set of half-open intervals between the given points <br>
	  * So the intervals are [min, es(0)), [es(0), es(1)), ... [es(n), max)
	  * @param es the endpoints of the intervals, in increasing order
	  * @return the resulting partition
	  */
	 public static Partition<Double> between(final Collection<Double> es){
		 return new Partition<Double>(){
			private ArrayList<Double> endpoints = new ArrayList<Double>(es);
			
			@Override
			public int maxID() {
				return endpoints.size();
			}

			@Override
			public int toID(Double elem) {
				int index = 0;
				for (double e : endpoints){
					if (elem < e) return index;
				}
				// must be greater than any element in the collection
				return maxID();
			}

			@Override
			public String idLabel(int id) {
				if (id == maxID()) return String.format("[%f, max)", endpoints.get(id-1));
			    else if (id == 0) return String.format("[min, %f)", endpoints.get(0));
			    else return String.format("[%f, %f)", endpoints.get(id-1), endpoints.get(id));
			}
			 
		 };
	 }
}
