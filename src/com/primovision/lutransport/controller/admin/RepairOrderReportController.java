package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import net.sf.jasperreports.engine.JasperPrint;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.primovision.lutransport.controller.BaseController;

import com.primovision.lutransport.core.util.MimeUtil;

import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrder;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderLineItem;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderComponent;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderLineItemType;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderReportInput;

import com.primovision.lutransport.service.DynamicReportService;

@Controller
@RequestMapping("/admin/vehiclemaint/repairorders/report/repairorderreport")
public class RepairOrderReportController extends BaseController {
	@Autowired
	private DynamicReportService dynamicReportService;

	public void setDynamicReportService(DynamicReportService dynamicReportService) {
		this.dynamicReportService = dynamicReportService;
	}
	
	public RepairOrderReportController() {
		setUrlContext("admin/vehiclemaint/repairorders/report/repairordersreport");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/start.do")
	public String start(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		if (criteria != null && criteria.getSearchMap() != null) {
				criteria.getSearchMap().clear();
		}
		
		setupList(model, request);
		return "admin/vehiclemaint/repairorders/report/" + "repairOrdersReport";
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/search.do")
	public String search(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("modelObject") RepairOrderReportInput input) {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		request.getSession().setAttribute("input", input);
		
		populateSearchCriteria(request, request.getParameterMap());
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(1000);
		
		Map<String, Object> datas = generateData(criteria, request, input);
		List<RepairOrderLineItem> repairOrderLineItemList = (List<RepairOrderLineItem>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		String type = "html";
		response.setContentType(MimeUtil.getContentType(type));
		
		try {
			JasperPrint jasperPrint = dynamicReportService.getJasperPrintFromFile("repairOrderReport",
					repairOrderLineItemList, params, request);
			request.setAttribute("jasperPrint", jasperPrint);
			
			return "admin/vehiclemaint/repairorders/report/repairordersreport/"+type;
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errors", e.getMessage());
			
			return "error";
		}
	}
	
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/export.do")
	public String export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = true, value = "type") String type) {
		Map imagesMap = new HashMap();
		request.getSession().setAttribute("IMAGES_MAP", imagesMap);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(15000);
		criteria.setPage(0);
		
		RepairOrderReportInput input = (RepairOrderReportInput) request.getSession().getAttribute("input");
		Map<String, Object> datas = generateData(criteria, request, input);
		List<RepairOrderLineItem> repairOrderLineItemList = (List<RepairOrderLineItem>) datas.get("data");
		Map<String, Object> params = (Map<String, Object>) datas.get("params");
		
		if (!StringUtils.equals("html", type) && !StringUtils.equals("print", type)) {
			response.setHeader("Content-Disposition", "attachment;filename=repairOrderReport." + type);
		}
		response.setContentType(MimeUtil.getContentType(type));
		
		ByteArrayOutputStream out = null;
		try {
			out = dynamicReportService.generateStaticReport("repairOrderReport",
					repairOrderLineItemList, params, type, request);
			
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
	
	private Map<String,Object> generateData(SearchCriteria searchCriteria, 
			HttpServletRequest request, RepairOrderReportInput input) {
		List<RepairOrderLineItem> repairOrderLineItemList = performSearch(searchCriteria, input);  
		
		Double totalCostForAllOrders = new Double(0.00);
		for (RepairOrderLineItem aLineItem : repairOrderLineItemList) {
			totalCostForAllOrders = totalCostForAllOrders + aLineItem.getTotalCost();
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		Location company = retrieveCompany(input.getCompany());
		String companyName = company == null ? StringUtils.EMPTY : company.getName();
		params.put("company", companyName);
		
		SubContractor subContractor = retrieveSubContractor(input.getSubcontractor());
		String subContractorName = subContractor == null ? StringUtils.EMPTY : subContractor.getName();
		params.put("subcontractor", subContractorName);
		
		String orderDateRange = StringUtils.isEmpty(input.getRepairOrderDateFrom()) ? StringUtils.EMPTY : input.getRepairOrderDateFrom();
		orderDateRange += " - ";
		orderDateRange += StringUtils.isEmpty(input.getRepairOrderDateTo()) ? StringUtils.EMPTY : input.getRepairOrderDateTo();
		params.put("orderDateRange", orderDateRange);
		
		params.put("totalCostForAllOrders", totalCostForAllOrders);
		params.put("noOfLineItems", new Integer(repairOrderLineItemList.size()));
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("data", repairOrderLineItemList);
		data.put("params",params);
		     
		return data;
	}
	
	private Location retrieveCompany(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		
		return genericDAO.getById(Location.class, Long.valueOf(id));
	}
	
	private SubContractor retrieveSubContractor(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		
		return genericDAO.getById(SubContractor.class, Long.valueOf(id));
	}
	
	private List<RepairOrderLineItem> performSearch(SearchCriteria criteria, RepairOrderReportInput input) {
		String orderId = input.getOrderId();
		String company = input.getCompany();
		String subcontractor = input.getSubcontractor();
		String vehicle = input.getVehicle();	
		String mechanic = input.getMechanic();
		String repairOrderDateFrom = input.getRepairOrderDateFrom();
		String repairOrderDateTo = input.getRepairOrderDateTo();
		String lineItemType = input.getLineItemType();
		String component = input.getLineItemComponent();
		
		StringBuffer query = new StringBuffer("select obj from RepairOrderLineItem obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from RepairOrderLineItem obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(orderId)) {
			whereClause.append(" and obj.repairOrder=" + orderId);
		}
		if (StringUtils.isNotEmpty(company)) {
			whereClause.append(" and obj.repairOrder.company=" + company);
		}
		if (StringUtils.isNotEmpty(subcontractor)) {
			whereClause.append(" and obj.repairOrder.subcontractor=" + subcontractor);
		}
		if (StringUtils.isNotEmpty(vehicle)) {
			whereClause.append(" and obj.repairOrder.vehicle.unit=" + vehicle);
		}
		if (StringUtils.isNotEmpty(mechanic)) {
			whereClause.append(" and obj.repairOrder.mechanic.id=" + mechanic);
		}
	   if (StringUtils.isNotEmpty(repairOrderDateFrom)){
        	try {
        		whereClause.append(" and obj.repairOrder.repairOrderDate >='"+sdf.format(dateFormat.parse(repairOrderDateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      if (StringUtils.isNotEmpty(repairOrderDateTo)){
	     	try {
	     		whereClause.append(" and obj.repairOrder.repairOrderDate <='"+sdf.format(dateFormat.parse(repairOrderDateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      if (StringUtils.isNotEmpty(lineItemType)) {
      	whereClause.append(" and obj.lineItemType.id=" + lineItemType);
		}
      if (StringUtils.isNotEmpty(component)) {
      	whereClause.append(" and obj.component.id=" + component);
		}
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by repairOrder.id desc, lineItemType.type asc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<RepairOrderLineItem> repairOrderLineItemList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		return repairOrderLineItemList;
	}

	public void setupList(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		
		model.addAttribute("lineItemTypes", genericDAO.findByCriteria(RepairOrderLineItemType.class, criterias, "type asc", false));
		model.addAttribute("components", genericDAO.findByCriteria(RepairOrderComponent.class, criterias, "component asc", false));
	
		String query = "select obj from Driver obj where obj.catagory.id=3 order by obj.fullName";
		List<Driver> mechanics = genericDAO.executeSimpleQuery(query);
		model.addAttribute("mechanics", mechanics);
		
		model.addAttribute("vehicles", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		model.addAttribute("subcontractors", genericDAO.findByCriteria(SubContractor.class, criterias, "name", false));
	
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
		criterias.clear();
		model.addAttribute("repairOrders", genericDAO.findByCriteria(RepairOrder.class, criterias, "id desc", false));
	}
	
	@ModelAttribute("modelObject")
	public RepairOrderReportInput setupModel(HttpServletRequest request) {
		return new RepairOrderReportInput();
	}
}
