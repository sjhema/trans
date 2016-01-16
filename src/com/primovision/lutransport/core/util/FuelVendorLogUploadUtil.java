package com.primovision.lutransport.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.primovision.lutransport.service.ImportMainSheetService;

public class FuelVendorLogUploadUtil {
	private static String VENDOR_TCH = "TCH";
	private static String VENDOR_DCFUELWB = "DC FUEL WB";
	private static String VENDOR_DCFUELLU = "DC FUEL LU";
	private static String VENDOR_QUARLES = "Quarles";
	private static String VENDOR_COMDATA_DREW = "COMDATA DREW";
	private static String VENDOR_COMDATA_LU = "COMDATA LU";
	private static String VENDOR_SUNOCO = "SUNOCO";
	private static String VENDOR_KW_RASTALL = "KW Rastall";
	private static String VENDOR_AC_T = "AC&T";
	private static String VENDOR_ATLANTIC_COAST = "Atlantic Coast";
	private static String VENDOR_QUICK_FUEL = "QUICK FUEL";
	
	static String expectedDateFormatStr = "MM/dd/yy";
	static SimpleDateFormat expectedDateFormat = new SimpleDateFormat(expectedDateFormatStr);
	
	static HashMap<String, LinkedHashMap<String, String>> vendorToFuelLogMapping = new HashMap<String, LinkedHashMap<String, String>>();
	static LinkedList<String> expectedColumnList = new LinkedList<String>();
	
	static HashMap<String, String> vendorToDateFormatMapping = new HashMap<String, String>();
	
	static {
		expectedColumnList.add("VENDOR"); // 0
		expectedColumnList.add("COMPANY"); // 1
		expectedColumnList.add("INVOICED DATE"); // 2
		expectedColumnList.add("INVOICE#"); // 3
		expectedColumnList.add("TRANSACTION DATE"); // 4
		expectedColumnList.add("TRANSACTION TIME"); // 5
		expectedColumnList.add("UNIT#"); // 6
		expectedColumnList.add("DRIVER LAST NAME"); // 7
		expectedColumnList.add("DRIVER FIRST NAME"); // 8
		expectedColumnList.add("CARD NUMBER"); // 9
		expectedColumnList.add("FUEL TYPE"); // 10
		expectedColumnList.add("CITY"); // 11
		expectedColumnList.add("STATE"); // 12
		expectedColumnList.add("Gallons"); // 13
		expectedColumnList.add("Unit Price"); // 14
		expectedColumnList.add("Gross Amount"); // 15
		expectedColumnList.add("fees"); // 16
		expectedColumnList.add("DISCOUNT"); // 17
		expectedColumnList.add("AMOUNT"); // 18

		vendorToDateFormatMapping.put(VENDOR_TCH, "yyyy-MM-dd");
		vendorToDateFormatMapping.put(VENDOR_COMDATA_LU, "MM/dd/yy");
		vendorToDateFormatMapping.put(VENDOR_COMDATA_DREW, "MM/dd/yy");
		vendorToDateFormatMapping.put(VENDOR_DCFUELLU, "MM/dd/yyyy");
		vendorToDateFormatMapping.put(VENDOR_DCFUELWB, "MM/dd/yyyy");
		vendorToDateFormatMapping.put(VENDOR_QUARLES, "MM/dd/yy");
		vendorToDateFormatMapping.put(VENDOR_SUNOCO, "MM/dd/yyyy");
		vendorToDateFormatMapping.put(VENDOR_KW_RASTALL, "MM/dd/yy");
		vendorToDateFormatMapping.put(VENDOR_AC_T, "MM/dd/yy");
		vendorToDateFormatMapping.put(VENDOR_ATLANTIC_COAST, "MM/dd/yy");
		vendorToDateFormatMapping.put(VENDOR_QUICK_FUEL, "MM/dd/yyyy");
		
		mapForTCH(expectedColumnList);
		mapForDCFuelWB(expectedColumnList);
		mapForQuarles(expectedColumnList);
		mapForComData(expectedColumnList);
		mapForSunoco(expectedColumnList);
		mapForKWRastall(expectedColumnList);
		mapForACAndT(expectedColumnList);
		mapForAtlanticCoast(expectedColumnList);
		mapForQuickFuel(expectedColumnList);
	}

	public static boolean isConversionRequired(Long fuelvendor) {
		long fuelVendorId = fuelvendor.longValue();
		if (fuelVendorId == 11l // TCH
				|| fuelVendorId == 12l || fuelVendorId == 13l // DC
				// Quarles
				|| fuelVendorId == 3l || fuelVendorId == 15l || fuelVendorId == 9l
				|| fuelVendorId == 7l || fuelVendorId == 6l
				// Comdata
				|| fuelVendorId == 4l || fuelVendorId == 10l
				// Sunoco
				|| fuelVendorId == 17l || fuelVendorId == 1l || fuelVendorId == 5l
				// KW Rastall
				|| fuelVendorId == 19l
				// AC & T
				|| fuelVendorId == 2l
				// Atlantic Coast
				|| fuelVendorId == 18l
				// Quick Fuel
				|| fuelVendorId == 16l || fuelVendorId == 20l) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean validateTotalFees(Long fuelvendor, BigDecimal totalFees) {
		long fuelVendorId = fuelvendor.longValue();
		if (fuelVendorId == 18l // Atlantic Coast
				// Quick Fuel
				|| fuelVendorId == 16l || fuelVendorId == 20l) {
			if (totalFees == null) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	
	private static void mapForTCH(LinkedList<String> expectedColumnList) {
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
		vendorToFuelLogMapping.put(VENDOR_TCH, actualColumnMap);
	}
	
	private static void mapForQuickFuel(LinkedList<String> expectedColumnList) {
		LinkedHashMap<String, String> actualColumnMap = new LinkedHashMap<String, String>();
		int expectedColumnStartIndex = 2;
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice Date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice #");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Vehicle");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Driver Name");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Fuel Type");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Gallons");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Price/Gal");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Ext. Price");
		// Total fees is entered in UI, divide it by no. of records
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "fees");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		// Net amount = Gross cost + Fees
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex), "Ext. Price");
		
		vendorToFuelLogMapping.put(VENDOR_QUICK_FUEL, actualColumnMap);
	}
	
	private static void mapForAtlanticCoast(LinkedList<String> expectedColumnList) {
		LinkedHashMap<String, String> actualColumnMap = new LinkedHashMap<String, String>();
		int expectedColumnStartIndex = 2;
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice Date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice #");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Delivery");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Asset");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Driver Name");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Item");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Quantity");
		// Unit price = Price + Taxes
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Price");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Amount");
		// Total fees is entered in UI, divide it by no. of records
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "fees");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		// Net amount = Gross cost + Fees
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex), "Amount");
		
		actualColumnMap.put("Taxes", "Taxes");
		
		vendorToFuelLogMapping.put(VENDOR_ATLANTIC_COAST, actualColumnMap);
	}
	
	private static void mapForKWRastall(LinkedList<String> expectedColumnList) {
		LinkedHashMap<String, String> actualColumnMap = new LinkedHashMap<String, String>();
		int expectedColumnStartIndex = 2;
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice Date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice #");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Vehicle");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Driver Name");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Gallons");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Price per gallon");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Total");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex), "Total");
		vendorToFuelLogMapping.put(VENDOR_KW_RASTALL, actualColumnMap);
	}
	
	private static void mapForACAndT(LinkedList<String> expectedColumnList) {
		LinkedHashMap<String, String> actualColumnMap = new LinkedHashMap<String, String>();
		int expectedColumnStartIndex = 2;
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice Date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice #");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Unit No");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Driver");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Card"); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Description");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Quantity");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "P P G");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Amount");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex), "Amount");
		vendorToFuelLogMapping.put(VENDOR_AC_T, actualColumnMap);
	}
	
	private static void mapForDCFuelWB(LinkedList<String> expectedColumnList2) {
		LinkedHashMap<String, String> actualColumnMap = new LinkedHashMap<String, String>();
		int expectedColumnStartIndex = 2;
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice Date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice #");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Unit #");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Driver Name");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Fuel Type");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "State");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Gallons");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Unit Price");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Total");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex), "Total");
		vendorToFuelLogMapping.put(VENDOR_DCFUELWB, actualColumnMap);
	}
	
	private static void mapForQuarles(LinkedList<String> expectedColumnList) {
		LinkedHashMap<String, String> actualColumnMap = new LinkedHashMap<String, String>();
		int expectedColumnStartIndex = 2;
		
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice Date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice #");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Tran Date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Time");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Unit #");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Card Desc");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Card No"); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Product");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Site  Desc"); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "ST");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Units");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Unit Price");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Total Price");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex), "Total Price");
		vendorToFuelLogMapping.put(VENDOR_QUARLES, actualColumnMap);
	}
	
	private static void mapForSunoco(LinkedList<String> expectedColumnList) {
		LinkedHashMap<String, String> actualColumnMap = new LinkedHashMap<String, String>();
		int expectedColumnStartIndex = 2;
	
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice Date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice #");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Transaction Date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Transaction Time");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Unit #");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Driver Last Name");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Driver First Name");
		// Account Number + last 5 digits
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Card Number"); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Product");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Merchant City"); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Merchant State / Province");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Units");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Unit Cost");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Total Fuel Cost");
		// Service Cost + Other Cost + Total Non-Fuel Cost = Fees
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Service Cost");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Discount");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex), "Net Cost");
		
		actualColumnMap.put("Account Number", "Account Number");
		actualColumnMap.put("Other Cost", "Other Cost");
		actualColumnMap.put("Total Non-Fuel Cost", "Total Non-Fuel Cost");
		
		vendorToFuelLogMapping.put(VENDOR_SUNOCO, actualColumnMap);
	}
	
	private static void mapForComData(LinkedList<String> expectedColumnList) {
		LinkedHashMap<String, String> actualColumnMap = new LinkedHashMap<String, String>();
		int expectedColumnStartIndex = 2;
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice #");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Transaction Date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Transaction Time");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Unit Number");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Driver Name");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "DriverLastName");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Comchek Card Number"); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  "Service Used");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Truck Stop City"); 
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Truck Stop State");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Number of Tractor Gallons");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Tractor Fuel Price Per Gallon");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Cost of Tractor Fuel");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Fees for Fuel & Oil & Products");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Rebate Amount");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex), "Total Amount Due Comdata");
		vendorToFuelLogMapping.put(VENDOR_COMDATA_DREW, actualColumnMap);
	}

	public static InputStream convertToGenericFuelLogFormat(InputStream is, Long vendor, GenericDAO genericDAO, ImportMainSheetService importMainSheetService, HashMap<String, Object> dataFromUser) throws Exception {
		String vendorName = getVendorName(genericDAO, vendor);
		LinkedHashMap<String, String> actualColumnListMap = getVendorSpecificMapping(vendorName);
		
		HashMap<String, Object> additionalVendorData = setupAdditionalVendorData(dataFromUser, vendorName);
			
		List<LinkedList<Object>> tempData = importMainSheetService.importVendorSpecificFuelLog(is, actualColumnListMap, vendor, additionalVendorData);
		System.out.println("Number of rows = " + tempData.size());
		
		// add the total number or row data in additionalVendorData for any vendor to use the information
		additionalVendorData.put("dataSetSize", tempData.size());
		
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
				System.out.println("Creating Column @ " + columnIndex + " with value (original) = " + oneCellValue);
				Cell cell = createExcelCell(sheet, row, columnIndex);
				oneCellValue = consolidateDataForVendors(wb, cell, oneRow, oneCellValue, vendorName, additionalVendorData);
				System.out.println("Creating Column @ " + columnIndex + " with value (manipulated) = " + oneCellValue);
				formatCellValueForVendor(wb, cell, oneCellValue, vendorName);
				columnIndex++;
			}
		}

		InputStream targetStream = createInputStream(wb);
	    return targetStream;
		//return is;
	}

	private static HashMap<String, Object> setupAdditionalVendorData(HashMap<String, Object> dataFromUser, String vendorName) {
		HashMap<String, Object> additionalVendorData = new HashMap<String, Object>();
		// For ATLANTIC_COAST & QUICK_FUEL only, we need to process fees as part of incoming data, for the rest, remove it
		if (StringUtils.containsIgnoreCase(vendorName, VENDOR_ATLANTIC_COAST) 
				|| StringUtils.containsIgnoreCase(vendorName, VENDOR_QUICK_FUEL)) {
				additionalVendorData.put("fees", dataFromUser.get("fees"));
		}
		return additionalVendorData;
	}

	private static Object consolidateDataForVendors(HSSFWorkbook wb, Cell cell, LinkedList<Object> oneRow, Object oneCellValue, String vendor, HashMap<String, Object> additionalVendorData) {
		if (StringUtils.contains(vendor, VENDOR_SUNOCO)) {
			if (cell.getColumnIndex() == 9) { // Card Number
				String cardNumber = oneRow.get(9) == null ? StringUtils.EMPTY : oneRow.get(9).toString();
				cardNumber = cardNumber.length() > 5 ? cardNumber.substring(cardNumber.length()-5, cardNumber.length()) : cardNumber;
				
				int accountNumberIndex = expectedColumnList.size();
				String accountNumber = oneRow.get(accountNumberIndex) == null ? StringUtils.EMPTY : oneRow.get(accountNumberIndex).toString();
				
				return accountNumber + cardNumber;
			} else if (cell.getColumnIndex() == 16) { // Fees
				// Service Cost + Other Cost + Total Non-Fuel Cost = Fees
				String serviceCost = oneRow.get(16) == null ? "0.0" : oneRow.get(16).toString();
				
				int otherCostIndex = expectedColumnList.size() + 1;
				String otherCost = oneRow.get(otherCostIndex) == null ? "0.0" : oneRow.get(otherCostIndex).toString();
				
				int nonFuelCostIndex = otherCostIndex + 1;
				String nonFuelCost = oneRow.get(nonFuelCostIndex) == null ? "0.0" : oneRow.get(nonFuelCostIndex).toString();
				
				BigDecimal fees = new BigDecimal(serviceCost).add(new BigDecimal(otherCost)).add(new BigDecimal(nonFuelCost));
				return fees.toPlainString();
			} else {
				return oneCellValue;
			}
		} else if (StringUtils.contains(vendor, VENDOR_ATLANTIC_COAST) || StringUtils.containsIgnoreCase(vendor, VENDOR_QUICK_FUEL)) {
			if (cell.getColumnIndex() == 14) { // Unit Price
				if (StringUtils.containsIgnoreCase(vendor, VENDOR_QUICK_FUEL)) {
					// no op
					return oneCellValue;
				}
				
				// Unit Price = Price + Taxes
				String price = oneRow.get(14) == null ? "0.0" : oneRow.get(14).toString();
				
				int taxesIndex = expectedColumnList.size();
				String taxes = oneRow.get(taxesIndex) == null ? "0.0" : oneRow.get(taxesIndex).toString();
				
				BigDecimal unitPrice = new BigDecimal(price).add(new BigDecimal(taxes));
				return unitPrice.toPlainString();
			} else if (cell.getColumnIndex() == 15) { // Amount
				
				String amount = oneRow.get(15) == null ? "0.0" : oneRow.get(15).toString();
				String totalFees = oneRow.get(16) == null ? "0.0" : oneRow.get(16).toString();
				System.out.println("Total Fees = " + totalFees);
				
				String totalNumOfTransactions = additionalVendorData.get("dataSetSize").toString();
				System.out.println("Total number of transactions = " + totalNumOfTransactions);
				
				MathContext mc = new MathContext(9, RoundingMode.HALF_EVEN);      
				BigDecimal feesPerTransaction = (new BigDecimal(totalFees)).divide(new BigDecimal(totalNumOfTransactions), mc).setScale(6, RoundingMode.CEILING);
				BigDecimal amountWithFees = new BigDecimal(amount).add(feesPerTransaction);
				
				System.out.println("Amount = " + amount);
				System.out.println("Fees per transaction = " + feesPerTransaction);
				System.out.println("Amount with fees = " + amountWithFees);
				
				oneRow.set(16, feesPerTransaction.toPlainString());
				oneRow.set(18, amountWithFees);
				return amount;
			} else {
				return oneCellValue;
			}
		} else {
			return oneCellValue;
		}
	}

	private static LinkedHashMap<String, String> getVendorSpecificMapping(String vendorName) {
		String commonVendorName = getCommonVendorName(vendorName);
		return vendorToFuelLogMapping.get(commonVendorName);
	}

	private static String getCommonVendorName(String vendorName) {
		if (vendorName.equalsIgnoreCase(VENDOR_DCFUELLU)) {
			vendorName = VENDOR_DCFUELWB;
		} else if (vendorName.equalsIgnoreCase(VENDOR_COMDATA_LU)) {
			vendorName = VENDOR_COMDATA_DREW;
		} else if (StringUtils.contains(vendorName, VENDOR_QUARLES)) {
			vendorName = VENDOR_QUARLES;
		} else if (StringUtils.contains(vendorName, VENDOR_SUNOCO)) {
			vendorName = VENDOR_SUNOCO;
		} else if (StringUtils.contains(vendorName, VENDOR_KW_RASTALL)) {
			vendorName = VENDOR_KW_RASTALL;
		} else if (StringUtils.contains(vendorName, VENDOR_AC_T)) {
			vendorName = VENDOR_AC_T;
		} else if (StringUtils.contains(vendorName, VENDOR_ATLANTIC_COAST)) {
			vendorName = VENDOR_ATLANTIC_COAST;
		} else if (StringUtils.containsIgnoreCase(vendorName, VENDOR_QUICK_FUEL)) {
			vendorName = VENDOR_QUICK_FUEL;
		}
		return vendorName;
	}

	private static String getVendorName(GenericDAO genericDAO, Long vendor) {
		Map criterias = new HashMap();
		criterias.put("id", vendor);
		FuelVendor fuelVendor = genericDAO.findByCriteria(FuelVendor.class, criterias, "name", false).get(0);
		return fuelVendor.getName();
	}

	private static void formatCellValueForVendor(HSSFWorkbook wb,  Cell cell, Object oneCellValue, String vendor)
			throws Exception {
		if (vendor.equalsIgnoreCase(VENDOR_TCH)) { 
			formatCellValueForTCH(wb, cell, oneCellValue, vendor);
		} else if (vendor.equalsIgnoreCase(VENDOR_DCFUELWB) || vendor.equalsIgnoreCase(VENDOR_DCFUELLU)) { 
			formatCellValueForDCFuelWB(wb, cell, oneCellValue, VENDOR_DCFUELWB);
		} else if (StringUtils.contains(vendor, VENDOR_QUARLES)) { 
			formatCellValueForQuarles(wb, cell, oneCellValue, VENDOR_QUARLES);
		} else if (StringUtils.equalsIgnoreCase(vendor, VENDOR_COMDATA_DREW) || StringUtils.equalsIgnoreCase(vendor, VENDOR_COMDATA_LU) ) { 
			formatCellValueForComData(wb, cell, oneCellValue, VENDOR_COMDATA_DREW);
		} else if (StringUtils.contains(vendor, VENDOR_SUNOCO)) { 
			formatCellValueForSunoco(wb, cell, oneCellValue, VENDOR_SUNOCO);
		} else if (StringUtils.contains(vendor, VENDOR_KW_RASTALL)) { 
			formatCellValueForKWRastall(wb, cell, oneCellValue, VENDOR_KW_RASTALL);
		} else if (StringUtils.contains(vendor, VENDOR_AC_T)) { 
			formatCellValueForACAndT(wb, cell, oneCellValue, VENDOR_AC_T);
		} else if (StringUtils.contains(vendor, VENDOR_ATLANTIC_COAST)) { 
			formatCellValueForAtlanticCoast(wb, cell, oneCellValue, VENDOR_ATLANTIC_COAST);
		} else if (StringUtils.containsIgnoreCase(vendor, VENDOR_QUICK_FUEL)) { 
			formatCellValueForQuickFuel(wb, cell, oneCellValue, VENDOR_QUICK_FUEL);
		}
	}

	private static void formatCellValueForComData(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws ParseException {
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		} 
		
		int columnIndex = cell.getColumnIndex();
		if (oneCellValue instanceof Date || columnIndex == 2 || columnIndex == 4) { 
			setCellValueDateFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 3) {
			setCellValueInvoiceNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 6) {
			setCellValueUnitNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 5) { // transaction time 
			setCellValueTimeFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 7 || columnIndex == 8) {
			setCellValueDriverFormat(wb, cell, oneCellValue);
		} else if (columnIndex == 10) {
			setCellValueFuelTypeFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 13) { // gallons
			setCellValueDoubleFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex > 13 && columnIndex < 19) { // Fee
			setCellValueFeeFormat(wb, cell, oneCellValue, vendor);
		} else {
			cell.setCellValue(oneCellValue.toString().toUpperCase());
		}
		
	}
	
	private static void formatCellValueForKWRastall(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws ParseException {
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		} 
		
		int columnIndex = cell.getColumnIndex();
		if (oneCellValue instanceof Date || columnIndex == 2 || columnIndex == 4) { 
			setCellValueDateFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 3) {
			setCellValueInvoiceNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 6) {
			setCellValueUnitNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 5) { // transaction time 
			setCellValueTimeFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 7 || columnIndex == 8) {
			setCellValueDriverFormat(wb, cell, oneCellValue);
		} else if (columnIndex == 9) { // cardnumber 
			setCellValueFuelCardFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 10) {
			setCellValueFuelTypeFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 12) {
			cell.setCellValue("NJ");
		} else if (columnIndex == 13) { // gallons
			setCellValueDoubleFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex > 13 && columnIndex < 19) { // Fee
			setCellValueFeeFormat(wb, cell, oneCellValue, vendor);
		} else {
			cell.setCellValue(oneCellValue.toString().toUpperCase());
		}
	}
	
	private static void formatCellValueForAtlanticCoast(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws ParseException {
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		} 
		
		int columnIndex = cell.getColumnIndex();
		if (oneCellValue instanceof Date || columnIndex == 2 || columnIndex == 4) { 
			setCellValueDateFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 3) {
			setCellValueInvoiceNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 6) {
			setCellValueUnitNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 5) { // transaction time 
			setCellValueTimeFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 7 || columnIndex == 8) {
			setCellValueDriverFormat(wb, cell, oneCellValue);
		} else if (columnIndex == 9) { // cardnumber 
			setCellValueFuelCardFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 10) {
			setCellValueFuelTypeFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 12) {
			cell.setCellValue("NY");
		} else if (columnIndex == 13) { // gallons
			setCellValueDoubleFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex > 13 && columnIndex < 19) { // Fee
			setCellValueFeeFormat(wb, cell, oneCellValue, vendor);
		} else {
			cell.setCellValue(oneCellValue.toString().toUpperCase());
		}
	}
	
	private static void formatCellValueForQuickFuel(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws ParseException {
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		} 
		
		int columnIndex = cell.getColumnIndex();
		if (oneCellValue instanceof Date || columnIndex == 2 || columnIndex == 4) { 
			setCellValueDateFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 3) {
			setCellValueInvoiceNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 6) {
			setCellValueUnitNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 5) { // transaction time 
			setCellValueTimeFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 7 || columnIndex == 8) {
			setCellValueDriverFormat(wb, cell, oneCellValue);
		} else if (columnIndex == 9) { // cardnumber 
			setCellValueFuelCardFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 10) {
			setCellValueFuelTypeFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 11) {
			cell.setCellValue("Tampa");
		} else if (columnIndex == 12) {
			cell.setCellValue("FL");
		} else if (columnIndex == 13) { // gallons
			setCellValueDoubleFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex > 13 && columnIndex < 19) { // Fee
			setCellValueFeeFormat(wb, cell, oneCellValue, vendor);
		} else {
			cell.setCellValue(oneCellValue.toString().toUpperCase());
		}
	}
	
	private static void formatCellValueForACAndT(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws ParseException {
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		} 
		
		int columnIndex = cell.getColumnIndex();
		if (oneCellValue instanceof Date || columnIndex == 2 || columnIndex == 4) { 
			setCellValueDateFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 3) {
			setCellValueInvoiceNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 6) {
			setCellValueUnitNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 5) { // transaction time 
			setCellValueTimeFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 9) { // cardnumber 
			setCellValueFuelCardFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 7 || columnIndex == 8) {
			setCellValueDriverFormat(wb, cell, oneCellValue);
		} else if (columnIndex == 10) {
			setCellValueFuelTypeFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 12) {
			cell.setCellValue("MD");
		} else if (columnIndex == 13) { // gallons
			setCellValueDoubleFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex > 13 && columnIndex < 19) { // Fee
			setCellValueFeeFormat(wb, cell, oneCellValue, vendor);
		} else {
			cell.setCellValue(oneCellValue.toString().toUpperCase());
		}
	}

	private static void formatCellValueForDCFuelWB(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws ParseException {
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		} 
		
		int columnIndex = cell.getColumnIndex();
		if (oneCellValue instanceof Date || columnIndex == 2 || columnIndex == 4) { // Invoice Date, Transaction Date
			setCellValueDateFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 3) {
			setCellValueInvoiceNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 6) {
			setCellValueUnitNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 5) { // transaction time 
			setCellValueTimeFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 7 || columnIndex == 8) {
			setCellValueDriverFormat(wb, cell, oneCellValue);
		} else if (columnIndex == 9) { // cardnumber 
			setCellValueFuelCardFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 13) {  // else if (oneCellValue instanceof Double || columnIndex == 13) { // gallons
			setCellValueDoubleFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex > 13 && columnIndex < 19) { // Fee
			setCellValueFeeFormat(wb, cell, oneCellValue, vendor);
		} else {
			cell.setCellValue(oneCellValue.toString().toUpperCase());
		}
	}
	
	private static void formatCellValueForQuarles(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws ParseException {
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		}
		
		int columnIndex = cell.getColumnIndex();
		if (oneCellValue instanceof Date || columnIndex == 2 || columnIndex == 4) { // Invoice Date, Transaction Date
			setCellValueDateFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 3) {
			setCellValueInvoiceNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 6) {
			setCellValueUnitNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 5) { // transaction time 
			setCellValueTimeFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 7 || columnIndex == 8) {
			setCellValueDriverFormat(wb, cell, oneCellValue);
		} else if (columnIndex == 9) { // cardnumber 
			setCellValueFuelCardFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 10) {
			setCellValueFuelTypeFormat(wb, cell, oneCellValue, vendor);
		} else if (oneCellValue instanceof Double || columnIndex == 13) { // gallons
			setCellValueDoubleFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex > 13 && columnIndex < 19) { // Fee
			setCellValueFeeFormat(wb, cell, oneCellValue, vendor);
		} else {
			cell.setCellValue(oneCellValue.toString().toUpperCase());
		}
	}
	
	private static void formatCellValueForSunoco(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws ParseException {
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		}
		
		int columnIndex = cell.getColumnIndex();
		
		if (oneCellValue instanceof Date || columnIndex == 2 || columnIndex == 4) { // Invoice Date, Transaction Date
			setCellValueDateFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 3) {
			setCellValueInvoiceNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 6) {
			setCellValueUnitNumberFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 5) { // transaction time 
			setCellValueTimeFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 7 || columnIndex == 8) {
			setCellValueDriverFormat(wb, cell, oneCellValue);
		} else if (columnIndex == 9) { // cardnumber 
			setCellValueFuelCardFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex == 10) {
			setCellValueFuelTypeFormat(wb, cell, oneCellValue, vendor);
		} else if (oneCellValue instanceof Double || columnIndex == 13) { // gallons
			setCellValueDoubleFormat(wb, cell, oneCellValue, vendor);
		} else if (columnIndex > 13 && columnIndex < 19) { // Fee
			setCellValueFeeFormat(wb, cell, oneCellValue, vendor);
		} else {
			cell.setCellValue(oneCellValue.toString().toUpperCase());
		}
	}

	private static void formatCellValueForTCH(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws Exception {
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
			setCellValueFuelCardFormat(wb, cell, oneCellValue, vendor);
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
	
	private static void setCellValueFuelCardFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) {
		String cardNumber = oneCellValue.toString();
		if (vendor.equalsIgnoreCase(VENDOR_DCFUELWB) || StringUtils.contains(vendor, VENDOR_KW_RASTALL) 
				|| StringUtils.contains(vendor, VENDOR_ATLANTIC_COAST) || StringUtils.containsIgnoreCase(vendor, VENDOR_QUICK_FUEL)) {
			if (StringUtils.isEmpty(cardNumber)) {
				cell.setCellValue("EXCLUDE_ERROR_CHECK");
			} else {
				cell.setCellValue(cardNumber);
			}
		} else if (vendor.equalsIgnoreCase(VENDOR_TCH)) {
			System.out.println("Incoming card number = " + cardNumber);
			cardNumber = cardNumber.length() > 5 ? cardNumber.substring(cardNumber.length()-5, cardNumber.length()) : cardNumber;
			System.out.println("TCH Formatted card number = " + cardNumber);
			cell.setCellValue(cardNumber);
		} else if (StringUtils.contains(vendor, VENDOR_QUARLES)) { 
			setIntegerValue(cell, oneCellValue);
		} else if (StringUtils.contains(vendor, VENDOR_SUNOCO) || StringUtils.contains(vendor, VENDOR_AC_T)) { 
			cell.setCellValue(cardNumber);
		}
	}
	
	private static void setCellValueDriverFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue) {
		int columnIndex = cell.getColumnIndex();
		String driverName = oneCellValue.toString();
		
		if (columnIndex == 7) { // lastname
			if (StringUtils.isEmpty(driverName)) {
				cell.setCellValue(StringUtils.EMPTY);
				return;
			}
			
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
			if (StringUtils.isEmpty(lastNameCellValue)) {
				cell.setCellValue(StringUtils.EMPTY);
				return;
			}
			
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
		if (StringUtils.contains(vendor, VENDOR_KW_RASTALL)) {
			cell.setCellValue("DSL");
			return;
		}
		
		if (oneCellValue == null || StringUtils.isEmpty(StringUtils.trimToEmpty(oneCellValue.toString()))) {
			cell.setCellValue(StringUtils.EMPTY);
		}
		
		String actualFuelType = oneCellValue.toString().trim();
		if (actualFuelType.equalsIgnoreCase("ULSD") || actualFuelType.equalsIgnoreCase("S") 
				|| actualFuelType.equalsIgnoreCase("Ultra Low Su") || actualFuelType.equalsIgnoreCase("ULSD CLEAR")
				|| actualFuelType.equalsIgnoreCase("2 - #2 ULSD CLEAR")) {
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
		if (vendor.equalsIgnoreCase(VENDOR_TCH) || StringUtils.contains(vendor, VENDOR_SUNOCO)) {
			String [] timeArr = timeStr.split(":");
			String cellValueStr = (timeArr.length > 1 ? timeArr[0] + ":" + timeArr[1] : timeArr[0]);
			cell.setCellValue(cellValueStr);
		} else if (vendor.equalsIgnoreCase(VENDOR_DCFUELWB) || StringUtils.contains(vendor, VENDOR_KW_RASTALL)
				|| StringUtils.contains(vendor, VENDOR_AC_T) || StringUtils.contains(vendor, VENDOR_ATLANTIC_COAST) || StringUtils.containsIgnoreCase(vendor, VENDOR_QUICK_FUEL)) {
			cell.setCellValue("00:00");
		} else if (StringUtils.contains(vendor, VENDOR_QUARLES) || StringUtils.contains(vendor, VENDOR_COMDATA_DREW)) { 
			cell.setCellValue(timeStr);
		}
	}
	
	private static void setCellValueUnitNumberFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) {
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		} 
		
		String cellValueStr = StringUtils.trimToEmpty(oneCellValue.toString());
		if (StringUtils.isEmpty(cellValueStr)) {
			cell.setCellValue(StringUtils.EMPTY);
			return;
		}
		
		if (StringUtils.contains(vendor, VENDOR_ATLANTIC_COAST)) {
			System.out.println("Original unit #: " + cellValueStr);
			cellValueStr = StringUtils.replace(cellValueStr, "LUTY ", StringUtils.EMPTY);
			System.out.println("Modified unit #: " + cellValueStr);
		}
		
		setIntegerValue(cell, cellValueStr);
	}

	private static void setIntegerValue(Cell cell, Object oneCellValue) {
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
	}

	private static void setCellValueInvoiceNumberFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue,
			String vendor) {
		setIntegerValue(cell, oneCellValue);
	}

	private static void setCellValueDateFormat(Workbook wb, Cell cell, Object oneCellValue, String vendor) throws ParseException {
		System.out.println("Incoming vendor = " + vendor);
		String vendorDateFormat = vendorToDateFormatMapping.get(vendor);
		System.out.println("Vendor Date format = " + vendorDateFormat);
		
		if (oneCellValue instanceof Date) {
			System.out.println("Incoming date is a Date Object, changing vendor datae format");
			vendorDateFormat = "EEE MMM dd hh:mm:ss z yyyy";
		}
		
		String dateStr = oneCellValue.toString();
		if (cell.getColumnIndex() == 4 && vendor.equalsIgnoreCase(VENDOR_DCFUELWB)) {
			 // Transaction Date, fill in value = InvoiceDate + 1 Day
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
		} else if (cell.getColumnIndex() == 4 && 
				(StringUtils.contains(vendor, VENDOR_KW_RASTALL) || StringUtils.contains(vendor, VENDOR_ATLANTIC_COAST) || StringUtils.containsIgnoreCase(vendor, VENDOR_QUICK_FUEL))) {
			 // Transaction Date, fill in value = Transaction Date + 1 Day
			if (StringUtils.isEmpty(dateStr)) {
				cell.setCellValue(StringUtils.EMPTY);
			} else {
				Date specifiedTxnDate = convertToExpectedDateFormat(dateStr, vendorDateFormat);
				Calendar c = Calendar.getInstance(); 
				c.setTime(specifiedTxnDate); 
				c.add(Calendar.DATE, 1);
				cell.setCellValue(c.getTime());
				System.out.println("Transaction date = " + c.getTime());
			}
		} else {
			if (StringUtils.isEmpty(dateStr)) {
				cell.setCellValue(StringUtils.EMPTY);
			} else {
				cell.setCellValue(convertToExpectedDateFormat(dateStr, vendorDateFormat));
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
	
	private static Cell createExcelCell(Sheet sheet, Row row, int columnIndex) {
		Cell cell = row.createCell(columnIndex);
		sheet.setColumnWidth(columnIndex, 256*20);
		return cell;
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
}
