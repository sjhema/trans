<%@ include file="/common/taglibs.jsp"%>

<script language="javascript">
function searchReport() {
	var c=document.getElementById("company").value;
	var d=document.getElementById("payrollDateFrom").value;
	var d1=document.getElementById("payrollDateTo").valu;
	if(c==""){
		alert("Please select company");
	}else if(d==""){
		alert("Please Enter PayRoll Batch Date from");
	}
	else if(d1==""){
		alert("Please Enter PayRoll Batch Date to");
	}else{
		
	}
	if(c!=""&&d!=""&&d1!=""){
		
	document.forms[0].target="reportData";
	document.forms[0].submit();
	}
}
</script>
<form:form action="search.do" method="post"
	name="searchForm">
	<br/>
	<br/>
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5" align="left">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b>Payroll Verification</b></td>
		</tr>
			<tr>
		<td class="first">Company</td>
			<td align="${left}">
				<select name="company" id="company"   style="min-width:154px; max-width:154px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${companies}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
		<!-- </tr>
		<tr> -->
		<td class="first">Terminal</td>
			<td align="${left}">
				<select name="terminal" id="terminal"  style="min-width:154px; max-width:154px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${terminals}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
		<td align="${left}" class="first"><label>Check Date From</label></td>
			<td align="${left}"><input name="payrollDateFrom" type="text" id="payrollDateFrom" style="min-width:154px; max-width:154px"
				value="${sessionScope.searchCriteria.searchMap.payrollDateFrom}" onblur="javascript:formatReportDate('payrollDateFrom');"/>
				<script type="text/javascript">
			$(function() {
			$("#payrollDateFrom").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>
		    
		    <td align="${left}" class="first"><label>Check Date To</label></td>
			<td align="${left}"><input name="payrollDateTo" type="text" id="payrollDateTo" style="min-width:154px; max-width:154px"
				value="${sessionScope.searchCriteria.searchMap.payrollDateTo}" onblur="javascript:formatReportDate('payrollDateTo');"/>
				<script type="text/javascript">
			$(function() {
			$("#payrollDateTo").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>
		  
		</tr>
			<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3"><input type="button"
			onclick="javascript:searchReport()" value="Preview" />
			</td>
		</tr>
	</table>
</form:form>
<table width="100%">
<tr>
<td align="${left}" width="100%" align="right">
<a href="${ctx}/hr/salaryreport/export.do?type=pdf" target="reportData"><img src="${ctx}/images/pdf.png" border="0" class="toolbarButton"/></a>
<a href="${ctx}/hr/salaryreport/export.do?type=xls" target="reportData"><img src="${ctx}/images/excel.png" border="0" class="toolbarButton"/></a>
<a href="${ctx}/hr/salaryreport/export.do?type=csv" target="reportData"><img src="${ctx}/images/csv.png" border="0" class="toolbarButton"/></a>
</td>
</tr>	
<tr><td align="${left}" width="100%" valign="top"><iframe src="${ctx}/trans/blank.jsp" width="100%" height="600" name="reportData" frameborder="0"></iframe></td></tr>
</table>

		