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
import com.primovision.lutransport.model.WMInvoice;
import com.primovision.lutransport.model.report.WMInvoiceReport;

@Controller
@RequestMapping("/admin/wmInvoice")
public class WMInvoiceController extends CRUDController<WMInvoice> {
	public WMInvoiceController() {
		setUrlContext("admin/wmInvoice");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		List<WMInvoiceReport> wmInvoiceReporttList = performSearch(criteria);
		model.addAttribute("list", wmInvoiceReporttList);
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		List<WMInvoiceReport> wmInvoiceReporttList = performSearch(criteria);
		model.addAttribute("list", wmInvoiceReporttList);
		
		return urlContext + "/list";
	}
	
	private List<WMInvoiceReport> performSearch(SearchCriteria criteria) {
		String origin = (String) criteria.getSearchMap().get("origin");
		String destination = (String) criteria.getSearchMap().get("destination");
		String ticket = (String) criteria.getSearchMap().get("ticket");
		String dateFrom = (String) criteria.getSearchMap().get("dateFrom");
		String dateTo = (String) criteria.getSearchMap().get("dateTo");
		
		StringBuffer query = new StringBuffer("select obj from WMInvoice obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from WMInvoice obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(origin)) {
			whereClause.append(" and obj.origin=" + origin);
		}
		
		if (StringUtils.isNotEmpty(destination)) {
			whereClause.append(" and obj.destination=" + destination);
		}
		
		String searchDate = "obj.txnDate";
		if (StringUtils.isNotEmpty(dateFrom)) {
        	try {
        		whereClause.append(" and " + searchDate + " >='"+sdf.format(dateFormat.parse(dateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      if (StringUtils.isNotEmpty(dateTo)){
	     	try {
	     		whereClause.append(" and " + searchDate + " <='"+sdf.format(dateFormat.parse(dateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if (StringUtils.isNotEmpty(ticket)) {
			whereClause.append(" and obj.ticket=" + ticket);
		}
		
      query.append(whereClause);
      countQuery.append(whereClause);
     
      query.append(" order by " + searchDate + " desc, obj.createdAt desc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<WMInvoice> wmInvoiceList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		List<WMInvoiceReport> wmInvoiceReportList = new ArrayList<WMInvoiceReport>();
		map(wmInvoiceReportList, wmInvoiceList);
		
		return wmInvoiceReportList;
	}
	
	private void map(List<WMInvoiceReport> wmInvoiceReportList, List<WMInvoice>  wmInvoiceList) {
		if (wmInvoiceList == null || wmInvoiceList.isEmpty()) {
			return;
		}
		
		for (WMInvoice aWMInvoice : wmInvoiceList) {
			WMInvoiceReport aWMInvoiceReport = new WMInvoiceReport();
			map(aWMInvoiceReport, aWMInvoice);
			
			wmInvoiceReportList.add(aWMInvoiceReport);
		}
	}
	
	private void map(WMInvoiceReport aWMInvoiceReport, WMInvoice aWMInvoice) {
		if (aWMInvoice == null) {
			return;
		}
		
		aWMInvoiceReport.setTicket(aWMInvoice.getTicket());
		aWMInvoiceReport.setReissuedTicket(aWMInvoice.getReissuedTicket());
		aWMInvoiceReport.setTxnDate(aWMInvoice.getTxnDate());
		
		aWMInvoiceReport.setTimeIn(aWMInvoice.getTimeIn());
		aWMInvoiceReport.setTimeOut(aWMInvoice.getTimeOut());
		
		aWMInvoiceReport.setGross(aWMInvoice.getGross());
		aWMInvoiceReport.setTare(aWMInvoice.getTare());
		aWMInvoiceReport.setNet(aWMInvoice.getNet());
		aWMInvoiceReport.setAmount(aWMInvoice.getAmount());
		aWMInvoiceReport.setFsc(aWMInvoice.getFsc());
		aWMInvoiceReport.setTotalAmount(aWMInvoice.getTotalAmount());
		
		aWMInvoiceReport.setWmVehicle(aWMInvoice.getWmVehicle());
		aWMInvoiceReport.setWmTrailer(aWMInvoice.getWmTrailer());
		
		aWMInvoiceReport.setWmOrigin(aWMInvoice.getWmOrigin());
		aWMInvoiceReport.setWmDestination(aWMInvoice.getWmDestination());
		
		if (aWMInvoice.getOrigin() != null) {
			aWMInvoiceReport.setOriginName(aWMInvoice.getOrigin().getName());
		}
		if (aWMInvoice.getDestination() != null) {
			aWMInvoiceReport.setDestinationName(aWMInvoice.getDestination().getName());
		}
		
		aWMInvoiceReport.setWmStatus(aWMInvoice.getWmStatus());
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
	}
	
	private List<WMInvoiceReport> searchForExport(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int origPage = criteria.getPage();
		
		criteria.setPage(0);
		criteria.setPageSize(100000);
		
		List<WMInvoiceReport> wmInvoiceReportList = performSearch(criteria);
		
		criteria.setPage(origPage);
		criteria.setPageSize(25);
		
		return wmInvoiceReportList;
	}
	
	@Override
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		List<WMInvoiceReport> wmInvoiceReportList = searchForExport(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		Map<String, Object> params = new HashMap<String, Object>();
		setParamsForExport(params, criteria);
		
		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html")) {
			response.setHeader("Content-Disposition", "attachment;filename=wmInvoiceReport." + type);
		}
		
		//List columnPropertyList = (List) request.getSession().getAttribute("columnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			/*out = dynamicReportService.exportReport(
						urlContext + "Report", type, getEntityClass(), wmInvoiceReportList,
						columnPropertyList, request);*/
			out = dynamicReportService.generateStaticReport("wmInvoice",
					wmInvoiceReportList, params, type, request);
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
	
	private String retrieveLocationName(Long locationId) {
		Location location = genericDAO.getById(Location.class, locationId);
		return (location == null) ? StringUtils.EMPTY : location.getName();
	}

	private void setParamsForExport(Map<String, Object> params, SearchCriteria criteria) {
		String origin = (String) criteria.getSearchMap().get("origin");
		String destination = (String) criteria.getSearchMap().get("destination");
		String dateFrom = (String) criteria.getSearchMap().get("dateFrom");
		String dateTo = (String) criteria.getSearchMap().get("dateTo");
		
		if (StringUtils.isNotEmpty(origin)) {
			params.put("origin", retrieveLocationName(Long.valueOf(origin)));
		}
		if (StringUtils.isNotEmpty(destination)) {
			params.put("destination", retrieveLocationName(Long.valueOf(destination)));
		}
	
		params.put("dateFrom", dateFrom);
		params.put("dateTo", dateTo);
	}
}
