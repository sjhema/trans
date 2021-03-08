<%@include file="/common/taglibs.jsp"%>

<h3>
	<primo:label code="Manage Loading Fee" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Loading Fee" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Valid From" />
			<td align="${left}"><input id="datepicker" name="validFromDate"
				type="text" onblur="return formatDate('datepicker');" style="min-width:150px; max-width:150px"
				value="${sessionScope.searchCriteria.searchMap.validFromDate}"/>
			</td>
			<td align="${left}" class="first"><primo:label code="Valid To" /></td>
			<td align="${left}"><input id="datepicker1" name="validToDate" style="min-width:150px; max-width:150px"
				type="text" onblur="formatDate('datepicker1');"
				value="${sessionScope.searchCriteria.searchMap.validToDate}"/>
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button" onclick="document.forms['searchForm'].submit();" value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br/>
<form:form name="serviceForm" id="serviceForm">
	<primo:datatable urlContext="admin/loadingfee" deletable="true"
		editable="true" exportPdf="true" exportXls="true"
		exportCsv="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Code" dataField="code" />
		<primo:textcolumn headerText="Rate" dataField="rate" dataFormat="#0.00" />
		<primo:textcolumn headerText="Valid From" dataField="validFrom" dataFormat="MM-dd-yyyy"/>
		<primo:textcolumn headerText="Valid To" dataField="validTo" dataFormat="MM-dd-yyyy"/>
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

<style>
.datagrid td {
	text-align: center;
}
</style> 
