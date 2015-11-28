<%@include file="/common/taglibs.jsp"%>
<h3>Employee Anniversary In Next 30 Days</h3>
<div style="width:100%; margin:0px auto;"><form:form name="delete.do" id="customerForm">
		<primo:datatable urlContext="" deletable="false" editable="false" insertable="false" exportPdf="false" exportXls="false" exportCsv="false"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
			<primo:textcolumn headerText="Category" dataField="catagory.name" />
			<primo:textcolumn headerText="Last Name" dataField="lastName" />
			<primo:textcolumn headerText="First Name" dataField="firstName" />
			<primo:datecolumn headerText="Date Hired" dataField="dateHired" dataFormat="MM-dd-yyyy"/>
			<primo:datecolumn headerText="Date Rehired" dataField="dateReHired" dataFormat="MM-dd-yyyy"/>
			<primo:datecolumn headerText="Probation Start Date" dataField="dateProbationStart" dataFormat="MM-dd-yyyy"/>
			<primo:datecolumn headerText="Probation End Date" dataField="dateProbationEnd" dataFormat="MM-dd-yyyy"/>
			<primo:datecolumn headerText="Anniversary Date" dataField="anniversaryDate" dataFormat="MM-dd-yyyy"/>
			<primo:datecolumn headerText="Next Anniversary Date" dataField="nextAnniversaryDate" dataFormat="MM-dd-yyyy"/>
			<primo:textcolumn headerText="Probation Period" dataField="probationDays" />			                                                             
			<primo:textcolumn headerText="Company" dataField="company.name"/>
			<primo:textcolumn headerText="Terminal" dataField="terminal.name"/>	
			<primo:datecolumn headerText="Date Terminated" dataField="dateTerminated" dataFormat="MM-dd-yyyy"/>	
			<primo:staticdatacolumn headerText="Complete Status" dataField="status" dataType="STATUS" />
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
	</form:form>
</div>