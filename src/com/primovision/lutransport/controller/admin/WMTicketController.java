package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.propertyeditors.CustomDateEditor;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.CRUDController;

import com.primovision.lutransport.core.util.MimeUtil;

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.WMTicket;
import com.primovision.lutransport.model.report.WMTicketReport;

@Controller
@RequestMapping("/admin/wmTicket")
public class WMTicketController extends CRUDController<WMTicket> {
	public WMTicketController() {
		setUrlContext("admin/wmTicket");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
		//binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		List<WMTicketReport> wmTicketReporttList = performSearch(criteria);
		model.addAttribute("list", wmTicketReporttList);
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		List<WMTicketReport> wmTicketReportList = performSearch(criteria);
		model.addAttribute("list", wmTicketReportList);
		
		return urlContext + "/list";
	}
	
	private List<WMTicketReport> performSearch(SearchCriteria criteria) {
		String origin = (String) criteria.getSearchMap().get("origin");
		String destination = (String) criteria.getSearchMap().get("destination");
		String ticket = (String) criteria.getSearchMap().get("ticket");
		String ticketType = (String) criteria.getSearchMap().get("ticketType");
		String processingStatus = (String) criteria.getSearchMap().get("processingStatus");	
		String dateFrom = (String) criteria.getSearchMap().get("dateFrom");
		String dateTo = (String) criteria.getSearchMap().get("dateTo");
		
		boolean originSelected = false;
		
		StringBuffer query = new StringBuffer("select obj from WMTicket obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from WMTicket obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(origin)) {
			whereClause.append(" and obj.origin=" + origin);
			originSelected = true;
		}
		if (StringUtils.isNotEmpty(destination)) {
			whereClause.append(" and obj.destination=" + destination);
		}
		if (StringUtils.isNotEmpty(ticket)) {
			whereClause.append(" and obj.ticket=" + ticket);
		}
		if (StringUtils.isNotEmpty(ticketType)) {
			whereClause.append(" and obj.ticketType='" + ticketType + "'");
		}
		if (StringUtils.isNotEmpty(processingStatus)) {
			whereClause.append(" and obj.processingStatus=" + processingStatus);
		}
	   if (StringUtils.isNotEmpty(dateFrom)) {
	   	String searchDate = "obj.loadDate";
	   	if (!originSelected) {
	   		searchDate = "obj.unloadDate";
	   	}
        	try {
        		whereClause.append(" and " + searchDate + " >='"+sdf.format(dateFormat.parse(dateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      if (StringUtils.isNotEmpty(dateTo)){
	     	try {
	     		whereClause.append(" and obj.txnDate <='"+sdf.format(dateFormat.parse(dateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.txnDate desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<WMTicket> wmTicketList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		List<WMTicketReport> wmTicketReportList = new ArrayList<WMTicketReport>();
		map(wmTicketReportList, wmTicketList);
		
		return wmTicketReportList;
	}
	
	private void map(List<WMTicketReport> wmTicketReportList, List<WMTicket>  wmTicketList) {
		if (wmTicketList == null || wmTicketList.isEmpty()) {
			return;
		}
		
		for (WMTicket aWMTicket : wmTicketList) {
			WMTicketReport aWMTicketReport = new WMTicketReport();
			map(aWMTicketReport, aWMTicket);
			
			wmTicketReportList.add(aWMTicketReport);
		}
	}
	
	private void map(WMTicketReport aWMTicketReport, WMTicket aWMTicket) {
		if (aWMTicket == null) {
			return;
		}
		
		aWMTicketReport.setTicket(aWMTicket.getTicket());
		aWMTicketReport.setHaulingTicket(aWMTicket.getHaulingTicket());
		aWMTicketReport.setTxnDate(aWMTicket.getTxnDate());
		aWMTicketReport.setTicketType(aWMTicket.getTicketType());
		aWMTicketReport.setProcessingStatus(aWMTicket.getProcessingStatus());
		
		aWMTicketReport.setTimeIn(aWMTicket.getTimeIn());
		aWMTicketReport.setTimeOut(aWMTicket.getTimeOut());
		
		aWMTicketReport.setGross(aWMTicket.getGross());
		aWMTicketReport.setTare(aWMTicket.getTare());
		aWMTicketReport.setNet(aWMTicket.getNet());
		aWMTicketReport.setTare(aWMTicket.getTare());
		
		aWMTicketReport.setWmVehicle(aWMTicket.getWmVehicle());
		aWMTicketReport.setWmTrailer(aWMTicket.getWmTrailer());
		
		aWMTicketReport.setWmDestination(aWMTicket.getWmDestination());
		
		if (aWMTicket.getVehicle() != null) {
			aWMTicketReport.setVehicle(aWMTicket.getVehicle().getUnit());
		}
		if (aWMTicket.getTrailer() != null) {
			aWMTicketReport.setTrailer(aWMTicket.getTrailer().getUnit());
		}
		
		if (aWMTicket.getOrigin() != null) {
			aWMTicketReport.setOriginName(aWMTicket.getOrigin().getName());
		}
		if (aWMTicket.getDestination() != null) {
			aWMTicketReport.setDestinationName(aWMTicket.getDestination().getName());
		}
		
		if (StringUtils.equals(WMTicket.ORIGIN_TICKET_TYPE, aWMTicket.getTicketType())) {
			aWMTicketReport.setLoadUnloadDate(aWMTicket.getLoadDate());
		} else {
			aWMTicketReport.setLoadUnloadDate(aWMTicket.getUnloadDate());
		}
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		criterias.clear();
		criterias.put("type", 1);
		model.addAttribute("origins", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
		criterias.clear();
		criterias.put("type", 2);
		criterias.put("id!", 91l);
		model.addAttribute("destinations", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	
		criterias.clear();
		criterias.put("dataType", "WMTICKET_PROCESSING_STATUS");
		//criterias.put("dataValue", "1,2,3,4");
		model.addAttribute("processingStatuses", genericDAO.findByCriteria(StaticData.class, criterias, "dataText", false));
	
		List<String> ticketTypeList = new ArrayList<String>();
		ticketTypeList.add("ORIGN");
		ticketTypeList.add("DESTN");
		model.addAttribute("ticketTypes", ticketTypeList);
	}
	
	private List<WMTicketReport> searchForExport(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int origPage = criteria.getPage();
		
		criteria.setPage(0);
		criteria.setPageSize(100000);
		
		List<WMTicketReport> wmTicketReportList = performSearch(criteria);
		
		criteria.setPage(origPage);
		criteria.setPageSize(25);
		
		return wmTicketReportList;
	}
	
	@Override
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html")) {
			response.setHeader("Content-Disposition", "attachment;filename=wmTicketReport." + type);
		}
		
		List<WMTicketReport> wmTicketReportList = searchForExport(model, request);
		Map<String, Object> params = new HashMap<String, Object>();
		
		//List columnPropertyList = (List) request.getSession().getAttribute("columnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			/*out = dynamicReportService.exportReport(
						urlContext + "Report", type, getEntityClass(), wmTicketReportList,
						columnPropertyList, request);*/
			out = dynamicReportService.generateStaticReport("wmTicket",
					wmTicketReportList, params, type, request);
			out.writeTo(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
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
}
