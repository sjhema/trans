package com.primovision.lutransport.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.BaseController;

import com.primovision.lutransport.core.util.MimeUtil;

import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.injury.Injury;
import com.primovision.lutransport.model.injury.InjuryIncidentType;
import com.primovision.lutransport.model.insurance.InsuranceCompany;

import com.primovision.lutransport.service.DynamicReportService;

@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/admin/injury/reports")
public class InjuryReportController extends BaseController {
	@Autowired
	private DynamicReportService dynamicReportService;

	public void setDynamicReportService(DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	public InjuryReportController() {
		setUrlContext("admin/injury/reports");
	}
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String start(ModelMap model, HttpServletRequest request) {
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (searchCriteria != null && searchCriteria.getSearchMap() != null) {
			searchCriteria.getSearchMap().clear();
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();

		model.addAttribute("insuranceCompanies", genericDAO.findByCriteria(InsuranceCompany.class, criterias, "name", false));
		model.addAttribute("incidentTypes", genericDAO.findByCriteria(InjuryIncidentType.class, criterias, "incidentType asc", false));
		
		criterias.clear();
		criterias.put("dataType", "INJURY_STATUS");
		model.addAttribute("statuses", genericDAO.findByCriteria(StaticData.class, criterias, "dataText", false));
		criterias.clear();
		model.addAttribute("injuries", genericDAO.findByCriteria(Injury.class, criterias, "claimNumber asc", false));
		
		String query = "select distinct(obj.fullName) from Driver obj order by obj.fullName";
		model.addAttribute("driverNames", genericDAO.executeSimpleQuery(query));
		
		return getUrlContext() + "/injuryReport";
	}
	
	private Map<String, Object> performSearch(SearchCriteria criteria, String overrideInjuryStatus,
			boolean searchNotInInjuryStatus) {
		String insuranceCompany = (String) criteria.getSearchMap().get("insuranceCompany");
		String driver = (String) criteria.getSearchMap().get("driver");
		
		String claimNumber = (String) criteria.getSearchMap().get("claimNumber");
		
		String incidentType = (String) criteria.getSearchMap().get("incidentType");
		
		String injuryStatus = StringUtils.EMPTY;
		if (StringUtils.isNotEmpty(overrideInjuryStatus)) {
			injuryStatus = overrideInjuryStatus;
		} else {
			injuryStatus = (String) criteria.getSearchMap().get("injuryStatus");
		}
		
		String incidentDateFrom = (String) criteria.getSearchMap().get("incidentDateFrom");
		String incidentDateTo = (String) criteria.getSearchMap().get("incidentDateTo");
		
		StringBuffer query = new StringBuffer("select obj from Injury obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Injury obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(insuranceCompany)) {
			whereClause.append(" and obj.insuranceCompany.id=" + insuranceCompany);
		}
		if (StringUtils.isNotEmpty(claimNumber)) {
			whereClause.append(" and obj.claimNumber='" + claimNumber + "'");
		}
		if (StringUtils.isNotEmpty(driver)) {
			whereClause.append(" and obj.driver.fullName='" + driver + "'");
		}
	   if (StringUtils.isNotEmpty(incidentDateFrom)){
        	try {
        		whereClause.append(" and obj.incidentDate >='"+sdf.format(dateFormat.parse(incidentDateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      if (StringUtils.isNotEmpty(incidentDateTo)){
	     	try {
	     		whereClause.append(" and obj.incidentDate <='"+sdf.format(dateFormat.parse(incidentDateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      if (StringUtils.isNotEmpty(incidentType)) {
			whereClause.append(" and obj.incidentType.id=" + incidentType);
		}
      if (StringUtils.isNotEmpty(injuryStatus)) {
      	String injuryStatusClause = " and obj.injuryStatus";
      	if (searchNotInInjuryStatus) {
      		injuryStatusClause += " not";
      	}
      	injuryStatusClause += " in (";
			whereClause.append(injuryStatusClause + injuryStatus + ")");
		}
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.driverCompany.name asc, obj.driverTerminal.name asc, obj.driver.fullName asc, obj.incidentDate asc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<Injury> injuryList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		populateDriverDetails(injuryList);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("driver", driver);
		
		String incidentDateRange = "-";
		if (StringUtils.isNotEmpty(incidentDateFrom) && StringUtils.isNotEmpty(incidentDateTo)) {
			incidentDateRange = incidentDateFrom + " - " + incidentDateTo;
		} 
		params.put("incidentDateRange", incidentDateRange);
		
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("data", injuryList);
		datas.put("params", params);
		     
		return datas;
	}
	
	private void populateDriverDetails(List<Injury> injuryList) {
		if (injuryList == null || injuryList.isEmpty()) {
			return;
		}
		
		for (Injury anInjury : injuryList) {
			if (anInjury.getReturnToWorkDate() != null) {
				anInjury.setWorking("Yes");
			} else {
				anInjury.setWorking("No");
			}
			
			Driver driver = anInjury.getDriver();
			if (driver.getStatus() == 1) {
				anInjury.setEmployed("Yes");
			} else {
				anInjury.setEmployed("No");
			}
		}
	}
	
	private Map<String, Object> generateNotReportedData(SearchCriteria searchCriteria, HttpServletRequest request) {
		Map<String, Object> datas = performSearch(searchCriteria, String.valueOf(Injury.INJURY_STATUS_NOT_REPORTED), false); 
		return datas;
	}
	
	private Map<String, Object> generateReportedData(SearchCriteria searchCriteria, HttpServletRequest request) {
		Map<String, Object> datas = performSearch(searchCriteria, String.valueOf(Injury.INJURY_STATUS_NOT_REPORTED), true); 
		return datas;
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/notReportedSearch.do")
	public String notReportedSearch(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> imagesMap = new HashMap<String, Object>();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		populateSearchCriteria(request, request.getParameterMap());
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(1000);
		criteria.setPage(0);
		
		Map<String, Object> datas = generateNotReportedData(criteria, request);
		List<Injury> injuryList = (List<Injury>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String type = "html";
		response.setContentType(MimeUtil.getContentType(type));
		
		String reportName = "injuriesNotReportedReport";
		try {
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(reportName,
					injuryList, params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return getUrlContext() + "/notReportedReport/"+type;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			
			return "error";
		}
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/notReportedExport.do")
	public String notReportedExport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true, value = "type") String type) {
		Map<String, Object> imagesMap = new HashMap<String, Object>();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
		
		Map<String, Object> datas = generateNotReportedData(criteria, request);
		List<Injury> injuryList = (List<Injury>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String reportName = "injuriesNotReportedReport";
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.generateStaticReport(reportName,
					injuryList, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
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
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/reportedSearch.do")
	public String reportedSearch(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> imagesMap = new HashMap<String, Object>();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		populateSearchCriteria(request, request.getParameterMap());
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(1000);
		criteria.setPage(0);
		
		Map<String, Object> datas = generateReportedData(criteria, request);
		List<Injury> injuryList = (List<Injury>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String type = "html";
		response.setContentType(MimeUtil.getContentType(type));
		
		String reportName = "injuriesReportedReport";
		try {
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(reportName,
					injuryList, params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return getUrlContext() + "/reportedReport/"+type;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			
			return "error";
		}
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/reportedExport.do")
	public String reportedExport(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true, value = "type") String type) {
		Map<String, Object> imagesMap = new HashMap<String, Object>();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
		
		Map<String, Object> datas = generateReportedData(criteria, request);
		List<Injury> injuryList = (List<Injury>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String reportName = "injuriesReportedReport";
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.generateStaticReport(reportName,
					injuryList, params, type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
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
}
