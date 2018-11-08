package com.primovision.lutransport.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.model.ErrorData;
import com.primovision.lutransport.model.EzToll;
import com.primovision.lutransport.model.TollCompany;
import com.primovision.lutransport.service.ImportMainSheetService;

public class TollCompanyTagUploadUtil {
	private static String TOLL_COMPANY_EZ_PASS_NY = "E-Z Pass NY";
	private static String TOLL_COMPANY_EZ_PASS_PA = "E-Z Pass PA";
	private static String TOLL_COMPANY_IPASS = "IPass";
	private static String TOLL_COMPANY_SUN_PASS = "SunPass";
	
	static String expectedDateFormatStr = "MM/dd/yy";
	static String expectedTimeFormatStr = "HH:mm";
	static SimpleDateFormat expectedDateFormat = new SimpleDateFormat(expectedDateFormatStr);
	static SimpleDateFormat expectedTimeFormat = new SimpleDateFormat(expectedTimeFormatStr);
	
	static HashMap<String, LinkedHashMap<String, String>> tollCompanyToTollTagMapping = new HashMap<String, LinkedHashMap<String, String>>();
	static LinkedList<String> expectedColumnList = new LinkedList<String>();
	
	static HashMap<String, String> tollCompanyToDateFormatMapping = new HashMap<String, String>();
	
	static {
		expectedColumnList.add("TOLL COMPANY"); // 0
		expectedColumnList.add("COMPANY"); // 1
		expectedColumnList.add("TERMINAL"); // 2
		expectedColumnList.add("TAG#"); // 3
		expectedColumnList.add("PLATE#"); // 4
		expectedColumnList.add("DRIVER NAME"); // 5
		expectedColumnList.add("TRANSACTION DATE"); // 6
		expectedColumnList.add("TRANSACTION TIME"); // 7
		expectedColumnList.add("AGENCY"); // 8
		expectedColumnList.add("AMOUNT"); // 9
		expectedColumnList.add("Invoice Date"); // 10
		//expectedColumnList.add("Unit #"); // 11

		tollCompanyToDateFormatMapping.put(TOLL_COMPANY_EZ_PASS_NY, "MM/dd/yy");
		tollCompanyToDateFormatMapping.put(TOLL_COMPANY_EZ_PASS_PA, "MM/dd/yy H:mm");
		tollCompanyToDateFormatMapping.put(TOLL_COMPANY_IPASS, "MM/dd/yy H:mm");
		tollCompanyToDateFormatMapping.put(TOLL_COMPANY_SUN_PASS, "MM/dd/yyyy hh:mm:ss");
		
		mapForEZPassNY(expectedColumnList);
		mapForEZPassPA(expectedColumnList);
		mapForIPass(expectedColumnList);
		mapForSunPass(expectedColumnList);
	}

	public static boolean isConversionRequired(Long tollCompanyId) {
		long tollCompnyIdLong = tollCompanyId.longValue();
		
		// EZ Pass NY
		if (tollCompnyIdLong == 3 || tollCompnyIdLong == 7 || tollCompnyIdLong == 8) {
			return true;
		} else if (tollCompnyIdLong == 1 || tollCompnyIdLong == 5 || tollCompnyIdLong == 6) { // EZ Pass PA
			return true;
		} else if (tollCompnyIdLong == 10 || tollCompnyIdLong == 11 || tollCompnyIdLong == 12) { // IPAss
			return true;
		} else if (tollCompnyIdLong == 9) { // SunPass
			return true;
		} else {
			return false;
		}
	}
	
	private static void mapForEZPassNY(LinkedList<String> expectedColumnList) {
		LinkedHashMap<String, String> actualColumnMap = new LinkedHashMap<String, String>();
		int expectedColumnStartIndex = 2;
		
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "TAG NUMBER/");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "TAG NUMBER/");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Driver Name");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "TRANS");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "TIME");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "AGENCY");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "AMOUNT");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice Date");
		//actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Unit #");
		tollCompanyToTollTagMapping.put(TOLL_COMPANY_EZ_PASS_NY, actualColumnMap);
	}
	
	private static void mapForEZPassPA(LinkedList<String> expectedColumnList) {
		LinkedHashMap<String, String> actualColumnMap = new LinkedHashMap<String, String>();
		int expectedColumnStartIndex = 2;
		
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "TAG/LICENSE");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "TAG/LICENSE");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Driver Name");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "EXIT DATE");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "EXIT DATE");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "EXIT PLAZA");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "AMOUNT");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice Date");
		//actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Unit #");
		tollCompanyToTollTagMapping.put(TOLL_COMPANY_EZ_PASS_PA, actualColumnMap);
	}
	
	private static void mapForIPass(LinkedList<String> expectedColumnList) {
		LinkedHashMap<String, String> actualColumnMap = new LinkedHashMap<String, String>();
		int expectedColumnStartIndex = 2;
		
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Transponder ID");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Transponder ID");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Driver Name");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Transaction date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Transaction date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Toll agency");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Transaction amount");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice Date");
		//actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Unit #");
		tollCompanyToTollTagMapping.put(TOLL_COMPANY_IPASS, actualColumnMap);
	}
	
	private static void mapForSunPass(LinkedList<String> expectedColumnList) {
		LinkedHashMap<String, String> actualColumnMap = new LinkedHashMap<String, String>();
		int expectedColumnStartIndex = 2;
		
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "TOLL TAG # / PLATE #");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "TOLL TAG # / PLATE #");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Driver Name");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "TRANSACTION DATE");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "TRANSACTION TIME");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "AMOUNT");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice Date");
		//actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Unit #");
		tollCompanyToTollTagMapping.put(TOLL_COMPANY_SUN_PASS, actualColumnMap);
	}
	
	public static InputStream convertToGenericTollTagFormat(InputStream is, Long tollCompanyId, GenericDAO genericDAO, ImportMainSheetService importMainSheetService) throws Exception {
		String tollCompanyName = getTollCompanyName(genericDAO, tollCompanyId);
		LinkedHashMap<String, String> actualColumnListMap = getTollCompanySpecificMapping(tollCompanyName);
		
		List<LinkedList<Object>> tempData = importMainSheetService.importTollCompanySpecificTollTag(is, actualColumnListMap, tollCompanyId);
		System.out.println("Number of rows = " + tempData.size());
		
		HSSFWorkbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		CellStyle style = wb.createCellStyle();

		createColumnHeaders(expectedColumnList, sheet, style);
		
		int rowIndex = 1;
		
		for (int i = 0; i < tempData.size(); i++) {
			System.out.println("Creating Row " + i + " with data = \n" + tempData.get(i));
			int columnIndex = 0;
			LinkedList<Object> oneRow = tempData.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			for (Object oneCellValue : oneRow) {
				if (columnIndex >= expectedColumnList.size()) { // For vendors where more than required columns are read from excel
					break;
				}
				System.out.println("Creating Column @ " + columnIndex + " with value = " + oneCellValue);
				Cell cell = createExcelCell(sheet, row, columnIndex);
				formatCellValueForTollCompany(wb, cell, oneCellValue, tollCompanyName);
				columnIndex++;
			}
		}

		InputStream targetStream = createInputStream(wb);
	   return targetStream;
	}

	private static LinkedHashMap<String, String> getTollCompanySpecificMapping(String tollCompanyName) {
		String commonTollCompanyName = getCommonTollCompanyName(tollCompanyName);
		return tollCompanyToTollTagMapping.get(commonTollCompanyName);
	}

	private static String getCommonTollCompanyName(String tollCompanyName) {
		String commonTollCompanyName = StringUtils.EMPTY;
		if (StringUtils.contains(tollCompanyName, TOLL_COMPANY_EZ_PASS_NY)) {
			commonTollCompanyName = TOLL_COMPANY_EZ_PASS_NY;
		} else if (StringUtils.contains(tollCompanyName, TOLL_COMPANY_EZ_PASS_PA)) {
			commonTollCompanyName = TOLL_COMPANY_EZ_PASS_PA;
		} else if (StringUtils.contains(tollCompanyName, TOLL_COMPANY_IPASS)) {
			commonTollCompanyName = TOLL_COMPANY_IPASS;
		} else if (StringUtils.contains(tollCompanyName, TOLL_COMPANY_SUN_PASS)) {
			commonTollCompanyName = TOLL_COMPANY_SUN_PASS;
		}
		return commonTollCompanyName;
	}

	private static String getTollCompanyName(GenericDAO genericDAO, Long tollCompanyId) {
		Map criterias = new HashMap();
		criterias.put("id", tollCompanyId);
		TollCompany tollCompany = genericDAO.findByCriteria(TollCompany.class, criterias, "name",false).get(0);
		return tollCompany.getName();
	}

	private static void formatCellValueForTollCompany(HSSFWorkbook wb,  Cell cell, Object oneCellValue, String vendor)
			throws Exception {
		if (vendor.contains(TOLL_COMPANY_EZ_PASS_NY)) { 
			formatCellValueForEZPassNY(wb, cell, oneCellValue, TOLL_COMPANY_EZ_PASS_NY);
		} else if (vendor.contains(TOLL_COMPANY_EZ_PASS_PA)) { 
			formatCellValueForEZPassPA(wb, cell, oneCellValue, TOLL_COMPANY_EZ_PASS_PA);
		} else if (vendor.contains(TOLL_COMPANY_IPASS)) { 
			formatCellValueForIPass(wb, cell, oneCellValue, TOLL_COMPANY_IPASS);
		} else if (vendor.contains(TOLL_COMPANY_SUN_PASS)) { 
			formatCellValueForSunPass(wb, cell, oneCellValue, TOLL_COMPANY_SUN_PASS);
		}
	}

	private static void formatCellValueForEZPassNY(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws Exception {
		if (oneCellValue == null) {
			oneCellValue = StringUtils.EMPTY;
			return;
		}
		
		int columnIndex = cell.getColumnIndex();
		
		if (columnIndex == 3) { // Tag num
			setCellValueTagNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 4) { // Plate num
			setCellValuePlateNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 5) { // Driver
			setCellValueDriverFormat(wb, cell, oneCellValue);
		} else if (oneCellValue instanceof Date || columnIndex == 6 || columnIndex == 10) { // Transaction date, Invoice date
			setCellValueDateFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 7) { // Transaction time
			String timeStr = stripToTimeFormat(oneCellValue);
			cell.setCellValue(timeStr);
		} else if (columnIndex == 8) { // Agency
			setCellValueAgencyFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 9) { // Amount
			setCellValueFeeFormat(wb, cell, oneCellValue, vendor);
		/*} else if (columnIndex == 11) {
			setCellValueUnitNumberFormat(wb, cell, oneCellValue, vendor);*/
		} else {
			cell.setCellValue(oneCellValue.toString().toUpperCase());
		}
	}
	
	private static void formatCellValueForEZPassPA(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws Exception {
		if (oneCellValue == null) {
			oneCellValue = StringUtils.EMPTY;
			return;
		}
		
		int columnIndex = cell.getColumnIndex();
		if (columnIndex == 4) { // Plate num
			setCellValuePlateNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 5) { // Driver
			setCellValueDriverFormat(wb, cell, oneCellValue);
		} else if (oneCellValue instanceof Date || columnIndex == 6 || columnIndex == 10) { // Transaction date and time, Invoice date
			setCellValueDateFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 9) { // Amount
			setCellValueFeeFormat(wb, cell, oneCellValue, vendor);
		/*} else if (columnIndex == 11) {
			setCellValueUnitNumberFormat(wb, cell, oneCellValue, vendor);*/
		} else {
			cell.setCellValue(oneCellValue.toString().toUpperCase());
		}
	}
	
	private static void formatCellValueForIPass(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws Exception {
		if (oneCellValue == null) {
			oneCellValue = StringUtils.EMPTY;
			return;
		}
		
		int columnIndex = cell.getColumnIndex();
		if (columnIndex == 5) { // Driver
			setCellValueDriverFormat(wb, cell, oneCellValue);
		} else if (oneCellValue instanceof Date || columnIndex == 6 || columnIndex == 10) { // Transaction date and time, Invoice date
			setCellValueDateFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 9) { // Amount
			setCellValueFeeFormat(wb, cell, oneCellValue, vendor);
		/*} else if (columnIndex == 11) {
			setCellValueUnitNumberFormat(wb, cell, oneCellValue, vendor);*/
		} else {
			cell.setCellValue(oneCellValue.toString().toUpperCase());
		}
	}
	
	private static void formatCellValueForSunPass(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws Exception {
		if (oneCellValue == null) {
			oneCellValue = StringUtils.EMPTY;
			return;
		}
		
		int columnIndex = cell.getColumnIndex();
		if (columnIndex == 3) { // Tag num
			setCellValueTagNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 5) { // Driver
			setCellValueDriverFormat(wb, cell, oneCellValue);
		} else if (oneCellValue instanceof Date || columnIndex == 6 || columnIndex == 10) { // Transaction date and time, Invoice date
			setCellValueDateFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 9) { // Amount
			setCellValueFeeFormat(wb, cell, oneCellValue, vendor);
		/*} else if (columnIndex == 11) {
			setCellValueUnitNumberFormat(wb, cell, oneCellValue, vendor);*/
		} else {
			cell.setCellValue(oneCellValue.toString().toUpperCase());
		}
	}
	
	/*private static void setCellValueUnitNumberFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) {
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		} 
		
		String cellValueStr = StringUtils.trimToEmpty(oneCellValue.toString());
		if (StringUtils.isEmpty(cellValueStr)) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		}
		
		setIntegerValue(cell, cellValueStr);
	}*/

	/*private static void setIntegerValue(Cell cell, Object oneCellValue) {
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		} 
		
		String cellValueStr = StringUtils.trimToEmpty(oneCellValue.toString());
		if (StringUtils.isEmpty(cellValueStr)) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		}
		
		if (!StringUtils.contains(cellValueStr, ".")) {
			cell.setCellValue(cellValueStr);
		} else {
			cell.setCellValue(Double.valueOf(cellValueStr).intValue());
		}
	}*/
	
	private static void setCellValueAgencyFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) {
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		} 
		
		String agency = StringUtils.trimToEmpty(oneCellValue.toString());
		agency = agency.replaceAll("\u00a0", StringUtils.EMPTY);
		cell.setCellValue(agency);
	}
	
	private static void setCellValueTagNumberFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) {
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		} 
		
		String tagNum = StringUtils.trimToEmpty(oneCellValue.toString());
		tagNum = tagNum.replaceAll("\u00a0", StringUtils.EMPTY);
		tagNum = StringUtils.stripStart(tagNum, "0");
		
		if (StringUtils.contains(vendor, TOLL_COMPANY_SUN_PASS)) {
			if (StringUtils.startsWith(tagNum, "155")) {
				tagNum = StringUtils.stripEnd(tagNum, "0");
			}
		}
		
		cell.setCellValue(tagNum);
	}
	
	private static void setCellValuePlateNumberFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) {
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		} 
		
		String plateNum = StringUtils.trimToEmpty(oneCellValue.toString());
		plateNum = plateNum.replaceAll("\u00a0", StringUtils.EMPTY);
		if (StringUtils.isEmpty(plateNum)) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		}
		
		if (StringUtils.contains(vendor, TOLL_COMPANY_EZ_PASS_NY)) {
			plateNum = StringUtils.stripStart(plateNum, "0");
			plateNum = StringUtils.substring(plateNum, 2);
		} else if (StringUtils.contains(vendor, TOLL_COMPANY_EZ_PASS_PA)) {
			plateNum = StringUtils.substring(plateNum, 3);
		}
		cell.setCellValue(plateNum);
	}
	
	private static void setCellValueDriverFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue) {
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
		} else {
			cell.setCellValue(oneCellValue.toString());
		}
	}

	private static void setCellValueFeeFormat(Workbook wb, Cell cell, Object oneCellValue, String vendor) {
		CellStyle style = wb.createCellStyle();
		style.setDataFormat(wb.createDataFormat().getFormat("$#,#0.00"));
		cell.setCellStyle(style);
		
		if (oneCellValue == null) {
			cell.setCellValue(Double.parseDouble("0.0"));
			return;
		}
		
		String feeStr = StringUtils.replace(oneCellValue.toString(), "$", StringUtils.EMPTY);
		feeStr = StringUtils.trimToEmpty(feeStr);
		feeStr = feeStr.replaceAll("\\p{javaSpaceChar}", StringUtils.EMPTY);
		if (StringUtils.isEmpty(feeStr)) {
			cell.setCellValue(Double.parseDouble("0.0"));
			return;
		}
		
		if (StringUtils.contains(vendor, TOLL_COMPANY_EZ_PASS_PA) || StringUtils.contains(vendor, TOLL_COMPANY_IPASS)
				 || StringUtils.contains(vendor, TOLL_COMPANY_SUN_PASS)) {
			if (StringUtils.startsWith(feeStr, "-")) {
				feeStr = StringUtils.substring(feeStr, 1);
			} else {
				feeStr = "-" + feeStr;
			}
		}
		
		cell.setCellValue(Double.parseDouble(feeStr));
	}

	private static void setCellValueDateFormat(Workbook wb, Cell cell, Object oneCellValue, String vendor) throws ParseException {
		System.out.println("Incoming vendor = " + vendor);
		String tollCompanyDateFormat = tollCompanyToDateFormatMapping.get(vendor);
		System.out.println("Value = " + tollCompanyDateFormat);
		
		int columnIndex = cell.getColumnIndex();
		
		if (oneCellValue instanceof Date) {
			System.out.println("Incoming date is a Date Object.");
			tollCompanyDateFormat = "EEE MMM dd HH:mm:ss z yyyy";
		}
		
		String dateStr = StringUtils.trimToEmpty(oneCellValue.toString());
		
		if (StringUtils.isEmpty(dateStr)) {
			cell.setCellValue(StringUtils.EMPTY);
		} else {
			if (columnIndex == 7) { // Transaction time
				cell.setCellValue(convertToExpectedTimeFormatStr(dateStr, tollCompanyDateFormat));
				return;
			} else {
				cell.setCellValue(convertToExpectedDateFormat(dateStr, tollCompanyDateFormat));
			}
		}
		
		CellStyle style = wb.createCellStyle();
		style.setDataFormat(wb.createDataFormat().getFormat(expectedDateFormat.toPattern()));
		cell.setCellStyle(style);
	}
	
	private static Date convertToExpectedDateFormat(String actualDateStr, String actualDateFormat) throws ParseException {
		Date actualDate = new SimpleDateFormat(actualDateFormat).parse(actualDateStr);
		String expectedDateStr = expectedDateFormat.format(actualDate);
		return expectedDateFormat.parse(expectedDateStr);
	}
	
	private static String convertToExpectedTimeFormatStr(String actualTimeStr, String actualTimeFormat) throws ParseException {
		Date actualTime = new SimpleDateFormat(actualTimeFormat).parse(actualTimeStr);
		String expectedTimeStr = expectedTimeFormat.format(actualTime);
		return expectedTimeStr;
	}
	
	private static String stripToTimeFormat(Object oneCellValue) {
		if (oneCellValue == null) {
			return StringUtils.EMPTY;
		}
		
		String actualTimeStr = StringUtils.trimToEmpty(oneCellValue.toString());
		return actualTimeStr.replaceAll("\\p{javaSpaceChar}", StringUtils.EMPTY);
	}
	
	private static InputStream createInputStream(HSSFWorkbook wb) {
		//dumpToFile(wb);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			wb.write(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	   InputStream targetStream = new ByteArrayInputStream(out.toByteArray());
		return targetStream;
	}
	
	private static ByteArrayOutputStream createOutputStream(HSSFWorkbook wb) {
		//dumpToFile(wb);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			wb.write(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	   return out;
	}
	
	private static void dumpToFile(HSSFWorkbook wb) {
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream("/Users/raghav/Desktop/Test.xls");
			wb.write(fOut);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void createColumnHeaders(LinkedList<String> expectedColumnList, Sheet sheet, CellStyle style) {
		Row headerRow = sheet.createRow(0);
		int columnHeaderIndex = 0;
		for (String columnHeader : expectedColumnList) { // TODO redundant, use actualColumnListMap.keys instead
			sheet.setColumnWidth(columnHeaderIndex, 256*20);
			Cell cell = headerRow.createCell(columnHeaderIndex++);
			cell.setCellValue(columnHeader);
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(style);
		}
	}

	private static Cell createExcelCell(Sheet sheet, Row row, int columnIndex) {
		return createExcelCell(sheet, row, columnIndex, 256*20);
	}
	
	private static Cell createExcelCell(Sheet sheet, Row row, int columnIndex, int width) {
		Cell cell = row.createCell(columnIndex);
		sheet.setColumnWidth(columnIndex, width);
		return cell;
	}
	
	public static ByteArrayOutputStream createTollUploadResponse(InputStream is, List<String> errors) throws IOException {
		if (errors == null || errors.isEmpty()) {
			return createTollUploadSuccessResponse();
		} else {
			return createTollUploadErrorResponse(is, errors);
		}
	}
	
	public static ByteArrayOutputStream createTollUploadErrorResponse(InputStream is, List<String> errors) throws IOException {
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
	
	public static ByteArrayOutputStream createTollUploadSuccessResponse() {
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
		cell.setCellValue("ALL tolls uploaded successfully");
		
		return createOutputStream(wb);
	}
	
	public static ByteArrayOutputStream createTollUploadExceptionResponse(Exception e) {
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
}
