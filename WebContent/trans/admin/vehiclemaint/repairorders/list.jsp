<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function processCopyLineItem(lineItemId) {
	if (!confirm("Do you want to Copy the selected Line Item?")) {
		return;
	}
	
	document.location = "${ctx}/admin/vehiclemaint/repairorders/copy.do?lineItemId=" + lineItemId;
}

function processCopyOrder(orderId) {
	if (!confirm("Do you want to Copy the selected Order?")) {
		return;
	}
	
	document.location = "${ctx}/admin/vehiclemaint/repairorders/copy.do?orderId=" + orderId;
}

function processEdit(lineItemId) {
	$.ajax({
  		url: "ajax.do?action=retrieveLineItem" + "&lineItemId=" + lineItemId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var lineItem = jQuery.parseJSON(responseData);
       		var orderId = lineItem.repairOrder.id;
       		
       		document.location = "${ctx}/admin/vehiclemaint/repairorders/edit.do?id=" + orderId;
		}
	});
}

function processDelete(lineItemId) {
	$.ajax({
  		url: "ajax.do?action=retrieveLineItem" + "&lineItemId=" + lineItemId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var lineItem = jQuery.parseJSON(responseData);
       		var orderId = lineItem.repairOrder.id;
       		
       		if (!confirm("Do you want to Delete Order # " + orderId + "?")) {
       			return;
       		}
       		
       		document.location = "${ctx}/admin/vehiclemaint/repairorders/delete.do?id=" + orderId;
		}
	});
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
			<td align="${left}" class="first"><primo:label code="Subcontractor"/></td>
			<td align="${left}">
				<select id="subcontractor.id" name="subcontractor.id" style="min-width:154px; max-width:154px">
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
					onclick="document.forms['repairOrderSearchForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="repairOrderServiceForm" id="repairOrderServiceForm">
	<primo:datatable urlContext="admin/vehiclemaint/repairorders" deletable="false"
		editable="false" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Ord #" dataField="repairOrder.id" width="50px"/>
	    <primo:datecolumn headerText="Order Date" dataField="repairOrder.repairOrderDate" dataFormat="MM-dd-yyyy" width="95px"/>
        <primo:textcolumn headerText="Company" dataField="repairOrder.company.name" />
        <primo:textcolumn headerText="Subcontractor" dataField="repairOrder.subcontractor.name" />
        <primo:textcolumn headerText="Vehic" dataField="repairOrder.vehicle.unitNum" width="35px"/>
        <primo:textcolumn headerText="Mechanic" dataField="repairOrder.mechanic.fullName" />
        <primo:textcolumn headerText="Type" dataField="lineItemType.type" />
        <primo:textcolumn headerText="Component" dataField="component.component" />
        <primo:textcolumn headerText="Description" dataField="description" width="450px"/>
        <primo:textcolumn headerText="Labor Rate" dataField="laborRate" width="75px"/>
        <primo:textcolumn headerText="No Of Hrs" dataField="noOfHours" width="75px"/>
        <primo:textcolumn headerText="Tot Labor Cost" dataField="totalLaborCost" width="75px"/>
        <primo:textcolumn headerText="Tot Parts Cost" dataField="totalPartsCost" width="75px"/>
       	<primo:textcolumn headerText="Tot Ln Itm Cost" dataField="totalCost" width="75px" />
        <primo:textcolumn headerText="Tot Order Cost" dataField="repairOrder.totalCost" width="60px"/>
       	<primo:anchorcolumn headerText="Cpy Ord" linkUrl="javascript:processCopyOrder('{repairOrder.id}');" linkText="Cpy Ord"/>
		<primo:imagecolumn headerText="Cpy LI" linkUrl="javascript:processCopyLineItem('{id}');" imageSrc="${ctx}/images/copy.png" HAlign="center"/>
		<primo:imagecolumn headerText="Edit" linkUrl="javascript:processEdit('{id}');" imageSrc="${ctx}/images/edit.png" HAlign="center"/>
		<primo:imagecolumn headerText="Del" linkUrl="javascript:processDelete('{id}');" imageSrc="${ctx}/images/delete.png" HAlign="center"/>
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


