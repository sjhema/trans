<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function getamount(){
	var noOfday=document.getElementById("nofday").value;
	var rate=document.getElementById("rate").value;
	if(noOfday!=""&&rate!=""){
		var amount=noOfday*rate;
		document.getElementById("amount").value=amount;
	}
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
</script>

<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<br/>
	<br/>
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="4"><b><primo:label code="Add/Update Holiday"/></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="name" cssClass="flat" style="min-width:150px; max-width:150px"/>
			 	<br><form:errors path="name" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Leave Type" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="leaveType" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${leavetypes}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="leaveType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Category" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="catagory" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${catagories}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="catagory" cssClass="errorMessage" />
			</td>
		</tr>
			<tr>
		<td class="form-left"><primo:label code="Company" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="company" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Terminal" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="terminal" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>
		</tr>
			<tr>
			<td class="form-left"><primo:label code="Date From" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="dateFrom" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="datepicker" onblur="return formatDate();"/> 
			<br> <form:errors path="dateFrom" cssClass="errorMessage" /></td>
			
			<td class="form-left"><primo:label code="Date To" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="dateTo" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="datepicker1" onblur="return formatDate1();"/> 
			<br> <form:errors path="dateTo" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="No. Of Days" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="noOfDays" style="min-width:150px; max-width:150px"
					cssClass="flat" id="nofday" onblur="javascript:getamount()"/> 
			<br> <form:errors path="noOfDays" cssClass="errorMessage" /></td>
			
			<td class="form-left"><primo:label code="Batch Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="batchdate" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="datepicker2" onblur="return formatDate2();"/> 
			<br> <form:errors path="batchdate" cssClass="errorMessage" /></td>
		</tr>
		<tr>
		
		
		<td class="form-left"><primo:label code="Rate" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="rate" style="min-width:150px; max-width:150px"
					cssClass="flat" id="rate" onblur="javascript:getamount()"/> 
			<br> <form:errors path="rate" cssClass="errorMessage" /></td>
			<td class="form-left"><primo:label code="Amount" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="amount" style="min-width:150px; max-width:150px"
					cssClass="flat" id="amount" /> 
			<br> <form:errors path="amount" cssClass="errorMessage" /></td>
			</tr>
		<tr>
		<td class="form-left"><primo:label code="Paid" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="paid">
					<form:options items="${paidstatus}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="paid" cssClass="errorMessage" />
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
		