package com.primovision.lutransport.controller.hr;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
/*import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.Customer;
import com.primovision.lutransport.model.FuelVendor;
*/import com.primovision.lutransport.model.hr.Leave;
/*import com.primovision.lutransport.model.Location;*/
import com.primovision.lutransport.model.SearchCriteria;
/*import com.primovision.lutransport.model.State;*/

@Controller
@RequestMapping("/hr/leave")
public class LeaveController extends CRUDController<Leave> {
	
	public LeaveController(){
		setUrlContext("hr/leave");
	}
	
	@Override
	public void initBinder(WebDataBinder binder) {
		
		//binder.registerCustomEditor(State.class, new AbstractModelEditor(State.class));
	}
	
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) 
	{
		//Map criterias = new HashMap();
		
       System.out.println("\nLeave contro---setupCreate\n");
		model.addAttribute("LeaveTypes", listStaticData("LEAVE_TYPE"));
		
	}
	
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		System.out.println("\nLeave contro---list\n");
		setupList(model, request);
		System.out.println("\nLeave contro---list1\n");
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		System.out.println("\nLeave contro---list2\n");
		 model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"leavetype",null,null));
		 System.out.println("\nLeave contro---list3\n");
		return urlContext + "/list";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		System.out.println("\nLeave contro---search\n");
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"leavetype",null,null));
		return urlContext + "/list";
	}
	
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		System.out.println("\nLeave contro---setuplist\n");
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") Leave entity,
			BindingResult bindingResult, ModelMap model)
	{
         
		return super.save(request, entity, bindingResult, model);
	}
}
	
	
	
	

