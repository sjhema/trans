package com.primovision.lutransport.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.core.util.PasswordUtil;
import com.primovision.lutransport.dao.admin.UserDAO;
import com.primovision.lutransport.model.Language;
import com.primovision.lutransport.model.User;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController{
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
	
	/**
	 * To dispatch request to forgetPassword Page
	 * @param request
	 * @param model
	 * @return
	 */
	/*@RequestMapping("/forgetpassword.do")
	public String forgetPassword(HttpServletRequest request, ModelMap model) {
		return "login/forgetpassword";
	}*/
	
	/**
	 * Method to "Reset Password" results in setting empty string to password corresponding to UserId.
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	/*@RequestMapping("/resetpassword.do")
	public String resetPassword(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String userid = request.getParameter("userid");
		if(StringUtils.isEmpty(userid))
		{
			request.getSession().setAttribute("errors", "UserId is required");
			return "login/forgetpassword";
		}
		else{
			Map<String, Object> criterias = new HashMap<String, Object>();
			criterias.put("username", userid);
			List<User> useList = genericDAO.findByCriteria(User.class, criterias);
			if(useList.isEmpty()){
				request.getSession().setAttribute("errors", "No User exits for this UserId");
				return "login/forgetpassword";
			}
			else{
				User user = useList.get(0);
				user.setPassword(PasswordUtil.generatePassword(6,6));
				genericDAO.saveOrUpdate(user);
				request.getSession().setAttribute("msg", "Your password has been reset.");
				model.addAttribute("password",user.getPassword());
				return "login/resetpassword";
			}
		}		
		
	}*/
	
	@RequestMapping("/unauthorized.do")
	public String displayUnauthorizedPage(HttpServletRequest request, ModelMap model) {
		return "login/unauthorized";
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
