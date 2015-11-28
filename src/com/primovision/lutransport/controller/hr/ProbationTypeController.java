package com.primovision.lutransport.controller.hr;

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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.Location;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.model.hr.HolidayType;
import com.primovision.lutransport.model.hr.ProbationType;


@Controller
@RequestMapping("/hr/probationtype")
public class ProbationTypeController extends CRUDController<ProbationType>{

	public ProbationTypeController() {
	setUrlContext("/hr/probationtype");
	}
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("categories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		
		
		criterias.clear();
		
		model.addAttribute("probationNames",genericDAO.findByCriteria(ProbationType.class, criterias, "probationName", false));
		model.addAttribute("probationCategories", genericDAO.executeSimpleQuery("select obj from SetupData obj where dataType='Probation_Category'  order by obj.dataLabel asc"));
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
		 model.addAttribute("list",genericDAO.search(getEntityClass(), criteria));
		return urlContext + "/list";
	}
	
	@Override
	 public void initBinder(WebDataBinder binder) { 
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
		binder.registerCustomEditor(EmployeeCatagory.class, new AbstractModelEditor(EmployeeCatagory.class));	   
	}
	
	@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") ProbationType entity,
			BindingResult bindingResult, ModelMap model) {
	
		
		if(StringUtils.isEmpty(entity.getProbationCategory())){
			bindingResult.rejectValue("probationCategory", "error.select.option",
					null, null);
			
		}
		if(entity.getEmpCategory()==null){
			bindingResult.rejectValue("empCategory", "error.select.option",
					null, null);
		
		}
		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option",
					null, null);
			
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option",
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
			return urlContext + "/form";
		}		
		
		StringBuffer query=new StringBuffer("");
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateFrom=dateFormat1.format(entity.getDateFrom());
		String dateTo=dateFormat1.format(entity.getDateTo());
		query.append("select obj from ProbationType obj where obj.company.id="+entity.getCompany().getId()+" and obj.terminal.id="
		+entity.getTerminal().getId()+" and obj.probationName='"+entity.getProbationName()+"' and obj.empCategory.id="+entity.getEmpCategory().getId()+"and obj.probationCategory='"+entity.getProbationCategory()+"' and obj.dateFrom<='"+dateFrom+"' and obj.dateTo>='"+dateTo+"'");
		if(entity.getId()!=null){
			query.append("and obj.id!="+entity.getId());
		}
			
		List<ProbationType> list=genericDAO.executeSimpleQuery(query.toString());			
		
		if(list.size()>0 && list!=null){
			request.getSession().setAttribute("error","duplicate entry");
			setupCreate(model, request);
			return urlContext + "/form";
		}	
		
		return super.save(request, entity, bindingResult, model);
	}
}
