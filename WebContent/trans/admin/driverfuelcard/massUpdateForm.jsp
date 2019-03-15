<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">

var submitting = false;

function submitform() {
	if (submitting) {
		alert('please wait a moment...');
		return;
	}
	
	var validTo = document.getElementById('datepicker').value;
	var status = document.getElementById('status').value;
	if (validTo == "" && status == "-1"){
		alert("Please enter valid to date and/or status");
		return;
	} 
    
	submitting = true;
	document.forms["bulkUodateDriverFuelCardForm"].submit();
}

function updateStatusSelect() {
	var x = document.getElementById("status");
	var option = document.createElement("option");
	option.value = "-1";
	option.text = "---Please Select---";
	x.add(option, x[0]);
	x.selectedIndex = 0;
}

function formatDate() {
	var date=document.getElementById("datepicker").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker").value="";
		$( "#datepicker" ).focus();
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
						$( "#datepicker" ).focus();
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					$( "#datepicker" ).focus();
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					$( "#datepicker" ).focus();
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker").value="";
						$( "#datepicker" ).focus();
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					$( "#datepicker" ).focus();
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					$( "#datepicker" ).focus();
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker").value="";
						$( "#datepicker" ).focus();
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					$( "#datepicker" ).focus();
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					$( "#datepicker" ).focus();
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker").value=date;
		}
		else{
			if(date.length!=10){
				alert("Invalidte date format");
				document.getElementById("datepicker").value="";
				$( "#datepicker" ).focus();
				return true;
			}
		}
	 }
   }
}
</script>
<h3>
	<primo:label code="Bulk Update Driver Fuel Cards" />
</h3>
<form:form action="updatebulkeditdata.do" name="bulkUodateDriverFuelCardForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Common Information" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Valid To" /></td>
			<td align="${left}">
				<form:input id="datepicker" path="validTo" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate();" /> 
					<br> <form:errors path="validTo" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Status" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="status">
					<form:options items="${fuelcardstatus}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> 
				<br> <form:errors path="status" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="button"
					name="create" id="create" onclick="javascript:submitform();"
					value="<primo:label code="Save"/>" class="flat" />
				 <input type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
					class="flat" /> 
				<input type="button" id="cancelBtn"
					value="<primo:label code="Cancel"/>" class="flat"
					onClick="location.href='cancelbulkedit.do'" />
			</td>
		</tr>
	</table>
</form:form>

<script type="text/javascript">
updateStatusSelect();
</script>