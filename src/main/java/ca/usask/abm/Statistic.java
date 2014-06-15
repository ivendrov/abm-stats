package ca.usask.abm;

import java.util.Collection;

/**
 * A functional interface for statistics over collections
 * @author isv452
 *
 * @param <T> the data type over which statistics are collected
 */
public interface Statistic<T> {
	/**
	 * @param data the data over which to collect the statistics
	 * @return the statistic
	 */
	public double collect(Collection<T> data);

}
