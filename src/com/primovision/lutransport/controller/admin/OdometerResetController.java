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

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Vehicle;

import com.primovision.lutransport.model.driver.OdometerReset;

@Controller
@RequestMapping("/admin/odometerreset")
public class OdometerResetController extends CRUDController<OdometerReset> {
	public OdometerResetController() {
		setUrlContext("admin/odometerreset");
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));		
	}
	 
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "truck", null, null));
		return urlContext + "/list";
	}
	 
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
		
		Map criterias = new HashMap();
		criterias.clear();
		model.addAttribute("trucks", genericDAO.executeSimpleQuery("select obj from Vehicle obj where obj.type=1 group by obj.unit"));
	}
	 
	@Override
	public String save(HttpServletRequest request,
				@ModelAttribute("modelObject") OdometerReset entity,
				BindingResult bindingResult, ModelMap model) {
		   Map criterias = new HashMap();
			
		   Vehicle truck = null;
			if (StringUtils.isEmpty(entity.getTempTruck())) {
				bindingResult.rejectValue("tempTruck", "NotNull.java.lang.String", null, null);
			} else {
				criterias.clear();
				criterias.put("unit", Integer.parseInt(entity.getTempTruck()));
				truck = genericDAO.getByCriteria(Vehicle.class, criterias);
				if (truck == null){
					bindingResult.rejectValue("tempTruck", "error.invalid.truck", null, null);
				}			
			}
			
			if (entity.getResetDate() == null){
				bindingResult.rejectValue("resetDate", "NotNull.java.util.Date", null, null);
			}
			
			if (entity.getResetReading() == null){
				bindingResult.rejectValue("resetReading", "NotNull.java.lang.Integer", null, null);
			}
			
			entity.setEnteredById(getUser(request).getId()); 
			entity.setEnteredBy(getUser(request).getFullName()); 
			
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
			
			/**
			 * Added: Bug Fix - Save the truck ID corresponding to the latest date range 
			 */
			System.out.println("Truck ID (Incoming) = " + truck.getId());
			setTruckForOdometer(entity);
			System.out.println("Truck ID (After date check) = " + entity.getTruck().getId());
			//	entity.setTruck(truck);
			 	
			beforeSave(request, entity, model);			
			genericDAO.saveOrUpdate(entity);
			cleanUp(request);
			
			/*if(!StringUtils.isEmpty(request.getParameter("id")) && request.getParameter("id")!=null){
				request.getSession().setAttribute("msg", "Odometer reset reading updated successfully");
				return "redirect:list.do";
			}
			else{			
				request.getSession().setAttribute("msg","Odometer reset reading added successfully");
				if(entity.getInComplete().equals("Yes")){
					return "redirect:list.do";
				}
				else{
					return "redirect:create.do";
				}
			}*/
			
			return "redirect:list.do";
	}
	 
	@Override
	protected String processAjaxRequest(HttpServletRequest request,
				String action, Model model) {
		if ("verifytruck".equalsIgnoreCase(action)) {	
			try {
					Map criterias = new HashMap();
					criterias.put("unit", Integer.parseInt(request.getParameter("truck")));
					Vehicle truck = genericDAO.getByCriteria(Vehicle.class, criterias);
					if (truck == null) {
						return "Invalid truck number";
					} else {
						return "";
					}
				} catch (Exception e) {				
					return "";
				}		
			}
			
			return "";
	}
	 
	private void setTruckForOdometer(OdometerReset entity) {
		String vehicleQuery = "Select obj from Vehicle obj where obj.type=1 and obj.unit="
					+ Integer.parseInt(entity.getTempTruck()) + " and obj.validFrom<='"
					+ dateFormat.format(entity.getResetDate()) + "' and obj.validTo>='"
					+ dateFormat.format(entity.getResetDate()) + "'";
		System.out.println("******* The vehicle query for driver tripsheet is " + vehicleQuery);
		
		List<Vehicle> vehicleList = genericDAO.executeSimpleQuery(vehicleQuery);
		if (vehicleList != null && vehicleList.size() > 0) {
			entity.setTruck(vehicleList.get(0));
		}
	}
}
