package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;

import com.primovision.lutransport.model.LoadingFee;
import com.primovision.lutransport.model.SearchCriteria;

import com.primovision.lutransport.service.DateUpdateService;

@Controller
@RequestMapping("/admin/loadingfee")
public class LoadingFeeController extends CRUDController<LoadingFee> {
	public LoadingFeeController(){
		setUrlContext("admin/loadingfee");
	}
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		
		dateupdateService.updateDate(request, "validFromDate", "validFrom");
		dateupdateService.updateDate(request, "validToDate", "validTo");
		
		model.addAttribute("list",
				genericDAO.search(getEntityClass(), criteria, "validFrom desc, code ",false));
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "validFrom desc, code", false));
		return urlContext + "/list";
	}
	
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") LoadingFee entity,
			BindingResult bindingResult, ModelMap model) {
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
		String rateQuery = StringUtils.EMPTY;
		String result = StringUtils.EMPTY;
		Date validFromTemp = entity.getValidFromTemp();
		Date validToTemp = entity.getValidToTemp();
		if (validFromTemp != null && validToTemp != null ) {
			 rateQuery = "select obj from LoadingFee obj where obj.code='" + entity.getCode()					
					+ "' and obj.validFrom !='"
					+ validFromTemp
					+ "' and obj.validTo != '"
					+ validToTemp +"'";
			  
			 result = validate(request, model, entity, rateQuery);
		} else {			
			rateQuery = "select obj from LoadingFee obj where obj.code='" + entity.getCode()
				+ "' order by obj.validFrom desc";
			result = validate(request, model, entity, rateQuery);
		}
		if (result.equalsIgnoreCase("error")) {            	
	   	  setupUpdate(model, request);			
	   	  request.getSession().setAttribute("error", "Rate already exists for the specified date range.");
	   	  return urlContext + "/form"; 
	   }
		
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		
		cleanUp(request);
		return "redirect:/" + urlContext + "/list.do";
	}
	
	private String validate(HttpServletRequest request, ModelMap model, LoadingFee entity, String rateQuery) {
		List<LoadingFee> loadingFeeList = genericDAO.executeSimpleQuery(rateQuery);
		if (loadingFeeList == null || loadingFeeList.isEmpty()) {
			return StringUtils.EMPTY;
		}
      
      for (LoadingFee loadingFee : loadingFeeList) {
	   	if ((entity.getValidFrom()).getTime() >= loadingFee.getValidFrom().getTime() 
	   			&& entity.getValidTo().getTime() <= loadingFee.getValidTo().getTime()
	   	|| (entity.getValidFrom().getTime() <= loadingFee.getValidFrom().getTime() 
	   		&& entity.getValidTo().getTime() >= loadingFee.getValidTo().getTime())  
	   	|| ((entity.getValidFrom().getTime() >= loadingFee.getValidFrom().getTime() 
	   		&& entity.getValidFrom().getTime() <=loadingFee.getValidTo().getTime())
	   		&& entity.getValidTo().getTime()>= loadingFee.getValidTo().getTime())
	   	|| (entity.getValidFrom().getTime() <= loadingFee.getValidFrom().getTime()
	   		&& (entity.getValidTo().getTime()>=loadingFee.getValidFrom().getTime()
	   		&& entity.getValidTo().getTime()<= loadingFee.getValidTo().getTime()))) {       		
	   		return "error";
	   	}      	
      }
         
      return StringUtils.EMPTY;
	}
}
