package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
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
import com.primovision.lutransport.model.FuelVendor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.report.FuelLogReportInput;
import com.primovision.lutransport.model.report.FuelLogReportWrapper;
import com.primovision.lutransport.model.report.FuelLogVerificationReportInput;
import com.primovision.lutransport.model.report.FuelLogVerificationReportWrapper;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;


@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/fuellogverificationreport")
public class FuelLogVerificationReportController extends BaseController{

	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(
			DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	public FuelLogVerificationReportController() {
	}
	
	@Autowired
	private ReportService reportService;
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String populate(ModelMap model, HttpServletRequest request) {

		if(request.getSession().getAttribute("fuelogverificationData")!=null){
			request.getSession().removeAttribute("fuelogverificationData");
		}
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (criteria != null)
		{
			
			if (criteria.getSearchMap() != null)
				criteria.getSearchMap().clear();
		}
		Map criterias = new HashMap();
		criterias.clear();
		criterias.put("type", 4);
		model.addAttribute("terminals",
				genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies",
				genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.clear();
		criterias.put("status", 1);
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "lastName", false));
		
		return "reportuser/report/fuellogverificationreport";
	}
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") FuelLogVerificationReportInput input, @RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
       
		
		if(StringUtils.isEmpty(input.getTransactionDateFrom()) || StringUtils.isEmpty(input.getTransactionDateTo())) {
			request.getSession().setAttribute("error","Please Enter Transaction From and Transaction To Dates");
		    return "redirect:start.do";
		}
		
		Map imagesMap = new HashMap();
		String p=request.getParameter("p");
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		//request.getSession().setAttribute("input", input);
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(1000);
		
		
		if(p==null)
			request.getSession().setAttribute("input", input);
		
		FuelLogVerificationReportInput input1=(FuelLogVerificationReportInput)request.getSession().getAttribute("input");
		
		
		 
		try {
			Map<String, Object> datas; 
			if(p==null)
			{
				 datas = generateData(criteria,request,input);
		    }else
			{
				 datas = generateData(criteria,request,input1);	
			}
			
			
		    if (StringUtils.isEmpty(type))
				type = "html";
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html"))
				response.setHeader("Content-Disposition",
						"attachment;filename=billinghistory." + type);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map params = (Map)datas.get("params");
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("fuellogVerificationReport",
					(List)datas.get("data"), params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "reportuser/report/fuellogverificationreport/"+type;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}

	}
	
	protected  Map<String,Object> generateData(SearchCriteria searchCriteria,HttpServletRequest request,FuelLogVerificationReportInput input) 
	{
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		if (!StringUtils.isEmpty(input.getCompany())) {
			Location comp = genericDAO.getById(Location.class,Long.parseLong(input.getCompany()));		
			params.put("company", comp.getName());
		}
		else{
			params.put("company", null);
		}
		if (!StringUtils.isEmpty(input.getTerminal())) {
			Location comp = genericDAO.getById(Location.class,Long.parseLong(input.getTerminal()));	
			params.put("terminal", comp.getName());
		}
		else{
			params.put("terminal", null);
		}		
		params.put("driver", input.getDriver());
		params.put("transactionDateFrom", input.getTransactionDateFrom());
		params.put("transactionDateTo", input.getTransactionDateTo());
		 FuelLogVerificationReportWrapper wrapper = generateFuellogVerificationReport(searchCriteria,input);	  
		data.put("data", wrapper.getFuellogverification());
		data.put("params",params);
		request.getSession().setAttribute("fuelogverificationData",data);
		return data;
	}
	
	
	public FuelLogVerificationReportWrapper generateFuellogVerificationReport(SearchCriteria searchCriteria,FuelLogVerificationReportInput input) {
		return reportService.generateFuelLogVerificationData(searchCriteria,input);
		}
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public String display(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		System.out.println("\nfuellogBillingController==export()==type===>"+type+"\n"); 
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
		//FuelLogVerificationReportInput input = (FuelLogVerificationReportInput)request.getSession().getAttribute("input");
		try {
			Map<String,Object> datas = (Map<String, Object>) request.getSession().getAttribute("fuelogverificationData");
			//List propertyList = (List<String>) request.getSession()
			//		.getAttribute("propertyList");
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= FuelLogVerificationReport." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String,Object>)datas.get("params");
			/*if (!type.equals("print") &&!type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("fuellogReport",
						(List)datas.get("data"), params, type, request);
			}*/
			if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("fuellogVerificationReport"+"pdf",
						(List)datas.get("data"), params, type, request);
			}
			else if (type.equals("csv")) {
				out = dynamicReportService.generateStaticReport("fuellogVerificationReport"+"csv",
						(List)datas.get("data"), params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport("fuellogVerificationReport",
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
	public FuelLogVerificationReportInput setupModel(HttpServletRequest request) {
		return new FuelLogVerificationReportInput();
	}
}
