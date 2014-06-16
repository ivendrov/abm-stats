package ca.usask.abm;

import java.util.Collection;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

/**
 * Contains only static methods that 
 * create / combine a set of common statistics
 * @author isv452
 *
 */
public class Statistics {
   /**
    * @return a statistic that simply counts the number of elements in the collection
    */
   public static <T> Statistic<T> count() { 
	   return new Statistic<T>(){
		   @Override
		   public Double apply(Collection<T> data) { return (double) data.size(); }
	   };
   }
   
   /**
    * @param predicate a predicate
    * @return a statistic that counts the number of elements in the collection satisfying the given predicate
    */
   public static <T> Statistic<T> count(final Function<T, Boolean> predicate) { 
	   return new Statistic<T>(){
		   @Override
		   public Double apply(Collection<T> data) { 
			   int count = 0;
			   for (T t : data) {
				   if (predicate.apply(t)) count++;
			   }
			   return (double) count;
		   }
	   };
   }
   
   /**
    * a statistic that computes the mean of all elements in the collection,
    * or returns 0 if the collection is empty.
    */
   public static Statistic<Double> mean = new Statistic<Double>() {
		@Override
		public Double apply(Collection<Double> data){
			double sum = 0;
			int count = 0;
			for (double d : data) { sum += d; count++; }
			if (count == 0) return 0.0;
			return sum / count;
		}
	};
	
	
	/**
	 * A statistic that computes the median of the collection in accordance
	 * with the Apache Commons Math Percentile class.
	 */
	public static Statistic<Double> median = percentile(0.5);
	/**
	 * Computes the given percentile of the collection (or NaN if the collection is empty)
	 * @param percentile the percentile to find
	 * @return the value at the given percentile (interpolated in accordance with the Apache Commons Math Percentile class)
	 */
	public static Statistic<Double> percentile(final double percentile){
		return new Statistic<Double>(){
			final Percentile p = new Percentile(percentile);
			
			@Override
			public Double apply(Collection<Double> data) {
				// convert to array
				double[] d = ArrayUtils.toPrimitive((Double[]) data.toArray());
				
				return p.evaluate(d);
			}
			
		};
	}
	
}
