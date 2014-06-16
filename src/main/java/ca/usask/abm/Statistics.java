package ca.usask.abm;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;

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
		   public double collect(Collection<T> data) { return data.size(); }
	   };
   }
   
   /**
    * a statistic that computes the mean of all elements in the collection,
    * or returns 0 if the collection is empty.
    */
   public static Statistic<Double> mean = new Statistic<Double>() {
		@Override
		public double collect(Collection<Double> data){
			double sum = 0;
			int count = 0;
			for (double d : data) { sum += d; count++; }
			if (count == 0) return 0;
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
			public double collect(Collection<Double> data) {
				// convert to array
				double[] d = ArrayUtils.toPrimitive((Double[]) data.toArray());
				
				return p.evaluate(d);
			}
			
		};
	}
	
	/**
	 * Creates a statistic over values of type T by applying an accessor to extract a
	 * values of type U, then applying a statistic over U on the result.
	 * @param <U> the type of the existing statistic
	 * @param <T> the type of the resulting statistic
	 * @param stat the existing statistic
	 * @param accessor the accessor function
	 * @return the resulting statistic
	 */
	public static <T, U> Statistic<T> lift(final Statistic<U> stat, final Function<T,U> accessor){ 
		return new Statistic<T>(){
			@Override
			public double collect(Collection<T> data){
				ArrayList<U> us = new ArrayList<U>(data.size());
				for (T datum : data) us.add(accessor.apply(datum));
				return stat.collect(us);
			}
		};
	}
}
