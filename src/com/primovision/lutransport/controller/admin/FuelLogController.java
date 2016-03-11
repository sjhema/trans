package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;
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

import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.DriverFuelCard;
import com.primovision.lutransport.model.FuelCard;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.FuelVendor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.SubContractor;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.hr.EmployeeCatagory;

/**
 * @author ravi
 */

@Controller
@RequestMapping("/operator/fuellog")
public class FuelLogController extends CRUDController<FuelLog> {

	
	public static SimpleDateFormat drvdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	
	public FuelLogController() {
		setUrlContext("operator/fuellog");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.primovision.lutransport.controller.BaseController#initBinder(org.springframework.web.bind.WebDataBinder)
	 */
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// SimpleDateFormat("yyyy-MM-dd")
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	    dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Long.TYPE, new CustomNumberEditor(Long.class, false));
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(Driver.class));
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));
		binder.registerCustomEditor(State.class, new AbstractModelEditor(State.class));
		binder.registerCustomEditor(FuelVendor.class, new AbstractModelEditor(FuelVendor.class));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(FuelCard.class, new AbstractModelEditor(FuelCard.class));
		// Fuel log - subcontractor
		binder.registerCustomEditor(SubContractor.class, new AbstractModelEditor(SubContractor.class));
	}

	 private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.primovision.lutransport.controller.CRUDController#setupCreate(org
	 * .springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		//model.addAttribute("fuelvendors", genericDAO.findByCriteria(FuelVendor.class, criterias, "name", false));
		criterias.clear();
		//EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class,2l);
		//criterias.put("catagory", empobj);
		if(request.getParameter("id")!=null){
			model.addAttribute("drivernames", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
			model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "firstName", false));
		}
		else{
			criterias.clear();
			criterias.put("status", 1);
			model.addAttribute("drivernames", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
			model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "firstName", false));
		}
		criterias.clear();
		criterias.put("type", 1);
		model.addAttribute("vehicles", genericDAO.findByCriteria(Vehicle.class, criterias, "unit", false));
		criterias.clear();
		model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		
		criterias.put("type", 3);
		model.addAttribute("companyLocation",genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		model.addAttribute("fuelTypes", listStaticData("FUEL_TYPE"));
		model.addAttribute("subcontractors", genericDAO.executeSimpleQuery("Select obj from SubContractor obj order by obj.name "));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.primovision.lutransport.controller.CRUDController#setupList(org.
	 * springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		if (request.getParameter("populate") == null)
			populateSearchCriteria(request, request.getParameterMap());
		Map criterias = new HashMap();
		criterias.clear();
		model.addAttribute("fuelvendors", genericDAO.findByCriteria(FuelVendor.class, criterias, "name", false));
		//EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class,2l);
		//criterias.put("catagory", empobj);
		criterias.clear();
		model.addAttribute("drivernames", genericDAO.executeSimpleQuery("Select obj from Driver obj group by obj.fullName order by obj.fullName "));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.primovision.lutransport.controller.CRUDController#save(javax.servlet
	 * .http.HttpServletRequest, com.primovision.lutransport.model.BaseModel,
	 * org.springframework.validation.BindingResult,
	 * org.springframework.ui.ModelMap)
	 */
	
	@Override	
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		FuelLog fuellog=genericDAO.getById(FuelLog.class,Long.parseLong(request.getParameter("id")));	
		if(fuellog.getDriversid()!=null){
		Map criteria = new HashMap();		
		criteria.put("driver.id",fuellog.getDriversid().getId());			
		model.addAttribute("driverfuelcard",genericDAO.findByCriteria(DriverFuelCard.class, criteria, "fuelvendor", false));
		}
		model.addAttribute("fuelcard",fuellog.getFuelcard());
		model.addAttribute("fuelvendor",fuellog.getFuelvendor());
		return urlContext + "/form";
	}
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") FuelLog entity,
			BindingResult bindingResult, ModelMap model) {
		/*if(entity.getFuelcard()==null){
			bindingResult.rejectValue("fuelcard", "error.select.option", null, null);			
		}*/		
		
		if(entity.getFuelvendor()==null){
			bindingResult.rejectValue("fuelvendor", "error.select.option", null, null);
		}
		
		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option", null, null);
		}
		/*if(entity.getDriverFname()==null){
			bindingResult.rejectValue("driverFname", "error.select.option", null, null);
		}
		if(entity.getDriverLname()==null){
			bindingResult.rejectValue("driverLname", "error.select.option", null, null);
		}*/
		/*if(entity.getState()==null){
			bindingResult.rejectValue("state", "error.select.option", null, null);
		}*/
		if(entity.getUnit()==null){
			
			bindingResult.rejectValue("unit", "error.select.option", null, null);
		}
        if(entity.getDriversid()==null){
			
			bindingResult.rejectValue("driversid", "error.select.option", null, null);
		}
        if(entity.getFueltype() == null){
			bindingResult.rejectValue("fueltype", "error.select.option", null, null);
		}
		/*if (entity.getDrivers().getId() == -1) {
			bindingResult.rejectValue("drivers", "error.select.option", null,
					null);
		}
		if (entity.getVehicles().getId() == -1) {
			bindingResult.rejectValue("vehicles", "error.select.option", null,
					null);
		}
		if (entity.getState().getId() == -1) {
			bindingResult.rejectValue("state", "error.select.option", null,
					null);
		}*/
		
		// validate duplicate Fuel-Card-Number
//		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put("fuelCardNumber", entity.getFuelCardNumber());
//		if (entity.getFuelCardNumber() != null && entity.getId() == null) {
//			if (genericDAO.getByCriteria(FuelLog.class, maps).getId().SIZE > 1) {
//				bindingResult.rejectValue("fuelCardNumber",
//						"error.duplicate.entry", null, null);
//			}
//		}
		
		// Total Transaction
		/*if (entity.getGallons() != null && entity.getPrice() != null){
			entity.setTotalTransaction(entity.getGallons() * entity.getPrice());
		}
		// Daily Miles
		if (entity.getFinishOdometerReading() != null
				&& entity.getStartOdometerReading() != null) {
			if (entity.getFinishOdometerReading() < entity
					.getStartOdometerReading()) {
				bindingResult.rejectValue("startOdometerReading",
						"typeMismatch.java.lang.Integer", null, null);
			} else {
				entity.setDailyMiles(entity.getFinishOdometerReading()
						- entity.getStartOdometerReading());
			}
		}*/
        try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		// return to form if we had errors
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);			
			common(model,request,entity);				
			return urlContext + "/form";
		}
		
		 Map prop=new HashMap();
    	 prop.put("fuelvendor",entity.getFuelvendor().getId() );
    	 prop.put("driversid",entity.getDriversid().getId() );
    	 prop.put("company",entity.getCompany().getId() );
    	 prop.put("terminal",entity.getTerminal().getId() );
    	 if(entity.getState()!=null){
    	 prop.put("state",entity.getState().getId());
    	 }
    	 prop.put("unit",entity.getUnit().getId() );
    	 prop.put("invoiceDate",dateFormat.format(entity.getInvoiceDate() ));
    	 prop.put("invoiceNo",entity.getInvoiceNo() );
    	 prop.put("transactiondate",dateFormat.format(entity.getTransactiondate()) );    	 
    	 prop.put("transactiontime",entity.getTransactiontime());
    	 if(entity.getFuelcard()!=null){
    	 prop.put("fuelcard",entity.getFuelcard().getId() );
    	 }
    	 prop.put("fueltype",entity.getFueltype() );
    	 prop.put("city",entity.getCity() );
    	 prop.put("gallons",entity.getGallons() );
    	 prop.put("unitprice",entity.getUnitprice() );
    	 prop.put("fees",entity.getFees() );
    	 prop.put("discounts",entity.getDiscounts() );
    	 prop.put("amount",entity.getAmount() );
    	  boolean rst=genericDAO.isUnique(FuelLog.class,entity, prop);      	  
    	  if(!rst){
    		  setupCreate(model, request);
    			common(model,request,entity);
    		  request.getSession().setAttribute("error","Duplicate Fuel Log Entry");
    		  return urlContext + "/form";
    	  }
		
    	  String ticktQuery = "select obj from Ticket obj where obj.driver="+entity.getDriversid().getId()+" and obj.loadDate <='"+drvdf.format(entity.getTransactiondate())+"' and obj.unloadDate>='"+drvdf.format(entity.getTransactiondate())+"'";
		  System.out.println("****** Ticket query is "+ticktQuery);
		  List<Ticket> tickObj= genericDAO.executeSimpleQuery(ticktQuery);
			
		  if(tickObj.size()>0 && tickObj!=null){
			entity.setFuelViolation("Not Violated");
		  }
		  else{
			entity.setFuelViolation("Violated");					
		  }    	  
		  
		  Map criti = new HashMap();
		  criti.clear();
		  criti.put("id", entity.getDriversid().getId());
		  Driver drvOBj=genericDAO.getByCriteria(Driver.class, criti);
		  if(drvOBj!=null)
			  entity.setDriverFullName(drvOBj.getFullName());
		  
		  criti.clear();
		  criti.put("id", entity.getUnit().getId());
		  Vehicle vehObj = genericDAO.getByCriteria(Vehicle.class, criti);
		  if(vehObj!=null)
			  entity.setUnitNum(vehObj.getUnitNum());
		  
		beforeSave(request, entity, model);
		
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		return "redirect:/" + urlContext + "/list.do";   	
	}
        
		
	
	
	public void common(ModelMap model,HttpServletRequest request,FuelLog entity){
		if(entity.getDriversid()!=null){
			Map criterias = new HashMap();			
			criterias.put("driver.id",Long.parseLong(request.getParameter("driversid")));
			model.addAttribute("driverfuelcard",genericDAO.findByCriteria(DriverFuelCard.class, criterias, "fuelvendor", false));				
		}				
		if(entity.getFuelcard()!=null && entity.getFuelvendor() !=null){			
		FuelCard fuelcard=genericDAO.getById(FuelCard.class,Long.parseLong(request.getParameter("fuelcard")));
		model.addAttribute("fuelcard",fuelcard);
		model.addAttribute("fuelvendor",fuelcard.getFuelvendor());	
		}			
		else if(entity.getFuelcard()!=null){				
			FuelCard fuelcard=genericDAO.getById(FuelCard.class,Long.parseLong(request.getParameter("fuelcard")));
			model.addAttribute("fuelcard",fuelcard);
		}		
		
	}
	
	
	
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		if("findFuelCard".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("driver"))){					
				Map criterias = new HashMap();			
				criterias.put("driver.id",Long.parseLong(request.getParameter("driver")));
				List<DriverFuelCard> fuelcards=genericDAO.findByCriteria(DriverFuelCard.class, criterias, "fuelvendor", false);		
				Gson gson = new Gson();
				return gson.toJson(fuelcards);
			}
		}
		
		if("findMissingFuelCard".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("driver"))&& !StringUtils.isEmpty(request.getParameter("fuelvendor"))){
				Map criterias = new HashMap();			
				criterias.put("driver.id",Long.parseLong(request.getParameter("driver")));
				criterias.put("fuelvendor.id",Long.parseLong(request.getParameter("fuelvendor")));
				List<DriverFuelCard> fuelcards=genericDAO.findByCriteria(DriverFuelCard.class, criterias, "fuelvendor", false);		
				Gson gson = new Gson();
				return gson.toJson(fuelcards);
			}
			
		}
		

		if("findFuelVendor".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("fuelcard"))){					
				Map criterias = new HashMap();
				criterias.put("id",Long.parseLong(request.getParameter("fuelcard")));
				List<FuelCard> fuelcards=genericDAO.findByCriteria(FuelCard.class, criterias);				
				Gson gson = new Gson();
				return gson.toJson(fuelcards);				
			}			
		}
		return "";
	}
	
}
