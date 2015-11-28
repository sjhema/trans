package com.primovision.lutransport.controller.hr.report;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hrreport.DriverPay;
import com.primovision.lutransport.model.hrreport.DriverPayWrapper;
import com.primovision.lutransport.model.hrreport.TimeSheetInput;
import com.primovision.lutransport.model.hrreport.TimeSheetWrapper;
import com.primovision.lutransport.model.report.FuelLogReportInput;
import com.primovision.lutransport.model.report.FuelLogReportWrapper;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;
/**
 * @author subodh
 *
 */
@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/hr/report/timesheet")
public class TimeSheetReportController extends BaseController{

	
	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(
			DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	@Autowired
	protected HrReportService hrReportService;
	
	public void setHrReportService(
			HrReportService hrReportService) {
		this.hrReportService = hrReportService;
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String populate(ModelMap model, HttpServletRequest request) {

		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		if (criteria != null) {
			if (criteria.getSearchMap() != null)
				criteria.getSearchMap().clear();
		}
		Map criterias = new HashMap();
		
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.clear();
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
	    criterias.clear();
		model.addAttribute("categories",genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		criterias.clear();
		criterias.put("status", 1);
		model.addAttribute("employees",genericDAO.findByCriteria(Driver.class, criterias,"fullName",false));
		
		return "hr/report/timesheet";
	}
	
	
	protected  Map<String,Object> generateData(SearchCriteria searchCriteria,HttpServletRequest request,TimeSheetInput input) 
	{
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		String sum= (String) searchCriteria.getSearchMap().get("summary");
		TimeSheetWrapper wrapper = generateTimeSheetReport(searchCriteria,input);
		 
		if(StringUtils.contains(sum, "true")){
		//params.put("totalRowCount", wrapper.getTotalRowCount());
		data.put("data", wrapper.getTimesheets());
		}
		else{
			
			data.put("data", wrapper.getTimesheetdetail());
		}
		params.put("totalrHours", wrapper.getTotalrHours());
		params.put("totalwHours", wrapper.getTotalwHours());
		params.put("totalot", wrapper.getTotalot());
		params.put("totaldt", wrapper.getTotaldt());
		data.put("params",params);
		  
		  return data;
	}
	
	
	public TimeSheetWrapper generateTimeSheetReport(SearchCriteria searchCriteria,TimeSheetInput input) {
	return hrReportService.generateTimeSheetData(searchCriteria,input);
	}
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") TimeSheetInput input, @RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
        //System.out.println("\nTimeSheetReportController==Search()==type===>"+type+"\n"); 
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		request.getSession().setAttribute("input", input);
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		 String sum= (String) criteria.getSearchMap().get("summary");
		try {
			Map<String, Object> datas = generateData(criteria, request,input);
		    if (StringUtils.isEmpty(type))
				type = "html";
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html"))
				response.setHeader("Content-Disposition",
						"attachment;filename=timesheet." + type);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map params = (Map)datas.get("params");
			/*JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("timeSheetReport",
					(List)datas.get("data"), params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "hr/report/timesheet/"+type;*/
			String reportName;
			if(StringUtils.contains(sum, "true")){
				reportName="timeSheetReport";
			}
			else{
				reportName="timeSheetReportDetail";
			}
			out = dynamicReportService.generateStaticReport(reportName,
					(List)datas.get("data"), params, type, request);
			out.writeTo(response.getOutputStream());
			out.close();
			return null;
			
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}

	}
	
	
		@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
		public String display(ModelMap model, HttpServletRequest request,
				HttpServletResponse response,
				@RequestParam(required = false, value = "type") String type,
				@RequestParam(required = false, value = "jrxml") String jrxml) {
			//System.out.println("\nfuellogBillingController==export()==type===>"+type+"\n"); 
			Map imagesMap = new HashMap();
			request.getSession().setAttribute("IMAGES_MAP", imagesMap);
			SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
			String sum= (String) criteria.getSearchMap().get("summary");
			criteria.setPageSize(10000);
			TimeSheetInput input = (TimeSheetInput)request.getSession().getAttribute("input");
			try {
				Map<String,Object> datas = generateData(criteria, request, input);
				//List propertyList = (List<String>) request.getSession()
				//		.getAttribute("propertyList");
				if (StringUtils.isEmpty(type))
					type = "xlsx";
				if (!type.equals("html") && !(type.equals("print"))) {
					response.setHeader("Content-Disposition",
							"attachment;filename= timesheet." + type);
				}
				response.setContentType(MimeUtil.getContentType(type));
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Map<String, Object> params = (Map<String,Object>)datas.get("params");
				/*if (!type.equals("print") &&!type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("fuellogReport",
							(List)datas.get("data"), params, type, request);
				}*/
				/*if (type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("timeSheetReport"+"pdf",
							(List)datas.get("data"), params, type, request);
				}
				else if (type.equals("csv")) {
					out = dynamicReportService.generateStaticReport("timeSheetReport"+"csv",
							(List)datas.get("data"), params, type, request);
				}*/
				if(StringUtils.contains(sum, "true")){
					if (type.equals("pdf") ||type.equals("csv") || type.equals("xls")){
						out = dynamicReportService.generateStaticReport("timeSheetReport",
							(List)datas.get("data"), params, type, request);	
					}
					else {
						out = dynamicReportService.generateStaticReport("timeSheetReport",
							(List)datas.get("data"), params, type, request);
					}
				}
				else{
					out = dynamicReportService.generateStaticReport("timeSheetReportDetail",
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
	public TimeSheetInput setupModel(HttpServletRequest request) {
		return new TimeSheetInput();
	}
	
	
	
	protected String processAjaxRequest(HttpServletRequest request,String action, Model model) 
	{
		if("findDCompany".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				List<Location> company=new ArrayList<Location>();
				Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
				company.add(driver.getCompany());
				company.add(driver.getTerminal());
				Gson gson = new Gson();
				return gson.toJson(company);
			}
		
		}
		if("findDTerminal".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				List<Location> terminal=new ArrayList<Location>();
				Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
				terminal.add(driver.getTerminal());
				Gson gson = new Gson();
				return gson.toJson(terminal);
			}
		
		}
		
		if("findDCategory".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("driver"))){
				List<EmployeeCatagory> category=new ArrayList<EmployeeCatagory>();
				Driver driver=genericDAO.getById(Driver.class,Long.parseLong(request.getParameter("driver")));
				category.add(driver.getCatagory());
				Gson gson = new Gson();
				return gson.toJson(category);
			}
		
		}
	
	
	return "";
}
	
}
