package com.primovision.lutransport.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
	
	GenericDAO genericDAO;
	private static String VENDOR_TCH = "TCH";
	private static String VENDOR_DCFUELWB = "DC FUEL WB";
	private static String VENDOR_DCFUELLU = "DC FUEL LU";
	
	SimpleDateFormat expectedDateFormat = new SimpleDateFormat("MM/dd/yy");
	
	static HashMap<String, LinkedHashMap<String, String>> vendorToFuelLogMapping = new HashMap<String, LinkedHashMap<String, String>>();
	static LinkedList<String> expectedColumnList = new LinkedList<String>();
	
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

		// TCH
		mapForTCH(expectedColumnList);
		mapForDCFuelWB(expectedColumnList);

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
	
	private static void mapForDCFuelWB(LinkedList<String> expectedColumnList2) {
		LinkedHashMap<String, String> actualColumnMap = new LinkedHashMap<String, String>();
		int expectedColumnStartIndex = 2;
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice Date");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Invoice #");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++), "Unit #");
		actualColumnMap.put(expectedColumnList.get(expectedColumnStartIndex++),  StringUtils.EMPTY);
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

	public InputStream convertToGenericFuelLogFormat(InputStream is, Long vendor, GenericDAO genericDAO, ImportMainSheetService importMainSheetService) throws Exception {
		
		this.genericDAO = genericDAO;
		
		String vendorName = getVendorName(genericDAO, vendor);
		LinkedHashMap<String, String> actualColumnListMap = getVendorSpecificMapping(vendorName);
		
		List<LinkedList<Object>> tempData = importMainSheetService.importVendorSpecificFuelLog(is, actualColumnListMap, vendor);
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
				System.out.println("Creating Column @ " + columnIndex + " with value = " + oneCellValue);
				Cell cell = createExcelCell(sheet, row, columnIndex);
				formatCellValueForVendor(wb, cell, oneCellValue, vendorName);
				columnIndex++;
			}
		}

		InputStream targetStream = createInputStream(wb);
	    return targetStream;
		//return is;
	}

	private LinkedHashMap<String, String> getVendorSpecificMapping(String vendorName) {
		if (vendorName.equalsIgnoreCase(VENDOR_DCFUELLU)) {
			vendorName = VENDOR_DCFUELWB;
		}
		
		return vendorToFuelLogMapping.get(vendorName);
	}

	private String getVendorName(GenericDAO genericDAO, Long vendor) {
		Map criterias = new HashMap();
		criterias.put("id", vendor);
		FuelVendor fuelVendor = genericDAO.findByCriteria(FuelVendor.class, criterias, "name", false).get(0);
		String vendorName = fuelVendor.getName();
		return vendorName;
	}

	private InputStream createInputStream(HSSFWorkbook wb) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FileOutputStream fOut;
		/*try {
			fOut = new FileOutputStream("/Users/hemarajesh/Desktop/Test.xls");
			wb.write(fOut);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		try {
			wb.write(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	    InputStream targetStream = new ByteArrayInputStream(out.toByteArray());
		return targetStream;
	}

	private void formatCellValueForVendor(HSSFWorkbook wb,  Cell cell, Object oneCellValue, String vendor)
			throws Exception {
		
		if (vendor.equalsIgnoreCase(VENDOR_TCH)) { // TCH
			formatCellValueForTCH(wb, cell, oneCellValue, vendor);
		} else if (vendor.equalsIgnoreCase(VENDOR_DCFUELWB) || vendor.equalsIgnoreCase(VENDOR_DCFUELLU)) { 
			formatCellValueForDCFuelWB(wb, cell, oneCellValue, VENDOR_DCFUELWB);
		}
		
	}

	private void formatCellValueForDCFuelWB(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws ParseException {
		
		int columnIndex = cell.getColumnIndex();
		
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
		} else {
			if (oneCellValue instanceof Date || columnIndex == 2 || columnIndex == 4) { // Invoice Date, Transaction Date
				setCellValueDateFormat(wb, cell, oneCellValue, vendor);
			} else if (columnIndex == 5) { // transaction time 
				setCellValueTimeFormat(wb, cell, oneCellValue, vendor);
			} else if (columnIndex == 9) { // cardnumber 
				setCellValueFuelCardFormat(wb, cell, oneCellValue, vendor);
			} else if (oneCellValue instanceof Double || columnIndex == 13) { // gallons
				setCellValueDoubleFormat(wb, cell, oneCellValue, vendor);
			} else if (columnIndex > 13 && columnIndex < 19) { // Fee
				setCellValueFeeFormat(wb, cell, oneCellValue, vendor);
			} else {
				cell.setCellValue(oneCellValue.toString().toUpperCase());
			}
		}
		
	}

	private void formatCellValueForTCH(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) throws Exception {
		
		int columnIndex = cell.getColumnIndex();
		
			if (oneCellValue == null) {
				oneCellValue = StringUtils.EMPTY;
			} 
			
			if (oneCellValue instanceof Date || columnIndex == 2 || columnIndex == 4) { 
				setCellValueDateFormat(wb, cell, oneCellValue, vendor);
			} else if (columnIndex == 5) {
				setCellValueTimeFormat(wb, cell, oneCellValue, vendor);
			} else if (columnIndex == 7 || columnIndex == 8) {
				setCellValueDriverFormat(wb, cell, oneCellValue);
			} else if (columnIndex == 9) {
				setCellValueFuelCardFormat(wb, cell, oneCellValue, vendor);
			} else if (columnIndex == 10) {
				setCellValueFuelTypeFormat(wb, cell, oneCellValue);
			} else if (oneCellValue instanceof Double || columnIndex == 13) { // gallons
				setCellValueDoubleFormat(wb, cell, oneCellValue, vendor);
			} else if (columnIndex > 13 && columnIndex < 19) { // fee
				setCellValueFeeFormat(wb, cell, oneCellValue, vendor);
			} else {
				cell.setCellValue(oneCellValue.toString().toUpperCase());
			}
		
	}

	private void createColumnHeaders(LinkedList<String> expectedColumnList, Sheet sheet, CellStyle style) {
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

	private void setCellValueFuelCardFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) {
		
		if(vendor.equalsIgnoreCase(VENDOR_DCFUELWB)) {
			if (StringUtils.isEmpty(oneCellValue.toString())) {
				cell.setCellValue("EXCLUDE_ERROR_CHECK");
			} else {
				cell.setCellValue(oneCellValue.toString());
			}
		} else if (vendor.equalsIgnoreCase(VENDOR_TCH)) {
			String cardNumber = oneCellValue.toString();
			System.out.println("Incoming card number = " + cardNumber);
			cardNumber = cardNumber.length() > 5 ? cardNumber.substring(cardNumber.length()-5, cardNumber.length()) : cardNumber;
			System.out.println("TCH Formatted card number = " + cardNumber);
			cell.setCellValue(cardNumber);
		}
	}

	private Cell createExcelCell(Sheet sheet, Row row, int columnIndex) {
		Cell cell = row.createCell(columnIndex);
		sheet.setColumnWidth(columnIndex, 256*20);
		return cell;
	}
	
	private void setCellValueDriverFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue) {
		int columnIndex = cell.getColumnIndex();
		String driverName = oneCellValue.toString();
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		if (columnIndex == 7) { // lastname
			// put the value as is, validation can be done after getting the firstname @ columnIndex=8
			cell.setCellValue(driverName.toUpperCase());
			
		} else if (columnIndex == 8) {
			
			Cell lastNameCell = cell.getRow().getCell(cell.getColumnIndex()-1);
			
			// split into firstname and lastname
			String [] nameArr = lastNameCell.getStringCellValue().split("\\ ");
			if (nameArr.length > 1) {
				lastNameCell.setCellValue(nameArr[1]);
				cell.setCellValue(nameArr[0]);
			} else {
				cell.setCellValue(StringUtils.EMPTY);
			}
			
			// Logic for resetting the driver name to Empty if lastname / firstname not found in DB
			// firstname 
			/*criterias.put("firstName", driverName);
			Driver fname = genericDAO.getByCriteria(Driver.class, criterias);
			if (fname == null) {
				// no driver with this firstname is found, blank out the name
				System.out.println("Driver not found, setting it to EMPTY");
				
				cell.setCellValue(StringUtils.EMPTY); // firstname = EMPTY
				lastNameCell.setCellValue(StringUtils.EMPTY); // lastname = EMPTY
				
			} else { // first name is valid, check the lastname
				criterias.clear();
				criterias.put("lastName", lastNameCell.getStringCellValue());
				Driver lname = genericDAO.getByCriteria(Driver.class, criterias);
				if (lname == null) {
					// no driver with this lastname is found, blank out the name
					System.out.println("Driver not found, setting it to EMPTY");

					cell.setCellValue(StringUtils.EMPTY);
					lastNameCell.setCellValue(StringUtils.EMPTY); // lastname = EMPTY
				} else {
					cell.setCellValue(driverName.toUpperCase());
				}
			} */
		}
	}

	private void setCellValueFuelTypeFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue) {
		String actualFuelType = oneCellValue.toString().toUpperCase();
		if (actualFuelType.equals("ULSD")) {
			cell.setCellValue("DSL");
		} else if (actualFuelType.equals("FUEL")) {
			cell.setCellValue("Regular");
		} else {
			cell.setCellValue(oneCellValue.toString());
		}
	}

	private void setCellValueFeeFormat(Workbook wb, Cell cell, Object oneCellValue, String vendor) {
		if (StringUtils.isEmpty(oneCellValue.toString())) {
			oneCellValue = "0";
		}
		cell.setCellValue(Double.parseDouble(oneCellValue.toString()));
		CellStyle style = wb.createCellStyle();
		style.setDataFormat(wb.createDataFormat().getFormat("$#,#0.00"));
		cell.setCellStyle(style);
	}

	private void setCellValueDoubleFormat(Workbook wb, Cell cell, Object oneCellValue, String vendor) {
		
		cell.setCellValue(Double.parseDouble(oneCellValue.toString()));
		CellStyle style = wb.createCellStyle();
		style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
		cell.setCellStyle(style);
	}

	private void setCellValueTimeFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue, String vendor) {
		
		if (vendor.equalsIgnoreCase(VENDOR_TCH)) {
			String [] timeArr = oneCellValue.toString().split(":");
			String cellValueStr = (timeArr.length > 1 ? timeArr[0] + ":" + timeArr[1] : timeArr[0]);
			cell.setCellValue(cellValueStr);
		} else if (vendor.equalsIgnoreCase(VENDOR_DCFUELWB)) {
			cell.setCellValue("00:00");
		}
	}

	private void setCellValueDateFormat(Workbook wb, Cell cell, Object oneCellValue, String vendor) throws ParseException {
		
		if (vendor.equalsIgnoreCase(VENDOR_TCH)) {
			Date actualDateFormat = new SimpleDateFormat("yyyy-MM-dd").parse(oneCellValue.toString());
			String expectedDateFormatStr = expectedDateFormat.format(actualDateFormat);
			cell.setCellValue(expectedDateFormat.parse(expectedDateFormatStr));
	
			CellStyle style = wb.createCellStyle();
			style.setDataFormat(wb.createDataFormat().getFormat("MM/dd/yy"));
			cell.setCellStyle(style);
		} else if (vendor.equalsIgnoreCase(VENDOR_DCFUELWB)) {
			
			if (cell.getColumnIndex() == 4) { // Transaction Date, fill in value = InvoiceDate + 1Day
				Date invoiceDate = cell.getRow().getCell(2).getDateCellValue(); // invoiceDate
				System.out.println("Invoice date = " + invoiceDate);
				
				Calendar c = Calendar.getInstance(); 
				c.setTime(invoiceDate); 
				c.add(Calendar.DATE, 1);
				cell.setCellValue(c.getTime());
				
				System.out.println("Transaction date = " + c.getTime());
			} else {
				
				System.out.println("Date = " + oneCellValue.toString());
				Date actualDateFormat = new SimpleDateFormat("dd/MM/yy").parse(oneCellValue.toString());
				String expectedDateFormatStr = expectedDateFormat.format(actualDateFormat);
				cell.setCellValue(expectedDateFormat.parse(expectedDateFormatStr));
			}
	
			CellStyle style = wb.createCellStyle();
			style.setDataFormat(wb.createDataFormat().getFormat("MM/dd/yy"));
			cell.setCellStyle(style);
		}
	}

}
