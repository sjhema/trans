package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.BillingRate;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.service.DateUpdateService;

/**
 * @author mritunjay
 */

@Controller
@RequestMapping("/admin/vehicle")
public class VehicleController extends CRUDController<Vehicle>{
	
	public VehicleController(){
		setUrlContext("/admin/vehicle");
	}
	
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(
				Location.class));
	}

	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("unitNo", genericDAO.executeSimpleQuery("select obj from Vehicle obj group by obj.unit,obj.type"));
		model.addAttribute("vinNo", genericDAO.findByCriteria(Vehicle.class, criterias, "vinNumber", false));
		model.addAttribute("plateNumber", genericDAO.findByCriteria(Vehicle.class, criterias, "plate", false));
		criterias.put("type", 3);
		model.addAttribute("companies",
				genericDAO.findByCriteria(Location.class, criterias, "name",false));
		criterias.put("type", 4);
		//model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		model.addAttribute("vehicleTypes", listStaticData("VEHICLE_TYPE"));
		model.addAttribute("models", listStaticData("MODEL_TYPE"));
		
		model.addAttribute("features", listStaticData("VEHICLE_FEATURE"));
		model.addAttribute("activeStauses", listStaticData("VEHICLE_STATUS"));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		Map criterias = new HashMap();
		criterias.put("type", 3);
		model.addAttribute("companies",
				genericDAO.findByCriteria(Location.class, criterias));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		model.addAttribute("vehicleTypes", listStaticData("VEHICLE_TYPE"));
		model.addAttribute("models", listStaticData("MODEL_TYPE"));
		setupCreate(model, request);
	}
	

	
	
	
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") Vehicle entity,
			BindingResult bindingResult, ModelMap model) {
		
		if(entity.getUnit()== null){
			bindingResult.rejectValue("unit","NotEmpty.java.lang.String",null,null);
		}
		
		if (entity.getOwner() == null) {
			bindingResult.rejectValue("owner", "error.select.option", null,
					null);
		}
		/*if (entity.getLocation() == null) {
			bindingResult.rejectValue("location", "error.select.option", null,
					null);
		}*/
		
		if(entity.getYear().equals("")){
			bindingResult.rejectValue("year","NotEmpty.java.lang.String",null,null);
		}
		
		if(entity.getMake().equals("")){
			bindingResult.rejectValue("make","NotEmpty.java.lang.String",null,null);
		}
		if(entity.getModel().equals("")){
			bindingResult.rejectValue("model","error.select.option",null,null);
		}
		
		if(entity.getVinNumber().equals("")){
			bindingResult.rejectValue("vinNumber","NotEmpty.java.lang.String",null,null);
		}
		
		if(entity.getPlate().equals("")){
			bindingResult.rejectValue("plate","NotEmpty.java.lang.String",null,null);
		}
		
		if (entity.getType() == null) {
			bindingResult.rejectValue("type", "error.select.option", null,
					null);
		}
		
		if(entity.getValidFrom()==null)
		{
			bindingResult.rejectValue("validFrom","error.NotNull.field",null,null);
		}
		
		if(entity.getValidTo()==null){
			bindingResult.rejectValue("validTo","error.NotNull.field",null,null);
		}		
		/*return super.save(request, entity, bindingResult, model);*/
		
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
		
		
		 Map prop=new HashMap();
		 prop.put("unit",entity.getUnit());
		 prop.put("owner",entity.getOwner().getId());
		 prop.put("year",entity.getYear());
		 prop.put("make",entity.getMake());
		 prop.put("model",entity.getModel());
		 prop.put("vinNumber",entity.getVinNumber());
		 prop.put("plate",entity.getPlate());
		 prop.put("type",entity.getType());
		 prop.put("validFrom",dateFormat.format(entity.getValidFrom()));
		 prop.put("validTo",dateFormat.format(entity.getValidTo()));
		
		 boolean rst=genericDAO.isUnique(Vehicle.class,entity, prop);      	  
   	  if(!rst){
   		  setupCreate(model, request);   			
   		  request.getSession().setAttribute("error","Duplicate Vehicle Entry");
   		  return urlContext + "/form";
   	  }
   	  
   	  
   	  if(entity.getId()!=null){
   		  String querystr="select obj from Vehicle obj where obj.id!="
   			  +entity.getId()
   			  +" and obj.unit="
   			  +entity.getUnit()
   			  +" and obj.type="
   			  +entity.getType()
   			  +" order by obj.validFrom desc";
   		  String result=common(request,model,entity,querystr);          
          if(result.equalsIgnoreCase("error")){
        	  setupCreate(model, request);   			
       		  request.getSession().setAttribute("error","There is Already a Vehicle Entry Exists With Same Unit# for Specified Date Ranage!");
       		  return urlContext + "/form";
          }
   		   
   		  
   	  }
   	  else
   	  {
   		 String querystr2="select obj from Vehicle obj where  obj.unit="
  			  +entity.getUnit()
  			  +" and obj.type="
   			  +entity.getType()
  			  +" order by obj.validFrom desc";
  		  String result=common(request,model,entity,querystr2);          
         if(result.equalsIgnoreCase("error")){
       	  setupCreate(model, request);   			
      		  request.getSession().setAttribute("error","There is Already a Vehicle Entry Exists With Same Unit# for Specified Date Ranage!");
      		  return urlContext + "/form";
         }
   	  }
		 
		 
		
		beforeSave(request, entity, model);
		// merge into datasource
		entity.setUnitNum(entity.getUnit().toString()); 
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		// return to list
		return "redirect:/" + urlContext + "/list.do";
		
	}
	
	
	
	private String common(HttpServletRequest request,ModelMap model,Vehicle entity,String rateQuery)
	{
		String result="";
		 List<Vehicle> vehicleObjs = genericDAO.executeSimpleQuery(rateQuery);
         
         if (vehicleObjs != null && vehicleObjs.size() > 0) { 
    	   
      	   for(Vehicle billingrate:vehicleObjs){
      	
      	if((entity.getValidFrom().getTime() >= billingrate.getValidFrom().getTime() && entity.getValidTo().getTime()<= billingrate.getValidTo().getTime())
      	||(entity.getValidFrom().getTime() <= billingrate.getValidFrom().getTime() && entity.getValidTo().getTime()>= billingrate.getValidTo().getTime())  
      	|| ((entity.getValidFrom().getTime() >= billingrate.getValidFrom().getTime() && entity.getValidFrom().getTime()<=billingrate.getValidTo().getTime()) && entity.getValidTo().getTime()>= billingrate.getValidTo().getTime())
      	|| (entity.getValidFrom().getTime() <= billingrate.getValidFrom().getTime() && (entity.getValidTo().getTime()>=billingrate.getValidFrom().getTime()   && entity.getValidTo().getTime()<= billingrate.getValidTo().getTime()))){       		
      		    result="error";      		   
      		    return result;
      	}      	
         }   
         }   
         return "";
         }
	
	
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"unit",null,null));
		return urlContext + "/list";
	}
	
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		System.out.println("********* the criti values is "+criteria.getSearchMap().get("unit.id"));
		 if(criteria.getSearchMap().get("unit.id")!=null){
		 if(!StringUtils.isEmpty(criteria.getSearchMap().get("unit.id").toString())){
		   String unitval=criteria.getSearchMap().get("unit.id").toString();
		   request.getSession().setAttribute("unitval",unitval);
		   String[] sepvals=unitval.split("-");
		   
		   String vehiclequery="select obj from Vehicle obj where obj.type=" 
		   		+sepvals[1]
		   		+" and obj.unit in ("
				+sepvals[0]
				+")";
			
			System.out.println("******** the truck query is "+vehiclequery);
			
			List<Vehicle> vehicleLists=genericDAO.executeSimpleQuery(vehiclequery);
			String truckIds="";
			if(vehicleLists!=null && vehicleLists.size()>0){				
				int count=0;
			for(Vehicle vehicleObj : vehicleLists) {
				if(count==0){
					count++;
					truckIds=String.valueOf(vehicleObj.getId());
				}
				else{
				truckIds=truckIds+","+String.valueOf(vehicleObj.getId());
				}
			}
			}
		   
			criteria.getSearchMap().put("unit.id",truckIds);		   
		 }
		 }
		
		dateupdateService.updateDate(request, "validFromDate", "validFrom");
		dateupdateService.updateDate(request, "validToDate", "validTo");
		System.out.println("\n inside vehicle cont searchCriteria-->"+criteria);
		 model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"unit",null,null));		
		 return urlContext + "/list";
	}

}