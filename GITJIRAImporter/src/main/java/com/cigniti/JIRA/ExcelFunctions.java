package com.cigniti.JIRA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
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


		// returns true if column is created successfully
	public static boolean fn_VerifyColumnExist(String xl_FilePath, String sheetName,String colName){
			//System.out.println("**************addColumn*********************");

			try{
				Boolean bln_ColExist = false;
				final MissingCellPolicy CREATE_NULL_AS_BLANK = MissingCellPolicy.CREATE_NULL_AS_BLANK;
				FileInputStream fis = new FileInputStream(xl_FilePath); 
				XSSFWorkbook WbookObj = new XSSFWorkbook(fis);
				int index = WbookObj.getSheetIndex(sheetName);
				if(index==-1)
					return false;

				XSSFSheet WSheetObj=WbookObj.getSheetAt(index);

				XSSFRow FstRowObj = WSheetObj.getRow(0);
				int columnCount = FstRowObj.getLastCellNum();
				int columnNumber = 0;
				for (int i = 0; i <= columnCount - 1; i++) {
					XSSFCell cellObj = FstRowObj.getCell(i, CREATE_NULL_AS_BLANK);
					String xl_ColumnName = cellObj.getStringCellValue();
					xl_ColumnName = xl_ColumnName.trim();
					colName = colName.trim();
					if (colName.equalsIgnoreCase(xl_ColumnName)) {
						bln_ColExist = true;
						break;
					}
				}
				return bln_ColExist;		    

			}catch(Exception e){
				e.printStackTrace();
				return false;
			}

			
		}

		
		@SuppressWarnings("deprecation")
		public static boolean fn_AddColumn(String xl_FilePath, String sheetName,String colName){
			//System.out.println("**************addColumn*********************");

			try{	
				int ColNo = 0;
				FileInputStream fis = new FileInputStream(xl_FilePath); 
				XSSFWorkbook WbookObj = new XSSFWorkbook(fis);
				int index = WbookObj.getSheetIndex(sheetName);
				if(index==-1)
					return false;

				XSSFSheet sheet=WbookObj.getSheetAt(index);

				
				XSSFCellStyle style = WbookObj.createCellStyle();
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				
				XSSFRow row = sheet.getRow(0);
				XSSFCell cell = null;
				ColNo = row.getLastCellNum();
				//set border style for new column
				for(int i=0; i<=sheet.getLastRowNum();i++)
				{
					row = sheet.getRow(i);
					cell = row.createCell(ColNo);				
					cell.setCellStyle(style);
					if(i==0)
					{
						cell.setCellValue(colName);
					}					
				}
		
	
				FileOutputStream fileOut = new FileOutputStream(xl_FilePath);
				WbookObj.write(fileOut);
				fileOut.close();		    

			}catch(Exception e){
				e.printStackTrace();
				return false;
			}

			return true;
		}

		// returns true if data is set successfully else false
		public static boolean fn_SetCellData(String xl_FilePath, String sheetName,String colName,int rowNum, String data){
			try{
				FileInputStream fis = new FileInputStream(xl_FilePath); 
				XSSFWorkbook workbook = new XSSFWorkbook(fis);
				if(rowNum<=0)
					return false;

				int index = workbook.getSheetIndex(sheetName);
				int colNum=-1;
				if(index==-1)
					return false;
				XSSFSheet sheet = workbook.getSheetAt(index);
				XSSFRow row=sheet.getRow(0);
				for(int i=0;i<row.getLastCellNum();i++){
					//System.out.println(row.getCell(i).getStringCellValue().trim());
					if(row.getCell(i).getStringCellValue().trim().equals(colName))
					{
						colNum=i;
						break;
					}
				}
				if(colNum==-1)
					return false;

				sheet.autoSizeColumn(colNum); 
				row = sheet.getRow(rowNum);
				if (row == null)
					row = sheet.createRow(rowNum);

				XSSFCell cell = row.getCell(colNum);	
				if (cell == null)
					cell = row.createCell(colNum);

				// cell style
				//CellStyle cs = workbook.createCellStyle();
				//cs.setWrapText(true);
				//cell.setCellStyle(cs);
				cell.setCellValue(data);

				FileOutputStream fileOut = new FileOutputStream(xl_FilePath);
				workbook.write(fileOut);
				fileOut.close();	

			}
			catch(Exception e){
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		
		@SuppressWarnings("deprecation")
		public static boolean fn_VerifyFileClosed(String xl_FilePath){
			//System.out.println("**************addColumn*********************");

			try{	
				
				File file = new File(xl_FilePath);
			    // try to rename the file with the same name
			    File sameFileName = new File(xl_FilePath);

			    if(file.renameTo(sameFileName))
			    	return true;
			    else
			    	return false;
			    

			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			
		}


}
