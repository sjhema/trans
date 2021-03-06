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
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.FuelSurchargeWeeklyRate;
import com.primovision.lutransport.model.Location;

@Controller
@RequestMapping("/admin/fuelsurchargeweeklyrate")
public class FuelSurchargeWeeklyRateController extends CRUDController<FuelSurchargeWeeklyRate> {
	
	public FuelSurchargeWeeklyRateController(){
		setUrlContext("admin/fuelsurchargeweeklyrate");
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(
				Location.class));
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		/*criterias.put("type", 1);
		model.addAttribute("transferStations", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
		criterias.put("type", 2);
		model.addAttribute("landfillStations", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		*/
		if(request.getParameter("id")!=null){
			criterias.clear();
			criterias.put("type", 1);
			model.addAttribute("transferStations", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
			criterias.clear();
			criterias.put("type", 2);
			//criterias.put("id!",91l);
			model.addAttribute("landfillStations", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		}else{
			criterias.clear();
			criterias.put("status", 1);
			criterias.put("type", 1);
			model.addAttribute("transferStations", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
			criterias.clear();
			criterias.put("status", 1);
			criterias.put("type", 2);
			//criterias.put("id!",91l);
			model.addAttribute("landfillStations", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		}		
		
		model.addAttribute("rateTypes", listStaticData("RATE_TYPE"));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
		Map criterias = new HashMap();
		criterias.clear();
		criterias.put("type", 1);
		model.addAttribute("transferStations", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
		criterias.clear();
		criterias.put("type", 2);
		//criterias.put("id!",91l);
		model.addAttribute("landfillStations", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") FuelSurchargeWeeklyRate entity, BindingResult bindingResult,
			ModelMap model) {
		if(entity.getTransferStations()==null){
			bindingResult.rejectValue("transferStations", "error.select.option", null, null);
		}
		if(entity.getLandfillStations()==null){
			bindingResult.rejectValue("landfillStations", "error.select.option", null, null);
		}
		if(entity.getRateType() == null){
			bindingResult.rejectValue("rateType", "error.select.option", null, null);
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
			model.addAttribute("type",(String) request.getSession().getAttribute("typ"));
			return urlContext + "/form";
		}
		
		
		
		Date validFromTemp=entity.getValidFromTemp();
		Date validToTemp=entity.getValidToTemp();
		
		if(entity.getValidFromTemp()!=null && entity.getValidToTemp() !=null ){
			
									
				  String rateQuery = "select obj from FuelSurchargeWeeklyRate obj where obj.transferStations='"
						+ entity.getTransferStations().getId()
						+ "' and obj.landfillStations='"					
						+ entity.getLandfillStations().getId()
						+"' and obj.fromDate !='"
						+ validFromTemp
						+ "' and obj.toDate != '"
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
	    String rateQuery = "select obj from FuelSurchargeWeeklyRate obj where obj.transferStations='"
			+ entity.getTransferStations().getId()
			+ "' and obj.landfillStations='"					
			+ entity.getLandfillStations().getId()+ "' order by obj.fromDate desc";
           String result=common(request,model,entity,rateQuery);
           
           if(result.equalsIgnoreCase("error")){            	
           setupUpdate(model, request);	
     		model.addAttribute("type",(String) request.getSession().getAttribute("typ"));	
     		request.getSession().setAttribute("error", "Rate already exists for specified date range.");
				return urlContext + "/form"; 
           }
       }
		
		beforeSave(request, entity, model);			
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);			
		    if(request.getParameter("typ")!="")   
			return "redirect:/rate/expirationreport/list.do?type=weeklyrate";
	
		return "redirect:/" + urlContext + "/list.do";
		/*return super.save(request, entity, bindingResult, model);*/
	}
	
	
	private String common(HttpServletRequest request,ModelMap model,FuelSurchargeWeeklyRate entity,String rateQuery)
	{
		String result="";
		 List<FuelSurchargeWeeklyRate> fsweeklycharges = genericDAO.executeSimpleQuery(rateQuery);
         
         if (fsweeklycharges != null && fsweeklycharges.size() > 0) { 
    	   
      	   for(FuelSurchargeWeeklyRate fsweeklycharge:fsweeklycharges){
      	
      	if((entity.getFromDate().getTime() >= fsweeklycharge.getFromDate().getTime() && entity.getToDate().getTime()<= fsweeklycharge.getToDate().getTime())
      	||(entity.getFromDate().getTime() <= fsweeklycharge.getFromDate().getTime() && entity.getToDate().getTime()>= fsweeklycharge.getToDate().getTime())  
      	|| ((entity.getFromDate().getTime() >= fsweeklycharge.getFromDate().getTime() && entity.getFromDate().getTime()<=fsweeklycharge.getToDate().getTime()) && entity.getToDate().getTime()>= fsweeklycharge.getToDate().getTime())
      	|| (entity.getFromDate().getTime() <= fsweeklycharge.getFromDate().getTime() && (entity.getToDate().getTime()>=fsweeklycharge.getFromDate().getTime()   && entity.getToDate().getTime()<= fsweeklycharge.getToDate().getTime()))){       		
      		    result="error";      		   
      		    return result;
      	}      	
         }   
         }   
         return "";
         }
	
	
	
	
	
	
	
	@RequestMapping("/changestatus.do")
	public String changeStatus(HttpServletRequest request, ModelMap modMap) {
		String type=(String) request.getSession().getAttribute("typ");	
		if(type!=null){	
			if(request.getParameter("status").equalsIgnoreCase("current")){
				request.getSession().setAttribute("error", "Unable to process your request. Please check rate status");
				return "redirect:/rate/expirationreport/list.do?type=weeklyrate";				
			}
			FuelSurchargeWeeklyRate fuelSurchargeWeeklyRate = genericDAO.getById(FuelSurchargeWeeklyRate.class,
		    Long.valueOf(request.getParameter("id")));
		if (fuelSurchargeWeeklyRate.getStatus() == 1) 
			fuelSurchargeWeeklyRate.setStatus(0);		 	
		genericDAO.save(fuelSurchargeWeeklyRate);
		return "redirect:/rate/expirationreport/list.do?type=weeklyrate";
		}
		else{			
			FuelSurchargeWeeklyRate fuelSurchargeWeeklyRate = genericDAO.getById(FuelSurchargeWeeklyRate.class,
			Long.valueOf(request.getParameter("id")));
			if (fuelSurchargeWeeklyRate.getStatus() == 0) 
				fuelSurchargeWeeklyRate.setStatus(1);		 	
			genericDAO.save(fuelSurchargeWeeklyRate);
			return "redirect:/" + urlContext + "/list.do";
		}
		
	}
	
	//from
	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {		
		String type=(String) request.getSession().getAttribute("typ");		
		setupUpdate(model, request);				
		    model.addAttribute("type", type);		
		return urlContext + "/form";
	}
	
	@Override
	public String delete(@ModelAttribute("modelObject") FuelSurchargeWeeklyRate entity,
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
			return "redirect:/rate/expirationreport/list.do?type=weeklyrate";
		
		return "redirect:/" + urlContext + "/list.do";
	}
	
	@Override
	public String create(ModelMap model, HttpServletRequest request) {
		String type=(String) request.getSession().getAttribute("typ");		
		setupCreate(model, request);				
		 model.addAttribute("type", type);		
		return urlContext + "/form";
	}
	
	//here
}
