

<%@ include file="/common/taglibs.jsp"%>
<br/>
<h3><primo:label code="Leave Balance Update History"/></h3>

<form:form name="delete.do" id="serviceForm">
	<primo:datatable urlContext="hr/updateleavebalance" deletable="true"
		editable="true" insertable="true" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		
		<primo:datecolumn headerText="&nbsp;Run&nbsp;Date&nbsp;" dataField="updateRunDate" dataFormat="MM-dd-yyyy"/>
		<primo:textcolumn headerText="Company" dataField="company.name" />
		<primo:textcolumn headerText="Terminal" dataField="terminal.name" />
		<primo:textcolumn headerText="Category" dataField="category.name" />
		<primo:textcolumn headerText="Leave Type" dataField="leavetype.name" />
		<primo:textcolumn headerText="Days Earned" dataField="dayearned" />
		<primo:textcolumn headerText="Hours Earned" dataField="hoursearned" />
		<primo:textcolumn headerText="Experiance Required in days" dataField="experienceindays" />
		<primo:textcolumn headerText="Experiance Required in Year From" dataField="experienceinyears" />
		<primo:textcolumn headerText="Experiance Required in Year To" dataField="experienceinyearsTo" />
		
		
		<primo:staticdatacolumn headerText="Annual Accrual" dataField="annualoraccrual" dataType="ANNUAL_ ACCRUAL" />
		<primo:staticdatacolumn headerText="Leave Qualifier" dataField="leaveQualifier" dataType="LeaveQualifier"/>
		<primo:staticdatacolumn headerText="Status" dataField="status" dataType="STATUS" />
		<primo:anchorcolumn headerText="updated Record" dataField="noOfRecord" linkUrl="/hr/updateleavebalance/showlist.do?id={ptodId}" target="_blank"/>
		<%-- <primo:textcolumn headerText="updated Record" dataField="noOfRecord" /> --%>
		
		
			</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>
<%-- <c:if test="${not empty ptodlists}">
	<c:forEach var="ptodlist" items="${ptodlists}">
		<div style="font:oblique; color: #686868;";>
			<c:out value="${ptodlist}" escapeXml="false" />
			<br />
		</div>
	</c:forEach>

	<c:remove var="ptodlist" />
</c:if> --%>