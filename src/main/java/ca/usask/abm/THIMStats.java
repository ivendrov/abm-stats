package ca.usask.abm;


import java.util.Arrays;


public class THIMStats<Model, DeathEvent, Sim> extends ABMStats<Model, DeathEvent> {
	
	/**
	 * @param endTime the time until which to collect statistics
	 */
	public THIMStats(double endTime,
					Function<Model, Iterable<Sim>> sims,
					Function<Sim, Double> age,
					Function<Sim, Double> education,
					Function<Sim, Double> health,	
					Function<Sim, Double> income,									
					Function<Sim, Boolean> doneEducation,
					Function<Sim, Sim> parent,
					Class<DeathEvent> deathEventClass,
					Function<DeathEvent, Double> ageAtDeath){
		
		Partition<Double> INCOME_GROUP_20 = Partitions.between(
				Arrays.<Double>asList(1.0,
							5000.0, 10000.0, 15000.0, 20000.0, 25000.0, 30000.0, 35000.0, 40000.0, 45000.0, 50000.0, 55000.0,
							60000.0, 65000.0, 70000.0, 75000.0,
							80000.0, 90000.0, 100000.0, 110000.0, 120000.0, 130000.0, 140000.0, 
							150000.0, 175000.0, 
							200000.0, 250000.0, 300000.0, 350000.0, 400000.0));
	
	
		final Statistic<Sim> count = Statistics.count();
			  
		//  type SimPartition = Named[Partition[Timed[Sim]]]
		//  type SimStatistic = Named[Statistic[Sim]]
		Partition<Double> decadePartition = Partitions.range(0, 10, endTime);
		  
		// 2. define the AgentStatisticsCollectors
		AgentStatisticsCollector<Model, Sim> avgsByYear;
		{
			// define the snapshot times
			Partition<Double> yearly = Partitions.range(0, 1.0, endTime);

			// create spec
			StatisticsSpec<Sim> spec = new StatisticsSpec<Sim>();
			// define partitions
			spec.addTimePartition("Year", yearly);
			// define statistics
			spec.addStatistic("Average education", Statistics.mean.on(education));
			spec.addStatistic("Average education", Statistics.mean.on(education));
			spec.addStatistic("Average income", Statistics.mean.on(income));
			spec.addStatistic("Average health", Statistics.mean.on(health));
			spec.addStatistic("Adults", Statistics.count(doneEducation));
			spec.addStatistic("Children", Statistics.countNot(doneEducation));

			// create the collector
	        avgsByYear = new AgentStatisticsCollector<Model, Sim>(sims, spec, yearly);
	        
		}
		AgentStatisticsCollector<Model, Sim> hDistByAgeDec;
		{
			// create spec
			StatisticsSpec<Sim> spec = new StatisticsSpec<Sim>();
	        // define the partitions
			spec.addPartition("Health", Partitions.range(0.0, 0.1, 1.0).lift(health));
			spec.addPartition("Age", Partitions.range(0.0, 5.0, 100).lift(age));
			spec.addTimePartition("Decade", decadePartition);
			// define the statistics
			spec.addStatistic("Count", count);
			
			hDistByAgeDec = new AgentStatisticsCollector<Model,Sim>(sims, spec, decadePartition);
		}
		AgentStatisticsCollector<Model, Sim> pDistByAgeDec;
		{
			// create spec
			StatisticsSpec<Sim> spec = new StatisticsSpec<Sim>();
			// define the partitions
			spec.addPartition("Age", Partitions.range(0.0, 1.0, 100.0).lift(age));
			spec.addTimePartition("Decade", decadePartition);
			// define the statistics
			spec.addStatistic("Count", count);
			
			pDistByAgeDec = new AgentStatisticsCollector<Model, Sim>(sims, spec, decadePartition);
		}
	    AgentStatisticsCollector<Model, Sim> yDistByPtoC;
	    {
	    	// create spec
	    	StatisticsSpec<Sim> spec = new StatisticsSpec<Sim>();
	    	// define the partitions
	    	spec.addPartition("child income", INCOME_GROUP_20.lift(income));
	    	spec.addPartition("parent income", INCOME_GROUP_20.lift(income).lift(parent));
	    	spec.addTimePartition("Decade", decadePartition);
	    	// define the statistics
	    	spec.addStatistic("Count", count);
	    	
	    	yDistByPtoC = new AgentStatisticsCollector<Model, Sim>(sims, spec, decadePartition);
	    }
	    EventStatisticsCollector<DeathEvent, DeathEvent> deathsByAgeDec;
	    {
	    	// create spec
	    	StatisticsSpec<DeathEvent> spec = new StatisticsSpec<DeathEvent>();
	    	// define the partitions
	    	spec.addPartition("Age", Partitions.range(0, 5, 100).lift(ageAtDeath));
	    	spec.addTimePartition("Decade", decadePartition);
	    	// define stats
	    	spec.addStatistic("Deaths during decade", Statistics.<DeathEvent>count());
	    	spec.addStatistic("Average age at death", Statistics.mean.on(ageAtDeath));
	    	
	    	deathsByAgeDec = new EventStatisticsCollector<DeathEvent, DeathEvent>(deathEventClass, spec, decadePartition);
	    }
	    EventStatisticsCollector<DeathEvent, DeathEvent> lifeExpectancy;
	    {
	    	// create spec
	    	StatisticsSpec<DeathEvent> spec = new StatisticsSpec<DeathEvent>();
	    	// define the partition
	    	spec.addTimePartition("Decade", decadePartition);
	    	// define stats
	    	spec.addStatistic("Life Expectancy", Statistics.mean.on(ageAtDeath));
	    	
	    	lifeExpectancy = new EventStatisticsCollector<DeathEvent, DeathEvent>(deathEventClass, spec, decadePartition);
	    }


		  
		// add all the collectors to the underlying ABMStats object  
	    addAgentCollector("AvgsByYear", avgsByYear);
	    addEventCollector("DeathsByAgeDec", deathsByAgeDec);
	    addAgentCollector("HDistByAgeDec", hDistByAgeDec);
	    addEventCollector("LifeExpectancy", lifeExpectancy);
	    addAgentCollector("PDistByAgeDec", pDistByAgeDec);
	    addAgentCollector("YDistByPtoC", yDistByPtoC);
	    
	}

}
