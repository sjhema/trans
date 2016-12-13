package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.FuelVendor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.Vehicle;
//import com.primovision.lutransport.model.report.BillingHistoryInput;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.report.BillingHistoryInput;
import com.primovision.lutransport.model.report.FuelLogReportInput;
import com.primovision.lutransport.model.report.FuelLogReportWrapper;

import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;

/**
 * @author Amit
 * 
 */
@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/fuellogbilling")
public class FuelLogBillingController extends BaseController{

	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(
			DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	public FuelLogBillingController() {
	}
	
	@Autowired
	private ReportService reportService;
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String populate(ModelMap model, HttpServletRequest request) {

		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (criteria != null)
		{
			
			if (criteria.getSearchMap() != null)
				criteria.getSearchMap().clear();
		}
		Map criterias = new HashMap();
		model.addAttribute("fuelvendor",genericDAO.findByCriteria(FuelVendor.class, criterias, "name", false));
		criterias.put("type", 1);
		model.addAttribute("origins",
				genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 2);
		model.addAttribute("destinations",
				genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals",
				genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 3);
		model.addAttribute("companies",
				genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 1);
		model.addAttribute("trucks",genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		criterias.clear();
		/*EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class,2l);
		criterias.put("catagory", empobj);*/
		model.addAttribute("drivers", genericDAO.executeSimpleQuery("Select obj from Driver obj group by obj.fullName order by obj.fullName "));
		criterias.clear();
		model.addAttribute("states", genericDAO.findByCriteria(State.class, criterias, "name", false));
		criterias.clear();
		model.addAttribute("fuelTypes", listStaticData("FUEL_TYPE"));
		
		model.addAttribute("subcontractors", genericDAO.executeSimpleQuery("Select obj from SubContractor obj order by obj.name "));
		
		return "reportuser/report/fuellogReport";
	}
	
	
	protected  Map<String,Object> generateData(SearchCriteria searchCriteria,HttpServletRequest request,FuelLogReportInput input) 
	{
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		
		FuelLogReportWrapper wrapper = generateFuellogReport(searchCriteria,input);
		 
		params.put("totalAmounts",wrapper.getTotalAmounts());
		params.put("totaldiscounts", wrapper.getTotaldiscounts());
		params.put("totalFees", wrapper.getTotalFees());
		params.put("totalGallons",wrapper.getTotalGallons());
		params.put("totalColumn",wrapper.getTotalColumn());
		params.put("totalGrossCost",wrapper.getTotalGrossCost());
		
		// Fuel log - truck report 3rd Jun 2016
		String company = "-";
		if (!StringUtils.isEmpty(wrapper.getCompany())) {
			company = retrieveCompanyNames(wrapper.getCompany());
		}
		params.put("company", company);
		
		// Fuel log - truck report 3rd Jun 2016
		String transactionDateRange = "-";
		if (!StringUtils.isEmpty(wrapper.getTransactionDateFrom()) && !StringUtils.isEmpty(wrapper.getTransactionDateTo())) {
			transactionDateRange = wrapper.getTransactionDateFrom() + " - " + wrapper.getTransactionDateTo();
		}
		params.put("transactionDateRange", transactionDateRange);
		  
		data.put("data", wrapper.getFuellog());
		data.put("params",params);
		  
		return data;
	}
	
	// Fuel log - truck report - 3rd Jun 2016
	private String retrieveCompanyNames(String companyIds) {
		if (StringUtils.isEmpty(companyIds)) {
			return StringUtils.EMPTY;
		}
		
		String companyQuery = "select obj from Location obj where obj.id in (" 
	   		+ companyIds
	   		+")";
		
		List<Location> locationList = genericDAO.executeSimpleQuery(companyQuery);
		if (locationList == null || locationList.isEmpty()) {
			return StringUtils.EMPTY;
		}
		
		String comanyNames = StringUtils.EMPTY;
		for (Location aLocation : locationList) {
			comanyNames = comanyNames + ", " + aLocation.getName();
		}
		
		return comanyNames.substring(2);
	}
	
	public FuelLogReportWrapper generateFuellogReport(SearchCriteria searchCriteria,FuelLogReportInput input) {
		return reportService.generateFuellogData(searchCriteria,input, false);
	}
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") FuelLogReportInput input, @RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
        System.out.println("\nfuellogBillingController==Search()==type===>"+type+"\n"); 
		Map imagesMap = new HashMap();
		String p=request.getParameter("p");
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		//request.getSession().setAttribute("input", input);
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		
		// JJ Keller Fuel card - 12th Dec 2016
		if (StringUtils.equals(FuelLogReportInput.REPORT_TYPE_JJ_KELLER_FUEL_TXN, input.getReportType())) {
			criteria.setPageSize(15000);
			criteria.setPage(0);
		} else {
			criteria.setPageSize(1000);
		}
		
		if(p==null)
			request.getSession().setAttribute("input", input);
		
		FuelLogReportInput input1=(FuelLogReportInput)request.getSession().getAttribute("input");
		 
		try {
			Map<String, Object> datas; 
			if(p==null)
			{
				 datas = generateData(criteria,request,input);
		    }else
			{
				 datas = generateData(criteria,request,input1);	
			}
			
			// JJ Keller Fuel card - 12th Dec 2016
			if (StringUtils.equals(FuelLogReportInput.REPORT_TYPE_JJ_KELLER_FUEL_TXN, input.getReportType())) {
				return processJJKellerFuelTxnReportRequest(datas, request, response);
			}
			
		    if (StringUtils.isEmpty(type))
				type = "html";
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html"))
				response.setHeader("Content-Disposition",
						"attachment;filename=billinghistory." + type);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map params = (Map)datas.get("params");
			
			String reportType = "fuellogReport";
			// Fuel log - truck report - 3rd Jun 2016
			if (FuelLogReportInput.REPORT_TYPE_FUEL_TRUCK.equals(input.getReportType())) {
				reportType = "fuelTruckReport";
			}
			
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(reportType,
					(List)datas.get("data"), params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "reportuser/report/fuellogreport/"+type;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}

	}
	
	// JJ Keller Fuel card - 12th Dec 2016
	private String processJJKellerFuelTxnReportRequest(Map<String, Object> datas, HttpServletRequest request, HttpServletResponse response) {
		StringBuffer reportBuff = new StringBuffer();
		
		String padChar = " ";
		String ssn = StringUtils.EMPTY;
		String txnDate = StringUtils.EMPTY;
		String txnTime = StringUtils.EMPTY;
		String city = StringUtils.EMPTY;
		String state = StringUtils.EMPTY;
		
		List<FuelLog> fuelLogList = (List<FuelLog>) datas.get("data");
		for (FuelLog aFuelLog : fuelLogList) {
			ssn = StringUtils.rightPad(aFuelLog.getSsn(), 9, padChar);
			
			txnDate = StringUtils.replace(aFuelLog.getTransactionsDate(), "-", StringUtils.EMPTY);
			txnDate = StringUtils.rightPad(txnDate, 8, padChar);
			
			txnTime = StringUtils.replace(aFuelLog.getTransactiontime(), ":", StringUtils.EMPTY);
			txnTime = StringUtils.rightPad(txnTime, 4, padChar);
			
			city = StringUtils.substring(aFuelLog.getCity(), 0, 50);
			city = StringUtils.rightPad(city, 50, padChar);
			state = StringUtils.rightPad(aFuelLog.getStates(), 2, padChar);
			
			reportBuff.append(ssn)
						 .append(txnDate)
						 .append(txnTime)
						 .append(city)
						 .append(state)
						 .append("\n");
		}
		
		String type = "txt";
		response.setHeader("Content-Disposition", "attachment;filename=JJKellerFuelCard." + type);
		response.setContentType(MimeUtil.getContentType(type));
		
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			out.write(reportBuff.toString().getBytes());
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

		@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
		public String display(ModelMap model, HttpServletRequest request,
				HttpServletResponse response,
				@RequestParam(required = false, value = "type") String type,
				@RequestParam(required = false, value = "jrxml") String jrxml,
				@RequestParam(required = false, value = "audit") boolean audit) {
			System.out.println("\nfuellogBillingController==export()==type===>"+type+"\n"); 
			Map imagesMap = new HashMap();
			request.getSession().setAttribute("IMAGES_MAP", imagesMap);
			SearchCriteria criteria = (SearchCriteria) request.getSession()
					.getAttribute("searchCriteria");
			criteria.setPageSize(15000);
			criteria.setPage(0);
			FuelLogReportInput input = (FuelLogReportInput)request.getSession().getAttribute("input");
			try {
				Map<String,Object> datas = generateData(criteria, request, input);
				//List propertyList = (List<String>) request.getSession()
				//		.getAttribute("propertyList");
				
				String reportType = "fuellogReport";
				if (FuelLogReportInput.REPORT_TYPE_FUEL_TRUCK.equals(input.getReportType())) {
					reportType = "fuelTruckReport";
				}
				
				if (StringUtils.isEmpty(type))
					type = "xlsx";
				if (!type.equals("html") && !(type.equals("print"))) {
					response.setHeader("Content-Disposition",
							"attachment;filename= fuellogReport." + type);
				}
				response.setContentType(MimeUtil.getContentType(type));
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Map<String, Object> params = (Map<String,Object>)datas.get("params");
				/*if (!type.equals("print") &&!type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("fuellogReport",
							(List)datas.get("data"), params, type, request);
				}*/
				if (type.equals("pdf")) {
					String reportFile = audit ? reportType+"pdfAudit" : reportType+"pdf";
					out = dynamicReportService.generateStaticReport(reportFile,
							(List)datas.get("data"), params, type, request);
				}
				else if (type.equals("csv")) {
					out = dynamicReportService.generateStaticReport(reportType+"csv",
							(List)datas.get("data"), params, type, request);
				}
				else {
					out = dynamicReportService.generateStaticReport(reportType,
							(List)datas.get("data"), params, type, request);
				}
			
				out.writeTo(response.getOutputStream());
				out.close();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				log.warn("Unable to create file :" + e);
				request.getSession().setAttribute("errors", e.getMessage());
				return "report.error";
			}
		}
	
	@ModelAttribute("modelObject")
	public FuelLogReportInput setupModel(HttpServletRequest request) {
		return new FuelLogReportInput();
	}

}
