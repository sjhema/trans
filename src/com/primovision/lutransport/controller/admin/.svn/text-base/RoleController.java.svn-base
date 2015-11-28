package com.primovision.lutransport.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.model.Role;

@Controller
@RequestMapping("admin/access/role")
public class RoleController extends CRUDController<Role> {

	public RoleController() {
		setUrlContext("admin/access/role");
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		super.setupCreate(model, request);
		model.addAttribute("statuses", listStaticData("STATUS"));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String save(HttpServletRequest request,@Valid @ModelAttribute("modelObject") Role entity, BindingResult bindingResult, ModelMap model) {
		if(bindingResult.hasErrors()) {
        	setupCreate(model, request);
        	return getUrlContext()+"/form";
        }
		//if Role name is unique
		Map<String,Object> criteria = new HashMap<String, Object>();
		entity.setName(entity.getName().toUpperCase());
		criteria.put("name", entity.getName());
		if(!genericDAO.isUnique(Role.class,entity, criteria)){
			setupCreate(model, request);
			FieldError error=new FieldError("modelObject","name",entity.getName(), false, null, null,"Role Name already exist.");
			bindingResult.addError(error);
			return getUrlContext()+"/form";
		}
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		return "redirect:/"+ getUrlContext()+"/list.do";
	}
}
