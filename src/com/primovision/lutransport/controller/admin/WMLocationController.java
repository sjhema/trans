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
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.WMLocation;

@Controller
@RequestMapping("/admin/wmLocation")
public class WMLocationController extends CRUDController<WMLocation> {
	public WMLocationController(){
		setUrlContext("admin/wmLocation");
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
		
		criterias.put("dataType", "LOCATION_TYPE");
		criterias.put("dataValue", "1,2");
		model.addAttribute("locationTypes", genericDAO.findByCriteria(StaticData.class, criterias, "dataText", false));
		
		criterias.clear();
		criterias.put("status", 1);
		criterias.put("type", "1,2");
		criterias.put("id!", 91l);
		model.addAttribute("locations", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") WMLocation entity,
			BindingResult bindingResult, ModelMap model) {
		String dupMsg = checkDuplicate(model, request, entity);
		if (StringUtils.isNotEmpty(dupMsg)) {
			return dupMsg;
		}
		
		if (entity.getLocation() == null) {
			bindingResult.rejectValue("location", "error.select.option", null, null);
		}
		if (StringUtils.isEmpty(entity.getWmLocationName())) {
			bindingResult.rejectValue("wmLocationName", "NotNull.java.lang.String", null, null);
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
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "location.name", null, null));
		
		return urlContext + "/list";
	}
	
	public String checkDuplicate(ModelMap model, HttpServletRequest request, WMLocation entity) {
		if (entity.getLocation() == null || StringUtils.isEmpty(entity.getWmLocationName())) {
			return StringUtils.EMPTY;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("location", entity.getLocation().getId());
		map.put("wmLocationName", entity.getWmLocationName());
		if (!(genericDAO.isUnique(WMLocation.class, entity, map))) {
			request.getSession().setAttribute("errors", "Cannot create duplicate entry");
			model.addAttribute("msg", "Cannot create duplicate entry with same Location and WM location name");
			
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
		return StringUtils.EMPTY;
	}
}
