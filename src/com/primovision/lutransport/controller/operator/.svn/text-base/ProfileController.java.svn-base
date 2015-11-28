package com.primovision.lutransport.controller.operator;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.controller.BaseController;
import com.primovision.lutransport.dao.admin.UserDAO;
import com.primovision.lutransport.model.Language;
import com.primovision.lutransport.model.User;
@Controller
@RequestMapping("/operator/login")
public class ProfileController extends BaseController{
	
	@Autowired
	private UserDAO userDAO;
	
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	@RequestMapping("/home.do")
	public String displayHome(HttpServletRequest request, ModelMap model) {
		return "login/home";
	}

	@RequestMapping("/login.do")
	public String displayLoginPage(HttpServletRequest request, ModelMap model) {
		Map criterias = new HashMap();
		model.addAttribute("modelObject",new User());
		return "login/login";
	}
	
	@RequestMapping("/unauthorized.do")
	public String displayUnauthorizedPage(HttpServletRequest request, ModelMap model) {
		return "login/unauthorized";
	}
	
	@RequestMapping("/changepassword.do")
	public String create(ModelMap model,HttpServletRequest request){
	return "/operator/firsttimechangepassword/changePassword";
	}
	
	@RequestMapping("/updatepassword.do")
	public String save(HttpServletRequest request, User entity,
			BindingResult bindingResult, ModelMap model) {
		User user = (User) request.getSession().getAttribute("userInfo");
		String newPassword = request.getParameter("newpassword");
		String confPassword = request.getParameter("confirmpassword");
		String oldPassword = request.getParameter("oldpassword");
		//to Verify OldPassword
		if(oldPassword.isEmpty() && !user.getPassword().isEmpty()){
			request.getSession().setAttribute("errors", "Old Password is required field.");
			return "/operator/firsttimechangepassword/changePassword";
		}
		else if(!user.getPassword().equals(oldPassword)){
			request.getSession().setAttribute("errors", "Old Password is not correct.");
			return "/operator/firsttimechangepassword/changePassword";
		}
		//to Verify NewPassword
		if(newPassword.isEmpty() || confPassword.isEmpty()){
			request.getSession().setAttribute("errors", "New Password and Confirm Password are required field.");
			return "/operator/firsttimechangepassword/changePassword";
		}		
		else if(newPassword.equals(confPassword)){
			user.setPassword(newPassword);
			user.setAccountStatus(1);
			userDAO.saveOrUpdate(user);
			//request.getSession().setAttribute("msg", "Changed Password Successfully");
			return "/operator/firsttimechangepassword/success";			
		}else{
			request.getSession().setAttribute("errors", "New Password and Confirm Password are mismatch.");
			return "/operator/firsttimechangepassword/changePassword";
		}			
	}

	@RequestMapping("/changeLanguage.do")
	public String changeLanguage(HttpServletRequest request) 
	{
		String lang = request.getParameter("lang");
		if (lang!=null) {
			Language language= genericDAO.getById(Language.class, Long.parseLong(lang));
			setLanguageAttributes(request, language);
		}
		return "redirect:/login/login.do";
	}
}

