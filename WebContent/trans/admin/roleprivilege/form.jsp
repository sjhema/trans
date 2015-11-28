<%@include file="/common/taglibs.jsp"%>
<%@page import="com.primovision.lutransport.model.BusinessObject,java.util.*,com.primovision.lutransport.model.menu.*,com.primovision.lutransport.model.RolePrivilege"%>
<%
    MenuTree menuTree = (MenuTree) request.getAttribute("fullMenuTree");
    Long boId = (Long) session.getAttribute("boid");
    if (boId==null)
        boId=1L;
    java.util.List<BusinessObject> businessObjects = MenuHelper.getMenuForParent(menuTree, boId);
%>
<%@page import="java.util.ArrayList"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ctx}/js/jquery.collapsibleCheckboxTree.js"></script>
<link rel="stylesheet" href="${ctx}/css/jquery.collapsibleCheckboxTree.css" type="text/css" />
<script type="text/javascript">
jQuery(document).ready(function(){
		$('ul#example').collapsibleCheckboxTree({
		checkParents : true, // When checking a box, all parents are checked (Default: true)
		checkChildren : false, // When checking a box, all children are checked (Default: false)
		uncheckChildren : false, // When unchecking a box, all children are unchecked (Default: true)
		initialState : 'default' // Options - 'expand' (fully expanded), 'collapse' (fully collapsed) or default
	});
});
</script>
</head>
<form:form action="save.do" method="post" commandName="modelObject">
<table id="form-table" width="100%" cellspacing="5" cellpadding="5">
<tr><td>
  <ul id="example">
  <li>Role Name : <%=request.getSession().getAttribute("rolename")%></li><br/>
  <c:forEach var="itemObj" items="<%=businessObjects%>">
  	<c:set var="item" value="${itemObj}" scope="request"/>
  		<jsp:include page="displaymenu.jsp"/>
  	<c:set var="item" value="" scope="request"/>	
  </c:forEach>
  </ul></td></tr>
  <tr>
  <td align="${left}"><input type="submit" value="submit"></td></tr>
  </table>
</form:form>

