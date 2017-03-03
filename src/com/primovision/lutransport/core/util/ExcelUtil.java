package com.primovision.lutransport.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelUtil {
	public static Object getCellValue(HSSFCell cell, boolean resolveFormula) {
		if (cell == null) {
			return null;
		}
		
		Object result = null;
		int cellType = cell.getCellType();
		switch (cellType) {
		case HSSFCell.CELL_TYPE_BLANK:
			result = "";
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			result = cell.getBooleanCellValue() ? Boolean.TRUE : Boolean.FALSE;
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			result = "ERROR: " + cell.getErrorCellValue();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			switch(cell.getCachedFormulaResultType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                System.out.println("Last evaluated as: " + cell.getNumericCellValue());
                result = cell.getNumericCellValue();
                break;
            case HSSFCell.CELL_TYPE_STRING:
                System.out.println("Last evaluated as \"" + cell.getRichStringCellValue() + "\"");
                result = cell.getRichStringCellValue();
                break;
			}
			//result = cell.getCellFormula();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			HSSFCellStyle cellStyle = cell.getCellStyle();
			short dataFormat = cellStyle.getDataFormat();

			System.out.println("Data format for " + cell.getColumnIndex() + " = " + dataFormat);
			// assumption is made that dataFormat = 14,
			// when cellType is HSSFCell.CELL_TYPE_NUMERIC
			// is equal to a DATE format.
			//if (dataFormat == 165 || dataFormat == 164 || dataFormat == 14) {
			if (DateUtil.isCellDateFormatted(cell)) {
				result = cell.getDateCellValue();
			} else {
				result = cell.getNumericCellValue();
			}
			
			if (dataFormat == 0) { // alternative way of getting value : can this be replaced for the entire block
				result = new HSSFDataFormatter().formatCellValue(cell);
			}
			System.out.println("Numeric cell value == " + result);
			
			break;
		case HSSFCell.CELL_TYPE_STRING:
			//result = cell.getStringCellValue();
			result = cell.getRichStringCellValue();
			System.out.println("String -> " + result);
			break;
		default:
			break;
		}
		
		if (result instanceof Integer) {
			return String.valueOf((Integer) result);
		} else if (result instanceof Double) {
			return String.valueOf(((Double) result)); //.longValue());
		}
		if (result instanceof Date) {
			return result;
		}
		return result.toString();
	}
   
	public static Cell createExcelCell(Sheet sheet, Row row, int columnIndex, int width) {
		Cell cell = row.createCell(columnIndex);
		sheet.setColumnWidth(columnIndex, width);
		return cell;
	}
	
	public static ByteArrayOutputStream createUploadErrorResponse(InputStream is, List<String> errors) throws IOException {
		POIFSFileSystem fs = new POIFSFileSystem(is);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		
		HSSFFont font = wb.createFont();
		font.setColor(Font.COLOR_RED);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(font);
		
		HSSFSheet sheet = wb.getSheetAt(0);
		
		Row row = sheet.getRow(0);
		int lastCell = row.getLastCellNum();
		Cell cell = createExcelCell(sheet, row, lastCell, 256*100);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("ERRORS");
		
		for (String anError : errors) {
			String lineNoStr = StringUtils.substringBefore(anError, ":");
			lineNoStr = StringUtils.substringAfter(lineNoStr, "Line ");
			Integer lineNo = new Integer(lineNoStr) - 1;
			
			row = sheet.getRow(lineNo);
			cell = createExcelCell(sheet, row, lastCell, 256*100);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(anError);
		}
		
		return createOutputStream(wb);
	}
	
	public static ByteArrayOutputStream createUploadSuccessResponse(String msg) {
		HSSFWorkbook wb = new HSSFWorkbook();
		
		HSSFFont font = wb.createFont();
		font.setColor(IndexedColors.GREEN.getIndex());
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(font);
		
		Sheet sheet = wb.createSheet();
		
		Row row = sheet.createRow(0);
		Cell cell = createExcelCell(sheet, row, 0, 256*100);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(msg);
		
		return createOutputStream(wb);
	}
	
	public static ByteArrayOutputStream createUploadExceptionResponse(Exception e) {
		HSSFWorkbook wb = new HSSFWorkbook();
		
		HSSFFont font = wb.createFont();
		font.setColor(Font.COLOR_RED);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(font);
		
		Sheet sheet = wb.createSheet();
		
		Row row = sheet.createRow(0);
		Cell cell = createExcelCell(sheet, row, 0, 256*100);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("An error occurred while uploading!!!");

		return createOutputStream(wb);
	}
	
	public static ByteArrayOutputStream createOutputStream(HSSFWorkbook wb) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	      
	   return out;
	}
}