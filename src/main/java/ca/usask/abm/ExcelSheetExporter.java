package ca.usask.abm;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public interface ExcelSheetExporter {
	/**
	  * Adds all internally collected data to the given excel sheet
	  */
	void exportData(XSSFSheet sheet);
}
