<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function searchReport() {
	var transactionDateFrom = document.getElementById('transactionDateFrom').value;
	var transactionDateTo = document.getElementById('transactionDateTo').value;
	if (transactionDateFrom == '' || transactionDateTo == '') {
		alert("Please select Transaction Date range and Company or Driver");
		return false;
	}
	
	var company = document.getElementById('company').value;
	var driver = document.getElementById('driver').value;
	if (company == '' && driver == '') {
		alert("Please select Transaction Date range and Company or Driver");
		return false;
	}
	
	var driverLogReportForm = document.getElementsByName('driverLogReportForm');
	driverLogReportForm[0].submit();
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

<br/>
<%-- <h3><primo:label code="Driver Log Report" /></h3> --%>
<form:form action="search.do" name="driverLogReportForm" method="post" commandName="modelObject">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Driver Log Report" /></b></td>
	    </tr>
	   	<tr>
		  	<td class="form-left"><primo:label code="Company" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="company" cssStyle="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br> <form:errors path="company" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Terminal" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="terminal" cssStyle="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select>
				<br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>
		</tr>
	    <tr>
		    <td class="form-left"><primo:label code="Driver" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="driver" cssStyle="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${drivers}" itemValue="fullName" itemLabel="fullName" />
				</form:select>
				<br> <form:errors path="driver" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
            <td class="form-left"><primo:label code="Transaction Date" /><span class="errorMessage">*</span></td>
			<td  align="${left}">
				From:<form:input size="10" path="transactionDateFrom" cssClass="flat"  onblur="return formatDate('transactionDateFrom');"/> 
					<script type="text/javascript">
						$(function() {
						$("#transactionDateFrom").datepicker({
							dateFormat:'mm-dd-yy',
			            	changeMonth: true,
			    			changeYear: true
			    		});
						});
					</script>
				To:<form:input size="10" path="transactionDateTo" cssClass="flat"  onblur="return formatDate('transactionDateTo');"/>
					<script type="text/javascript">
						$(function() {
						$("#transactionDateTo").datepicker({
							dateFormat:'mm-dd-yy',
			            	changeMonth: true,
			    			changeYear: true
			    		});
						});
					</script>
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3">
				<input type="button" onclick="javascript:searchReport()" value="Preview" />
			</td>
		</tr>
	</table>
</form:form>
 

