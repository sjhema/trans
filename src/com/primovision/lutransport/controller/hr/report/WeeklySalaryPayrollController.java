package com.primovision.lutransport.controller.hr.report;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateMidnight;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.hrreport.WeeklyPay;
import com.primovision.lutransport.model.hrreport.WeeklypayWrapper;
import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;

/**
 * @author kishor
 *
 */
@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/hr/report/weeklysalarypayroll")
public class WeeklySalaryPayrollController extends BaseController{
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
	public String populate(ModelMap model, HttpServletRequest request, @RequestParam("for") String from) {
		
		System.out.println("\n\nWeekly Salary Pay Roll :  Start\n");
		Map criterias=new HashMap();
		String query="select obj from Driver obj where obj.status=1 and obj.payTerm=3 order by fullName ASC";
		model.addAttribute("employees", genericDAO.executeSimpleQuery(query));
		
		String qury1="select obj from EmployeeCatagory obj where obj.id not in (2,3)";
		model.addAttribute("categories", genericDAO.executeSimpleQuery(qury1));
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.clear();
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		
		if(StringUtils.equalsIgnoreCase(from,"payroll")){
			return "hr/report/weeklypayroll";
		}
		else{
			return "hr/report/weeklypayrollreport";
		}
	}
	protected Map<String,Object> generateData(SearchCriteria criteria,
			HttpServletRequest request, String from) {
		
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> params = new HashMap<String,Object>();
		WeeklypayWrapper wrapper=generateWeeklyPayroll(criteria,from);
		params.put("sumAmount", wrapper.getSumAmount());
		params.put("sumTotal", wrapper.getSumTotal());
		params.put("totalRowCount", wrapper.getTotalRowCount());
		params.put("company", wrapper.getCompany());
		params.put("terminal", wrapper.getTerminal());
		params.put("payrollNumber", wrapper.getPayrollNumber());
		params.put("payRollBatch", wrapper.getPayRollBatch());
		
		data.put("params", params);
		data.put("data", wrapper.getDetails());
		return data;
	}
	public WeeklypayWrapper generateWeeklyPayroll(SearchCriteria criteria,String from){
		return hrReportService.generateWeeklyPayrollData(criteria,from);
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml, @RequestParam("for") String from) {
		
		 Map imagesMap = new HashMap();
		   request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		   populateSearchCriteria(request, request.getParameterMap());
		   SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		   Map<String,Object> datas = generateData(searchCriteria, request, from);
		   if (StringUtils.isEmpty(type))
				type = "html";
			
			
			response.setContentType(MimeUtil.getContentType(type));
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + "weeklypay" + "." + type);
			}
		   try{
			   ByteArrayOutputStream out = new ByteArrayOutputStream();
			   Map params = (Map)datas.get("params");
			   out = dynamicReportService.generateStaticReport("weeklypay",
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
			@RequestParam(required = false, value = "jrxml") String jrxml,@RequestParam("for") String from) {
		Map imagesMap = new HashMap();
		//String sum=request.getParameter("typ");	
		
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		try {
			String report="weeklypay";
			
			
			Map<String,Object> datas = generateData(criteria, request,from);
			//List propertyList = (List<String>) request.getSession()
			//		.getAttribute("propertyList");
			if (StringUtils.isEmpty(type))
				type = "xlsx";
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition",
						"attachment;filename= salarypayroll." + type);
			}
			response.setContentType(MimeUtil.getContentType(type));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = (Map<String,Object>)datas.get("params");
			if (!type.equals("print")&&!type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport(report,
						(List)datas.get("data"), params, type, request);
			}
			else if (type.equals("pdf")) {
				out = dynamicReportService.generateStaticReport(report,
						(List)datas.get("data"), params, type, request);
			}
			else {
				out = dynamicReportService.generateStaticReport(report,
						(List)datas.get("data"), params, type, request);
			}
		
			out.writeTo(response.getOutputStream());
			criteria.setPageSize(25);
			out.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
			return "report.error";
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/save.do")
	public String save(ModelMap model, HttpServletRequest request,	HttpServletResponse response, @RequestParam("for")String from) {
		
		System.out.println("\n\nWeekly Salary Pay Roll :  Save\n");
		
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		String company=(String)criteria.getSearchMap().get("company");
		String terminal =(String) criteria.getSearchMap().get("terminal");
		
		String payrollNumber=(String)criteria.getSearchMap().get("checkDate");
		payrollNumber=ReportDateUtil.getFromDate(payrollNumber);
		StringBuilder query= new StringBuilder("select obj from WeeklyPay obj where obj.company="+company+" and obj.checkDate='"+payrollNumber+"'");
		
		if(!StringUtils.isEmpty(terminal)){
			query.append(" and obj.terminal="+terminal);
		}
		
		List<WeeklyPay> pays=genericDAO.executeSimpleQuery(query.toString());
		if(!pays.isEmpty()){
			request.getSession().setAttribute("error", "Payroll with entered check date already exists. Please choose another");
			return "blank/blank";
		}
		try {
			hrReportService.saveWeeklyPayData(request, criteria,from);
			request.getSession().setAttribute("msg", "Weekly Pay is saved successfully");
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
		}
		return "blank/blank";
	}
	
	
	
	
	
	@Override
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		
		
		
		
		if ("findDriver".equalsIgnoreCase(action)) {

			List<Driver> dlst=null;
			String companyId = request.getParameter("company");
			if (!StringUtils.isEmpty(companyId))
			{		
				
					String query="select obj from Driver obj where obj.status=1 and obj.payTerm=3 and obj.company='"+companyId+"' order by fullName ASC";
					dlst=genericDAO.executeSimpleQuery(query);
				
			}
			Gson gson = new Gson();
			return gson.toJson(dlst);
		}		
		return "";
	}
}
