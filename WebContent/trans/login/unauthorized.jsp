<%@include file="/common/taglibs.jsp"%>
<%@page
	import="org.springframework.security.core.AuthenticationException"%>
<%@page
	import="org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter"%>
	<table width="100%">
	<tr><td align="${left}" class="table-head"><primo:label code="Access Denied"/></td></tr>
</table>
<table width="100%" height="300px">
	<tr>
		<td align="${left}" width="50%" style="border: 1px solid #d8d8d8; text-align: center">
		<primo:label code="You are not authorized to view this page"/>
		</td>
	</tr>
</table>