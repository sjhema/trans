<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Updated Leave Balance"/>
</h3>
<form:form name="delete.do" id="serviceForm">
	<primo:datatable urlContext="" deletable="false"
		editable="false" insertable="false" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Company" dataField="company.name"/>
		<primo:textcolumn headerText="Terminal" dataField="terminal.name"/>	
		<primo:textcolumn headerText="Category" dataField="empcategory.name" />
		<primo:textcolumn headerText="Name" dataField="empname.fullName" />
		<primo:textcolumn headerText="Leave Type" dataField="leavetype.name" />
		<primo:textcolumn headerText="Days Earned" dataField="dayssbalance" />
		<primo:textcolumn headerText="Days Used" dataField="daysused" />
		<primo:textcolumn headerText="Days Remaining" dataField="daysremain" />
		<primo:textcolumn headerText="Hours Earned" dataField="hoursbalance" />
		<primo:textcolumn headerText="Hours Used" dataField="hoursused" />
		<primo:textcolumn headerText="Hours Remaining" dataField="hourremain" />
		<primo:textcolumn headerText="Note" dataField="note" />
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>