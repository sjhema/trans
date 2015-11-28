/**
 * 
 */
package com.primovision.lutransport.controller.hr;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.hr.SetupData;

import antlr.collections.List;



/**
 * @author kishan
 *
 */

@Controller
@RequestMapping("/hr/setupdata")
public class SetupDataController extends CRUDController<SetupData>{
	public SetupDataController() {
		setUrlContext("hr/setupdata");
	}
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));		
	}
	
	
	
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {		
		return super.list(model, request);
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		model.addAttribute("dataTypes", genericDAO.executeSimpleQuery("Select obj.dataType from SetupData obj Group By obj.dataType"));
		super.setupCreate(model, request);
	}
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		model.addAttribute("dataTypes", genericDAO.executeSimpleQuery("Select obj.dataType from SetupData obj Group By obj.dataType"));
		super.setupList(model, request);
	}
	
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") SetupData entity,
			BindingResult bindingResult, ModelMap model) {
		
		
		if(StringUtils.isEmpty(entity.getDataLabel()) ){
			bindingResult.rejectValue("dataLabel", "NotNull.java.lang.String", null, null);
			
		}
		if(StringUtils.isEmpty(entity.getDataType()) ){
			bindingResult.rejectValue("dataType", "NotNull.java.lang.String", null, null);
		}
		
		if(StringUtils.isEmpty(entity.getDataValue()) ){
			bindingResult.rejectValue("dataValue", "NotNull.java.lang.String", null, null);
		}
		
		
		// validate entity
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		// return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			return urlContext + "/form";
		}
		beforeSave(request, entity, model);
		// merge into datasource
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		// return to list
		return "redirect:/" + urlContext + "/list.do";
	}

}
