package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.TonnagePremium;
import com.primovision.lutransport.service.DateUpdateService;

@Controller
@RequestMapping("/admin/tonnagepremium")
public class TonnagePremiumController extends CRUDController<TonnagePremium> {
	
	public TonnagePremiumController(){
		setUrlContext("admin/tonnagepremium");
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
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		dateupdateService.updateDate(request, "validFromDate", "validFrom");
		dateupdateService.updateDate(request, "validToDate", "validTo");
		model.addAttribute("list",
				genericDAO.search(getEntityClass(), criteria,"validFrom desc,code ",false));
		return urlContext + "/list";
	}
	
	
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"validFrom desc,code ",false));
		return urlContext + "/list";
	}
	
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") TonnagePremium entity,
			BindingResult bindingResult, ModelMap model) {
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
		
		
		Date validFromTemp=entity.getValidFromTemp();
		Date validToTemp=entity.getValidToTemp();
		
		if(entity.getValidFromTemp()!=null && entity.getValidToTemp() !=null ){
			
			
			  String rateQuery = "select obj from TonnagePremium obj where obj.code='"
				    + entity.getCode()					
					+"' and obj.validFrom !='"
					+ validFromTemp
					+ "' and obj.validTo != '"
					+validToTemp+"'";					  
			  String result=common(request,model,entity,rateQuery);
             
             if(result.equalsIgnoreCase("error")){	            	   
             setupUpdate(model, request);	      		
       		request.getSession().setAttribute("error", "Rate already exists for specified date range.");
 				return urlContext + "/form"; 
             }  		      		
	}
	else{			
  String rateQuery = "select obj from TonnagePremium obj where obj.code='"
		+ entity.getCode()+"' order by obj.validFrom desc";
     String result=common(request,model,entity,rateQuery);
     
     if(result.equalsIgnoreCase("error")){            	
     setupUpdate(model, request);			
		request.getSession().setAttribute("error", "Rate already exists for specified date range.");
			return urlContext + "/form"; 
     }
 }
		
		
		
		
		beforeSave(request, entity, model);
		// merge into datasource
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		// return to list
		return "redirect:/" + urlContext + "/list.do";
	}
	
	
	
	private String common(HttpServletRequest request,ModelMap model,TonnagePremium entity,String rateQuery)
	{
		String result="";
		 List<TonnagePremium> tonnagePremiums = genericDAO.executeSimpleQuery(rateQuery);
         
         if (tonnagePremiums  != null && tonnagePremiums .size() > 0) { 
    	   
      	   for(TonnagePremium tonnagePremium :tonnagePremiums ){
      	
      	if((entity.getValidFrom()).getTime() >= tonnagePremium.getValidFrom().getTime() && entity.getValidTo().getTime()<= tonnagePremium.getValidTo().getTime()
      	||(entity.getValidFrom().getTime() <= tonnagePremium.getValidFrom().getTime() && entity.getValidTo().getTime()>= tonnagePremium.getValidTo().getTime())  
      	|| ((entity.getValidFrom().getTime() >= tonnagePremium.getValidFrom().getTime() && entity.getValidFrom().getTime()<=tonnagePremium.getValidTo().getTime()) && entity.getValidTo().getTime()>= tonnagePremium.getValidTo().getTime())
      	|| (entity.getValidFrom().getTime() <= tonnagePremium.getValidFrom().getTime() && (entity.getValidTo().getTime()>=tonnagePremium.getValidFrom().getTime()   && entity.getValidTo().getTime()<= tonnagePremium.getValidTo().getTime()))){       		
      		    result="error";      		   
      		    return result;
      	}      	
         }   
         }   
         return "";
         }
	
	

}
