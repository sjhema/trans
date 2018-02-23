package com.primovision.lutransport.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.model.NoGPSVehicle;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Vehicle;

@Controller
@RequestMapping("/admin/nogps")
public class VehicleGPSController extends CRUDController<NoGPSVehicle> {
	public VehicleGPSController(){
		setUrlContext("admin/nogps");
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Vehicle.class, new AbstractModelEditor(Vehicle.class));
		
		super.initBinder(binder);
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		// select obj from Vehicle obj where obj.type=1 group by obj.unit
		model.addAttribute("vehicles", genericDAO.executeSimpleQuery("select obj from Vehicle obj group by obj.unit, obj.type"));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") NoGPSVehicle entity,
			BindingResult bindingResult, ModelMap model) {
		String dupMsg = checkDuplicate(model, request, entity);
		if (StringUtils.isNotEmpty(dupMsg)) {
			return dupMsg;
		}
		
		if (entity.getVehicle() == null) {
			bindingResult.rejectValue("vehicle", "error.select.option", null, null);
		}
			
		return super.save(request, entity, bindingResult, model);
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		return search2(model, request);
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria, "vehicle.unit asc", null, null));
		
		return urlContext + "/list";
	}
	
	public String checkDuplicate(ModelMap model, HttpServletRequest request, NoGPSVehicle entity) {
		if (entity.getVehicle() == null) {
			return StringUtils.EMPTY;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vehicle", entity.getVehicle().getId());
		if (!(genericDAO.isUnique(NoGPSVehicle.class, entity, map))) {
			request.getSession().setAttribute("errors", "Cannot create duplicate entry");
			model.addAttribute("msg", "Cannot create duplicate entry with same vehicle");
			
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
		return StringUtils.EMPTY;
	}
}
