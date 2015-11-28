<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Pay Roll Rate" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Pay Roll Rate" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="state"><primo:label code="Orign" /></td>
			<td align="${left}"><select id="locations" name="origin.id">
					<option value="">------<primo:label code="Please Select" />------</option>
					<c:forEach items="${origins}" var="locations">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['origin.id'] == locations.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${locations.id}"${selected}>${locations.name}</option>
					</c:forEach>
			</select></td>				
			<td align="${left}" class="state"><primo:label code="Destination" /></td>
			<td align="${left}"><select id="destination" name="destination.id">
						<option value="">------<primo:label code="Please Select" />------</option>
						<c:forEach items="${destinations}" var="locations">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['destination.id'] == locations.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${locations.id}"${selected}>${locations.name}</option>
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
		<primo:datatable urlContext="admin/payrollrate" deletable="true" editable="true" insertable="true"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
			<primo:textcolumn headerText="Orign" dataField="origin.name" />
			<primo:textcolumn headerText="Destination" dataField="destination.name" />
			<primo:textcolumn headerText="Terminal" dataField="state.name" />
			<primo:textcolumn headerText="Rate" dataField="rate" dataFormat="#0.00" />
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

