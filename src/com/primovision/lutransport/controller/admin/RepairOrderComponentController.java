package com.primovision.lutransport.controller.admin;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;

import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderComponent;
import com.primovision.lutransport.model.vehiclemaintenance.RepairOrderLineItemType;

@Controller
@RequestMapping("/admin/vehiclemaint/repairorders/component")
public class RepairOrderComponentController extends CRUDController<RepairOrderComponent> {
	public RepairOrderComponentController() {
		setUrlContext("admin/vehiclemaint/repairorders/component");
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "component asc", null));
		
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "component asc", null));
		return urlContext + "/list";
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		
		Map criterias = new HashMap();
		model.addAttribute("components", genericDAO.findByCriteria(RepairOrderComponent.class, criterias, "component asc", false));
	}

	private void validateSave(RepairOrderComponent entity, BindingResult bindingResult) {
		if (StringUtils.isEmpty(entity.getComponent())) {
			bindingResult.rejectValue("component", "NotNull.java.lang.String", null, null);
		}
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") RepairOrderComponent entity,
			BindingResult bindingResult, ModelMap model) {
		validateSave(entity, bindingResult);
		if(bindingResult.hasErrors()) {
        	setupCreate(model, request);
        	return getUrlContext()+"/form";
      }
	
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		request.getSession().setAttribute("msg", "Repair Order Component saved successfully");
		
		setupCreate(model, request);
		return getUrlContext() + "/form";
	}
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		
		return urlContext + "/form";
	}
	
	private RepairOrderComponent save(HttpServletRequest request) {
		String component = request.getParameter("component");
		String description = request.getParameter("description");
		
		RepairOrderComponent entity = new RepairOrderComponent();
		entity.setCreatedAt(Calendar.getInstance().getTime());
		entity.setCreatedBy(getUser(request).getId());
		entity.setComponent(component);
		entity.setDescription(description);
		
		genericDAO.saveOrUpdate(entity);
		return entity;
	}
	
	@Override
	public String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		if (StringUtils.equalsIgnoreCase("save", action)) {
			RepairOrderComponent entity = save(request);
			return "Repair Order Component saved successfully:" + entity.getId();
		} 
		
		return StringUtils.EMPTY;
	}
}
