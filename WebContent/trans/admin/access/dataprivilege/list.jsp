<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Data Access for ${roleName}" />
</h3>
<br />
<form:form name="dataAccessServiceForm" id="dataAccessServiceForm">
	<primo:datatable urlContext="admin/access/dataprivilege" deletable="true"
		editable="true" insertable="true" insertableParams="roleId=${roleId}" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Role" dataField="role.name" />
		<primo:textcolumn headerText="Screen Name" dataField="bo.objectName" />
		<primo:textcolumn headerText="Emp Cat Priv" dataField="empCatPrivNames" />
		<primo:textcolumn headerText="Has Editable" dataField="hasEditable" />
		<primo:textcolumn headerText="Has Deletable" dataField="hasDeletable" />
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


