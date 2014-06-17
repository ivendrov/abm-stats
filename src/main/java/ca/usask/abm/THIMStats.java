package ca.usask.abm;


import java.util.Arrays;


public class THIMStats<Model, Event, Sim> extends ABMStats<Model, Event> {
	
	/**
	 * @param endTime the time until which to collect statistics
	 */
	public THIMStats(double endTime,
					Function<Model, Iterable<Sim>> sims,
					Function<Sim, Double> education,
					Function<Sim, Double> income,
					Function<Sim, Double> health,
					Function<Sim, Double> age,
					Function<Sim, Boolean> doneEducation){
		
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
			spec.addPartition("Age", Partitions.range(0.0, 10.0, 100).lift(age));
			spec.addPartition("Health", Partitions.range(0.0, 0.1, 1.0).lift(health));
			spec.addTimePartition("Decade", decadePartition);
			// define the statistics
			spec.addStatistic("Count", count);
			
			hDistByAgeDec = new AgentStatisticsCollector<Model,Sim>(sims, spec, decadePartition);
		}
		  /*
		  val pDistByAgeDec = {
			  val partitions : Seq[(String, Partition[Sim])] = Seq (
			      ("Age", Partition.inject(100).lift(_.age)))
			  new AgentStatisticsCollector(agentAccessor, partitions, justCount, Some(1), 0 to endTime by 10)
		  }
		  val yDistByPtoC = {
			  val partitions : Seq[(String, Partition[Sim])] = Seq (
			      ("child income", INCOME_GROUP_20 lift (_.income)), 
			      ("parent income", INCOME_GROUP_20 liftOption (_.parent.map(_.income))) 
			      )
			  
			   new AgentStatisticsCollector(agentAccessor, partitions, justCount, Some(2), 0 to endTime by 10)
		  }
		  
		  // 3. define the EventStatisticsCollectors
		  
		  val deathsByYear = {
		    val partitions : Seq [(String, Partition[DeathReport])] = Seq (
		        ("Age", Partition.range(0, 100, 5).lift(_.sim.age))
		        )
		    val stats : Seq [(String, Seq[DeathReport] => Double)] = Seq (
		       ("Deaths during decade", _ length),
		       ("Average Age at Death", _ map (_.sim.age) average)
		       )
		    new EventStatisticsCollector[EventReport, DeathReport](EventReport.toDeath, partitions, stats, Some(1), 0 to endTime by 10)
		  }
		  
		  val lifeExpectancy = {
		    val stats : Seq [(String, Seq[DeathReport] => Double)] = Seq (
		        ("Life Expectancy", _ map (_.sim.age) average) // TODO is this right?
		        )
		    new EventStatisticsCollector[EventReport, DeathReport](EventReport.toDeath, Seq(), stats, Some(0), 0 to endTime by 10)
		  }
		  
		  // 4. add all the observers 
		  model.addObserver(avgsByYear)
		  model.addObserver(hDistByAgeDec)
		  model.addObserver(pDistByAgeDec)
		  model.addObserver(yDistByPtoC)
		  model.addObserver(deathsByYear)
		  model.addObserver(lifeExpectancy)
		  */
		  
		  
		  addAgentCollector("AvgsByYear", avgsByYear);
		  addAgentCollector("HDistByAgeDec", hDistByAgeDec);
	}

}
