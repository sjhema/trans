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

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.service.DynamicReportService;

@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/admin/ticket/reports")
public class TicketReportController extends BaseController {
	@Autowired
	private DynamicReportService dynamicReportService;

	public void setDynamicReportService(DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	public TicketReportController() {
		setUrlContext("admin/ticket/reports");
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
		
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
		criterias.clear();
		model.addAttribute("subcontractors", genericDAO.findByCriteria(SubContractor.class,	criterias, "name", false));
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 order by obj.unit"));
		model.addAttribute("trailers", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=2 order by obj.unit"));
		model.addAttribute("operators", genericDAO.findByCriteria(User.class, criterias, "name", false));
		
		criterias.clear();
		criterias.put("type", 1);
		model.addAttribute("origins", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
		criterias.clear();
		criterias.put("type", 2);
		criterias.put("id!", 91l);
		model.addAttribute("destinations", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	
		String query = "select distinct(obj.fullName) from Driver obj order by obj.fullName";
		model.addAttribute("driverNames", genericDAO.executeSimpleQuery(query));
		
		return getUrlContext() + "/ticketReport";
	}
	
	private Map<String, Object> performSearch(SearchCriteria criteria, String overrideAccidentStatus,
			Boolean searchNotInAccidentStatus) {
		String driverCompany = (String) criteria.getSearchMap().get("driverCompany");
		String terminal = (String) criteria.getSearchMap().get("terminal");
		String subcontractor = (String) criteria.getSearchMap().get("subcontractor");
		
		String driver = (String) criteria.getSearchMap().get("driver");
		
		String origin = (String) criteria.getSearchMap().get("origin");
		String destination = (String) criteria.getSearchMap().get("destination");
		
		String originTicket = (String) criteria.getSearchMap().get("originTicket");
		String destinationTicket = (String) criteria.getSearchMap().get("destinationTicket");
		
		String payrollBatchDateFrom = (String) criteria.getSearchMap().get("payrollBatchDateFrom");
		String payrollBatchDateTo = (String) criteria.getSearchMap().get("payrollBatchDateTo");
		
		String billBatchDateFrom = (String) criteria.getSearchMap().get("billBatchDateFrom");
		String billBatchDateTo = (String) criteria.getSearchMap().get("billBatchDateTo");
		
		String loadDateFrom = (String) criteria.getSearchMap().get("loadDateFrom");
		String loadDateTo = (String) criteria.getSearchMap().get("loadDateTo");
		
		String unloadDateFrom = (String) criteria.getSearchMap().get("unloadDateFrom");
		String unloadDateTo = (String) criteria.getSearchMap().get("unloadDateTo");
		
		String ticketStatus = (String) criteria.getSearchMap().get("ticketStatus");
		String processStatus = (String) criteria.getSearchMap().get("processStatus");
		String payrollPending = (String) criteria.getSearchMap().get("payrollPending");
		
		String enteredBy = (String) criteria.getSearchMap().get("enteredBy");
		
		StringBuffer query = new StringBuffer("select obj from Ticket obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from Ticket obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(driverCompany)) {
			whereClause.append(" and obj.driverCompany.id=" + driverCompany);
		}
		if (StringUtils.isNotEmpty(terminal)) {
			whereClause.append(" and obj.terminal.id=" + terminal);
		}
		if (StringUtils.isNotEmpty(subcontractor)) {
			whereClause.append(" and obj.subcontractor.id=" + subcontractor);
		}
		if (StringUtils.isNotEmpty(driver)) {
			whereClause.append(" and obj.driver.fullName='" + driver + "'");
		}
		if (StringUtils.isNotEmpty(origin)) {
			whereClause.append(" and obj.origin.id=" + origin);
		}
		if (StringUtils.isNotEmpty(destination)) {
			whereClause.append(" and obj.destination.id=" + destination);
		}
		if (StringUtils.isNotEmpty(originTicket)) {
			whereClause.append(" and obj.originTicket=" + originTicket);
		}
		if (StringUtils.isNotEmpty(destinationTicket)) {
			whereClause.append(" and obj.destinationTicket=" + destinationTicket);
		}
		
	   if (StringUtils.isNotEmpty(payrollBatchDateFrom)){
        	try {
        		whereClause.append(" and obj.payRollBatch >='"+sdf.format(dateFormat.parse(payrollBatchDateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      if (StringUtils.isNotEmpty(payrollBatchDateTo)){
	     	try {
	     		whereClause.append(" and obj.payRollBatch <='"+sdf.format(dateFormat.parse(payrollBatchDateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      
      if (StringUtils.isNotEmpty(billBatchDateFrom)){
        	try {
        		whereClause.append(" and obj.billBatch >='"+sdf.format(dateFormat.parse(billBatchDateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      if (StringUtils.isNotEmpty(billBatchDateTo)){
	     	try {
	     		whereClause.append(" and obj.billBatch <='"+sdf.format(dateFormat.parse(billBatchDateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      
      if (StringUtils.isNotEmpty(loadDateFrom)){
        	try {
        		whereClause.append(" and obj.loadDate >='"+sdf.format(dateFormat.parse(loadDateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      if (StringUtils.isNotEmpty(loadDateTo)){
	     	try {
	     		whereClause.append(" and obj.loadDate <='"+sdf.format(dateFormat.parse(loadDateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      
      if (StringUtils.isNotEmpty(unloadDateFrom)){
        	try {
        		whereClause.append(" and obj.unloadDate >='"+sdf.format(dateFormat.parse(unloadDateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      if (StringUtils.isNotEmpty(unloadDateTo)){
	     	try {
	     		whereClause.append(" and obj.unloadDate <='"+sdf.format(dateFormat.parse(unloadDateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      
		if (StringUtils.isNotEmpty(ticketStatus)) {
		  whereClause.append(" and obj.status=" + ticketStatus);
		}
		if (StringUtils.isNotEmpty(processStatus)) {
			whereClause.append(" and obj.ticketStatus=" + processStatus);
		}
		if (StringUtils.isNotEmpty(payrollPending)) {
			whereClause.append(" and obj.payRollStatus=" + payrollPending);
		}
		
		if (StringUtils.isNotEmpty(enteredBy)) {
			whereClause.append(" and obj.createdBy=" + enteredBy);
		}
     
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.id desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<Ticket> ticketList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("data", ticketList);
		datas.put("params", params);
		     
		return datas;
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
		criteria.setPageSize(500);
		criteria.setPage(0);
		
		Map<String, Object> datas = generateData(criteria, request);
		List<Ticket> ticketList = (List<Ticket>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String type = "html";
		response.setContentType(MimeUtil.getContentType(type));
		
		String reportName = "ticketReport";
		try {
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile(reportName,
					ticketList, params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return getUrlContext() + "/"+type;
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
		criteria.setPageSize(500);
		criteria.setPage(0);
		
		Map<String, Object> datas = generateData(criteria, request);
		List<Ticket> ticketList = (List<Ticket>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String reportName = "ticketReport";
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename=" + reportName + "." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.generateStaticReport(reportName, ticketList, params, type, request);
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
