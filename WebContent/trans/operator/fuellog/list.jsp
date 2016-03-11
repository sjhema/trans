<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Fuel Log" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b>Search Fuel Log</b>
			</td>
		</tr>
		<!-- Fuel Card Number & Driver Name , for searchCriteria -->
		<tr>
			<%-- <td align="${left}" class="first"><label>Fuel Card Number</label>
			</td>
			<td align="${left}"><input name="fuelCardNumber" type="text"
				value="${sessionScope.searchCriteria.searchMap.fuelCardNumber}" />
			</td> --%>	
			<td align="${left}" class="first"><primo:label code="Fuel Vendor"/></td>
			<td align="${left}"><select id="fuelvendor" name="fuelvendor.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${fuelvendors}" var="vendors">
				<c:set var="selected" value=""/>
				<c:if test="${sessionScope.searchCriteria.searchMap['vendors.id'] == vendors.id}">
					<c:set var="selected" value="selected"/>
				</c:if>
					<option value="${vendors.id}" ${selected}>${vendors.name}</option>
				</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td align="${left}" class="first">
			<primo:label code="Driver"/>
			</td>
			<td align="${left}">
			<select id="driversid" name="driversid.fullName" style="min-width:154px; max-width:154px">
			<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${drivernames}" var="drivers">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['drivers.fullName'] == drivers.fullName}">
					<c:set var="selected" value="selected"/>
					</c:if>
				<option value="${drivers.fullName}" ${selected}>${drivers.fullName}</option>
				</c:forEach>
			</select>
			</td>
			
			<td align="${left}" class="first"><primo:label code="Subcontractor"/></td>
			<td align="${left}"><select id="subContractor" name="subContractor.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${subcontractors}" var="aSubcontractor">
				<c:set var="selected" value=""/>
				<c:if test="${sessionScope.searchCriteria.searchMap['subContractor.id'] == aSubcontractor.id}">
					<c:set var="selected" value="selected"/>
				</c:if>
					<option value="${aSubcontractor.id}" ${selected}>${aSubcontractor.name}</option>
				</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();" value="Search" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<div style="width: 100%; margin: 0px auto;">
	<form:form action="delete.do" id="deleteForm" name="deleteForm">
		<primo:datatable urlContext="operator/fuellog" deletable="true"
			editable="true" insertable="true" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="true" searcheable="false">
			<primo:textcolumn headerText="Transaction Date" dataField="transactiondate" dataFormat="MM-dd-yyyy"/>
			<primo:textcolumn headerText="Invoiced Date" dataField="invoiceDate" dataFormat="MM-dd-yyyy"/>
			<%-- <primo:textcolumn headerText="Fuel Card No" dataField="fuelCardNumber" /> --%>
			<primo:textcolumn headerText="Fuel Card No" dataField="fuelcard.fuelcardNum" />
			<primo:textcolumn headerText="Vendor" dataField="fuelvendor.name"/>
			<%-- <primo:textcolumn headerText="Driver First Name" dataField="driverFname.firstName"/>
			<primo:textcolumn headerText="Driver last Name" dataField="driverLname.lastName"/> --%>
			<primo:textcolumn headerText="Subcontractor" dataField="subContractor.name"/>
			<primo:textcolumn headerText="Driver Name" dataField="driversid.fullName"/>
			<primo:textcolumn headerText="company" dataField="company.name"/>
			<primo:textcolumn headerText="Unit#" dataField="unit.unit" type="int"/>
			<primo:textcolumn headerText="Terminal" dataField="terminal.name"/>
		
		     <%-- <primo:textcolumn headerText="State" dataField="state.name" /> --%>
			
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
	</form:form>
</div>