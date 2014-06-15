package ca.usask.abm;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public interface ExcelSheetExporter {
	/**
	  * Adds all internally collected data to the given Excel sheet
	  * @param sheet a blank Excel sheet
	  */
	void exportData(XSSFSheet sheet);
}
