package ca.usask.abm;

import java.util.ArrayList;

/**
 * A statistics collector which collects statistics over a particular
 * set of agents in a model. Every time its update() gets called, it will
 * process statistics for all agents indicated by agentAccessor()
 * 
 *
 * @param <Model> the model type
 * @param <Agent> the agent type
 */
public class AgentStatisticsCollector<Model, Agent> extends StatisticsCollector<Agent> 
													implements Observer<Model> 
{

	private Function<Model, Iterable<Agent>> agentAccessor;
	private Partition<Double> timePartition;
	
	/**
	 * Creates a new agent statistics collector. 
	 *  NOTE: the timePartition provided to the constructor must be at least 
     *    as discriminant as the partitions in spec (no agent category should belong to more
     *    than one identifier in timePartition)
	 * @param agentAccessor a function that extracts the needed set of agents from the model
	 * @param spec the statistics collection specification
	 * @param timePartition a partition of time which is used to implement Observer's timePartition
	 * 			
	 */
	public AgentStatisticsCollector(Function<Model, Iterable<Agent>> agentAccessor, 
									StatisticsSpec<Agent> spec, 
									Partition<Double> timePartition) {
		super(spec);
		this.timePartition = timePartition;
		this.agentAccessor = agentAccessor;
	}

	@Override
	public Partition<Double> timePartition() {
		return timePartition;
	}

	@Override
	public void update(double time, Model e) {
		Iterable<Agent> agents = agentAccessor.apply(e);
		// add times to each agent //TODO optimize away this memory-intensive copy
		ArrayList<Timed<Agent>> timedAgents = new ArrayList<Timed<Agent>>();
		for (Agent agent : agents){
			timedAgents.add(new Timed<Agent>(time, agent));
		}
		this.processStats(timedAgents);
	}

}
