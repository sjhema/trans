<%@include file="/common/taglibs.jsp"%>
<%@page
	import="org.springframework.security.core.AuthenticationException"%>
<%@page
	import="org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter"%>
	
<form:form action="${ctx}/j_spring_security_check">`
	<table width="40%" height="300px" align="center">
	<tr>
	<td align="${left}" style="border:1px solid #d8d8d8; padding:5px; width: 50%">
	<div style="font-size: 20px; font-weight: bold; color: #003366;text-align:center"">Welcome to &nbsp;<b style="color: #5C91D5;"></b>ERP System</div>
	<br/>
	<%
	if (session.getAttribute(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY) != null) {
	%>
	<div class="errorMessage" align="center">
	 <br />
	 <%if(session.getAttribute("error")!=null)out.println(session.getAttribute("error"));session.removeAttribute("error");%>
		<!-- Reason: <%//=session.getAttribute(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY)%>-->
	</div>
	<br/>
	<%		
	}
	%>
	<table id="form-table" cellspacing="1" cellpadding="5">
	
		<tr class="table-heading">
			<td align="${left}" align="${left}" colspan="2"><b><primo:label code="User Log In"/></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="User ID"/></td>
			<td align="${left}" align="${left}"><input name="j_username" size="30" type="text" id="userid"></input>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Password"/></td>
			<td align="${left}" align="${left}"><input name="j_password" size="30" type="password"></input>
			</td>
		</tr>
		<tr>
		    <td align="${left}">&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="${left}" align="${left}"><input value="<primo:label code='Submit'/>"	type="submit">&nbsp;<input value="<primo:label code='Cancel'/>" type="reset">
			</td>
		</tr>
	</table>
	</td>
	</tr>
	</table>
</form:form>