<%@include file="/common/taglibs.jsp"%>

<form:form action="search.do" method="get" name="equipmentReportSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Equipment Report" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Vehicle"/></td>
			<td align="${left}">
				<select id="vehicle" name="vehicle" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${vehicles}" var="item">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['vehicle'] ne ''}">
							<c:if test="${sessionScope.searchCriteria.searchMap['vehicle'] == item.unit}">
								<c:set var="selected" value="selected" />
							</c:if>
						</c:if>
						<option value="${item.unit}"${selected}>${item.unit}</option>
					</c:forEach>
				</select>
			</td>
	   	</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Loan No"/></td>
			<td align="${left}">
				<select id="loanNo" name="loanNo" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${vehicleLoans}" var="aVehicleLoan">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['loanNo'] == aVehicleLoan.loanNo}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aVehicleLoan.loanNo}"${selected}>${aVehicleLoan.loanNo}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
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
			<td align="${left}" class="first"><primo:label code="Lender"/></td>
			<td align="${left}">
				<select id="lender" name="lender" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${lenders}" var="aLender">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['lender'] == aLender.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${aLender.id}" ${selected}>${aLender.name}</option>
					</c:forEach>
				</select>
			</td>
	   	</tr>
	 	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['equipmentReportSearchForm'].submit();"
					value="<primo:label code="Preview"/>" />
			</td>
		</tr>
	</table>
</form:form>



