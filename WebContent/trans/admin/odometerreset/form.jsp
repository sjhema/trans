<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function submitform() {
	document.forms["odometerResetForm"].action='${ctx}/admin/odometerreset/save.do';
	
	/*var resetReading = document.getElementById("resetReading").value;
	if(resetReading == ''){
	}
	else {*/
		document.forms["odometerResetForm"].submit();
	//}
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

function verifyTruck(){
	var truckUnit = document.getElementById("truckUnit").value;
	if (truckUnit != '') {
		jQuery.ajax({				
			url:'${ctx}/admin/odometerreset/ajax.do?action=verifytruck&truck='+truckUnit,
			success: function( data ) {
				if (data != '') {
					document.getElementById("spanId1").innerHTML="Invalid truck number";
				} else {
					document.getElementById("spanId1").innerHTML="";
				}			
			}
		});
	}
}
</script>
<br/>
<form:form action="save.do" name="odometerResetForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table" cellspacing="1" cellpadding="5"">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Reset Odometer Reading" />
			</b>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Truck#" /><span class="errorMessage">*</span></td>
			<td>
				<form:input cssClass="flat" path="tempTruck" id="truckUnit" style="min-width:180px; max-width:180px;" maxlength="8" onkeypress="return onlyNumbers(event, false)" onblur="verifyTruck();" onchange="verifyTruck();"/>
				<br>
				<form:errors path="tempTruck" cssClass="errorMessage" /> <span id="spanId1" style="color:red; font-size:11px; font-weight:bold"> </span>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Reset Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input id="datepicker" path="resetDate" style="min-width:180px; max-width:180px"
					cssClass="flat" onblur="return formatDate();"/> 
			<br> 
			<form:errors path="resetDate" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Reset Reading" /><span class="errorMessage">*</span>
			</td>
			<td align="${left}">
				<form:input path="resetReading" cssClass="flat" id="resetReading" style="min-width:180px; max-width:180px" maxlength="20" />
				<br> 
				<form:errors path="resetReading" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit"
					name="create" id="create" onclick="javascript:submitform();"
					value="<primo:label code="Save"/>" class="flat2" /> 
				<input type="reset" id="resetBtn" value="<primo:label code="Reset"/>" class="flat2" /> 
				<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat2"
					onClick="location.href='list.do'" />
			</td>
		</tr>		
	</table>
</form:form>
