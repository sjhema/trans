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


function formatDate(){
	var date=document.getElementById("datepicker").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker").value="";
		return true;
	}
	else{
		var str=new String(date);
		if(!str.match("-")){
			var mm=str.substring(0,2);
			var dd=str.substring(2,4);
			var yy=str.substring(4,8);
			var enddigit=str.substring(6,8);
			if(!enddigit==00 && enddigit%4==0 ){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker").value=date;
		}
	 }
   }
}
function formatDate1(){
	var date=document.getElementById("datepicker1").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker1").value="";
		return true;
	}
	else{
		var str=new String(date);
		if(!str.match("-")){
			var mm=str.substring(0,2);
			var dd=str.substring(2,4);
			var yy=str.substring(4,8);
			var enddigit=str.substring(6,8);
			if(!enddigit==00 && enddigit%4==0 ){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker1").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker1").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker1").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker1").value=date;
		}
	 }
   }
}





function getComTerm(){
	var employeselected=document.getElementById("driverId");
	var driver=employeselected.options[employeselected.selectedIndex].value;
	if(driver!=''){
  	jQuery.ajax({
		url:'${ctx}/hr/miscellaneousamount/ajax.do?action=findDCompany&driver='+driver, 
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
			$("#companyId").html(options);
		}
			
		}); 
  	
  	jQuery.ajax({
		url:'${ctx}/hr/miscellaneousamount/ajax.do?action=findDTerminal&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			if(driver==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var terminal=listData[i];
				options += '<option value="'+terminal.id+'">'+terminal.name+'</option>';
				}
			$("#terminalId").html(options);
		}
			
		}); 
	}
	else{

	  	jQuery.ajax({
			url:'${ctx}/hr/miscellaneousamount/ajax.do?action=findDCompany', 
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
				$("#companyId").html(options);
			}
				
			}); 
	  	
	  	jQuery.ajax({
			url:'${ctx}/hr/miscellaneousamount/ajax.do?action=findDTerminal&', 
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var options;
				if(driver==""){
					options = '<option value="">Please Select</option>';
				}
				for (var i = 0; i <listData.length; i++) {
					var terminal=listData[i];
					options += '<option value="'+terminal.id+'">'+terminal.name+'</option>';
					}
				$("#terminalId").html(options);
			}
				
			});
  	}
}



function getTerminal()
{
	//var selecteddriver=document.getElementById("driverId").value;
	var selectedcompany = document.getElementById('companyId');
	var companyid = selectedcompany.options[selectedcompany.selectedIndex].value;
	var selectedterminal= document.getElementById('terminalId');
	var terminalId=selectedterminal.options[selectedterminal.selectedIndex].value;
	
	if(companyid!='')
	{
	jQuery.ajax({
		
		url:'${ctx}/hr/miscellaneousamount/ajax.do?action=findTerminal&company='+companyid, 
		
			success: function( data ) 
			{
				var listData=jQuery.parseJSON(data);
				
				var options = '<option value="">------Please Select------</option>';
				for (var i = 0; i <listData.length; i++) {
					var dlst=listData[i];
					if(terminalId==dlst.id)
					options += '<option value="'+dlst.id+'" selected="selected">'+dlst.name+'</option>';
					else
						options += '<option value="'+dlst.id+'">'+dlst.name+'</option>';
					
				}
				$("#terminalId").html(options);
		}
	});	
	}
	else
	{
		jQuery.ajax({
			url:'${ctx}/hr/miscellaneousamount/ajax.do?action=findAllTerminal', 
			
				success: function( data ) {
					var listData=jQuery.parseJSON(data);
					var options = '<option value="">------Please Select------</option>';
					for (var i = 0; i <listData.length; i++) {
						var dlst=listData[i];
						options += '<option value="'+dlst.id+'">'+dlst.name+'</option>';
					  }
					$("#terminalId").html(options);
			}
		});	
	}
}


function getDriver()
{
	//var selecteddriver=document.getElementById("driverId").value;
	
	var selectedcompany = document.getElementById('companyId');
	var companyid = selectedcompany.options[selectedcompany.selectedIndex].value;
	var selecteddriver= document.getElementById('driverId');
	var driverId=selecteddriver.options[selecteddriver.selectedIndex].value;	
	var selectedterminal= document.getElementById('terminalId');
	var terminalId=selectedterminal.options[selectedterminal.selectedIndex].value;
	var ticId=document.getElementById('id').value;
	
	if(terminalId!='')
	{
		jQuery.ajax({
			
			url:'${ctx}/hr/miscellaneousamount/ajax.do?action=findDriver&terminal='+terminalId+'&tickId='+ticId+'&company='+companyid, 
			
				success: function( data ) 
				{
					var listData=jQuery.parseJSON(data);
					var options = '<option value="">------Please Select------</option>';
					for (var i = 0; i <listData.length; i++) {
						var dlst=listData[i];
						if(driverId==dlst.id)
						options += '<option value="'+dlst.id+'" selected="selected">'+dlst.fullName+'</option>';
						else
							options += '<option value="'+dlst.id+'">'+dlst.fullName+'</option>';
						
					}
					$("#driverId").html(options);
			}
		});	
	}
	else
	{
		jQuery.ajax({
			url:'${ctx}/hr/miscellaneousamount/ajax.do?action=findAllDriver', 
			
				success: function( data ) {
					var listData=jQuery.parseJSON(data);
					var options = '<option value="">------Please Select------</option>';
					for (var i = 0; i <listData.length; i++) {
						var dlst=listData[i];
						options += '<option value="'+dlst.id+'">'+dlst.fullName+'</option>';
					  }
					$("#driverId").html(options);
			}
		});
	}
}


</script>
<h3>
	<primo:label code="Miscellaneous Pay" />
</h3>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<br/>
	<br/>
	<table align="left" id="form-table" width="80%" cellspacing="1" cellpadding="5">		
	<tr class="table-heading">
			<td align="${left}" colspan="9"><b>Miscellaneous Pay</b>
			</td>
		</tr>
<tr>
		<td class="form-left"><primo:label code="Company" /><span
				class="errorMessage">*</span></td>
		<%-- <td><form:select cssClass="flat" path="company" id="companyid" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td> --%>			
			<td><form:select cssClass="flat" path="company" id="companyId" onchange="javascript:getTerminal();" style="min-width:154px; max-width:154px">
							<form:option value="">-----Please Select----</form:option>
							<form:options items="${companies}" itemValue="id" itemLabel="name" />
						</form:select> 
					<br><form:errors path="company" cssClass="errorMessage" />
				</td>
			
			
			<td class="form-left"><primo:label code="Terminal" /><span
				class="errorMessage">*</span></td>
		<%-- <td><form:select cssClass="flat" path="terminal" id="terminalid" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td> --%>
			
			<td><form:select cssClass="flat" path="terminal" style="min-width:154px; max-width:154px" id="terminalId"
					onchange="javascript:getDriver();">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${terminals}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>
</tr>

<tr> 

<td class="form-left"><primo:label code="From Batch Date" /><span class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="batchFrom" id="datepicker" style="min-width:150px; max-width:150px"
					Class="flat" onblur="return formatDate();" /> <br> <form:errors path="batchFrom" cssClass="errorMessage" /></td>
					
			<td class="form-left"><primo:label code="To Batch Date" /><span class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="batchTo" id="datepicker1" style="min-width:150px; max-width:150px"
					Class="flat" onblur="return formatDate1();" /> <br> <form:errors path="batchTo" cssClass="errorMessage" /></td>
</tr>



<tr>
<td class="form-left"><primo:label code="Employee" /><span class="errorMessage">*</span></td>
<%-- <td><form:select cssClass="flat" path="driver" id="empId" style="min-width:154px; max-width:154px" onchange="javascript:getComTermCat()">
<form:option value="">------<primo:label
			code="Please Select" />------</form:option>
<form:options items="${employees}" itemValue="id" itemLabel="fullName" />
</form:select> <br> <form:errors path="driver" cssClass="errorMessage" />
</td> --%>

<td>    <form:select cssClass="flat" path="driver" id="driverId" style="min-width:154px; max-width:154px" onchange="javascript:getComTerm();">
		<form:option value="">------<primo:label code="Please Select" />------</form:option>
		<form:options items="${employees}" itemValue="id"	itemLabel="fullName"></form:options> 
		</form:select> <br> <form:errors path="driver" cssClass="errorMessage" />
</td>



<td class="form-left" ><primo:label code="Misc. Amount" /><span
				class="errorMessage">*</span></td>
<td align="${left}"><form:input id="misamount" style="min-width:150px; max-width:150px"   path="misamount" cssClass="flat"/> 
<br> <form:errors path="misamount" cssClass="errorMessage" /></td>
</tr>

<tr>
<td class="form-left" ><primo:label code="Misc. Description" /><span
				class="errorMessage">*</span></td>
<td>
				<form:select cssClass="flat" path="miscNotes" id="miscNotes" style="min-width:154px; max-width:154px;">
				<form:option value="">-----Please Select----</form:option>
				   <form:options items="${miscellaneousDesc}" itemValue="dataLabel" itemLabel="dataLabel"/> 
                   
            </form:select>
            <br> <form:errors path="miscNotes" cssClass="errorMessage" />
</td>

<td class="form-left" ><primo:label code="Sequence Number" /><span
				class="errorMessage">*</span></td>
<td align="${left}"><form:input id="sequenceNo" style="min-width:150px; max-width:150px"   path="sequenceNumber" cssClass="flat"/> 
<br> <form:errors path="sequenceNumber" cssClass="errorMessage" /></td>

</tr>
<tr>
<td class="form-left" ><primo:label code="Category" /></td>
				
<td>
		<form:select cssClass="flat" path="miscType" id="miscCategory" style="min-width:154px; max-width:154px">
		
		<form:options items="${miscellaneousType}" itemValue="dataValue" itemLabel="dataLabel"/> 
         </form:select>
            <%-- <br> <form:errors path="miscType" cssClass="errorMessage" /> --%>
</td>
</tr>
<%--
<tr>
<td class="form-left" ><primo:label code="Payroll Batch" /></td>
<td align="${left}"><form:input style="min-width:150px; max-width:150px" path="payRollBatch" cssClass="flat"/> 
</td>
<td class="form-left" ><primo:label code="Payroll Pending" /></td>
<td align="${left}"><form:input style="min-width:150px; max-width:150px" path="payRollStatus" cssClass="flat"/> 
</td>
</tr>
--%>	
<tr>
  <td class="form-left"><primo:label code="Notes" /><span
				class="errorMessage"></span></td>
	<td align="${left}" colspan="5">
				<form:textarea path="miscDetailedNotes" rows="2" cols="69"/>    	
			</td>
</tr>


	<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick=""
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" /></td>
		</tr>
		</table>
		</form:form>