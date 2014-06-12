package ca.usask.abm;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Contains only static methods that 
 * create / combine a set of common statistics
 * @author isv452
 *
 */
public class Statistics {
   public static <T> Statistic<T> count() { 
	   return new Statistic<T>(){
		   @Override
		   public double collect(Collection<T> data) { return data.size(); }
	   };
   }
   
   public static Statistic<Double> average = new Statistic<Double>() {
		@Override
		public double collect(Collection<Double> data){
			double sum = 0;
			int count = 0;
			for (double d : data) { sum += d; count++; }
			return sum / count;
		}
	};
	
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
