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

import com.primovision.lutransport.model.InternalSubcontractorMapping;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.SubContractor;

@Controller
@RequestMapping("/admin/internalSubcon")
public class InternalSubcontractorMappingController extends CRUDController<InternalSubcontractorMapping> {
	public InternalSubcontractorMappingController(){
		setUrlContext("admin/internalSubcon");
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(SubContractor.class, new AbstractModelEditor(SubContractor.class));
		
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
		
		criterias.clear();
		criterias.put("status", 1);
		criterias.put("type", 3);
		model.addAttribute("companies", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	
		criterias.clear();
		criterias.put("status", 1);
		model.addAttribute("subcontractors", genericDAO.findByCriteria(SubContractor.class, criterias, "name", false));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") InternalSubcontractorMapping entity,
			BindingResult bindingResult, ModelMap model) {
		String dupMsg = checkDuplicate(model, request, entity);
		if (StringUtils.isNotEmpty(dupMsg)) {
			return dupMsg;
		}
		
		if (entity.getDriverCompany() == null) {
			bindingResult.rejectValue("driverCompany", "error.select.option", null, null);
		}
		if (entity.getBillingCompany() == null) {
			bindingResult.rejectValue("billingCompany", "error.select.option", null, null);
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
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, 
									"driverCompany.name asc, billingCompany.name asc, origin.name asc, destination.name asc", null, null));
		
		return urlContext + "/list";
	}
	
	public String checkDuplicate(ModelMap model, HttpServletRequest request, InternalSubcontractorMapping entity) {
		if (entity.getOrigin() == null || entity.getDestination() == null
				|| entity.getDriverCompany() == null || entity.getBillingCompany() == null) {
			return StringUtils.EMPTY;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("driverCompany", entity.getDriverCompany().getId());
		map.put("billingCompany", entity.getBillingCompany().getId());
		map.put("origin", entity.getOrigin().getId());
		map.put("destination", entity.getDestination().getId());
		if (!(genericDAO.isUnique(InternalSubcontractorMapping.class, entity, map))) {
			request.getSession().setAttribute("errors", "Cannot create duplicate entry");
			model.addAttribute("msg", "Cannot create duplicate entry with same Driver Company, Billing Company, Origin and Destination");
			
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
		return StringUtils.EMPTY;
	}
}
