package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.DemurrageCharges;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.service.DateUpdateService;


@Controller
@RequestMapping("admin/demurragecharges")
public class DemurrageChargesController extends CRUDController<DemurrageCharges> {
	
	public DemurrageChargesController(){
		setUrlContext("admin/demurragecharges");
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
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("dataType", "TIMES_USED");
		map.put("dataValue", "1,2");
		model.addAttribute("timesUsed", genericDAO.findByCriteria(StaticData.class, map,"dataText",false));
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		dateupdateService.updateDate(request, "validFromDate", "validFrom");
		dateupdateService.updateDate(request, "validToDate", "validTo");
		model.addAttribute("list",
				genericDAO.search(getEntityClass(), criteria,"validFrom desc,demurragecode ",false));
		return urlContext + "/list";
	}	
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"validFrom desc,demurragecode ",false));
		return urlContext + "/list";
	}
	
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") DemurrageCharges entity,
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
			
			
			  String rateQuery = "select obj from DemurrageCharges obj where obj.demurragecode='"
				    + entity.getDemurragecode()					
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
		String rateQuery = "select obj from DemurrageCharges obj where obj.demurragecode='"
		+ entity.getDemurragecode()+"' order by obj.validFrom desc";
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
	
	
	
	private String common(HttpServletRequest request,ModelMap model,DemurrageCharges entity,String rateQuery)
	{
		String result="";
		 List<DemurrageCharges> demurrageCharges = genericDAO.executeSimpleQuery(rateQuery);
         
         if (demurrageCharges != null && demurrageCharges .size() > 0) { 
    	   
      	   for(DemurrageCharges demurrageCharge :demurrageCharges ){
      	
      	if((entity.getValidFrom()).getTime() >= demurrageCharge.getValidFrom().getTime() && entity.getValidTo().getTime()<= demurrageCharge.getValidTo().getTime()
      	||(entity.getValidFrom().getTime() <= demurrageCharge.getValidFrom().getTime() && entity.getValidTo().getTime()>= demurrageCharge.getValidTo().getTime())  
      	|| ((entity.getValidFrom().getTime() >= demurrageCharge.getValidFrom().getTime() && entity.getValidFrom().getTime()<=demurrageCharge.getValidTo().getTime()) && entity.getValidTo().getTime()>= demurrageCharge.getValidTo().getTime())
      	|| (entity.getValidFrom().getTime() <= demurrageCharge.getValidFrom().getTime() && (entity.getValidTo().getTime()>=demurrageCharge.getValidFrom().getTime()   && entity.getValidTo().getTime()<= demurrageCharge.getValidTo().getTime()))){       		
      		    result="error";      		   
      		    return result;
      	}      	
         }   
         }   
         return "";
         }
	
}
