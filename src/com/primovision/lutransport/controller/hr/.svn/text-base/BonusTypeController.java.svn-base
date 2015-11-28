package com.primovision.lutransport.controller.hr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.primovision.lutransport.model.StaticData;
import com.primovision.lutransport.model.hr.BonusType;
import com.primovision.lutransport.model.hr.EmpBonusTypesList;
import com.primovision.lutransport.model.hr.EmployeeCatagory;
import com.primovision.lutransport.service.DateUpdateService;

/**
 * @author kishor
 *
 */
@Controller
@RequestMapping("/hr/bonustype")
public class BonusTypeController extends CRUDController<BonusType>{
	
	public BonusTypeController() {
      
		setUrlContext("/hr/bonustype");
	}
	
	@Autowired
	private DateUpdateService dateupdateService;
	
	public void setDateupdateService(DateUpdateService dateupdateService) {
		this.dateupdateService = dateupdateService;
	}
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		model.addAttribute("bonustypes", genericDAO.findByCriteria(BonusType.class, criterias,"typename",false));
		model.addAttribute("catagories", genericDAO.findByCriteria(EmployeeCatagory.class, criterias,"name",false));
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("dataType", "STATUS");
		map.put("dataValue", "0,1");
		model.addAttribute("bonustypestatus", genericDAO.findByCriteria(StaticData.class, map,"dataText",false));
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		setupCreate(model, request);
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
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"company asc,terminal asc, typename",false,null));
		return urlContext + "/list";
	}
	
	@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") BonusType entity,
			BindingResult bindingResult, ModelMap model) {
		/*if(entity.getName()==null){
			bindingResult.rejectValue("name", "error.select.option",
					null, null);
		}*/
		
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
		return super.save(request, entity, bindingResult, model);
	}
	
	@Override
	public String delete(@ModelAttribute("modelObject")BonusType entity, BindingResult bindingResult,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		Map criterias=new HashMap();
		criterias.put("bonusType.id", entity.getId());
		EmpBonusTypesList bonusTypesList=genericDAO.getByCriteria(EmpBonusTypesList.class, criterias);
		try {
			if(bonusTypesList==null){
			genericDAO.delete(entity);
			}else{
				request.getSession().setAttribute("error", "can't be deleted bonus type, its in use");
			}
		} catch (Exception ex) {
			request.getSession().setAttribute(
					"errors",
					"This" + entity.getClass().getSimpleName()
							+ " can't be deleted");
			log.warn("Error deleting record " + entity.getId(), ex);
			request.getSession().setAttribute("error","Cannot delete a parent row");
		}
		// return to list
		return "redirect:/" + urlContext + "/list.do";
	}
}
