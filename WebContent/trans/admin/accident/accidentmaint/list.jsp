<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function processDownloadVideo(id) {
	$.ajax({
  		url: "ajax.do?action=doesVideoExist" + "&id=" + id,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData == 'false') {
       			alert("No video uploaded for the selected accident");
       			return;
       		}
       		
       		document.location = "${ctx}/admin/accident/accidentmaint/uploadvideo/download.do?id=" + id;
		}
	});
}

function processUploadVideo(id) {
	$.ajax({
  		url: "ajax.do?action=doesVideoExist" + "&id=" + id,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData == 'true') {
       			if (confirm("Do you want to replace the video already uploaded?")) {
       				uploadVideo(id);
           		}
       		} else {
       			uploadVideo(id);
       		}
		}
	});
}

function uploadVideo(id) {
	document.location = "${ctx}/admin/accident/accidentmaint/uploadvideo/start.do?id=" + id;
}

function processDeleteVideo(id) {
	$.ajax({
  		url: "ajax.do?action=doesVideoExist" + "&id=" + id,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData == 'false') {
       			alert("No video uploaded for the selected accident");
       			return;
       		}
       		
       		if (confirm("Do you want to Delete the selected video?")) {
       			deleteVideo(id);
       		}
		}
	});
}

function deleteVideo(id) {
	$.ajax({
  		url: "ajax.do?action=deleteVideo" + "&id=" + id,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		alert(responseData);
		}
	});
}
</script>

<h3>
	<primo:label code="Manage Accidents" />
</h3>
<form:form action="search.do" method="get" name="accidentSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Accidents" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
			<td align="${left}">
				<select id="driverCompany" name="driverCompany" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${companies}" var="aCompany">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['driverCompany'] == aCompany.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aCompany.id}" ${selected}>${aCompany.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Employee"/></td>
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
			<td align="${left}" class="first"><primo:label code="Insurance Company"/></td>
			<td align="${left}">
				<select id="insuranceCompany" name="insuranceCompany" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${insuranceCompanies}" var="anInsuranceCompany">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['insuranceCompany'] == anInsuranceCompany.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${anInsuranceCompany.id}" ${selected}>${anInsuranceCompany.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Claim No."/></td>
			<td align="${left}">
				<select id="claimNumber" name="claimNumber" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${accidents}" var="anAccident">
						<c:if test="${anAccident.claimNumber != null and anAccident.claimNumber != ''}">
							<c:set var="selected" value="" />
							<c:if test="${sessionScope.searchCriteria.searchMap['claimNumber'] == anAccident.claimNumber}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${anAccident.claimNumber}" ${selected}>${anAccident.claimNumber}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
	   	</tr>
		<tr>
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
			<td align="${left}" class="first"><primo:label code="Incident Date From"/></td>
			<td align="${left}">
				<input id="datepicker" name="incidentDateFrom" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.incidentDateFrom}" /> 
			</td>
			<td align="${left}" class="first"><primo:label code="Incident Date To"/></td>
			<td align="${left}">
				<input id="datepicker1" name="incidentDateTo" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.incidentDateTo}" /> 
			</td>
		</tr>
		<tr>
			<!-- 
			<td align="${left}" class="first"><primo:label code="Accident Type"/></td>
			<td align="${left}">
				<select id="accidentType" name="accidentType" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${accidentTypes}" var="anAccidentType">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['accidentType'] == anAccidentType.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${anAccidentType.id}" ${selected}>${anAccidentType.accidentType}</option>
					</c:forEach>
				</select>
			</td>
			 -->
			<td align="${left}" class="first"><primo:label code="Status"/></td>
			<td align="${left}">
				<select id="accidentStatus" name="accidentStatus" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${statuses}" var="aStatus">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['accidentStatus'] == aStatus.dataValue}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aStatus.dataValue}" ${selected}>${aStatus.dataText}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Recordable"/></td>
			<td align="${left}">
				<select id="recordable" name="recordable" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<option value="Yes">Yes</option>
					<option value="No">No</option>
				</select>
			</td>
	   	</tr>
	 	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['accidentSearchForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="accidentServiceForm" id="accidentServiceForm">
	<primo:datatable urlContext="admin/accident/accidentmaint" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="true" exportXls="true" exportCsv="true">
		<primo:textcolumn headerText="Ins Comp." dataField="insuranceCompany.name"/>
		<primo:textcolumn headerText="Claim #" dataField="claimNumber"/>
	    <primo:datecolumn headerText="Accident Date" dataField="incidentDate" dataFormat="MM-dd-yyyy"/>
	    <primo:textcolumn headerText="Accident Time" dataField="incidentTime" />
        <primo:textcolumn headerText="Employee" dataField="driver.fullName" />
        <primo:textcolumn headerText="Subcon." dataField="subcontractor.name" />
        <primo:textcolumn headerText="Comp." dataField="driverCompany.name"/>
        <primo:textcolumn headerText="Term." dataField="driverTerminal.code"/>
        <primo:textcolumn headerText="Unit" dataField="vehicle.unitNum" />
        <primo:textcolumn headerText="Location" dataField="location" />
        <primo:textcolumn headerText="State" dataField="state.name" />
        <primo:staticdatacolumn headerText="Status" dataField="accidentStatus" dataType="ACCIDENT_STATUS"/>
        <primo:textcolumn headerText="Accident Cause" dataField="accidentCause.cause" />
        <primo:textcolumn headerText="Rec." dataField="recordable"/>
        <primo:textcolumn headerText="# inj." dataField="noInjured"/>
        <primo:textcolumn headerText="Fatal." dataField="fatality"/>
        <primo:textcolumn headerText="Road" dataField="roadCondition.roadCondition"/>
        <primo:textcolumn headerText="Citn." dataField="citation"/>
        <primo:textcolumn headerText="Tow" dataField="towed"/>
        <primo:numbercolumn headerText="Tot Cost" dataField="totalCost" dataFormat="#####0.00"/>
        <primo:textcolumn headerText="Claim Rep" dataField="claimRep.name"/>
        <primo:textcolumn headerText="Phone" dataField="claimRep.phone"/>
        <primo:textcolumn headerText="Email" dataField="claimRep.email"/>
        <primo:anchorcolumn headerText="Upload Video" linkUrl="javascript:processUploadVideo('{id}');" linkText="Upload Video"/>
		<primo:anchorcolumn headerText="Down Video" linkUrl="javascript:processDownloadVideo('{id}');" linkText="Down Video"/>
		<primo:anchorcolumn headerText="Delete Video" linkUrl="javascript:processDeleteVideo('{id}');" linkText="Delete Video"/>
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


