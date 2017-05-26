<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var submitting = false;
function submitForm(){
	if(submitting) {
    	alert('Please wait a moment...');
    } else {
    	var driver = $("#driver").val();
    	var vehicle = $("#vehicle").val();
    	if (driver == "" && vehicle == "") {
    		alert("Please choose either Driver or Vehicle");
    		return false;
    	}
    	if (driver != "" && vehicle != "") {
    		alert("Please choose one of either Driver or Vehicle");
    		return false;
    	}
    	
        submitting = true;
        document.forms["fuelcardForm"].submit();
	}
}

function findFuelCard(){
	if(document.getElementById("fuelvendor").value == ""){
		
	}else{			
		var selectedfuelvendor= document.getElementById('fuelvendor');
		var fuelvendor=selectedfuelvendor.options[selectedfuelvendor.selectedIndex].value;
		if(fuelvendor!=""){
			jQuery.ajax({
				url:'${ctx}/admin/driverfuelcard/ajax.do?action=findFuelCard&fuelvendor='+fuelvendor, 
				success: function( data ) {
					var listData=jQuery.parseJSON(data);
					var options = '<option value="">-----Please Select-----</option>';
					for (var i = 0; i <listData.length; i++) {
						var fuelcard=listData[i];
						options += '<option value="'+fuelcard.id+'">'+fuelcard.fuelcardNum+'</option>';
					  }
					$("#fuelcards").html(options);
				}
			});
		
				}
		
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
	</script>

<h3><primo:label code="Manage Driver Fuel Cards"/></h3>

<form:form action="save.do" name="fuelcardForm"
	commandName="modelObject" method="post" >
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="2"><b><primo:label code="Add/Update Driver Fuel Card" />
			</b>
			</td>
		</tr>
		
			<tr>
			<td class="form-left"><primo:label code="Driver" /><span
				class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="driver" style="min-width:184px; max-width:184px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${drivers}" itemValue="id"
						itemLabel="fullName" />
				</form:select> <br> <form:errors path="driver" cssClass="errorMessage" /></td>
			</tr>
		<tr>
			<td class="form-left"><primo:label code="Vehicle" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="vehicle" id="vehicle" style="min-width:184px; max-width:184px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${vehicles}" itemValue="id" itemLabel="unit" />
				</form:select> 
				<br> <form:errors path="vehicle" cssClass="errorMessage" />
			</td>
		</tr>
		
		
		
		<tr>
			<td class="form-left"><primo:label code="Fuel Vendor" /><span
				class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="fuelvendor" style="min-width:184px; max-width:184px" id="fuelvendor" onchange="findFuelCard();">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${fuelvendor}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <form:errors path="fuelvendor" cssClass="errorMessage" /></td>
			
			</tr>
		 <tr >
			<td class="form-left"><primo:label code="Fuel Card" /><span
				class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="fuelcard" style="min-width:184px; max-width:184px" id="fuelcards">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${fuelcard}" itemValue="id"
						itemLabel="fuelcardNum" />
				</form:select> <br> <form:errors path="fuelcard" cssClass="errorMessage" /></td>
			
		
		</tr>	 
		
		<tr>
		<td class="form-left"><primo:label code="Status" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="status">
					<form:options items="${fuelcardstatus}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="status" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Valid From" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input id="datepicker" path="validFrom" style="min-width:184px; max-width:184px"
					cssClass="flat" onblur="return formatDate();"/> 
				<br> <form:errors path="validFrom" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Valid To" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input id="datepicker1" style="min-width:184px; max-width:184px"
					path="validTo" cssClass="flat" onblur="return formatDate1();"/> 
				<br> <form:errors path="validTo" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="button" name="create" id="create" onclick="javascript:submitForm();"
					value="<primo:label code="Save"/>" class="flat" />
				<input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" /></td>
		</tr>
	</table>
</form:form>

