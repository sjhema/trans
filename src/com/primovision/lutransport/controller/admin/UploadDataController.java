package com.primovision.lutransport.controller.admin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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
import com.primovision.lutransport.core.configuration.Configuration;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.FuelSurcharge;
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
	public String fuellog(HttpServletRequest request,
			HttpServletResponse response) {
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
			@RequestParam("dataFile") MultipartFile file) {
		
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
