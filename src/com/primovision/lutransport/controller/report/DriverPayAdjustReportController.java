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

import com.primovision.lutransport.model.ChangedTicket;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;

import com.primovision.lutransport.service.DynamicReportService;

@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/admin/driverpayadjust/reports")
public class DriverPayAdjustReportController extends BaseController {
	@Autowired
	private DynamicReportService dynamicReportService;

	public void setDynamicReportService(DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	public DriverPayAdjustReportController() {
		setUrlContext("admin/driverpayadjust/reports");
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

		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		
		String query = "select distinct(obj.fullName) from Driver obj order by obj.fullName";
		model.addAttribute("driverNames", genericDAO.executeSimpleQuery(query));
		
		return getUrlContext() + "/driverPayAdjustReport";
	}
	
	private Map<String, Object> performSearch(SearchCriteria criteria, String overrideInjuryStatus,
			Boolean searchNotInInjuryStatus) {
		String driverCompany = (String) criteria.getSearchMap().get("driverCompany");
		String terminal = (String) criteria.getSearchMap().get("terminal");
		
		String driver = (String) criteria.getSearchMap().get("driver");
		
		String checkDateFrom = (String) criteria.getSearchMap().get("checkDateFrom");
		String checkDateTo = (String) criteria.getSearchMap().get("checkDateTo");
		
		StringBuffer query = new StringBuffer("select obj from ChangedTicket obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from ChangedTicket obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		String companyName = StringUtils.EMPTY;
		if (StringUtils.isNotEmpty(driverCompany)) {
			companyName = retrieveLoation(driverCompany).getName();
			whereClause.append(" and obj.driverCompany.id=" + driverCompany);
		}
		
		String terminalName = StringUtils.EMPTY;
		if (StringUtils.isNotEmpty(terminal)) {
			terminalName = retrieveLoation(terminal).getName();
			whereClause.append(" and obj.terminal.id=" + terminal);
		}
		
		if (StringUtils.isNotEmpty(driver)) {
			whereClause.append(" and obj.driver.fullName='" + driver + "'");
		}
		
	   if (StringUtils.isNotEmpty(checkDateFrom)){
        	try {
        		whereClause.append(" and obj.newPayRollBatch >='"+sdf.format(dateFormat.parse(checkDateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      if (StringUtils.isNotEmpty(checkDateTo)){
	     	try {
	     		whereClause.append(" and obj.newPayRollBatch <='"+sdf.format(dateFormat.parse(checkDateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.driverCompany.name asc, obj.terminal.name asc, obj.driver.fullName asc, obj.newPayRollBatch desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<ChangedTicket> changedTicketList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("driver", driver);
		params.put("company", companyName);
		params.put("terminal", terminalName);
		
		String checkDateRange = "-";
		if (StringUtils.isNotEmpty(checkDateFrom) && StringUtils.isNotEmpty(checkDateTo)) {
			checkDateRange = checkDateFrom + " - " + checkDateTo;
		} 
		params.put("checkDateRange", checkDateRange);
		
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("data", changedTicketList);
		datas.put("params", params);
		     
		return datas;
	}
	
	private Location retrieveLoation(String id) {
		return genericDAO.getById(Location.class, Long.valueOf(id));
	}
	
	private Map<String, Object> generateData(SearchCriteria searchCriteria, HttpServletRequest request) {
		Map<String, Object> datas = performSearch(searchCriteria, null, null); 
		return datas;
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> imagesMap = new HashMap<String, Object>();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		populateSearchCriteria(request, request.getParameterMap());
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(1000);
		criteria.setPage(0);
		
		Map<String, Object> datas = generateData(criteria, request);
		List<ChangedTicket> changedTicketList = (List<ChangedTicket>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String type = "html";
		response.setContentType(MimeUtil.getContentType(type));
		
		String reportName = "driverPayAdjustReport";
		try {
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(reportName,
					changedTicketList, params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return getUrlContext() + "/driverPayAdjustReport/"+type;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			
			return "error";
		}
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public String export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true, value = "type") String type) {
		Map<String, Object> imagesMap = new HashMap<String, Object>();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
		
		Map<String, Object> datas = generateData(criteria, request);
		List<ChangedTicket> changedTicketList = (List<ChangedTicket>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String reportName = "driverPayAdjustReport";
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.generateStaticReport(reportName,
					changedTicketList, params, type, request);
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
