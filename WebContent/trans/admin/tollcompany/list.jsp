<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Toll Company" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Toll Company" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Name"/></td>
			<td align="${left}"><select id="name" name="id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${tollcompanyname}" var="name">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['id'] == name.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${name.id}" ${selected}>${name.name}</option>
				</c:forEach>
			</select>
			</td>

			<%-- <td align="${left}" class="first"><primo:label code="City" /></td>
			<td align="${left}"><input name="city" type="text"
				value="${sessionScope.searchCriteria.searchMap.city}" /></td> --%>
		</tr>
		
		<%-- <tr>
			<td align="${left}" class="first"><primo:label code="Transfer Station"/></td>
			<td align="${left}"><select id="transferStation" name="transferStation.id">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${transferStation}" var="transferStation">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['transferStation.id'] == transferStation.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${transferStation.id}" ${selected}>${transferStation.name}</option>
				</c:forEach>
			</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Landfill Station"/></td>
			<td align="${left}"><select id="landfill" name="landfill.id">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${landfill}" var="landfill">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['landfill.id'] == landfill.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${landfill.id}" ${selected}>${landfill.name}</option>
				</c:forEach>
			</select></td>
		</tr>
		
		 --%>
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
	<primo:datatable urlContext="admin/tollcompany" deletable="true"
		editable="true" insertable="true" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Name" dataField="name" />
		<primo:textcolumn headerText="Company" dataField="company.name" />
		<primo:textcolumn headerText="Address Line1" dataField="address" />
		<primo:textcolumn headerText="Address Line2" dataField="address2" />
		<primo:textcolumn headerText="City" dataField="city" />
		<primo:textcolumn headerText="State" dataField="state.name" />
		<primo:textcolumn headerText="Zipcode" dataField="zipcode" />
		<primo:textcolumn headerText="Phone" dataField="phone" />
		<primo:textcolumn headerText="Fax" dataField="fax" />
	   
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


