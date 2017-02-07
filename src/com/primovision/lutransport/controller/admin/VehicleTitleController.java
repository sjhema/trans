package com.primovision.lutransport.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.equipment.VehicleTitle;

@Controller
@RequestMapping("/admin/equipment/title")
public class VehicleTitleController extends CRUDController<VehicleTitle> {
	public VehicleTitleController() {
		setUrlContext("admin/equipment/title");
	}
	
	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		
		criterias.clear();
		criterias.put("type", 3);
		model.addAttribute("owners", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	
		criterias.clear();
		model.addAttribute("vehicles", genericDAO.executeSimpleQuery("select obj from Vehicle obj group by obj.unit, obj.type"));
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
		
		Map criterias = new HashMap();
		model.addAttribute("titles", genericDAO.findByCriteria(VehicleTitle.class, criterias, "title", false));
	}

	private void validateSave(VehicleTitle entity, BindingResult bindingResult) {
		if (entity.getVehicle() == null) {
			bindingResult.rejectValue("vehicle", "error.select.option", null, null);
		}
		if (entity.getTitleOwner() == null) {
			bindingResult.rejectValue("titleOwner", "error.select.option", null, null);
		}
		if (entity.getRegisteredOwner() == null) {
			bindingResult.rejectValue("registeredOwner", "error.select.option", null, null);
		}
		/*if (StringUtils.isEmpty(entity.getTitle())) {
			bindingResult.rejectValue("title", "NotNull.java.lang.String", null, null);
		}*/
		if (StringUtils.isEmpty(entity.getHoldsTitle())) {
			bindingResult.rejectValue("holdsTitle", "NotNull.java.lang.String", null, null);
		}
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") VehicleTitle entity,
			BindingResult bindingResult, ModelMap model) {
		validateSave(entity, bindingResult);
		if(bindingResult.hasErrors()) {
        	setupCreate(model, request);
        	return getUrlContext() + "/form";
      }
	
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		request.getSession().setAttribute("msg", "Vehicle title saved successfully");
		
		setupCreate(model, request);
		
		if (entity.getModifiedBy() == null) {
			model.addAttribute("modelObject", new VehicleTitle());
		} else {
			Vehicle vehicle = genericDAO.getById(Vehicle.class, entity.getVehicle().getId());
			entity.setVehicle(vehicle);
		}
		
		return getUrlContext() + "/form";
	}
	
	@Override
	public String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		Gson gson = new Gson();
		
		if (StringUtils.equalsIgnoreCase("retrieveVehicle", action)) {
			String id = request.getParameter("id");
			Vehicle vehicle = genericDAO.getById(Vehicle.class, Long.valueOf(id));
			return gson.toJson(vehicle);
		} 
		
		return StringUtils.EMPTY;
	}
}
