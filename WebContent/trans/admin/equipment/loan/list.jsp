<%@include file="/common/taglibs.jsp"%>

<h3>
	<primo:label code="Manage Vehicle Loan" />
</h3>
<form:form action="search.do" method="get" name="vehicleLoanSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Vehicle Loan" /></b></td>
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
			<td align="${left}" class="first"><primo:label code="Company"/></td>
			<td align="${left}">
				<select id="company.id" name="company.id" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${companies}" var="aCompany">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['company.id'] == aCompany.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${aCompany.id}" ${selected}>${aCompany.name}</option>
					</c:forEach>
				</select>
			</td>
	   	</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Loan No"/></td>
			<td align="${left}">
				<select id="loanNo" name="loanNo" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${vehicleLoans}" var="aVehicleLoanNo">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['loanNo'] == aVehicleLoanNo}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aVehicleLoanNo}"${selected}>${aVehicleLoanNo}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Lender"/></td>
			<td align="${left}">
				<select id="lender.id" name="lender.id" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${lenders}" var="aLender">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['lender.id'] == aLender.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${aLender.id}" ${selected}>${aLender.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<!--
		<tr>
			<td align="${left}" class="first"><primo:label code="Loan Start Date"/></td>
			<td align="${left}">
				<input id="datepicker" name="loanStartDate" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.loanStartDate}" /> 
			</td>
			<td align="${left}" class="first"><primo:label code="Loan End Date"/></td>
			<td align="${left}">
				<input id="datepicker1" name="loanEndDate" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.loanEndDate}" /> 
			</td>
		</tr>
		-->
	 	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['vehicleLoanSearchForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="vehicleLoanServiceForm" id="vehicleLoanServiceForm">
	<primo:datatable urlContext="admin/equipment/loan" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" exportCsv="true">
		<primo:textcolumn headerText="Company" dataField="vehicle.owner.name" />
		<primo:textcolumn headerText="Unit" dataField="vehicle.unitNum" width="35px"/>
		<primo:textcolumn headerText="VIN" dataField="vehicle.vinNumber" />
		<primo:textcolumn headerText="Year" dataField="vehicle.year" width="35px"/>
		<primo:textcolumn headerText="Make" dataField="vehicle.make" width="35px"/>
		<primo:textcolumn headerText="Model" dataField="vehicle.model" width="35px"/>
        <primo:textcolumn headerText="Lender" dataField="lender.name" />
        <primo:textcolumn headerText="Loan No" dataField="loanNo"/>
        <primo:textcolumn headerText="Payment Amt" dataField="paymentAmount" width="75px"/>
        <primo:textcolumn headerText="Due Date" dataField="paymentDueDom" width="75px"/>
        <primo:datecolumn headerText="Start Date" dataField="startDate" dataFormat="MM-dd-yyyy" width="95px"/>
        <primo:datecolumn headerText="End Date" dataField="endDate" dataFormat="MM-dd-yyyy" width="95px"/>
       	<primo:textcolumn headerText="Interest Rate" dataField="interestRate" width="75px" dataFormat="##.000'%'"/>
        <primo:textcolumn headerText="No Of Payments" dataField="noOfPayments" width="75px"/>
        <primo:textcolumn headerText="Payments Left" dataField="paymentsLeft" width="75px"/>
        <primo:textcolumn headerText="Notes" dataField="description" width="400px"/>
    </primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


