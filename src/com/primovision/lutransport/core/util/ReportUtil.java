package com.primovision.lutransport.core.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.JasperPrint;

import net.sf.jasperreports.j2ee.servlets.ImageServlet;

public class ReportUtil {
	public static String JASPER_PRINT_SESSION_ATTRIBUTE_KEY = ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE;
	
	protected static Logger log = Logger
			.getLogger("com.primovision.lutransport.core.util.ReportUtil");
	
	public static void addJasperPrint(HttpServletRequest request, JasperPrint jp, String className) {
		request.getSession().setAttribute(JASPER_PRINT_SESSION_ATTRIBUTE_KEY, jp);
		log.info(className + "->Added Jasper Print");
	}
	
	public static String removeJasperPrint(HttpServletRequest request, String className) {
		request.getSession().removeAttribute(JASPER_PRINT_SESSION_ATTRIBUTE_KEY);
		log.info(className + "->Removed Jasper Print");
		return "Success";
	}
	
	public static JasperPrint getJasperPrint(HttpServletRequest request) {
		JasperPrint jasperPrint = (JasperPrint)request.getSession().getAttribute(JASPER_PRINT_SESSION_ATTRIBUTE_KEY);
		return jasperPrint;
	}
	
	public static boolean hasJasperPrint(HttpServletRequest request) {
		JasperPrint jasperPrint = (JasperPrint)request.getSession().getAttribute(JASPER_PRINT_SESSION_ATTRIBUTE_KEY);
		return (jasperPrint != null ? true : false);
	}
}
