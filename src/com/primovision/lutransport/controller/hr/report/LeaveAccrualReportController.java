package com.primovision.lutransport.controller.hr.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
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

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.LeaveType;
import com.primovision.lutransport.model.hrreport.LeaveAccrualReport;

import com.primovision.lutransport.service.DynamicReportService;
import com.primovision.lutransport.service.HrReportService;

@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/hr/report/leaveaccrual")
public class LeaveAccrualReportController extends BaseController {
	@Autowired
	protected DynamicReportService dynamicReportService;

	public void setDynamicReportService(DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	@Autowired
	protected HrReportService hrReportService;
	
	public void setHrReportService(HrReportService hrReportService) {
		this.hrReportService = hrReportService;
	}
	
	public LeaveAccrualReportController() {
		setUrlContext("/hr/report/leaveaccrual");
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String start(ModelMap model, HttpServletRequest request) {
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (searchCriteria != null) {
			if (searchCriteria.getSearchMap() != null)
				searchCriteria.getSearchMap().clear();
		}
		
		request.getSession().setAttribute("errors", StringUtils.EMPTY);
		
		setupEnquiry(model);
		return getUrlContext() + "/enquiry";
	}
	
	private void setupEnquiry(ModelMap model) {
		Map criterias = new HashMap();
		
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	   criterias.clear();
		model.addAttribute("categories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias, "name", false));
		criterias.clear();
		criterias.put("status", 1);
		model.addAttribute("employees", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		
		criterias.clear();
		criterias.put("name", "Vacation");
		model.addAttribute("leavetypes", genericDAO.findByCriteria(LeaveType.class, criterias, "name", false));
		
		List<Integer> years = deriveYears();
		model.addAttribute("years", years);
	}
	
	private List<Integer> deriveYears() {
		List<Integer> years = new ArrayList<Integer>();
		
		int startYear = 2015;
		Calendar cal = Calendar.getInstance();
		int currentYear = cal.get(Calendar.YEAR);
		
		for (int aYear = startYear; aYear <= currentYear; aYear++) {
			years.add(aYear);
		}
		
		return years;
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") LeaveAccrualReport input, 
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		if (StringUtils.isEmpty(input.getCompany()) && StringUtils.isEmpty(input.getEmployee())
				 && StringUtils.isEmpty(input.getTerminal()) && StringUtils.isEmpty(input.getCategory())) {
			request.getSession().setAttribute("errors", "Please choose at least one of Company, Terminal, Employee or Category");
			setupEnquiry(model);
			return getUrlContext() + "/enquiry";
		}
		
		if (input.getAccrualYear() == null) {
			request.getSession().setAttribute("errors", "Please choose Accrual Year");
			setupEnquiry(model);
			return getUrlContext() + "/enquiry";
		}
		
		if (StringUtils.isEmpty(input.getLeaveType())) {
			request.getSession().setAttribute("errors", "Please choose Leave Type");
			setupEnquiry(model);
			return getUrlContext() + "/enquiry";
		}
		
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		request.getSession().setAttribute("input", input);
		
		populateSearchCriteria(request, request.getParameterMap());
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		try {
			Map<String, Object> datas = generateData(criteria, request, input);
			
		   if (StringUtils.isEmpty(type)) {
				type = "html";
		   }
			
		   response.setContentType(MimeUtil.getContentType(type));
			
		   if (!type.equals("html")) {
				response.setHeader("Content-Disposition",
						"attachment;filename=leaveAccrual." + type);
		   }
		   
		   List<LeaveAccrualReport> data = (List<LeaveAccrualReport>) datas.get("data");
			Map<String, Object> params = (Map<String, Object>) datas.get("params");
			
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("leaveAccrual",
					data, params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return getUrlContext() + "/" + type;
		} catch (Exception e) {
			return processError(e, "searching", request);
		}
	}
	
	private String processError(Exception e, String event, HttpServletRequest request) {
		e.printStackTrace();
		log.warn("Exception while " + event + ":" + e);
		
		List<String> errors = new ArrayList<String>();
		errors.add(e.getMessage());
		request.getSession().setAttribute("error", errors);
		
		return "error";
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public String export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25000);
		criteria.setPage(0);
		
		ByteArrayOutputStream out = null;
		LeaveAccrualReport input = (LeaveAccrualReport) request.getSession().getAttribute("input");
		try {
			Map<String,Object> datas = generateData(criteria, request, input);
			
			if (StringUtils.isEmpty(type)) {
				type = "xlsx";
			}
			
			if (!"html".equals(type) && !"print".equals(type)) {
				response.setHeader("Content-Disposition",
						"attachment;filename=leaveAccrual." + type);
			}
			
			response.setContentType(MimeUtil.getContentType(type));
			
			List<LeaveAccrualReport> data = (List<LeaveAccrualReport>) datas.get("data");
			Map<String, Object> params = (Map<String, Object>) datas.get("params");
			
			String report = "leaveAccrual";
			if ("pdf".equals(type) || "print".equals(type) ) {
				report += type;
			}
			
			out = dynamicReportService.generateStaticReport(report, data, params, type, request);
		
			out.writeTo(response.getOutputStream());
			criteria.setPageSize(25);
			
			return null;
		} catch (Exception e) {
			return processError(e, "creating file", request);
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private Map<String,Object> generateData(SearchCriteria searchCriteria,
			HttpServletRequest request, LeaveAccrualReport input) {
		List<LeaveAccrualReport> list = hrReportService.generateLeaveAccrualReport(searchCriteria, input);
		
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("data", list);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("company", retrieveCompanyName(input.getCompany()));
		params.put("accrualYear", (input.getAccrualYear() == null || input.getAccrualYear() == 0) ? null : input.getAccrualYear());
		data.put("params", params);
		
		return data;
	}
	
	private String retrieveCompanyName(String companyId) {
		if (StringUtils.isEmpty(companyId)) {
			return StringUtils.EMPTY;
		}
		
		Location company = genericDAO.getById(Location.class, Long.valueOf(companyId));
		return company.getName();
	}
	
	
	@ModelAttribute("modelObject")
	public LeaveAccrualReport setupModel(HttpServletRequest request) {
		return new LeaveAccrualReport();
	}
}





