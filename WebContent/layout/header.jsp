<%@ include file="/common/taglibs.jsp"%>
<%@page import="java.util.List"%>
<div id="header">
<table cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td align="${left}" width="65%" valign="top">
            <primo:label code="Welcome"/>
                        <c:if test="${sessionScope.userInfo==null}"><b><primo:label code="Guest!"/></b></c:if> 
                        <c:if test="${sessionScope.userInfo!=null}"><b>${sessionScope.userInfo.firstName} ${sessionScope.userInfo.lastName}!</b><br/><span style="font-size: 10px"><primo:label code="Last Login"/> : <fmt:formatDate value="${sessionScope.userInfo.lastLoginDate}" pattern="MM-dd-yyyy HH:mm:ss"/></span></c:if>
                    
        </td>
        <td align="right" width="35%" valign="top">
            <table cellpadding="0" cellspacing="0" width="100%" border="0">
                <tr height="10">
                    <td align="right"><div style="font-size:11; font-weight: bold">
                    <c:if test="${sessionScope.userInfo ==null}">
                    	<a href="${ctx}/login/login.do">Login</a>
                    </c:if>
                    <c:if test="${sessionScope.userInfo!=null}">
                    <a href="${ctx}/home/home.do">Home</a>
                    &#160;
                    |&#160;<a href="${ctx}/j_spring_security_logout">Logout</a>
                    </c:if>
                    </div>
                   
                         
                    </td>
                </tr>
                                         
            </table>
        </td>
    </tr>
</table>
</div>