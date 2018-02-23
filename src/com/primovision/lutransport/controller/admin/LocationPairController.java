package com.primovision.lutransport.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.LocationPair;
import com.primovision.lutransport.model.SearchCriteria;

@Controller
@RequestMapping("/admin/locationPair")
public class LocationPairController extends CRUDController<LocationPair> {
	public LocationPairController(){
		setUrlContext("admin/locationPair");
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		
		super.initBinder(binder);
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		
		criterias.put("status", 1);
		criterias.put("type", 1);
		criterias.put("id!", 91l);
		model.addAttribute("origins", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		criterias.put("status", 1);
		criterias.put("type", 2);
		criterias.put("id!", 91l);
		model.addAttribute("destinations", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") LocationPair entity,
			BindingResult bindingResult, ModelMap model) {
		String dupMsg = checkDuplicate(model, request, entity);
		if (StringUtils.isNotEmpty(dupMsg)) {
			return dupMsg;
		}
		
		if (entity.getOrigin() == null) {
			bindingResult.rejectValue("origin", "error.select.option", null, null);
		}
		if (entity.getDestination() == null) {
			bindingResult.rejectValue("destination", "error.select.option", null, null);
		}
			
		return super.save(request, entity, bindingResult, model);
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		return search2(model, request);
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "origin.name asc, destination.name asc", null, null));
		
		return urlContext + "/list";
	}
	
	public String checkDuplicate(ModelMap model, HttpServletRequest request, LocationPair entity) {
		if (entity.getOrigin() == null || entity.getDestination() == null) {
			return StringUtils.EMPTY;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("origin", entity.getOrigin().getId());
		map.put("destination", entity.getDestination().getId());
		if (!(genericDAO.isUnique(LocationPair.class, entity, map))) {
			request.getSession().setAttribute("errors", "Cannot create duplicate entry");
			model.addAttribute("msg", "Cannot create duplicate entry with same Origin and Destination");
			
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
		return StringUtils.EMPTY;
	}
}
