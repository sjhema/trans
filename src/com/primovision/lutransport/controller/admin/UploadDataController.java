package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.util.FuelVendorLogUploadUtil;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.TicketUtils;
import com.primovision.lutransport.core.util.TollCompanyTagUploadUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.FuelSurcharge;
import com.primovision.lutransport.model.FuelVendor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.TollCompany;
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
	public String eztoll(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Map criterias = new HashMap();
		model.addAttribute("tollcompanies", genericDAO.findByCriteria(TollCompany.class, criterias, "name",false));
		return "admin/uploaddata/eztoll";
	}
	
	@RequestMapping("/mileagelog.do")
	public String mileage(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		return "admin/uploaddata/mileage";
	}
	
	@RequestMapping("/accident.do")
	public String accident(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		return "admin/uploaddata/accident";
	}
	
	@RequestMapping("/injury.do")
	public String injury(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		return "admin/uploaddata/injury";
	}
	
	@RequestMapping("/subcontractorrate.do")
	public String subcontractorrate(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		return "admin/uploaddata/subcontractorrate";
	}
	
	@RequestMapping("/employee.do")
	public String employee(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		return "admin/uploaddata/employee";
	}
	
	@RequestMapping("/wmInvoice.do")
	public String wmInvoice(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		return "admin/uploaddata/wmInvoice";
	}
	
	@RequestMapping("/wmTicket.do")
	public String wmTicket(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		setupWMTicketUpload(model);
		return "admin/uploaddata/wmTicket";
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
	public void eztollSaveData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFile") MultipartFile file,
			@RequestParam("tollcompany") Long tollCompanyId) {
		// Toll upload improvement - 23rd Jul 2016
		//Map criterias = new HashMap();
		//model.addAttribute("tollcompanies", genericDAO.findByCriteria(TollCompany.class, criterias, "name", false));
	
		ByteArrayOutputStream out = null;
		InputStream dataFileInputStream1 = null;
		InputStream dataFileInputStream2 = null;
		InputStream convertedDataFileInputStream = null;
		try {
			// Toll upload improvement - 23rd Jul 2016
			/*
			if (StringUtils.isEmpty(file.getOriginalFilename())) {
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "admin/uploaddata/eztoll";
		   }
			if (!StringUtils.isEmpty(file.getOriginalFilename()))  {
				String ext=	file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if(!(ext.equalsIgnoreCase(".xls"))) {
                request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
                return "admin/uploaddata/eztoll";
				}
			}*/
			
			dataFileInputStream1 = file.getInputStream();
			if (TollCompanyTagUploadUtil.isConversionRequired(tollCompanyId)) {
				convertedDataFileInputStream = TollCompanyTagUploadUtil.convertToGenericTollTagFormat(dataFileInputStream1, tollCompanyId, this.genericDAO, this.importMainSheetService);
			}
			closeInputStream(dataFileInputStream1);
			
			System.out.println("\nimportMainSheetService.importeztollMainSheet(is)\n");
			List<String> errorList = importMainSheetService.importeztollMainSheet(convertedDataFileInputStream, false);
			closeInputStream(convertedDataFileInputStream);
			
			// Toll upload improvement - 23rd Jul 2016
			/*
			if(str.isEmpty()) {
				model.addAttribute("msg", "Successfully uploaded all tolls");
			} else {
				model.addAttribute("errorList", str);
			}*/
			
			// Toll upload improvement - 23rd Jul 2016
			dataFileInputStream2 = file.getInputStream();
			out = TollCompanyTagUploadUtil.createTollUploadResponse(dataFileInputStream2, errorList);
			closeInputStream(dataFileInputStream2);
		} catch (Exception ex) {
			log.warn("Unable to import :===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			//str.add("Exception while uploading");
			//model.addAttribute("errorList", str);
			
			// Toll upload improvement - 23rd Jul 2016
			/*
			model.addAttribute("error", "An error occurred while uploading!!!");
			return "admin/uploaddata/eztoll";
			*/
			
			// Toll upload improvement - 23rd Jul 2016
			out = TollCompanyTagUploadUtil.createTollUploadExceptionResponse(ex);
		} finally {
			closeInputStream(dataFileInputStream1);
			closeInputStream(dataFileInputStream2);
			closeInputStream(convertedDataFileInputStream);
		}
		
		// Toll upload improvement - 23rd Jul 2016
		//return "admin/uploaddata/eztoll";
		
		String responseFileName = "TollUploadResults_" + StringUtils.replace(file.getOriginalFilename(), " ", "_");
		String type = file.getContentType();
		response.setHeader("Content-Disposition", "attachment;filename="+ responseFileName);
		response.setContentType(type);
	
		try {
			out.writeTo(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeOutputStream(out);
		}
	}
	
	private void closeInputStream(InputStream is) {
		if (is == null) {
			return;
		}
		
		try {
			is.close();
			is = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void closeOutputStream(OutputStream os) {
		if (os == null) {
			return;
		}
		
		try {
			os.close();
			os = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	@RequestMapping("/accident/uploadMain.do")
	public String importAccidentMainData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFileMain") MultipartFile file) {
		model.addAttribute("errorList", new ArrayList<String>());
		//model.addAttribute("error", StringUtils.EMPTY);
		//request.getSession().setAttribute("error", StringUtils.EMPTY);
		
		try {
			if (StringUtils.isEmpty(file.getOriginalFilename())) {
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "admin/uploaddata/accident";
		   }
			
			String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			if (!(ext.equalsIgnoreCase(".xls"))) {
          	request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
          	return "admin/uploaddata/accident";
			}
			
			InputStream is = file.getInputStream();
			Long createdBy = getUser(request).getId();
			System.out.println("\nimportMainSheetService.importAccidentMainData(is)\n");
			List<String> errorList = importMainSheetService.importAccidentMainData(is, createdBy);
			if (errorList.isEmpty()) {
				model.addAttribute("msg", "Successfully uploaded all Accident Main data");
			} else {
				model.addAttribute("errorList", errorList);
			}
		} catch (Exception ex) {
			log.warn("Unable to import Accident main data:===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			//str.add("Exception while uploading");
			//model.addAttribute("errorList", str);
			
			model.addAttribute("error", "An error occurred while uploading Accident main data!!");
		}
		
		return "admin/uploaddata/accident";
	}
	
	@RequestMapping("/accident/uploadReported.do")
	public String importAccidentReportedData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFileReported") MultipartFile file) {
		model.addAttribute("errorList", new ArrayList<String>());
		//model.addAttribute("error", StringUtils.EMPTY);
		//request.getSession().setAttribute("error", StringUtils.EMPTY);
		
		try {
			if (StringUtils.isEmpty(file.getOriginalFilename())) {
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "admin/uploaddata/accident";
		   }
			
			String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			if (!(ext.equalsIgnoreCase(".xls"))) {
          	request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
          	return "admin/uploaddata/accident";
			}
			
			InputStream is = file.getInputStream();
			Long createdBy = getUser(request).getId();
			System.out.println("\nimportMainSheetService.importAccidentReportedData(is)\n");
			List<String> errorList = importMainSheetService.importAccidentReportedData(is, createdBy);
			if (errorList.isEmpty()) {
				model.addAttribute("msg", "Successfully uploaded all Accident Reported data");
			} else {
				model.addAttribute("errorList", errorList);
			}
		} catch (Exception ex) {
			log.warn("Unable to import Accident reported data:===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			//str.add("Exception while uploading");
			//model.addAttribute("errorList", str);
			
			model.addAttribute("error", "An error occurred while uploading Accident reported data!!");
		}
		
		return "admin/uploaddata/accident";
	}
	
	@RequestMapping("/accident/uploadNotReported.do")
	public String importAccidentNotReportedData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFileNotReported") MultipartFile file) {
		model.addAttribute("errorList", new ArrayList<String>());
		//model.addAttribute("error", StringUtils.EMPTY);
		//request.getSession().setAttribute("error", StringUtils.EMPTY);
		
		try {
			if (StringUtils.isEmpty(file.getOriginalFilename())) {
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "admin/uploaddata/accident";
		   }
			
			String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			if (!(ext.equalsIgnoreCase(".xls"))) {
          	request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
          	return "admin/uploaddata/accident";
			}
			
			InputStream is = file.getInputStream();
			Long createdBy = getUser(request).getId();
			System.out.println("\nimportMainSheetService.importAccidentNotReportedData(is)\n");
			List<String> errorList = importMainSheetService.importAccidentNotReportedData(is, createdBy);
			if (errorList.isEmpty()) {
				model.addAttribute("msg", "Successfully uploaded all Accident Not Reported data");
			} else {
				model.addAttribute("errorList", errorList);
			}
		} catch (Exception ex) {
			log.warn("Unable to import Accident not reported data:===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			//str.add("Exception while uploading");
			//model.addAttribute("errorList", str);
			
			model.addAttribute("error", "An error occurred while uploading Accident not reported data!!");
		}
		
		return "admin/uploaddata/accident";
	}
	
	@RequestMapping("/injury/uploadMain.do")
	public String importInjuryMainData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFileMain") MultipartFile file) {
		model.addAttribute("errorList", new ArrayList<String>());
		//model.addAttribute("error", StringUtils.EMPTY);
		//request.getSession().setAttribute("error", StringUtils.EMPTY);
		
		try {
			if (StringUtils.isEmpty(file.getOriginalFilename())) {
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "admin/uploaddata/injury";
		   }
			
			String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			if (!(ext.equalsIgnoreCase(".xls"))) {
          	request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
          	return "admin/uploaddata/injury";
			}
			
			InputStream is = file.getInputStream();
			Long createdBy = getUser(request).getId();
			System.out.println("\nimportMainSheetService.importInjuryMainData(is)\n");
			List<String> errorList = importMainSheetService.importInjuryMainData(is, createdBy);
			if (errorList.isEmpty()) {
				model.addAttribute("msg", "Successfully uploaded all Injury Main data");
			} else {
				model.addAttribute("errorList", errorList);
			}
		} catch (Exception ex) {
			log.warn("Unable to import Injury main data:===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			//str.add("Exception while uploading");
			//model.addAttribute("errorList", str);
			
			model.addAttribute("error", "An error occurred while uploading Injury main data!!");
		}
		
		return "admin/uploaddata/injury";
	}
	
	@RequestMapping("/injury/uploadReported.do")
	public String importInjuryReportedData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFileReported") MultipartFile file) {
		model.addAttribute("errorList", new ArrayList<String>());
		//model.addAttribute("error", StringUtils.EMPTY);
		//request.getSession().setAttribute("error", StringUtils.EMPTY);
		
		try {
			if (StringUtils.isEmpty(file.getOriginalFilename())) {
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "admin/uploaddata/injury";
		   }
			
			String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			if (!(ext.equalsIgnoreCase(".xls"))) {
          	request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
          	return "admin/uploaddata/injury";
			}
			
			InputStream is = file.getInputStream();
			Long createdBy = getUser(request).getId();
			System.out.println("\nimportMainSheetService.importInjuryReportedData(is)\n");
			List<String> errorList = importMainSheetService.importInjuryReportedData(is, createdBy);
			if (errorList.isEmpty()) {
				model.addAttribute("msg", "Successfully uploaded all Injury Reported data");
			} else {
				model.addAttribute("errorList", errorList);
			}
		} catch (Exception ex) {
			log.warn("Unable to import injury reported data:===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			//str.add("Exception while uploading");
			//model.addAttribute("errorList", str);
			
			model.addAttribute("error", "An error occurred while uploading Injury reported data!!");
		}
		
		return "admin/uploaddata/injury";
	}
	
	@RequestMapping("/injury/uploadNotReported.do")
	public String importInjuryNotReportedData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFileNotReported") MultipartFile file) {
		model.addAttribute("errorList", new ArrayList<String>());
		//model.addAttribute("error", StringUtils.EMPTY);
		//request.getSession().setAttribute("error", StringUtils.EMPTY);
		
		try {
			if (StringUtils.isEmpty(file.getOriginalFilename())) {
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "admin/uploaddata/injury";
		   }
			
			String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			if (!(ext.equalsIgnoreCase(".xls"))) {
          	request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
          	return "admin/uploaddata/injury";
			}
			
			InputStream is = file.getInputStream();
			Long createdBy = getUser(request).getId();
			System.out.println("\nimportMainSheetService.importInjuryNotReportedData(is)\n");
			List<String> errorList = importMainSheetService.importInjuryNotReportedData(is, createdBy);
			if (errorList.isEmpty()) {
				model.addAttribute("msg", "Successfully uploaded all Injury Not Reported data");
			} else {
				model.addAttribute("errorList", errorList);
			}
		} catch (Exception ex) {
			log.warn("Unable to import injury not reported data:===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			//str.add("Exception while uploading");
			//model.addAttribute("errorList", str);
			
			model.addAttribute("error", "An error occurred while uploading Injury not reported data!!");
		}
		
		return "admin/uploaddata/injury";
	}
	
	@RequestMapping("/mileagelog/upload.do")
	public String saveMileageData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("period") String periodStr,
			@RequestParam("resetMiles") Double resetMiles,
			@RequestParam("dataFile") MultipartFile file) {
		try {
			if (StringUtils.isEmpty(file.getOriginalFilename())) {
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "admin/uploaddata/mileage";
		   }
			
			String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			if (!(ext.equalsIgnoreCase(".xls"))) {
          	request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
          	return "admin/uploaddata/mileage";
			}
			
			SimpleDateFormat mileageSearchDateFormat = new SimpleDateFormat("MMMMM yyy");
			Date period = mileageSearchDateFormat.parse(periodStr);
			
			InputStream is = file.getInputStream();
			Long createdBy = getUser(request).getId();
			System.out.println("\nimportMainSheetService.importMileageLogMainSheet(is)\n");
			List<String> str = importMainSheetService.importMileageLogMainSheet(is, period, resetMiles, createdBy);
			if (str.isEmpty()) {
				model.addAttribute("msg", "Successfully uploaded all mileage records");
			} else {
				model.addAttribute("errorList", str);
			}
		} catch (Exception ex) {
			log.warn("Unable to import :===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			//str.add("Exception while uploading");
			//model.addAttribute("errorList", str);
			
			model.addAttribute("error", "An error occurred while uploading!!");
			return "admin/uploaddata/mileage";
		}
		
		return "admin/uploaddata/mileage";
	}
	
	@RequestMapping("/subcontractorrate/upload.do")
	public String saveSubcontractorRateData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("validFrom") String validFromStr,
			@RequestParam("validTo") String validToStr,
			@RequestParam("dataFile") MultipartFile file) {
		try {
			if (StringUtils.isEmpty(file.getOriginalFilename())) {
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "admin/uploaddata/subcontractorrate";
		   }
			
			String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			if (!(ext.equalsIgnoreCase(".xls"))) {
          	request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
          	return "admin/uploaddata/subcontractorrate";
			}
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			Date validFrom = dateFormat.parse(validFromStr);
			Date validTo = dateFormat.parse(validToStr);
			
			InputStream is = file.getInputStream();
			Long createdBy = getUser(request).getId();
			System.out.println("\nimportMainSheetService.importMileageLogMainSheet(is)\n");
			List<String> str = importMainSheetService.importSubcontractorRateMainSheet(is, validFrom, validTo, createdBy);
			if (str.isEmpty()) {
				model.addAttribute("msg", "Successfully uploaded all subcontractor rate records");
			} else {
				model.addAttribute("errorList", str);
			}
		} catch (Exception ex) {
			log.warn("Unable to import :===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			//str.add("Exception while uploading");
			//model.addAttribute("errorList", str);
			
			model.addAttribute("error", "An error occurred while uploading!!");
			return "admin/uploaddata/subcontractorrate";
		}
		
		return "admin/uploaddata/subcontractorrate";
	}
	
	@RequestMapping("/wmTicket/upload.do")
	public String saveWMTicket(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("locationType") String locationType,
			@RequestParam(required = false, value = "destinationLocation") String destinationLocation,
			@RequestParam("dataFile") MultipartFile file) {
		setupWMTicketUpload(model);
		
		model.addAttribute("errorList", new ArrayList<String>());
		//model.addAttribute("error", StringUtils.EMPTY);
		//request.getSession().setAttribute("error", StringUtils.EMPTY);
		
		try {
			if (StringUtils.isEmpty(locationType)) {
			    request.getSession().setAttribute("error", "Please choose location type");
			    return "admin/uploaddata/wmTicket";
		   }
			if (StringUtils.isEmpty(file.getOriginalFilename())) {
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "admin/uploaddata/wmTicket";
		   }
			
			String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			if (!(ext.equalsIgnoreCase(".xls"))) {
          	request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
          	return "admin/uploaddata/wmTicket";
			}
			
			InputStream is = file.getInputStream();
			Long createdBy = getUser(request).getId();
			System.out.println("\nimportMainSheetService.importWMTicket(is)\n");
			List<String> errorList = importMainSheetService.importWMTickets(is, locationType, destinationLocation, createdBy);
			if (errorList.isEmpty()) {
				model.addAttribute("msg", "Successfully uploaded all WM tickets");
			} else {
				model.addAttribute("errorList", errorList);
			}
		} catch (Exception ex) {
			log.warn("Unable to import :===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			//str.add("Exception while uploading");
			//model.addAttribute("errorList", str);
			
			model.addAttribute("error", "An error occurred while uploading!!");
		}
		
		return "admin/uploaddata/wmTicket";
	}
	
	@RequestMapping("/wmInvoice/upload.do")
	public String saveWMInvoice(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFile") MultipartFile file) {
		model.addAttribute("errorList", new ArrayList<String>());
		//model.addAttribute("error", StringUtils.EMPTY);
		//request.getSession().setAttribute("error", StringUtils.EMPTY);
		
		try {
			if (StringUtils.isEmpty(file.getOriginalFilename())) {
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "admin/uploaddata/wmInvoice";
		   }
			
			String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			if (!(ext.equalsIgnoreCase(".xls"))) {
          	request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
          	return "admin/uploaddata/wmInvoice";
			}
			
			InputStream is = file.getInputStream();
			Long createdBy = getUser(request).getId();
			System.out.println("\nimportMainSheetService.importWMInvoice(is)\n");
			List<String> errorList = importMainSheetService.importWMInvoice(is, createdBy);
			if (errorList.isEmpty()) {
				model.addAttribute("msg", "Successfully uploaded all WM Invoices");
			} else {
				model.addAttribute("errorList", errorList);
			}
		} catch (Exception ex) {
			log.warn("Unable to import :===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			//str.add("Exception while uploading");
			//model.addAttribute("errorList", str);
			
			model.addAttribute("error", "An error occurred while uploading!!");
		}
		
		return "admin/uploaddata/wmInvoice";
	}
	
	@RequestMapping("/employee/upload.do")
	public String saveEmployeeData(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("dataFile") MultipartFile file) {
		try {
			if (StringUtils.isEmpty(file.getOriginalFilename())) {
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "admin/uploaddata/employee";
		   }
			
			String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			if (!(ext.equalsIgnoreCase(".xls"))) {
          	request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
          	return "admin/uploaddata/employee";
			}
			
			InputStream is = file.getInputStream();
			Long createdBy = getUser(request).getId();
			System.out.println("\nimportMainSheetService.importEmployeeMainSheet(is)\n");
			List<String> str = importMainSheetService.importEmployeeMainSheet(is, createdBy);
			if (str.isEmpty()) {
				model.addAttribute("msg", "Successfully uploaded all employee records");
			} else {
				model.addAttribute("errorList", str);
			}
		} catch (Exception ex) {
			log.warn("Unable to import :===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			//str.add("Exception while uploading");
			//model.addAttribute("errorList", str);
			
			model.addAttribute("error", "An error occurred while uploading!!");
			return "admin/uploaddata/employee";
		}
		
		return "admin/uploaddata/employee";
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
			@RequestParam("fuelvendor") Long fuelvendor,
			@RequestParam("totalFees") BigDecimal totalFees) {
		Map criterias = new HashMap();
		model.addAttribute("fuelvendor", genericDAO.findByCriteria(FuelVendor.class, criterias, "name", false));
		
		List<String> str=new ArrayList<String>();
		boolean flag=false;
		try {
			if (StringUtils.isEmpty(file.getOriginalFilename())) {
			    request.getSession().setAttribute("error", "Please choose a file to upload !!");
			    return "admin/uploaddata/fuellog";
		   }
			if (!StringUtils.isEmpty(file.getOriginalFilename())) {
				String ext=	file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if(!(ext.equalsIgnoreCase(".xls")) && !(ext.equalsIgnoreCase(".xlsx")) ) {
                request.getSession().setAttribute("error", "Please choose a file to upload with extention .xls!!");
                return "admin/uploaddata/fuellog";
				}
			}
			
			InputStream is = file.getInputStream();
			
			if (FuelVendorLogUploadUtil.isConversionRequired(fuelvendor)) {
				if (!FuelVendorLogUploadUtil.validateTotalFees(fuelvendor, totalFees)) {
					request.getSession().setAttribute("error", "Please specify total fees!!");
					return "admin/uploaddata/fuellog";
				}
				
				HashMap<String, Object> dataFromUser = new HashMap<String, Object>();
				dataFromUser.put("fees", totalFees); // use the column name that matches ExpectedColumn, ie., as in generic excel
				is = FuelVendorLogUploadUtil.convertToGenericFuelLogFormat(is, fuelvendor, this.genericDAO, this.importMainSheetService, dataFromUser);
			}
			
			str = importMainSheetService.importfuellogMainSheet(is,flag);
			System.out.println("\nimportMainSheetService.importMainSheet(is)\n");
			if (str.isEmpty()) {
				model.addAttribute("msg","successfully uploaded all fuellogs");
			}
			else {
				
				model.addAttribute("errorList", str);
			}
		} 
		catch (Exception ex) {
			log.warn("Unable to import :===>>>>>>>>>" + ex);
			ex.printStackTrace();
			
			//str.add("Exception while uploading");
			//model.addAttribute("errorList", str);
			
			model.addAttribute("error", "An error occurred while uploading!!");
			return "admin/uploaddata/fuellog";
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
	
	private void setupWMTicketUpload(ModelMap model) {
		Location location = genericDAO.getById(Location.class, 392l); // Fairless landfill
		List<Location> locationList = new ArrayList<Location>();
		locationList.add(location);
		model.addAttribute("destinationLocations", locationList);
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
