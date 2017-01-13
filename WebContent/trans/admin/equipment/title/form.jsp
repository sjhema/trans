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
				<form:select cssClass="flat" path="vehicle" id="vehicle" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${vehicles}" itemValue="id" itemLabel="unit" />
				</form:select> 
				<br> <form:errors path="vehicle" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Owner" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="owner" id="owner" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${owners}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br> <form:errors path="owner" cssClass="errorMessage" />
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
			<td class="form-left"><primo:label code="Title" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="title" style="min-width:162px; max-width:162px" cssClass="flat"  />
				<br> <form:errors path="title" cssClass="errorMessage" />   	
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Title Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input id="datepicker" path="titleDate" style="min-width:158px; max-width:158px" cssClass="flat"/>
				<br><form:errors path="titleDate" cssClass="errorMessage" />
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

