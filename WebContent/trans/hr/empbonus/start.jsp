<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function searchReport() {
	var d1 = document.getElementById('fromDate').value;
	if (d1 != null && d1 != '' && !isValidDate(d1)) {
		alert("Invalid billBatch date");
	}
	var d6 = document.getElementById('toDate').value;
	if (d6 != null && d6 != '' && !isValidDate(d6)) {
		alert("Invalid billBatch date");
	}
	
	document.forms[0].submit();
}
function getComTerm(){
	var employeselected=document.getElementById("empId");
	var employee=employeselected.options[employeselected.selectedIndex].value;
  	jQuery.ajax({
		url:'${ctx}/hr/empbonus/ajax.do?action=findDCompany&driver='+employee, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			if(employee==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var company=listData[i];
				options += '<option value="'+company.id+'">'+company.name+'</option>';
				}
			$("#companyid").html(options);
		}
			
		}); 
  	
  	jQuery.ajax({
		url:'${ctx}/hr/empbonus/ajax.do?action=findDTerminal&driver='+employee, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			if(employee==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var terminal=listData[i];
				options += '<option value="'+terminal.id+'">'+terminal.name+'</option>';
				}
			$("#terminalid").html(options);
		}
			
		}); 
} 
</script>
<h3>
	<primo:label code="Employee Bonus" />
</h3>
<form:form action="addEmpBonus.do" method="post" name="searchForm">
	<table width="100%" id="form-table" cellspacing="1" cellpadding="5" >
	<tr class="table-heading">
			<td align="${left}" colspan="9"><b>Employee Bonus</b>
			</td>
		</tr>
<tr>
		<td align="${left}" class="first">Company</td>
			<td align="${left}"><select name="company" id="companyid"
				 style="min-width:154px; max-width:154px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${companies}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
			</select></td>
			<td align="${left}" class="first">Terminal</td>
			<td align="${left}"><select name="terminal" id="terminalid"
				style="min-width:154px; max-width:154px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${terminals}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
			</select></td>
</tr>
<tr>
	<td align="${left}" class="first">Employee</td>
			<td align="${left}"><select name="driver" id="empId"
				 style="min-width:154px; max-width:154px" onchange="javascript:getComTerm()">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${employees}">
						<option value="${item.fullName}">${item.fullName}</option>
					</c:forEach>
			</select></td>
			<td class="first"></td>
	
</tr>
<tr> 
 <td align="${left}" class="first"><label>From Batch Date</label>
			</td>
			<td align="${left}"><input name="fromDate" type="text" style="min-width:150px; max-width:150px"
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
			<td align="${left}"><input name="toDate" type="text" id="toDate" style="min-width:150px; max-width:150px"
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
</tr>
	<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3"><input type="button"
				onclick="javascript:searchReport()" value="Start" />
			</td>
		</tr>
</table>		
	</form:form>