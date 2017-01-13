package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.google.gson.Gson;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.equipment.EquipmentBuyer;
import com.primovision.lutransport.model.equipment.VehicleLoan;
import com.primovision.lutransport.model.equipment.VehicleSale;

@Controller
@RequestMapping("/admin/equipment/sale")
public class VehicleSaleController extends CRUDController<VehicleSale> {
	public VehicleSaleController() {
		setUrlContext("admin/equipment/sale");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

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
		Map criterias = new HashMap();
		
		model.addAttribute("vehicles", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
		model.addAttribute("buyers", genericDAO.findByCriteria(EquipmentBuyer.class, criterias, "name", false));
		model.addAttribute("states", genericDAO.findByCriteria(State.class, criterias, "name", false));
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}

	private void validateSave(VehicleSale entity, BindingResult bindingResult) {
		if (entity.getVehicle() == null) {
			bindingResult.rejectValue("vehicle", "error.select.option", null, null);
		}
		if (entity.getBuyer() == null) {
			bindingResult.rejectValue("buyer", "error.select.option", null, null);
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
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		request.getSession().setAttribute("msg", "Vehicle sale saved successfully");
		
		setupCreate(model, request);
		
		if (entity.getModifiedBy() == null) {
			model.addAttribute("modelObject", new VehicleSale());
		} else {
			EquipmentBuyer buyer = genericDAO.getById(EquipmentBuyer.class, entity.getBuyer().getId());
			entity.setBuyer(buyer);
		}
		
		return getUrlContext() + "/form";
	}
	
	@Override
	public String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		Gson gson = new Gson();
		
		if (StringUtils.equalsIgnoreCase("retrieveBuyer", action)) {
			String id = request.getParameter("id");
			EquipmentBuyer buyer = genericDAO.getById(EquipmentBuyer.class, Long.valueOf(id));
			return gson.toJson(buyer);
		} 
		
		return StringUtils.EMPTY;
	}
}
