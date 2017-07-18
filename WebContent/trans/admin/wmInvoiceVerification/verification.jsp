<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	var controllerPath = "${ctx}/admin/wmInvoiceVerification";
	
	function submitForm() {
		var form = document.getElementById('verificationForm');
		
		form.target = "reportData";
		form.submit();
	}
	
	function processExport(elem, type) {
		var reportCtx = $('#reportCtx').val();
		if (reportCtx == '') {
			return false;
		}
		
		var href = controllerPath + "/export.do";
		var hrefParams = "?type=" + type + "&reportCtx=" + reportCtx;
		href += hrefParams;
		elem.href = href;
	}
	
	function processHoldTickets(elem) {
		return processSave(elem, 'HOLD_TICKETS');
	}
	
	function processSave(elem, action) {
		var reportCtx = $('#reportCtx').val();
		if (reportCtx != 'wmInvoiceMissingTicketsInWM') {
			alert("No missing tickets available to put on hold");
			return false;
		}
		
		if (!validate()) {
			return false;
		}
		
		var response = confirm("Do you wnat to put the missing tickets on HOLD?")
		if (response == false) {
			return false;
		}
		
		var href = controllerPath + "/save.do";
		var hrefParams = "?action=" + action + "&reportCtx=" + reportCtx;
		href += hrefParams;
		elem.href = href;
	}
	
	function generateWMInvoiceMissingTicketsInWMReport() {
		return generateWMInvoiceTicketsReport('wmInvoiceMissingTicketsInWM');
	}
	
	function generateWMInvoiceMissingTicketsReport() {
		return generateWMInvoiceTicketsReport('wmInvoiceMissingTickets');
	}
	
	function generateWMInvoiceTicketsDiscrepancyReport() {
		return generateWMInvoiceTicketsReport('wmInvoiceTicketsDiscrepancy');
	}
	
	function generateWMInvoiceTicketsMatchingReport() {
		return generateWMInvoiceTicketsReport('wmInvoiceMatchingTickets');
	}
	
	function generateWMInvoiceTicketsReport(reportCtx) {
		if (!validate()) {
			return false;
		}
		
		$('#reportCtx').val(reportCtx);
		$('#wmInvoiceTicketsToolbar').show();
		
		submitForm();
	}
	
	function validate() {
		var valid = true;
		
		var invoiceDate = document.getElementById('invoiceDate').value;
		if (invoiceDate != null && invoiceDate != '' && !isValidDate(invoiceDate)) {
			valid = false;
			alert("Invalid invoice date");
		}
		
		var fromBatchDate = document.getElementById('fromBatchDate').value;
		if (fromBatchDate != null && fromBatchDate != '' && !isValidDate(fromBatchDate)) {
			valid = false;
			alert("Invalid from bill batch date");
		}
		var toBatchDate = document.getElementById('toBatchDate').value;
		if (toBatchDate != null && toBatchDate != '' && !isValidDate(toBatchDate)) {
			valid = false;
			alert("Invalid to bill batch date");
		}
		
		var fromLoadDate = document.getElementById('fromLoadDate').value;
		if (fromLoadDate != null && fromLoadDate != '' && !isValidDate(fromLoadDate)) {
			valid = false;
			alert("Invalid from load date");
		}
		var toLoadDate = document.getElementById('toLoadDate').value;
		if (toLoadDate != null && toLoadDate != '' && !isValidDate(toLoadDate)) {
			valid = false;
			alert("Invalid to load date");
		}
		
		var fromUnloadDate = document.getElementById('fromUnloadDate').value;
		if (fromUnloadDate != null && fromUnloadDate != '' && !isValidDate(fromUnloadDate)) {
			valid = false;
			alert("Invalid from unload date");
		}
		var toUnloadDate = document.getElementById('toUnloadDate').value;
		if (toUnloadDate != null && toUnloadDate != '' && !isValidDate(toUnloadDate)) {
			valid = false;
			alert("Invalid to unload date");
		}
		
		if (((fromLoadDate != null && fromLoadDate != '') || (toLoadDate != null && toLoadDate != ''))
			&& ((fromUnloadDate != null && fromUnloadDate != '') || (toUnloadDate != null && toUnloadDate != ''))) {
			valid = false;
			alert("Enter only one of either load date range OR unload date range");
		}
		
		if ((fromLoadDate == null || fromLoadDate == '' || toLoadDate == null || toLoadDate == '')
			&& (fromUnloadDate == null || fromUnloadDate == '' || toUnloadDate == null || toUnloadDate == '')) {
			valid = false;
			alert("Enter load date range OR unload date range");
		}
		
		/*if (!validateLocations()) {
			valid = false;
			alert("Please choose origin and destination");
		}*/
		
		return valid;
	}
	
	function validateLocations() {
		var valid = true;
		
		var selectedTransfer = document.getElementById('origin');
		var origin = selectedTransfer.options[selectedTransfer.selectedIndex].value;
		var selectedLandfill = document.getElementById('destination');
		var destination = selectedLandfill.options[selectedLandfill.selectedIndex].value;
		
		if (origin == "" || destination == "") {
			valid = false;
		}
		
		return valid;
	}

	function populateLocations() {
		var selectedTransfer = document.getElementById('origin');
		var origin = selectedTransfer.options[selectedTransfer.selectedIndex].value;
		var selectedLandfill = document.getElementById('destination');
		var destination = selectedLandfill.options[selectedLandfill.selectedIndex].value;
		
		if (origin != "" && destination == "") {
			populateDestinations();
		}
		if (destination != "" && origin == "") {
			populateOrigins();
		}
	}

	function populateDestinations() {
		var selectedtransfer = document.getElementById('origin');
		var origin = selectedtransfer.options[selectedtransfer.selectedIndex].value;
		jQuery
				.ajax({
					url : '${ctx}/admin/wmInvoiceVerification/ajax.do?action=findDestinations&origin='
							+ origin,
					success : function(data) {
						var listData = jQuery.parseJSON(data);
						var options = '<option value="">Please Select</option>';
						for ( var i = 0; i < listData.length; i++) {
							var destination = listData[i];
							options += '<option value="'+destination.id+'">'
									+ destination.name + '</option>';
						}
						$("#destination").html(options);
					}
				});
	}

	function populateOrigins() {
		var selectedlandfill = document.getElementById('destination');
		var destination = selectedlandfill.options[selectedlandfill.selectedIndex].value;
		jQuery
				.ajax({
					url : '${ctx}/admin/wmInvoiceVerification/ajax.do?action=findOrigins&destination='
							+ destination,
					success : function(data) {
						var listData = jQuery.parseJSON(data);
						var options = '<option value="">Please Select</option>';
						for ( var i = 0; i < listData.length; i++) {
							var origin = listData[i];
							options += '<option value="'+origin.id+'">'
									+ origin.name + '</option>';
						}
						$("#origin").html(options);
					}
				});
	}
</script>
<br/>
<form:form action="search.do" method="post" name="verificationForm" id="verificationForm">
	<input id="reportCtx" name="reportCtx" type="hidden" value=""/>
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b>WM Invoice Ticket Verification</b>
			</td>
		</tr>
		<tr>
			<td class="form-left">Origin</td>
			<td align="${left}">
				<select name="origin" id="origin" onchange="javascript:populateLocations()" style="width: 130px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${origins}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first">Destination</td>
			<td align="${left}">
				<select name="destination" id="destination" onchange="javascript:populateLocations()" style="width: 130px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${destinations}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first">Invoice No.</td>
			<td align="${left}">
				<input name="invoiceNumber" id="invoiceNumber" size="15" value="" disabled/>
			</td>
			<td class="first"><label>Invoice Date</label></td>
			<td>
				<input name="invoiceDate" type="text" id="invoiceDate" size="15" disabled
					value="${sessionScope.searchCriteria.searchMap.invoiceDate}"
					onblur="javascript:formatReportDate('invoiceDate');" /> 
					<script type="text/javascript">
						$(function() {
							$("#invoiceDate").datepicker({
								dateFormat : 'mm-dd-yy',
								changeMonth : true,
								changeYear : true
							});
						});
					</script>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><label>From Batch Date</label></td>
			<td align="${left}">
				<input name="fromBatchDate" type="text" id="fromBatchDate" size="15" disabled
					value="${sessionScope.searchCriteria.searchMap.fromBatchDate}"
					onblur="javascript:formatReportDate('fromBatchDate');" /> 
					<script type="text/javascript">
						$(function() {
							$("#fromBatchDate").datepicker({
								dateFormat : 'mm-dd-yy',
								changeMonth : true,
								changeYear : true
							});
						});
					</script>
			</td>
			<td align="${left}" class="first"><label>To Batch Date</label></td>
			<td align="${left}">
				<input name="toBatchDate" type="text" id="toBatchDate"
					size="15" value="${sessionScope.searchCriteria.searchMap.toBatchDate}" disabled
					onblur="javascript:formatReportDate('toBatchDate');" /> 
					<script
						type="text/javascript">
						$(function() {
							$("#toBatchDate").datepicker({
								dateFormat : 'mm-dd-yy',
								changeMonth : true,
								changeYear : true
							});
						});
					</script>
			</td>
			<td align="${left}" class="first"><label>From Load Date</label></td>
			<td align="${left}">
				<input name="fromLoadDate" type="text" id="fromLoadDate" size="15" 
					value="${sessionScope.searchCriteria.searchMap.fromLoadDate}"
					onblur="javascript:formatReportDate('fromLoadDate');" /> 
					<script
						type="text/javascript">
						$(function() {
							$("#fromLoadDate").datepicker({
								dateFormat : 'mm-dd-yy',
								changeMonth : true,
								changeYear : true
							});
						});
					</script>
			</td>
			<td align="${left}" class="first"><label>To Load Date</label></td>
			<td align="${left}">
				<input name="toLoadDate" type="text" id="toLoadDate" 
					value="${sessionScope.searchCriteria.searchMap.toLoadDate}"
					onblur="javascript:formatReportDate('toLoadDate');" /> 
					<script
						type="text/javascript">
						$(function() {
							$("#toLoadDate").datepicker({
								dateFormat : 'mm-dd-yy',
								changeMonth : true,
								changeYear : true
							});
						});
					</script>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><label>From Unload Date</label></td>
			<td align="${left}">
				<input name="fromUnloadDate" type="text" id="fromUnloadDate" 
				value="${sessionScope.searchCriteria.searchMap.fromUnloadDate}"
				onblur="javascript:formatReportDate('fromUnloadDate');" /> 
				<script
					type="text/javascript">
					$(function() {
						$("#fromUnloadDate").datepicker({
							dateFormat : 'mm-dd-yy',
							changeMonth : true,
							changeYear : true
						});
					});
				</script>
			</td>
			<td align="${left}" class="first"><label>To Unload Date</label></td>
			<td align="${left}">
				<input name="toUnloadDate" type="text" id="toUnloadDate" size="15"
					value="${sessionScope.searchCriteria.searchMap.toUnloadDate}"
					onblur="javascript:formatReportDate('toUnloadDate');" /> 
					<script type="text/javascript">
						$(function() {
							$("#toUnloadDate").datepicker({
								dateFormat : 'mm-dd-yy',
								changeMonth : true,
								changeYear : true
							});
						});
					</script>
			</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="6">
				<input type="button" onclick="javascript:generateWMInvoiceMissingTicketsInWMReport();" value="Tickets missing in WM invoicing " />
				&nbsp;&nbsp;&nbsp;
				<input type="button" onclick="javascript:generateWMInvoiceMissingTicketsReport();" value="Tickets missing in our system" />
				&nbsp;&nbsp;&nbsp;
				<input type="button" onclick="javascript:generateWMInvoiceTicketsMatchingReport();" value="Matching tickets" />
				&nbsp;&nbsp;&nbsp;
				<input type="button" disabled onclick="javascript:generateWMInvoiceTicketsDiscrepancyReport();" value="Discrepancy Report" />
			</td>
		</tr>
	</table>
</form:form>
<table width="100%">
	<tr>
		<td align="${left}" width="100%">
			<div id="wmInvoiceTicketsToolbar" style="display:none;">
				<a href="" onclick="return processExport(this, 'pdf');" target="reportData">
					<img src="${ctx}/images/pdf.png" border="0" class="toolbarButton" />
				</a> 
				<a href="" onclick="return processExport(this, 'xls');" target="reportData">
					<img src="${ctx}/images/excel.png" border="0" class="toolbarButton" />
				</a> 
				<a href="" onclick="return processExport(this, 'csv');" target="reportData">
					<img src="${ctx}/images/csv.png" border="0" class="toolbarButton" />
				</a> 
				<a href="" onclick="return processExport(this, 'print');" target="reportData">
					<img src="${ctx}/images/print.png" border="0" class="toolbarButton" />
				</a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="" onclick="return processHoldTickets(this);" target="reportData"
					style="vertical-align: top; font-size: 15px">
					HOLD
				</a>
			</div>
		</td>
	</tr>
	<tr>
		<td align="${left}" width="100%" valign="top">
			<iframe src="${ctx}/trans/blank.jsp" width="100%" height="600" name="reportData" frameborder="0"></iframe>
		</td>
	</tr>
</table>
