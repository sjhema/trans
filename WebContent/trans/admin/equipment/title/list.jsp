<%@include file="/common/taglibs.jsp"%>

<h3>
	<primo:label code="Manage Vehicle Title" />
</h3>
<form:form action="search.do" method="get" name="vehicleTitleSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Vehicle Title" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Vehicle"/></td>
			<td align="${left}">
				<select id="vehicle.unit" name="vehicle.unit" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${vehicles}" var="item">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['vehicle.unit'] ne ''}">
							<c:if test="${sessionScope.searchCriteria.searchMap['vehicle.unit'] == item.unit}">
								<c:set var="selected" value="selected" />
							</c:if>
						</c:if>
						<option value="${item.unit}"${selected}>${item.unit}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Owner"/></td>
			<td align="${left}">
				<select id="owner.id" name="owner.id" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${owners}" var="aOwner">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['owner.id'] == aOwner.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${aOwner.id}" ${selected}>${aOwner.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
	 	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['vehicleTitleSearchForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="vehicleTitleServiceForm" id="vehicleTitleServiceForm">
	<primo:datatable urlContext="admin/equipment/title" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportCsv="true" exportXls="true">
		<primo:textcolumn headerText="Unit" dataField="vehicle.unitNum" width="35px"/>
		<primo:textcolumn headerText="Owner" dataField="owner.name" />
		<primo:textcolumn headerText="Holds Title" dataField="holdsTitle" width="35px"/>
		<primo:textcolumn headerText="Title" dataField="title"/>
		<primo:textcolumn headerText="Title Date" dataField="titleDate" dataFormat="MM-dd-yyyy" width="95px"/>
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


