<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Vehicle Without GPS" />
</h3>
<form:form action="search.do" method="get" name="searchNoPSForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Manage Vehicle Without GPS" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Unit#" /></td>
			<td align="${left}">
				<select id="vehicle.id" name="vehicle.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
					<c:forEach items="${vehicles}" var="aVehicle">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['vehicle.id'] == aVehicle.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aVehicle.id}"${selected}>${aVehicle.unit}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button" onclick="document.forms['searchNoPSForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="noGPSServiceForm" id="noGPSServiceForm">
	<primo:datatable urlContext="admin/nogps" deletable="true"
		editable="true" exportPdf="true" exportXls="true"
		exportCsv="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Unit" dataField="vehicle.unitNum" />
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


