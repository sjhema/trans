<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	var submitting = false;
</script>
<script language="javascript">
	function searchReport() {
		var d1 = document.getElementById('fromDate').value;
		if (d1 != null && d1 != '' && !isValidDate(d1)) {
			alert("Invalid billBatch date");
		}
		var d6 = document.getElementById('toDate').value;
		if (d6 != null && d6 != '' && !isValidDate(d6)) {
			alert("Invalid billBatch date");
		}
		var d2 = document.getElementById('fromloadDate').value;
		if (d2 != null && d2 != '' && !isValidDate(d2)) {
			alert("Invalid from loaddate");
		}
		var d3 = document.getElementById('toloadDate').value;
		if (d3 != null && d3 != '' && !isValidDate(d3)) {
			alert("Invalid to loaddate");
		}
		var d4 = document.getElementById('fromunloadDate').value;
		if (d4 != null && d4 != '' && !isValidDate(d4)) {
			alert("Invalid from unloadDate");
		}
		var d5 = document.getElementById('tounloadDate').value;
		if (d5 != null && d5 != '' && !isValidDate(d5)) {
			alert("Invalid to unloadDate");
		}
		var d7 = document.getElementById('invoiceDate').value;
		if (d7 != null && d7 != '' && !isValidDate(d7)) {
			alert("Invalid invoice date");
		}
		document.forms[0].target = "reportData";
		document.forms[0].submit();
	}

	function getNote() {
		document.getElementById("notes").value = "";
		var selectedtransfer = document.getElementById('origin');
		var origin = selectedtransfer.options[selectedtransfer.selectedIndex].value;
		var selectedlandfill = document.getElementById('destination');
		var destination = selectedlandfill.options[selectedlandfill.selectedIndex].value;

		var fromDate = document.getElementById('fromDate').value;
		var toDate = document.getElementById('toDate').value;

		var fromloadDate = document.getElementById('fromloadDate').value;
		var toloadDate = document.getElementById('toloadDate').value;

		var fromunloadDate = document.getElementById('fromunloadDate').value;
		var tounloadDate = document.getElementById('tounloadDate').value;

		if (origin != "" && destination == "") {
			getDestinationLoad();
		}
		if (destination != "" && origin == "") {
			getOriginLoad();
		}
		jQuery
				.ajax({
					url : '${ctx}/reportuser/report/billing/ajax.do?action=findNote&origin='
							+ origin
							+ '&destination='
							+ destination
							+ '&fromDate='
							+ fromDate
							+ '&toDate='
							+ toDate
							+ '&fromloadDate='
							+ fromloadDate
							+ '&toloadDate='
							+ toloadDate
							+ '&fromunloadDate='
							+ fromunloadDate
							+ '&tounloadDate=' + tounloadDate,
					success : function(data) {
						var listData = jQuery.parseJSON(data);
						document.getElementById("notes").innerHTML = listData;
					}
				});
	}

	function getDestinationLoad() {
		var selectedtransfer = document.getElementById('origin');
		var origin = selectedtransfer.options[selectedtransfer.selectedIndex].value;
		jQuery
				.ajax({
					url : '${ctx}/reportuser/report/billing/ajax.do?action=findDestinationLoad&origin='
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

	function getOriginLoad() {
		var selectedlandfill = document.getElementById('destination');
		var destination = selectedlandfill.options[selectedlandfill.selectedIndex].value;
		jQuery
				.ajax({
					url : '${ctx}/reportuser/report/billing/ajax.do?action=findDOriginLoad&destination='
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
<form:form action="search.do" method="post" name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b>Invoicing</b>
			</td>
		</tr>
		<tr>
			<td class="form-left">Origin</td>
			<td align="${left}"><select name="origin" id="origin"
				onchange="javascript:getNote()" style="width: 130px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${origins}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
			</select></td>
			
			<td align="${left}" class="first">Destination</td>
			<td align="${left}"><select name="destination" id="destination"
				onchange="javascript:getNote()" style="width: 130px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${destinations}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
			</select></td>
			<td align="${left}" class="first">Invoice No.</td>
			<td align="${left}"><input name="invoiceNumber"
				id="invoiceNumber" size="15" value="" /></td>
			<td class="first"><label>Invoice Date</label>
			</td>
			<td><input name="invoiceDate" type="text" id="invoiceDate"
				size="15"
				value="${sessionScope.searchCriteria.searchMap.invoiceDate}"
				onblur="javascript:formatReportDate('invoiceDate');" /> <script
					type="text/javascript">
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
			<td align="${left}" class="first"><label>From Batch Date</label>
			</td>
			<td align="${left}"><input name="fromDate" type="text"
				id="fromDate" size="15"
				value="${sessionScope.searchCriteria.searchMap.fromDate}"
				onchange="javascript:getNote()"
				onblur="javascript:formatReportDate('fromDate');" /> <script
					type="text/javascript">
					$(function() {
						$("#fromDate").datepicker({
							dateFormat : 'mm-dd-yy',
							changeMonth : true,
							changeYear : true
						});
					});
				</script>
			</td>
			<td align="${left}" class="first"><label>To Batch Date</label>
			</td>
			<td align="${left}"><input name="toDate" type="text" id="toDate"
				size="15" value="${sessionScope.searchCriteria.searchMap.toDate}"
				onchange="javascript:getNote()"
				onblur="javascript:formatReportDate('toDate');" /> <script
					type="text/javascript">
					$(function() {
						$("#toDate").datepicker({
							dateFormat : 'mm-dd-yy',
							changeMonth : true,
							changeYear : true
						});
					});
				</script>
			</td>
			<td align="${left}" class="first"><label>From Load Date</label>
			</td>
			<td align="${left}"><input name="fromloadDate" type="text"
				id="fromloadDate" size="15" 
				onchange="javascript:getNote()"
				value="${sessionScope.searchCriteria.searchMap.fromloadDate}"
				onblur="javascript:formatReportDate('fromloadDate');" /> <script
					type="text/javascript">
					$(function() {
						$("#fromloadDate").datepicker({
							dateFormat : 'mm-dd-yy',
							changeMonth : true,
							changeYear : true
						});
					});
				</script>
			</td>
			<td align="${left}" class="first"><label>To Load Date</label>
			</td>
			<td align="${left}"><input name="toloadDate" type="text"
				id="toloadDate" 
				onchange="javascript:getNote()" size="15"
				value="${sessionScope.searchCriteria.searchMap.toloadDate}"
				onblur="javascript:formatReportDate('toloadDate');" /> <script
					type="text/javascript">
					$(function() {
						$("#toloadDate").datepicker({
							dateFormat : 'mm-dd-yy',
							changeMonth : true,
							changeYear : true
						});
					});
				</script>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><label>From UnLoad
					Date</label>
			</td>
			<td align="${left}"><input name="fromunloadDate" type="text"
				id="fromunloadDate" 
				onchange="javascript:getNote()" size="15"
				value="${sessionScope.searchCriteria.searchMap.fromunloadDate}"
				onblur="javascript:formatReportDate('fromunloadDate');" /> <script
					type="text/javascript">
					$(function() {
						$("#fromunloadDate").datepicker({
							dateFormat : 'mm-dd-yy',
							changeMonth : true,
							changeYear : true
						});
					});
				</script>
			</td>
			<td align="${left}" class="first"><label>To UnLoad Date</label>
			</td>
			<td align="${left}"><input name="tounloadDate" type="text"
				id="tounloadDate" size="15"
				value="${sessionScope.searchCriteria.searchMap.tounloadDate}"
				onchange="javascript:getNote()"
				onblur="javascript:formatReportDate('tounloadDate');" /> <script
					type="text/javascript">
					$(function() {
						$("#tounloadDate").datepicker({
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
			<td align="${left}" colspan="3"><input type="button"
				onclick="javascript:searchReport()" value="Preview" />
			</td>
		</tr>
		<tr>
			<td colspan="4" align="center"><strong><span id="notes"
					style="color: red"></span>
			</strong>
			</td>
		</tr>
	</table>
</form:form>
<table width="100%">
	<tr>
		<td align="${left}" width="100%" align="right"><a
			href="${ctx}/reportuser/report/billing/export.do?type=pdf"
			target="reportData"><img src="${ctx}/images/pdf.png" border="0"
				class="toolbarButton" />
		</a> <a href="${ctx}/reportuser/report/billing/export.do?type=xls"
			target="reportData"><img src="${ctx}/images/excel.png" border="0"
				class="toolbarButton" />
		</a> <a href="${ctx}/reportuser/report/billing/export.do?type=csv"
			target="reportData"><img src="${ctx}/images/csv.png" border="0"
				class="toolbarButton" />
		</a> <a href="${ctx}/reportuser/report/billing/save.do"
			target="reportData" name="myButton"
			onclick=""><img
				src="${ctx}/images/save.png" border="0" class="toolbarButton" />
		</a> <a href="${ctx}/reportuser/report/billing/export.do?type=print"
			target="reportData"><img src="${ctx}/images/print.png" border="0"
				class="toolbarButton" />
		</a></td>
	</tr>
	<tr>
		<td align="${left}" width="100%" valign="top"><iframe
				src="${ctx}/trans/blank.jsp" width="100%" height="600"
				name="reportData" frameborder="0"></iframe>
		</td>
	</tr>
</table>
