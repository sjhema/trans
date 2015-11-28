<%@ include file="/common/taglibs.jsp"%>
<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="net.sf.jasperreports.engine.util.*" %>
<%@ page import="net.sf.jasperreports.engine.export.*" %>
<%@ page import="net.sf.jasperreports.engine.data.JRBeanCollectionDataSource" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%
	JasperPrint jasperPrint = (JasperPrint)request.getAttribute("jasperPrint");
	JRHtmlExporter exporter = new JRHtmlExporter();
	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);
	StringBuffer buffer = new StringBuffer("</td></tr></table><table width=\"100%\"><tr><td width=\"50%\">&nbsp;</td></tr>\n");
	buffer.append("<input type=\"button\" value=\"Back\" onclick=\"javascript:location.href='"+request.getContextPath()+"/hr/report/employeepayroll/start.do'\">");
	buffer.append("<input type=\"button\" value=\"Export As Pdf\" onclick=\"javascript:location.href='"+request.getContextPath()+"/hr/report/employeepayroll/export.do?type=pdf'\">");
	buffer.append("<input type=\"button\" value=\"Export As CSV\" onclick=\"javascript:location.href='"+request.getContextPath()+"/hr/report/employeepayroll/export.do?type=csv'\">"); 
	buffer.append("</table>\n");
	exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, buffer.toString());
	exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, (Map)request.getAttribute("IMAGES_MAP"));
	exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,request.getContextPath() + "/image?image=");
	try{
		exporter.exportReport();
	}catch(Exception e){
			e.printStackTrace();
	}
	request.removeAttribute("jasperPrint");
%>
