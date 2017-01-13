package com.primovision.lutransport.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.equipment.EquipmentLender;

@Controller
@RequestMapping("/admin/equipment/lender")
public class EquipmentLenderController extends CRUDController<EquipmentLender> {
	public EquipmentLenderController() {
		setUrlContext("admin/equipment/lender");
	}

	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(State.class, new AbstractModelEditor(State.class));
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		
		model.addAttribute("states", genericDAO.findByCriteria(State.class, criterias, "name", false));
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
		
		Map criterias = new HashMap();
		model.addAttribute("lenders", genericDAO.findByCriteria(EquipmentLender.class, criterias, "name", false));
	}

	private void validateSave(EquipmentLender entity, BindingResult bindingResult) {
		if (StringUtils.isEmpty(entity.getName())) {
			bindingResult.rejectValue("name", "NotNull.java.lang.String", null, null);
		} 
		if (StringUtils.isEmpty(entity.getAddress1())) {
			bindingResult.rejectValue("address1", "NotNull.java.lang.String", null, null);
		} 
		if (StringUtils.isEmpty(entity.getCity())) {
			bindingResult.rejectValue("city", "NotNull.java.lang.String", null, null);
		} 
		if (entity.getState() == null) {
			bindingResult.rejectValue("state", "error.select.option", null, null);
		}
		if (StringUtils.isEmpty(entity.getZipcode())) {
			bindingResult.rejectValue("zipcode", "NotNull.java.lang.String", null, null);
		} else if(entity.getZipcode().length() < 5) {
			bindingResult.rejectValue("zipcode", "typeMismatch.java.lang.String", null, null);
		}
		if (StringUtils.isNotEmpty(entity.getPhone())) {
			if(entity.getPhone().length() < 12) {
				bindingResult.rejectValue("phone", "typeMismatch.java.lang.String", null, null);
			}
		}
		if (StringUtils.isNotEmpty(entity.getFax())) {
			if(entity.getFax().length() < 12) {
				bindingResult.rejectValue("fax", "typeMismatch.java.lang.String", null, null);
			}
		}
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") EquipmentLender entity,
			BindingResult bindingResult, ModelMap model) {
		validateSave(entity, bindingResult);
		if(bindingResult.hasErrors()) {
        	setupCreate(model, request);
        	return getUrlContext() + "/form";
      }
	
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		request.getSession().setAttribute("msg", "Equipment lender saved successfully");
		
		setupCreate(model, request);
		
		if (entity.getModifiedBy() == null) {
			model.addAttribute("modelObject", new EquipmentLender());
		}
		
		return getUrlContext() + "/form";
	}
}
