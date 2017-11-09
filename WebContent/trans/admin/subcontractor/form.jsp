<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function formatPhone(){
	var phone = document.getElementById("phone").value;
	if(phone != ""){
		if(phone.length < 10){
			alert("Invalid Phone Number");
			document.getElementById("phone").value = "";
			return true;
		}
		else{
			var str = new String(phone);
			if(!str.match("-")){
				var p1 = str.substring(0,3);
				var p2 = str.substring(3,6);
				var p3 = str.substring(6,10);				
				var phone = p1 + "-" + p2 + "-" + p3;
				document.getElementById("phone").value = phone;
			}
		}
	}	
}

function formatFax(){
	var fax = document.getElementById("fax").value;
	if(fax != ""){
		if(fax.length < 10){
			alert("Invalid Phone Number");
			document.getElementById("fax").value = "";
			return true;
		}
		else{
			var str = new String(fax);
			if(!str.match("-")){
				var p1 = str.substring(0,3);
				var p2 = str.substring(3,6);
				var p3 = str.substring(6,10);				
				var fax = p1 + "-" + p2 + "-" + p3;
				document.getElementById("fax").value = fax;
			}
		}
	}	
}

function submitForm() {
	var company1 = document.getElementById("company1").value;
	var terminal1 = document.getElementById("terminal1").value;
	var company2 = document.getElementById("company2").value;
	var terminal2 = document.getElementById("terminal2").value;
	var company3 = document.getElementById("company3").value;
	var terminal3 = document.getElementById("terminal3").value;
	
	var valid = true;
	if (company1 != '' && terminal1 == '') {
		alert("Please select Terminal1");
		valid = false;
	}
	if (company1 == '' && terminal1 != '') {
		alert("Please select Company1");
		valid = false;
	}
	if (company2 != '' && terminal2 == '') {
		alert("Please select Terminal2");
		valid = false;
	}
	if (company2 == '' && terminal2 != '') {
		alert("Please select Company2");
		valid = false;
	}
	if (company3 != '' && terminal3 == '') {
		alert("Please select Terminal3");
		valid = false;
	}
	if (company3 == '' && terminal3 != '') {
		alert("Please select Company3");
		valid = false;
	}
	if (!valid) {
		return false;
	}
    
	document.forms["subcontractorForm"].submit();
}
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
</script>
<br/>
<form:form action="save.do" name="subcontractorForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="4"><b><primo:label code="Add/Update Subcontractor"/></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="name" cssClass="flat" style="min-width:200px; max-width:200px" maxlength="45" />
			 	<br><form:errors path="name" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Owner Operator" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="ownerOp" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:option value="Yes">Yes</form:option>
					<form:option value="No">No</form:option>
				</form:select> 
				<br><form:errors path="ownerOp" cssClass="errorMessage" />
			</td>
		</tr>
			<tr>
			<td class="form-left"><primo:label code="Date Hired" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="subdateHired" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="datepicker" onblur="return formatDate();"/> 
			<br> <form:errors path="subdateHired" cssClass="errorMessage" /></td>
			<td class="form-left"></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Address Line1"/><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:textarea path="address" cssClass="flat" style="min-width:150px; max-width:150px"/>
				 <br><form:errors path="address" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Address Line2"/></td>
			<td align="${left}">
				<form:textarea path="address2" cssClass="flat" style="min-width:150px; max-width:150px"/>
				 <br><form:errors path="address2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="City" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input cssClass="flat" path="city" style="min-width:150px; max-width:150px"/>
				<br><form:errors path="city" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><primo:label code="State" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="state" style="min-width:154px; max-width:154px">
					<form:option value="">-----------Please Select----------</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="state" cssClass="errorMessage" />
			</td>
			
			
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Zipcode" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="zipcode" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="5"
					 onkeypress="return onlyNumbers(event, false)"/>
			 	<br><form:errors path="zipcode" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"></td>
		</tr>		
		<tr>
			<td class="form-left"><primo:label code="Phone" /></td>
			<td align="${left}">
				<form:input path="phone" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="12" 
					id="phone" onkeypress="return onlyNumbers(event, false)" onblur="return formatPhone();"/>
			 	<br><form:errors path="phone" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Fax" /></td>
			<td align="${left}">
				<form:input path="fax" cssClass="flat"	style="min-width:150px; max-width:150px" maxlength="12" 
					id="fax" onkeypress="return onlyNumbers(event, false)" onblur="return formatFax();"/>
				 <br><form:errors path="fax" cssClass="errorMessage" />
			</td>
		</tr>
	    <%-- <tr>
			<td class="form-left">Transfer Station<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="transferStation" cssStyle="width:200px;">
					<form:option value="">-----------Please Select----------</form:option>
					<form:options items="${transferStation}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="transferStation" cssClass="errorMessage" />
			</td>
			<td class="form-left">Landfill<span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="landfill" cssStyle="width:200px;">
					<form:option value="" >-----------Please Select----------</form:option>
					<form:options items="${landfill}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="landfill" cssClass="errorMessage" />
			</td>
		</tr> --%>
		
	<tr>
			<td class="form-left"><primo:label code="Date Terminated" /></td>
			<td align="${left}"><form:input path="subdateTerminated" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="datepicker1" onblur="return formatDate1();"/> 
			<%-- <br> <form:errors path="subdateTerminated" cssClass="errorMessage" /> --%></td>
		  <td class="form-left"></td>
		</tr>
			<tr>
		<td class="form-left"><primo:label code="Status" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="status">
					<form:options items="${subcontstatus}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="status" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Company1" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="company1">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br> <form:errors path="company1" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Terminal1" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="terminal1">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br> <form:errors path="terminal1" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Company2" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="company2">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br> <form:errors path="company2" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Terminal2" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="terminal2">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br> <form:errors path="terminal2" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Company3" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="company3">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br> <form:errors path="company3" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Terminal3" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="terminal3">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br> <form:errors path="terminal3" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="button"
					name="create" id="create" onclick="javascript:submitForm();"
					value="<primo:label code="Save"/>" class="flat" />
				<input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" /></td>
		</tr>
	</table>
</form:form>
	
