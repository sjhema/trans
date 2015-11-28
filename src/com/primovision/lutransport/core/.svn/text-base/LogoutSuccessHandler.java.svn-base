package com.primovision.lutransport.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;

public class LogoutSuccessHandler
		implements
		org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
	private static final Log LOG = LogFactory
			.getLog(LogoutSuccessHandler.class);

	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication auth)
			throws IOException, ServletException {
		response.sendRedirect(request.getContextPath() + "/login/login.do");
		LOG.debug("Logout successful");
	}

}
