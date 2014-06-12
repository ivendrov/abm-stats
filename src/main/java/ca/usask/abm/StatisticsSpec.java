package ca.usask.abm;

import java.util.Collection;

/**
 * Container class whose fields specify a general statistics collection process
 * @author isv452
 *
 */
public class StatisticsSpec<T> {
	
	final Function<T, Boolean> condition;
	final Collection<Named<Partition<Timed<T>>>> partitions;
	final Collection<Named<Statistic<T>>> statistics;
	
	/**
	 * @param condition an object is included in the statistics if the condition is true; 
	 * 					otherwise it is ignored
	 * @param partitions the dimensions along which to split the objects into categories
	 * @param statistics the statistics to apply to each category
	 */
	public StatisticsSpec(Function<T, Boolean> condition,
			Collection<Named<Partition<Timed<T>>>> partitions,
			Collection<Named<Statistic<T>>> statistics) {
		super();
		this.condition = condition;
		this.partitions = partitions;
		this.statistics = statistics;
	}
	
}
