<%@include file="/common/taglibs.jsp"%>

<form:form action="search.do" method="get" name="repairOrderReportSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Repair Order Report" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Order no"/></td>
			<td align="${left}">
				<select id="orderId" name="orderId" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${repairOrders}" var="item">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['orderId'] == item.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.id}"${selected}>${item.id}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Order Date From"/></td>
			<td align="${left}">
				<input id="datepicker" name="repairOrderDateFrom" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.repairOrderDateFrom}" /> 
			</td>
			<td align="${left}" class="first"><primo:label code="Order Date To"/></td>
			<td align="${left}">
				<input id="datepicker1" name="repairOrderDateTo" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.repairOrderDateTo}" /> 
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
			<td align="${left}" class="first"><primo:label code="Subcontractor"/></td>
			<td align="${left}">
				<select id="subcontractor" name="subcontractor" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${subcontractors}" var="aSubcontractor">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['subcontractor'] == aSubcontractor.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${aSubcontractor.id}" ${selected}>${aSubcontractor.name}</option>
					</c:forEach>
				</select>
			</td>
	   	</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Vehicle"/></td>
			<td align="${left}">
				<select id="vehicle" name="vehicle" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${vehicles}" var="item">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['vehicle.unit'] ne ''}">
							<c:if test="${sessionScope.searchCriteria.searchMap['vehicle'] == item.unit}">
								<c:set var="selected" value="selected" />
							</c:if>
						</c:if>
						<option value="${item.unit}"${selected}>${item.unit}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Mechanic"/></td>
			<td align="${left}">
				<select id="mechanic" name="mechanic" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${mechanics}" var="aMechanic">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['mechanic'] == aMechanic.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aMechanic.id}" ${selected}>${aMechanic.fullName}</option>
					</c:forEach>
				</select>
			</td>
	   	</tr>
	   	<!--<tr>
			<td align="${left}" class="first"><primo:label code="Line Item Type"/></td>
			<td align="${left}">
				<select id="lineItemType" name="lineItemType" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${lineItemTypes}" var="aLineItemType">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['lineItemType'] == aLineItemType.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${aLineItemType.id}" ${selected}>${aLineItemType.type}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Component"/></td>
			<td align="${left}">
				<select id="lineItemComponent" name="lineItemComponent" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${components}" var="alineItemComponent">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['lineItemComponent'] == alineItemComponent.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${alineItemComponent.id}" ${selected}>${alineItemComponent.component}</option>
					</c:forEach>
				</select>
			</td>
	   	</tr>-->
	 	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['repairOrderReportSearchForm'].submit();"
					value="<primo:label code="Preview"/>" />
			</td>
		</tr>
	</table>
</form:form>



