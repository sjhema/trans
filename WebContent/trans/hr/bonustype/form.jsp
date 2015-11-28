<%@ include file="/common/taglibs.jsp"%>
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

<h3>
	<primo:label code="Add/Update Bonus Type" />
</h3>
<form:form action="save.do" name="employeeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Add/Update Bonus Type"/></b></td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Bonus Type" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="typename" cssClass="flat" style="min-width:150px; max-width:150px"
					  /> <br> <form:errors
					path="typename" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Description" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input     path="description" cssClass="flat" style="min-width:150px; max-width:150px"
					  /> <br> <form:errors
					path="description" cssClass="errorMessage" /></td>
		
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Company" /></td>
		<td><form:select cssClass="flat" path="company" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td>
				<td class="form-left"><primo:label code="Terminal" /></td>
		<td><form:select cssClass="flat" path="terminal" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>
		
		</tr>
			<tr>
		<td class="form-left"><primo:label code="Category" /></td>
		<td><form:select cssClass="flat" path="catagory" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${catagories}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="catagory" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		
			<td class="form-left"><primo:label code="Experince Range" /><span
				class="errorMessage"></span></td>
				<td align="${left}"><form:input path="experinceRageFrom" style="min-width:150px; max-width:150px"
					cssClass="flat" /><span style="color: black">Days</span> 
			<br> <form:errors path="experinceRageFrom" cssClass="errorMessage" /></td>
			<td align="${left}"><form:input path="experinceRageTo" style="min-width:150px; max-width:150px"
					cssClass="flat" /><td><span style="color: black">Days</span></td> 
			<br> <form:errors path="experinceRageTo" cssClass="errorMessage" /></td>
		
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Amount" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input     path="amount" cssClass="flat" style="min-width:150px; max-width:150px"
					  /> <br> <form:errors
					path="amount" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
			<td width="200" class="form-left"><primo:label code="Mapto Ticket" /><span
				class="errorMessage"></span></td>
					<td><form:checkbox path="mapToTicket" value="1"/>
		</tr>
		<tr>
			<td width="200" class="form-left"><primo:label code="Whether Referal" /><span
				class="errorMessage"></span></td>
					<td><form:checkbox path="whetherReferal" value="1"/>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Effective Date" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="dateEffectiveFrom" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="datepicker" onblur="return formatDate();"/> 
			<br> <form:errors path="dateEffectiveFrom" cssClass="errorMessage" /></td>
			
			<td class="form-left"><primo:label code="Effective Date" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="dateEffectiveTo" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="datepicker1" onblur="return formatDate1();"/> 
			<br> <form:errors path="dateEffectiveTo" cssClass="errorMessage" /></td>
		
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Status" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="status" style="min-width:154px; max-width:154px">
					<form:options items="${bonustypestatus}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="status" cssClass="errorMessage" />
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
		