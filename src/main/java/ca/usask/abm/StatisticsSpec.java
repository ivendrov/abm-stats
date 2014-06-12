package ca.usask.abm;

import java.util.Collection;

/**
 * Container class whose fields specify a general statistics collection process
 * @author isv452
 *
 */
public class StatisticsSpec<T> {
	
	private Collection<Named<Partition<Timed<T>>>> partitions;
	private Collection<Named<Statistic<T>>> statistics;
	public StatisticsSpec<T>()
}
