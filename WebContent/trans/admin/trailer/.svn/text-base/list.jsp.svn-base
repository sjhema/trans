<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Trailers" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b>Search Trailer</b></td>
		</tr>
		<tr>
			<td align="${left}" class="first">Number</td>
			<td align="${left}"><input name="number" type="text"
				value="${sessionScope.searchCriteria.searchMap.number}" /></td>
			<td align="${left}" class="first">Type</td>
			<td align="${left}"><input name="type" type="text"
				value="${sessionScope.searchCriteria.searchMap.type}" /></td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();" value="Search" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<div style="width: 100%; margin: 0px auto;">
	<form:form name="delete.do" id="customerForm">
		<primo:datatable urlContext="admin/trailer" deletable="true"
			editable="true" insertable="true" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false">
			<primo:textcolumn headerText="Number" dataField="number" />
			<primo:textcolumn headerText="Type" dataField="type" />
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
	</form:form>
</div>