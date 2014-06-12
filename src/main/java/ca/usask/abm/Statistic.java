package ca.usask.abm;

import java.util.Collection;

/**
 * A functional interface for statistics over collections
 * @author isv452
 *
 * @param <T>
 */
public interface Statistic<T> {
	/**
	 * @param data the data over which to collect the statistics
	 * @return the statistic
	 */
	public double collect(Collection<T> data);
	
	
	
	// static helper methods
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
		return null; //TODO fix
	}
	

}
