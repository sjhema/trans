<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
var submitting = false;
function submitForm(){
	if(submitting) {
    	alert('Please wait a moment...');
    } else{
         submitting = true;
         document.forms["vehicleLoanForm"].submit();
	}
}

function populateNoOfPaymentsAndLeft() {
	var noOfPaymentsElem = $("#noOfPayments");
	var paymentsLeftElem = $("#paymentsLeft");
	noOfPaymentsElem.val("");
	paymentsLeftElem.val("");
	
	var startDate = $("[name='startDate']").val();
	var endDate = $("[name='endDate']").val();
	var paymentDueDom = $("#paymentDueDom").val();
	if (startDate == '' || endDate == '' || paymentDueDom == '') {
		return;
	}
	
	$.ajax({
  		url: "ajax.do?action=calculateNoOfPaymentsAndLeft" + "&startDate=" + startDate + "&endDate=" + endDate
  											+ "&paymentDueDom=" + paymentDueDom,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var paymentsArr = jQuery.parseJSON(responseData);
       		noOfPaymentsElem.val(paymentsArr[0]);
       		paymentsLeftElem.val(paymentsArr[1]);
		}
	});
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
	<primo:label code="Add/Update Vehicle Loan" />
</h3>
<form:form action="save.do" name="vehicleLoanForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Vehicle Loan" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Loan No" /></td>
			<td align="${left}">
				<form:input path="loanNo" style="min-width:162px; max-width:162px" cssClass="flat"/> 
				<br> <form:errors path="loanNo" cssClass="errorMessage" />
			</td>
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
			<td class="form-left">
				<primo:label code="Lender" /><span class="errorMessage">*</span>
			</td>
			<td>
				<form:select cssClass="flat" path="lender" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${lenders}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="lender" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Start Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input id="datepicker" path="startDate" style="min-width:158px; max-width:158px" cssClass="flat" 
					onChange="return populateNoOfPaymentsAndLeft();" />
				<br><form:errors path="startDate" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="End Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input id="datepicker1" path="endDate" style="min-width:158px; max-width:158px" cssClass="flat"  
					onChange="return populateNoOfPaymentsAndLeft();"/>
				<br><form:errors path="endDate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Interest Rate" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="interestRate" style="min-width:162px; max-width:162px" cssClass="flat" 
					maxlength="5" onkeypress="return onlyNumbers(event, true)"/>
				<br> <form:errors path="interestRate" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Payment Amount" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="paymentAmount" style="min-width:162px; max-width:162px" cssClass="flat"
					maxlength="8" onkeypress="return onlyNumbers(event, true)" />
				<br> <form:errors path="paymentAmount" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Payment Due Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="paymentDueDom" id="paymentDueDom" style="min-width:75px; max-width:75px"
					onChange="return populateNoOfPaymentsAndLeft();">
					<form:options items="${paymentDueDates}"/>
				</form:select> of the month
				<br><form:errors path="paymentDueDom" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="No Of Payments" /></td>
			<td align="${left}">
				<form:input path="noOfPayments" style="background-color: #eee; min-width:162px; max-width:162px" cssClass="flat" 
					readonly="true"/>
				<br> <form:errors path="noOfPayments" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Payments Left" /></td>
			<td align="${left}">
				<form:input path="paymentsLeft" style="background-color: #eee; min-width:162px; max-width:162px" cssClass="flat" 
					readonly="true"/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Description" /></td>
			<td align="${left}" colspan="4">
				<form:input path="description" style="min-width:500px; max-width:500px" cssClass="flat"  />
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

