package ca.usask.abm;

/**
 * Collects statistics over a particular type of events in the model. 
 * Every time update() is called with an event whose runtime class 
 * is actually SpecificEvent, the event is added to the statistics 
 * collection to be processed later. 
 * 
 * @author isv452
 *
 * @param <Event> the most general model event type
 * @param <SpecificEvent> the specific event type that this event collector is concerned with
 * 						(usually a subtype of Event)
 */
public class EventStatisticsCollector<Event, SpecificEvent> 
						extends StatisticsCollector<SpecificEvent> 
						implements Observer<Event> {
	private Class<SpecificEvent> specificClass;
	private Partition<Double> timePartition;
	
	/**
	 * Creates a new collector
	 * @param specificClass the class of the specific event this collector is concerned with
	 * @param spec the specification of how the statistics are processed
	 * @param timePartition a partition of time used to optimize memory usage (statistics
	 * 			are greedily processed over all accumulated events every time we enter a new
	 * 			time partition, so no agent category according to spec should fit into more than one
	 * 			time partition)
	 */
	public EventStatisticsCollector(Class<SpecificEvent> specificClass, 
									StatisticsSpec<SpecificEvent> spec, 
									Partition<Double> timePartition) {
		super(spec);
		this.specificClass = specificClass;
		this.timePartition = timePartition;
	}


	@Override
	public Partition<Double> timePartition() {
		return timePartition();
	}

	private int prevID = Partition.INVALID_ID;
	
	@Override
	public void update(double time, Event e) {
		if (! e.getClass().equals(specificClass)) return; // wrong event type
		SpecificEvent se = specificClass.cast(e);
		
		// flush the buffer if we've transitioned into a new time partition
		int newID = timePartition.toID(time);
		if (newID != prevID){
			prevID = newID;
			this.flushBuffer();
		}
		
		// add the event to the buffer
		this.objectBuffer.add(new Timed<SpecificEvent>(time, se));
	}
	
	
}
