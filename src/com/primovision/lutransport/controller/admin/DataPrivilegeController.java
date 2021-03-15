package com.primovision.lutransport.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

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
import com.primovision.lutransport.core.util.CoreUtil;

import com.primovision.lutransport.model.BusinessObject;
import com.primovision.lutransport.model.DataPrivilege;
import com.primovision.lutransport.model.Role;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.hr.EmployeeCatagory;

@Controller
@RequestMapping("/admin/access/dataprivilege")
public class DataPrivilegeController extends CRUDController<DataPrivilege> {
	public DataPrivilegeController() {
		setUrlContext("admin/access/dataprivilege");
	}
	
	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		super.initBinder(binder);
		
		binder.registerCustomEditor(BusinessObject.class, new AbstractModelEditor(BusinessObject.class));
		binder.registerCustomEditor(Role.class, new AbstractModelEditor(Role.class));
	}
	
	private void setupCommon(ModelMap model, HttpServletRequest request) {
		List<String> empCatNames = retrieveEmpCatNames();
		model.addAttribute("empCatNames", empCatNames);
		
		List<BusinessObject> boList = getProtectedBO();
		model.addAttribute("boList", boList);
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		super.setupCreate(model, request);
		
		setupCommon(model, request);
		
		DataPrivilege entity = (DataPrivilege) model.get("modelObject");
		entity.setPrivilegeArrEmpCat(new String[0]);
		
		Long roleId = null;
		Role role = entity.getRole();
		if (role == null || role.getId() == null) {
			String roleIdStr = request.getParameter("roleId");
			if (StringUtils.isNotEmpty(roleIdStr)) {
				roleId = Long.valueOf(roleIdStr);
			}
		} else if (StringUtils.isEmpty(role.getName())) {
			roleId = role.getId();
		}
		if (roleId != null) {
			role = genericDAO.getById(Role.class, roleId);
			entity.setRole(role);
		}
	}
	
	@Override
	public void setupList(ModelMap model, HttpServletRequest request) {
		populateSearchCriteria(request, request.getParameterMap());
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		String roleId = (String) criteria.getSearchMap().get("role.id");
		model.addAttribute("roleId", roleId);
		
		Role role = genericDAO.getById(Role.class, Long.valueOf(roleId));
		model.addAttribute("roleName", role.getName());
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.setPageSize(25);
		
		List<EmployeeCatagory> empCategories = retrieveEmpCatagories();
		List<DataPrivilege> dataPrivilegeList = genericDAO.search(DataPrivilege.class, criteria);
		for (DataPrivilege aDataPrivilege : dataPrivilegeList) {
			String empCatPrivilege = aDataPrivilege.getEmpCatPriv();
			if (StringUtils.isEmpty(empCatPrivilege)) {
				continue;
			}
			
			List<String> empCatNamesList = buildEmpCatNamesList(empCategories, empCatPrivilege);
			aDataPrivilege.setEmpCatPrivNames(CoreUtil.toString(empCatNamesList));
		}
		
		model.addAttribute("list", genericDAO.search(getEntityClass(), criteria,  "bo.objectName asc", null, null));
		return urlContext + "/list";
	}
	
	@Override
	public String delete(@ModelAttribute("modelObject") DataPrivilege entity,
			BindingResult bindingResult, HttpServletRequest request) {
		String listParams = constructListParams(entity);
		
		super.delete(entity, bindingResult, request);
		
		return "redirect:/" + urlContext + "/list.do" + listParams;
	}
	
	private String constructListParams(DataPrivilege entity) {
		return "?role.id=" + entity.getRole().getId();
	}
	
	private List<String> buildEmpCatNamesList(List<EmployeeCatagory> empCategories, String empCatPrivilege) {
		List<String> empCatNamesList = new ArrayList<String>();
		String[] empCatIdArr = empCatPrivilege.split(",");
		for (String empCatId : empCatIdArr) {
			empCatNamesList.add(extractEmpCatName(empCategories, empCatId));
		}
		return empCatNamesList;
	}
	
	@Override
	public void setupUpdate(ModelMap model, HttpServletRequest request) {
		setupCommon(model, request);
		
		DataPrivilege entity = (DataPrivilege) model.get("modelObject");
		populateEmpCatPrivilegeNames(entity);
	}
	
	private void populateEmpCatPrivilegeNames(DataPrivilege entity) {
		String empCatPriv = entity.getEmpCatPriv();
		if (StringUtils.isEmpty(empCatPriv)) {
			entity.setPrivilegeArrEmpCat(new String[0]);
			return;
		} 
		
		List<EmployeeCatagory> empCategories = retrieveEmpCatagories();
		
		List<String> accessibleEmpCats = buildEmpCatNamesList(empCategories, empCatPriv);
		String[] accessibleEmpCatsArr = accessibleEmpCats.toArray(new String[0]);
		entity.setPrivilegeArrEmpCat(accessibleEmpCatsArr);
	}
	
	private String extractEmpCatName(List<EmployeeCatagory> empCategories, String id) {
		String empCatName = StringUtils.EMPTY;
		Long empCatId = Long.valueOf(id);
		for (EmployeeCatagory anEmpCat : empCategories) {
			if (anEmpCat.getId().longValue() == empCatId.longValue()) {
				empCatName = anEmpCat.getName();
			}
		}
		return empCatName;
	}
	
	private List<String> retrieveEmpCatNames() {
		List<EmployeeCatagory> empCategories = retrieveEmpCatagories();
		List<String> empCatNames = new ArrayList<String>();
		for (EmployeeCatagory anEmpCat : empCategories) {
			empCatNames.add(anEmpCat.getName());
		}
		return empCatNames;
	}
	
	private List<EmployeeCatagory> retrieveEmpCatagories() {
		String query = "select obj from EmployeeCatagory obj where obj.status=1 order by obj.name";
		List<EmployeeCatagory> empCategories = genericDAO.executeSimpleQuery(query);
		return empCategories;
	}
	
	private String buildEmpCatPrivilege(String[] privilegeArr) {
		String privilege = StringUtils.EMPTY;
		if (privilegeArr == null || privilegeArr.length <= 0) {
			return privilege;
		}
		
		for (String aPrivilege : privilegeArr) {
			EmployeeCatagory empCat = retrieveEmpCat(aPrivilege);
			privilege += (empCat.getId() + ",");
		}
		return privilege.substring(0, privilege.length() - 1);
	}
	
	private EmployeeCatagory retrieveEmpCat(String name) {
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("name", name);
		List<EmployeeCatagory> empCatList = genericDAO.findByCriteria(EmployeeCatagory.class, criterias, "name", false);
		return empCatList.get(0);
	}
	
	private void validateSave(DataPrivilege entity, BindingResult bindingResult, HttpServletRequest request) {
		if (entity.getBo() == null || entity.getBo().getId() == null) {
			bindingResult.rejectValue("bo.id", "error.select.option", null, null);
		}
	}
	
	@Override
	public String save(HttpServletRequest request,
			@ModelAttribute("modelObject") DataPrivilege entity, BindingResult bindingResult, ModelMap model) {
		validateSave(entity, bindingResult, request);
		try {
			getValidator().validate(entity, bindingResult);
		} catch (ValidationException e) {
			e.printStackTrace();
			log.warn("Error in validation :" + e);
		}
		
		if (bindingResult.hasErrors()) {
			setupCreate(model, request);
			return urlContext + "/form";
		}
		
		String empCatPrivilege = buildEmpCatPrivilege(entity.getPrivilegeArrEmpCat());
		entity.setEmpCatPriv(empCatPrivilege);
		
		if (StringUtils.isEmpty(entity.getHasDeletable())) {
			entity.setHasDeletable(null);
		}
		if (StringUtils.isEmpty(entity.getHasEditable())) {
			entity.setHasEditable(null);
		}
		
		beforeSave(request, entity, model);
		genericDAO.saveOrUpdate(entity);
		cleanUp(request);
		
		String listParams = constructListParams(entity);
		return "redirect:/" + urlContext + "/list.do" + listParams;
	}
}
