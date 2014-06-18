abm-stats
=========

A statistics library for discrete-event agent-based modelling. Meant to be usable by any Java-based modelling framework (e.g. AnyLogic, Repast) as well as standalone models written in Java, Scala, Clojure, etc.

Usage Guide
----------------

### Prerequisites

To use the library, a model needs the following: 

1. Java classes for
  * the model as a whole
  * every agent that you want statistics to be collected over
  * every event that you want statistics to be collected over (must be immutable / value objects), and an `Event` type that is a supertype of all the event classes
2. A globally accessible model time

### Basic Usage

1. Before running the model
  1. Create an instance of `ABMStats`.
  2. Add specific statistics collectors to it using the `addAgentCollector` and `addEventCollector` methods.
  3. Store the instance somewhere globally accessible in the model
2. As the model runs
  * Call `timeChanged` on the `ABMStats` instance whenever the model time changes, or periodically depending on the AgentStatisticsCollectors added in 1.2
  * Call `event` on the `ABMStats` instance whenever an event occurs that you wish to track
3. After the model terminates
  * use any of the export methods to get the data from the `ABMStats` instance (currently only `exportToSpreadsheet`)

### THIM-Specific Usage

Same as above, but replace step 1.1 and 1.2 by calling the constructor of `THIMStats` with the appropriate parameters.



