package com.cigniti.JIRA;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcelFunctions {

	
	
	/**
	 * @author Akarsh Durgekar
	 * @param WbookPath
	 * @param SheetName
	 * @param RowNum
	 * @param ColumnName
	 * @return
	 * @throws IOException
	 */
	public static String fn_GetCellData(String WbookPath, String SheetName, int RowNum, String ColumnName)
			throws IOException {

		try {
			final MissingCellPolicy CREATE_NULL_AS_BLANK = MissingCellPolicy.CREATE_NULL_AS_BLANK;
			FileInputStream FISObj = new FileInputStream(WbookPath);
			@SuppressWarnings("resource")
			XSSFWorkbook WbookObj = new XSSFWorkbook(FISObj);
			XSSFSheet WSheetObj = WbookObj.getSheet(SheetName);
			XSSFRow RowObj = WSheetObj.getRow(RowNum);
			int columnNumber = fn_GetCellNumberByColumName(WSheetObj, ColumnName);
			XSSFCell CellObj = RowObj.getCell(columnNumber, CREATE_NULL_AS_BLANK);
			int celltypenumber = CellObj.getCellType();
			String CellVal = null;
			if (celltypenumber == XSSFCell.CELL_TYPE_NUMERIC) {
				Double DblCellVal = CellObj.getNumericCellValue();
				Integer intcellval = DblCellVal.intValue();
				CellVal = intcellval.toString();
			} else if (celltypenumber == XSSFCell.CELL_TYPE_STRING) {
				CellVal = CellObj.getStringCellValue();
			}
			return CellVal;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw(e);
		}
	}

	/**
	 * @author Akarsh Durgekar
	 * @param WbookPath
	 * @param SheetName
	 * @param RowNum
	 * @param ColumnName
	 * @return
	 * @throws IOException
	 */
	public static String fn_GetCellData(String WbookPath, String SheetName, int RowNum, int ColumnNum)
			throws IOException {

		final MissingCellPolicy CREATE_NULL_AS_BLANK = MissingCellPolicy.CREATE_NULL_AS_BLANK;
		FileInputStream FISObj = new FileInputStream(WbookPath);
		@SuppressWarnings("resource")
		XSSFWorkbook WbookObj = new XSSFWorkbook(FISObj);
		XSSFSheet WSheetObj = WbookObj.getSheet(SheetName);
		XSSFRow RowObj = WSheetObj.getRow(RowNum);
		XSSFCell CellObj = RowObj.getCell(ColumnNum, CREATE_NULL_AS_BLANK);
		int celltypenumber = CellObj.getCellType();
		String CellVal = null;
		if (celltypenumber == XSSFCell.CELL_TYPE_NUMERIC) {
			Double DblCellVal = CellObj.getNumericCellValue();
			Integer intcellval = DblCellVal.intValue();
			CellVal = intcellval.toString();
		} else if (celltypenumber == XSSFCell.CELL_TYPE_STRING) {
			CellVal = CellObj.getStringCellValue();
		}
		return CellVal;
	}

	
	/**
	 * @author Akarsh Durgekar
	 * @param WSheetObj
	 * @param arg_ColumnName
	 * @return
	 * @throws IOException
	 */
	public static int fn_GetCellNumberByColumName(XSSFSheet WSheetObj, String ColumnName) throws IOException {

		final MissingCellPolicy CREATE_NULL_AS_BLANK = MissingCellPolicy.CREATE_NULL_AS_BLANK;
		XSSFRow FstRowObj = WSheetObj.getRow(0);
		int columnCount = FstRowObj.getLastCellNum();
		int columnNumber = 0;
		for (int i = 0; i <= columnCount - 1; i++) {
			XSSFCell cellObj = FstRowObj.getCell(i, CREATE_NULL_AS_BLANK);
			String xl_ColumnName = cellObj.getStringCellValue();
			xl_ColumnName = xl_ColumnName.trim();
			ColumnName = ColumnName.trim();
			if (ColumnName.equalsIgnoreCase(xl_ColumnName) == true) {
				columnNumber = i;
				break;
			}
		}
		return columnNumber;
	}
	
	/**
	 * @author Akarsh Durgekar
	 * @param xl_FilePath
	 * @param SheetName
	 * @return
	 * @throws IOException
	 */
	public static int fn_GetRowCount(String xl_FilePath, String SheetName) throws IOException {

		try {
			File xlFileObj = new File(xl_FilePath);
			FileInputStream FISObj = new FileInputStream(xlFileObj);
			@SuppressWarnings("resource")
			XSSFWorkbook WbookObj = new XSSFWorkbook(FISObj);
			XSSFSheet WSheetObj = WbookObj.getSheet(SheetName);
			int rowcount = WSheetObj.getLastRowNum();
			return rowcount;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw(e);
		}
	}
	
	
	/**
	 * @author Akarsh Durgekar
	 * @param xl_FilePath
	 * @param SheetName
	 * @return
	 * @throws IOException
	 */
	public static int fn_GetColumnCount(String xl_FilePath, String SheetName, int RowNum) throws IOException {

		File xlFileObj = new File(xl_FilePath);
		FileInputStream FISObj = new FileInputStream(xlFileObj);
		@SuppressWarnings("resource")
		XSSFWorkbook WbookObj = new XSSFWorkbook(FISObj);
		XSSFSheet WSheetObj = WbookObj.getSheet(SheetName);
		int colcount = WSheetObj.getRow(RowNum).getLastCellNum();
		return colcount;
	}

	
	
	/**
	 * @author Akarsh Durgekar
	 * @param xl_FilePath
	 * @param SheetName
	 * @return
	 * @throws IOException
	 */
	public static List<String> fnGetSheets(String xl_FilePath) throws IOException {

		
		try
		{
			List<String> sheetList = new ArrayList<String>();
			File xlFileObj = new File(xl_FilePath);
			FileInputStream FISObj = new FileInputStream(xlFileObj);
			@SuppressWarnings("resource")
			XSSFWorkbook WbookObj = new XSSFWorkbook(FISObj);
					
			for (int i=0; i<WbookObj.getNumberOfSheets(); i++) {
				sheetList.add(WbookObj.getSheetName(i));
			}
			
			return sheetList;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}




}
