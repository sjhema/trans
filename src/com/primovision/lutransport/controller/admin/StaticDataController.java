package com.primovision.lutransport.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;

@Controller
@RequestMapping("/admin/staticdata")
public class StaticDataController extends CRUDController<StaticData> {

	public StaticDataController() {
		setUrlContext("admin/staticdata");
	}

	@Override
	public String list(ModelMap model, HttpServletRequest request)
	{
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"dataType",null,null));
		return urlContext + "/list";
	}
	
	
        public void setupList(ModelMap model, HttpServletRequest request) 
	{
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request)
	{
		Map criterias = new HashMap();
		model.addAttribute("dataTypes", genericDAO.findByCriteria(StaticData.class, criterias,"dataType",false));
		System.out.println("\n\nStaticDataController........setupCreate(ModelMap model, HttpServletRequest request)\n\n");

	}
	
	
	
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request)
	{
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"dataType",null,null));
		return urlContext + "/list";
	}


	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") StaticData entity,
			BindingResult bindingResult, ModelMap model) {
		
		
		if(StringUtils.isEmpty(entity.getDataText()) ){
			bindingResult.rejectValue("dataText", "NotNull.java.lang.String", null, null);
			
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
