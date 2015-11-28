package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.primovision.lutransport.model.FuelSurchargePadd;
import com.primovision.lutransport.model.FuelSurchargeWeeklyRate;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.service.DateUpdateService;


@Controller
@RequestMapping("/admin/fuelsurchargepadd")
public class FuelSurchargePaddController extends CRUDController<FuelSurchargePadd> {
	
	public FuelSurchargePaddController(){
		setUrlContext("admin/fuelsurchargepadd");
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
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(
				Long.class, false));
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
	
	
	
	//from
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		String type=(String) request.getSession().getAttribute("typ");		
		setupCreate(model, request);			
		    model.addAttribute("type", type);		
		return urlContext + "/form";
	}
	
	
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {		
		String type=(String) request.getSession().getAttribute("typ");		
		setupUpdate(model, request);				
		    model.addAttribute("type", type);			
		return urlContext + "/form";
	}
	
	
	
	@Override
	public String delete(@ModelAttribute("modelObject") FuelSurchargePadd entity,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			genericDAO.delete(entity);
		} catch (Exception ex) {
			request.getSession().setAttribute(
					"errors",
					"This" + entity.getClass().getSimpleName()
							+ " can't be deleted");
			log.warn("Error deleting record " + entity.getId(), ex);
		}
		// return to list
		if(request.getSession().getAttribute("typ")!=null) 			
			return "redirect:/rate/expirationreport/list.do?type=surchargepadd";
		
		return "redirect:/" + urlContext + "/list.do";
	}
	@RequestMapping("/changestatus.do")
	public String changeStatus(HttpServletRequest request, ModelMap modMap) {
		String type=(String) request.getSession().getAttribute("typ");	
		if(type!=null){	
			if(request.getParameter("status").equalsIgnoreCase("current")){
				request.getSession().setAttribute("error", "Unable to process your request. Please check rate status");
				return "redirect:/rate/expirationreport/list.do?type=surchargepadd";				
			}
			FuelSurchargePadd fuelSurchargePadd = genericDAO.getById(FuelSurchargePadd.class,
		    Long.valueOf(request.getParameter("id")));
		if (fuelSurchargePadd.getStatus() == 1) 
			fuelSurchargePadd.setStatus(0);		 	
		genericDAO.save(fuelSurchargePadd);
		return "redirect:/rate/expirationreport/list.do?type=surchargepadd";
		}
		else{			
			FuelSurchargePadd fuelSurchargePadd = genericDAO.getById(FuelSurchargePadd.class,
			Long.valueOf(request.getParameter("id")));
			if (fuelSurchargePadd.getStatus() == 0) 
				fuelSurchargePadd.setStatus(1);		 	
			genericDAO.save(fuelSurchargePadd);
			return "redirect:/" + urlContext + "/list.do";
		}
		
	}

	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") FuelSurchargePadd entity,
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
			model.addAttribute("type",(String) request.getSession().getAttribute("typ"));
			return urlContext + "/form";
		}
		
		Date validFromTemp=entity.getValidFromTemp();
		Date validToTemp=entity.getValidToTemp();
		
		if(entity.getValidFromTemp()!=null && entity.getValidToTemp() !=null ){
			
			
			  String rateQuery = "select obj from FuelSurchargePadd obj where obj.code='"
				    + entity.getCode()					
					+"' and obj.validFrom !='"
					+ validFromTemp
					+ "' and obj.validTo != '"
					+validToTemp+"'";					  
			  String result=common(request,model,entity,rateQuery);
             
             if(result.equalsIgnoreCase("error")){	            	   
             setupUpdate(model, request);	
       		model.addAttribute("type",(String) request.getSession().getAttribute("typ"));	
       		request.getSession().setAttribute("error", "Rate already exists for specified date range.");
 				return urlContext + "/form"; 
             }  		      		
	}
	else{			
  String rateQuery = "select obj from FuelSurchargePadd obj where obj.code='"
		+ entity.getCode()+"' order by obj.validFrom desc";
     String result=common(request,model,entity,rateQuery);
     
     if(result.equalsIgnoreCase("error")){            	
     setupUpdate(model, request);	
		model.addAttribute("type",(String) request.getSession().getAttribute("typ"));	
		request.getSession().setAttribute("error", "Rate already exists for specified date range.");
			return urlContext + "/form"; 
     }
 }
		
		
		beforeSave(request, entity, model);
		// merge into datasource
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		// return to list		
		if(request.getParameter("typ")!="")   
			return "redirect:/rate/expirationreport/list.do?type=surchargepadd";
	
		return "redirect:/" + urlContext + "/list.do";
	}
	
	//here
	
	
	
	
	private String common(HttpServletRequest request,ModelMap model,FuelSurchargePadd entity,String rateQuery)
	{
		String result="";
		 List<FuelSurchargePadd> fuelSurchargePadds = genericDAO.executeSimpleQuery(rateQuery);
         
         if (fuelSurchargePadds  != null && fuelSurchargePadds .size() > 0) { 
    	   
      	   for(FuelSurchargePadd fuelSurchargePadd :fuelSurchargePadds ){
      	
      	if((entity.getValidFrom()).getTime() >= fuelSurchargePadd.getValidFrom().getTime() && entity.getValidTo().getTime()<= fuelSurchargePadd.getValidTo().getTime()
      	||(entity.getValidFrom().getTime() <= fuelSurchargePadd.getValidFrom().getTime() && entity.getValidTo().getTime()>= fuelSurchargePadd.getValidTo().getTime())  
      	|| ((entity.getValidFrom().getTime() >= fuelSurchargePadd.getValidFrom().getTime() && entity.getValidFrom().getTime()<=fuelSurchargePadd.getValidTo().getTime()) && entity.getValidTo().getTime()>= fuelSurchargePadd.getValidTo().getTime())
      	|| (entity.getValidFrom().getTime() <= fuelSurchargePadd.getValidFrom().getTime() && (entity.getValidTo().getTime()>=fuelSurchargePadd.getValidFrom().getTime()   && entity.getValidTo().getTime()<= fuelSurchargePadd.getValidTo().getTime()))){       		
      		    result="error";      		   
      		    return result;
      	}      	
         }   
         }   
         return "";
         }
	

}
