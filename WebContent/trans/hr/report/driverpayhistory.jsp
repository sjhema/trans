<%@ include file="/common/taglibs.jsp"%>
<script language="javascript">
window.onload = function WindowLoad(event) {
	element=document.getElementById('saved');
	element.style.display = 'none';
}
</script>

<script language="javascript">


function searchReport2() {
	
	var company= document.getElementById('company').value;
	//var company=selecteddriver.options[selectedcompany.selectedIndex].value;
	
	
	
	var d1 = document.getElementById('fromDate').value;	
	if (d1!=null && d1!='' && !isValidDate(d1)) {
		alert("Invalid from check date");
	}
	var d6 = document.getElementById('toDate').value;
	if (d6!=null && d6!='' && !isValidDate(d6)) {
		alert("Invalid to check date ");
	}
	
	
	
	var e;
	
	 if(company==""){
		alert("Please select Company");
		e=yes;
	} 
	if(d1==""&&e!="yes"){
		alert("Please select From Batch Date");
		e=yes;
	}
	if(d6==""&&e!="yes"){
		alert("Please select To Batch Date");
		e=yes;
	}
	
	
	if(d1!=""&& d6!=""  && company!=""){
	document.forms[0].target="reportData";
	document.forms[0].action="search.do";
	document.forms[0].submit();
	}
}


function searchReport() {
	var company= document.getElementById('company').value;
	//var company=selecteddriver.options[selectedcompany.selectedIndex].value;
	
	
	
	var d1 = document.getElementById('fromDate').value;	
	if (d1!=null && d1!='' && !isValidDate(d1)) {
		alert("Invalid billBatch date");
	}
	var d6 = document.getElementById('toDate').value;
	if (d6!=null && d6!='' && !isValidDate(d6)) {
		alert("Invalid billBatch date");
	}
	
	
	
	var e;
	
	 if(company==""){
		alert("Please select Company");
		e=yes;
	} 
	if(d1==""&&e!="yes"){
		alert("Please select From Batch Date");
		e=yes;
	}
	if(d6==""&&e!="yes"){
		alert("Please select To Batch Date");
		e=yes;
	}
	
	
	

	if(d1!=""&& d6!="" && company!=""){
	document.forms[0].target="reportData";
	
	document.forms[0].submit();
	}
}
function searchReport1() {
	
	var driver= document.getElementById('driver').value;
	
	
	var company= document.getElementById('company').value;
	//var company=selecteddriver.options[selectedcompany.selectedIndex].value;
	var reportType= document.getElementById('reportType').value;
	
	
	
	var d1 = document.getElementById('fromDate').value;
	
	if (d1!=null && d1!='' && !isValidDate(d1)) {
		alert("Invalid from check date");
	}
	var d6 = document.getElementById('toDate').value;
	if (d6!=null && d6!='' && !isValidDate(d6)) {
		alert("Invalid to check date");
	}
	
	
	var e;
	
	if(reportType==""){
		alert("Please Select Report Type");
		e=yes;
	}
	
	if(driver==""){
		if(company==""){
			alert("Please Select Company");
			e=yes;
		} 
	}
	
	if(d1==""&&e!="yes"){
		alert("Please Enter Check Date From");
		e=yes;
	}
	if(d6==""&&e!="yes"){
		alert("Please Enter Check Date To");
		e=yes;
	}
	
	
	if(driver==""){
		if(d1!=""&& d6!="" && company!=""){
	
			document.forms[0].target="reportData";
			if(reportType=="1"){
				document.forms[0].action="search.do";
			}
			else if(reportType=="2"){
				document.forms[0].action="searchHourlyPayroll.do";
			}	
			else{
				document.forms[0].action="searchWeeklyPayroll.do";
			}
			document.forms[0].submit();
		}
	}
	else{
		if(d1!=""&& d6!=""){
			
			document.forms[0].target="reportData";
			if(reportType=="1"){
				document.forms[0].action="search.do";
			}
			else if(reportType=="2"){
				document.forms[0].action="searchHourlyPayroll.do";
			}	
			else{
				document.forms[0].action="searchWeeklyPayroll.do";
			}
			document.forms[0].submit();
			}
	}
}


function getComTerm(){
	var selecteddriver= document.getElementById('driver');
	var driver=selecteddriver.options[selecteddriver.selectedIndex].value;
	if(driver!=""){
	jQuery.ajax({
		url:'${ctx}/hr/report/driverpay/ajax.do?action=findReportType&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);		
			$("#reportType").val(listData[0]);
		}
	});		
 	}
	else{	
		$("#reportType").val("");		
	}
}
$(document).ready(function(){
	   $("#driversmul").multiselect();
});



function getDrivers(){	
	var selectedcategory= document.getElementById('category');	
	var category=selectedcategory.options[selectedcategory.selectedIndex].value;	
	jQuery.ajax({
		url:'${ctx}/hr/report/driverpay/ajax.do?action=findDrivers&category='+category, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);			
			var options;
			options = '<option value="">--Please Select--</option>';
			for (var i = 0; i <listData.length; i++) {
				var drivers=listData[i];
				options += '<option value="'+drivers.fullName+'">'+drivers.fullName+'</option>';
			}
			$("#driver").html(options);
		}
	});
	
	}




</script>

<form:form action="start.do" method="post"
	name="searchForm">
	
	<br/>
	<br/>
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b>Payroll Run History</b></td>
		</tr>
		<tr>
		
		<td class="first">Report Type<span	class="errorMessage">*</span></td>
			<td align="${left}">
				<select name="reportType" id="reportType"   style="min-width:140px; max-width:140px">
					<option value="">--Please Select--</option>
					<option value="1">Driver PayRoll Report</option>
					<option value="2">Hourly PayRoll Report</option>
					<option value="3">Salary PayRoll Report</option>
				</select>
			</td>
		
		
		
		 <td class="form-left"><primo:label code="Category" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<select  style="min-width:140px; max-width:140px"  name="category" id="category" onchange="javascript:getDrivers()">
					<option value="">-------Please Select------</option>					
					<c:forEach var="item" items="${catagories}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select> 				
			</td>
		
		
		<td class="form-left">Employee</td>
			<td align="${left}">
				<!-- <select name="driver" id="driver"  onchange="javascript:getComTerm()" style="width:130px"> -->
				<select name="driver" id="driver"  style="min-width:140px; max-width:140px" onchange="javascript:getComTerm()">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${drivers}">
						<option value="${item.fullName}">${item.fullName}</option>
					</c:forEach>
				</select>
			</td>
		
		
		<!-- </tr>
		<tr> -->
		
		</tr>
		<tr>
		
		<td class="first">Company<span	class="errorMessage">*</span></td>
			<td align="${left}">
				<select name="company" id="company"   style="min-width:140px; max-width:140px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${companies}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
		
		
		
		<td class="first">Terminal</td>
			<td align="${left}">
				<select name="terminal" id="terminal"   style="min-width:140px; max-width:140px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${terminals}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
		
		
	    
		    
		</tr>
		
		<tr>
		
		<td align="${left}" class="first"><label>Check Date From<span	class="errorMessage">*</span></label></td>
			<td align="${left}"><input name="fromDate" type="text" id="fromDate" size="15" style="min-width:136px; max-width:136px"
				value="${sessionScope.searchCriteria.searchMap.fromDate}" onblur="javascript:formatReportDate('fromDate');"/>
				<script type="text/javascript">
			$(function() {
			$("#fromDate").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>
		   <td align="${left}" class="first"><label>Check Date To<span	class="errorMessage">*</span></label></td>
			<td align="${left}"><input name="toDate" type="text" id="toDate" size="15" style="min-width:136px; max-width:136px"
				value="${sessionScope.searchCriteria.searchMap.toDate}" onblur="javascript:formatReportDate('toDate');"/>
				<script type="text/javascript">
			$(function() {
			$("#toDate").datepicker({
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
			onclick="javascript:searchReport1()" value="Submit" />
			</td>
		</tr>
		</table>
		</form:form>
		<table width="100%">
	<tr>
		<td align="${left}" width="100%" align="right">
			<%-- <a href="${ctx}/hr/report/driver/save.do" target="reportData" id="save" onclick="
			var c=document.getElementById('company').value;
			var sum=document.getElementById('sum').value;
			var pb =document.getElementById('payrollDate').value;
			if(c==''){
				alert('please select company');
				//this.disabled=true;
				  return false;
			}else if(pb==''){
				alert('Please Enter Payroll Batch Date');
				return false;
			}else if(sum=='false'||sum==''){
				alert('please click on Summary');
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
				<img src="${ctx}/images/saved.png" border="0" class="toolbarButton" id="saved"/> --%>
			<a href="${ctx}/hr/report/driverpay/export.do?type=print" target="reportData"><img src="${ctx}/images/print.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/hr/report/driverpay/export.do?type=pdf" target="reportData"><img src="${ctx}/images/pdf.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/hr/report/driverpay/export.do?type=xls" target="reportData"><img src="${ctx}/images/excel.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/hr/report/driverpay/export.do?type=csv" target="reportData"><img src="${ctx}/images/csv.png" border="0" class="toolbarButton"/></a>
		</td>
		</tr>
		<tr><td align="${left}" width="100%" valign="top"><iframe src="${ctx}/trans/blank.jsp" width="100%" height="600" name="reportData" frameborder="0"></iframe></td></tr>
</table>
