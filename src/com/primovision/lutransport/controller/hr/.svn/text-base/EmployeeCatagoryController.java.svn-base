package com.primovision.lutransport.controller.hr;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.hr.EmployeeCatagory;

/**
 * @author kishor
 *
 */
@Controller
@RequestMapping("/hr/employeecatagory")
public class EmployeeCatagoryController extends CRUDController<EmployeeCatagory> {
	
	public EmployeeCatagoryController() {
		setUrlContext("/hr/employeecatagory");
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		 model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"name",null,null));
		return urlContext + "/list";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"name",null,null));
		return urlContext + "/list";
	}

@Override
public void setupCreate(ModelMap model, HttpServletRequest request) {
	Map criterias = new HashMap();
	model.addAttribute("catagory",genericDAO.findByCriteria(EmployeeCatagory.class, criterias, "name", false));
}
}

