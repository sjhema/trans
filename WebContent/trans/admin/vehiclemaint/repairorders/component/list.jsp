<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Repair Order Component" />
</h3>
<form:form action="search.do" method="get" name="repairOrderComponentSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Repair Order Components" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Component"/></td>
			<td align="${left}">
				<select id="component" name="component" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${components}" var="item">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['component'] == item.component}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.component}"${selected}>${item.component}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
	 	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['repairOrderComponentSearchForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="repairOrderComponentServiceForm" id="repairOrderComponentServiceForm">
	<primo:datatable urlContext="admin/vehiclemaint/repairorders/component" deletable="true"
		editable="true" insertable="true" exportPdf="true" exportXls="true"
		exportCsv="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Component" dataField="component" />
	    <primo:textcolumn headerText="Description" dataField="description"/>
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


