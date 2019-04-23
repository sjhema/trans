<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
var submitting = false;
function submitForm(){
	if(submitting) {
    	alert('Please wait a moment...');
    } else{
         submitting = true;
         document.forms["roadsideInspectionForm"].submit();
	}
}

function retrieveDriverCompTerm() {
	var driverId = $('#driver').val();
	if (driverId == "") {
		return;
	}
	
	$.ajax({
  		url: "${ctx}/admin/roadsideinspec/roadsideinspecmaint/ajax.do?action=retrieveDriverCompTerm&driverId=" + driverId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData != '') {
	       		var compTermTokens = responseData.split("|");
	       		var companySel = $("#company");
	       		companySel.val(compTermTokens[0]);
       		}
		}
	});
}

function deleteViolation(violationId) {
	if(confirm("Do you want to delete the selected Violation?")) {
		var id = document.getElementById("id").value;
		document.location = "${ctx}/admin/roadsideinspec/roadsideinspecmaint/deleteViolation.do?id=" + id + "&violationId=" + violationId;
	}
}

function editViolation(violationId) {
	emptyViolation();
	
	$.ajax({
  		url: "ajax.do?action=retrieveViolation" + "&violationId=" + violationId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var violation = jQuery.parseJSON(responseData);
       		$("#violationId").val(violation.id);
       		$("#citationNo").val(violation.citationNo);
       		$("#outOfService").val(violation.outOfService);
       		$("#violationType").val(violation.violationType);
		}
	});
}

function emptyViolation() {
	$("#violationId").val("");
	$("#citationNo").val("");
	$("#outOfService").val("");
	$("#violationType").val("");
}

</script>
<h3>
	<primo:label code="Add/Update Roadside Inspections" />
</h3>
<form:form action="save.do" name="roadsideInspectionForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="violationId" id="violationId" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Roadside Inspection" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Inspection No" /></td>
			<td align="${left}">
				<form:input path="id" style="background-color: #eee; min-width:162px; max-width:162px" cssClass="flat" 
					 readonly="true" /> 
			</td>
			<td class="form-left"><primo:label code="Inspection Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input id="datepicker" path="inspectionDate" style="min-width:158px; max-width:158px" cssClass="flat"  />
				<br> 
				<form:errors path="inspectionDate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Driver" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="driver" id="driver" style="min-width:166px; max-width:166px"
					onchange="javascript:retrieveDriverCompTerm();">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${drivers}" itemValue="id" itemLabel="fullName"/>
				</form:select> 
				<br> <form:errors path="driver" cssClass="errorMessage" />
			</td>
			<td class="form-left">
				<primo:label code="Company" /><span class="errorMessage">*</span>
			</td>
			<td>
				<form:select cssClass="flat" path="company" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="company" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Inspection Level" /><span class="errorMessage">*</span></td>
			<td>	
				<form:select cssClass="flat" path="inspectionLevel" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:option value="1">1</form:option>
					<form:option value="2">2</form:option>
					<form:option value="3">3</form:option>
				</form:select> 
				<br><form:errors path="inspectionLevel" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Truck" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="truck" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${trucks}" itemValue="id" itemLabel="unit" />
				</form:select>
				<br> <form:errors path="truck" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Trailer" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="trailer" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${trailers}" itemValue="id" itemLabel="unit" />
				</form:select>
				<br> <form:errors path="trailer" cssClass="errorMessage" />
			</td>
		</tr>
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Violations" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Citation No" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="citationNo" style="min-width:162px; max-width:162px" cssClass="flat" />
				<br><form:errors path="citationNo" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Out Of Service" /><span class="errorMessage">*</span></td>
			<td>	
				<form:select cssClass="flat" path="outOfService" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:option value="Yes">Yes</form:option>
					<form:option value="No">No</form:option>
				</form:select> 
				<br><form:errors path="outOfService" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		    <td class="form-left"><primo:label code="Violation Type" /><span class="errorMessage">*</span></td>
			<td align="${left}" colspan="5">
				<form:textarea path="violationType" rows="2" cols="75"/>
				<br><form:errors path="violationType" cssClass="errorMessage" />   	
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="button" name="create" id="create" onclick="javascript:submitForm();"
					value="<primo:label code="Save"/>" class="flat" />
				&nbsp;
				<input type="reset" id="resetBtn"
					value="<primo:label code="Reset"/>" class="flat" />
				&nbsp;
				<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat"
					onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="violationServiceForm" id="violationServiceForm">
	<primo:datatable urlContext="admin/roadsideinspec/roadsideinspecmaint"
		baseObjects="${violationList}"
		searchCriteria="<%=null%>" cellPadding="2">
		<primo:textcolumn headerText="Citation No" dataField="citationNo" width="150px"/>
		<primo:textcolumn headerText="Out Of Service" dataField="outOfService" width="20px"/>
		<primo:textcolumn headerText="Violation Type" dataField="violationType" />
       	<primo:imagecolumn headerText="EDIT" linkUrl="javascript:editViolation('{id}');" imageSrc="${ctx}/images/edit.png" HAlign="center" width="25px"/>
       	<primo:imagecolumn headerText="DELETE" linkUrl="javascript:deleteViolation('{id}');" imageSrc="${ctx}/images/delete.png" HAlign="center" width="25px"/>
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


