package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Vehicle;
import com.primovision.lutransport.model.VehicleLocation;
import com.primovision.lutransport.service.DateUpdateService;


@Controller
@RequestMapping("/admin/vehiclelocation")
public class VehicleLocationController extends CRUDController<VehicleLocation>{
	
	public VehicleLocationController(){
		setUrlContext("/admin/vehiclelocation");
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
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(
				Location.class));
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("vinNo", genericDAO.findByCriteria(Vehicle.class, criterias, "vinNumber", false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		Map criterias = new HashMap();
		model.addAttribute("vinNo", genericDAO.findByCriteria(Vehicle.class, criterias, "vinNumber", false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		setupCreate(model, request);
	}
	
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		dateupdateService.updateDate(request, "validFromDate", "validFrom");
		dateupdateService.updateDate(request, "validToDate", "validTo");		
		 model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"vehicleNum",null,null));
		return urlContext + "/list";
	}
	
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"vehicleNum",null,null));
		return urlContext + "/list";
	}
	
	
    @Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") VehicleLocation entity,
			BindingResult bindingResult, ModelMap model) {
		
    	if (entity.getVehicleNum() == null) {
			bindingResult.rejectValue("vehicleNum", "error.select.option", null,
					null);
		}
    	
    	if (entity.getLocation() == null) {
			bindingResult.rejectValue("location", "error.select.option", null,
					null);
		}
		
		return super.save(request, entity, bindingResult, model);
	}
	
	
}
