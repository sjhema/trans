<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
var submitting = false;
function submitForm(){
	if(submitting) {
    	alert('Please wait a moment...');
    } else{
         submitting = true;
         document.forms["accidentForm"].submit();
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
  		url: "${ctx}/admin/accident/accidentmaint/ajax.do?action=retrieveClaimReps&insuranceCompanyId=" + insuranceCompanyId,
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

function retrieveDriverCompTerm() {
	var driverId = $('#driver').val();
	if (driverId == "") {
		return;
	}
	
	$.ajax({
  		url: "${ctx}/admin/accident/accidentmaint/ajax.do?action=retrieveDriverCompTerm&driverId=" + driverId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData != '') {
	       		var compTermTokens = responseData.split("|");
	       		var driverCompanySel = $("#driverCompany");
	       		driverCompanySel.val(compTermTokens[0]);
	       		var driverTerminalSel = $("#driverTerminal");
	       		driverTerminalSel.val(compTermTokens[1]);
       		}
		}
	});
}

function saveAccidentCause() {
	clearAccidentCauseDialogMsgs();
	
	var accidentCauseSel = $('#accidentCause');
	
	var accidentCause = $('#accidentCauseDialogAccidentCause').val();
	
	var accidentCauseDialogErrorMessageElem = $('#accidentCauseDialogErrorMessage');
	$.ajax({
  		url: "${ctx}/admin/accident/accidentmaint/ajax.do?action=saveAccidentCause" + "&accidentCause=" + accidentCause,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData.indexOf("success") == -1) {
       			accidentCauseDialogErrorMessageElem.html(responseData);
       		} else {
       			var id = responseData.substring(responseData.indexOf(":") + 1);
       			var option = "<option value='" + id + "'>" + accidentCause + "</option>";
       			accidentCauseSel.append(option);
       			accidentCauseSel.val(id);
       			
       			closeAccidentCauseDialog();
       		}
		}
	});
}

function clearAccidentCauseDialog() {
	$('#accidentCauseDialogAccidentCause').val("");
	
	clearAccidentCauseDialogMsgs();
}

function clearAccidentCauseDialogMsgs() {
	$('#accidentCauseDialogErrorMessage').html("");
	$('#accidentCauseDialogSuccessMessage').html("");
}

function openAccidentCauseDialog() {
	clearAccidentCauseDialog();
	$('#accidentCauseDialog').dialog({ 
		 modal: true,
         bgiframe: true,
         width: 500,
         resizable: false
     });
}

function closeAccidentCauseDialog() {
	clearAccidentCauseDialog();
	$('#accidentCauseDialog').dialog('close');
}

function saveAccidentWeather() {
	clearAccidentWeatherDialogMsgs();
	
	var accidentWeatherSel = $('#weather');
	
	var accidentWeather = $('#accidentWeatherDialogAccidentWeather').val();
	
	var accidentWeatherDialogErrorMessageElem = $('#accidentWeatherDialogErrorMessage');
	
	$.ajax({
  		url: "${ctx}/admin/accident/accidentmaint/ajax.do?action=saveAccidentWeather" + "&accidentWeather=" + accidentWeather,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData.indexOf("success") == -1) {
       			accidentWeatherDialogErrorMessageElem.html(responseData);
       		} else {
       			var id = responseData.substring(responseData.indexOf(":") + 1);
       			var option = "<option value='" + id + "'>" + accidentWeather + "</option>";
       			accidentWeatherSel.append(option);
       			accidentWeatherSel.val(id);
       			
       			closeAccidentWeatherDialog();
       		}
		}
	});
}

function clearAccidentWeatherDialog() {
	$('#accidentWeatherDialogAccidentWeather').val("");
	
	clearAccidentWeatherDialogMsgs();
}

function clearAccidentWeatherDialogMsgs() {
	$('#accidentWeatherDialogErrorMessage').html("");
	$('#accidentWeatherDialogSuccessMessage').html("");
}

function openAccidentWeatherDialog() {
	clearAccidentWeatherDialog();
	$('#accidentWeatherDialog').dialog({ 
		 modal: true,
         bgiframe: true,
         width: 500,
         resizable: false
     });
}

function closeAccidentWeatherDialog() {
	clearAccidentWeatherDialog();
	$('#accidentWeatherDialog').dialog('close');
}

function saveAccidentType() {
	clearAccidentTypeDialogMsgs();
	
	var accidentTypeSel = $('#accidentType');
	
	var accidentType = $('#accidentTypeDialogAccidentType').val();
	
	var accidentTypeDialogErrorMessageElem = $('#accidentTypeDialogErrorMessage');
	
	$.ajax({
  		url: "${ctx}/admin/accident/accidentmaint/ajax.do?action=saveAccidentType" + "&accidentType=" + accidentType,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData.indexOf("success") == -1) {
       			accidentTypeDialogErrorMessageElem.html(responseData);
       		} else {
       			var id = responseData.substring(responseData.indexOf(":") + 1);
       			var option = "<option value='" + id + "'>" + accidentType + "</option>";
       			accidentTypeSel.append(option);
       			accidentTypeSel.val(id);
       			
       			closeAccidentTypeDialog();
       		}
		}
	});
}

function clearAccidentTypeDialog() {
	$('#accidentTypeDialogAccidentType').val("");
	
	clearAccidentTypeDialogMsgs();
}

function clearAccidentTypeDialogMsgs() {
	$('#accidentTypeDialogErrorMessage').html("");
	$('#accidentTypeDialogSuccessMessage').html("");
}

function openAccidentTypeDialog() {
	clearAccidentTypeDialog();
	$('#accidentTypeDialog').dialog({ 
		 modal: true,
         bgiframe: true,
         width: 500,
         resizable: false
     });
}

function closeAccidentTypeDialog() {
	clearAccidentTypeDialog();
	$('#accidentTypeDialog').dialog('close');
}

function saveAccidentRoadCondition() {
	clearAccidentRoadConditionDialogMsgs();
	
	var accidentRoadConditionSel = $('#roadCondition');
	
	var accidentRoadCondition = $('#accidentRoadConditionDialogRoadCondition').val();
	
	var accidentRoadConditionDialogErrorMessageElem = $('#accidentRoadConditionDialogErrorMessage');
	
	$.ajax({
  		url: "${ctx}/admin/accident/accidentmaint/ajax.do?action=saveAccidentRoadCondition" + "&accidentRoadCondition=" + accidentRoadCondition,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData.indexOf("success") == -1) {
       			accidentRoadConditionDialogErrorMessageElem.html(responseData);
       		} else {
       			var id = responseData.substring(responseData.indexOf(":") + 1);
       			var option = "<option value='" + id + "'>" + accidentRoadCondition + "</option>";
       			accidentRoadConditionSel.append(option);
       			accidentRoadConditionSel.val(id);
       			
       			closeAccidentRoadConditionDialog();
       		}
		}
	});
}

function clearAccidentRoadConditionDialog() {
	$('#accidentRoadConditionDialogRoadCondition').val("");
	
	clearAccidentRoadConditionDialogMsgs();
}

function clearAccidentRoadConditionDialogMsgs() {
	$('#accidentRoadConditionDialogErrorMessage').html("");
	$('#accidentRoadConditionDialogSuccessMessage').html("");
}

function openAccidentRoadConditionDialog() {
	clearAccidentRoadConditionDialog();
	$('#accidentRoadConditionDialog').dialog({ 
		 modal: true,
         bgiframe: true,
         width: 500,
         resizable: false
     });
}

function closeAccidentRoadConditionDialog() {
	clearAccidentRoadConditionDialog();
	$('#accidentRoadConditionDialog').dialog('close');
}

function saveInsuranceCompany() {
	clearInsuranceCompanyDialogMsgs();
	
	var insuranceCompanySel = $('#insuranceCompany');
	var claimRepSel = $("#claimRep");
	
	var name = $('#insuranceCompanyDialogName').val();
	
	var insuranceCompanyDialogErrorMessageElem = $('#insuranceCompanyDialogErrorMessage');
	
	$.ajax({
  		url: "${ctx}/admin/accident/accidentmaint/ajax.do?action=saveInsuranceCompany" + "&name=" + name,
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
  		url: "${ctx}/admin/accident/accidentmaint/ajax.do?action=saveInsuranceCompanyRep" 
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
	<primo:label code="Add/Update Accidents" />
</h3>
<form:form action="save.do" name="accidentForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Accident" /></b></td>
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
			<td class="form-left"><primo:label code="Status" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="accidentStatus" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${statuses}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> 
				<br><form:errors path="accidentStatus" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Employee" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="driver" id="driver" style="min-width:166px; max-width:166px"
					onchange="javascript:retrieveDriverCompTerm();">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${drivers}" itemValue="id" itemLabel="fullName"/>
				</form:select> 
				<br> <form:errors path="driver" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Vehicle" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="vehicle.unitNum" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${vehicles}" itemValue="unitNum" itemLabel="unitNum"/>
				</form:select> 
				<br> <form:errors path="vehicle.unitNum" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Subcontractor" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="subcontractor" id="subcontractor" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${subcontractors}" itemValue="id" itemLabel="name"/>
				</form:select> 
				<br> <form:errors path="subcontractor" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Company" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="driverCompany" id="driverCompany" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name"/>
				</form:select> 
				<br> <form:errors path="driverCompany" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Terminal" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="driverTerminal" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name"/>
				</form:select> 
				<br> <form:errors path="driverTerminal" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Accident Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input id="datepicker" path="incidentDate" style="min-width:158px; max-width:158px" 
					cssClass="flat" size="10" onblur="return formatDate('datepicker');" />
				<br><form:errors path="incidentDate" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Accident Time" /></td>
			<td align="${left}">
				<form:input path="incidentTime" size="5" style="min-width:158px; max-width:158px" cssClass="flat" 
					onkeypress="return onlyNumbers(event, false)" onblur="return formatTime('incidentTime')"/>
				<br><form:errors path="incidentTime" cssClass="errorMessage" /> 
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Accident Cause" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="accidentCause" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${accidentCauses}" itemValue="id" itemLabel="cause" />
				</form:select> 
				&nbsp;
				<a href="javascript:openAccidentCauseDialog();">
					<img class="toolbarButton" src="/trans/images/add.png" style="vertical-align: top" title="Add Incident Type" border="0">
				</a>
				<br><form:errors path="accidentCause" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Accident Type" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="accidentType" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${accidentTypes}" itemValue="id" itemLabel="accidentType" />
				</form:select>
				&nbsp;
				<a href="javascript:openAccidentTypeDialog();">
					<img class="toolbarButton" src="/trans/images/add.png" style="vertical-align: top" title="Add Incident Type" border="0">
				</a>
				<br><form:errors path="accidentType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Location" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="location" style="min-width:162px; max-width:162px" cssClass="flat" />
				<br><form:errors path="location" cssClass="errorMessage" /> 
			</td>
			<td class="form-left"><primo:label code="State" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="state" id="state" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${states}" itemValue="id" itemLabel="name"/>
				</form:select> 
				<br> <form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Road Condition" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="roadCondition" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${roadConditions}" itemValue="id" itemLabel="roadCondition" />
				</form:select>
				&nbsp;
				<a href="javascript:openAccidentRoadConditionDialog();">
					<img class="toolbarButton" src="/trans/images/add.png" style="vertical-align: top" title="Add Incident Type" border="0">
				</a>
				<br><form:errors path="roadCondition" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Weather" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="weather" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${allWeather}" itemValue="id" itemLabel="weather" />
				</form:select>
				&nbsp;
				<a href="javascript:openAccidentWeatherDialog();">
					<img class="toolbarButton" src="/trans/images/add.png" style="vertical-align: top" title="Add Incident Type" border="0">
				</a>
				<br><form:errors path="weather" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Fatality" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="fatality" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:option value="Yes">Yes</form:option>
					<form:option value="No">No</form:option>
				</form:select>
				<br><form:errors path="fatality" cssClass="errorMessage" /> 
			</td>
			<td class="form-left"><primo:label code="# Injured" /></td>
			<td align="${left}">
				<form:input path="noInjured" style="min-width:162px; max-width:162px" cssClass="flat" />
				<br><form:errors path="noInjured" cssClass="errorMessage" /> 
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Vehicle Damage" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="vehicleDamage" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:option value="Yes">Yes</form:option>
					<form:option value="No">No</form:option>
				</form:select>
				<br><form:errors path="vehicleDamage" cssClass="errorMessage" /> 
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Towed" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="towed" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:option value="Yes">Yes</form:option>
					<form:option value="No">No</form:option>
				</form:select>
				<br><form:errors path="towed" cssClass="errorMessage" /> 
			</td>
			<td class="form-left"><primo:label code="Citation" /></td>
			<td align="${left}">
				<form:select cssClass="flat" path="citation" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:option value="Yes">Yes</form:option>
					<form:option value="No">No</form:option>
				</form:select>
				<br><form:errors path="citation" cssClass="errorMessage" /> 
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="HM Release" /></td>
			<td align="${left}">
				<form:select cssClass="flat" path="hmRelease" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:option value="Yes">Yes</form:option>
					<form:option value="No">No</form:option>
				</form:select>
				<br><form:errors path="hmRelease" cssClass="errorMessage" /> 
			</td>
			<td class="form-left"><primo:label code="Recordable" /></td>
			<td align="${left}">
				<form:select cssClass="flat" path="recordable" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:option value="Yes">Yes</form:option>
					<form:option value="No">No</form:option>
				</form:select>
				<br><form:errors path="recordable" cssClass="errorMessage" /> 
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Deductible" /><span class="errorMessage"></span></td>
			<td>
				<form:input path="deductible" style="min-width:162px; max-width:162px" cssClass="flat" 
					 maxlength="7" onkeypress="return onlyNumbers(event, true)"  />  
				<br><form:errors path="deductible" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Reimbursement" /><span class="errorMessage"></span></td>
			<td>
				<form:input path="reimbursement" style="min-width:162px; max-width:162px" cssClass="flat" 
					 maxlength="7" onkeypress="return onlyNumbers(event, true)"  />  
				<br><form:errors path="reimbursement" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Expense" /><span class="errorMessage"></span></td>
			<td>
				<form:input path="expense" style="min-width:162px; max-width:162px" cssClass="flat" 
					 maxlength="7" onkeypress="return onlyNumbers(event, true)"  />  
				<br><form:errors path="expense" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Reserve" /><span class="errorMessage"></span></td>
			<td>
				<form:input path="reserve" style="min-width:162px; max-width:162px" cssClass="flat" 
					 maxlength="7" onkeypress="return onlyNumbers(event, true)" />  
				<br><form:errors path="reserve" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Paid" /><span class="errorMessage"></span></td>
			<td>
				<form:input path="paid" style="min-width:162px; max-width:162px" cssClass="flat" 
					 maxlength="7" onkeypress="return onlyNumbers(event, true)" />  
				<br><form:errors path="paid" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Total Cost" /><span class="errorMessage"></span></td>
			<td>
				<form:input path="totalCost" style="min-width:162px; max-width:162px" cssClass="flat" 
					 maxlength="9" onkeypress="return onlyNumbers(event, true)" />
				<br><form:errors path="totalCost" cssClass="errorMessage" />
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

<div id="accidentCauseDialog" title="Add Accident Cause" style="display:none;">
	<div id="accidentCauseDialogBody">
		<div id="accidentCauseDialogMessage">
     		<div id="accidentCauseDialogErrorMessage" style="color:red; font-size:14px; vertical-align:center;"></div>
     		<div id="accidentCauseDialogSuccessMessage" style="color:green; font-size:14px; vertical-align:center;"></div>
     	</div>
		<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
			<tr>
				<td class="form-left"><primo:label code="Accident Cause" /><span class="errorMessage">*</span></td>
				<td align="${left}">
					<input type="text" id="accidentCauseDialogAccidentCause" style="min-width:300px; max-width:300px" class="flat">
				</td>
			</tr>
			<tr><td colspan="2"></td></tr>
			<tr>
				<td>&nbsp;</td>
				<td align="${left}" colspan="2">
					<input type="button" id="accidentCauseDialogCreate" onclick="javascript:saveAccidentCause();"
						value="<primo:label code="Save"/>" class="flat" />
					<input type="button" id="accidentCauseDialogCancel" value="<primo:label code="Cancel"/>" class="flat"
						onClick="javascript:closeAccidentCauseDialog();" />
				</td>
			</tr>
		</table>
	</div>
</div>

<div id="accidentTypeDialog" title="Accident Type" style="display:none;">
	<div id="accidentTypeDialogBody">
		<div id="accidentTypeDialogMessage">
     		<div id="accidentTypeDialogErrorMessage" style="color:red; font-size:14px; vertical-align:center;"></div>
     		<div id="accidentTypeDialogSuccessMessage" style="color:green; font-size:14px; vertical-align:center;"></div>
     	</div>
		<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
			<tr>
				<td class="form-left"><primo:label code="Accident Type" /><span class="errorMessage">*</span></td>
				<td align="${left}">
					<input type="text" id="accidentTypeDialogAccidentType" style="min-width:300px; max-width:300px" class="flat">
				</td>
			</tr>
			<tr><td colspan="2"></td></tr>
			<tr>
				<td>&nbsp;</td>
				<td align="${left}" colspan="2">
					<input type="button" id="accidentTypeDialogCreate" onclick="javascript:saveAccidentType();"
						value="<primo:label code="Save"/>" class="flat" />
					<input type="button" id="accidentTypeDialogCancel" value="<primo:label code="Cancel"/>" class="flat"
						onClick="javascript:closeAccidentTypeDialog();" />
				</td>
			</tr>
		</table>
	</div>
</div>

<div id="accidentWeatherDialog" title="Accident Weather" style="display:none;">
	<div id="accidentWeatherDialogBody">
		<div id="accidentWeatherDialogMessage">
     		<div id="accidentWeatherDialogErrorMessage" style="color:red; font-size:14px; vertical-align:center;"></div>
     		<div id="accidentWeatherDialogSuccessMessage" style="color:green; font-size:14px; vertical-align:center;"></div>
     	</div>
		<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
			<tr>
				<td class="form-left"><primo:label code="Accident Weather" /><span class="errorMessage">*</span></td>
				<td align="${left}">
					<input type="text" id="accidentWeatherDialogAccidentWeather" style="min-width:300px; max-width:300px" class="flat">
				</td>
			</tr>
			<tr><td colspan="2"></td></tr>
			<tr>
				<td>&nbsp;</td>
				<td align="${left}" colspan="2">
					<input type="button" id="accidentWeatherDialogCreate" onclick="javascript:saveAccidentWeather();"
						value="<primo:label code="Save"/>" class="flat" />
					<input type="button" id="accidentWeatherDialogCancel" value="<primo:label code="Cancel"/>" class="flat"
						onClick="javascript:closeAccidentWeatherDialog();" />
				</td>
			</tr>
		</table>
	</div>
</div>

<div id="accidentRoadConditionDialog" title="Accident Road Condition" style="display:none;">
	<div id="accidentRoadConditionDialogBody">
		<div id="accidentRoadConditionDialogMessage">
     		<div id="accidentRoadConditionDialogErrorMessage" style="color:red; font-size:14px; vertical-align:center;"></div>
     		<div id="accidentRoadConditionDialogSuccessMessage" style="color:green; font-size:14px; vertical-align:center;"></div>
     	</div>
		<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
			<tr>
				<td class="form-left"><primo:label code="Road Condition" /><span class="errorMessage">*</span></td>
				<td align="${left}">
					<input type="text" id="accidentRoadConditionDialogRoadCondition" style="min-width:300px; max-width:300px" class="flat">
				</td>
			</tr>
			<tr><td colspan="2"></td></tr>
			<tr>
				<td>&nbsp;</td>
				<td align="${left}" colspan="2">
					<input type="button" id="accidentRoadConditionDialogCreate" onclick="javascript:saveAccidentRoadCondition();"
						value="<primo:label code="Save"/>" class="flat" />
					<input type="button" id="accidentRoadConditionDialogCancel" value="<primo:label code="Cancel"/>" class="flat"
						onClick="javascript:closeAccidentRoadConditionDialog();" />
				</td>
			</tr>
		</table>
	</div>
</div>

