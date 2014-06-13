package ca.usask.abm;

/**
 * An observer that receives events with an associated time
 * @author isv452
 *
 * @param <Event> the event type
 */
public interface Observer<Event> {
	
	/** 
	 * @return the observer's time partition
	 */
	public Partition<Double> timePartition();

	/**
	 * Called whenever the observed object changes
	 * @param time the time of the change
	 * @param e the value associated with the change
	 */
	public void update(double time, Event e);

}
