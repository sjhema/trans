package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.primovision.lutransport.model.Terminal;

@Controller
@RequestMapping("/admin/terminal")
public class TerminalController extends CRUDController<Terminal>{
	public TerminalController() {
		setUrlContext("/admin/terminal");
	}
	
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		Map criterias = new HashMap();
		criterias.put("type", 3);
		model.addAttribute("companies",genericDAO.findByCriteria(Location.class, criterias,"name",false));
		criterias.put("type", 4);
		model.addAttribute("terminals", genericDAO.findByCriteria(Location.class, criterias,"name",false));
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
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"company.name",null,null));
		return urlContext + "/list";
	}
	@Override
	 public void initBinder(WebDataBinder binder) { 
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Location.class, new AbstractModelEditor(Location.class));
	}
	@Override
	public String save(HttpServletRequest request,@ModelAttribute("modelObject") Terminal entity,
			BindingResult bindingResult, ModelMap model) {
		if(entity.getCompany()==null){
			bindingResult.rejectValue("company", "error.select.option",
					null, null);
		}
		if(entity.getTerminal()==null){
			bindingResult.rejectValue("terminal", "error.select.option",
					null, null);
		}
		if (entity.getCompany()!=null&&entity.getTerminal()!=null&&entity.getId()==null){
			Map criterias=new HashMap();
			criterias.put("company.id", entity.getCompany().getId());
			criterias.put("terminal.id", entity.getTerminal().getId());
		
			List<Terminal> terminals=genericDAO.findByCriteria(Terminal.class, criterias);
			if(!terminals.isEmpty()){
				request.getSession().setAttribute("error","Duplicate Entry");
			setupCreate(model, request);
			return urlContext + "/form";
			}
		}
		return super.save(request, entity, bindingResult, model);
	}
}
