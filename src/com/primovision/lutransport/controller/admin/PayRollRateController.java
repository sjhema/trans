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
import com.primovision.lutransport.model.PayRollRate;
import com.primovision.lutransport.model.State;
/**
 * @author sudhir
 */

@Controller
@RequestMapping("/admin/payrollrate")
public class PayRollRateController extends CRUDController<PayRollRate>{
	
	public PayRollRateController(){
		setUrlContext("admin/payrollrate");
	}
	@Override
	@InitBinder
	 public void initBinder(WebDataBinder binder) { 
		binder.registerCustomEditor(State.class, new AbstractModelEditor(State.class));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
	}
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		criterias.put("type", 1);
		model.addAttribute("origins",
				genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 2);
		model.addAttribute("destinations",
				genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("states", genericDAO.findByCriteria(Location.class, criterias,"name",false));
	}
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		populateSearchCriteria(request, request.getParameterMap());
		Map criterias = new HashMap();
		criterias.put("type", 1);
		model.addAttribute("origins",
				genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 2);
		model.addAttribute("destinations",
				genericDAO.findByCriteria(Location.class, criterias,"name",false));
	}
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject")PayRollRate entity,
			BindingResult bindingResult, ModelMap model) {
		
		if(entity.getOrigin()==null)
		{
			bindingResult.rejectValue("origin", "error.select.option", null, null);
		}		
		if(entity.getState()==null)
		{
			bindingResult.rejectValue("state", "error.select.option", null, null);
		}
		if(entity.getDestination()==null)
		{
			bindingResult.rejectValue("destination", "error.select.option", null, null);
		}
		return super.save(request, entity, bindingResult, model);
	}
	
}

	
