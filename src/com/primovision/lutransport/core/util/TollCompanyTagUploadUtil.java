package com.primovision.lutransport.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.model.FuelVendor;
import com.primovision.lutransport.model.TollCompany;
import com.primovision.lutransport.service.ImportMainSheetService;

public class TollCompanyTagUploadUtil {
	private static String TOLL_COMPANY_EZ_PASS = "E-Z Pass";
	
	static String expectedDateFormatStr = "MM/dd/yy";
	static SimpleDateFormat expectedDateFormat = new SimpleDateFormat(expectedDateFormatStr);
	
	static HashMap<String, LinkedHashMap<String, String>> tollCompanyToTollTagMapping = new HashMap<String, LinkedHashMap<String, String>>();
	static LinkedList<String> expectedColumnList = new LinkedList<String>();
	
	static HashMap<String, String> tollCompanyToDateFormatMapping = new HashMap<String, String>();
	
	static {
		expectedColumnList.add("TOLL COMPANY"); // 0
		expectedColumnList.add("COMPANY"); // 1
		expectedColumnList.add("TERMINAL	TAG#"); // 2
		expectedColumnList.add("PLATE#"); // 3
		expectedColumnList.add("Driver Name"); // 4
		expectedColumnList.add("TRANSACTION DATE"); // 5
		expectedColumnList.add("TRANSACTION TIME"); // 6
		expectedColumnList.add("AGENCY"); // 7
		expectedColumnList.add("AMOUNT"); // 8

		tollCompanyToDateFormatMapping.put(TOLL_COMPANY_EZ_PASS, "yyyy-MM-dd");
		
		mapForEZPass(expectedColumnList);
	}

	public static boolean isConversionRequired(Long tollCompanyId) {
		// EZ Pass
		if (tollCompanyId.longValue() == 8) {
			return true;
		} else {
			return false;
		}
	}
	
	private static void mapForEZPass(LinkedList<String> expectedColumnList) {
		LinkedHashMap<String, String> actualColumnMap = new LinkedHashMap<String, String>();
		int expectedColumnStartIndex = 2;
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "TransactionPOSDate");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "TransactionPOSTime");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Unit");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "DriverName");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "DriverLastName");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "CardNumber"); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "ProductCode");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "LocationCity"); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "LocationState");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Quantity");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "PricePerUnit");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "TotalInvoice");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "TransactionFee");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "TransactionDiscount");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex), "TransactionGross");
		tollCompanyToTollTagMapping.put(TOLL_COMPANY_EZ_PASS, actualColumnMap);
	}
	
	public static InputStream convertToGenericTollTagFormat(InputStream is, Long tollCompanyId, GenericDAO genericDAO, ImportMainSheetService importMainSheetService) throws Exception {
		String tollCompanyName = getTollCompanyName(genericDAO, tollCompanyId);
		LinkedHashMap<String, String> actualColumnListMap = getTollCompanySpecificMapping(TOLL_COMPANY_EZ_PASS);
		
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
				oneCellValue = consolidateDataForTollCompanies(wb, cell, oneRow, oneCellValue, tollCompanyName);
				formatCellValueForTollCompany(wb, cell, oneCellValue, tollCompanyName);
				columnIndex++;
			}
		}

		InputStream targetStream = createInputStream(wb);
	   return targetStream;
	}

	private static Object consolidateDataForTollCompanies(HSSFWorkbook wb, Cell cell, LinkedList<Object> oneRow, Object oneCellValue, String tollCompany) {
		if (!StringUtils.contains(tollCompany, TOLL_COMPANY_EZ_PASS)) {
			return oneCellValue;
		}
		
		if (cell.getColumnIndex() == 9) { // Card Number
			String cardNumber = oneRow.get(9) == null ? StringUtils.EMPTY : oneRow.get(9).toString();
			cardNumber = cardNumber.length() > 5 ? cardNumber.substring(cardNumber.length()-5, cardNumber.length()) : cardNumber;
			
			int accountNumberIndex = expectedColumnList.size();
			String accountNumber = oneRow.get(accountNumberIndex) == null ? StringUtils.EMPTY : oneRow.get(accountNumberIndex).toString();
			
			return accountNumber + cardNumber;
		} else if (cell.getColumnIndex() == 16) { // fees
			// Service Cost + Other Cost + Total Non-Fuel Cost = Fees
			String serviceCost = oneRow.get(16) == null ? "0.0" : oneRow.get(16).toString();
			
			int otherCostIndex = expectedColumnList.size() + 1;
			String otherCost = oneRow.get(otherCostIndex) == null ? "0.0" : oneRow.get(otherCostIndex).toString();
			
			int nonFuelCostIndex = otherCostIndex + 1;
			String nonFuelCost = oneRow.get(nonFuelCostIndex) == null ? "0.0" : oneRow.get(nonFuelCostIndex).toString();
			
			BigDecimal fees = new BigDecimal(serviceCost).add(new BigDecimal(otherCost)).add(new BigDecimal(nonFuelCost));
			return fees.toPlainString();
		}
		
		return oneCellValue;
	}

	private static LinkedHashMap<String, String> getTollCompanySpecificMapping(String tollCompanyName) {
		String commonTollCompanyName = getCommonTollCompanyName(tollCompanyName);
		return tollCompanyToTollTagMapping.get(commonTollCompanyName);
	}

	private static String getCommonTollCompanyName(String tollCompanyName) {
		String commonTollCompanyName = StringUtils.EMPTY;
		if (StringUtils.contains(tollCompanyName, TOLL_COMPANY_EZ_PASS)) {
			commonTollCompanyName = TOLL_COMPANY_EZ_PASS;
		}
		return commonTollCompanyName;
	}

	private static String getTollCompanyName(GenericDAO genericDAO, Long tollCompanyId) {
		Map criterias = new HashMap();
		criterias.put("id", tollCompanyId);
		TollCompany tollCompany = genericDAO.findByCriteria(TollCompany.class, criterias, "name",false).get(0);
		return tollCompany.getName();
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
	
	private static void dumpToFile(HSSFWorkbook wb) {
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream("/Users/raghavp/Desktop/Test.xls");
			wb.write(fOut);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void formatCellValueForTollCompany(HSSFWorkbook wb,  Cell cell, Object oneCellValue, String vendor)
			throws Exception {
		if (vendor.equalsIgnoreCase(TOLL_COMPANY_EZ_PASS)) { 
			formatCellValueForEZPass(wb, cell, oneCellValue, vendor);
		} 
	}

	private static void setCellValueUnitNumberFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) {
		setIntegerValue(cell, oneCellValue);
	}

	private static void setIntegerValue(Cell cell, Object oneCellValue) {
		if (oneCellValue == null || (StringUtils.isEmpty(oneCellValue.toString()))) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		} 
		
		String cellValueStr = oneCellValue.toString();
		if (!StringUtils.contains(cellValueStr, ".")) {
			cell.setCellValue(cellValueStr);
		} else {
			cell.setCellValue(Double.valueOf(cellValueStr).intValue());
		}
	}

	private static void setCellValueInvoiceNumberFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue,
			String vendor) {
		setIntegerValue(cell, oneCellValue);
	}

	private static void formatCellValueForEZPass(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws Exception {
		if (oneCellValue == null) {
			oneCellValue = StringUtils.EMPTY;
			return;
		} 
		
		int columnIndex = cell.getColumnIndex();
		if (oneCellValue instanceof Date || columnIndex == 2 || columnIndex == 4) { 
			setCellValueDateFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 3) {
			setCellValueInvoiceNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 6) {
			setCellValueUnitNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 5) {
			setCellValueTimeFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 7 || columnIndex == 8) {
			setCellValueDriverFormat(wb, cell, oneCellValue);
		} else if (columnIndex == 9) {
			//setCellValueFuelCardFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 10) {
			setCellValueFuelTypeFormat(wb, cell, oneCellValue, vendor);
		} else if (oneCellValue instanceof Double || columnIndex == 13) { // gallons
			setCellValueDoubleFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex > 13 && columnIndex < 19) { // fee
			setCellValueFeeFormat(wb, cell, oneCellValue, vendor);
		} else {
			cell.setCellValue(oneCellValue.toString().toUpperCase());
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
		Cell cell = row.createCell(columnIndex);
		sheet.setColumnWidth(columnIndex, 256*20);
		return cell;
	}
	
	private static void setCellValueDriverFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue) {
		int columnIndex = cell.getColumnIndex();
		String driverName = oneCellValue.toString();
		if (columnIndex == 7) { // lastname
			// Put the value as is, validation can be done after getting the firstname @ columnIndex=8
			cell.setCellValue(StringUtils.upperCase(driverName));
			return;
		}  
		
		if (columnIndex == 8) { // firstname
			if (!StringUtils.isEmpty(driverName)) {
				cell.setCellValue(StringUtils.upperCase(driverName));
				return;
			}
			
			Cell lastNameCell = cell.getRow().getCell(7);
			String lastNameCellValue = lastNameCell.getStringCellValue();
			String [] nameArr = null;
			// Split into firstname and lastname
			if (lastNameCellValue.contains(",")) {
				// Split based on comma
				nameArr = lastNameCellValue.split(",");
				if (nameArr.length > 1) {
					cell.setCellValue(nameArr[1]); // firstname
					lastNameCell.setCellValue(nameArr[0]);
				} else {
					cell.setCellValue(nameArr[0]); // firstname
					lastNameCell.setCellValue(StringUtils.EMPTY);
				}
			} else {
				nameArr = lastNameCellValue.split("\\ ");
				if (nameArr.length > 1) {
					cell.setCellValue(nameArr[0]); // firstname
					lastNameCell.setCellValue(StringUtils.substringAfter(lastNameCellValue, " ")); // lastname can have spaces
				} else {
					cell.setCellValue(StringUtils.EMPTY);
				}
			}
		}
	}

	private static void setCellValueFuelTypeFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) {
		String actualFuelType = oneCellValue.toString();
		if (actualFuelType.equalsIgnoreCase("ULSD") || actualFuelType.equalsIgnoreCase("S")) {
			cell.setCellValue("DSL");
		} else if (actualFuelType.equalsIgnoreCase("FUEL")) {
			cell.setCellValue("Regular");
		} else if (actualFuelType.equalsIgnoreCase("B")) {
			cell.setCellValue("DEF");
		} else {
			cell.setCellValue(actualFuelType);
		}
	}

	private static void setCellValueFeeFormat(Workbook wb, Cell cell, Object oneCellValue, String vendor) {
		String feeStr = oneCellValue.toString();
		if (StringUtils.isEmpty(feeStr)) {
			feeStr = "0.0";
		}
				
		if (StringUtils.startsWith(feeStr, "$") || StringUtils.startsWith(feeStr, "-")) {
			feeStr = StringUtils.substring(feeStr, 1);
		}
		
		cell.setCellValue(Double.parseDouble(feeStr));
		CellStyle style = wb.createCellStyle();
		style.setDataFormat(wb.createDataFormat().getFormat("$#,#0.00"));
		cell.setCellStyle(style);
	}

	private static void setCellValueDoubleFormat(Workbook wb, Cell cell, Object oneCellValue, String vendor) {
		cell.setCellValue(Double.parseDouble(oneCellValue.toString()));
		CellStyle style = wb.createCellStyle();
		style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
		cell.setCellStyle(style);
	}

	private static void setCellValueTimeFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) {
		String timeStr = oneCellValue.toString();
		if (vendor.equalsIgnoreCase(TOLL_COMPANY_EZ_PASS) || StringUtils.contains(vendor, TOLL_COMPANY_EZ_PASS)) {
			String [] timeArr = timeStr.split(":");
			String cellValueStr = (timeArr.length > 1 ? timeArr[0] + ":" + timeArr[1] : timeArr[0]);
			cell.setCellValue(cellValueStr);
		} else if (vendor.equalsIgnoreCase(TOLL_COMPANY_EZ_PASS)) {
			cell.setCellValue("00:00");
		} else if (StringUtils.contains(vendor, TOLL_COMPANY_EZ_PASS) || StringUtils.contains(vendor, TOLL_COMPANY_EZ_PASS)) { 
			cell.setCellValue(timeStr);
		}
	}

	private static void setCellValueDateFormat(Workbook wb, Cell cell, Object oneCellValue, String vendor) throws ParseException {
		System.out.println("Incoming vendor = " + vendor);
		String tollCompanyDateFormat = tollCompanyToDateFormatMapping.get(vendor);
		System.out.println("Value = " + tollCompanyDateFormat);
		
		if (oneCellValue instanceof Date) {
			System.out.println("Incoming date is a Date Object.");
			tollCompanyDateFormat = "EEE MMM dd hh:mm:ss z yyyy";
		}
		
		String dateStr = oneCellValue.toString();
		
		if (vendor.equalsIgnoreCase(TOLL_COMPANY_EZ_PASS) && cell.getColumnIndex() == 4) {
			 // Transaction Date, fill in value = InvoiceDate + 1Day
			Date invoiceDate = cell.getRow().getCell(2).getDateCellValue(); // invoiceDate
			System.out.println("Invoice date = " + invoiceDate);
			
			if (invoiceDate == null) {
				cell.setCellValue(StringUtils.EMPTY);
			} else {
				Calendar c = Calendar.getInstance(); 
				c.setTime(invoiceDate); 
				c.add(Calendar.DATE, 1);
				cell.setCellValue(c.getTime());
				System.out.println("Transaction date = " + c.getTime());
			}
			
		} else {
			if (StringUtils.isEmpty(dateStr)) {
				cell.setCellValue(StringUtils.EMPTY);
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
}
