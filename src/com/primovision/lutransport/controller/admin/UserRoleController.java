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
import com.primovision.lutransport.model.Role;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.UserRole;

@Controller
@RequestMapping("/admin/access/userrole")
public class UserRoleController extends CRUDController<UserRole>{

	public UserRoleController() {
		setUrlContext("admin/access/userrole");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		String userid =request.getParameter("id");
		Long id=null;
		if(userid==null){
			id=(Long) request.getSession().getAttribute("userId");
		}else{
		id=Long.parseLong(userid);
		request.getSession().setAttribute("userId", id);}
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("id", id);
		model.addAttribute("roles", genericDAO.findByNamedQuery("selectRoleById", criteria));
		Map<String, Object> criterias = new HashMap<String, Object>();
		criterias.put("user.id", id);
		model.addAttribute("userroles", genericDAO.findByCriteria(UserRole.class,criterias));
		super.setupCreate(model, request);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String save(HttpServletRequest request, UserRole entity, BindingResult bindingResult, ModelMap model) {
		String[] roles= request.getParameterValues("userrole");
		Long userId = (Long) request.getSession().getAttribute("userId");
		Role role;User user;UserRole objuserRole=null;
		List<UserRole> userRoles = null;
		if(roles!=null){
		for(int i=0;i<roles.length;i++){
			role=new Role();
			user=new User();
			objuserRole=new UserRole();
			user.setId(userId);
			role.setId(Long.parseLong(roles[i]));
			objuserRole.setRole(role);
			objuserRole.setUser(user);
			Map<String, Object> criterias = new HashMap<String, Object>();
			criterias.put("role.id", Long.parseLong(roles[i]));
			criterias.put("user.id", userId);
			userRoles=genericDAO.findByCriteria(UserRole.class,criterias);
			if(!userRoles.isEmpty()){
				Map<String, Object> criteria = new HashMap<String, Object>();
				criteria.put("id", Long.parseLong(roles[i]));
				criteria.put("userid", userId);	
				List<UserRole> userRoleList=genericDAO.findByNamedQuery("selectUserRoleById", criteria);
				for(UserRole userRole:userRoleList){
					genericDAO.deleteById(UserRole.class,userRole.getId());
				}
			}else{
				genericDAO.save(objuserRole);
			}
		   }
		}
		
		//if role is null
		if(roles==null){
			Map<String, Object> criteria = new HashMap<String, Object>();
			criteria.put("user.id", userId);
			userRoles=genericDAO.findByCriteria(UserRole.class,criteria);
			genericDAO.deleteById(UserRole.class,userRoles.get(0).getId());
		}
		return "redirect:/systemuser/list.do";
	}
}
