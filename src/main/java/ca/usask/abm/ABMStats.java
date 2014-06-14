package ca.usask.abm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * The main entry point for the library.
 * 
 * Typical use: 
 * 	 1) before model starts, parameterize the collection process using addAgentCollector and addEventCollector
 *   2) as the model runs, call timeChanged() and event() as appropriate
 *   3) after model terminates, use exportToSpreadsheet() to output all the statistics as an excel spreadsheet
 * @author isv452
 *
 */
public class ABMStats<Model, Event> {
	private HashMap<Observer<Model>, Integer> prevIDs = new HashMap<Observer<Model>, Integer>();
	private List<Observer<Event>> eventObservers = new ArrayList<Observer<Event>>();
	private LinkedHashMap<String, ExcelSheetExporter> exporters = new LinkedHashMap<String, ExcelSheetExporter>();
	
	/**
	 * Add an agent statistics collector to the ABMStats object
	 * @param name the excel sheet name used when the collector's data is eventually exported
	 * @param observer the agent collector
	 */
	public <Agent> void addAgentCollector(String name, AgentStatisticsCollector<Model, Agent> observer){
		prevIDs.put(observer, Partition.INVALID_ID);
		exporters.put(name, observer);
	}
	
	/**
	 * Add an event statistics collector to the ABMStats object
	 * @param name the excel sheet name used when the collector's data is eventually exported
	 * @param observer the event collector
	 */
	public <SpecificEvent> void addEventCollector(String name, EventStatisticsCollector<Event,SpecificEvent> observer){
		eventObservers.add(observer);
		exporters.put(name, observer);
	}
	
	/**
	 * Must be called whenever the time changes, with the updated model state
	 * @param time the new time
	 * @param m the updated model state
	 */
	public void timeChanged(double time, Model m){
		for (Observer<Model> observer : prevIDs.keySet()){
			// if we've transitioned to a new time partition, let the observer know
			int prevID = prevIDs.get(observer);
			int curID = observer.timePartition().toID(time);
			if (curID != prevID && curID != Partition.INVALID_ID){
				observer.update(time, m);
			}
			// update prevIDs
			prevIDs.put(observer, curID);
		}
	}
	
	/**
	 * Must be called whenever a new (tracked) event occurs
	 * @param time the time of the event
	 * @param e the event itself
	 */
	public void event(double time, Event e){
		for (Observer<Event> observer : eventObservers){
			observer.update(time, e);
		}
	}
	
	/**
	 * Export all the collectors' data into an excel spreadsheet at the given path (e.g. ~/mydata.xlsx)
	 * Each collector will generate a sheet, and sheets will be in the order that the collectors were added
	 * @param filepath
	 * @throws IOException if file cannot be created or written to
	 */
	public void exportToSpreadsheet(String filepath) throws IOException{
		File file = new File(filepath);
		file.mkdirs();
		FileOutputStream out = new FileOutputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook();
		for (String name : exporters.keySet()){
			exporters.get(name).exportData(wb.createSheet(name));
		}
		wb.write(out);
		out.close();
	}
	
	
}
