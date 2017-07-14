package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.report.ReportController;

import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.WMInvoice;
import com.primovision.lutransport.model.WMInvoiceVerification;

@Controller
@RequestMapping("/admin/wmInvoiceVerification")
public class WMInvoiceVerificationController extends ReportController<WMInvoiceVerification> {
	public WMInvoiceVerificationController() {
		setUrlContext("admin/wmInvoiceVerification");
		setReportName("wmInvoiceVerification");
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String start(ModelMap model, HttpServletRequest request) {
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (searchCriteria != null) {
			if (searchCriteria.getSearchMap() != null) {
				searchCriteria.getSearchMap().clear();
			}
		}
		
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("type", 1);
		model.addAttribute("origins", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
		criterias.put("name", "Grows");
		criterias.put("type", 2);
		Location grows = genericDAO.getByCriteria(Location.class, criterias);
		
		criterias.clear();
		criterias.put("type", 2);
		criterias.put("id!", grows.getId());
		model.addAttribute("destinations", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
		return urlContext + "/verification";
	}
	
	@Override
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "jrxml") String jrxml) {
		Map imagesMap = new HashMap();
	   request.getSession().setAttribute("IMAGES_MAP", imagesMap);
	   
	   populateSearchCriteria(request, request.getParameterMap());
	   SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
	
	   Map<String, Object> dataMap = generateData(searchCriteria, request);
	   Map<String, Object> params = (Map<String, Object>) dataMap.get("params");
	   List<WMInvoiceVerification> wmInvoiceVerificationList = (List<WMInvoiceVerification>) dataMap.get("data");
      
      ByteArrayOutputStream out = null;
      try {
	 		if (StringUtils.isEmpty(type)) {
				type = "html";
	 		}
			response.setContentType(MimeUtil.getContentType(type));
			
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition", "attachment;filename=" + getReportName() + "." + type);
			}
			
			out = dynamicReportService.generateStaticReport(getReportName(), wmInvoiceVerificationList, params, 
						type, request);
			out.writeTo(response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			return "error";
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
	
	private List<Ticket> determineMissingTickets(List<Ticket> tickets, List<WMInvoice> wmInvoiceList) {
		List<Ticket> missingTickets = new ArrayList<Ticket>();
		if (tickets == null || tickets.isEmpty()
				|| wmInvoiceList == null || wmInvoiceList.isEmpty()) {
			return missingTickets;
		}
		
		String searchKey = StringUtils.EMPTY;
		
		Map<String, WMInvoice> wmInvoiceMap = new HashMap<String, WMInvoice>();
		for (WMInvoice anWMInvoice : wmInvoiceList) {
			searchKey = anWMInvoice.getTicket() + "|" + anWMInvoice.getOrigin() + "|" + anWMInvoice.getDestination();
			wmInvoiceMap.put(searchKey, anWMInvoice);
		}
		
		for (Ticket aTicket : tickets) {
			searchKey = aTicket.getDestinationTicket() + "|" + aTicket.getOrigin() + "|" + aTicket.getDestination();
			if (!wmInvoiceMap.containsKey(searchKey)) {
				searchKey = aTicket.getOriginTicket() + "|" + aTicket.getOrigin() + "|" + aTicket.getDestination();
				if (!wmInvoiceMap.containsKey(searchKey)) {
					missingTickets.add(aTicket);
				}
			}
		}
		
		return missingTickets;
	}
	
	private void map(List<WMInvoiceVerification> wmInvoiceVerificationList, List<Ticket> tickets) {
		if (tickets == null || tickets.isEmpty()) {
			return;
		}
		
		for (Ticket aTicket : tickets) {
			WMInvoiceVerification aWMInvoiceVerification = new WMInvoiceVerification();
			map(aWMInvoiceVerification, aTicket);
		}
	}
	
	private void map(WMInvoiceVerification aWMInvoiceVerification, Ticket aTticket) {
		if (aTticket == null) {
			return;
		}
		
		aWMInvoiceVerification.setOriginTicket(aTticket.getOriginTicket());
		aWMInvoiceVerification.setDestinationTicket(aTticket.getDestinationTicket());
		aWMInvoiceVerification.setOrigin(aTticket.getOrigin().getName());
		aWMInvoiceVerification.setDestination(aTticket.getDestination().getName());
		aWMInvoiceVerification.setWmOrigin(aTticket.getOrigin().getLongName());
		aWMInvoiceVerification.setWmDestination(aTticket.getDestination().getLongName());
		aWMInvoiceVerification.setLoadDate(aTticket.getLoadDate());
		aWMInvoiceVerification.setUnloadDate(aTticket.getUnloadDate());
	}
	
	private String retrieveLocationName(String locationIdStr) {
		return retrieveLocationName(Long.valueOf(locationIdStr));
	}
	
	private String retrieveLocationName(Long locationId) {
		Location location = genericDAO.getById(Location.class, locationId);
		return (location == null) ? StringUtils.EMPTY : location.getName();
	}
	
	@Override
	protected Map<String, Object> generateData(SearchCriteria searchCriteria, HttpServletRequest request) {
		String fromBatchDate = (String) searchCriteria.getSearchMap().get("fromBatchDate");
      String toBatchDate = (String)searchCriteria.getSearchMap().get("toBatchDate");
      String fromLoadDate = (String) searchCriteria.getSearchMap().get("fromLoadDate");
      String toLoadDate = (String) searchCriteria.getSearchMap().get("toLoadDate");
      String fromUnloadDate = (String) searchCriteria.getSearchMap().get("fromUnloadDate");
      String toUnloadDate = (String) searchCriteria.getSearchMap().get("toUnloadDate");
        
      fromBatchDate = ReportDateUtil.getFromDate(fromBatchDate);
      toBatchDate = ReportDateUtil.getToDate(toBatchDate);
      fromLoadDate = ReportDateUtil.getFromDate(fromLoadDate);
      toLoadDate	= ReportDateUtil.getToDate(toLoadDate);
      fromUnloadDate = ReportDateUtil.getFromDate(fromUnloadDate);
      toUnloadDate	= ReportDateUtil.getToDate(toUnloadDate);

      String origin = (String) searchCriteria.getSearchMap().get("origin");
      String destination = (String) searchCriteria.getSearchMap().get("destination");

      StringBuffer ticketQuery = new StringBuffer("select obj from Ticket obj where obj.status=1 and obj.ticketStatus=1");
      StringBuffer wmInvoiceQuery = new StringBuffer("select obj from WMInvoice obj where 1=1");
      
      if (StringUtils.isNotEmpty(fromBatchDate)) {
      	ticketQuery.append(" and obj.billBatch>='").append(fromBatchDate + "'");				
      }
      if (StringUtils.isNotEmpty(toBatchDate)) {
      	ticketQuery.append(" and obj.billBatch<='").append(toBatchDate + "'");				
      }
      if (StringUtils.isNotEmpty(fromLoadDate) && StringUtils.isNotEmpty(toLoadDate)) {
      	ticketQuery.append(" and obj.loadDate between '" + fromLoadDate + "' and '" + toLoadDate + "'");
      }
      
      String dateRange = StringUtils.EMPTY;
      if (StringUtils.isNotEmpty(fromUnloadDate) && StringUtils.isNotEmpty(toUnloadDate)) {
      	dateRange = fromUnloadDate + " - " + toUnloadDate;
      	
      	ticketQuery.append(" and obj.unloadDate between '" + fromUnloadDate + "' and '" + toUnloadDate + "'");
      	wmInvoiceQuery.append(" and obj.txnDate between '" + fromUnloadDate + "' and '" + toUnloadDate + "'");
      }
      
      if (StringUtils.isNotEmpty(origin)) {
      	ticketQuery.append(" and obj.origin=").append(origin);
      	wmInvoiceQuery.append(" and obj.origin=" + origin);
      }
      if (StringUtils.isNotEmpty(destination)) {
			if (destination.equalsIgnoreCase("91")) {
				Map<String, Object> criterias = new HashMap<String, Object>();
				criterias.put("name", "Grows");
				criterias.put("type", 2);
				Location grows = genericDAO.getByCriteria(Location.class, criterias);
				criterias.clear();
				criterias.put("name", "Tullytown");
				criterias.put("type", 2);
				Location tullyTown= genericDAO.getByCriteria(Location.class, criterias);
				
				ticketQuery.append(" and obj.destination in("+grows.getId()+","+tullyTown.getId()+")");
				wmInvoiceQuery.append(" and obj.destination in("+grows.getId()+","+tullyTown.getId()+")");
			} else {
				ticketQuery.append(" and obj.destination=").append(destination);
				wmInvoiceQuery.append(" and obj.destination=" + destination);
			}
      }
     
      ticketQuery.append(" order by obj.billBatch desc");
      wmInvoiceQuery.append(" order by obj.txnDate desc");
      
      List<Ticket> tickets = genericDAO.executeSimpleQuery(ticketQuery.toString());
      List<WMInvoice> wmInvoiceList = genericDAO.executeSimpleQuery(wmInvoiceQuery.toString());
      
      List<Ticket> missingTickets = determineMissingTickets(tickets, wmInvoiceList);
      List<WMInvoiceVerification> wmInvoiceVerificationList = new ArrayList<WMInvoiceVerification>();
      map(wmInvoiceVerificationList, missingTickets);
      
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("origin", (StringUtils.isNotEmpty(origin) ? retrieveLocationName(origin) : StringUtils.EMPTY));
      params.put("destination", (StringUtils.isNotEmpty(destination) ? retrieveLocationName(destination) : StringUtils.EMPTY));
      params.put("dateRane", dateRange);
      
      Map<String, Object> dataMap = new HashMap<String, Object>();
      dataMap.put("params", params);
      dataMap.put("data", wmInvoiceVerificationList);
      
      return dataMap;
	}
	
	@Override
	protected String processAjaxRequest(HttpServletRequest request, String action, Model model) {
		if("findDestinations".equalsIgnoreCase(action)) {
			String origin = request.getParameter("origin");
			if(StringUtils.isEmpty(origin)) {
				return StringUtils.EMPTY;
			}
			
			List<BillingRate> billing = genericDAO.executeSimpleQuery(
					"select obj from BillingRate obj where obj.transferStation="+Long.parseLong(origin)
					+ " group by landfill order by landfill.name");
			List<Location> destinations = new ArrayList<Location>();
			for(BillingRate bill : billing) {
				destinations.add(bill.getLandfill());
			}
			
			Gson gson = new Gson();
			return gson.toJson(destinations);
		}
		
		if ("findOrigins".equalsIgnoreCase(action)) {
			String destination = request.getParameter("destination");
			if(StringUtils.isEmpty(destination)) {
				return StringUtils.EMPTY;
			}
			
			List<BillingRate> billing = genericDAO.executeSimpleQuery(
					"select obj from BillingRate obj where obj.landfill="+Long.parseLong(destination)
					+ " group by transferStation order by transferStation.name");
			List<Location> origins = new ArrayList<Location>();
			for(BillingRate bill : billing){
				origins.add(bill.getTransferStation());
			}
			
			Gson gson = new Gson();
			return gson.toJson(origins);
		}
		
		return StringUtils.EMPTY;
	}
}
