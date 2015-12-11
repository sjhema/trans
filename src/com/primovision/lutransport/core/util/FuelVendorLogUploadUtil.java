package com.primovision.lutransport.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.FuelVendor;
import com.primovision.lutransport.service.ImportMainSheetService;

public class FuelVendorLogUploadUtil {
	
	GenericDAO genericDAO;
	
	static HashMap<String, LinkedHashMap<String, String>> vendorToFuelLogMapping = new HashMap<String, LinkedHashMap<String, String>>();
	static LinkedList<String> expectedColumnList = new LinkedList<String>();
	
	static {
		
		expectedColumnList.add("VENDOR");
		expectedColumnList.add("COMPANY");
		expectedColumnList.add("INVOICED DATE");
		expectedColumnList.add("INVOICE#");
		expectedColumnList.add("TRANSACTION DATE");
		expectedColumnList.add("TRANSACTION TIME");
		expectedColumnList.add("UNIT#");
		expectedColumnList.add("DRIVER LAST NAME");
		expectedColumnList.add("DRIVER FIRST NAME");
		expectedColumnList.add("CARD NUMBER");
		expectedColumnList.add("FUEL TYPE");
		expectedColumnList.add("CITY");
		expectedColumnList.add("STATE");
		expectedColumnList.add("Gallons");
		expectedColumnList.add("Unit Price");
		expectedColumnList.add("Gross Amount");
		expectedColumnList.add("fees");
		expectedColumnList.add("DISCOUNT");
		expectedColumnList.add("AMOUNT");

		// TCH
		mapForTCH(expectedColumnList);

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
		vendorToFuelLogMapping.put("TCH", actualColumnMap);
	}
	
	public InputStream convertToGenericFuelLogFormat(InputStream is, Long vendor, GenericDAO genericDAO, ImportMainSheetService importMainSheetService) throws Exception {
		
		this.genericDAO = genericDAO;
		
		String vendorName = getVendorName(genericDAO, vendor);
		LinkedHashMap<String, String> actualColumnListMap = vendorToFuelLogMapping.get(vendorName);
		
		List<LinkedList<Object>> tempData = importMainSheetService.importVendorSpecificFuelLog(is, actualColumnListMap, vendor);
		System.out.println("Number of rows = " + tempData.size());
		
		HSSFWorkbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		CellStyle style = wb.createCellStyle();

		createColumnHeaders(expectedColumnList, sheet, style);
		
		int rowIndex = 1;
		
		for (int i = 0; i < tempData.size(); i++) {
			
			System.out.println("Creating Row " + i);
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

	private String getVendorName(GenericDAO genericDAO, Long vendor) {
		Map criterias = new HashMap();
		criterias.put("id", vendor);
		FuelVendor fuelVendor = genericDAO.findByCriteria(FuelVendor.class, criterias, "name", false).get(0);
		String vendorName = fuelVendor.getName();
		return vendorName;
	}

	private InputStream createInputStream(HSSFWorkbook wb) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//FileOutputStream fOut = new FileOutputStream("/Users/hemarajesh/Desktop/Test.xls");
		try {
			wb.write(out);
			//wb.write(fOut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	    InputStream targetStream = new ByteArrayInputStream(out.toByteArray());
		return targetStream;
	}

	private void formatCellValueForVendor(HSSFWorkbook wb,  Cell cell, Object oneCellValue, String vendor)
			throws Exception {
		
		if (vendor.equalsIgnoreCase("TCH")) { // TCH
			formatCellValueForTCH(wb, cell, oneCellValue);
		}
		
	}

	private void formatCellValueForTCH(HSSFWorkbook wb, Cell cell, Object oneCellValue) throws Exception {
		
		int columnIndex = cell.getColumnIndex();
		
		if (oneCellValue == null) {
			cell.setCellValue(StringUtils.EMPTY);
		} else {
			if (oneCellValue instanceof Double) {
				cell.setCellValue((Double)oneCellValue);
			} else if (columnIndex == 2 || columnIndex == 4) { 
				setCellValueDateFormat(wb, cell, oneCellValue);
			} else if (columnIndex == 5) {
				setCellValueTimeFormat(cell, oneCellValue);
			} /*else if (columnIndex == 7 || columnIndex == 8) {
				setCellValueDriverFormat(wb, cell, oneCellValue);
			}*/ else if (columnIndex == 9) {
				setCellValueCardNumberFormat(wb, cell, oneCellValue);
			} else if (columnIndex == 10) {
				setCellValueFuelTypeFormat(wb, cell, oneCellValue);
			} else if (columnIndex == 13) {
				setCellValueDoubleFormat(wb, cell, oneCellValue);
			} else if (columnIndex > 13 && columnIndex < 19) {
				setCellValueFeeFormat(wb, cell, oneCellValue);
			} else if (oneCellValue instanceof Boolean) {
				cell.setCellValue((Boolean)oneCellValue);
			} else if (oneCellValue instanceof Byte) {
				cell.setCellValue((Byte)oneCellValue);
			} else {
				cell.setCellValue(oneCellValue.toString().toUpperCase());
			}
		}
		
	}

	private void createColumnHeaders(LinkedList<String> expectedColumnList, Sheet sheet, CellStyle style) {
		Row headerRow = sheet.createRow(0);
		int columnHeaderIndex = 0;
		for (String columnHeader : expectedColumnList) { // TODO redundant, use actualColumnListMap.keys instead
			sheet.setColumnWidth(columnHeaderIndex, 256*20);
			Cell cell = headerRow.createCell(columnHeaderIndex++);
			System.out.println("Setting cell value " + columnHeader);
			cell.setCellValue(columnHeader);
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(style);
		}
	}

	private void setCellValueCardNumberFormat(HSSFWorkbook wb, Cell cell, Object oneCellValue) {
		String cardNumber = oneCellValue.toString();
		System.out.println("Incoming card number = " + cardNumber);
		cardNumber = cardNumber.length() > 5 ? cardNumber.substring(cardNumber.length()-5, cardNumber.length()) : cardNumber;
		System.out.println("TCH Formatted card number = " + cardNumber);
		cell.setCellValue(cardNumber);
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
			// firstname 
			criterias.put("firstName", driverName);
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
			}
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

	private void setCellValueFeeFormat(Workbook wb, Cell cell, Object oneCellValue) {
		cell.setCellValue(Double.parseDouble(oneCellValue.toString()));
		CellStyle style = wb.createCellStyle();
		style.setDataFormat(wb.createDataFormat().getFormat("$#,#0.00"));
		cell.setCellStyle(style);
	}

	private void setCellValueDoubleFormat(Workbook wb, Cell cell, Object oneCellValue) {
		
		cell.setCellValue(Double.parseDouble(oneCellValue.toString()));
		CellStyle style = wb.createCellStyle();
		style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
		cell.setCellStyle(style);
	}

	private void setCellValueTimeFormat(Cell cell, Object oneCellValue) {
		
		String [] timeArr = oneCellValue.toString().split(":");
		String cellValueStr = (timeArr.length > 1 ? timeArr[0] + ":" + timeArr[1] : timeArr[0]);
		cell.setCellValue(cellValueStr);
	}

	private void setCellValueDateFormat(Workbook wb, Cell cell, Object oneCellValue) throws ParseException {
		System.out.println("Date = " + oneCellValue.toString());
		Date actualDateFormat = new SimpleDateFormat("yyyy-MM-dd").parse(oneCellValue.toString());
		String expectedDateFormatStr = new SimpleDateFormat("MM/dd/yy").format(actualDateFormat);
		cell.setCellValue(new SimpleDateFormat("MM/dd/yy").parse(expectedDateFormatStr));

		CellStyle style = wb.createCellStyle();
		style.setDataFormat(wb.createDataFormat().getFormat("MM/dd/yy"));
		cell.setCellStyle(style);
	}

}
