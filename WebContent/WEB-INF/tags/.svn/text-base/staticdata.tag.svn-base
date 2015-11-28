<%@ tag import="com.primovision.lutransport.core.tags.CacheUtil,com.primovision.lutransport.model.StaticData" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="type" required="true" type="java.lang.String" %>
<%@ attribute name="value" required="true" type="java.lang.String" rtexprvalue="true"%>
<%
	out.print(CacheUtil.getText("staticDataCache",type+"_"+value));
%>