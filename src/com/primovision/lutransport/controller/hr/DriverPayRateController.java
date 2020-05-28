package com.primovision.lutransport.controller.hr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.hr.DriverPayRate;
import com.primovision.lutransport.model.hr.EmployeeCatagory;

/**
 * @author kishor
 *
 */
@Controller
@RequestMapping("/hr/driverpayrate")
public class DriverPayRateController extends CRUDController<DriverPayRate>{
	
	public DriverPayRateController() {
		setUrlContext("/hr/driverpayrate");
	}

	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
		Map criterias = new HashMap();
		criterias.clear();
		criterias.put("type", 1);
		model.addAttribute("transferStation",genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		criterias.put("type", 2);
		model.addAttribute("landfill",genericDAO.findByCriteria(Location.class, criterias, "name", false));
	
	}
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("catagories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		
		if(request.getParameter("id")!=null){
			criterias.clear();
			criterias.put("type", 1);
			model.addAttribute("transferStation",genericDAO.findByCriteria(Location.class, criterias, "name", false));
			criterias.clear();
			criterias.put("type", 2);
			model.addAttribute("landfill",genericDAO.findByCriteria(Location.class, criterias, "name", false));
		}else{
			criterias.clear();
			criterias.put("status", 1);
			criterias.put("type", 1);
			model.addAttribute("transferStation",genericDAO.findByCriteria(Location.class, criterias, "name", false));
			criterias.clear();
			criterias.put("status", 1);
			criterias.put("type", 2);
			model.addAttribute("landfill",genericDAO.findByCriteria(Location.class, criterias, "name", false));
		}

		
		model.addAttribute("rateTypes", listStaticData("RATE_TYPE"));
		model.addAttribute("rateUsing", listStaticData("RATE_USING"));
		
		String fromAlertPage = request.getParameter("fromAlertPage");
		if (StringUtils.isNotEmpty(fromAlertPage) && BooleanUtils.toBoolean(fromAlertPage)) {
			model.addAttribute("fromAlertPage", "true");
		}
	}
	
	@Override
	 public void initBinder(WebDataBinder binder) { 
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(EmployeeCatagory.class, new AbstractModelEditor(EmployeeCatagory.class));
	}
	/*@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") DriverPayRate entity,
			BindingResult bindingResult, ModelMap model) {
		if(entity.getTransferStation() == null){
			bindingResult.rejectValue("transferStation", "error.select.option", null, null);
		}
		if(entity.getLandfill() == null){
			bindingResult.rejectValue("landfill", "error.select.option", null, null);
		}
		if(entity.getRateType() == null){
			bindingResult.rejectValue("rateType", "error.select.option", null, null);
		}
		if(entity.getRateUsing()== null){
			bindingResult.rejectValue("rateUsing", "error.select.option", null, null);
		}
		if(entity.getCatagory()==null){
			bindingResult.rejectValue("catagory", "error.select.option",
					null, null);
		}
		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option",
					null, null);
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option",
					null, null);
		}
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		
		// return to form if we had errors
		if (bindingResult.hasErrors()) {
			System.out.println("\n bindingResult-->"+bindingResult.getFieldError());
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
		return super.save(request, entity, bindingResult, model);
	}*/
	
	
	
	
	@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") DriverPayRate entity,
			BindingResult bindingResult, ModelMap model) {
		
		if(entity.getTransferStation() == null){
			bindingResult.rejectValue("transferStation", "error.select.option", null, null);
		}
		if(entity.getLandfill() == null){
			bindingResult.rejectValue("landfill", "error.select.option", null, null);
		}
		if(entity.getRateType() == null){
			bindingResult.rejectValue("rateType", "error.select.option", null, null);
		}
		if(entity.getRateUsing()== null){
			bindingResult.rejectValue("rateUsing", "error.select.option", null, null);
		}
		if(entity.getCatagory()==null){
			bindingResult.rejectValue("catagory", "error.select.option",
					null, null);
		}
		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option",
					null, null);
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option",
					null, null);
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
			return urlContext + "/form";
		}
		
		Date validFromTemp=entity.getValidFromTemp();
		Date validToTemp=entity.getValidToTemp();
		
		if(entity.getValidFromTemp()!=null && entity.getValidToTemp() !=null ){
			if(entity.getValidFrom().getTime()== validFromTemp.getTime() && entity.getValidTo().getTime()== validToTemp.getTime()){					
					// do nothing			
			}
			else{		
				String rateQuery = "select obj from DriverPayRate obj where obj.company='"
					+ entity.getCompany().getId()
					+ "' and obj.terminal='"					
					+ entity.getTerminal().getId()
					+ "' and obj.transferStation='"					
					+ entity.getTransferStation().getId()
					+ "' and obj.landfill='"					
					+ entity.getLandfill().getId()
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
				
				
				//Newly added
				
				Location location = genericDAO.getById(Location.class, entity
						.getLandfill().getId());
				
				if (location.getName().equalsIgnoreCase("grows") || location.getName().equalsIgnoreCase("tullytown")) {
	                    
	                    String rateQuery2 = "select obj from DriverPayRate obj where obj.company='"
	        				+ entity.getCompany().getId()
	        				+ "' and obj.terminal='"					
	        				+ entity.getTerminal().getId()
	        				+ "' and obj.transferStation='"					
	        				+ entity.getTransferStation().getId()
	        				+ "' and obj.landfill='91'"
	        				+" and obj.validFrom !='"
	    					+ validFromTemp
	    					+ "' and obj.validTo != '"
	    					+validToTemp+"'";
	                    
	                    String result2=common(request,model,entity,rateQuery2);
	                    
	        			if(result2.equalsIgnoreCase("error")){    
	        				setupUpdate(model, request);	
	        				model.addAttribute("type",(String) request.getSession().getAttribute("typ"));	
	        				request.getSession().setAttribute("error", "Tullytown/Grows Rate already exists for specified date range.");
	        				return urlContext + "/form"; 
	        			}
	           }
				else if(location.getName().equalsIgnoreCase("grows/tullytown")){
					
					String rateQuery2 = "select obj from DriverPayRate obj where obj.company='"
	    				+ entity.getCompany().getId()
	    				+ "' and obj.terminal='"					
	    				+ entity.getTerminal().getId()
	    				+ "' and obj.transferStation='"					
	    				+ entity.getTransferStation().getId()
	    				+ "' and obj.landfill='253'"
	    				+" and obj.validFrom !='"
						+ validFromTemp
						+ "' and obj.validTo != '"
						+validToTemp+"'";
					
	                String result2=common(request,model,entity,rateQuery2);
	    			
	    			
	    			String rateQuery3 = "select obj from DriverPayRate obj where obj.company='"
	    				+ entity.getCompany().getId()
	    				+ "' and obj.terminal='"					
	    				+ entity.getTerminal().getId()
	    				+ "' and obj.transferStation='"					
	    				+ entity.getTransferStation().getId()
	    				+ "' and obj.landfill='254' order by obj.validFrom desc";
	                String result3=common(request,model,entity,rateQuery3);
	                
	    			if(result3.equalsIgnoreCase("error") && result2.equalsIgnoreCase("error")){    
	    				setupUpdate(model, request);	
	    				model.addAttribute("type",(String) request.getSession().getAttribute("typ"));	
	    				request.getSession().setAttribute("error", "Grows and Tullytown Rate already exists for specified date range.");
	    				return urlContext + "/form"; 
	    			}
				}
				
				
			}		
		}
		else{	
			String rateQuery = "select obj from DriverPayRate obj where obj.company='"
				+ entity.getCompany().getId()
				+ "' and obj.terminal='"					
					+ entity.getTerminal().getId()
					+ "' and obj.transferStation='"					
					+ entity.getTransferStation().getId()
					+ "' and obj.landfill='"					
					+ entity.getLandfill().getId()+ "' order by obj.validFrom desc";
			String result=common(request,model,entity,rateQuery);
			if(result.equalsIgnoreCase("error")){    
				setupUpdate(model, request);	
				model.addAttribute("type",(String) request.getSession().getAttribute("typ"));	
				request.getSession().setAttribute("error", "Rate already exists for specified date range.");
				return urlContext + "/form"; 
			}
			
			// Newly added
			Location location = genericDAO.getById(Location.class, entity
					.getLandfill().getId());
			
			if (location.getName().equalsIgnoreCase("grows") || location.getName().equalsIgnoreCase("tullytown")) {
                    
                    String rateQuery2 = "select obj from DriverPayRate obj where obj.company='"
        				+ entity.getCompany().getId()
        				+ "' and obj.terminal='"					
        				+ entity.getTerminal().getId()
        				+ "' and obj.transferStation='"					
        				+ entity.getTransferStation().getId()
        				+ "' and obj.landfill='91' order by obj.validFrom desc";
                    
                    String result2=common(request,model,entity,rateQuery2);
                    
        			if(result2.equalsIgnoreCase("error")){    
        				setupUpdate(model, request);	
        				model.addAttribute("type",(String) request.getSession().getAttribute("typ"));	
        				request.getSession().setAttribute("error", "Tullytown/Grows Rate already exists for specified date range.");
        				return urlContext + "/form"; 
        			}
           }
			else if(location.getName().equalsIgnoreCase("grows/tullytown")){
				
				String rateQuery2 = "select obj from DriverPayRate obj where obj.company='"
    				+ entity.getCompany().getId()
    				+ "' and obj.terminal='"					
    				+ entity.getTerminal().getId()
    				+ "' and obj.transferStation='"					
    				+ entity.getTransferStation().getId()
    				+ "' and obj.landfill='253' order by obj.validFrom desc";
                String result2=common(request,model,entity,rateQuery2);
    			
    			
    			String rateQuery3 = "select obj from DriverPayRate obj where obj.company='"
    				+ entity.getCompany().getId()
    				+ "' and obj.terminal='"					
    				+ entity.getTerminal().getId()
    				+ "' and obj.transferStation='"					
    				+ entity.getTransferStation().getId()
    				+ "' and obj.landfill='254' order by obj.validFrom desc";
                String result3=common(request,model,entity,rateQuery3);
                
    			if(result3.equalsIgnoreCase("error") && result2.equalsIgnoreCase("error")){    
    				setupUpdate(model, request);	
    				model.addAttribute("type",(String) request.getSession().getAttribute("typ"));	
    				request.getSession().setAttribute("error", "Grows and Tullytown Rate already exists for specified date range.");
    				return urlContext + "/form"; 
    			}
			}
			
		}
		
		beforeSave(request, entity, model);			
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		String fromAlertPage = request.getParameter("fromAlertPage");
		if (StringUtils.isNotEmpty(fromAlertPage) && BooleanUtils.toBoolean(fromAlertPage)) { 
			return "redirect:/hr/payrollratealert/list.do?type=driverPayRate";
		} else {
			return "redirect:/" + urlContext + "/list.do";
		}
		
		//return super.save(request, entity, bindingResult, model);
	}
	
	@Override
	public String delete(@ModelAttribute("modelObject") DriverPayRate entity,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			genericDAO.delete(entity);
		} catch (Exception ex) {
			request.getSession().setAttribute(
					"errors",
					"This" + entity.getClass().getSimpleName() + " can't be deleted");
			log.warn("Error deleting record " + entity.getId(), ex);
		}
				
		String fromAlertPage = request.getParameter("fromAlertPage");
		if(StringUtils.isNotEmpty(fromAlertPage) && BooleanUtils.toBoolean(fromAlertPage)) { 
			return "redirect:/hr/payrollratealert/list.do?type=driverPayRate";
		} else {
			return "redirect:/" + urlContext + "/list.do";
		}
	}
	
	@RequestMapping("/changeAlertStatus.do")
	public String changeAlertStatus(HttpServletRequest request, ModelMap modMap) {
		DriverPayRate driverPayRate = genericDAO.getById(DriverPayRate.class, Long.valueOf(request.getParameter("id")));
		String redirectUrl = StringUtils.EMPTY;
		String fromAlertPage = request.getParameter("fromAlertPage");
		if (StringUtils.isNotEmpty(fromAlertPage) && BooleanUtils.toBoolean(fromAlertPage)) {
			redirectUrl = "redirect:/hr/payrollratealert/list.do?type=driverPayRate";
			if (driverPayRate.getAlertStatus() == 1) { 
				driverPayRate.setAlertStatus(0);
			}
		} else {			
			if (driverPayRate.getAlertStatus() == 0) {
				driverPayRate.setAlertStatus(1);
			}
			redirectUrl = "redirect:/" + urlContext + "/list.do";
		}
		
		genericDAO.save(driverPayRate);
		return redirectUrl;
	}
	
	private String common(HttpServletRequest request,ModelMap model,DriverPayRate entity,String rateQuery)
	{
		String result="";
		List<DriverPayRate> driverPayRates = genericDAO.executeSimpleQuery(rateQuery);		
		if (driverPayRates != null && driverPayRates.size() > 0) { 
			for(DriverPayRate driverPayRate: driverPayRates){
				if((entity.getValidFrom().getTime() >= driverPayRate.getValidFrom().getTime() && entity.getValidTo().getTime()<= driverPayRate.getValidTo().getTime())
						||(entity.getValidFrom().getTime() <= driverPayRate.getValidFrom().getTime() && entity.getValidTo().getTime()>= driverPayRate.getValidTo().getTime())  
						|| ((entity.getValidFrom().getTime() >= driverPayRate.getValidFrom().getTime() && entity.getValidFrom().getTime()<=driverPayRate.getValidTo().getTime()) && entity.getValidTo().getTime()>= driverPayRate.getValidTo().getTime())
						|| (entity.getValidFrom().getTime() <= driverPayRate.getValidFrom().getTime() && (entity.getValidTo().getTime()>=driverPayRate.getValidFrom().getTime()   && entity.getValidTo().getTime()<= driverPayRate.getValidTo().getTime()))){       		
					result="error";      		   
					return result;
				}      	
			}   
		}   
		return "";
	}
	
	
	
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		if("findDestinationLoad".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("origin"))){
		
				List<Location> destinatioLoad=new ArrayList<Location>();
				Map criterias = new HashMap();
				criterias.put("transferStation.id", Long.parseLong(request.getParameter("origin")));
				List<DriverPayRate> billing=genericDAO.findByCriteria(DriverPayRate.class, criterias, "transferStation", false);
				for(DriverPayRate bill:billing){
					destinatioLoad.add(bill.getLandfill());
				}
				Gson gson = new Gson();
				return gson.toJson(destinatioLoad);
			}
		}
		if("findOriginLoad".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("destination"))){
				
				List<Location> originLoad=new ArrayList<Location>();
				Map criterias = new HashMap();
				criterias.put("landfill.id", Long.parseLong(request.getParameter("destination")));
				List<DriverPayRate> billing=genericDAO.findByCriteria(DriverPayRate.class, criterias, "landfill", false);
				for(DriverPayRate bill:billing){
					originLoad.add(bill.getTransferStation());
				}
				Gson gson = new Gson();
				return gson.toJson(originLoad);
			}
		}
		return "";
	}
}
