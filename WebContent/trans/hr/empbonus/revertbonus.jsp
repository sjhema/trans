<%@ include file="/common/taglibs.jsp"%>
<script language="javascript">


</script>
<script type="text/javascript">
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
function formatDate2(){
	var date=document.getElementById("datepicker2").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker2").value="";
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
						document.getElementById("datepicker2").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker2").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker2").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker2").value=date;
		}
	 }
   }
}
function formatDate3(){
	var date=document.getElementById("datepicker3").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker3").value="";
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
						document.getElementById("datepicker3").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker3").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker3").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker3").value=date;
		}
	 }
   }
}
function getComTermCat(){
	var employeselected=document.getElementById("empId");
	var driver=employeselected.options[employeselected.selectedIndex].text;
  	jQuery.ajax({
		url:'${ctx}/hr/empbonus/ajax.do?action=findDCompany&driver='+driver, 
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
			$("#companyid").html(options);
		}
			
		});  
         
  	jQuery.ajax({
		url:'${ctx}/hr/empbonus/ajax.do?action=findDTerminal&driver='+driver, 
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
			$("#terminalid").html(options);
		}
			
		}); 
	
 	jQuery.ajax({
		url:'${ctx}/hr/empbonus/ajax.do?action=findDCategory&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			if(driver==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var category=listData[i];
				options += '<option value="'+category.id+'">'+category.name+'</option>';
				}
			$("#categoryid").html(options);
		}
			
		}); 
	/*jQuery.ajax({
		url:'${ctx}/hr/empbonus/ajax.do?action=findhireex&driver='+driver,
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var dateHired=listData[0];
			var exp=listData[1];
		    document.getElementById("datepicker").value=dateHired;
			 document.getElementById("experience").value=exp;
		}
	});	*/

	  }
	  
      function getamount(){
    	  var bonusselected=document.getElementById("btype");
  		var bonus=bonusselected.options[bonusselected.selectedIndex].value;
  		jQuery.ajax({
			url:'${ctx}/hr/empbonus/ajax.do?action=findamount&bid='+bonus,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var amount=listData[0];
				
			    document.getElementById("amounts").value=amount.amount;
				
			}
		});
      }
 
</script>
<h3>
	<primo:label code="Revert Employee Bonus" />
</h3>
<form:form action="save.do" name="employeeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="mode" id="mode" />
	<table width="100%" id="form-table" cellspacing="1" cellpadding="5" >
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Revert Employee Bonus"/></b></td>
		</tr>
		<tr>
		<%--
		<td class="form-left"><primo:label code="Employee" /><span class="errorMessage"></span></td>
		<td align="${left}">${modelObject.driver.fullName}</td>
		<td class="form-left"><primo:label code="Category" /><span
				class="errorMessage"></span></td>
		<td align="${left}">${modelObject.category.name}</td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Company" /><span
				class="errorMessage"></span></td>
		<td align="${left}">${modelObject.company.name}</td>
		<td class="form-left"><primo:label code="Terminal" /><span
				class="errorMessage"></span></td>
		<td align="${left}">${modelObject.terminal.name}</td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Bonus Type" /><span class="errorMessage"></span></td>
		<td>${modelObject.bonustypes}</td>
		<td class="form-left" ><primo:label code="Amount" /><span
				class="errorMessage"></span></td>
			<td align="${left}">${modelObject.amounts}</td>
		</tr>
		--%>
		
		<tr>
		<td class="form-left"><primo:label code="Employee" /><span class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="driver" id="empId" onchange="javascript:getComTermCat()">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="fullName" />
					
				</form:select> <br> <form:errors path="driver" cssClass="errorMessage" />
			</td>
		
		
		<td class="form-left"><primo:label code="Category" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="category" id="categoryid">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${categories}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="category" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Company" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="company" id="companyid">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td>
		
		<td class="form-left"><primo:label code="Terminal" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="terminal" id="terminalid">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Bonus Type" /><span class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="bonustype" id="btype" onchange="javascript:getamount()">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${bonustypes}" itemValue="id" itemLabel="typename" />
				</form:select> <br> <form:errors path="bonustype" cssClass="errorMessage" />
			</td>
		
		<td class="form-left" ><primo:label code="Amount" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="amounts" path="amounts" cssClass="flat"/> 
			<br> <form:errors path="amounts" cssClass="errorMessage" /></td>
			
		</tr>
	<%-- 	<tr>
		<td class="form-left"><primo:label code="Refferal" /><span class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="refferal" id="ref" onchange="javascript:getref()">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${reffrals}" itemValue="id" itemLabel="fullName" />
				</form:select> <br> <form:errors path="refferal" cssClass="errorMessage" />
			</td>
			
		<td class="form-left"><primo:label code="Hired Date" /><span class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="refferalhireddate"
					cssClass="flat" readonly="true"   id="datepicker1" onblur="return formatDate1();"/> 
			<br> <form:errors path="refferalhireddate" cssClass="errorMessage" /></td>
			
			
			
			
	    </tr> --%>
		
		<tr>
		<td class="form-left"><primo:label code="Batch Date From" /><span class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="batchFrom"
					cssClass="flat"    id="datepicker2" onblur="return formatDate2();"/> 
			<br> <form:errors path="batchFrom" cssClass="errorMessage" /></td>
			
			<td class="form-left"><primo:label code="Batch Date To" /><span class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="batchTo"
					cssClass="flat"   id="datepicker3" onblur="return formatDate3();"/> 
			<br> <form:errors path="batchTo" cssClass="errorMessage" /></td>
			
		</tr>
		 <tr>
  <td class="form-left"><primo:label code="Notes" /><span
				class="errorMessage"></span></td>
	<td align="${left}" colspan="5">
				<form:textarea path="notes" rows="2" cols="69"/>    	
			</td>
</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}"><input type="submit"
				name="create" id="create" onclick=""
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>

