package com.primovision.lutransport.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import antlr.collections.List;

import com.primovision.lutransport.controller.CRUDController;
import com.primovision.lutransport.controller.editor.AbstractModelEditor;
import com.primovision.lutransport.model.Role;
import com.primovision.lutransport.model.SearchCriteria;
import com.primovision.lutransport.model.Ticket;
import com.primovision.lutransport.model.User;

@Controller
@RequestMapping("/admin/access/user")
public class UsersController extends CRUDController<User> {

	public UsersController() {
		setUrlContext("admin/access/user");
	}
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		super.setupCreate(model, request);
		Map criterias = new HashMap();
		criterias.clear();
		criterias.put("status", 1);
		model.addAttribute("statuses", listStaticData("STATUS"));
		model.addAttribute("roles", genericDAO.findAll(Role.class));
		
		String firstNameQuery="select distinct(obj.firstName) from Driver obj order by obj.firstName";
		model.addAttribute("employeesFirstName", genericDAO.executeSimpleQuery(firstNameQuery));
		String lastNameQuery="select distinct(obj.lastName) from Driver obj order by obj.lastName";
		model.addAttribute("employeesLastName", genericDAO.executeSimpleQuery(lastNameQuery));
	}
	
	

	@RequestMapping(method = RequestMethod.GET, value = "/resetuserpassword.do")
	public String resetUserPassword(ModelMap model, HttpServletRequest request) {
		setupUpdate(model, request);
		return urlContext + "/resetpasswordform";
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/updatenewpassword.do")
	public String updatePassword(HttpServletRequest request, @ModelAttribute("modelObject") User user, BindingResult results, ModelMap model) {
		validator.validate(user, results);
		if (user.getPassword()==null || user.getPassword().length()<6) {
			results.rejectValue("password", "errors.password", new Object[]{"Password"},"Invalid password");
		}
		if (user.getConfirmPassword()==null || user.getConfirmPassword().length()<6 || (!user.getPassword().equals(user.getConfirmPassword()))) {
			results.rejectValue("confirmPassword", "errors.password", new Object[]{"Password"},"Invalid confirm password");
		}
		
		if (results.hasErrors()) {
			setupCreate(model, request);
			return getUrlContext()+"/resetpasswordform";
		}
		
		try {
			genericDAO.saveOrUpdate(user);
			request.getSession().setAttribute("savedUser", user);
			request.getSession().setAttribute("msg", "User information saved successfully.");
			return "redirect:/"+getUrlContext()+"/list.do";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("err", e.getMessage());
			setupCreate(model, request);
			return getUrlContext()+"/resetpasswordform";
		}
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		criteria.setPageSize(25);
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"username",false));
		model.addAttribute("lists",genericDAO.executeSimpleQuery("select obj from User obj order by username asc"));
		model.addAttribute("statuses", listStaticData("STATUS"));
		model.addAttribute("roles", genericDAO.findAll(Role.class));
		return urlContext + "/list";
	}
	
	@Override
	public String search2(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		SearchCriteria criteria = (SearchCriteria) request.getSession()
				.getAttribute("searchCriteria");
		model.addAttribute("list",genericDAO.search(getEntityClass(), criteria,"username",false));
		model.addAttribute("lists",genericDAO.executeSimpleQuery("select obj from User obj order by username asc"));
		model.addAttribute("statuses", listStaticData("STATUS"));
		model.addAttribute("roles", genericDAO.findAll(Role.class));
		return urlContext + "/list";
	}
	
	@Override
	public String save(HttpServletRequest request, @ModelAttribute("modelObject") User user, BindingResult results, ModelMap model) {
		validator.validate(user, results);
		boolean error = false;
		if (user.getId()==null) {
			if (user.getPassword()==null || user.getPassword().length()<6) {
				results.rejectValue("password", "errors.password", new Object[]{"Password"},"Invalid password");
			}
			if (user.getConfirmPassword()==null || user.getConfirmPassword().length()<6 || (!user.getPassword().equals(user.getConfirmPassword()))) {
				results.rejectValue("confirmPassword", "errors.password", new Object[]{"Password"},"Invalid confirm password");
			}
		}
		if (results.hasErrors()) {
			setupCreate(model, request);
			return getUrlContext()+"/form";
		}
		/*String query="select obj from User obj where obj.firstName='"+user.getFirstName()+"' and obj.lastName='"+user.getLastName()+"' or obj.username='"+user.getUsername()+"' or obj.name='"+user.getName()+"'";
		java.util.List<User> user2=genericDAO.executeSimpleQuery(query);
		if(!user2.isEmpty()){
			setupCreate(model, request);
			request.getSession().setAttribute("error"," user  alredy exist");
			return getUrlContext()+"/form";
		}*/

		StringBuffer query = new StringBuffer("select obj from User obj where"); 
		if(user.getId()!=null){			
			query.append(" obj.id!="+user.getId()+" and");			
		}
		query.append(" obj.firstName='"+user.getFirstName()+"' and obj.lastName='"+user.getLastName()+"'");		
		java.util.List<User> user2=genericDAO.executeSimpleQuery(query.toString());
		
		if(!user2.isEmpty()){
			error=true;
		}
		else
		{
			StringBuffer query1=new StringBuffer("select obj from User obj where");
			if(user.getId()!=null){				
				query1.append(" obj.id!="+user.getId()+" and");			
			}
			query1.append(" obj.username='"+user.getUsername()+"'");
			java.util.List<User> user3=genericDAO.executeSimpleQuery(query1.toString());
			if(!user3.isEmpty()){
				error=true;
			}
			else
			{				
				StringBuffer query2=new StringBuffer("select obj from User obj where");
				if(user.getId()!=null){				
					query2.append(" obj.id!="+user.getId()+" and");			
				}
				query2.append(" obj.name='"+user.getName()+"'");
				java.util.List<User> user4=genericDAO.executeSimpleQuery(query2.toString());
				if(!user4.isEmpty()){
					error=true;
				}
			}
		}
		if(error){
			setupCreate(model, request);
			request.getSession().setAttribute("error"," user  alredy exist");
			return getUrlContext()+"/form";
		}

		try {
			genericDAO.saveOrUpdate(user);
			//request.getSession().setAttribute("savedUser", user);
			request.getSession().setAttribute("msg", "User information saved successfully.");
			return "redirect:/"+getUrlContext()+"/list.do";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("err", e.getMessage());
			setupCreate(model, request);
			return getUrlContext()+"/form";
		}
	}
	
	/*@RequestMapping(method = RequestMethod.GET, value = "/delete.do")
	public String delete(@ModelAttribute("modelObject") Ticket entity,
			BindingResult bindingResult, HttpServletRequest request) {
		try {
			genericDAO.delete(entity);
		} catch (Exception ex) {
			request.getSession().setAttribute(
					"errors",
					"This" + entity.getClass().getSimpleName()
							+ " can't be deleted");
			log.warn("Error deleting record " + entity.getId(), ex);
		}
		// return to list
		return "redirect:/" + urlContext + "/list.do";
	}*/
	
	@Override
	public String delete(User entity, BindingResult bindingResult,
			HttpServletRequest request) {

		String query ="select obj from Ticket obj where obj.createdBy="+entity.getId();
				java.util.List<Ticket> tickets=genericDAO.executeSimpleQuery(query);
				User user=genericDAO.getById(User.class,entity.getId());
				if(!tickets.isEmpty()){
					
					request.getSession().removeAttribute("createdBy");
					request.getSession().setAttribute("error","can't delete user: "+user.getName()+" ,this user has added ticket.so please make status to inactive");
					return "redirect:/" + urlContext + "/list.do";
					
				}
				
			
				try {
					genericDAO.delete(user);
				} catch (Exception ex) {
					request.getSession().setAttribute(
							"errors",
							"This" + entity.getClass().getSimpleName()
									+ " can't be deleted");
					log.warn("Error deleting record " + entity.getId(), ex);
				}
				// return to list
				return "redirect:/" + urlContext + "/list.do";
	}

	@RequestMapping("/resetLockout.do")
	public String resetLockout(HttpServletRequest request, ModelMap modelMap) {
		modelMap.addAttribute("user",new User());
		return getUrlContext()+"/userLockoutReset";

	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Role.class,new AbstractModelEditor(Role.class));
		binder.registerCustomEditor(User.class,new AbstractModelEditor(User.class));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
