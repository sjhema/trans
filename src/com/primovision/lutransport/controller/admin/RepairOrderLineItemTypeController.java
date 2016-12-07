package com.primovision.lutransport.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;

import com.primovision.lutransport.model.SearchCriteria;

import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderLineItemType;

@Controller
@RequestMapping("/admin/vehiclemaint/repairorders/lineitemtype")
public class RepairOrderLineItemTypeController extends CRUDController<RepairOrderLineItemType> {
	public RepairOrderLineItemTypeController() {
		setUrlContext("admin/vehiclemaint/repairorders/lineitemtype");
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "type asc", null));
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "type asc", null));
		return urlContext + "/list";
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		
		Map criterias = new HashMap();
		model.addAttribute("types", genericDAO.findByCriteria(RepairOrderLineItemType.class, criterias, "type asc", false));
	}

	private void validateSave(RepairOrderLineItemType entity, BindingResult bindingResult) {
		if (StringUtils.isEmpty(entity.getType())) {
			bindingResult.rejectValue("type", "NotNull.java.lang.String", null, null);
		}
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") RepairOrderLineItemType entity,
			BindingResult bindingResult, ModelMap model) {
		validateSave(entity, bindingResult);
		if(bindingResult.hasErrors()) {
        	setupCreate(model, request);
        	return getUrlContext()+"/form";
      }
	
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		request.getSession().setAttribute("msg", "Repair Order Line Item Type saved successfully");
		
		setupCreate(model, request);
		return getUrlContext() + "/form";
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		
		return urlContext + "/form";
	}
}
