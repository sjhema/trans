<%@ include file="/common/taglibs.jsp"%>
<script language="javascript">
window.onload = function WindowLoad(event) {
	element=document.getElementById('saved');
	element.style.display = 'none';
}
</script>
<script language="javascript">
function searchReport() {
	var c=document.getElementById("company").value;
	var d=document.getElementById("payrollDate").value;
	if(c==""){
		alert("Please select company");
	}else if(d==""){
		alert("Please Enter Check Date");
	}
	else{
		
	}
	if(c!=""&&d!=""){
    document.getElementById('sum').value='true' ;	
	document.forms[0].target="reportData";
	document.forms[0].submit();
	}
}
</script>
<form:form action="search.do" method="post"
	name="searchForm">
	<br/>
	<br/>
	<table id="form-table" width="60%" cellspacing="0" cellpadding="5" align="left">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b>Paychex</b></td>
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
		<td align="${left}" class="first"><label>Check Date</label><input type="hidden" name="reportbtnclicked" id="sum"></td>
			<td align="${left}"><input name="payrollDate" type="text" id="payrollDate" style="min-width:154px; max-width:154px"
				value="${sessionScope.searchCriteria.searchMap.toDate}" onblur="javascript:formatReportDate('payrollDate');"/>
				<script type="text/javascript">
			$(function() {
			$("#payrollDate").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>
		   <%--  <td align="${left}" class="first"><label>Paychex Run Number</label></td>
		    
		    <td align="${left}"><input name="payrollnumber" id="payno" type="text" style="min-width:154px; max-width:154px" /></td> --%>
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
			<a href="${ctx}/hr/paychex/savedetails.do" target="reportData" id="save" onclick="
			var c=document.getElementById('company').value;		
			var sum=document.getElementById('sum').value;
			var pb =document.getElementById('payrollDate').value;
			if(c==''){
				alert('Please select company');
				//this.disabled=true;
				  return false;
			}else if(pb==''){
				alert('Please Enter Payroll Batch Date');
				return false;
			}else if(sum=='false'||sum==''){
				alert('Please click on Preview Button.');
				return false;
			}
			else{
				element=document.getElementById('save');
				element.style.display = 'none';
				element1=document.getElementById('saved');
				element1.style.display = '';
			}
			">
				<img src="${ctx}/images/save.png" border="0" class="toolbarButton"/></a>
				<img src="${ctx}/images/saved.png" border="0" class="toolbarButton" id="saved"/>
<a href="${ctx}/hr/paychex/export.do?type=print" target="reportData"><img src="${ctx}/images/print.png" border="0" class="toolbarButton"/></a>
<a href="${ctx}/hr/paychex/export.do?type=xls" target="reportData"><img src="${ctx}/images/excel.png" border="0" class="toolbarButton"/></a>
<a href="${ctx}/hr/paychex/export.do?type=csv" target="reportData"><img src="${ctx}/images/csv.png" border="0" class="toolbarButton"/></a>
</td>
</tr>	
<tr><td align="${left}" width="100%" valign="top"><iframe src="${ctx}/trans/blank.jsp" width="1400" height="600" name="reportData" frameborder="0"></iframe></td></tr>
</table>

		