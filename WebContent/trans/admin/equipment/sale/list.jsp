<%@include file="/common/taglibs.jsp"%>

<h3>
	<primo:label code="Manage Vehicle Sale" />
</h3>
<form:form action="search.do" method="get" name="vehicleSaleSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Vehicle Sale" /></b></td>
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
		<tr>
			<td align="${left}" class="first"><primo:label code="Sold Date From"/></td>
			<td align="${left}">
				<input id="datepicker" name="soldDateFrom" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.loanStartDate}" /> 
			</td>
			<td align="${left}" class="first"><primo:label code="Sold Date To"/></td>
			<td align="${left}">
				<input id="datepicker1" name="soldDateTo" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.loanEndDate}" /> 
			</td>
		</tr>
	 	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['vehicleSaleSearchForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="vehicleSaleServiceForm" id="vehicleSaleServiceForm">
	<primo:datatable urlContext="admin/equipment/sale" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportCsv="true" exportXls="true">
		<primo:textcolumn headerText="Unit" dataField="vehicle.unitNum" width="35px"/>
		<primo:textcolumn headerText="Company" dataField="vehicle.owner.name" />
		<primo:textcolumn headerText="VIN" dataField="vehicle.vinNumber" width="200px"/>
		<primo:textcolumn headerText="Year" dataField="vehicle.year" width="35px"/>
		<primo:textcolumn headerText="Make" dataField="vehicle.make" width="35px"/>
		<primo:textcolumn headerText="Model" dataField="vehicle.model" width="200px"/>
		<primo:textcolumn headerText="Buyer" dataField="buyer.name" />
		<primo:datecolumn headerText="Sold Date" dataField="saleDate" dataFormat="MM-dd-yyyy" width="95px"/>
		<primo:textcolumn headerText="Sale Price" dataField="salePrice" width="75px"/>
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


