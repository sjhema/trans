package com.primovision.lutransport.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.primovision.lutransport.model.TripRate;

@Controller
@RequestMapping("/admin/triprate")
public class TripRateController extends CRUDController<TripRate> {
	
	public TripRateController(){
		setUrlContext("admin/triprate");
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {	
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(
				Location.class));
	}

	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		criterias.put("type", 1);
		model.addAttribute("transferStations",
				genericDAO.findByCriteria(Location.class, criterias));
		criterias.put("type", 2);
		model.addAttribute("landfills",
				genericDAO.findByCriteria(Location.class, criterias));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") TripRate entity,
			BindingResult bindingResult, ModelMap model) {
	
		if (entity.getLandfill() == null) {
			bindingResult.rejectValue("landfill", "error.select.option", null,
					null);
		}
		if (entity.getTransferStation() == null) {
			bindingResult.rejectValue("transferStation", "error.select.option", null,
					null);
		}
		return super.save(request, entity, bindingResult, model);
	}
}
