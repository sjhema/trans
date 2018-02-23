<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage WM Locations" />
</h3>
<form:form action="search.do" method="get" name="searchWMLocationForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Manage WM Locations" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Name" /></td>
			<td align="${left}">
				<select id="location" name="location" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
					<c:forEach items="${locations}" var="aLocation">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['location'] == aLocation.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aLocation.id}"${selected}>${aLocation.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Type" /></td>
			<td align="${left}">
				<select id="location.type" name="location.type" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
					<c:forEach items="${locationTypes}" var="aLocationType">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['location.type'] == aLocationType.dataValue}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aLocationType.dataValue}"${selected}>${aLocationType.dataText}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button" onclick="document.forms['searchWMLocationForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="wmLocationServiceForm" id="wmLocationServiceForm">
	<primo:datatable urlContext="admin/wmLocation" deletable="true"
		editable="true" exportPdf="true" exportXls="true"
		exportCsv="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Location" dataField="location.name" />
		<primo:staticdatacolumn dataType="LOCATION_TYPE" headerText="Type" dataField="location.type" />
		<primo:textcolumn headerText="WM Location Name" dataField="wmLocationName" />
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


