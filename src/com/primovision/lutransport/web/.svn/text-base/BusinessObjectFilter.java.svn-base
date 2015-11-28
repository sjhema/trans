package com.primovision.lutransport.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.model.BusinessObject;

public class BusinessObjectFilter implements Filter {
	private static Logger log = Logger.getLogger(BusinessObjectFilter.class.getName());
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String url = ((HttpServletRequest)request).getServletPath();
		GenericDAO genericDAO = (GenericDAO)SpringAppContext.getBean("genericDAO");
		String query = "select obj from BusinessObject obj where url like '%"+url+"%'";
		List<BusinessObject> bos = genericDAO.executeSimpleQuery(query);
		if (bos!=null && bos.size()>0) {
			((HttpServletRequest)request).getSession().setAttribute("curObj", bos.get(0));
			log.debug("Current business object is:"+bos.get(0).getObjectHierarchy());
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
