package com.primovision.lutransport.controller.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.propertyeditors.CustomDateEditor;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.WMTicket;

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
		
		List<WMTicket> wmTicketList = performSearch(criteria);
		model.addAttribute("list", wmTicketList);
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		List<WMTicket> wmTicketList = performSearch(criteria);
		model.addAttribute("list", wmTicketList);
		
		return urlContext + "/list";
	}
	
	private List<WMTicket> performSearch(SearchCriteria criteria) {
		String origin = (String) criteria.getSearchMap().get("origin");
		String destination = (String) criteria.getSearchMap().get("destination");
		String ticket = (String) criteria.getSearchMap().get("ticket");
		String processingStatus = (String) criteria.getSearchMap().get("processingStatus");	
		String dateFrom = (String) criteria.getSearchMap().get("dateFrom");
		String dateTo = (String) criteria.getSearchMap().get("dateTo");
		
		StringBuffer query = new StringBuffer("select obj from WMTicket obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from WMTicket obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(origin)) {
			whereClause.append(" and obj.origin=" + origin);
		}
		if (StringUtils.isNotEmpty(destination)) {
			whereClause.append(" and obj.destination=" + destination);
		}
		if (StringUtils.isNotEmpty(ticket)) {
			whereClause.append(" and obj.ticket=" + ticket);
		}
		if (StringUtils.isNotEmpty(processingStatus)) {
			whereClause.append(" and obj.processingStatus=" + processingStatus);
		}
	   if (StringUtils.isNotEmpty(dateFrom)){
        	try {
        		whereClause.append(" and obj.txnDate >='"+sdf.format(dateFormat.parse(dateFrom))+"'");
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
		
		return wmTicketList;
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
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
		criterias.put("id!",91l);
		model.addAttribute("destinations", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	
		criterias.clear();
		criterias.put("dataType", "WMTICKET_PROCESSING_STATUS");
		//criterias.put("dataValue", "1,2,3,4");
		model.addAttribute("processingStatuses", genericDAO.findByCriteria(StaticData.class, criterias, "dataText", false));
	}
}
