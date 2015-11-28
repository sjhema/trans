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
	<primo:label code="Add/Update Vehicle Toll Tag" />
</h3>
<form:form action="save.do" name="vehicleTollTagForm" commandName="modelObject"	method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td align="${left}" colspan="2"><b><primo:label code="Add/Update Vehicle Toll Tag"/></b></td>
		</tr>		
		<tr>
			<td class="form-left"><primo:label code="Unit#" /><span class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="vehicle" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${vehicle}" itemValue="id" itemLabel="unit" />
				</form:select> <br> <form:errors path="vehicle" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Toll Company" /><span
					class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="tollCompany" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${tollCompany}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="tollCompany" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Toll Tag No."/><span class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="tollTagNumber" style="min-width:150px; max-width:150px" cssClass="flat"/>
			 <br> <form:errors path="tollTagNumber" cssClass="errorMessage" /></td>
		</tr>
		
				<tr>
			<td class="form-left"><primo:label code="Valid From" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker" path="validFrom" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate();"/> 
			<br> <form:errors path="validFrom" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Valid To" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker1"
					path="validTo" style="min-width:150px; max-width:150px" cssClass="flat" onblur="return formatDate1();"/> 
			<br> <form:errors path="validTo" cssClass="errorMessage" /></td>
</tr>
		
		
		
		
		<%-- <tr>
			<td class="form-left"><primo:label code="Tag Status" /><span class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="tollTagStatus">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${tollTagStatus}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="tollTagStatus" cssClass="errorMessage" />
			</td>
		</tr> --%>
		

		
		
		
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
