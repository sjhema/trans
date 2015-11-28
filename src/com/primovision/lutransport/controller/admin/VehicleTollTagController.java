package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.TollCompany;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.VehicleTollTag;
import com.primovision.lutransport.service.DateUpdateService;

/**
 * @author Ravi
 * 
 */

@Controller
@RequestMapping("/admin/vehicletolltag")
public class VehicleTollTagController extends CRUDController<VehicleTollTag>{

	public VehicleTollTagController(){
		setUrlContext("admin/vehicletolltag");
	}	
	
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	
	
	@Override
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(TollCompany.class, new AbstractModelEditor(TollCompany.class));
	}
	
	
	
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		String query="SELECT p FROM VehicleTollTag p GROUP BY p.unit ORDER BY ABS(p.unit) ";
		model.addAttribute("vehicleList", genericDAO.executeSimpleQuery(query));
		model.addAttribute("vehicle", genericDAO.findByCriteria(Vehicle.class, criterias,"unit",false));
		model.addAttribute("tollCompany",genericDAO.findByCriteria(TollCompany.class, criterias,"name",false));
		/*Map<String,Object> map=new HashMap<String,Object>();
		map.put("dataType", "STATUS");
		map.put("dataValue", "0,1");
		model.addAttribute("tollTagStatus", genericDAO.findByCriteria(StaticData.class, map));
		map.clear();
		map.put("dataType", "TOLL_TAG");
		 model.addAttribute("tollTag", genericDAO.findByCriteria(StaticData.class, map, "dataText", false));*/
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}	
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		dateupdateService.updateDate(request, "validFromDate", "validFrom");
		dateupdateService.updateDate(request, "validToDate", "validTo");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"unit asc,tollCompany asc,validFrom desc,validTo desc,tollTagNumber",false ));
		return urlContext + "/list";
	}
	
	
	@Override	
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"unit asc,tollCompany asc,validFrom desc,validTo desc,tollTagNumber",false ));
		return urlContext + "/list";
	}
	
	
	/*public List<VehicleTollTag> common(List<VehicleTollTag> vehicletolltags){
		 List<VehicleTollTag> list=new ArrayList<VehicleTollTag>();
		 for(VehicleTollTag vehicletolltag:vehicletolltags){
		 if(vehicletolltag.getUnit()==null || vehicletolltag.getUnit()!=vehicletolltag.getVehicle().getUnit().toString()){				
		  vehicletolltag.setUnit(vehicletolltag.getVehicle().getUnit().toString());
		  genericDAO.save(vehicletolltag);
		}
		  list.add(vehicletolltag);	
		}
		 return list;
		}*/
	
	
	
	/*@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") VehicleTollTag entity,
			BindingResult bindingResult, ModelMap model) {
		
		if(entity.getVehicle() == null)
			bindingResult.rejectValue("vehicle", "error.select.option", null,	null);	
		if(entity.getTollCompany()==null)
			bindingResult.rejectValue("tollCompany", "error.select.option", null,	null);
		
		if(entity.getTollTag() == null)
			bindingResult.rejectValue("tollTag", "error.select.option", null,	null);
		if(entity.getTollTagStatus() == null)
			bindingResult.rejectValue("tollTagStatus", "error.select.option", null,	null);
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
		
	   	Vehicle vehicle=genericDAO.getById(Vehicle.class,entity.getVehicle().getId());
		entity.setUnit(vehicle.getUnit().toString());
		
		Date validFromTemp=entity.getValidFromTemp();
		Date validToTemp=entity.getValidToTemp();
		if(entity.getValidFromTemp()!=null && entity.getValidToTemp() !=null ){									
			String query="select obj from VehicleTollTag obj where obj.tollTagNumber='"
			+entity.getTollTagNumber()+"' and obj.tollCompany='"
			+entity.getTollCompany().getId()+"' and obj.validFrom !='"
			+ validFromTemp
			+ "' and obj.validTo != '"
			+validToTemp+"'";	
			String result=common(request,model,entity,query);
			 if(result.equalsIgnoreCase("error")){	            	 
	            		setupUpdate(model, request);		         		
	         		request.getSession().setAttribute("error", "Entered Toll tag already exists for specified date range");
	   		return urlContext + "/form"; 
	               }  		      	
		}
		else{
			String query="select obj from VehicleTollTag obj where obj.tollTagNumber='"
			+entity.getTollTagNumber()+"' and obj.tollCompany='"
			+entity.getTollCompany().getId()+"' order by obj.validFrom desc";
		 	String result=common(request,model,entity,query);
		  	if(result.equalsIgnoreCase("error")){            	
              				setupUpdate(model, request);        		
             				 request.getSession().setAttribute("error", "Entered Toll tag already exists for specified date range");
  			 	 return urlContext + "/form"; 
              			}		
		}

		beforeSave(request, entity, model);
		// merge into datasource
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		// return to list
		return "redirect:/" + urlContext + "/list.do";		

		//return super.save(request, entity, bindingResult, model);
	}*/

	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") VehicleTollTag entity,
			BindingResult bindingResult, ModelMap model) {

		if(entity.getVehicle() == null)
			bindingResult.rejectValue("vehicle", "error.select.option", null,	null);	
		if(entity.getTollCompany()==null)
			bindingResult.rejectValue("tollCompany", "error.select.option", null,	null);

		/*if(entity.getTollTag() == null)
			bindingResult.rejectValue("tollTag", "error.select.option", null,	null);
		if(entity.getTollTagStatus() == null)
			bindingResult.rejectValue("tollTagStatus", "error.select.option", null,	null);*/
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

		Vehicle vehicle=genericDAO.getById(Vehicle.class,entity.getVehicle().getId());
		entity.setUnit(vehicle.getUnit().toString());

		Date validFromTemp=entity.getValidFromTemp();
		Date validToTemp=entity.getValidToTemp();
		if(entity.getValidFromTemp()!=null && entity.getValidToTemp() !=null ){									
			String query="select obj from VehicleTollTag obj where obj.tollTagNumber='"
				+entity.getTollTagNumber()+"' and obj.tollCompany='"
				+entity.getTollCompany().getId()+"' and obj.validFrom !='"
				+ validFromTemp
				+ "' and obj.validTo != '"
				+validToTemp+"'";	
			String result=common(request,model,entity,query);
			if(result.equalsIgnoreCase("error")){	            	 
				setupUpdate(model, request);		         		
				request.getSession().setAttribute("error", "Entered Toll tag already exists for specified date range");
				return urlContext + "/form"; 
			}  	
			
			String queryCheckMultiTags="select obj from VehicleTollTag obj where obj.unit='"
				+entity.getUnit()+"' and obj.tollCompany='"
				+entity.getTollCompany().getId()+"' and obj.validFrom !='"
				+ validFromTemp
				+ "' and obj.validTo != '"
				+validToTemp+"'";	
			
			String result2=common(request,model,entity,queryCheckMultiTags);
			if(result2.equalsIgnoreCase("error")){	            	 
				setupUpdate(model, request);		         		
				request.getSession().setAttribute("error", "Active Toll Tag already exist for specified date range");
				return urlContext + "/form"; 
			}  
			
			
		}
		else{
			String query="select obj from VehicleTollTag obj where obj.tollTagNumber='"
				+entity.getTollTagNumber()+"' and obj.tollCompany='"
				+entity.getTollCompany().getId()+"' order by obj.validFrom desc";
			String result=common(request,model,entity,query);
			if(result.equalsIgnoreCase("error")){            	
				setupUpdate(model, request);        		
				request.getSession().setAttribute("error", "Entered Toll tag already exists for specified date range");
				return urlContext + "/form"; 
			}
			
			String queryCheckMultiTags="select obj from VehicleTollTag obj where obj.unit='"
				+entity.getUnit()+"' and obj.tollCompany='"
				+entity.getTollCompany().getId()+"' and obj.validFrom !='"
				+ validFromTemp
				+ "' and obj.validTo != '"
				+validToTemp+"'";	
			
			String result2=common(request,model,entity,queryCheckMultiTags);
			if(result2.equalsIgnoreCase("error")){	            	 
				setupUpdate(model, request);		         		
				request.getSession().setAttribute("error", "Active Toll Tag already exist for specified date range");
				return urlContext + "/form"; 
			} 
		}

		beforeSave(request, entity, model);
		// merge into datasource
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		// return to list
		return "redirect:/" + urlContext + "/list.do";		

		//return super.save(request, entity, bindingResult, model);
	}
	
	
		private String common(HttpServletRequest request,ModelMap model,VehicleTollTag entity,String query)
		{
			String result="";
			List<VehicleTollTag> vehicletolltags=genericDAO.executeSimpleQuery(query);
			if(!vehicletolltags.isEmpty() && vehicletolltags.size()>0){		
			for(VehicleTollTag vehicletolltag:vehicletolltags){
				if((entity.getValidFrom().getTime() >= vehicletolltag.getValidFrom().getTime() && entity.getValidTo().getTime()<= vehicletolltag.getValidTo().getTime())			
			      	||(entity.getValidFrom().getTime() <= vehicletolltag.getValidFrom().getTime() && entity.getValidTo().getTime()>= vehicletolltag.getValidTo().getTime())  
			      	|| ((entity.getValidFrom().getTime() >= vehicletolltag.getValidFrom().getTime() && entity.getValidFrom().getTime()<=vehicletolltag.getValidTo().getTime()) && entity.getValidTo().getTime()>= vehicletolltag.getValidTo().getTime())
			      	|| (entity.getValidFrom().getTime() <= vehicletolltag.getValidFrom().getTime() && (entity.getValidTo().getTime()>=vehicletolltag.getValidFrom().getTime()   && entity.getValidTo().getTime()<= vehicletolltag.getValidTo().getTime()))){
					result="error";      		   
	      		  		return result;
				}
				}
			}
			return "";
		}
}

