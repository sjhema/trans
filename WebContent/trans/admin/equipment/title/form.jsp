<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
var submitting = false;
function submitForm(){
	if(submitting) {
    	alert('Please wait a moment...');
    } else{
         submitting = true;
         document.forms["vehicleTitleForm"].submit();
	}
}

function populateVehicleDetails() {
	$("#company").val("");
	$("#vin").val("");
	$("#year").val("");
	$("#make").val("");
	$("#model").val("");
		
	var id = $("#vehicle").val();
	if (id == '') {
		return;
	}
	
	$.ajax({
  		url: "ajax.do?action=retrieveVehicle" + "&id=" + id,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var vehicle = jQuery.parseJSON(responseData);
       		$("#company").val(vehicle.owner.name);
       		$("#vin").val(vehicle.vinNumber);
       		$("#year").val(vehicle.year);
       		$("#make").val(vehicle.make);
       		$("#model").val(vehicle.model);
		}
	});
}
</script>
<h3>
	<primo:label code="Add/Update Vehicle Title" />
</h3>
<form:form action="save.do" name="vehicleTitleForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Vehicle Title" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Vehicle" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="vehicle" id="vehicle" style="min-width:166px; max-width:166px"
					onChange="return populateVehicleDetails();">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${vehicles}" itemValue="id" itemLabel="unit" />
				</form:select> 
				<br> <form:errors path="vehicle" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Company" /></td>
			<td align="${left}">
				<input id="company" value="${modelObject.vehicle.owner.name}" style="background-color: #eee; min-width:162px; max-width:162px" 
					readonly class="flat"  />
			</td>
			<td class="form-left"><primo:label code="VIN" /></td>
			<td align="${left}">
				<input id="vin" value="${modelObject.vehicle.vinNumber}" style="background-color: #eee; min-width:162px; max-width:162px" class="flat" readonly/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Year" /></td>
			<td align="${left}">
				<input id="year" value="${modelObject.vehicle.year}" style="background-color: #eee; min-width:162px; max-width:162px" class="flat" readonly/>
			</td>
			<td class="form-left"><primo:label code="Make" /></td>
			<td align="${left}">
				<input id="make" value="${modelObject.vehicle.make}" style="background-color: #eee; min-width:162px; max-width:162px" class="flat" readonly/>
			</td>
			<td class="form-left"><primo:label code="Model" /></td>
			<td align="${left}">
				<input id="model" value="${modelObject.vehicle.model}" style="background-color: #eee; min-width:162px; max-width:162px" class="flat" readonly/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Title Owner" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="titleOwner" id="titleOwner" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${owners}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br> <form:errors path="titleOwner" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Registered Owner" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="registeredOwner" id="registeredOwner" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${owners}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br> <form:errors path="registeredOwner" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Holds Title" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="holdsTitle" id="holdsTitle" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:option value="Yes">Yes</form:option>
					<form:option value="No">No</form:option>
				</form:select> 
				<br> <form:errors path="holdsTitle" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Title" /></td>
			<td align="${left}">
				<form:input path="title" style="min-width:162px; max-width:162px" cssClass="flat"  />
				<br> <form:errors path="title" cssClass="errorMessage" />   	
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Notes" /></td>
			<td align="${left}" colspan="4">
				<form:input path="description" style="min-width:600px; max-width:600px" cssClass="flat"  />
				<br> <form:errors path="description" cssClass="errorMessage" />   	
			</td>
		<tr>
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

