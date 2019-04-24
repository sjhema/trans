<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
var submitting = false;
function submitForm(){
	if(submitting) {
    	alert('Please wait a moment...');
    } else{
         submitting = true;
         document.forms["citationForm"].submit();
	}
}

function retrieveDriverCompTerm() {
	var driverId = $('#driver').val();
	if (driverId == "") {
		return;
	}
	
	$.ajax({
  		url: "${ctx}/admin/citation/citationmaint/ajax.do?action=retrieveDriverCompTerm&driverId=" + driverId,
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
</script>
<h3>
	<primo:label code="Add/Update Citation" />
</h3>
<form:form action="save.do" name="citationForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Citation" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Citation No" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="citationNo" style="min-width:162px; max-width:162px" cssClass="flat" />
				<br><form:errors path="citationNo" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Citation Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input id="datepicker" path="incidentDate" style="min-width:158px; max-width:158px" 
					cssClass="flat" size="10" onblur="return formatDate('datepicker');" />
				<br><form:errors path="incidentDate" cssClass="errorMessage" />
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
				<br><form:errors path="driver" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Company" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="company" id="company" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name"/>
				</form:select> 
				<br><form:errors path="company" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Truck" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="truck.unitNum" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${trucks}" itemValue="unitNum" itemLabel="unitNum"/>
				</form:select> 
				<br><form:errors path="truck.unitNum" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Trailer" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="trailer.unitNum" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${trailers}" itemValue="unitNum" itemLabel="unitNum"/>
				</form:select> 
				<br><form:errors path="trailer.unitNum" cssClass="errorMessage" />
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

