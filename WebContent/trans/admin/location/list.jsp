<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Locations" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Location" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Name" /></td>
			<td align="${left}"><select id="location" name="id" style="min-width:154px; max-width:154px">
					<option value="">------
					<primo:label code="Please Select" />
					------
					</option>
					<c:forEach items="${locations}" var="location">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['name'] ==location.name}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${location.id}"${selected}>${location.name}</option>
					</c:forEach>
			</select></td>
			
			
			<%-- <td align="${left}" class="first"><primo:label code="Name" /></td>
			<td align="${left}"><input name="name" type="text"
				value="${sessionScope.searchCriteria.searchMap.name}" /></td> --%>

			<td align="${left}" class="first"><primo:label code="Type" /></td>
			<td align="${left}"><select id="type" name="type" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${locationTypes}" var="locationType">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['type'] == locationType.dataValue}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${locationType.dataValue}"${selected}>${locationType.dataText}</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();"
				value="<primo:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="delete.do" id="serviceForm">
	<primo:datatable urlContext="admin/location" deletable="true"
		editable="true" exportPdf="true" exportXls="true"
		exportCsv="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Name" dataField="name" />
		<primo:textcolumn headerText="Long Name" dataField="longName" />
		<primo:textcolumn headerText="Hauling Name" dataField="haulingName" />
		<primo:textcolumn headerText="Code" dataField="code" />
		<primo:staticdatacolumn dataType="LOCATION_TYPE" headerText="Type" dataField="type" />
		<primo:staticdatacolumn headerText="Status" dataField="status" dataType="STATUS" />
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


