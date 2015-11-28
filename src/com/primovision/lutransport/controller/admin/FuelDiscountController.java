package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.model.FuelDiscount;

import com.primovision.lutransport.model.FuelVendor;
//import com.primovision.lutransport.model.Location;

@Controller
@RequestMapping("/admin/fueldiscount")
public class FuelDiscountController extends CRUDController<FuelDiscount> {
	
	public FuelDiscountController(){
		setUrlContext("admin/fueldiscount");
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(FuelVendor.class, new AbstractModelEditor(FuelVendor.class));
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("fuelvendors", genericDAO.findByCriteria(FuelVendor.class, criterias, "name", false));
	}
	
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") FuelDiscount entity, BindingResult bindingResult,
			ModelMap model) {
		if(entity.getFuelvendor()==null){
			bindingResult.rejectValue("fuelvendor", "error.select.option", null, null);
		}
		
		
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		// return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			//model.addAttribute("type",(String) request.getSession().getAttribute("typ"));
			return urlContext + "/form";
		}
		
		
		
		
		beforeSave(request, entity, model);			
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);			
		   
	
		return "redirect:/" + urlContext + "/list.do";
		/*return super.save(request, entity, bindingResult, model);*/
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
