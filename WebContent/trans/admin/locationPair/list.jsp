<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Location Pair" />
</h3>
<form:form action="search.do" method="get" name="searchLocationPairForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Manage Location Pair" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Origin" /></td>
			<td align="${left}">
				<select id="origin" name="origin" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
					<c:forEach items="${origins}" var="anOrigin">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['origin'] == anOrigin.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrigin.id}"${selected}>${anOrigin.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Destination" /></td>
			<td align="${left}">
				<select id="destination" name="destination" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
					<c:forEach items="${destinations}" var="aDestination">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['destination'] == aDestination.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDestination.id}"${selected}>${aDestination.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button" onclick="document.forms['searchLocationPairForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="locationPairServiceForm" id="locationPairServiceForm">
	<primo:datatable urlContext="admin/locationPair" deletable="true"
		editable="true" exportPdf="true" exportXls="true"
		exportCsv="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Origin" dataField="origin.name" />
		<primo:textcolumn headerText="Destination" dataField="destination.name" />
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


