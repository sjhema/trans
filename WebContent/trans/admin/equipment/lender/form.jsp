<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
var submitting = false;
function submitForm(){
	if(submitting) {
    	alert('Please wait a moment...');
    } else{
         submitting = true;
         document.forms["equipmentLenderForm"].submit();
	}
}

function formatPhone(elementId, elementName){
	var phoneElem = document.getElementById(elementId);
	var phone = phoneElem.value
	if (phone == "") {
		return;
	}
	
	if (phone.length < 10) {
		alert("Invalid " + elementName);
		phoneElem.value = "";
		return true;
	}
	
	var str = new String(phone);
	if(!str.match("-")){
		var p1 = str.substring(0,3);
		var p2 = str.substring(3,6);
		var p3 = str.substring(6,10);				
		var formattedPhone = p1 + "-" + p2 + "-" + p3;
		phoneElem.value = formattedPhone;
	}	
}
</script>
<h3>
	<primo:label code="Add/Update Equiment Lender" />
</h3>
<form:form action="save.do" name="equipmentLenderForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Equipment Lender" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="name" style="min-width:162px; max-width:162px" cssClass="flat"  />
				<br> <form:errors path="name" cssClass="errorMessage" />   	
			</td>
		<tr>
		<tr>
			<td class="form-left"><primo:label code="Address1" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="address1" style="min-width:262px; max-width:262px" cssClass="flat"  />
				<br> <form:errors path="address1" cssClass="errorMessage" />   	
			</td>
			<td class="form-left"><primo:label code="Address2" /></td>
			<td align="${left}">
				<form:input path="address2" style="min-width:262px; max-width:262px" cssClass="flat"  />
				<br> <form:errors path="address2" cssClass="errorMessage" />   	
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="City" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="city" style="min-width:162px; max-width:162px" cssClass="flat"  />
				<br> <form:errors path="city" cssClass="errorMessage" />   	
			</td>
			<td class="form-left"><primo:label code="State" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="state" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${states}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Zipcode" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="zipcode" style="min-width:162px; max-width:162px" cssClass="flat" 
					 maxlength="5" onkeypress="return onlyNumbers(event, false)"/>
				<br> <form:errors path="zipcode" cssClass="errorMessage" />   	
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Phone" /></td>
			<td align="${left}">
				<form:input path="phone" style="min-width:162px; max-width:162px" cssClass="flat"  
					maxlength="12" onkeypress="return onlyNumbers(event, false)" onblur="return formatPhone('phone', 'Phone Number');"/>
				<br> <form:errors path="phone" cssClass="errorMessage" />   	
			</td>
			<td class="form-left"><primo:label code="Fax" /></td>
			<td align="${left}">
				<form:input path="fax" style="min-width:162px; max-width:162px" cssClass="flat" 
					maxlength="12" onkeypress="return onlyNumbers(event, false)" onblur="return formatPhone('fax', 'Fax');"/>
				<br> <form:errors path="fax" cssClass="errorMessage" />   	
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

