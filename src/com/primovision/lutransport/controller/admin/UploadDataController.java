package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.FuelSurcharge;
import com.primovision.lutransport.model.FuelVendor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Trailer;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.service.ImportMainSheetService;

@Controller
@RequestMapping("/uploadData")
@SuppressWarnings("unchecked")
public class UploadDataController extends BaseController {
	
	@Autowired
	private ImportMainSheetService importMainSheetService;

	public void setImportMainSheetService(
			ImportMainSheetService importMainSheetService) {
		this.importMainSheetService = importMainSheetService;
	}

	@RequestMapping("/ticket.do")
	public String ticket(HttpServletRequest request,
			HttpServletResponse response) {
		return "admin/uploaddata/ticket";
	}

	@RequestMapping("/fuelSurcharge.do")
	public String fuelSurcharge(HttpServletRequest request,
			HttpServletResponse response) {

		return "admin/fuelSurcharge";
	}
	
	
	@RequestMapping("/fuellog.do")
	public String fuellog(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Map criterias = new HashMap();
		model.addAttribute("fuelvendor", genericDAO.findByCriteria(FuelVendor.class, criterias, "name", false));
		return "admin/uploaddata/fuellog";
	}
	
	@RequestMapping("/eztoll.do")
	public String eztoll(HttpServletRequest request,
			HttpServletResponse response) {
		return "admin/uploaddata/eztoll";
	}
	
	
	@RequestMapping("/vehiclepermit.do")
	public String vehiclepermit(HttpServletRequest request,
			HttpServletResponse response) {
		return "admin/uploaddata/vehiclepermit";
	}



	@RequestMapping("/vehiclepermit/upload.do")
	public String vehiclePermitSaveData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFile") MultipartFile file) throws Exception {
		
		List<String> str=new ArrayList<String>();
		InputStream is = file.getInputStream();
		str=importMainSheetService.importVehiclePermitMainSheet(is);
		if(str.isEmpty())
		{
				model.addAttribute("msg","Successfully uploaded all Vehicle Permits");
		}else{
				
				model.addAttribute("errorList", str);
		}	
		return "admin/uploaddata/vehiclepermit";
	}
	
	
	@RequestMapping("/eztoll/upload.do")
	public String eztollSaveData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFile") MultipartFile file) {
		
		List<String> str=new ArrayList<String>();
		boolean flag=false;
		try 
		{
			if (StringUtils.isEmpty(file.getOriginalFilename())) 
			{
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "admin/uploaddata/eztoll";
		    }
			if (!StringUtils.isEmpty(file.getOriginalFilename())) 
			{
				String ext=	file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if(!(ext.equalsIgnoreCase(".xls")))
                {
                	request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
                	return "admin/uploaddata/eztoll";
				}
			}
			InputStream is = file.getInputStream();
			str=importMainSheetService.importeztollMainSheet(is,flag);
			if(str.isEmpty())
			{
				model.addAttribute("msg","Successfully uploaded all tolls");
			}
			else
			{
				model.addAttribute("errorList", str);
			}
		} 
		catch (Exception ex) 
		{
			model.addAttribute("errors", "An error occurred while upload !!");
			ex.printStackTrace();
			log.warn("Unable to import :===>>>>>>>>>" + ex);
		}
		return "admin/uploaddata/eztoll";
	}
	
	
	
	
	@RequestMapping("/eztoll/override.do")
	public String eztollSaveDataOverride(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFile") MultipartFile file) {
		
		Boolean flag=false;
		List<String> str=new ArrayList<String>();
		try 
		{
			InputStream is = file.getInputStream();
			flag=true;
			str=importMainSheetService.importeztollMainSheet(is,flag);
			if(str.isEmpty())
			{
				model.addAttribute("msg","Successfully uploaded all tolls");
			}
			else
			{
				
				model.addAttribute("errorList", str);
			}
			
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
			log.warn("Unable to import :===>>>>>>>>>" + ex);
		}
		return "admin/uploaddata/eztollOverride";
	}
	
	
	
	
	
	@RequestMapping("/fuellog/override.do")
	public String fuellogSaveDataOverride(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFile") MultipartFile file) {
		
		Boolean flag=false;
		List<String> str=new ArrayList<String>();
		try 
		{
			InputStream is = file.getInputStream();
			flag=true;
			str=importMainSheetService.importfuellogMainSheet(is,flag);
			if(str.isEmpty())
			{
				model.addAttribute("msg","successfully uploaded all fuellogs");
			}
			else
			{
				
				model.addAttribute("errorList", str);
			}
			
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
			log.warn("Unable to import :===>>>>>>>>>" + ex);
		}
		return "admin/uploaddata/fuellogOverride";
	}
	
	
	@RequestMapping("/fuellog/upload.do")
	public String fuellogSaveData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFile") MultipartFile file,
			@RequestParam("fuelvendor") String fuelvendor) {
		
		List<String> str=new ArrayList<String>();
		boolean flag=false;
		try 
		{
			if (StringUtils.isEmpty(file.getOriginalFilename())) 
			{
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "admin/uploaddata/fuellog";
		    }
			if (!StringUtils.isEmpty(file.getOriginalFilename())) 
			{
				String ext=	file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if(!(ext.equalsIgnoreCase(".xls")))
                {
                	request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
                	return "admin/uploaddata/fuellog";
				}
			}
			
			InputStream is = file.getInputStream();
			
			// TODO: Convert data format
			is = convertToGenericFuelLogFormat(is, fuelvendor);
			
			str=importMainSheetService.importfuellogMainSheet(is,flag);
			System.out.println("\nimportMainSheetService.importMainSheet(is)\n");
			if(str.isEmpty())
			{
				model.addAttribute("msg","successfully uploaded all fuellogs");
			}
			else
			{
				
				model.addAttribute("errorList", str);
			}
		} 
		catch (Exception ex) 
		{
			model.addAttribute("errors", "An error occurred while upload !!");
			ex.printStackTrace();
			log.warn("Unable to import :===>>>>>>>>>" + ex);
		}
		return "admin/uploaddata/fuellog";
	}
	
	
	private InputStream convertToGenericFuelLogFormat(InputStream is, String vendor) throws Exception {
		
		LinkedList<String> expectedColumnList = getExpectedColumnList();
		LinkedHashMap<String, String> actualColumnListMap = getVendorSpecificColumnList(vendor);
		
		List<LinkedList<Object>> tempData = importMainSheetService.importVendorSpecificFuelLog(is, actualColumnListMap, vendor);
		System.out.println("Number of rows = " + tempData.size());
		HSSFWorkbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		
		CellStyle style = wb.createCellStyle();;

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
		
		int rowIndex = 1;
		
		for (int i = 0; i < tempData.size(); i++) {
			
			int columnIndex = 0;
			LinkedList<Object> oneRow = tempData.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			for (Object oneCellValue : oneRow) {
				
				Cell cell = row.createCell(columnIndex);
				sheet.setColumnWidth(columnIndex, 256*20);
				
				if (oneCellValue == null) {
					cell.setCellValue(StringUtils.EMPTY);
				} else {
					if (oneCellValue instanceof Double) {
						cell.setCellValue((Double)oneCellValue);
					} else if (columnIndex == 2 || columnIndex == 4) { //if (oneCellValue instanceof Date) {
						setCellValueDateFormat(wb, cell, oneCellValue);
					}  else if (columnIndex == 5) {
						setCellValueTimeFormat(cell, oneCellValue);
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
				columnIndex++;
			}
			
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//FileOutputStream fOut = new FileOutputStream("/Users/hemarajesh/Desktop/Test.xls");
		try {
			wb.write(out);
			//wb.write(fOut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
		// TODO: Convert to input stream before returning
	    InputStream targetStream = new ByteArrayInputStream(out.toByteArray());
		
	    return targetStream;
		//return is;
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

	private static CellStyle createBorderedStyle(Workbook wb){
	      CellStyle style = wb.createCellStyle();
	      style.setBorderRight(CellStyle.BORDER_THIN);
	      style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	      style.setBorderBottom(CellStyle.BORDER_THIN);
	      style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	      style.setBorderLeft(CellStyle.BORDER_THIN);
	      style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	      style.setBorderTop(CellStyle.BORDER_THIN);
	      style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	      return style;
	  }

	private LinkedHashMap<String, String> getVendorSpecificColumnList(String vendor) {
		
		//ist<LinkedHashMap<String, String>> vendorToFuelLogMapping = new ArrayList<LinkedHashMap<String, String>>();
 		
		LinkedHashMap<String, String> actualColumnMap = new LinkedHashMap<String, String>();
		actualColumnMap.put("INVOICED DATE", "Invoice date");
		actualColumnMap.put("INVOICE#", "Invoice");
		actualColumnMap.put("TRANSACTION DATE",  "TransactionPOSDate");
		actualColumnMap.put("TRANSACTION TIME",  "TransactionPOSTime");
		actualColumnMap.put("UNIT#", "Unit");
		actualColumnMap.put("DRIVER LAST NAME",  "DriverName");
		actualColumnMap.put("DRIVER FIRST NAME",  "DriverLastName");
		actualColumnMap.put("CARD NUMBER", "CardNumber"); // card Number
		actualColumnMap.put("FUEL TYPE",  "ProductCode");
		actualColumnMap.put("CITY", "LocationCity"); 
		actualColumnMap.put("STATE", "LocationState");
		actualColumnMap.put("Gallons", "Quantity");
		actualColumnMap.put("Unit Price", "PricePerUnit");
		actualColumnMap.put("Gross Amount", "TotalInvoice");
		actualColumnMap.put("fees", "TransactionFee");
		actualColumnMap.put("DISCOUNT", "TransactionDiscount");
		actualColumnMap.put("AMOUNT", "TransactionGross");
		
		return actualColumnMap;
	}

	private LinkedList<String> getExpectedColumnList() {
		LinkedList<String> expectedColumnList = new LinkedList<String>();
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
		
		return expectedColumnList;
	}

	@RequestMapping("/ticket/upload.do")
	public String ticketSaveData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFile") MultipartFile file) {
		
		List<String> str=new ArrayList<String>();
		try {
			if (file.getOriginalFilename() == ""
				|| !file.getOriginalFilename()
						.substring(
								file.getOriginalFilename().lastIndexOf("."))
						.equalsIgnoreCase(".xls")) {
			model.addAttribute("errors",
					"Please choose a file to upload !!");
			return "admin/uploaddata/ticket";
		}
			System.out.println("***** Here step 1");
			
			InputStream is = file.getInputStream();
			str=importMainSheetService.importMainSheet(is);
			if(str.isEmpty()){
				model.addAttribute("msg","successfully uploaded all tickets");
			}else{
				model.addAttribute("errorList", str);
			}
		} catch (Exception ex) {
			model.addAttribute("errors", "An error occurred while upload !!");
			ex.printStackTrace();
			log.warn("Unable to import :" + ex);
		}
		return "admin/uploaddata/ticket";
	}

	private Driver getDriverByName(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	private Location getLocationByName(String upperCase) {
		// TODO Auto-generated method stub
		return null;
	}

	private Trailer getTrailerByNo(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	private Vehicle getVehicleByUnit(String unit) {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping("/fuelSurcharge/upload.do")
	public String fuelSurchargeSaveData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFile") MultipartFile file) {

		try {
			if (file.getOriginalFilename() == ""
					|| !file.getOriginalFilename()
							.substring(
									file.getOriginalFilename().lastIndexOf("."))
							.equalsIgnoreCase(".xlsx")) {
				model.addAttribute("errors",
						"Please choose a file to upload !!");
				return "admin/fuelSurcharge";
			}
			// delete old data

			InputStream is = file.getInputStream();
			XSSFWorkbook wb = null;
			wb = new XSSFWorkbook(is);
			new StringBuffer();
			XSSFSheet sheet = wb.getSheetAt(0);
			FuelSurcharge fuelSurcharge = null;
			// int countRow = 0;
			for (Iterator rows = sheet.rowIterator(); rows.hasNext();) {
				// if (countRow == 0) {
				// rows.next();
				// countRow++;
				// continue;
				// }
				XSSFRow row = (XSSFRow) rows.next();
				short c1 = row.getFirstCellNum();
				short c2 = row.getLastCellNum();
				Object[] data = new Object[(c2 - c1)];
				for (int c = c1; c < c2; c++) {
					XSSFCell cell = row.getCell(c);

					if (cell.getCellType() == 1) {
						String str = getCellValue(cell).toString()
								.toLowerCase();
						if (str.contains("per trip")) {

							String fromPlace = row.getCell(1)
									.getStringCellValue();
							XSSFRow row1 = sheet.getRow((row.getRowNum() - 1));
							for (int i = row1.getFirstCellNum(); i < row1
									.getLastCellNum(); i++) {
								if (row1.getCell(i).getCellType() == 2) {
									fuelSurcharge = new FuelSurcharge();
									//fuelSurcharge.setFromPlace(fromPlace.toUpperCase());
									//fuelSurcharge.setToPlace(row1.getCell(i)
									//		.getStringCellValue().toUpperCase());
									fuelSurcharge.setFuelPrice((row.getCell(i)
											.getNumericCellValue()));
									fuelSurcharge.setCreatedAt(new Date());
									genericDAO.save(fuelSurcharge);
								}
							}

						}

					}
				}
			}
		} catch (Exception ex) {
			model.addAttribute("errors", "An error occurred while upload !!");
			ex.printStackTrace();
			log.warn("Unable to import :" + ex);
			return "admin/fuelSurcharge";
		}
		model.addAttribute("msg", "Data uploaded successfully !!");
		return "admin/fuelSurcharge";
	}

	private String getFormattedDate(Date validDate) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		return dateFormat.format(validDate);
	}

	private Object getCellValue(XSSFCell cell) {
		if (cell == null) {
			return null;
		}
		Object result = null;
		int cellType = cell.getCellType();
		switch (cellType) {
		case XSSFCell.CELL_TYPE_BLANK:
			result = "";
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			result = cell.getBooleanCellValue() ? Boolean.TRUE : Boolean.FALSE;
			break;
		case XSSFCell.CELL_TYPE_ERROR:
			result = "ERROR: " + cell.getErrorCellValue();
			break;
		case XSSFCell.CELL_TYPE_FORMULA:
			result = cell.getCellFormula();
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			XSSFCellStyle cellStyle = cell.getCellStyle();
			short dataFormat = cellStyle.getDataFormat();
			if (dataFormat == 14) {
				result = cell.getDateCellValue();
			} else {
				result = cell.getNumericCellValue();
			}
			break;
		case XSSFCell.CELL_TYPE_STRING:
			result = cell.getStringCellValue();
			break;
		default:
			break;
		}
		/*
		 * if (result instanceof Double) { return String.valueOf(((Double)
		 * result).longValue()); } if (result instanceof Date) { return result;
		 * }
		 */
		return result;
	}	
	
}
