package com.primovision.lutransport.core;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.dao.admin.UserDAO;
import com.primovision.lutransport.model.BusinessObject;
import com.primovision.lutransport.model.Role;
import com.primovision.lutransport.model.User;
import com.primovision.lutransport.model.menu.MenuHelper;
import com.primovision.lutransport.model.menu.MenuTree;

public class AuthenticationSuccessHandler
		extends
		org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private GenericDAO genericDAO;

	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	@Autowired
	private UserDAO userDAO;

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public AuthenticationSuccessHandler() {
		super();
	}

	public AuthenticationSuccessHandler(String defaultUrl) {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication auth)
			throws IOException, ServletException {
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		User user = userDAO.getUserByName(userDetails.getUsername());
		System.out.println("User retrieved = " + user.getName());
		Timestamp currTime = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		System.out.println("Time = " + currTime);
		user.setLastLoginDate(currTime);
		Role role = user.getRole();
		System.out.println("Role = " + role.getName());
		// request.getSession().setAttribute("theme", role.getTheme());
		//genericDAO.saveOrUpdate(user);
		request.getSession().setAttribute("userInfo", user);
		StringBuffer query = new StringBuffer(
				"select distinct bo from BusinessObject bo, RolePrivilege rp where rp.businessObject.id=bo.id and rp.role.id="
						+ role.getId());
		query.append(" order by bo.objectLevel, bo.displayOrder");
		try {
			List<BusinessObject> businessObjects = genericDAO
					.executeSimpleQuery(query.toString());
			MenuTree menuTree = MenuHelper.getMenuTree(businessObjects);
			request.getSession().setAttribute("menuTree", menuTree);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		super.onAuthenticationSuccess(request, response, auth);
	}
}