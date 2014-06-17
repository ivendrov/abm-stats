package ca.usask.abm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;



public abstract class StatisticsCollector<T> implements ExcelSheetExporter {
	
	protected StatisticsSpec<T> spec;
	protected MultiDimArray<List<Double>> partitionedStats;
	

	/**
	 * @param spec the specification object
	 */
	public StatisticsCollector(StatisticsSpec<T> spec) {
		super();
		this.spec = spec;
		
		// statistics results on empty sequence
		List<Double> result = spec.evalStatisticsOn(Collections.<T> emptyList());
		
		// create the n-dimensional statistics array
		partitionedStats = new MultiDimArray<List<Double>>
							(spec.partitionDimensions(), result);

	}
	

	/**
     * Processes a given collection of objects into statistics 
     * and places them into the statistics array.
     * 
     * Crucially, this will overwrite existing statistics, so 
     * to use this method correctly, it must never be called twice
     * with agents that are in the same category according to the StatisticsSpec
	 * 
	 * @param objects the objects to be processed
	 */
	protected void processStats(Collection<Timed<T>> objects){
		// 1. initialize a sparse array of new stats to be added to 
		// partitionedStats
	    final HashMap<Integer, List<T>> sparseArray = 
	    			new HashMap<Integer, List<T>>();
	    
	    // 2. fill the sparse array
	    for (Timed<T> obj : objects){
	    	List<Integer> ids = spec.evalPartitionsOn(obj);
	    	if (ids == null) continue; // object not part of stat collection
	    	int index = partitionedStats.toLinearIndex(ids);
	    	// add this object to sparse array
	    	if (sparseArray.containsKey(index))
	    		sparseArray.get(index).add(obj.getValue());
	    	else {
	    		ArrayList<T> list = new ArrayList<T>();
	    		list.add(obj.getValue());
	    		sparseArray.put(index, list);
	    	}
	    }
	    
	    // 3. add sparse array to partitionedStats
	    for (int index : sparseArray.keySet()){
	    	Collection<T> os = sparseArray.get(index);
	    	partitionedStats.set(index, spec.evalStatisticsOn(os));
	    }
	}
	
	
	/**
	 * An optimization - store objects in the buffer as they come in,
	 * then flush the buffer periodically
	 */
	protected List<Timed<T>> objectBuffer = new ArrayList<Timed<T>>();
	
	/**
	 * Processes statistics with the objectBuffer, then clears it.
	 * Make sure to respect the distinct-category requirement of processStats.
	 */
	protected void flushBuffer(){
		processStats(objectBuffer);
		objectBuffer.clear();
	}
	
	@Override
	public void exportData(XSSFSheet sheet) {
	    flushBuffer();
	    // print first row, with all the names
	    XSSFRow row = sheet.createRow(0);
	    int col = 0;
	    List<String> names = spec.partitionNames();
	    names.addAll(spec.statisticNames());
	    for (String name : names){
	    	row.createCell(col).setCellValue(name);
	    	col++;
	    }
	    // for each classified group of objects    
	    for (int i = 0; i < partitionedStats.size(); i++){
	      // create a new row
	      row = sheet.createRow(i+1);
	      col = 0;
	      // print all the partition ID labels
	      Collection<Integer> index = partitionedStats.fromLinearIndex(i);
	      for (String label : spec.partitionLabels(index)){
	    	  row.createCell(col).setCellValue(label);
	    	  col++;
	      }
	      // print all the statistics
	      for (double stat : partitionedStats.get(i)){
	    	  row.createCell(col).setCellValue(stat);
	    	  col++;
	      }
	    }
	  } 
}
