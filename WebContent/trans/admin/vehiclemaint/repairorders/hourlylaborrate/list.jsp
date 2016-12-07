<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Repair Order Hourly Labor Rate" />
</h3>
<form:form action="search.do" method="get" name="repairOrderHourlyLaborRateSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Repair Order Hourly Labor Rate" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Effective Start Date"/></td>
			<td align="${left}">
				<input id="datepicker" name="effectiveStartDate" style="min-width:150px; max-width:150px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.effectiveStartDate}" /> 
			</td>
			<td align="${left}" class="first"><primo:label code="Effective End Date"/></td>
			<td align="${left}">
				<input id="datepicker1" name="effectiveEndDate" style="min-width:150px; max-width:150px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.effectiveEndDate}" /> 
			</td>
		</tr>
	 	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['repairOrderHourlyLaborRateSearchForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="repairOrderHourlyLaborRateServiceForm" id="repairOrderHourlyLaborRateServiceForm">
	<primo:datatable urlContext="admin/vehiclemaint/repairorders/hourlylaborrate" deletable="true"
		editable="true" insertable="true" exportPdf="true" exportXls="true"
		exportCsv="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Labor Rate" dataField="laborRate" />
		<primo:datecolumn headerText="Effective Start Date" dataField="effectiveStartDate" dataFormat="MM-dd-yyyy"/>
		<primo:datecolumn headerText="Effective End Date" dataField="effectiveEndDate" dataFormat="MM-dd-yyyy"/>
		<primo:textcolumn headerText="Description" dataField="description" />
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


