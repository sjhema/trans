<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
var submitting = false;
function submitForm(){
	if(submitting) {
    	alert('Please wait a moment...');
    } else{
         submitting = true;
         document.forms["injuryForm"].submit();
	}
}

function emptySelect(selectElem) {
	selectElem.empty();
	selectElem.append('<option selected="selected" value="">-----Please Select-----</option>');
}

function retrieveClaimReps() {
	var claimRepSel = $("#claimRep");
	emptySelect(claimRepSel);
	
	var insuranceCompanyId = $('#insuranceCompany').val();
	if (insuranceCompanyId == "") {
		return;
	}
	
	$.ajax({
  		url: "${ctx}/admin/injury/injurymaint/ajax.do?action=retrieveClaimReps&insuranceCompanyId=" + insuranceCompanyId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var claimRepsList = jQuery.parseJSON(responseData);
			
			var options = '<option value="">-----Please Select-----</option>';
			for (var i = 0; i < claimRepsList.length; i++) {
				var aClaimrep = claimRepsList[i];
				options += '<option value="'+aClaimrep.id+'">'+aClaimrep.name+'</option>';
			}
			claimRepSel.html(options);
		}
	});
}

function saveInjuryTo() {
	clearInjuryToDialogMsgs();
	
	var injuryToSel = $('#injuryTo');
	
	var injuryTo = $('#injuryToDialogInjuryTo').val();
	
	var injuryToDialogErrorMessageElem = $('#injuryToDialogErrorMessage');
	$.ajax({
  		url: "${ctx}/admin/injury/injurymaint/ajax.do?action=saveInjuryTo" + "&injuryTo=" + injuryTo,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData.indexOf("success") == -1) {
       			injuryToDialogErrorMessageElem.html(responseData);
       		} else {
       			var id = responseData.substring(responseData.indexOf(":") + 1);
       			var option = "<option value='" + id + "'>" + injuryTo + "</option>";
       			injuryToSel.append(option);
       			injuryToSel.val(id);
       			
       			closeInjuryToDialog();
       		}
		}
	});
}

function clearInjuryToDialog() {
	$('#injuryToDialogInjuryTo').val("");
	
	clearInjuryToDialogMsgs();
}

function clearInjuryToDialogMsgs() {
	$('#injuryToDialogErrorMessage').html("");
	$('#injuryToDialogSuccessMessage').html("");
}

function openInjuryToDialog() {
	clearInjuryToDialog();
	$('#injuryToDialog').dialog({ 
		 modal: true,
         bgiframe: true,
         width: 500,
         resizable: false
     });
}

function closeInjuryToDialog() {
	clearInjuryToDialog();
	$('#injuryToDialog').dialog('close');
}

function saveIncidentType() {
	clearIncidentTypeDialogMsgs();
	
	var incidentTypeSel = $('#incidentType');
	
	var incidentType = $('#incidentTypeDialogIncidentType').val();
	
	var incidentTypeDialogErrorMessageElem = $('#incidentTypeDialogErrorMessage');
	
	$.ajax({
  		url: "${ctx}/admin/injury/injurymaint/ajax.do?action=saveIncidentType" + "&incidentType=" + incidentType,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData.indexOf("success") == -1) {
       			incidentTypeDialogErrorMessageElem.html(responseData);
       		} else {
       			var id = responseData.substring(responseData.indexOf(":") + 1);
       			var option = "<option value='" + id + "'>" + incidentType + "</option>";
       			incidentTypeSel.append(option);
       			incidentTypeSel.val(id);
       			
       			closeIncidentTypeDialog();
       		}
		}
	});
}

function clearIncidentTypeDialog() {
	$('#incidentTypeDialogName').val("");
	
	clearIncidentTypeDialogMsgs();
}

function clearIncidentTypeDialogMsgs() {
	$('#incidentTypeDialogErrorMessage').html("");
	$('#incidentTypeDialogSuccessMessage').html("");
}

function openIncidentTypeDialog() {
	clearIncidentTypeDialog();
	$('#incidentTypeDialog').dialog({ 
		 modal: true,
         bgiframe: true,
         width: 500,
         resizable: false
     });
}

function closeIncidentTypeDialog() {
	clearIncidentTypeDialog();
	$('#incidentTypeDialog').dialog('close');
}

function saveInsuranceCompany() {
	clearInsuranceCompanyDialogMsgs();
	
	var insuranceCompanySel = $('#insuranceCompany');
	var claimRepSel = $("#claimRep");
	
	var name = $('#insuranceCompanyDialogName').val();
	
	var insuranceCompanyDialogErrorMessageElem = $('#insuranceCompanyDialogErrorMessage');
	
	$.ajax({
  		url: "${ctx}/admin/injury/injurymaint/ajax.do?action=saveInsuranceCompany" + "&name=" + name,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData.indexOf("success") == -1) {
       			insuranceCompanyDialogErrorMessageElem.html(responseData);
       		} else {
       			var id = responseData.substring(responseData.indexOf(":") + 1);
       			var option = "<option value='" + id + "'>" + name + "</option>";
       			insuranceCompanySel.append(option);
       			insuranceCompanySel.val(id);
       			
       			emptySelect(claimRepSel);
       			
       			closeInsuranceCompanyDialog();
       		}
		}
	});
}

function clearInsuranceCompanyDialog() {
	$('#insuranceCompanyDialogName').val("");
	
	clearInsuranceCompanyDialogMsgs();
}

function clearInsuranceCompanyDialogMsgs() {
	$('#insuranceCompanyDialogErrorMessage').html("");
	$('#insuranceCompanyDialogSuccessMessage').html("");
}

function openInsuranceCompanyDialog() {
	clearInsuranceCompanyDialog();
	$('#insuranceCompanyDialog').dialog({ 
		 modal: true,
         bgiframe: true,
         width: 500,
         resizable: false
     });
}

function closeInsuranceCompanyDialog() {
	clearInsuranceCompanyDialog();
	$('#insuranceCompanyDialog').dialog('close');
}

function saveInsuranceCompanyRep() {
	clearInsuranceCompanyRepDialogMsgs();
	
	var insuranceCompanySel = $('#insuranceCompany');
	var claimRepSel = $('#claimRep');
	
	var inusranceCompanyId = insuranceCompanySel.val();
	var name = $('#insuranceCompanyRepDialogName').val();
	var phone = $('#insuranceCompanyRepDialogPhone').val();
	var email = $('#insuranceCompanyRepDialogEmail').val();
	
	var insuranceCompanyRepDialogErrorMessageElem = $('#insuranceCompanyRepDialogErrorMessage');
	
	$.ajax({
  		url: "${ctx}/admin/injury/injurymaint/ajax.do?action=saveInsuranceCompanyRep" 
  				+ "&inusranceCompanyId=" + inusranceCompanyId + "&name=" + name + "&phone=" + phone + "&email=" + email,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData.indexOf("success") == -1) {
       			insuranceCompanyRepDialogErrorMessageElem.html(responseData);
       		} else {
       			var id = responseData.substring(responseData.indexOf(":") + 1);
       			claimRepSel.append($('<option>', {
       			    value: id,
       			    text: name
       			}));
       			claimRepSel.val(id);
       			
       			closeInsuranceCompanyRepDialog();
       		}
		}
	});
}

function clearInsuranceCompanyRepDialog() {
	$('#insuranceCompanyRepDialogName').val("");
	$('#insuranceCompanyRepDialogPhone').val("");
	$('#insuranceCompanyRepDialogEmail').val("");
	
	clearInsuranceCompanyRepDialogMsgs();
}

function clearInsuranceCompanyRepDialogMsgs() {
	$('#insuranceCompanyRepDialogErrorMessage').html("");
	$('#insuranceCompanyRepDialogSuccessMessage').html("");
}

function openInsuranceCompanyRepDialog() {
	var insuranceCompanyId = $('#insuranceCompany').val();
	if (insuranceCompanyId == '') {
		alert("Please select Insurance Company for adding claim rep");
		return;
	}
	
	clearInsuranceCompanyRepDialog();
	$('#insuranceCompanyRepDialog').dialog({ 
		 modal: true,
         bgiframe: true,
         width: 500,
         resizable: false
     });
}

function closeInsuranceCompanyRepDialog() {
	clearInsuranceCompanyRepDialog();
	$('#insuranceCompanyRepDialog').dialog('close');
}

function populateTotalCost() {
	var medicalCostElem = document.getElementById("medicalCost");
	var indemnityCostElem = document.getElementById("indemnityCost");
	var expenseElem = document.getElementById("expense");
	var reserveElem = document.getElementById("reserve");
	
	var toalPaidElem = document.getElementById("totalPaid");
	var totalClaimedElem = document.getElementById("totalClaimed");
	
	var medicalCost = parseFloat(0.00);
	if (medicalCostElem.value != '') {
		medicalCost = parseFloat((parseFloat(medicalCostElem.value)).toFixed(2));
	}
	
	var indemnityCost = parseFloat(0.00);
	if (indemnityCostElem.value != '') {
		indemnityCost = parseFloat((parseFloat(indemnityCostElem.value)).toFixed(2));
	}
	
	var expense = parseFloat(0.00);
	if (expenseElem.value != '') {
		expense = parseFloat((parseFloat(expenseElem.value)).toFixed(2));
	}
	
	var reserve = parseFloat(0.00);
	if (reserveElem.value != '') {
		reserve = parseFloat((parseFloat(reserveElem.value)).toFixed(2));
	}
	
	var totalPaid =  parseFloat((medicalCost + indemnityCost + expense + reserve).toFixed(2));
	var totalClaimed =  parseFloat((medicalCost + indemnityCost + expense + reserve).toFixed(2));
	
	toalPaidElem.value = totalPaid;
	totalClaimedElem.value = totalClaimed;
}

function formatDate(dateElemId) {
	var dateElem = document.getElementById(dateElemId);
	var date = dateElem.value;
	
	if (date != "") {
		if (date.length < 8) {
			alert("Invalidte date format");
			dateElem.value = "";
			return true;
		} else {
			var str = new String(date);
			if (!str.match("-")) {
				var mm = str.substring(0,2);
				var dd = str.substring(2,4);
				var yy = str.substring(4,8);
				var enddigit = str.substring(6,8);
				if (!enddigit == 00 && enddigit%4 == 0 ) {
					if (mm == 04 || mm == 06 || mm == 09 || mm == 11) {
						if (dd > 30) {
							alert("Invalid date format");
							dateElem.value="";
							return true;
						}
					} if (mm == 02 && dd > 29) {
						alert("Invalid date format");
						dateElem.value = "";
						return true;
					} else if (dd > 31) {
						alert("Invalid date format");
						dateElem.value="";
						return true;
					}
				} if (enddigit == 00 && yy%400 == 0) {
					if (mm == 04 || mm == 06 || mm == 09 || mm == 11) {
						if (dd > 30) {
							alert("Invalid date format");
							dateElem.value="";
							return true;
						}
					} if (mm == 02 && dd > 29) {
						alert("Invalid date format");
						dateElem.value="";
						return true;
					} else if (dd > 31) {
						alert("Invalid date format");
						dateElem.value="";
						return true;
					}					
				} else {
					if (mm == 04 || mm == 06 || mm == 09 || mm == 11) {
						if (dd > 30) {
							alert("Invalid date format");
							dateElem.value = "";
							return true;
						}
					} if (mm == 02 && dd > 28) {
						alert("Invalid date format");
						dateElem.value = "";
						return true;
					} else if (dd > 31) {
						alert("Invalid date format");
						dateElem.value = "";
						return true;
					}
				}
				date = mm+"-"+dd+"-"+yy;
				dateElem.value = date;
			}
	 	}
	}
}

function formatTime(timeElemId) {
	var timeElem = document.getElementById(timeElemId);
	var time = timeElem.value;
	
	if (time != "") {
		if (time.length < 4) {
			alert("Invalidte Time format");
			timeElem.value = "";
			timeElem.focus();
			return true;
		} else {
			var str = new String(time);
			if (!str.match(":")) {
				var hour = str.substring(0,2);
				var min = str.substring(2,4);
				if (hour >= 24 || min >= 60){
					alert("Invalidte Time format");
					timeElem.value = "";
					timeElem.focus();
					return true;
				}
				time = hour + ":" + min;
				timeElem.value = time;
			}
		}
	}
}
</script>
<h3>
	<primo:label code="Add/Update Injuries" />
</h3>
<form:form action="save.do" name="injuryForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Injury" /></b></td>
		</tr>
		<tr>
			<td class="form-left">
				<primo:label code="Insurance Company" /><span class="errorMessage"></span>
			</td>
			<td>
				<form:select cssClass="flat" path="insuranceCompany" style="min-width:166px; max-width:166px"
					onchange="javascript:retrieveClaimReps();">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${insuranceCompanies}" itemValue="id" itemLabel="name" />
				</form:select>
				&nbsp;
				<a href="javascript:openInsuranceCompanyDialog();">
					<img class="toolbarButton" src="/trans/images/add.png" style="vertical-align: top" title="Add Insurance Company" border="0">
				</a>
				<br><form:errors path="insuranceCompany" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Claim Rep" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="claimRep" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${claimReps}" itemValue="id" itemLabel="name" />
				</form:select> 
				&nbsp;
				<a href="javascript:openInsuranceCompanyRepDialog();">
					<img class="toolbarButton" src="/trans/images/add.png" style="vertical-align: top" title="Add Insurance Company Rep" border="0">
				</a>
				<br><form:errors path="claimRep" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Claim No" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="claimNumber" style="min-width:162px; max-width:162px" cssClass="flat" />
				<br><form:errors path="claimNumber" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Employee" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="driver" id="driver" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${drivers}" itemValue="id" itemLabel="fullName"/>
				</form:select> 
				<br> <form:errors path="driver" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Incident Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input id="datepicker" path="incidentDate" style="min-width:158px; max-width:158px" 
					cssClass="flat" size="10" onblur="return formatDate('datepicker');" />
				<br><form:errors path="incidentDate" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Incident Time" /></td>
			<td align="${left}">
				<form:input path="incidentTime" size="5" style="min-width:162px; max-width:162px" cssClass="flat" 
					onkeypress="return onlyNumbers(event, false)" onblur="return formatTime('incidentTime')"/>
				<br><form:errors path="incidentTime" cssClass="errorMessage" /> 
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Incident Type" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="incidentType" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${incidentTypes}" itemValue="id" itemLabel="incidentType" />
				</form:select>
				&nbsp;
				<a href="javascript:openIncidentTypeDialog();">
					<img class="toolbarButton" src="/trans/images/add.png" style="vertical-align: top" title="Add Incident Type" border="0">
				</a>
				<br><form:errors path="incidentType" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Status" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="injuryStatus" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${statuses}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> 
				<br><form:errors path="injuryStatus" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Location" /><span class="errorMessage"></span></td>
			<!--  
			<td>
				<form:select cssClass="flat" path="location" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${locations}" itemValue="id" itemLabel="typeDescription" />
				</form:select> 
				<br><form:errors path="location" cssClass="errorMessage" />
			</td>
			-->
			<td align="${left}">
				<form:input path="location" style="min-width:162px; max-width:162px" cssClass="flat" />
				<br><form:errors path="location" cssClass="errorMessage" /> 
			</td>
			<td class="form-left"><primo:label code="Injury To" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="injuryTo" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${injuryToTypes}" itemValue="id" itemLabel="injuryTo" />
				</form:select>
				&nbsp;
				<a href="javascript:openInjuryToDialog();">
					<img class="toolbarButton" src="/trans/images/add.png" style="vertical-align: top" title="Add Injury To" border="0">
				</a>
				<br><form:errors path="injuryTo" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Tarp Related Injury" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="tarpRelatedInjury" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:option value="Yes">Yes</form:option>
					<form:option value="No">No</form:option>
				</form:select> 
				<br><form:errors path="tarpRelatedInjury" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="First Report Of Injury" /></td>
			<td>
				<form:select cssClass="flat" path="firstReportOfInjury" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:option value="Yes">Yes</form:option>
					<form:option value="No">No</form:option>
				</form:select> 
				<br><form:errors path="firstReportOfInjury" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Return To Work" /></td>
			<td align="${left}">
				<form:input path="returnToWork" style="min-width:162px; max-width:162px" cssClass="flat" />
				<br><form:errors path="returnToWork" cssClass="errorMessage" /> 
			</td>
			<td class="form-left"><primo:label code="Return To Work Date" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input id="datepicker1" path="returnToWorkDate" style="min-width:158px; max-width:158px" 
					cssClass="flat" size="10" onblur="return formatDate('datepicker1');"/>
				<br><form:errors path="returnToWorkDate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="No Of Lost Work Days" /></td>
			<td align="${left}">
				<form:input path="noOfLostWorkDays" style="min-width:162px; max-width:162px" cssClass="flat" />
				<br><form:errors path="noOfLostWorkDays" cssClass="errorMessage" /> 
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Osha Recordable" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="oshaRecordable" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:option value="Yes">Yes</form:option>
					<form:option value="No">No</form:option>
				</form:select> 
				<br><form:errors path="oshaRecordable" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Attorney" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="attorney" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:option value="Yes">Yes</form:option>
					<form:option value="No">No</form:option>
				</form:select> 
				<br><form:errors path="attorney" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Medical Cost" /><span class="errorMessage"></span></td>
			<td>
				<form:input path="medicalCost" style="min-width:162px; max-width:162px" cssClass="flat" 
					 maxlength="7" onkeypress="return onlyNumbers(event, true)" onchange="return populateTotalCost();"  />  
				<br><form:errors path="medicalCost" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Indemnity" /><span class="errorMessage"></span></td>
			<td>
				<form:input path="indemnityCost" style="min-width:162px; max-width:162px" cssClass="flat" 
					 maxlength="7" onkeypress="return onlyNumbers(event, true)" onchange="return populateTotalCost();"  />  
				<br><form:errors path="indemnityCost" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Expense" /><span class="errorMessage"></span></td>
			<td>
				<form:input path="expense" style="min-width:162px; max-width:162px" cssClass="flat" 
					 maxlength="7" onkeypress="return onlyNumbers(event, true)" onchange="return populateTotalCost();"  />  
				<br><form:errors path="expense" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Reserve" /><span class="errorMessage"></span></td>
			<td>
				<form:input path="reserve" style="min-width:162px; max-width:162px" cssClass="flat" 
					 maxlength="7" onkeypress="return onlyNumbers(event, true)" onchange="return populateTotalCost();"  />  
				<br><form:errors path="reserve" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Total Paid" /><span class="errorMessage"></span></td>
			<td>
				<!-- 
				<form:input path="totalPaid" style="background-color: #eee; min-width:164px; max-width:164px" cssClass="flat" 
					readonly="true" />
				 -->
				<form:input path="totalPaid" style="min-width:162px; max-width:162px" cssClass="flat" 
					 maxlength="9" onkeypress="return onlyNumbers(event, true)" />
				<br><form:errors path="totalPaid" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Total Claimed" /><span class="errorMessage"></span></td>
			<td>
				<form:input path="totalClaimed" style="min-width:162px; max-width:162px" cssClass="flat" 
					 maxlength="9" onkeypress="return onlyNumbers(event, true)" />
				<br><form:errors path="totalClaimed" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		    <td class="form-left"><primo:label code="Notes" /><span class="errorMessage"></span></td>
			<td align="${left}" colspan="5">
				<form:textarea path="notes" rows="2" cols="75"/>    	
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="button" name="create" id="create" onclick="javascript:submitForm();"
					value="<primo:label code="Save"/>" class="flat" />
				&nbsp;
				<input type="reset" id="resetBtn" value="<primo:label code="Reset"/>" class="flat" />
				&nbsp;
				<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat"
					onClick="document.location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>
<br />

<div id="insuranceCompanyDialog" title="Add Insurance Company" style="display:none;">
	<div id="insuranceCompanyDialogBody">
		<div id="insuranceCompanyDialogMessage">
     		<div id="insuranceCompanyDialogErrorMessage" style="color:red; font-size:14px; vertical-align:center;"></div>
     		<div id="insuranceCompanySuccessMessage" style="color:green; font-size:14px; vertical-align:center;"></div>
     	</div>
		<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
			<tr>
				<td class="form-left"><primo:label code="Name" /><span class="errorMessage">*</span></td>
				<td align="${left}">
					<input type="text" id="insuranceCompanyDialogName" style="min-width:300px; max-width:300px" class="flat">
				</td>
			</tr>
			<tr><td colspan="2"></td></tr>
			<tr>
				<td>&nbsp;</td>
				<td align="${left}" colspan="2">
					<input type="button" id="insuranceCompanyDialogCreate" onclick="javascript:saveInsuranceCompany();"
						value="<primo:label code="Save"/>" class="flat" />
					<input type="button" id="insuranceCompanyDialogCancel" value="<primo:label code="Cancel"/>" class="flat"
						onClick="javascript:closeInsuranceCompanyDialog();" />
				</td>
			</tr>
		</table>
	</div>
</div>

<div id="insuranceCompanyRepDialog" title="Add Claim Rep" style="display:none;">
	<div id="insuranceCompanyRepDialogBody">
		<div id="insuranceCompanyRepDialogMessage">
     		<div id="insuranceCompanyRepDialogErrorMessage" style="color:red; font-size:14px; vertical-align:center;"></div>
     		<div id="insuranceCompanyRepDialogSuccessMessage" style="color:green; font-size:14px; vertical-align:center;"></div>
     	</div>
		<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
			<tr>
				<td class="form-left"><primo:label code="Claim Rep" /><span class="errorMessage">*</span></td>
				<td align="${left}">
					<input type="text" id="insuranceCompanyRepDialogName" style="min-width:300px; max-width:300px" class="flat">
				</td>
			</tr>
			<tr>
				<td class="form-left"><primo:label code="Phone" /></td>
				<td align="${left}">
					<input type="text" id="insuranceCompanyRepDialogPhone" style="min-width:300px; max-width:300px" class="flat">
				</td>
			</tr>
			<tr>
				<td class="form-left"><primo:label code="Email" /></td>
				<td align="${left}">
					<input type="text" id="insuranceCompanyRepDialogEmail" style="min-width:300px; max-width:300px" class="flat">
				</td>
			</tr>
			<tr><td colspan="2"></td></tr>
			<tr>
				<td>&nbsp;</td>
				<td align="${left}" colspan="2">
					<input type="button" id="insuranceCompanyRepDialogCreate" onclick="javascript:saveInsuranceCompanyRep();"
						value="<primo:label code="Save"/>" class="flat" />
					<input type="button" id="insuranceCompanyRepDialogCancel" value="<primo:label code="Cancel"/>" class="flat"
						onClick="javascript:closeInsuranceCompanyRepDialog();" />
				</td>
			</tr>
		</table>
	</div>
</div>

<div id="incidentTypeDialog" title="Add Incident Type" style="display:none;">
	<div id="incidentTypeDialogBody">
		<div id="incidentTypeDialogMessage">
     		<div id="incidentTypeDialogErrorMessage" style="color:red; font-size:14px; vertical-align:center;"></div>
     		<div id="incidentTypeDialogSuccessMessage" style="color:green; font-size:14px; vertical-align:center;"></div>
     	</div>
		<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
			<tr>
				<td class="form-left"><primo:label code="Incident Type" /><span class="errorMessage">*</span></td>
				<td align="${left}">
					<input type="text" id="incidentTypeDialogIncidentType" style="min-width:300px; max-width:300px" class="flat">
				</td>
			</tr>
			<tr><td colspan="2"></td></tr>
			<tr>
				<td>&nbsp;</td>
				<td align="${left}" colspan="2">
					<input type="button" id="incidentTypeDialogCreate" onclick="javascript:saveIncidentType();"
						value="<primo:label code="Save"/>" class="flat" />
					<input type="button" id="incidentTypeRepDialogCancel" value="<primo:label code="Cancel"/>" class="flat"
						onClick="javascript:closeIncidentTypeDialog();" />
				</td>
			</tr>
		</table>
	</div>
</div>

<div id="injuryToDialog" title="Add Injury To" style="display:none;">
	<div id="injuryToDialogBody">
		<div id="injuryToDialogMessage">
     		<div id="injuryToDialogErrorMessage" style="color:red; font-size:14px; vertical-align:center;"></div>
     		<div id="injuryToDialogSuccessMessage" style="color:green; font-size:14px; vertical-align:center;"></div>
     	</div>
		<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
			<tr>
				<td class="form-left"><primo:label code="Injury To" /><span class="errorMessage">*</span></td>
				<td align="${left}">
					<input type="text" id="injuryToDialogInjuryTo" style="min-width:300px; max-width:300px" class="flat">
				</td>
			</tr>
			<tr><td colspan="2"></td></tr>
			<tr>
				<td>&nbsp;</td>
				<td align="${left}" colspan="2">
					<input type="button" id="injuryToDialogCreate" onclick="javascript:saveInjuryTo();"
						value="<primo:label code="Save"/>" class="flat" />
					<input type="button" id="injuryToDialogCancel" value="<primo:label code="Cancel"/>" class="flat"
						onClick="javascript:closeInjuryToDialog();" />
				</td>
			</tr>
		</table>
	</div>
</div>

