<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var submitting = false;
function submitForm(){
	if(submitting) {
    	alert('Please wait a moment...');
    } else{
         submitting = true;
         document.forms["repairOrderHourlyLaborRateForm"].submit();
	}
}
</script>
<h3>
	<primo:label code="Add/Update Repair Order Hourly Labor Rate" />
</h3>
<form:form action="save.do" name="repairOrderHourlyLaborRateForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Repair Order Hourly Labor Rate" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Effective Start Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input id="datepicker" path="effectiveStartDate" style="min-width:150px; max-width:150px" cssClass="flat"  />
				<br> 
				<form:errors path="effectiveStartDate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Effective End Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input id="datepicker1" path="effectiveEndDate" style="min-width:150px; max-width:150px" cssClass="flat"  />
				<br> 
				<form:errors path="effectiveEndDate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Labor Rate" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="laborRate" style="min-width:150px; max-width:150px" cssClass="flat"  /> 
				<br> 
				<form:errors path="laborRate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Description" /></td>
			<td align="${left}">
				<form:input path="description" style="min-width:600px; max-width:600px" cssClass="flat"  /> 
				<br> 
				<form:errors path="description" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="button" name="create" id="create" onclick="javascript:submitForm();"
					value="<primo:label code="Save"/>" class="flat" />
				<input type="reset" id="resetBtn" value="<primo:label code="Reset"/>" class="flat" /> 
				<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat"
					onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>

