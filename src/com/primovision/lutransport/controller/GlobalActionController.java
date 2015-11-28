package com.primovision.lutransport.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.primovision.lutransport.core.dao.GenericDAO;
import com.primovision.lutransport.model.BusinessObject;

@Controller
@RequestMapping("/global")
public class GlobalActionController extends GenericController {

	@Autowired
	private GenericDAO genericDAO;

	public void setGenericDAO(GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	@RequestMapping("/displayTab.do")
	public String displayTab(HttpServletRequest request) {
		request.getSession().setAttribute("boid",
				Long.parseLong(request.getParameter("id")));
		BusinessObject currentBusinessObject = genericDAO.getById(
				BusinessObject.class,
				Long.parseLong(request.getParameter("id")));
		request.getSession().setAttribute("curObj", currentBusinessObject);
		return "redirect:" + currentBusinessObject.getAction();
	}

	@RequestMapping("/displaySubTab.do")
	public String displaySubTab(HttpServletRequest request) {
		request.getSession().setAttribute("bosubid",
				Long.parseLong(request.getParameter("id")));
		BusinessObject currentBusinessObject = genericDAO.getById(
				BusinessObject.class,
				Long.parseLong(request.getParameter("id")));
		return "redirect:" + currentBusinessObject.getUrl();
	}
}
