package com.primovision.lutransport.core.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.primovision.lutransport.model.Role;
import com.primovision.lutransport.service.AuthenticationService;

public class CustomSecureResourceFilter implements
		FilterInvocationSecurityMetadataSource {

	private List<String> ignoreUrls = new ArrayList<String>(0);

	@Autowired
	private AuthenticationService authenticationService;

	public void setAuthenticationService(
			AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object filter)
			throws IllegalArgumentException {
		FilterInvocation filterInvocation = (FilterInvocation) filter;
		String url = filterInvocation.getRequestUrl();
		if (url.indexOf("?") != -1)
			url = url.substring(0, url.indexOf("?"));
		url = url.substring(1);
		StringBuilder rolesStringBuilder = new StringBuilder();
		List<Role> roles = authenticationService.findRolesForURL(url);
		for (Role role : roles) {
			rolesStringBuilder.append("ROLE_" + role.getName().toUpperCase())
					.append(",");
		}
		if (rolesStringBuilder.indexOf(",") != -1)
			rolesStringBuilder
					.deleteCharAt(rolesStringBuilder.lastIndexOf(","));
		return SecurityConfig
				.createListFromCommaDelimitedString(rolesStringBuilder
						.toString());
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	public List<String> getIgnoreUrls() {
		return ignoreUrls;
	}

	public void setIgnoreUrls(List<String> ignoreUrls) {
		this.ignoreUrls = ignoreUrls;
	}

}
