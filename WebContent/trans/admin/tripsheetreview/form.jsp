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
			getWeekOfDate(date);
		}
		getWeekOfDate(date);
	 }
   }
	else{
		document.getElementById("weekOfDateId").value="";
	}
}


function getWeekOfDate(date){
	jQuery.ajax({				
		url:'${ctx}/admin/tripsheetreview/ajax.do?action=weekOfDate&unloadDate='+date,
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			if(listData)
			document.getElementById("weekOfDateId").value=listData[0];			
		}
	});
}


function verifyTruck(){
	var truckNum = document.getElementById("truckId").value;
	if(truckNum!=''){
	jQuery.ajax({				
		url:'${ctx}/admin/tripsheetreview/ajax.do?action=verifytruck&truck='+truckNum,
		success: function( data ) {
			if(data!=''){
				document.getElementById("spanId1").innerHTML="Invalid truck number";
				
			}
			else{
				document.getElementById("spanId1").innerHTML="";
			}			
		}
	});
	}
}

function verifyTrailer(){
	var trailerNum = document.getElementById("trailerId").value;
	if(trailerNum!=''){
	jQuery.ajax({				
		url:'${ctx}/admin/tripsheetreview/ajax.do?action=verifytrailer&trailer='+trailerNum,
		success: function( data ) {
			if(data!=''){
				document.getElementById("spanId2").innerHTML="Invalid trailer number";
				
			}
			else{
				document.getElementById("spanId2").innerHTML="";
			}			
		}
	});
	}
}


function checkLandfillTicket() {
	var landfill=document.getElementById("origin").value;
	var originTicket=document.getElementById("originticket").value;
	var  ticID=document.getElementById("id").value;
	
	if(landfill!="" && originTicket!=""){
		jQuery.ajax({
			url:'${ctx}/admin/tripsheetreview/ajax.do?action=checkOriginTicket&landfil='+landfill+'&originTcket='+originTicket+'&ticId='+ticID, 
			success: function( data ) {
				if(data!=''){
					document.getElementById("spanId5").innerHTML="Duplicate Origin Ticket";
					$("#originticket").focus();
				}
				else{
					document.getElementById("spanId5").innerHTML="";
				}
			}
		});	
	}
	
}


function checkTransferTicket() {	
	var transferstattion=document.getElementById("destination").value;
	var destinationTicket=document.getElementById("destinationticket").value;
	var  ticID=document.getElementById("id").value;
	
	if(transferstattion!="" && destinationTicket!=""){
		jQuery.ajax({
			url:'${ctx}/admin/tripsheetreview/ajax.do?action=checkDestinationTicket&transferstation='+transferstattion+'&destinationTcket='+destinationTicket+'&ticId='+ticID, 
			success: function( data ) {
				if(data!=''){
					document.getElementById("spanId6").innerHTML="Duplicate Destination Ticket";
					$("#destinationticket").focus();
				}
				else{
					document.getElementById("spanId6").innerHTML="";
				}
			}
		});	
	}
}


function getComterminal(){
	var selecteddriver= document.getElementById('driverId');
	var driver=selecteddriver.options[selecteddriver.selectedIndex].value;	
	
	if(driver!=''){
		jQuery.ajax({				
			url:'${ctx}/admin/odometerreview/ajax.do?action=fetchcomterm&driver='+driver,
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				document.getElementById("companyId").value=listData[0];	
				document.getElementById("terminalId").value=listData[1];	
			}
		});
	}
	else{
		document.getElementById("companyId").value="";	
		document.getElementById("terminalId").value="";
	}
	
}


</script>
<br/>
<form:form action="save.do" name="tripSheetReviewForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table" cellspacing="1" cellpadding="5"">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Add/Update Trip Sheet" />
			</b>
			</td>
		</tr>
	
		<tr>
		<td class="form-left"><primo:label code="Driver" /><span class="errorMessage">*</span></td>		
		<td><form:select cssClass="flat" path="driver" id="driverId"
					style="min-width:184px; max-width:184px" onchange="getComterminal();">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${drivers}" itemValue="id"
						itemLabel="fullName"></form:options> 
			</form:select> <br> <form:errors path="driver" cssClass="errorMessage" />
		</td>		
		
		<td class="form-left"><primo:label code="Company" /></td>	
		<td><form:input  cssClass="flat" path="companyName" id="companyId" style="min-width:180px; max-width:180px;" readonly="true"/>
				
		</td>
		
		<td class="form-left"><primo:label code="Terminal" /></td>	
		<td><form:input  cssClass="flat" path="terminalName" id="terminalId" style="min-width:180px; max-width:180px;" readonly="true"/>
				 
		</td>
		
		
		</tr>
		
		<tr>
		<td class="form-left"><primo:label code="Truck#" /><span class="errorMessage">*</span></td>
		<td><form:input  cssClass="flat" path="tempTruck" id="truckId" style="min-width:180px; max-width:180px;" maxlength="8" onkeypress="return onlyNumbers(event, false)" onblur="verifyTruck();" onchange="verifyTruck();"/>
		 <br> <form:errors path="tempTruck" cssClass="errorMessage" /><span id="spanId1" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		
		</tr>
		
		
		<tr>
		
		<td class="form-left"><primo:label code="Trailer#" /><span class="errorMessage">*</span></td>
		<td><form:input cssClass="flat" id="trailerId" path="tempTrailer" style="min-width:180px; max-width:180px" maxlength="20"  onkeypress="return onlyNumbers(event, false)" onblur="verifyTrailer();" onchange="verifyTrailer();"/>
			 <br> <form:errors path="tempTrailer" cssClass="errorMessage" /><span id="spanId2" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>	
		
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Origin" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="origin" id="origin" style="min-width:184px; max-width:184px" onchange="return checkLandfillTicket();">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${origins}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="origin" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Org.&nbsp;Ticket#" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="originTicket" cssClass="flat" id="originticket"
					style="min-width:180px; max-width:180px" maxlength="20" onkeypress="return onlyNumbers(event, false)" onblur="return checkLandfillTicket();" /> <br> <form:errors
					path="originTicket" cssClass="errorMessage" /><span id="spanId5" style="color:red; font-size:11px; font-weight:bold"> </span>
			</td>
		</tr>
		
		
		<tr>
			<td class="form-left"><primo:label code="Date Loaded" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker" path="loadDate" style="min-width:180px; max-width:180px"
					cssClass="flat" onblur="return formatDate();"/> 
			<br> <form:errors path="loadDate" cssClass="errorMessage" /></td>
		</tr>
		
		
		
		
		<tr>
			<td class="form-left"><primo:label code="Destination" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="destination" style="min-width:184px; max-width:184px" id="destination" onchange="return checkTransferTicket();">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${destinations}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="destination" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Dest.&nbsp;Ticket#" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="destinationTicket" cssClass="flat" id="destinationticket"
					style="min-width:180px; max-width:180px" maxlength="20" onkeypress="return onlyNumbers(event, false)"  onblur="return checkTransferTicket();"/> <br> <form:errors
					path="destinationTicket" cssClass="errorMessage" /><span id="spanId6" style="color:red; font-size:11px; font-weight:bold"> </span>
			</td>
		</tr>
		
		
		<tr>
			<td class="form-left"><primo:label code="Date Unloaded" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker1" style="min-width:180px; max-width:180px"
					path="unloadDate" cssClass="flat" onblur="return formatDate1();" onchange="return formatDate1();"/> 
			<br> <form:errors path="unloadDate" cssClass="errorMessage" /></td>
		</tr>
		
		
		
		
	  <tr>
			<td class="form-left"><primo:label code="Week Of" />
			</td>
			<td align="${left}"><form:input path="batchDate" cssClass="flat" id="weekOfDateId"
					style="min-width:180px; max-width:180px" maxlength="20"  readonly="true"/> <br> <form:errors
					path="batchDate" cssClass="errorMessage" />
			</td>
		</tr>
		
		
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick=""
				value="<primo:label code="Save"/>" class="flat2" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat2" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat2"
				onClick="location.href='list.do'" /></td>
		</tr>
		
	
	</table>
</form:form>
