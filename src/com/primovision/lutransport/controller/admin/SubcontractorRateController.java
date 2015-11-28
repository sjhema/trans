package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.FuelSurchargePadd;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.SubcontractorRate;
import com.primovision.lutransport.model.TonnagePremium;

@Controller
@SuppressWarnings("unchecked")
@RequestMapping("/admin/subcontractorrate")
public class SubcontractorRateController extends
		CRUDController<SubcontractorRate> {

	public SubcontractorRateController() {
		setUrlContext("/admin/subcontractorrate");
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
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(
				Location.class));
		binder.registerCustomEditor(SubContractor.class, new AbstractModelEditor(
				SubContractor.class));
		
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		/*criterias.put("type", 1);
		model.addAttribute("transferStation", genericDAO.findByCriteria(
				Location.class, criterias, "name", false));
		criterias.put("type", 2);
		model.addAttribute("landfill", genericDAO.findByCriteria(
				Location.class, criterias, "name", false));*/
		
		
		if(request.getParameter("id")!=null){
			criterias.clear();
			criterias.put("type", 1);
			model.addAttribute("transferStation", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
			criterias.clear();
			criterias.put("type", 2);
			//criterias.put("id!",91l);
			model.addAttribute("landfill", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		}else{
			criterias.clear();
			criterias.put("status", 1);
			criterias.put("type", 1);
			model.addAttribute("transferStation", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
			criterias.clear();
			criterias.put("status", 1);
			criterias.put("type", 2);
			//criterias.put("id!",91l);
			model.addAttribute("landfill", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		}	
		
		
		criterias.put("type", 3);
		model.addAttribute("companyLocation", genericDAO.findByCriteria(
				Location.class, criterias, "name", false));
		model.addAttribute("rateTypes", listStaticData("RATE_TYPE"));
		model.addAttribute("billUsing", listStaticData("BILL_USING"));
		model.addAttribute("rateUsing", listStaticData("RATE_USING"));
	    
		criterias.clear();
		criterias.put("status", 1);
		model.addAttribute("subContractor", genericDAO.findByCriteria(
				SubContractor.class, criterias, "name", false));
		
	}

	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
		Map criterias = new HashMap();
		criterias.clear();
		criterias.put("type", 1);
		model.addAttribute("transferStation", genericDAO.findByCriteria(Location.class,	criterias, "name", false));
		criterias.clear();
		criterias.put("type", 2);
		//criterias.put("id!",91l);
		model.addAttribute("landfill", genericDAO.findByCriteria(Location.class, criterias, "name", false));
	
	}

	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") SubcontractorRate entity,
			BindingResult bindingResult, ModelMap model) {

		if (entity.getCompanyLocation() == null) {
			bindingResult.rejectValue("companyLocation", "error.select.option",
					null, null);
		}
		if (entity.getSubcontractor() == null) {
			bindingResult.rejectValue("subcontractor", "error.select.option",
					null, null);
		}
		if (entity.getTransferStation() == null) {
			bindingResult.rejectValue("transferStation", "error.select.option",
					null, null);
		}
		if (entity.getLandfill() == null) {
			bindingResult.rejectValue("landfill", "error.select.option", null,
					null);
		}
		if (entity.getRateType() == null) {
			bindingResult.rejectValue("rateType", "error.select.option", null,
					null);
		}
		if (entity.getBillUsing() == null) {
			bindingResult.rejectValue("billUsing", "error.select.option", null,
					null);
		}
		if (entity.getSortBy() == null) {
			bindingResult.rejectValue("sortBy", "error.select.option", null,
					null);
		}
		if(entity.getRateUsing()==null){
			bindingResult.rejectValue("rateUsing", "error.select.option", null,
					null);
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
			
								
				  String rateQuery = "select obj from SubcontractorRate obj where obj.subcontractor='"
					  +entity.getSubcontractor().getId()
					  + "' and transferStation='"					  
						+ entity.getTransferStation().getId()
						+ "' and obj.landfill='"					
						+ entity.getLandfill().getId()
						+"' and id not in ("+entity.getId()+")";					  
				  String result=common(request,model,entity,rateQuery);   
				  if(result.equalsIgnoreCase("error")){
		               setupUpdate(model, request);	
		         		model.addAttribute("type",(String) request.getSession().getAttribute("typ"));	
		         		request.getSession().setAttribute("error", "Rate already exists for specified date range.");
		   				return urlContext + "/form"; 
		               }
		      	
		}
		else{			
	    String rateQuery = "select obj from SubcontractorRate obj where obj.subcontractor='"
					  +entity.getSubcontractor().getId()
					  + "' and transferStation='"					  
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
       }
		beforeSave(request, entity, model);			
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);	
		
		    if(request.getParameter("typ")!="")   
			return "redirect:/rate/expirationreport/list.do?type=subcontractorrate";
	
		return "redirect:/" + urlContext + "/list.do";
	
		/*return super.save(request, entity, bindingResult, model);*/
	}
	
	private String common(HttpServletRequest request,ModelMap model,SubcontractorRate entity,String rateQuery)
	{
		 String result="";
		 List<SubcontractorRate> subcontractorrates = genericDAO.executeSimpleQuery(rateQuery);
         
         if (subcontractorrates != null && subcontractorrates.size() > 0) { 
    	   
      	   for(SubcontractorRate subcontractorrate:subcontractorrates){
      	
      	if((entity.getValidFrom().getTime() >= subcontractorrate.getValidFrom().getTime() && entity.getValidTo().getTime()<= subcontractorrate.getValidTo().getTime())
      	||(entity.getValidFrom().getTime() <= subcontractorrate.getValidFrom().getTime() && entity.getValidTo().getTime()>= subcontractorrate.getValidTo().getTime())  
      	|| ((entity.getValidFrom().getTime() >= subcontractorrate.getValidFrom().getTime() && entity.getValidFrom().getTime()<=subcontractorrate.getValidTo().getTime()) && entity.getValidTo().getTime()>= subcontractorrate.getValidTo().getTime())
      	|| (entity.getValidFrom().getTime() <= subcontractorrate.getValidFrom().getTime() && (entity.getValidTo().getTime()>=subcontractorrate.getValidFrom().getTime()   && entity.getValidTo().getTime()<= subcontractorrate.getValidTo().getTime()))){       		
      		result="error";
  		    return result;       		
      	}      	
         }   
         }   
         return "";
         }
	//from
	
	@RequestMapping("/changestatus.do")
	public String changeStatus(HttpServletRequest request, ModelMap modMap) {
		String type=(String) request.getSession().getAttribute("typ");	
		if(type!=null){	
			if(request.getParameter("status").equalsIgnoreCase("current")){
				request.getSession().setAttribute("error", "Unable to process your request. Please check rate status");
				return "redirect:/rate/expirationreport/list.do?type=subcontractorrate";				
			}
			SubcontractorRate subcontractorRates = genericDAO.getById(SubcontractorRate.class,
		    Long.valueOf(request.getParameter("id")));
		if (subcontractorRates.getStatus() == 1) 
			subcontractorRates.setStatus(0);		 	
		genericDAO.save(subcontractorRates);
		return "redirect:/rate/expirationreport/list.do?type=subcontractorrate";
		}
		else{			
			SubcontractorRate subcontractorRates = genericDAO.getById(SubcontractorRate.class,
			Long.valueOf(request.getParameter("id")));
			if (subcontractorRates.getStatus() == 0) 
				subcontractorRates.setStatus(1);		 	
			genericDAO.save(subcontractorRates);
			return "redirect:/" + urlContext + "/list.do";
		}
		
	}
	
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
	public String delete(@ModelAttribute("modelObject") SubcontractorRate entity,
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
			return "redirect:/rate/expirationreport/list.do?type=subcontractorrate";
	
		return "redirect:/" + urlContext + "/list.do";
	}
	
	//here
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		if ("findDestinationLoad".equalsIgnoreCase(action)) {
			if (!StringUtils.isEmpty(request.getParameter("origin"))) {
				List<Location> destinatioLoad = new ArrayList<Location>();
				Map criterias = new HashMap();
				criterias.put("transferStation.id",
						Long.parseLong(request.getParameter("origin")));
				List<SubcontractorRate> billing = genericDAO.findByCriteria(
						SubcontractorRate.class, criterias, "transferStation",
						false);
				for (SubcontractorRate bill : billing) {
					destinatioLoad.add(bill.getLandfill());
				}
				Gson gson = new Gson();
				return gson.toJson(destinatioLoad);
			}
		}
		if ("findOriginLoad".equalsIgnoreCase(action)) {
			if (!StringUtils.isEmpty(request.getParameter("destination"))) {
				List<Location> originLoad = new ArrayList<Location>();
				Map criterias = new HashMap();
				criterias.put("landfill.id",
						Long.parseLong(request.getParameter("destination")));
				List<SubcontractorRate> billing = genericDAO.findByCriteria(
						SubcontractorRate.class, criterias, "landfill", false);
				for (SubcontractorRate bill : billing) {
					originLoad.add(bill.getTransferStation());
				}
				Gson gson = new Gson();
				return gson.toJson(originLoad);
			}
		}
		return "";
	}
	
	

}
