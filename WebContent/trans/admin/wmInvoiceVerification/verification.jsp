<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	var submitting = false;
</script>
<script language="javascript">
	function searchMissingWMInvoiceTickets() {
		var d1 = document.getElementById('fromBatchDate').value;
		if (d1 != null && d1 != '' && !isValidDate(d1)) {
			alert("Invalid from bill batch date");
		}
		var d6 = document.getElementById('toBatchDate').value;
		if (d6 != null && d6 != '' && !isValidDate(d6)) {
			alert("Invalid to bill batch date");
		}
		var d2 = document.getElementById('fromLoadDate').value;
		if (d2 != null && d2 != '' && !isValidDate(d2)) {
			alert("Invalid from load date");
		}
		var d3 = document.getElementById('toLoadDate').value;
		if (d3 != null && d3 != '' && !isValidDate(d3)) {
			alert("Invalid to load date");
		}
		var d4 = document.getElementById('fromUnloadDate').value;
		if (d4 != null && d4 != '' && !isValidDate(d4)) {
			alert("Invalid from unload date");
		}
		var d5 = document.getElementById('toUnloadDate').value;
		if (d5 != null && d5 != '' && !isValidDate(d5)) {
			alert("Invalid to unload date");
		}
		var d7 = document.getElementById('invoiceDate').value;
		if (d7 != null && d7 != '' && !isValidDate(d7)) {
			alert("Invalid invoice date");
		}
		
		document.forms[0].target = "reportData";
		document.forms[0].submit();
	}

	function populateLocations() {
		var selectedtransfer = document.getElementById('origin');
		var origin = selectedtransfer.options[selectedtransfer.selectedIndex].value;
		var selectedlandfill = document.getElementById('destination');
		var destination = selectedlandfill.options[selectedlandfill.selectedIndex].value;

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
					url : '${ctx}/admin/wmInvoiveVerification/ajax.do?action=findDestinations&origin='
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
					url : '${ctx}/admin/wmInvoiveVerification/ajax.do?action=findOrigins&destination='
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
<form:form action="search.do" method="post" name="verificationForm">
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
				<input name="fromLoadDate" type="text" id="fromLoadDate" size="15" disabled
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
				<input name="toLoadDate" type="text" id="toLoadDate" disabled
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
			<td align="${left}" colspan="3">
				<input type="button" onclick="javascript:searchMissingWMInvoiceTickets()" value="Search Missing WM Invoice Tickets" />
			</td>
		</tr>
	</table>
</form:form>
<table width="100%">
	<tr>
		<td align="${left}" width="100%" align="right">
			<a href="${ctx}/admin/wmInvoiceVerification/export.do?type=pdf" target="reportData">
				<img src="${ctx}/images/pdf.png" border="0" class="toolbarButton" />
			</a> 
			<a href="${ctx}/admin/wmInvoiceVerification/export.do?type=xls" target="reportData">
				<img src="${ctx}/images/excel.png" border="0" class="toolbarButton" />
			</a> 
			<a href="${ctx}/admin/wmInvoiceVerification/export.do?type=csv" target="reportData">
				<img src="${ctx}/images/csv.png" border="0" class="toolbarButton" />
			</a> 
			<a href="${ctx}/admin/wmInvoiceVerification/save.do" target="reportData" onclick="">
				<img src="${ctx}/images/save.png" border="0" class="toolbarButton" />
			</a> 
			<a href="${ctx}/admin/wmInvoiceVerification/export.do?type=print" target="reportData">
				<img src="${ctx}/images/print.png" border="0" class="toolbarButton" />
			</a>
		</td>
	</tr>
	<tr>
		<td align="${left}" width="100%" valign="top">
			<iframe src="${ctx}/trans/blank.jsp" width="100%" height="600" name="reportData" frameborder="0"></iframe>
		</td>
	</tr>
</table>
