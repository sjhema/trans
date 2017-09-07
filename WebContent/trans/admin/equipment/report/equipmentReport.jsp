<%@include file="/common/taglibs.jsp"%>

<form:form action="search.do" method="get" name="equipmentReportSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Equipment Report" /></b></td>
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
	   	</tr>
	   	<tr>
	   		<td align="${left}" class="first"><primo:label code="Unit#"/></td>
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
	   		<td align="${left}" class="first"><primo:label code="Include Sold Vehicles"/></td>
			<td align="${left}">
				<select id="includeSoldVehicle" name="includeSoldVehicle" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<option value="Yes">Yes</option>
					<option value="No">No</option>
				</select>
			</td>
	   	</tr>
		<!--
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
			<td align="${left}" class="first"><primo:label code="Title Owner"/></td>
			<td align="${left}">
				<select id="titleOwner" name="titleOwner" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${owners}" var="aOwner">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['titleOwner'] == aOwner.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aOwner.id}" ${selected}>${aOwner.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Registered Owner"/></td>
			<td align="${left}">
				<select id="registeredOwner" name="registeredOwner" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${owners}" var="aOwner">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['registeredOwner'] == aOwner.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aOwner.id}" ${selected}>${aOwner.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Title"/></td>
			<td align="${left}">
				<select id="title" name="title" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${titles}" var="aTitle">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['title'] == aTitle.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aTitle.id}" ${selected}>${aTitle.title}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Holds Title"/></td>
			<td align="${left}">
				<select id="holdsTitle" name="holdsTitle" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<option value="Yes">Yes</option>
					<option value="No">No</option>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Buyer"/></td>
			<td align="${left}">
				<select id="buyer" name="buyer" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${buyers}" var="aBuyer">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['buyer'] == aBuyer.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aBuyer.id}"${selected}>${aBuyer.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		-->
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



