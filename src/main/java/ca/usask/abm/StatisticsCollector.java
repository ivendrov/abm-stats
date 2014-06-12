package ca.usask.abm;

import java.util.Map;

public abstract class StatisticsCollector<T> {
	public Map<String, Partition<T>> partitions;
}
