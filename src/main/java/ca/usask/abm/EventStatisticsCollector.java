package ca.usask.abm;

/**
 * TODO add documentation
 * @author isv452
 *
 * @param <Event>
 * @param <SpecificEvent>
 */
public class EventStatisticsCollector<Event, SpecificEvent> 
						extends StatisticsCollector<SpecificEvent> 
						implements Observer<Event> {
	private Class<SpecificEvent> specificClass;
	private Partition<Double> timePartition;
	
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
