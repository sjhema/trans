<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var submitting = false;
function submitForm(){
	if(submitting) {
    	alert('Please wait a moment...');
    } else{
         submitting = true;
         document.forms["insuranceCompanyRepForm"].submit();
	}
}
</script>
<h3>
	<primo:label code="Add/Update Insurance Company Rep" />
</h3>
<form:form action="save.do" name="insuranceCompanyRepForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr>
			<td class="form-left">
				<primo:label code="Insurance Company" /><span class="errorMessage">*</span>
			</td>
			<td>
				<form:select cssClass="flat" path="insuranceCompany" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${insuranceCompanies}" itemValue="id" itemLabel="name" />
				</form:select>
				<br><form:errors path="insuranceCompany" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="name" style="min-width:162px; max-width:162px" cssClass="flat"  /> 
				<br> 
				<form:errors path="name" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Phone" /></td>
			<td align="${left}">
				<form:input path="phone" style="min-width:162px; max-width:162px" cssClass="flat"  /> 
				<br> 
				<form:errors path="phone" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Email" /></td>
			<td align="${left}">
				<form:input path="email" style="min-width:162px; max-width:162px" cssClass="flat"  /> 
				<br> 
				<form:errors path="email" cssClass="errorMessage" />
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

