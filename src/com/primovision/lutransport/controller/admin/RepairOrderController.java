package com.primovision.lutransport.controller.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrder;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderLineItem;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderComponent;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderHourlyLaborRate;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderLineItemType;

import com.primovision.lutransport.service.DateUpdateService;

@Controller
@RequestMapping("/admin/vehiclemaint/repairorders")
public class RepairOrderController extends CRUDController<RepairOrder> {
	public RepairOrderController() {
		setUrlContext("admin/vehiclemaint/repairorders");
	}
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public DateUpdateService getDateupdateService() {
		return dateupdateService;
	}
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(Driver.class));
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(SubContractor.class, new AbstractModelEditor(SubContractor.class));
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		getDateupdateService().updateDate(request, "RepairOrderDate", "repairOrderDate");
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "id desc", null));
		
		criteria.getSearchMap().put("RepairOrderDate", request.getParameter("RepairOrderDate"));
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		getDateupdateService().updateDate(request, "RepairOrderDate", "repairOrderDate");
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "id desc", null));
		
		criteria.getSearchMap().put("RepairOrderDate", request.getParameter("RepairOrderDate"));
		
		return urlContext + "/list";
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
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
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		Map criterias = new HashMap();
		
		model.addAttribute("repairOrders", genericDAO.findByCriteria(RepairOrder.class, criterias, "id desc", false));
		
		String query = "select obj from Driver obj where obj.catagory.id=3 order by obj.fullName";
		List<Driver> mechanics = genericDAO.executeSimpleQuery(query);
		model.addAttribute("mechanics", mechanics);
		
		model.addAttribute("vehicles", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		model.addAttribute("subcontractors", genericDAO.findByCriteria(SubContractor.class, criterias, "name", false));
		
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	}

	private void validateSave(RepairOrder entity, BindingResult bindingResult) {
		if (entity.getRepairOrderDate() == null) {
			bindingResult.rejectValue("repairOrderDate", "NotNull.java.util.Date", null, null);
		}
		if (entity.getTotalCost() == null) {
			bindingResult.rejectValue("totalCost", "NotNull.java.lang.Float", null, null);
		}
		if (entity.getCompany() == null) {
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		if (entity.getVehicle() == null) {
			bindingResult.rejectValue("vehicle", "error.select.option", null, null);
		}
		if (entity.getMechanic() == null) {
			bindingResult.rejectValue("mechanic", "error.select.option", null, null);
		}
		
		validateSaveLineItem(entity, bindingResult);
	}
	
	private void validateSaveLineItem(RepairOrder entity, BindingResult bindingResult) {
		if (StringUtils.isEmpty(entity.getLineItemType())) {
			if (!StringUtils.isEmpty(entity.getLineItemId())
					|| !StringUtils.isEmpty(entity.getLineItemComponent())
					|| !StringUtils.isEmpty(entity.getLineItemDescription())
					|| entity.getLineItemNoOfHours() != null
					|| entity.getLineItemLaborRate() != null
					|| entity.getLineItemTotalLaborCost() != null
					|| entity.getLineItemTotalPartsCost() != null
					|| entity.getLineItemTotalCost() != null) {
				bindingResult.rejectValue("lineItemType", "error.select.option", null, null);
			}
		} else {
			if (StringUtils.isEmpty(entity.getLineItemComponent())) {
				bindingResult.rejectValue("lineItemComponent", "error.select.option", null, null);
			}
			if (entity.getLineItemNoOfHours() == null) {
				bindingResult.rejectValue("lineItemNoOfHours", "NotNull.java.lang.Integer", null, null);
			}
			if (entity.getLineItemLaborRate() == null) {
				bindingResult.rejectValue("lineItemLaborRate", "NotNull.java.lang.Float", null, null);
			}
			if (entity.getLineItemTotalLaborCost() == null) {
				bindingResult.rejectValue("lineItemTotalLaborCost", "NotNull.java.lang.Float", null, null);
			}
			if (entity.getLineItemTotalPartsCost() == null) {
				bindingResult.rejectValue("lineItemTotalPartsCost", "NotNull.java.lang.Float", null, null);
			}
			if (entity.getLineItemTotalCost() == null) {
				bindingResult.rejectValue("lineItemTotalCost", "NotNull.java.lang.Float", null, null);
			}
		}
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") RepairOrder entity,
			BindingResult bindingResult, ModelMap model) {
		validateSave(entity, bindingResult);
		if(bindingResult.hasErrors()) {
        	setupCreate(model, request);
        	setupLineItem(model, request, entity);
        	return getUrlContext()+"/form";
      }
	
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		saveLineItem(entity);
		
		request.getSession().setAttribute("msg", "Repair order saved successfully");
		
		setupCreate(model, request);
		setupLineItem(model, request, entity);
		return getUrlContext() + "/form";
	}
	
	private void saveLineItem(RepairOrder entity) {
		if (StringUtils.isEmpty(entity.getLineItemType())) {
			return;
		}
		
		RepairOrderLineItem aRepairOrderLineItem = null;
		if (!StringUtils.isEmpty(entity.getLineItemId())) {
			Long lineItemId = Long.valueOf(entity.getLineItemId());
			aRepairOrderLineItem = genericDAO.getById(RepairOrderLineItem.class, lineItemId);
			aRepairOrderLineItem.setModifiedAt(entity.getModifiedAt());
			aRepairOrderLineItem.setModifiedBy(entity.getModifiedBy());
		} else {
			aRepairOrderLineItem = new RepairOrderLineItem();
			aRepairOrderLineItem.setCreatedAt(entity.getCreatedAt());
			aRepairOrderLineItem.setCreatedBy(entity.getCreatedBy());
			aRepairOrderLineItem.setStatus(1);
		}
		
		aRepairOrderLineItem.setRepairOrder(entity);
		
		String lineItemTypeId = entity.getLineItemType();
		RepairOrderLineItemType aRepairOrderLineItemType = genericDAO.getById(RepairOrderLineItemType.class, new Long(lineItemTypeId));
		aRepairOrderLineItem.setLineItemType(aRepairOrderLineItemType);
		
		String lineItemCcomponentId = entity.getLineItemComponent();
		RepairOrderComponent aRepairOrderLineItemComponent = genericDAO.getById(RepairOrderComponent.class, new Long(lineItemCcomponentId));
		aRepairOrderLineItem.setComponent(aRepairOrderLineItemComponent);
		
		aRepairOrderLineItem.setDescription(entity.getLineItemDescription());
		
		aRepairOrderLineItem.setLaborRate(entity.getLineItemLaborRate());
		aRepairOrderLineItem.setTotalLaborCost(entity.getLineItemTotalLaborCost());
		aRepairOrderLineItem.setTotalPartsCost(entity.getLineItemTotalPartsCost());
		aRepairOrderLineItem.setNoOfHours(entity.getLineItemNoOfHours());
		aRepairOrderLineItem.setTotalCost(entity.getLineItemTotalCost());
		
		genericDAO.saveOrUpdate(aRepairOrderLineItem);
		
		emptyLineItem(entity);
	}
	
	private void emptyLineItem(RepairOrder entity) {
		entity.setLineItemId(StringUtils.EMPTY);
		entity.setLineItemType(StringUtils.EMPTY);
		entity.setLineItemComponent(StringUtils.EMPTY);
		entity.setLineItemDescription(StringUtils.EMPTY);
		entity.setLineItemNoOfHours(null);
		entity.setLineItemLaborRate(null);
		entity.setLineItemTotalLaborCost(null);
		entity.setLineItemTotalPartsCost(null);
		entity.setLineItemTotalCost(null);
	}
	
	private void emptyLineItem(RepairOrderLineItem aRepairOrderLineItem) {
		aRepairOrderLineItem.setDescription(StringUtils.EMPTY);
		aRepairOrderLineItem.setLaborRate(0.0);
		aRepairOrderLineItem.setNoOfHours(0.0);
		aRepairOrderLineItem.setTotalLaborCost(0.0);
		aRepairOrderLineItem.setTotalPartsCost(0.0);
		aRepairOrderLineItem.setTotalCost(0.0);
	}
	
	@Override
	public String delete(@ModelAttribute("modelObject") RepairOrder entity,
			BindingResult bindingResult, HttpServletRequest request) {
		String query = "select obj from RepairOrderLineItem obj where obj.repairOrder=" + entity.getId();
		List<RepairOrderLineItem> repairOrderLineItemList = genericDAO.executeSimpleQuery(query);
		for (RepairOrderLineItem aRepairOrderLineItem : repairOrderLineItemList) {
			genericDAO.delete(aRepairOrderLineItem);
		}
		
		String errorMsg = StringUtils.EMPTY;
		try {
			genericDAO.delete(entity);
		} catch (Exception ex) {
			errorMsg = String.format("Error while deleting Repair Order with id: %d", entity.getId());
			request.getSession().setAttribute("error", errorMsg);
			log.warn(errorMsg, ex);
		}
		
		if (StringUtils.isEmpty(errorMsg)) {
			request.getSession().setAttribute("msg", "Repair Order deleted successfully");
		}
		
		return "redirect:/" + urlContext + "/list.do";
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		setupLineItem(model, request, null);
		
		return urlContext + "/form";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/deleteLineItem.do")
	public String deleteLineItem(HttpServletRequest request, ModelMap model,
			@RequestParam(value = "id") Long orderId,
			@RequestParam(value = "lineItemId") Long lineItemId) {
		RepairOrderLineItem aRepairOrderLineItem = genericDAO.getById(RepairOrderLineItem.class, lineItemId);
		String errorMsg = StringUtils.EMPTY;
		try {
			genericDAO.delete(aRepairOrderLineItem);
		} catch (Exception ex) {
			errorMsg = String.format("Error while deleting Line Item with id: %d", lineItemId);
			request.getSession().setAttribute("error", errorMsg);
			log.warn(errorMsg, ex);
		}
		
		if (StringUtils.isEmpty(errorMsg)) {
			request.getSession().setAttribute("msg", "Line Item deleted successfully");
		}
		
		RepairOrder aRepairOrder = genericDAO.getById(RepairOrder.class, orderId);
		double updatedTotalCost = aRepairOrder.getTotalCost().doubleValue() - aRepairOrderLineItem.getTotalCost().doubleValue();
		aRepairOrder.setTotalCost(updatedTotalCost);
		
		genericDAO.save(aRepairOrder);
		
		model.addAttribute("modelObject", aRepairOrder);
		
		setupCreate(model, request);
		setupLineItem(model, request, aRepairOrder);
		return getUrlContext() + "/form";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/copy.do")
	public String copy(HttpServletRequest request, ModelMap model,
			@RequestParam(value = "id") Long orderId) {
		RepairOrder orderToBeCopied = genericDAO.getById(RepairOrder.class, orderId);
		RepairOrder newOrder = new RepairOrder();
		copy(orderToBeCopied, newOrder, request);
		
		genericDAO.save(newOrder);
		
		String query = "select obj from RepairOrderLineItem obj where obj.repairOrder=" + orderToBeCopied.getId()
					+ " order by id asc";
		List<RepairOrderLineItem> lineItemToBeCopiedList = genericDAO.executeSimpleQuery(query);
		for (RepairOrderLineItem aLineItemToBeCopied : lineItemToBeCopiedList) {
			RepairOrderLineItem newLineItem = new RepairOrderLineItem();
			newLineItem.setCreatedAt(Calendar.getInstance().getTime());
			newLineItem.setCreatedBy(getUser(request).getId());
			newLineItem.setStatus(1);
			newLineItem.setRepairOrder(newOrder);
			
			copy(aLineItemToBeCopied, newLineItem);
			
			genericDAO.save(newLineItem);
		}
		
		model.addAttribute("modelObject", newOrder);
		
		setupCreate(model, request);
		setupLineItem(model, request, newOrder);
		return getUrlContext() + "/form";
	}
	
	private void setupLineItem(ModelMap model, HttpServletRequest request, RepairOrder entity) {
		String orderIdStr = request.getParameter("id");
		if (entity == null && StringUtils.isEmpty(orderIdStr)) {
			return;
		}
		
		Long orderId = entity != null ? entity.getId() : Long.valueOf(orderIdStr);
		if (orderId == null) {
			return;
		}
		
		String query = "select obj from RepairOrderLineItem obj where obj.repairOrder.id=" + orderId
								+ " order by obj.id desc";
		List<RepairOrderLineItem> repairOrderLineItemList = genericDAO.executeSimpleQuery(query);
		model.addAttribute("repairOrderLineItemList", repairOrderLineItemList);
	}
	
	@Override
	public String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		Gson gson = new Gson();
		
		if (StringUtils.equalsIgnoreCase("retrieveLineItem", action)) {
			RepairOrderLineItem aRepairOrderLineItem = retrieveLineItem(request);
			return gson.toJson(aRepairOrderLineItem);
		} else if (StringUtils.equalsIgnoreCase("retrieveSimilarLineItem", action)) {
			RepairOrderLineItem similarLineItem = retrieveSimilarLineItem(request);
			return (similarLineItem == null ? StringUtils.EMPTY : gson.toJson(similarLineItem));
		}
		
		return StringUtils.EMPTY;
	}
	
	private RepairOrderLineItem retrieveLineItem(HttpServletRequest request) {
		Long lineItemId = Long.valueOf(request.getParameter("lineItemId"));
		return genericDAO.getById(RepairOrderLineItem.class, lineItemId);
	}
	
	private RepairOrderLineItem retrieveSimilarLineItem(HttpServletRequest request) {
		RepairOrderLineItem similarLineItem = new RepairOrderLineItem();
		emptyLineItem(similarLineItem);
		
		Double currentLaborRate =  retrieveCurrentHourlyLaborRate();
		similarLineItem.setLaborRate(currentLaborRate);
		
		Long typeId = Long.valueOf(request.getParameter("typeId"));
		Long componentId = Long.valueOf(request.getParameter("componentId"));
		
		String query = "select obj from RepairOrderLineItem obj where obj.lineItemType=" + typeId
				+ " and obj.component=" + componentId
				+ " order by obj.id desc";
		List<RepairOrderLineItem> repairOrderLineItemList = genericDAO.executeSimpleQuery(query);
		if (repairOrderLineItemList == null || repairOrderLineItemList.isEmpty()) {
			return similarLineItem;
		}
		
		copy(repairOrderLineItemList.get(0), similarLineItem);
		
		similarLineItem.setLaborRate(currentLaborRate);
		similarLineItem.setTotalLaborCost(currentLaborRate * similarLineItem.getNoOfHours());
		similarLineItem.setTotalCost(similarLineItem.getTotalLaborCost() + similarLineItem.getTotalPartsCost());
		
		return similarLineItem;
	}
	
	private Double retrieveCurrentHourlyLaborRate() {
		String query = "select obj from RepairOrderHourlyLaborRate obj order by obj.id desc";
		List<RepairOrderHourlyLaborRate> repairOrderHourlyLaborRateList = genericDAO.executeSimpleQuery(query);
		if (repairOrderHourlyLaborRateList == null || repairOrderHourlyLaborRateList.isEmpty()) {
			return new Double (0.0);
		} else {
			return repairOrderHourlyLaborRateList.get(0).getLaborRate();
		}
	}
	
	private void copy(RepairOrder aOrder, RepairOrder bOrder, HttpServletRequest request) {
		bOrder.setCreatedAt(Calendar.getInstance().getTime());
		bOrder.setCreatedBy(getUser(request).getId());
		bOrder.setStatus(1);
		
		try {
			String todayDateStr = dateFormat.format((new Date()));
			Date todayDate = dateFormat.parse(todayDateStr);
			bOrder.setRepairOrderDate(todayDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		bOrder.setDescription(aOrder.getDescription());
		bOrder.setCompany(aOrder.getCompany());
		bOrder.setSubcontractor(aOrder.getSubcontractor());
		bOrder.setVehicle(aOrder.getVehicle());
		bOrder.setMechanic(aOrder.getMechanic());
		bOrder.setTotalCost(aOrder.getTotalCost());
	}
	
	private void copy(RepairOrderLineItem aLineItem, RepairOrderLineItem bLineItem) {
		bLineItem.setLineItemType(aLineItem.getLineItemType());
		bLineItem.setComponent(aLineItem.getComponent());
		bLineItem.setDescription(aLineItem.getDescription());
		bLineItem.setLaborRate(aLineItem.getLaborRate());
		bLineItem.setNoOfHours(aLineItem.getNoOfHours());
		bLineItem.setTotalLaborCost(aLineItem.getTotalLaborCost());
		bLineItem.setTotalPartsCost(aLineItem.getTotalPartsCost());
		bLineItem.setTotalCost(aLineItem.getTotalCost());
	}
}
