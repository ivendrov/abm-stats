package ca.usask.abm;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A functional interface for statistics over collections
 * @author isv452
 *
 * @param <T> the data type over which statistics are collected
 */
public abstract class Statistic<T> implements Function<Collection<T>, Double> {
	/**
	 * @param data the data over which to collect the statistics
	 * @return the statistic
	 */
	public abstract Double apply(Collection<T> data);
	
	/**
	 * Creates a statistic over values of type U by applying an accessor to extract a
	 * values of type T, then applying a statistic over T on the result.
	 * @param <U> the type of the resulting statistic
	 * @param accessor the accessor function
	 * @return the resulting statistic
	 */
	public <U> Statistic<U> on(final Function<U, T> accessor){ 
		final Statistic<T> outer = this;
		return new Statistic<U>(){
			@Override
			public Double apply(Collection<U> data){
				ArrayList<T> ts = new ArrayList<T>(data.size());
				for (U datum : data) ts.add(accessor.apply(datum));
				return outer.apply(ts);
			}
		};
	}

}
