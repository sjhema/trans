<%@include file="/common/taglibs.jsp"%>

<h3>
	<primo:label code="Manage Equipment Lenders" />
</h3>
<form:form action="search.do" method="get" name="equipmentLenderSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Equipment Lenders" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Name"/></td>
			<td align="${left}">
				<select id="name" name="name" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${lenders}" var="aEquipmentLender">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['name'] == aEquipmentLender.name}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aEquipmentLender.name}"${selected}>${aEquipmentLender.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
	 	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['equipmentLenderSearchForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="equipmentLenderServiceForm" id="equipmentLenderServiceForm">
	<primo:datatable urlContext="admin/equipment/lender" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportCsv="true" exportXls="true">
		<primo:textcolumn headerText="Name" dataField="name" />
        <primo:textcolumn headerText="Address1" dataField="address1" />
        <primo:textcolumn headerText="Address2" dataField="address2"/>
        <primo:textcolumn headerText="City" dataField="city"/>
        <primo:textcolumn headerText="State" dataField="state.name" width="75px"/>
        <primo:textcolumn headerText="Zipcode" dataField="zipcode" width="75px"/>
        <primo:textcolumn headerText="Phone" dataField="phone" width="75px"/>
        <primo:textcolumn headerText="Fax" dataField="fax" width="75px"/>
    </primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


