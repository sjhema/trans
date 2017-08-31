package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import com.primovision.lutransport.controller.report.ReportController;
import com.primovision.lutransport.core.tags.StaticDataUtil;
import com.primovision.lutransport.core.util.MimeUtil;
import com.primovision.lutransport.core.util.ReportDateUtil;

import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.WMInvoice;
import com.primovision.lutransport.model.WMInvoiceVerification;
import com.primovision.lutransport.model.report.Billing;
import com.primovision.lutransport.model.report.BillingWrapper;
import com.primovision.lutransport.service.ReportService;

@Controller
@RequestMapping("/admin/wmInvoiceVerification")
public class WMInvoiceVerificationController extends ReportController<WMInvoiceVerification> {
	@Autowired
	private ReportService reportService;
	
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	public WMInvoiceVerificationController() {
		setUrlContext("admin/wmInvoiceVerification");
		setReportName(WMInvoiceVerification.WM_INVOICE_MISSING_TICKETS_IN_WM_REPORT);
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
	   List dataList = (List) dataMap.get("data");
      
      ByteArrayOutputStream out = null;
      try {
	 		if (StringUtils.isEmpty(type)) {
				type = "html";
	 		}
			response.setContentType(MimeUtil.getContentType(type));
			
			if (!type.equals("html") && !(type.equals("print"))) {
				response.setHeader("Content-Disposition", "attachment;filename=" + getReportName() + "." + type);
			}
			
			out = dynamicReportService.generateStaticReport(getReportName(), dataList, params, 
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
	
	private List<Ticket> determineMatchingTickets(List<Ticket> tickets, List<WMInvoice> wmInvoiceList) {
		List<Ticket> matchingTickets = new ArrayList<Ticket>();
		if (tickets == null || tickets.isEmpty()) {
			return matchingTickets;
		}
		if (wmInvoiceList == null || wmInvoiceList.isEmpty()) {
			return matchingTickets;
		}
		
		String searchKey = StringUtils.EMPTY;
		
		Map<String, WMInvoice> wmInvoiceMap = new HashMap<String, WMInvoice>();
		for (WMInvoice anWMInvoice : wmInvoiceList) {
			searchKey = anWMInvoice.getTicket() + "|" + anWMInvoice.getOrigin().getName() + "|" + anWMInvoice.getDestination().getName();
			wmInvoiceMap.put(searchKey, anWMInvoice);
		}
		
		for (Ticket aTicket : tickets) {
			searchKey = aTicket.getDestinationTicket() + "|" + aTicket.getOrigin().getName() + "|" + aTicket.getDestination().getName();
			if (wmInvoiceMap.containsKey(searchKey)) {
				matchingTickets.add(aTicket);
			} else {
				searchKey = aTicket.getOriginTicket() + "|" + aTicket.getOrigin().getName() + "|" + aTicket.getDestination().getName();
				if (wmInvoiceMap.containsKey(searchKey)) {
					matchingTickets.add(aTicket);
				}
			}
		}
		
		return matchingTickets;
	}
	
	private List<Ticket> determineMissingTicketsInWM(List<Ticket> tickets, List<WMInvoice> wmInvoiceList) {
		List<Ticket> missingTickets = new ArrayList<Ticket>();
		if (tickets == null || tickets.isEmpty()) {
			return missingTickets;
		}
		if (wmInvoiceList == null || wmInvoiceList.isEmpty()) {
			return tickets;
		}
		
		String searchKey = StringUtils.EMPTY;
		
		Map<String, WMInvoice> wmInvoiceMap = new HashMap<String, WMInvoice>();
		for (WMInvoice anWMInvoice : wmInvoiceList) {
			searchKey = anWMInvoice.getTicket() + "|" + anWMInvoice.getOrigin().getName() + "|" + anWMInvoice.getDestination().getName();
			wmInvoiceMap.put(searchKey, anWMInvoice);
		}
		
		for (Ticket aTicket : tickets) {
			searchKey = aTicket.getDestinationTicket() + "|" + aTicket.getOrigin().getName() + "|" + aTicket.getDestination().getName();
			if (!wmInvoiceMap.containsKey(searchKey)) {
				searchKey = aTicket.getOriginTicket() + "|" + aTicket.getOrigin().getName() + "|" + aTicket.getDestination().getName();
				if (!wmInvoiceMap.containsKey(searchKey)) {
					missingTickets.add(aTicket);
				}
			}
		}
		
		return missingTickets;
	}
	
	private void sort(List<WMInvoiceVerification> wmInvoiceVerificationList, final boolean useWMAtrrib) {
		if (wmInvoiceVerificationList == null || wmInvoiceVerificationList.isEmpty()) {
			return;
		}
		
		Comparator<WMInvoiceVerification> originComparator = new Comparator<WMInvoiceVerification>() {
			@Override
			public int compare(WMInvoiceVerification o1, WMInvoiceVerification o2) {
				if (StringUtils.isEmpty(o1.getOrigin()) || StringUtils.isEmpty(o2.getOrigin())) {
					return 0;
				}
				return o1.getOrigin().compareTo(o2.getOrigin());
			}
		};
			
		Comparator<WMInvoiceVerification> destinationComparator = new Comparator<WMInvoiceVerification>() {
			@Override
			public int compare(WMInvoiceVerification o1, WMInvoiceVerification o2) {
				if (StringUtils.isEmpty(o1.getDestination()) || StringUtils.isEmpty(o2.getDestination())) {
					return 0;
				}
				return o1.getDestination().compareTo(o2.getDestination());
			}
		};
	        
		Comparator<WMInvoiceVerification> dateComparator = new Comparator<WMInvoiceVerification>() {
			@Override
			public int compare(WMInvoiceVerification o1, WMInvoiceVerification o2) {
				if (useWMAtrrib) {
					if (o1.getTxnDate() == null || o2.getTxnDate() == null) {
						return 0;
					}
					return o1.getTxnDate().compareTo(o2.getTxnDate());
				} else {
					if (o1.getLoadDate() == null || o2.getLoadDate() == null) {
						return 0;
					}
					return o1.getLoadDate().compareTo(o2.getLoadDate());
				}
			}
		};  
			
		Comparator<WMInvoiceVerification> ticketComparator = new Comparator<WMInvoiceVerification>() {
			@Override
			public int compare(WMInvoiceVerification o1, WMInvoiceVerification o2) {
				if (useWMAtrrib) {
					if (o1.getTicket() == null || o2.getTicket() == null) {
						return 0;
					}
					return o1.getTicket().compareTo(o2.getTicket());
				} else {
					if (o1.getOriginTicket() == null || o2.getOriginTicket() == null) {
						return 0;
					}
					return o1.getOriginTicket().compareTo(o2.getOriginTicket());
				}
			}
		};
		
		ComparatorChain chain = new ComparatorChain(); 
		chain.addComparator(originComparator);
		chain.addComparator(destinationComparator);			
		chain.addComparator(dateComparator);
		chain.addComparator(ticketComparator);
		
		Collections.sort(wmInvoiceVerificationList, chain);
	}
	
	private List<WMInvoice> determineMissingTickets(List<Ticket> tickets, List<WMInvoice> wmInvoiceList) {
		List<WMInvoice> missingTickets = new ArrayList<WMInvoice>();
		if (wmInvoiceList == null || wmInvoiceList.isEmpty()) {
			return missingTickets;
		}
		if (tickets == null || tickets.isEmpty()) {
			return wmInvoiceList;
		}
		
		String searchKey = StringUtils.EMPTY;
		
		Map<String, Ticket> ticketByOriginMap = new HashMap<String, Ticket>();
		for (Ticket aTicket : tickets) {
			searchKey = aTicket.getOriginTicket() + "|" + aTicket.getOrigin().getName() + "|" + aTicket.getDestination().getName();
			ticketByOriginMap.put(searchKey, aTicket);
		}
		
		Map<String, Ticket> ticketByDestinationMap = new HashMap<String, Ticket>();
		for (Ticket aTicket : tickets) {
			searchKey = aTicket.getDestinationTicket() + "|" + aTicket.getOrigin().getName() + "|" + aTicket.getDestination().getName();
			ticketByDestinationMap.put(searchKey, aTicket);
		}
		
		for (WMInvoice anWMInvoice : wmInvoiceList) {
			if (StringUtils.equalsIgnoreCase("VOID", anWMInvoice.getWmStatusCode())) {
				continue;
			}
			
			searchKey = anWMInvoice.getTicket() + "|" + anWMInvoice.getOrigin().getName() + "|" + anWMInvoice.getDestination().getName();
			
			Ticket matchingTicket = ticketByDestinationMap.get(searchKey);
			if (matchingTicket == null) {
				matchingTicket = ticketByOriginMap.get(searchKey);
			} 
			
			if (matchingTicket == null) {
				missingTickets.add(anWMInvoice);
			} else if (matchingTicket.getTicketStatus() == 0) { // HOLD
				anWMInvoice.setTicketStatus(getTicketStatusText(matchingTicket.getTicketStatus()));
				missingTickets.add(anWMInvoice);
			}
			
			/*if (!wmTicketByDestinationMap.containsKey(searchKey)) {
				if (!wmTicketByOriginMap.containsKey(searchKey)) {
					missingTickets.add(anWMInvoice);
				}
			}*/
		}
		
		return missingTickets;
	}
	
	private String getTicketStatusText(Integer ticketStatus) {
		if (ticketStatus == null) {
			return StringUtils.EMPTY;
		}
		return StaticDataUtil.getText("TICKET_STATUS", String.valueOf(ticketStatus));
	}
	
	private void map(List<WMInvoiceVerification> wmInvoiceVerificationList, List<Ticket> tickets) {
		if (tickets == null || tickets.isEmpty()) {
			return;
		}
		
		for (Ticket aTicket : tickets) {
			WMInvoiceVerification aWMInvoiceVerification = new WMInvoiceVerification();
			map(aWMInvoiceVerification, aTicket);
			wmInvoiceVerificationList.add(aWMInvoiceVerification);
		}
	}
	
	private void map(WMInvoiceVerification aWMInvoiceVerification, Ticket aTicket) {
		if (aTicket == null) {
			return;
		}
		
		aWMInvoiceVerification.setOriginTicket(aTicket.getOriginTicket());
		aWMInvoiceVerification.setDestinationTicket(aTicket.getDestinationTicket());
		
		aWMInvoiceVerification.setOriginId(aTicket.getOrigin() == null ? null : aTicket.getOrigin().getId());
		aWMInvoiceVerification.setDestinationId(aTicket.getDestination() == null ? null : aTicket.getDestination().getId());
		
		aWMInvoiceVerification.setOrigin(aTicket.getOrigin() == null ? StringUtils.EMPTY : aTicket.getOrigin().getName());
		aWMInvoiceVerification.setDestination(aTicket.getDestination() == null ? StringUtils.EMPTY : aTicket.getDestination().getName());
		
		aWMInvoiceVerification.setWmOrigin(aTicket.getOrigin() == null ? StringUtils.EMPTY : aTicket.getOrigin().getLongName());
		aWMInvoiceVerification.setWmDestination(aTicket.getDestination() == null ? StringUtils.EMPTY : aTicket.getDestination().getLongName());
		
		aWMInvoiceVerification.setLoadDate(aTicket.getLoadDate());
		aWMInvoiceVerification.setUnloadDate(aTicket.getUnloadDate());
		
		aWMInvoiceVerification.setTicketStatus(getTicketStatusText(aTicket.getTicketStatus()));
	}
	
	private void mapWMInvoice(List<WMInvoiceVerification> wmInvoiceVerificationList, List<WMInvoice> wmInvoiceList) {
		if (wmInvoiceList == null || wmInvoiceList.isEmpty()) {
			return;
		}
		
		for (WMInvoice anWMInvoice : wmInvoiceList) {
			WMInvoiceVerification aWMInvoiceVerification = new WMInvoiceVerification();
			map(aWMInvoiceVerification, anWMInvoice);
			wmInvoiceVerificationList.add(aWMInvoiceVerification);
		}
	}
	
	private void map(WMInvoiceVerification aWMInvoiceVerification, WMInvoice anWMInvoice) {
		if (anWMInvoice == null) {
			return;
		}
		
		aWMInvoiceVerification.setTicket(anWMInvoice.getTicket());
		
		aWMInvoiceVerification.setOrigin(anWMInvoice.getOrigin() == null ? StringUtils.EMPTY : anWMInvoice.getOrigin().getName());
		aWMInvoiceVerification.setDestination(anWMInvoice.getDestination() == null ? StringUtils.EMPTY : anWMInvoice.getDestination().getName());
		
		aWMInvoiceVerification.setOriginId(anWMInvoice.getOrigin() == null ? null : anWMInvoice.getOrigin().getId());
		aWMInvoiceVerification.setDestinationId(anWMInvoice.getDestination() == null ? null : anWMInvoice.getDestination().getId());
		
		aWMInvoiceVerification.setWmOrigin(anWMInvoice.getOrigin() == null ? StringUtils.EMPTY : anWMInvoice.getOrigin().getLongName());
		aWMInvoiceVerification.setWmDestination(anWMInvoice.getDestination() == null ? StringUtils.EMPTY : anWMInvoice.getDestination().getLongName());
		
		aWMInvoiceVerification.setTxnDate(anWMInvoice.getTxnDate());
		
		aWMInvoiceVerification.setTicketStatus(anWMInvoice.getTicketStatus());
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
		Map searchMap = searchCriteria.getSearchMap();
		
		String reportCtx = (String) searchMap.get("reportCtx");
		
		String fromBatchDate = (String) searchMap.get("fromBatchDate");
      String toBatchDate = (String) searchMap.get("toBatchDate");
      String fromLoadDate = (String) searchMap.get("fromLoadDate");
      String toLoadDate = (String) searchMap.get("toLoadDate");
      String fromUnloadDate = (String) searchMap.get("fromUnloadDate");
      String toUnloadDate = (String) searchMap.get("toUnloadDate");
        
      fromBatchDate = ReportDateUtil.getFromDate(fromBatchDate);
      toBatchDate = ReportDateUtil.getToDate(toBatchDate);
      fromLoadDate = ReportDateUtil.getFromDate(fromLoadDate);
      toLoadDate	= ReportDateUtil.getToDate(toLoadDate);
      fromUnloadDate = ReportDateUtil.getFromDate(fromUnloadDate);
      toUnloadDate	= ReportDateUtil.getToDate(toUnloadDate);

      String origin = (String) searchCriteria.getSearchMap().get("origin");
      String destination = (String) searchCriteria.getSearchMap().get("destination");

      StringBuffer ticketQuery = new StringBuffer("select obj from Ticket obj where obj.status=1");
      StringBuffer wmInvoiceQuery = new StringBuffer("select obj from WMInvoice obj where 1=1");
      
      if (StringUtils.equals(WMInvoiceVerification.WM_INVOICE_MISSING_TICKETS_IN_WM_REPORT, reportCtx)
      		|| StringUtils.equals(WMInvoiceVerification.WM_INVOICE_DISCREPANCY_REPORT, reportCtx)
      		|| StringUtils.equals(WMInvoiceVerification.WM_INVOICE_MATCHING_REPORT, reportCtx)) {
      	ticketQuery.append(" and obj.ticketStatus=1"); // Available
      } 
      
      if (StringUtils.isNotEmpty(fromBatchDate)) {
      	ticketQuery.append(" and obj.billBatch>='").append(fromBatchDate + "'");				
      }
      if (StringUtils.isNotEmpty(toBatchDate)) {
      	ticketQuery.append(" and obj.billBatch<='").append(toBatchDate + "'");				
      }
      
      String dateRange = StringUtils.EMPTY;
      if (StringUtils.isNotEmpty(fromLoadDate) && StringUtils.isNotEmpty(toLoadDate)) {
      	dateRange = determineDateRangeDisplay(fromLoadDate, toLoadDate);
      	
      	ticketQuery.append(" and obj.loadDate between '" + fromLoadDate + "' and '" + toLoadDate + "'");
      	wmInvoiceQuery.append(" and obj.txnDate between '" + fromLoadDate + "' and '" + toLoadDate + "'");
      }
      if (StringUtils.isNotEmpty(fromUnloadDate) && StringUtils.isNotEmpty(toUnloadDate)) {
      	dateRange = determineDateRangeDisplay(fromUnloadDate, toUnloadDate);
      	
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
      
      List<WMInvoiceVerification> wmInvoiceVerificationList = null;
      BillingWrapper billingWrapper = null;
      boolean billingData = false;
      if (StringUtils.equals(WMInvoiceVerification.WM_INVOICE_MISSING_TICKETS_IN_WM_REPORT, reportCtx)) {
      	setReportName(WMInvoiceVerification.WM_INVOICE_MISSING_TICKETS_IN_WM_REPORT);
      	wmInvoiceVerificationList = generateWMInvoiceMissingTicketsInWMData(tickets, wmInvoiceList);
      } else if (StringUtils.equals(WMInvoiceVerification.WM_INVOICE_MISSING_TICKETS_REPORT, reportCtx)) {
      	setReportName(WMInvoiceVerification.WM_INVOICE_MISSING_TICKETS_REPORT);
      	wmInvoiceVerificationList = generateWMInvoiceMissingTicketsData(tickets, wmInvoiceList);
      } else if (StringUtils.equals(WMInvoiceVerification.WM_INVOICE_MATCHING_REPORT, reportCtx)) {
      	setReportName(WMInvoiceVerification.WM_INVOICE_MATCHING_REPORT);
      	wmInvoiceVerificationList = generateWMInvoiceMatchingData(tickets, wmInvoiceList, origin, destination);
      } else if (StringUtils.equals(WMInvoiceVerification.WM_INVOICE_DISCREPANCY_REPORT, reportCtx)) {
      	setReportName(WMInvoiceVerification.WM_INVOICE_DISCREPANCY_REPORT);
      	billingWrapper = generateWMInvoiceDiscrepancyData(tickets, wmInvoiceList, origin, destination);
      	billingData = true;
      }
      
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("origin", (StringUtils.isNotEmpty(origin) ? retrieveLocationName(origin) : StringUtils.EMPTY));
      params.put("destination", (StringUtils.isNotEmpty(destination) ? retrieveLocationName(destination) : StringUtils.EMPTY));
      params.put("dateRange", dateRange);
      
      Map<String, Object> dataMap = new HashMap<String, Object>();
      
      if (billingData) {
      	map(params, billingWrapper);
   		
      	List<Billing> billingList = billingWrapper.getBilling();
      	dataMap.put("data", billingList);
      } else {
      	dataMap.put("data", wmInvoiceVerificationList);
      }
      
      dataMap.put("params", params);
      
      return dataMap;
	}
	
	private void map(Map<String, Object> params, BillingWrapper billingWrapper) {
		params.put("sumBillableTon", billingWrapper.getSumBillableTon());
		params.put("totalRowCount",billingWrapper.getTotalRowCount());
		params.put("sumOriginTon", billingWrapper.getSumOriginTon());
		params.put("sumDestinationTon", billingWrapper.getSumDestinationTon());
		params.put("sumTonnage", billingWrapper.getSumTonnage());
		params.put("sumTotal", billingWrapper.getSumTotal());
		params.put("sumDemurrage", billingWrapper.getSumDemmurage());
		params.put("sumNet", billingWrapper.getSumNet());
		params.put("sumAmount", billingWrapper.getSumAmount());
		params.put("sumFuelSurcharge", billingWrapper.getSumFuelSurcharge());
		params.put("sumGallon", billingWrapper.getSumGallon());
	}
	
	private String determineDateRangeDisplay(String fromDateStr, String toDateStr) {
		if (StringUtils.isEmpty(fromDateStr) || StringUtils.isEmpty(toDateStr)) {
			return StringUtils.EMPTY;
		}
		
		try {
			Date fromDate = ReportDateUtil.oracleFormatter.parse(fromDateStr);
			Date toDate = ReportDateUtil.oracleFormatter.parse(toDateStr);
			
			String fromDateDisplayStr =  ReportDateUtil.dateFormatter.format(fromDate);
			String toDateDisplayStr =  ReportDateUtil.dateFormatter.format(toDate);
			
			return fromDateDisplayStr + " - " + toDateDisplayStr;
		} catch (ParseException e) {
			e.printStackTrace();
			return StringUtils.EMPTY;
		}
	}
	
	private List<WMInvoiceVerification> generateWMInvoiceMissingTicketsInWMData(List<Ticket> tickets, 
			List<WMInvoice> wmInvoiceList) {
		List<WMInvoiceVerification> wmInvoiceVerificationList = new ArrayList<WMInvoiceVerification>();
		
		List<Ticket> missingTickets = determineMissingTicketsInWM(tickets, wmInvoiceList);
		if (missingTickets == null || missingTickets.isEmpty()) {
			return wmInvoiceVerificationList;
		}
		
      map(wmInvoiceVerificationList, missingTickets);
      sort(wmInvoiceVerificationList, false);
      return wmInvoiceVerificationList;
	}
	
	private List<WMInvoiceVerification> generateWMInvoiceMissingTicketsData(List<Ticket> tickets, 
			 List<WMInvoice> wmInvoiceList) {
		List<WMInvoiceVerification> wmInvoiceVerificationList = new ArrayList<WMInvoiceVerification>();
		
		List<WMInvoice> missingTickets = determineMissingTickets(tickets, wmInvoiceList);
		if (missingTickets == null || missingTickets.isEmpty()) {
			return wmInvoiceVerificationList;
		}
		
		mapWMInvoice(wmInvoiceVerificationList, missingTickets);
		sort(wmInvoiceVerificationList, true);
		return wmInvoiceVerificationList;
	}
	
	private List<WMInvoiceVerification> generateWMInvoiceMatchingData(List<Ticket> tickets, 
			List<WMInvoice> wmInvoiceList, String origin, String destination) {
		List<WMInvoiceVerification> wmInvoiceVerificationList = new ArrayList<WMInvoiceVerification>();
		
		List<Ticket> matchingTickets = determineMatchingTickets(tickets, wmInvoiceList);
		if (matchingTickets == null || matchingTickets.isEmpty()) {
			return wmInvoiceVerificationList;
		}
		
      map(wmInvoiceVerificationList, matchingTickets);
      sort(wmInvoiceVerificationList, false);
      return wmInvoiceVerificationList;
	}
	
	private BillingWrapper generateWMInvoiceDiscrepancyData(List<Ticket> tickets, List<WMInvoice> wmInvoiceList, 
			String origin, String destination) {
		List<Billing> emptyBillingList = new ArrayList<Billing>();
		BillingWrapper emptyBillingWrapper = new BillingWrapper();
		emptyBillingWrapper.setBilling(emptyBillingList);
		
		if (tickets == null || tickets.isEmpty()) {
			return emptyBillingWrapper;
		}
		if (wmInvoiceList == null || wmInvoiceList.isEmpty()) {
			return emptyBillingWrapper;
		}
		
		List<Ticket> matchingTickets = determineMatchingTickets(tickets, wmInvoiceList);
		if (matchingTickets == null || matchingTickets.isEmpty()) {
			return emptyBillingWrapper;
		}
		
		StringBuffer ticketIdBuff = new StringBuffer();
		for (Ticket aMatchingTicket : matchingTickets) {
			ticketIdBuff.append(aMatchingTicket.getId()).append(",");
		}
		String ticketIds = ticketIdBuff.toString();
		ticketIds = ticketIds.substring(0, ticketIds.length()-1);
		
		Map<String, Object> billingSearchMap = new HashMap<String, Object>();
		billingSearchMap.put("ticketIds", ticketIds);
		billingSearchMap.put("origin", origin);
		billingSearchMap.put("destination", destination);
		
		SearchCriteria billingSearchCriteria = new SearchCriteria();
		billingSearchCriteria.setSearchMap(billingSearchMap);
		
		BillingWrapper billingWrapper = reportService.generateBillingData(billingSearchCriteria);
		List<Billing> billingList = billingWrapper.getBilling();
		if (billingList == null || billingList.isEmpty()) {
			return billingWrapper;
		}
		
		String searchKey = StringUtils.EMPTY;
		
		Map<String, WMInvoice> wmInvoiceMap = new HashMap<String, WMInvoice>();
		for (WMInvoice anWMInvoice : wmInvoiceList) {
			searchKey = anWMInvoice.getTicket() + "|" + anWMInvoice.getOrigin().getName() + "|" + anWMInvoice.getDestination().getName();
			wmInvoiceMap.put(searchKey, anWMInvoice);
		}
		
		List<Billing> resultBillingList = new ArrayList<Billing>();
		for (Billing aBilling : billingList) {
			searchKey = aBilling.getDestinationTicket() + "|" + aBilling.getOrigin() + "|" + aBilling.getDestination();
			WMInvoice aWMInvoice = wmInvoiceMap.get(searchKey);
			if (aWMInvoice == null) {
				searchKey = aBilling.getOriginTicket() + "|" + aBilling.getOrigin() + "|" + aBilling.getDestination();
				aWMInvoice = wmInvoiceMap.get(searchKey);
			} 
			if (aWMInvoice == null) {
				continue;
			}
			
			boolean matching = true;
			if (aBilling.getTotAmt() == null || aWMInvoice.getTotalAmount() == null) {
				matching = false;
			} else {
				if (aBilling.getTotAmt().compareTo(aWMInvoice.getTotalAmount()) != 0) {
					DecimalFormat df = new DecimalFormat("###0.00");
					
					String billingTotalAmountStr = df.format(aBilling.getTotAmt().doubleValue());
					String wmInvoiceTotalAmountStr = df.format(aWMInvoice.getTotalAmount().doubleValue());
					if (!StringUtils.equals(billingTotalAmountStr, wmInvoiceTotalAmountStr)) {
						matching = false;
					}
				}
			}
			
			if (!matching) {
				aBilling.setWmAmount(aWMInvoice.getAmount());
				aBilling.setWmFSC(aWMInvoice.getFsc());
				aBilling.setWmTotalAmount(aWMInvoice.getTotalAmount());
				
				resultBillingList.add(aBilling);
			}
		}
		
		billingWrapper.setBilling(resultBillingList);
		return billingWrapper;
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/save.do")
	public String save(ModelMap model, HttpServletRequest request,	HttpServletResponse response,
				@RequestParam(required = true, value = "action") String action) {
		SearchCriteria searchCriteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		String blankReturn = "blank/blank";
		Long modifieddBy = getUser(request).getId();
		try {
			Map<String, Object> dataMap = generateData(searchCriteria, request);
			List<WMInvoiceVerification> wmInvoiceVerificationList = (List<WMInvoiceVerification>) dataMap.get("data");
		   if (wmInvoiceVerificationList == null || wmInvoiceVerificationList.isEmpty()) {
		   	return blankReturn;
		   }
		   
		   String successMsg = StringUtils.EMPTY;
			if (StringUtils.equals(WMInvoiceVerification.WM_INVOICE_HOLD_TICKETS_ACTION, action)) {
		   	processHoldTickets(wmInvoiceVerificationList, modifieddBy);
		   	successMsg = "Missing Tickets successfully put on HOLD";
		   }
		   
			request.getSession().setAttribute("msg", successMsg);
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
		}
		
		return blankReturn;
	}
	
	private void processHoldTickets(List<WMInvoiceVerification> wmInvoiceVerificationList, Long modifieddBy) {
		for (WMInvoiceVerification wmInvoiceVerification : wmInvoiceVerificationList) {
			processHold(wmInvoiceVerification, modifieddBy);
		}
	}
	
	private void processHold(WMInvoiceVerification wmInvoiceVerification, Long modifieddBy) {
		Ticket ticket = retrieveTicket(wmInvoiceVerification);
		if (ticket == null) {
			return;
		}
		
		ticket.setTicketStatus(0);
		
		ticket.setModifiedAt(Calendar.getInstance().getTime());
		ticket.setModifiedBy(modifieddBy);
		
		genericDAO.saveOrUpdate(ticket);
	}
	
	private Ticket retrieveTicket(WMInvoiceVerification wmInvoiceVerification) {
		Long originTicket = wmInvoiceVerification.getOriginTicket();
		Long destinationTicket = wmInvoiceVerification.getDestinationTicket();
		
		Long originId = wmInvoiceVerification.getOriginId();
		Long destinationId = wmInvoiceVerification.getDestinationId();
		
		String query = "select obj from Ticket obj where"
						 + " obj.origin=" + originId + " and obj.originTicket=" + originTicket
						 + " and obj.destination=" + destinationId + " and obj.destinationTicket=" + destinationTicket;
		
		List<Ticket> ticketList = genericDAO.executeSimpleQuery(query);
		return (ticketList == null || ticketList.isEmpty()) ? null : ticketList.get(0);
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
