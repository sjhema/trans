<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Trip Sheet Locations" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Trip Sheet Location" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Name" /></td>
			<td align="${left}"><select id="location" name="name" style="min-width:154px; max-width:154px">
					<option value="">------
					<primo:label code="Please Select" />
					------
					</option>
					<c:forEach items="${locations}" var="aLocation">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['name'] == aLocation}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aLocation}"${selected}>${aLocation}</option>
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
	<primo:datatable urlContext="admin/tripsheetlocation" deletable="true"
		editable="true" exportPdf="true" exportXls="true"
		exportCsv="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Company" dataField="driverCompany.name" />
		<primo:textcolumn headerText="Terminal" dataField="terminal.name" />
		<primo:textcolumn headerText="Name" dataField="name" />		
		<primo:staticdatacolumn dataType="LOCATION_TYPE" headerText="Type" dataField="type" />
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


