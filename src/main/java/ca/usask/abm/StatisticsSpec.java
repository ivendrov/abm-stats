package ca.usask.abm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Container class whose fields specify a general statistics collection process,
 * and associated helper methods
 * @author isv452
 * 
 * @param <T> the basic data type over which statistics are collected
 *
 */
public class StatisticsSpec<T> {
	
	private final Function<T, Boolean> condition;
	private final Collection<Named<Partition<Timed<T>>>> partitions;
	private final Collection<Named<Statistic<T>>> statistics;
	
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
	
	/**
	 * @return the list of partition dimensions in this spec
	 *         = map (_.maxID() + 1) partitions
	 */
	public List<Integer> partitionDimensions(){
		ArrayList<Integer> dimensions = new ArrayList<Integer>();
		for (Named<Partition<Timed<T>>> p : partitions){
			dimensions.add(p.getValue().maxID() + 1); // +1 because maxID is inclusive
		}
		return dimensions;
	}
	
	/**
	 * 
	 * @param ts a collection of Ts
	 * @return the results of evaluating all the statistics on the collection
	 *         = map (_.collect(ts)) statistics
	 */
	public List<Double> evalStatisticsOn(Collection<T> ts){
		ArrayList<Double> statValues = new ArrayList<Double>();
		for (Named<Statistic<T>> stat : statistics){
			statValues.add(stat.getValue().collect(ts));
		}
		return statValues;
	}
	
	/**
	 * @param t the timed value
	 * @return the partitions IDs, or null if the element either
	 * 			1) does not satisfy the "condition"
	 * 			2) is not part of any one of the partitions.
	 * 
	 */
	public List<Integer> evalPartitionsOn(Timed<T> t){
		// check condition
		if (!condition.apply(t.getValue())) return null;
		// finds ids for each partition
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (Named<Partition<Timed<T>>> p : partitions){
			int id = p.getValue().toID(t);
			if (id == Partition.INVALID_ID) return null;
			ids.add(id);
		}
		return ids;
	}
	
	/**
	 * @return a list of all the partition names
	 */
	public List<String> partitionNames(){
		return Named.getNames(partitions);
	}
	
	/**
	 * @return a list of all the statistic names
	 */
	public List<String> statisticNames(){
		return Named.getNames(statistics);
	}
	
	/**
	 * @param ids identifiers for each partition // TODO sanitize
	 * @return the labels for each identifier, in order
	 * 		   = zipWith (_.toID(_)) partitions ids
	 */
	public List<String> partitionLabels(Collection<Integer> ids){
		ArrayList<String> labels = new ArrayList<String>();
		Iterator<Integer> idIterator = ids.iterator();
		for (Named<Partition<Timed<T>>> p : partitions){
			String label = p.getValue().idLabel(idIterator.next());
			labels.add(label);
		}
		return labels;
	}
	
}
