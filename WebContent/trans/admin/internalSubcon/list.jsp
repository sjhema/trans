<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Internal Subcontractor" />
</h3>
<form:form action="search.do" method="get" name="searchInternalSubconMapping">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Manage Internal Subcontractor" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Driver Company" /></td>
			<td align="${left}">
				<select id="driverCompany" name="driverCompany" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
					<c:forEach items="${companies}" var="aCompany">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['driverCompany'] == aCompany.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCompany.id}"${selected}>${aCompany.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Billing Company" /></td>
			<td align="${left}">
				<select id="billingCompany" name="billingCompany" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
					<c:forEach items="${companies}" var="aCompany">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['billingCompany'] == aCompany.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aCompany.id}"${selected}>${aCompany.name}</option>
					</c:forEach>
				</select>
			</td>
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
			<td align="${left}" class="first"><primo:label code="Subcontractor" /></td>
			<td align="${left}">
				<select id="subcontractor" name="subcontractor" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
					<c:forEach items="${subcontractors}" var="aSubcontractor">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['subcontractor'] == aSubcontractor.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aSubcontractor.id}"${selected}>${aSubcontractor.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button" onclick="document.forms['searchInternalSubconMapping'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="internalSubconMappingServiceForm" id="internalSubconMappingServiceForm">
	<primo:datatable urlContext="admin/internalSubcon" deletable="true"
		editable="true" exportPdf="true" exportXls="true"
		exportCsv="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Driver Company" dataField="driverCompany.name" />
		<primo:textcolumn headerText="Billing Company" dataField="billingCompany.name" />
		<primo:textcolumn headerText="Origin" dataField="origin.name" />
		<primo:textcolumn headerText="Destination" dataField="destination.name" />
		<primo:textcolumn headerText="Subcontractor" dataField="subcontractor.name" />
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


