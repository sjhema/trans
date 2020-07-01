<%@ include file="/common/taglibs.jsp"%>
<script language="javascript">
window.onload = function WindowLoad(event) {
	element=document.getElementById('saved');
	element.style.display = 'none';
}
</script>
<script language="javascript">


function getDriver()
{
	var selectedterminal= document.getElementById('company');
	var companyId=selectedterminal.options[selectedterminal.selectedIndex].value;	
	
	if(companyId!='')
	{
	jQuery.ajax({		
		url:'${ctx}/hr/report/weeklysalarypayroll/ajax.do?action=findDriver&company='+companyId, 
		
			success: function( data ) 
			{
				var listData=jQuery.parseJSON(data);
				
				var options = '<option value="">------Please Select------</option>';
				
				$("#driversmul").html(options);
		}
	});	
	}
}



function searchReport() {
	var c=document.getElementById("company").value;
	var d=document.getElementById("payrollDate").value;
	var pn=document.getElementById('payno').value;
	if(c==""){
		alert("Please select company");
	}else if(d==""){
		alert("Please Enter PayRoll Batch Date");
	}else if(pn==""){
		alert('Please Enter Check Date');
	}
	else{
		
	}
	if(c!=""&&d!=""&&pn!=""){
		
	document.forms[0].target="reportData";
	document.forms[0].submit();
	}
}

$(document).ready(function(){
	   $("#driversmul").multiselect();
	   $("#holidayexpdriversmul").multiselect();
});
</script>
<form:form action="search.do?for=payroll" method="post"
	name="searchForm">
	<br/>
	<br/>
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b>Salary Payroll</b></td>
		</tr>
			<tr>
		<td class="form-left">Employee</td>
			<td align="${left}">
				<!-- <select name="driver" id="driver"  onchange="javascript:getComTerm()" style="width:130px"> -->
				<select name="driver" id="driver"  style="min-width:154px; max-width:154px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${employees}">
						<option value="${item.fullName}">${item.fullName}</option>
					</c:forEach>
				</select>
			</td>
		<!-- </tr>
		<tr> -->
		<td class="first">Company</td>
			<td align="${left}">
				<select name="company" id="company"    style="min-width:154px; max-width:154px"  >
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
		<td align="${left}" class="first"><label>Batch Date</label></td>
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
		    
		    
		    <td align="${left}" class="first"><label>Check Date</label></td>
			<td align="${left}"><input name="checkDate" type="text" id="payno" style="min-width:154px; max-width:154px"
				value="${sessionScope.searchCriteria.searchMap.checkDate}" onblur="javascript:formatReportDate('payno');"/>
				<script type="text/javascript">
			$(function() {
			$("#payno").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>  
		    
		    
		    <td align="${left}" class="first"><label>Not to Pay For Employees</label></td>
		    <td align="${left}">
				<select name="driversmul" id="driversmul" multiple="multiple" style="width:100px">
					<option value="-1">--Please Select--</option>
					<c:forEach var="item" items="${employees}">
						<option value="${item.id}">${item.fullName}</option>
					</c:forEach>
				</select>
			</td>
		    
		</tr>
		<tr>
			<td align="${left}" class="first"><label>Holiday Exceptions</label></td>
		    <td align="${left}">
				<select name="holidayexpdriversmul" id="holidayexpdriversmul" multiple="multiple" style="width:100px">
					<option value="-1">--Please Select--</option>
					<c:forEach var="item" items="${drivers}">
						<option value="${item.id}">${item.fullName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
			<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3"><input type="button"
			onclick="javascript:searchReport()" value="Summary" />
			</td>
		</tr>
	</table>
</form:form>
<table width="100%">
	<tr>
		<td align="${left}" width="100%" align="right">
			<a href="${ctx}/hr/report/weeklysalarypayroll/save.do?for=payroll" target="reportData" id="save" onclick="
			var c=document.getElementById('company').value;
			var pb =document.getElementById('payrollDate').value;
			var pn=document.getElementById('payno').value;
			if(c==''){
				alert('please select company');
				//this.disabled=true;
				  return false;
			}else if(pb==''){
				alert('Please Enter Payroll Batch Date')
				return false;
			}else if(pn==''){
				alert('Please Enter Payroll Number');
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
			<a href="${ctx}/hr/report/weeklysalarypayroll/export.do?type=print&for=payroll" target="reportData"><img src="${ctx}/images/print.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/hr/report/weeklysalarypayroll/export.do?type=pdf&for=payroll" target="reportData"><img src="${ctx}/images/pdf.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/hr/report/weeklysalarypayroll/export.do?type=xls&for=payroll" target="reportData"><img src="${ctx}/images/excel.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/hr/report/weeklysalarypayroll/export.do?type=csv&for=payroll" target="reportData"><img src="${ctx}/images/csv.png" border="0" class="toolbarButton"/></a>
		</td>
		</tr>
		<tr><td align="${left}" width="100%" valign="top"><iframe src="${ctx}/trans/blank.jsp" width="100%" height="600" name="reportData" frameborder="0"></iframe></td></tr>
</table>