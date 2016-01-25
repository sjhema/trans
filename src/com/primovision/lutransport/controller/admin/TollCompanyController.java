package com.primovision.lutransport.controller.admin;
//TollCompanyController
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
import com.primovision.lutransport.controller.editor.AbstractModelEditor;

import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.State;
import com.primovision.lutransport.model.TollCompany;


@Controller
@RequestMapping("/admin/tollcompany")
public class TollCompanyController extends CRUDController<TollCompany> {
	
	public TollCompanyController(){
		setUrlContext("admin/tollcompany");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#initBinder(org.springframework.web.bind.WebDataBinder)
	 */
	@Override
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(State.class, new AbstractModelEditor(State.class));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#setupCreate(org.springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
	   model.addAttribute("tollcompanyname",genericDAO.findByCriteria(TollCompany.class, criterias, "name", false));

		model.addAttribute("state", genericDAO.findByCriteria(State.class, criterias, "name", false));
		criterias.put("type", 1);
		model.addAttribute("transferStation",
				genericDAO.findByCriteria(Location.class, criterias, "name", false));
		criterias.put("type", 2);
		model.addAttribute("landfill",
				genericDAO.findByCriteria(Location.class, criterias, "name", false));
		
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
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
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#setupList(org.springframework.ui.ModelMap, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.primovision.lutransport.controller.CRUDController#save(javax.servlet.http.HttpServletRequest, com.primovision.lutransport.model.BaseModel, org.springframework.validation.BindingResult, org.springframework.ui.ModelMap)
	 */
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") TollCompany entity,
			BindingResult bindingResult, ModelMap model) {

		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option", null, null);
		}
		if(entity.getState() == null)
			bindingResult.rejectValue("state", "error.select.option", null, null);
		if(entity.getCity() == null)
			bindingResult.rejectValue("city", "typeMismatch.java.lang.String", null, null);
		//server side verification
		if(entity.getZipcode() != null){
			if(entity.getZipcode().toString().length() < 5)
			bindingResult.rejectValue("zipcode", "typeMismatch.java.lang.Integer", null, null);
		}
		if(!StringUtils.isEmpty(entity.getPhone())){
			if(entity.getPhone().length() < 12)
				bindingResult.rejectValue("phone", "typeMismatch.java.lang.String", null, null);
		}
		if(!StringUtils.isEmpty(entity.getFax())){
			if(entity.getFax().length() < 12)
				bindingResult.rejectValue("fax", "typeMismatch.java.lang.String", null, null);
		}
		return super.save(request, entity, bindingResult, model);
	}
}