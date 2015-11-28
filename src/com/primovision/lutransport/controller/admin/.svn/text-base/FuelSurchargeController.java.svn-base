package com.primovision.lutransport.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.FuelSurcharge;
import com.primovision.lutransport.model.Location;

@Controller
@RequestMapping("admin/fuelsurcharge")
public class FuelSurchargeController extends CRUDController<FuelSurcharge> {
	
	public FuelSurchargeController(){
		setUrlContext("admin/fuelsurcharge");
	}
	
	@Override
	@InitBinder
	 public void initBinder(WebDataBinder binder) { 
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(Long.class,false));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		model.addAttribute("locations", genericDAO.findAll(Location.class));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		List<Location> location= genericDAO.findAll(Location.class);
		model.addAttribute("location", location);
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") FuelSurcharge entity,
			BindingResult bindingResult, ModelMap model) {
		
			if(entity.getFromPlace()==null){
				bindingResult.rejectValue("fromPlace", "error.select.option", null, null);
			}
			if(entity.getToPlace()==null){
				bindingResult.rejectValue("toPlace", "error.select.option", null, null);
			}
		return super.save(request, entity, bindingResult, model);
	}

}
