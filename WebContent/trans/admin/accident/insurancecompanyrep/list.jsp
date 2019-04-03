<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Insurance Company Rep" />
</h3>
<form:form action="search.do" method="get" name="insuranceCompanyRepSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Insurance Company Rep" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Insurance Company"/></td>
			<td align="${left}">
				<select id="insuranceCompany" name="insuranceCompany" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${insuranceCompanies}" var="anInsuranceCompany">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['insuranceCompany'] == anInsuranceCompany.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${anInsuranceCompany.id}" ${selected}>${anInsuranceCompany.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
	 	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['insuranceCompanyRepSearchForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="insuranceCompanyRepServiceForm" id="insuranceCompanyRepServiceForm">
	<primo:datatable urlContext="admin/accident/insurancecompanyrep" deletable="false"
		editable="true" insertable="true" exportPdf="true" exportXls="true"
		exportCsv="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Insurance Company" dataField="insuranceCompany.name" />
	    <primo:textcolumn headerText="Name" dataField="name"/>
	    <primo:textcolumn headerText="Phone" dataField="phone"/>
	    <primo:textcolumn headerText="Email" dataField="email"/>
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


