<%@include file="/common/taglibs.jsp"%>

<h3>
	<primo:label code="Manage Citations" />
</h3>
<form:form action="search.do" method="get" name="citationSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Citations" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
			<td align="${left}">
				<select id="company" name="company" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${companies}" var="aCompany">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['company'] == aCompany.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aCompany.id}" ${selected}>${aCompany.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Driver"/></td>
			<td align="${left}">
				<select id="driver" name="driver" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${driverNames}" var="aDriverName">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['driver'] == aDriverName}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aDriverName}" ${selected}>${aDriverName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Citation Date From"/></td>
			<td align="${left}">
				<input id="datepicker" name="citationDateFrom" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.citationDateFrom}" /> 
			</td>
			<td align="${left}" class="first"><primo:label code="Citation Date To"/></td>
			<td align="${left}">
				<input id="datepicker1" name="citationDateTo" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.citationDateTo}" /> 
			</td>
		</tr>
	 	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['citationSearchForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="citationServiceForm" id="citationServiceForm">
	<primo:datatable urlContext="admin/citation/citationmaint" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportCsv="true" exportXls="true">
	    <primo:datecolumn headerText="Date" dataField="incidentDate" dataFormat="MM-dd-yyyy" width="85px"/>
		<primo:textcolumn headerText="Company" dataField="company.name" width="145px"/>
		<primo:textcolumn headerText="Driver" dataField="driver.fullName" width="210px"/>
		<primo:textcolumn headerText="Truck" dataField="truck.unitNum" width="35px"/>
		<primo:textcolumn headerText="Trailer" dataField="trailer.unitNum" width="35px"/>
		<primo:textcolumn headerText="Citation No" dataField="citationNo" width="150px"/>
		<primo:textcolumn headerText="Out Of Service" dataField="outOfService" width="20px"/>
		<primo:textcolumn headerText="Violation Type" dataField="violationType" />
    </primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


