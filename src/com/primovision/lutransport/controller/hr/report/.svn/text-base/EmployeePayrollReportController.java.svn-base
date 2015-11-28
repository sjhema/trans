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


import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.util.MimeUtil;

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hrreport.EmployeePayrollInput;
import com.primovision.lutransport.model.hrreport.EmployeePayrollWrapper;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;
/**
 * @author subodh
 *
 */
@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/hr/report/employeepayroll")
public class EmployeePayrollReportController extends BaseController{

	
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
		model.addAttribute("payrollrunStatus", listStaticData("PAYROLLRUN_STATUS"));
		return "hr/report/employeepayroll/emppayroll";
	}
	
	
	protected  Map<String,Object> generateData(SearchCriteria searchCriteria,HttpServletRequest request,EmployeePayrollInput input) 
	{
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		
		EmployeePayrollWrapper wrapper = generateEmployeePayrollData(searchCriteria,input);
		
		
		 
		
		 
		 
		params.put("totalRowCount", wrapper.getTotalRowCount());
		params.put("sumtotalAmount", wrapper.getSumtotalAmount());
		params.put("sumdtAmount", wrapper.getSumdtAmount());
		params.put("sumotAmount", wrapper.getSumotAmount());
		params.put("sumregularAmount", wrapper.getSumregularAmount());
		
		  
		  
		    //data.put("data", wrapper.getAttendance());
		data.put("data", wrapper.getTimesheets());
			data.put("params",params);
		  
		  return data;
	}
	
	
	public EmployeePayrollWrapper generateEmployeePayrollData(SearchCriteria searchCriteria,EmployeePayrollInput input) {
	return hrReportService.generateEmployeePayrollData(searchCriteria,input);
	}
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") EmployeePayrollInput input, @RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
        System.out.println("\nEmployeePayrollController==Search()==type===>"+type+"\n"); 
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		request.getSession().setAttribute("input", input);
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		try {
			Map<String, Object> datas = generateData(criteria, request,input);
		    if (StringUtils.isEmpty(type))
				type = "html";
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html"))
				response.setHeader("Content-Disposition",
						"attachment;filename=employeepayroll." + type);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map params = (Map)datas.get("params");
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("employeepayrollReport",
					(List)datas.get("data"), params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "hr/report/employeepayroll/"+type;
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
			System.out.println("\nHourlyPayroll==export()==type===>"+type+"\n"); 
			Map imagesMap = new HashMap();
			request.getSession().setAttribute("IMAGES_MAP", imagesMap);
			SearchCriteria criteria = (SearchCriteria) request.getSession()
					.getAttribute("searchCriteria");
			criteria.setPageSize(10000);
			EmployeePayrollInput input = (EmployeePayrollInput)request.getSession().getAttribute("input");
			try {
				Map<String,Object> datas = generateData(criteria, request, input);
				//List propertyList = (List<String>) request.getSession()
				//		.getAttribute("propertyList");
				if (StringUtils.isEmpty(type))
					type = "xlsx";
				if (!type.equals("html") && !(type.equals("print"))) {
					response.setHeader("Content-Disposition",
							"attachment;filename= hourlyPayroll." + type);
				}
				response.setContentType(MimeUtil.getContentType(type));
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Map<String, Object> params = (Map<String,Object>)datas.get("params");
				/*if (!type.equals("print") &&!type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("fuellogReport",
							(List)datas.get("data"), params, type, request);
				}*/
				if (type.equals("pdf")) {
					out = dynamicReportService.generateStaticReport("employeepayrollReportpdf",
							(List)datas.get("data"), params, type, request);
				}
				else if (type.equals("csv")) {
					out = dynamicReportService.generateStaticReport("employeepayrollReport",
							(List)datas.get("data"), params, type, request);
					/*out = dynamicReportService.generateStaticReport("fuellogReport"+"csv",
							(List)datas.get("data"), params, type, request);*/
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
	public EmployeePayrollInput setupModel(HttpServletRequest request) {
		return new EmployeePayrollInput();
	}
	
	
}
