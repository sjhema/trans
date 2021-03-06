package com.primovision.lutransport.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.core.util.MimeUtil;

import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.equipment.EquipmentBuyer;
import com.primovision.lutransport.model.equipment.VehicleSale;
import com.primovision.lutransport.model.equipment.VehicleTitle;

@Controller
@RequestMapping("/admin/equipment/sale")
public class VehicleSaleController extends CRUDController<VehicleSale> {
	public VehicleSaleController() {
		setUrlContext("admin/equipment/sale");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));
		binder.registerCustomEditor(EquipmentBuyer.class, new AbstractModelEditor(EquipmentBuyer.class));
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		model.addAttribute("vehicles", genericDAO.executeSimpleQuery("select obj from Vehicle obj group by obj.unit, obj.type"));
		model.addAttribute("buyers", genericDAO.findByCriteria(EquipmentBuyer.class, criterias, "name", false));
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		List<VehicleSale> vehicleSaleList = performSearch(criteria);
		model.addAttribute("list", vehicleSaleList);
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		List<VehicleSale> vehicleSaleList = performSearch(criteria);
		model.addAttribute("list", vehicleSaleList);
		
		return urlContext + "/list";
	}
	
	@Override
	public void export(ModelMap model, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("type") String type,
			Object objectDAO, Class clazz) {
		response.setContentType(MimeUtil.getContentType(type));
		if (!type.equals("html")) {
			response.setHeader("Content-Disposition", "attachment;filename=vehicleSalesReport." + type);
		}
		
		List<VehicleSale> vehicleSalesList = searchForExport(model, request);
		Map<String, Object> params = new HashMap<String, Object>();
		
		//List columnPropertyList = (List) request.getSession().getAttribute("columnPropertyList");
		ByteArrayOutputStream out = null;
		try {
			/*out = dynamicReportService.exportReport(
						urlContext + "Report", type, getEntityClass(), vehicleSalesList,
						columnPropertyList, request);*/
			out = dynamicReportService.generateStaticReport("vehicleSales",
					vehicleSalesList, params, type, request);
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
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		
		VehicleSale entity = (VehicleSale) model.get("modelObject");
		populateAdditionalData(entity);
		
		return urlContext + "/form";
	}
	
	private void populateAdditionalData(List<VehicleSale> vehicleSaleList) {
		for (VehicleSale aVehicleSale : vehicleSaleList) {
			populateAdditionalData(aVehicleSale);
		}
	}
	
	private void populateAdditionalData(VehicleSale aVehicleSale) {
		aVehicleSale.setBuyerName(aVehicleSale.getBuyer().getName());
	}
	
	private List<VehicleSale> performSearch(SearchCriteria criteria) {
		String vehicle = (String) criteria.getSearchMap().get("vehicle.unit");	
		String buyer = (String) criteria.getSearchMap().get("buyer");
		String soldDateFrom = (String) criteria.getSearchMap().get("soldDateFrom");
		String soldDateTo = (String) criteria.getSearchMap().get("soldDateTo");
		String scrapped = (String) criteria.getSearchMap().get("scrapped");
		
		StringBuffer query = new StringBuffer("select obj from VehicleSale obj where 1=1");
		StringBuffer countQuery = new StringBuffer("select count(obj) from VehicleSale obj where 1=1");
		StringBuffer whereClause = new StringBuffer();
		
		if (StringUtils.isNotEmpty(vehicle)) {
			whereClause.append(" and obj.vehicle.unit=" + vehicle);
		}
		if (StringUtils.isNotEmpty(buyer)) {
			whereClause.append(" and obj.buyer=" + buyer);
		}
		
	   if (StringUtils.isNotEmpty(soldDateFrom)){
        	try {
        		whereClause.append(" and obj.saleDate >='"+sdf.format(dateFormat.parse(soldDateFrom))+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        	
		}
      if (StringUtils.isNotEmpty(soldDateTo)){
	     	try {
	     		whereClause.append(" and obj.saleDate <='"+sdf.format(dateFormat.parse(soldDateTo))+"'");
	     	} catch (ParseException e) {
				e.printStackTrace();
			}
		}
      
      if (StringUtils.isNotEmpty(scrapped)) {
      	String scrappedCondn = StringUtils.EMPTY;;
      	if (StringUtils.equals("1", scrapped)) {
      		scrappedCondn = " and obj.scrapped=1";
      	} else {
      		scrappedCondn = " and obj.scrapped is null";
      	}
			whereClause.append(scrappedCondn);
		}
      
      query.append(whereClause);
      countQuery.append(whereClause);
      
      query.append(" order by obj.vehicle.unit asc");
      
      Long recordCount = (Long) genericDAO.getEntityManager().createQuery(countQuery.toString()).getSingleResult();        
		criteria.setRecordCount(recordCount.intValue());	
		
		List<VehicleSale> vehicleSaleList = 
				genericDAO.getEntityManager().createQuery(query.toString())
						.setMaxResults(criteria.getPageSize())
						.setFirstResult(criteria.getPage() * criteria.getPageSize())
						.getResultList();
		
		populateAdditionalData(vehicleSaleList);
		
		return vehicleSaleList;
	}
	
	private List<VehicleSale> searchForExport(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		int origPage = criteria.getPage();
		
		criteria.setPage(0);
		criteria.setPageSize(100000);
		
		List<VehicleSale> vehicleSalesList = performSearch(criteria);
		
		criteria.setPage(origPage);
		criteria.setPageSize(25);
		
		return vehicleSalesList;
	}

	private void validateSave(VehicleSale entity, BindingResult bindingResult) {
		if (entity.getVehicle() == null) {
			bindingResult.rejectValue("vehicle", "error.select.option", null, null);
		}
		/*if (entity.getBuyer() == null) {
			bindingResult.rejectValue("buyer", "error.select.option", null, null);
		}*/
		if (StringUtils.isEmpty(entity.getBuyerName())) {
			bindingResult.rejectValue("buyerName", "NotNull.java.lang.String", null, null);
		}
		if (entity.getSalePrice() == null) {
			bindingResult.rejectValue("salePrice", "NotNull.java.lang.Float", null, null);
		}
		if (entity.getSaleDate() == null) {
			bindingResult.rejectValue("saleDate", "NotNull.java.util.Date", null, null);
		}
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") VehicleSale entity,
			BindingResult bindingResult, ModelMap model) {
		validateSave(entity, bindingResult);
		if(bindingResult.hasErrors()) {
        	setupCreate(model, request);
        	return getUrlContext() + "/form";
      }
	
		beforeSave(request, entity, model);
		
		populateBuyer(entity);
		
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		// Delete vehicle title - 29th May 2017
		deleteVehicleTitle(entity);
		// Inactivate vehicle - 21st Sep 2017
		inactivateVehicle(entity);
		
		request.getSession().setAttribute("msg", "Vehicle sale details saved successfully");
		
		setupCreate(model, request);
		
		if (entity.getModifiedBy() == null) {
			model.addAttribute("modelObject", new VehicleSale());
		} else {
			/*EquipmentBuyer buyer = genericDAO.getById(EquipmentBuyer.class, entity.getBuyer().getId());
			entity.setBuyer(buyer);*/
			Vehicle vehicle = genericDAO.getById(Vehicle.class, entity.getVehicle().getId());
			entity.setVehicle(vehicle);
		}
		
		return getUrlContext() + "/form";
	}
	
	// Delete vehicle title - 29th May 2017
	private void deleteVehicleTitle(VehicleSale vehicleSale) {
		if (vehicleSale.getModifiedBy() != null) {
			return;
		}
		
		String query = "select obj from VehicleTitle obj where obj.vehicle=" + vehicleSale.getVehicle().getId();
		List<VehicleTitle> vehicleTitleList = genericDAO.executeSimpleQuery(query);
		if (vehicleTitleList == null || vehicleTitleList.isEmpty()) {
			return;
		}
		
		for (VehicleTitle aVehicleTitle : vehicleTitleList) {
			genericDAO.delete(aVehicleTitle);
		}
	}
	
	// Inactivate vehicle - 21st Sep 2017
	private void inactivateVehicle(VehicleSale vehicleSale) {
		/*if (vehicleSale.getModifiedBy() != null) {
			return;
		}*/
		
		String query = "select obj from Vehicle obj where obj.id=" + vehicleSale.getVehicle().getId();
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(query);
		if (vehicleList == null || vehicleList.isEmpty()) {
			return;
		}
		
		for (Vehicle aVehicle : vehicleList) {
			aVehicle.setActiveStatus(2); // Inactive
			aVehicle.setInactiveDate(vehicleSale.getSaleDate());
			aVehicle.setModifiedAt(vehicleSale.getCreatedAt());
			aVehicle.setModifiedBy(vehicleSale.getCreatedBy());
			genericDAO.saveOrUpdate(aVehicle);
		}
	}
	
	private void populateBuyer(VehicleSale entity) {
		String buyerName = StringUtils.replace(entity.getBuyerName(), "'", "''", -1);
		buyerName = StringUtils.upperCase(buyerName);
		String query = "select obj from EquipmentBuyer obj "
						+ " where UPPER(obj.name)='" + buyerName + "' order by id desc";
		List<EquipmentBuyer> buyerList = genericDAO.executeSimpleQuery(query);
		
		EquipmentBuyer buyer = null;
		if (buyerList.isEmpty()) {
			buyer = addBuyer(entity);
		} else {
			buyer = buyerList.get(0);
		}
		
		entity.setBuyer(buyer);
	}
	
	private EquipmentBuyer addBuyer(VehicleSale entity) {
		EquipmentBuyer buyer = new EquipmentBuyer();
		buyer.setName(entity.getBuyerName());
		buyer.setAddress1("address1");
		buyer.setCity("Chicago");
		
		State state = retrieveState("IL");
		buyer.setState(state);
		
		buyer.setZipcode("60632");
		
		buyer.setCreatedAt(entity.getCreatedAt());
		buyer.setCreatedBy(entity.getCreatedBy());
		
		genericDAO.save(buyer);
		
		return buyer;
	}
	
	private State retrieveState(String code) {
		String query = "select obj from State obj "
				+ " where obj.code='" + code + "'";
		List<State> stateList = genericDAO.executeSimpleQuery(query);
		return stateList.get(0);
	}
	
	@Override
	public String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		Gson gson = new Gson();
		
		if (StringUtils.equalsIgnoreCase("retrieveBuyer", action)) {
			String id = request.getParameter("id");
			EquipmentBuyer buyer = genericDAO.getById(EquipmentBuyer.class, Long.valueOf(id));
			return gson.toJson(buyer);
		} else if (StringUtils.equalsIgnoreCase("retrieveVehicle", action)) {
			String id = request.getParameter("id");
			Vehicle vehicle = genericDAO.getById(Vehicle.class, Long.valueOf(id));
			return gson.toJson(vehicle);
		} 
		
		return StringUtils.EMPTY;
	}
}
