package com.primovision.lutransport.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.model.BusinessObject;
import com.primovision.lutransport.model.Role;
import com.primovision.lutransport.model.RolePrivilege;
import com.primovision.lutransport.model.menu.MenuHelper;
import com.primovision.lutransport.model.menu.MenuTree;

@Controller
@RequestMapping("/admin/access/roleprivilege")
public class RolePrivilegeController extends CRUDController<RolePrivilege> {

	public RolePrivilegeController() {
		setUrlContext("admin/access/roleprivilege");
	}
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		super.setupCreate(model, request);
		String roleId = request.getParameter("id");
		Long id = null;
		if (roleId == null) {
			id = (Long) request.getSession().getAttribute("roleId");
		} else {
			id = Long.parseLong(roleId);
		}
		StringBuffer query = new StringBuffer("select bo from BusinessObject bo where bo.status=1 order by bo.objectLevel, bo.displayOrder");
		List<BusinessObject> businessObjects = genericDAO.executeSimpleQuery(query.toString());
		MenuTree menuTree = MenuHelper.getMenuTree(businessObjects);
		model.put("fullMenuTree", menuTree);
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("role.id", id);
		request.getSession().setAttribute("rolePrivileges",
				genericDAO.findByCriteria(RolePrivilege.class, criteria));
		request.getSession().setAttribute("roleId", id);
		request.getSession().setAttribute("rolename",
				genericDAO.getById(Role.class,id).getName());
	}

	@SuppressWarnings("unchecked")
	@Override
	public String save(HttpServletRequest request, RolePrivilege entity,
			BindingResult bindingResult, ModelMap model) {
		String[] items = request.getParameterValues("item");
		Long roleId = (Long) request.getSession().getAttribute("roleId");
		RolePrivilege rolePrivilege = null;
		BusinessObject businessObject = null;
		Role role = null;
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("role.id", roleId);
		List<RolePrivilege> rolePrivileges = genericDAO.findByCriteria(RolePrivilege.class,criteria);
		for (RolePrivilege rolePrivlg : rolePrivileges) {
			genericDAO.deleteById(RolePrivilege.class,rolePrivlg.getId());
		}
		if (items != null) {
			rolePrivilege = new RolePrivilege();
			businessObject = new BusinessObject();
			role = new Role();
			role.setId(roleId);
			rolePrivilege.setRole(role);
			businessObject.setId(1L);
			rolePrivilege.setBusinessObject(businessObject);
			genericDAO.save(rolePrivilege);
			for (int i = 0; i < items.length; i++) {
				rolePrivilege = new RolePrivilege();
				businessObject = new BusinessObject();
				role = new Role();
				role.setId(roleId);
				rolePrivilege.setRole(role);
				businessObject.setId(Long.parseLong(items[i]));
				rolePrivilege.setBusinessObject(businessObject);
				genericDAO.save(rolePrivilege);
			}
		}
		return "redirect:/admin/access/role/list.do";
	}
}
