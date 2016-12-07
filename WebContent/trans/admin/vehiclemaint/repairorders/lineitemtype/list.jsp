<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Repair Order Line Item Type" />
</h3>
<form:form action="search.do" method="get" name="repairOrderLineItemTypeSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Repair Order Line Item Types" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Type"/></td>
			<td align="${left}">
				<select id="type" name="type" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${types}" var="item">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['type'] == item.type}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.type}"${selected}>${item.type}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
	 	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['repairOrderLineItemTypeSearchForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="repairOrderLineItemTypeServiceForm" id="repairOrderLineItemTypeServiceForm">
	<primo:datatable urlContext="admin/vehiclemaint/repairorders/lineitemtype" deletable="true"
		editable="true" insertable="true" exportPdf="true" exportXls="true"
		exportCsv="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Type" dataField="type" />
	    <primo:textcolumn headerText="Description" dataField="description"/>
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


