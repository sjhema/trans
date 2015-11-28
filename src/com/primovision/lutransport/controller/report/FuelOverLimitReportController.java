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
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.report.FuelOverLimitInput;
import com.primovision.lutransport.model.report.FuelOverLimitInput;
import com.primovision.lutransport.model.report.FuelOverLimitReportWrapper;
import com.primovision.lutransport.model.report.FuelViolationReportWrapper;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.ReportService;

@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/reportuser/report/fueloverlimit")
public class FuelOverLimitReportController extends BaseController{

	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(
			DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	
	public FuelOverLimitReportController() {
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
		
		criterias.put("type", 4);
		model.addAttribute("terminals",
				genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 3);
		model.addAttribute("companies",
				genericDAO.findByCriteria(Location.class, criterias, "name",false));
		
		criterias.clear();
		EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class,2l);
		criterias.put("catagory", empobj);
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "lastName", false));
		
		
		return "reportuser/report/fueloverlimitreport/fueloverlimitform";
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") FuelOverLimitInput input, @RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
       
		if(StringUtils.isEmpty(request.getParameter("maxGallons"))) {
			request.getSession().setAttribute("error","Please Enter Max Gallons Value");
		    return "redirect:start.do";
		}
		
		if(StringUtils.isEmpty(request.getParameter("transactionDateFrom")) || StringUtils.isEmpty(request.getParameter("transactionDateTo"))) {
			request.getSession().setAttribute("error","Please Enter Transaction From and Transaction To Dates");
		    return "redirect:start.do";
		}
		
		
		
		Map imagesMap = new HashMap();
		String p=request.getParameter("p");
		
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);		
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(100000);
		
		
		if(p==null)
			request.getSession().setAttribute("input", input);
		
		FuelOverLimitInput input1=(FuelOverLimitInput)request.getSession().getAttribute("input");
		
		
		 
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
						"attachment;filename=fueloverlimit." + type);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map params = (Map)datas.get("params");
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("fueloverlimitReport",
					(List)datas.get("data"), params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "/reportuser/report/fueloverlimitreport/"+type;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
		}

	}
	
	
	
	protected  Map<String,Object> generateData(SearchCriteria searchCriteria,HttpServletRequest request,FuelOverLimitInput input) 
	{
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		
		FuelOverLimitReportWrapper wrapper = generateFuelOverLimitReport(searchCriteria,input);
	
		String companyquery="select obj from Location obj where id in ("+input.getCompany()+")";
		
		List<Location> companyList= genericDAO.executeSimpleQuery(companyquery);
		String companies="";
		for(Location locObj:companyList){
			if(companies.equals("")){
				companies=locObj.getName();
			}
			else{
				companies=companies+","+locObj.getName();
			}
		}
		
		params.put("company",companies);
		
		
        String terminalquery="select obj from Location obj where id in ("+input.getTerminal()+")";
		
		List<Location> terminalList= genericDAO.executeSimpleQuery(terminalquery);
		String terminals="";
		for(Location locObj:terminalList){
			if(terminals.equals("")){
				terminals=locObj.getName();
			}
			else{
				terminals=terminals+","+locObj.getName();
			}
		}	
		params.put("terminal",terminals);
		
		
		String driverquery="select obj from Driver obj where id in ("+input.getDriver()+")";
		
		List<Driver> driverList= genericDAO.executeSimpleQuery(driverquery);
		String drivers="";
		for(Driver drvObj:driverList){
			if(drivers.equals("")){
				drivers=drvObj.getFullName();
			}
			else{
				drivers=drivers+","+drvObj.getFullName();
			}
		}		
		params.put("driver",drivers);
		
		params.put("transactionDateFrom",(String) searchCriteria.getSearchMap()
				.get("transactionDateFrom"));
		
		params.put("transactionDateTo",(String) searchCriteria.getSearchMap().get(
		"transactionDateTo"));
		
		params.put("maxGallons",input.getMaxGallons());
		
		params.put("totalGallons",wrapper.getTotalGallons());
		
		data.put("data", wrapper.getFuellog());
		data.put("params",params);
		  
		request.getSession().setAttribute("fueloverlimitdataList",data);
		
		return data;
	}
	
	
	public FuelOverLimitReportWrapper generateFuelOverLimitReport(SearchCriteria searchCriteria,FuelOverLimitInput input) {
		return reportService.generateFuelOverLimitData(searchCriteria,input);
	}
	
	
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public String display(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(100000);
		criteria.setPage(0);
		FuelOverLimitInput input = (FuelOverLimitInput)request.getSession().getAttribute("input");
		try {
			
			Map<String,Object> datas = generateData(criteria,request,input);
			//Map<String,Object> datas = generateData(criteria, request, input);
			//List propertyList = (List<String>) request.getSession()
			//		.getAttribute("propertyList");
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= fueloverlimitReport." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String,Object>)datas.get("params");
			/*if (!type.equals("print") &&!type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("fuellogReport",
						(List)datas.get("data"), params, type, request);
			}*/
			if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport("fueloverlimitReport"+"pdf",
						(List)datas.get("data"), params, type, request);
			}
			else if (type.equals("csv")) {
				out = dynamicReportService.generateStaticReport("fueloverlimitReport"+"csv",
						(List)datas.get("data"), params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport("fueloverlimitReport",
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
	public FuelOverLimitInput setupModel(HttpServletRequest request) {
		return new FuelOverLimitInput();
	}

}
