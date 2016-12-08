<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function processCopy(orderId) {
	if (!confirm("Do you want to Copy Order # " + orderId + "?")) {
		return;
	}
	
	document.location = "${ctx}/admin/vehiclemaint/repairorders/copy.do?id=" + orderId;
}
</script>

<h3>
	<primo:label code="Manage Repair Orders" />
</h3>
<form:form action="search.do" method="get" name="repairOrderSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Repair Orders" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Order no"/></td>
			<td align="${left}">
				<select id="id" name="id" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${repairOrders}" var="item">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['id'] == item.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.id}"${selected}>${item.id}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Order Date"/></td>
			<td align="${left}">
				<input id="datepicker" name="RepairOrderDate" style="min-width:150px; max-width:150px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.RepairOrderDate}" /> 
			</td>
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
			<td align="${left}" class="first"><primo:label code="Mechanic"/></td>
			<td align="${left}">
				<select id="mechanic.id" name="mechanic.id" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${mechanics}" var="aMechanic">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['mechanic.id'] == aMechanic.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aMechanic.id}" ${selected}>${aMechanic.fullName}</option>
					</c:forEach>
				</select>
			</td>
	   	</tr>
        <tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
			<td align="${left}">
				<select id="company" name="company.id" style="min-width:154px; max-width:154px">
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
			<td align="${left}" class="first"><primo:label code="Subcontractor"/></td>
			<td align="${left}">
				<select id="subcontractor" name="subcontractor.id" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${subcontractors}" var="aSubcontractor">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['subcontractor.id'] == aSubcontractor.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${aSubcontractor.id}" ${selected}>${aSubcontractor.name}</option>
					</c:forEach>
				</select>
			</td>
	   	</tr>
	 	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['repairOrderSearchForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="repairOrderServiceForm" id="repairOrderServiceForm">
	<primo:datatable urlContext="admin/vehiclemaint/repairorders" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Ord #" dataField="id" width="50px"/>
	    <primo:datecolumn headerText="Order Date" dataField="repairOrderDate" dataFormat="MM-dd-yyyy" width="95px"/>
        <primo:textcolumn headerText="Company" dataField="company.name" />
        <primo:textcolumn headerText="Subcontractor" dataField="subcontractor.name" />
        <primo:textcolumn headerText="Vehicle" dataField="vehicle.unitNum" width="40px"/>
        <primo:textcolumn headerText="Mechanic" dataField="mechanic.fullName" />
        <primo:textcolumn headerText="Description" dataField="description" width="450px"/>
        <primo:textcolumn headerText="Tot Cost" dataField="totalCost" width="60px"/>
		<primo:imagecolumn headerText="Copy" linkUrl="javascript:processCopy('{id}');" imageSrc="${ctx}/images/copy.png" HAlign="center"/>
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


