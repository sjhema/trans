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

import com.google.gson.Gson;
import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.Driver;
import com.primovision.lutransport.model.EzToll;
import com.primovision.lutransport.model.FuelLog;
import com.primovision.lutransport.model.FuelVendor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.TollCompany;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.VehicleTollTag;
import com.primovision.lutransport.model.hr.EmployeeCatagory;

/**
 * @author ravi
 */

@Controller
@RequestMapping("/operator/eztoll")
public class EzTollController extends CRUDController<EzToll> {

	public EzTollController() {
		setUrlContext("operator/eztoll");
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
		binder.registerCustomEditor(VehicleTollTag.class, new AbstractModelEditor(VehicleTollTag.class));
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(TollCompany.class, new AbstractModelEditor(TollCompany.class));
		binder.registerCustomEditor(Driver.class, new AbstractModelEditor(Driver.class));
	}

	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("tollTagNumbers", genericDAO.findByCriteria(VehicleTollTag.class, criterias, "tollTagNumber", false));
		criterias.clear();
		model.addAttribute("vehicleplates", genericDAO.findByCriteria(Vehicle.class, criterias, "plate", true));
		criterias.put("type", 3);
		model.addAttribute("companyLocation",genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.clear();
		model.addAttribute("tollcompanies",genericDAO.findByCriteria(TollCompany.class, criterias, "name",false));
		if(request.getParameter("id")!=null){
		criterias.clear();
		//criterias.put("status", 1);
		
		
		EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class,2l);
		criterias.put("catagory", empobj);
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
		criterias.clear();
		//criterias.clear();
		//criterias.put("type", 1);
		model.addAttribute("trucks", genericDAO.findByCriteria(Vehicle.class, criterias, "unit", false));
		}
		else{
			criterias.clear();
			criterias.put("status", 1);
			model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
			criterias.clear();
			criterias.put("type", 1);
			model.addAttribute("trucks", genericDAO.findByCriteria(Vehicle.class, criterias, "unit", false));
		}
	}

	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		setupCreate(model, request);
		if (request.getParameter("populate") == null)
			populateSearchCriteria(request, request.getParameterMap());
		/*model.addAttribute("drivers", genericDAO.findAll(Driver.class));*/
		model.addAttribute("vehicleplates", genericDAO.findAll(Vehicle.class));
		model.addAttribute("tollcompanies", genericDAO.findAll(TollCompany.class));
		Map criterias = new HashMap();
		criterias.clear();
		EmployeeCatagory empobj = genericDAO.getById(EmployeeCatagory.class,2l);
		criterias.put("catagory", empobj);
		model.addAttribute("drivers", genericDAO.findByCriteria(Driver.class, criterias, "fullName", false));
	}

	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		model.addAttribute("list",common(genericDAO.search(getEntityClass(), criteria)));
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);
		model.addAttribute("list",common(genericDAO.search(getEntityClass(), criteria)));
		return urlContext + "/list";
	}
	

	@Override
	public String edit2(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		EzToll eztoll=genericDAO.getById(EzToll.class,Long.parseLong(request.getParameter("id")));
		model.addAttribute("eztoll",eztoll);
		if(eztoll.getTollTagNumber()!=null)
		model.addAttribute("tolltag", genericDAO.getById(VehicleTollTag.class,eztoll.getTollTagNumber().getId()));
		if(eztoll.getPlateNumber()!=null)
		model.addAttribute("plateNum",genericDAO.getById(Vehicle.class,eztoll.getPlateNumber().getId()).getPlate());
		return urlContext + "/form";
	}

	public List<EzToll> common(List<EzToll> list){
	List<EzToll> eztolls=new ArrayList<EzToll>();
	for(EzToll eztoll:list){
		if(eztoll.getPlateNumber()!=null)
			eztoll.setUnits(eztoll.getPlateNumber().getUnit().toString());
		else
			eztoll.setUnits(eztoll.getTollTagNumber().getVehicle().getUnit().toString());
		
		eztolls.add(eztoll);
		}
		return eztolls;
	}	
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") EzToll entity,
			BindingResult bindingResult, ModelMap model) {
		
		if(entity.getToolcompany()==null){
			bindingResult.rejectValue("toolcompany", "error.select.option", null, null);
		}
		
		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option", null, null);
		}
		if(entity.getTollTagNumber()==null && entity.getPlateNumber()==null)
		{
			bindingResult.rejectValue("tollTagNumber", "error.select.option", null, null);
			bindingResult.rejectValue("plateNumber", "error.select.option", null, null);
		}
	
		if (entity.getDriver() == null) {
			bindingResult.rejectValue("driver", "error.select.option",
					null, null);
		}
		if (entity.getUnit() == null) {
			bindingResult.rejectValue("unit", "error.select.option",
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
			if(entity.getTollTagNumber()==null && entity.getPlateNumber()==null){
				request.getSession().setAttribute("error", "Either tolltag number or plate number is required ");
			}				
			else if(entity.getTollTagNumber()!=null){
				
				if (request.getParameter("id").equals("")) {					
					model.addAttribute("tolltags", genericDAO.getById(VehicleTollTag.class,entity.getTollTagNumber().getId()));
					}
				else{
						EzToll eztoll=genericDAO.getById(EzToll.class,Long.parseLong(request.getParameter("id")));
						model.addAttribute("eztoll",eztoll);
						if(eztoll.getTollTagNumber()!=null)
						model.addAttribute("tolltag", genericDAO.getById(VehicleTollTag.class,eztoll.getTollTagNumber().getId()));
						if(eztoll.getPlateNumber()!=null)
						model.addAttribute("plateNum",genericDAO.getById(Vehicle.class,eztoll.getPlateNumber().getId()).getPlate());
					}
			}
			return urlContext + "/form";
		}
		
		List<EzToll> eztolls = null;		
		if (!request.getParameter("id").equals("")){
		Map criteria=new HashMap();
		criteria.put("id",Long.parseLong(request.getParameter("id")));
		if(entity.getTollTagNumber()!=null)
		criteria.put("tollTagNumber",entity.getTollTagNumber());
		if(entity.getPlateNumber()!=null)
		criteria.put("plateNumber",entity.getPlateNumber());
		eztolls=genericDAO.findByCriteria(EzToll.class,criteria);
		}
		
		if(eztolls==null || eztolls.size()<=0){		
		if(entity.getTollTagNumber()!=null && entity.getPlateNumber()!=null){			
			VehicleTollTag vehicletolltag=genericDAO.getById(VehicleTollTag.class,entity.getTollTagNumber().getId());			
		if(!entity.getPlateNumber().getId().equals(vehicletolltag.getVehicle().getId())){			
			setupCreate(model, request);
			if (request.getParameter("id").equals("")) {
			model.addAttribute("tolltags", genericDAO.getById(VehicleTollTag.class,entity.getTollTagNumber().getId()));
			}
			else{
				EzToll eztoll=genericDAO.getById(EzToll.class,Long.parseLong(request.getParameter("id")));
				model.addAttribute("eztoll",eztoll);
				if(eztoll.getTollTagNumber()!=null)
				model.addAttribute("tolltag", genericDAO.getById(VehicleTollTag.class,eztoll.getTollTagNumber().getId()));		
				if(eztoll.getPlateNumber()!=null)
				model.addAttribute("plateNum",entity.getPlates());
			}
				
			request.getSession().setAttribute("error", "Entered Toll tag number and Plate number does not match");
			return urlContext + "/form";
		}
		}
		}
		
		
		if(entity.getPlateNumber()==null){
			VehicleTollTag  vehicletoll=genericDAO.getById(VehicleTollTag.class,entity.getTollTagNumber().getId());
	      entity.setPlateNumber(vehicletoll.getVehicle());			
		}		

		entity.setCompany(entity.getCompany());
		entity.setTerminal(entity.getTerminal()); 
		
		  Map criti = new HashMap();
		  criti.clear();
		  criti.put("id", entity.getDriver().getId());
		  Driver drvOBj=genericDAO.getByCriteria(Driver.class, criti);
		  if(drvOBj!=null)
			  entity.setDriverFullName(drvOBj.getFullName());
		  
		  criti.clear();
		  criti.put("id", entity.getUnit().getId());
		  Vehicle vehObj = genericDAO.getByCriteria(Vehicle.class, criti);
		  if(vehObj!=null)
			  entity.setUnitNum(vehObj.getUnitNum());
		
		
		beforeSave(request, entity, model);
		// merge into datasource
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		// return to list
		return "redirect:/" + urlContext + "/list.do";
		
		//return super.save(request, entity, bindingResult, model);
	}
	
	protected String processAjaxRequest(HttpServletRequest request,
			String action, Model model) {
		

		if("findplatenum".equalsIgnoreCase(action)){
			if(!StringUtils.isEmpty(request.getParameter("tolltag"))){					
				Map criterias = new HashMap();
				criterias.put("id",Long.parseLong(request.getParameter("tolltag")));
				List<VehicleTollTag> tolltags=genericDAO.findByCriteria(VehicleTollTag.class, criterias);				
				Gson gson = new Gson();
				return gson.toJson(tolltags);				
			}			
		}
		
		else if("findall".equalsIgnoreCase(action)){
			Map criterias = new HashMap();
			List<Vehicle> tolltags=genericDAO.findByCriteria(Vehicle.class, criterias,"plate",false);
			Gson gson = new Gson();
			return gson.toJson(tolltags);
			
		}
		
		else if("findterminal".equalsIgnoreCase(action)){
			Driver driver = genericDAO.getById(Driver.class, Long.parseLong(request.getParameter("driverId")));
			Location terminal = genericDAO.getById(Location.class, driver.getTerminal().getId());
			Gson gson = new Gson();
			return gson.toJson(terminal);
		}
		
		else if("findcompany".equalsIgnoreCase(action)){
				Driver driver = genericDAO.getById(Driver.class, Long.parseLong(request.getParameter("driverId")));
				Location company = genericDAO.getById(Location.class, driver.getCompany().getId());
				Gson gson = new Gson();
				return gson.toJson(company);
			}
		
		else if("findallterminal".equalsIgnoreCase(action)){
			Map criterias = new HashMap();
			criterias.put("type", 4);
			List<Location> tolltags=genericDAO.findByCriteria(Location.class, criterias,"name",false);
			Gson gson = new Gson();
			return gson.toJson(tolltags);
			
		}
		else if("findallcompany".equalsIgnoreCase(action)){
			Map criterias = new HashMap();
			criterias.put("type", 3);
			List<Location> tolltags=genericDAO.findByCriteria(Location.class, criterias,"name",false);
			Gson gson = new Gson();
			return gson.toJson(tolltags);
			
		}
		return "";
	}
}
