<%@ include file="/common/taglibs.jsp"%>
<script language="javascript">
window.onload = function WindowLoad(event) {
	element=document.getElementById('saved');
	element.style.display = 'none';
}
</script>

<script language="javascript">


function searchReport2() {
	var d1 = document.getElementById('fromDate').value;
	var driver = document.getElementById('driver').value;
	if (d1!=null && d1!='' && !isValidDate(d1)) {
		alert("Invalid billBatch date");
	}
	var d6 = document.getElementById('toDate').value;
	if (d6!=null && d6!='' && !isValidDate(d6)) {
		alert("Invalid billBatch date");
	}
	var e;
	/* if(driver==""){
		alert("please select Driver");
		e=yes;
	} */
	/* if(d1==""&&e!="yes"){
		alert("please select from batch date");
		e=yes;
	} */
	if(d6==""&&e!="yes"){
		alert("please select to batch date");
		e=yes;
	}
	document.getElementById('specialdet').value='true' ;
	document.getElementById('sum').value='false' ;
	if(d6!=""/* &&driver!="" */){
	document.forms[0].target="reportData";
	document.forms[0].submit();
	}
}


function searchReport() {
	var d1 = document.getElementById('fromDate').value;
	var driver = document.getElementById('driver').value;
	if (d1!=null && d1!='' && !isValidDate(d1)) {
		alert("Invalid billBatch date");
	}
	var d6 = document.getElementById('toDate').value;
	if (d6!=null && d6!='' && !isValidDate(d6)) {
		alert("Invalid billBatch date");
	}
	var e;
	/* if(driver==""){
		alert("please select Driver");
		e=yes;
	} */
	/* if(d1==""&&e!="yes"){
		alert("please select from batch date");
		e=yes;
	} */
	if(d6==""&&e!="yes"){
		alert("please select to batch date");
		e=yes;
	}
	document.getElementById('specialdet').value='false' ;
	document.getElementById('sum').value='false' ;
	if(d6!=""/* &&driver!="" */){
	document.forms[0].target="reportData";
	document.forms[0].submit();
	}
}
function searchReport1() {
	var d1 = document.getElementById('fromDate').value;
	var driver = document.getElementById('driver').value;
	if (d1!=null && d1!='' && !isValidDate(d1)) {
		alert("Invalid billBatch date");
	}
	var d6 = document.getElementById('toDate').value;
	if (d6!=null && d6!='' && !isValidDate(d6)) {
		alert("Invalid billBatch date");
	}
	var e;
	
	
	/* if(d1==""&&e!="yes"){
		alert("please select from batch date");
		e=yes;
	} */
	if(d6==""&&e!="yes"){
		alert("please select to batch date");
		e=yes;
	}
	if(d6!=""/* &&driver!="" */){
	document.getElementById('specialdet').value='false' ;
	document.getElementById('sum').value='true' ;
	document.forms[0].target="reportData";
	document.forms[0].submit();
	}
	
}
function savedata(){
	document.getElementById('sum').value='true'; 
	var c= document.getElementById('company');
	if(c==""){
		alert("please select company");
	}
	else{
		location.href='';
	}
}

function getComTerm(){
	var selecteddriver= document.getElementById('driver');
	var driver=selecteddriver.options[selecteddriver.selectedIndex].value;
	/* if(driver!=""){ */
	jQuery.ajax({
		url:'${ctx}/hr/report/driver/ajax.do?action=findDCompany&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			if(driver==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var company=listData[i];
				options += '<option value="'+company.id+'">'+company.name+'</option>';
			  }
			$("#company").html(options);
		}
	});	
	jQuery.ajax({
		url:'${ctx}/hr/report/driver/ajax.do?action=findDterminal&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options ;
			if(driver==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var terminal=listData[i];
				options += '<option value="'+terminal.id+'">'+terminal.name+'</option>';
			  }
			$("#terminal").html(options);
		}
	});
	
	/* } */
}
$(document).ready(function(){
	   $("#driversmul").multiselect();
	   $("#doubleholpaydriversmul").multiselect();
	   $("#holidayexpdriversmul").multiselect();
});

</script>

<form:form action="search.do" method="post"
	name="searchForm">
	
	<br/>
	<br/>
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b>Driver Payroll</b></td>
		</tr>
		<tr>
		<td class="form-left">Driver</td>
			<td align="${left}">
				<!-- <select name="driver" id="driver"  onchange="javascript:getComTerm()" style="width:130px"> -->
				<select name="driver" id="driver"  style="min-width:140px; max-width:140px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${drivers}">
						<option value="${item.fullName}">${item.fullName}</option>
					</c:forEach>
				</select>
			</td>
		<!-- </tr>
		<tr> -->
		<td class="first">Company</td>
			<td align="${left}">
				<select name="company" id="company"   style="min-width:140px; max-width:140px">
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
				<select name="terminal" id="terminal"   style="min-width:140px; max-width:140px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${terminals}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
		<td align="${left}" class="first"><label>From Batch Date</label></td>
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
		   <td align="${left}" class="first"><label>To Batch Date</label></td>
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
		    <td align="${left}" class="first"><label>Rate Expiration</label></td>
		    <td align="${left}">
		    <select name="expire" style="min-width:140px; max-width:140px">
		    <option value="0">None</option>
	                <option value="1">Validate</option>
		    </select>
		     <input type="hidden" name="summary" id="sum">
		     <input type="hidden" name="specdetail" id="specialdet">
		    </td>
		</tr>
		<tr>
		  <td align="${left}" class="first"><label>Check Date</label></td>
			<td align="${left}"><input name="payrollDate" type="text" id="payrollDate" size="15" style="min-width:136px; max-width:136px"
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
		    <td align="${left}" class="first"><label>Not to Pay For Drivers</label></td>
		    <td align="${left}">
				<select name="driversmul" id="driversmul" multiple="multiple" style="width:100px">
					<option value="-1">--Please Select--</option>
					<c:forEach var="item" items="${drivers}">
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
						<option value="${item.fullName}">${item.fullName}</option>
					</c:forEach>
				</select>
			</td>
			
			<td align="${left}" class="first"><label>Double Holiday Pay for</label></td>
		    <td align="${left}">
				<select name="doubleholpayriversmul" id="doubleholpaydriversmul" multiple="multiple" style="width:100px">
					<option value="-1">--Please Select--</option>
					<c:forEach var="item" items="${drivers}">
						<option value="${item.fullName}">${item.fullName}</option>
					</c:forEach>
				</select>
			</td>
		
		</tr>
		
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3"><input type="button"
			onclick="javascript:searchReport1()" value="Summary" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button"
				onclick="javascript:searchReport()" value="Detail" />
				
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button"
				onclick="javascript:searchReport2()" value="Detail - New Format " />
				</td>
		</tr>
		</table>
		</form:form>
		<table width="100%">
	<tr>
		<td align="${left}" width="100%" align="right">
			<a href="${ctx}/hr/report/driver/save.do" target="reportData" id="save" onclick="
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
				<img src="${ctx}/images/saved.png" border="0" class="toolbarButton" id="saved"/>
			<a href="${ctx}/hr/report/driver/export.do?type=print" target="reportData"><img src="${ctx}/images/print.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/hr/report/driver/export.do?type=pdf" target="reportData"><img src="${ctx}/images/pdf.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/hr/report/driver/export.do?type=xls" target="reportData"><img src="${ctx}/images/excel.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/hr/report/driver/export.do?type=csv" target="reportData"><img src="${ctx}/images/csv.png" border="0" class="toolbarButton"/></a>
			
		</td>
		</tr>
		<tr><td align="${left}" width="100%" valign="top"><iframe src="${ctx}/trans/blank.jsp" width="100%" height="600" name="reportData" frameborder="0"></iframe></td></tr>
</table>
