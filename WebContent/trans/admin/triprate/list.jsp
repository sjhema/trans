<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Trip Rate" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Location" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Transfer Station" /></td>
			<td align="${left}"><select id="transferStation" name="transferStation.id">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${transferStations}" var="transferStation">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['transferStation.id'] == transferStation.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${transferStation.id}"${selected}>${transferStation.name}</option>
					</c:forEach>
			</select></td>
			<td align="${left}" class="first"><primo:label code="LandFill" /></td>
			<td align="${left}"><select id="landfill" name="landfill.id">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${landfills}" var="landfill">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['landfill.id'] == landfill.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${landfill.id}"${selected}>${landfill.name}</option>
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
	<primo:datatable urlContext="admin/triprate" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Transfer Station" dataField="transferStation.name" />
		<primo:textcolumn headerText="LandFill" dataField="landfill.name" />
		<primo:textcolumn headerText="Rate" dataField="rate" dataFormat="#0.00"/>
	</primo:datatable>
</form:form>


