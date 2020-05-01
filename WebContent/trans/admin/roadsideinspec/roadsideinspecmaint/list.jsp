<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function processManageDocs(id) {
	document.location = "${ctx}/admin/roadsideinspec/roadsideinspecmaint/managedocs/start.do?id=" + id;
}

function processEdit(violationId) {
	$.ajax({
  		url: "ajax.do?action=retrieveViolation" + "&violationId=" + violationId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var violation = jQuery.parseJSON(responseData);
       		var roadsideInspectionId = violation.roadsideInspection.id;
       		
       		document.location = "${ctx}/admin/roadsideinspec/roadsideinspecmaint/edit.do?id=" + roadsideInspectionId;
		}
	});
}

function processDelete(violationId) {
	$.ajax({
  		url: "ajax.do?action=retrieveViolation" + "&violationId=" + violationId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var violation = jQuery.parseJSON(responseData);
       		var roadsideInspectionId = violation.roadsideInspection.id;
       		
       		if (!confirm("Do you want to Delete Roadside Inspection # " + roadsideInspectionId + "?")) {
       			return;
       		}
       		
       		document.location = "${ctx}/admin/roadsideinspec/roadsideinspecmaint/delete.do?id=" + roadsideInspectionId;
		}
	});
}
</script>

<h3>
	<primo:label code="Manage Roadside Inspection" />
</h3>
<form:form action="search.do" method="get" name="roadsideInspectionSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Roadside Inspection" /></b></td>
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
			<td align="${left}" class="first"><primo:label code="Driver"/></td>
			<td align="${left}">
				<select id="driver" name="driver" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${driverNames}" var="aDriverName">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['driver'] == aDriverName}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aDriverName}" ${selected}>${aDriverName}</option>
					</c:forEach>
				</select>
			</td>
	   	</tr>
	   	<tr>
			<td align="${left}" class="first"><primo:label code="Inspection Date From"/></td>
			<td align="${left}">
				<input id="datepicker" name="inspectionDateFrom" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.inspectionDateFrom}" /> 
			</td>
			<td align="${left}" class="first"><primo:label code="Inspection Date To"/></td>
			<td align="${left}">
				<input id="datepicker1" name="inspectionDateTo" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.inspectionDateTo}" /> 
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Truck"/></td>
			<td align="${left}">
				<select id="truck.unit" name="truck.unit" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${trucks}" var="item">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['truck.unit'] ne ''}">
							<c:if test="${sessionScope.searchCriteria.searchMap['truck.unit'] == item.unit}">
								<c:set var="selected" value="selected" />
							</c:if>
						</c:if>
						<option value="${item.unit}"${selected}>${item.unit}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Trailer"/></td>
			<td align="${left}">
				<select id="trailer.unit" name="trailer.unit" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${trailers}" var="item">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['trailer.unit'] ne ''}">
							<c:if test="${sessionScope.searchCriteria.searchMap['trailer.unit'] == item.unit}">
								<c:set var="selected" value="selected" />
							</c:if>
						</c:if>
						<option value="${item.unit}"${selected}>${item.unit}</option>
					</c:forEach>
				</select>
			</td>
	   	</tr>
	  	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['roadsideInspectionSearchForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="roadsideInspectionServiceForm" id="roadsideInspectionServiceForm">
	<primo:datatable urlContext="admin/roadsideinspec/roadsideinspecmaint" deletable="false"
		editable="false" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Insp #" dataField="roadsideInspection.id" width="45px"/>
	    <primo:datecolumn headerText="Insp Date" dataField="roadsideInspection.inspectionDate" dataFormat="MM-dd-yyyy" width="90px"/>
        <primo:textcolumn headerText="Company" dataField="roadsideInspection.company.name" width="115px"/>
        <primo:textcolumn headerText="Truck" dataField="roadsideInspection.truck.unitNum" width="35px"/>
        <primo:textcolumn headerText="Trailer" dataField="roadsideInspection.trailer.unitNum" width="35px"/>
        <primo:textcolumn headerText="Driver" dataField="roadsideInspection.driver.fullName" width="190px"/>
        <primo:textcolumn headerText="Citation No" dataField="citationNo" width="130px"/>
        <primo:textcolumn headerText="Insp level" dataField="roadsideInspection.inspectionLevel" width="20px"/>
        <primo:textcolumn headerText="Has Violation" dataField="isViolation" />
        <primo:textcolumn headerText="Violation Type" dataField="violationType" />
        <primo:textcolumn headerText="Out Of Service" dataField="outOfService"/>
        <primo:textcolumn headerText="Pdf Uploaded" dataField="roadsideInspection.docs" width="20px"/>
		<primo:anchorcolumn headerText="Manage Pdf" linkUrl="javascript:processManageDocs('{id}');" linkText="Manage Pdf" width="20px"/>
	 	<primo:imagecolumn headerText="Edit" linkUrl="javascript:processEdit('{id}');" imageSrc="${ctx}/images/edit.png" HAlign="center" width="25px"/>
		<primo:imagecolumn headerText="Del" linkUrl="javascript:processDelete('{id}');" imageSrc="${ctx}/images/delete.png" HAlign="center" width="25px"/>
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


