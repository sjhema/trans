<%@include file="/common/taglibs.jsp"%>
<%@page import="com.primovision.lutransport.model.BusinessObject,java.util.*,com.primovision.lutransport.model.RolePrivilege"%>

<%
	BusinessObject item = (BusinessObject)request.getAttribute("item");
	List<RolePrivilege> rolePrivilegeList = (ArrayList<RolePrivilege>) request.getSession().getAttribute("rolePrivileges");
	String checked =null;

%>
<%
  	for(int i=0; i<rolePrivilegeList.size();i++)
  	{
  		if(rolePrivilegeList.get(i).getBusinessObject().getId().equals(item.getId())) {
  			checked = "checked";
  			break;
  		} else 
  			checked = "";
  	}
  	%>
<li>
  <input type="checkbox" name="item" value="${item.id}" <%=checked%>/>${item.objectName}
  <c:if test="${not empty item.children}">
  <ul>
  	<c:forEach var="subItem" items="${item.children}">
	  	<c:set var="item" scope="request" value="${subItem}"/>
	  		<jsp:include page="displaymenu.jsp"/>
	  	<c:set var="item" value=""/>
    </c:forEach>
  </ul>
</c:if>
</li>