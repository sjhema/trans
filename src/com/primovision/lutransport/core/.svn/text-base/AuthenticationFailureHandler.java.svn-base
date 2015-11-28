package com.primovision.lutransport.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;

import com.primovision.lutransport.dao.admin.UserDAO;
import com.primovision.lutransport.model.User;

public class AuthenticationFailureHandler
		extends
		org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler {

	public AuthenticationFailureHandler() {
		super();
	}

	public AuthenticationFailureHandler(String defaultUrl) {
		super(defaultUrl);
	}

	@Autowired
	private UserDAO userDAO;

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		String username = request.getParameter("j_username");
		String password = request.getParameter("j_password");

		if (username != null && !"".equals(username)) {
			User user = userDAO.getUserByName(username);

			if (user == null) {
				request.getSession().setAttribute("error",
						"Invalid username or password");
			}

			else {
				if (password == null || "".equals(password)){
					request.getSession().setAttribute("error",
							"Password is required field.");
				}
				else{
					request.getSession().setAttribute("error",
					"Invalid username or password");
				}
			}
		} else
			request.getSession().setAttribute("error",
					"User Id and password is required field.");
		logger.warn("Login failed: " + exception.getMessage());
		super.onAuthenticationFailure(request, response, exception);
	}

}
