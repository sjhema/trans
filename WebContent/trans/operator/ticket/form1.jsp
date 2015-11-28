<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">

	var submitting = false;



function submitform(type){
		document.forms["ticketForm"].action='${ctx}/operator/ticket/save.do?type='+type;
		 if(submitting) {
    alert('please wait a moment...');
    }
    else{
         submitting = true;
		document.forms["ticketForm"].submit();
        }
}


$(function() {
    $( "#batch" ).datepicker({
         dateFormat: 'mm-dd-yy',
         beforeShowDay: enableSUNDAYS
    });
   
    function enableSUNDAYS(date) {
        var day = date.getDay();
        return [(day == 0), ''];
    }
});



function getTime(field){
	var timein=document.getElementById(field).value;
	if(timein!=""){
		if(timein.length<4){
			alert("Invalidte time format");
			document.getElementById(field).value="";
			$("#"+field).focus();
			return true;
		}else{
			var str=new String(timein);
			if(!str.match(":")){
				var hour=str.substring(0,2);
				var min=str.substring(2,4);
				if(hour>=24 || min>=60){
					alert("Invalidte time format");
					document.getElementById(field).value="";
					$("#"+field).focus();
					return true;
				}
				var time=hour+":"+min;
				document.getElementById(field).value=time;
			}
		}
	}
}

/* function getTerminal(){
	document.getElementById("terminalId").value="";
	var selecteddriver= document.getElementById('driverId');
	var driverId=selecteddriver.options[selecteddriver.selectedIndex].value;
	jQuery.ajax({
		url:'${ctx}/operator/ticket/ajax.do?action=findTerminal&driver='+driverId, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			document.getElementById("terminalId").value=listData;
		}
	});	
} */



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
		
		url:'${ctx}/operator/ticket/ajax.do?action=findTerminal&company='+companyid, 
		
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
			url:'${ctx}/operator/ticket/ajax.do?action=findAllTerminal', 
			
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
			
			url:'${ctx}/operator/ticket/ajax.do?action=findDriver&terminal='+terminalId+'&tickId='+ticId+'&company='+companyid, 
			
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
			url:'${ctx}/operator/ticket/ajax.do?action=findAllDriver', 
			
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





/* function getDriver()
{
	//var selecteddriver=document.getElementById("driverId").value;
	var selecteddriver= document.getElementById('driverId');
	var driverId=selecteddriver.options[selecteddriver.selectedIndex].value;	
	var selectedterminal= document.getElementById('terminalId');
	var terminalId=selectedterminal.options[selectedterminal.selectedIndex].value;
	
	var ticId=document.getElementById('id').value;
	if(terminalId!='')
	{
	jQuery.ajax({
		
		url:'${ctx}/operator/ticket/ajax.do?action=findDriver&terminal='+terminalId+"&tickId="+ticId, 
		
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
			url:'${ctx}/operator/ticket/ajax.do?action=findAllDriver', 
			
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
} */






function getTransferContents() {
	var transferGross=document.getElementById("transferGrossId").value;
	var transferTare=document.getElementById("transferTareId").value;
	if(transferGross=="" && transferTare==""){
		document.getElementById("transferNetId").value="";
		document.getElementById("transferTons").value="";
	}
	if(transferGross!="" && transferTare!=""){
		jQuery.ajax({
			url:'${ctx}/operator/ticket/ajax.do?action=findTransferContents&transferGross='+transferGross+'&transferTare='+transferTare, 
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var transferNet=listData[0];
				var transferTon=listData[1];
				document.getElementById("transferNetId").value=transferNet;
				document.getElementById("transferTons").value=transferTon;
			}
		});	
	}
}
function getLandfillContents() {
	var landfillGross=document.getElementById("landfillGrossId").value;
	var landfillTare=document.getElementById("landfillTareId").value;
	if(landfillGross=="" && landfillTare==""){
		document.getElementById("LandFillNetId").value="";
		document.getElementById("landfillTons").value="";
	}
	if(landfillGross!="" && landfillTare!=""){
		jQuery.ajax({
			url:'${ctx}/operator/ticket/ajax.do?action=findLandFillContenets&landfillGross='+landfillGross+'&landfillTare='+landfillTare, 
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var landfillNet=listData[0];
				var landfillTon=listData[1];
				document.getElementById("LandFillNetId").value=landfillNet;
				document.getElementById("landfillTons").value=landfillTon;
			}
		});	
	}
}


function checkBatchDate() {
	var billbatchdate=document.getElementById("batch").value;
	var loaddate=document.getElementById("datepicker").value;
	var unloaddate=document.getElementById("datepicker1").value;
	 
	
	
	if(billbatchdate!="" && loaddate!="" && unloaddate!="" ){
		jQuery.ajax({
			url:'${ctx}/operator/ticket/ajax.do?action=checkBillBatch&billbatchdte='+billbatchdate+'&loaddte='+loaddate+'&unloaddte='+unloaddate, 
			success: function( data ) {
				if(data!=''){
				alert(data);
				}
				/* var listData=jQuery.parseJSON(data);
				var landfillNet=listData[0];
				var landfillTon=listData[1];
				document.getElementById("LandFillNetId").value=landfillNet;
				document.getElementById("landfillTons").value=landfillTon; */
				
				
			}
		});	
	}
	
	/* checkVehicleEntry();
	checkTrailerEntry(); */
	
}

/* function checkVehicleEntry() {
	var truck=document.getElementById("truckId").value;	
	var loaddate=document.getElementById("datepicker").value;
	var unloaddate=document.getElementById("datepicker1").value;
	if(truck!="" && loaddate!="" && unloaddate!="" ){
		jQuery.ajax({
			url:'${ctx}/operator/ticket/ajax.do?action=checkVehicle&truckId='+truck+'&loaddte='+loaddate+'&unloaddte='+unloaddate, 
			success: function( data ) {				
				if(data!=''){
					document.getElementById("spanId3").innerHTML="No Valid Vehicle Entry Available for Selected Truck";
					$("#truckId").focus();
				}
			    else{
				     document.getElementById("spanId3").innerHTML="";
			    }
			}
		});	
	}
	
} */

/* function checkTrailerEntry() {
	var trailer=document.getElementById("trailerId").value;	
	var loaddate=document.getElementById("datepicker").value;
	var unloaddate=document.getElementById("datepicker1").value;
	if(trailer!="" && loaddate!="" && unloaddate!="" ){
		jQuery.ajax({
			url:'${ctx}/operator/ticket/ajax.do?action=checkTrailer&trailerId='+trailer+'&loaddte='+loaddate+'&unloaddte='+unloaddate, 
			success: function( data ) {				
				if(data!=''){
					document.getElementById("spanId4").innerHTML="No Valid Vehicle Entry Available for Selected Trailer";
					$("#trailerId").focus();
				}
				else{
					document.getElementById("spanId4").innerHTML="";
				}
			}
		});	
	}
	
} */



function checkLandfillTicket() {
	var landfill=document.getElementById("origin").value;
	var originTicket=document.getElementById("originticket").value;
	var  ticID=document.getElementById("id").value;
	
	if(landfill!="" && originTicket!=""){
		jQuery.ajax({
			url:'${ctx}/operator/ticket/ajax.do?action=checkOriginTicket&landfil='+landfill+'&originTcket='+originTicket+'&ticId='+ticID, 
			success: function( data ) {
				if(data!=''){
					document.getElementById("spanId1").innerHTML="Duplicate Origin Ticket";
					$("#originticket").focus();
				}
				else{
					document.getElementById("spanId1").innerHTML="";
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
			url:'${ctx}/operator/ticket/ajax.do?action=checkDestinationTicket&transferstation='+transferstattion+'&destinationTcket='+destinationTicket+'&ticId='+ticID, 
			success: function( data ) {
				if(data!=''){
					document.getElementById("spanId2").innerHTML="Duplicate Destination Ticket";
					$("#destinationticket").focus();
				}
				else{
					document.getElementById("spanId2").innerHTML="";
				}
			}
		});	
	}
}



function formatDate(){
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
function formatDate1(){
	var date=document.getElementById("datepicker1").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker1").value="";
		$( "#datepicker1" ).focus();
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
						$( "#datepicker1" ).focus();
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					$( "#datepicker1" ).focus();
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					$( "#datepicker1" ).focus();
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker1").value="";
						$( "#datepicker1" ).focus();
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					$( "#datepicker1" ).focus();
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					$( "#datepicker1" ).focus();
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker1").value="";
						$( "#datepicker1" ).focus();
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					$( "#datepicker1" ).focus();
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					$( "#datepicker1" ).focus();
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker1").value=date;
		}
		else{
			if(date.length!=10){
				alert("Invalidte date format");
				document.getElementById("datepicker1").value="";
				$( "#datepicker1" ).focus();
				return true;
			}
		}
	 }
   }
}

</script>
<h3>
	<primo:label code="Add/Update Tickets" />
</h3>
<form:form action="save.do" name="ticketForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Common Information" />
			</b></td>
		</tr>
		<tr>
		
		
		<td class="form-left"><primo:label code="Employee Company" /><span class="errorMessage">*</span>
			</td><td><form:select cssClass="flat" path="driverCompany" id="companyId" onchange="javascript:getTerminal();" style="min-width:154px; max-width:154px" disabled="true">
							<form:option value="">-----Please Select----</form:option>
							<form:options items="${terminals}" itemValue="id" itemLabel="name" />
						</form:select> 
					<br><form:errors path="driverCompany" cssClass="errorMessage" />
				</td>			
		
		
			
			<td class="form-left"><primo:label code="Batch Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}">
    		<form:input id="batch" path="billBatch" style="min-width:150px; max-width:150px" cssClass="flat"  onchange="return checkBatchDate();" disabled="true"/> <br>
    		<form:errors path="billBatch" cssClass="errorMessage" />
       		</td>
		</tr>
	<%-- 	<tr>
			<td class="form-left"><primo:label code="Driver" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="driver" id="driverId"  style="min-width:154px; max-width:154px" onchange="javascript:getTerminal();">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${drivers}" itemValue="id" itemLabel="fullName"></form:options>
				</form:select> <br> <form:errors path="driver" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Terminal" /><span
				class="errorMessage"></span></td>
			<td>
			<c:if test="${empty modelObject.id}">
				<input type="text" name="terminal" id="terminalId" style="min-width:150px; max-width:150px" disabled="disabled" value="${sessionScope.driver.terminal.name}">
				</c:if>
				<c:if test="${not empty modelObject.id}">
				<input type="text" disabled="disabled" style="min-width:150px; max-width:150px"  value="${modelObject.terminal.name}">
				</c:if>
			</td>
		</tr> --%>
		
		
		
		<tr>
			<td class="form-left"><primo:label code="Terminals" /><span
				class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="terminal"
					style="min-width:154px; max-width:154px" id="terminalId"
					onchange="javascript:getDriver();" disabled="true">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${terminalslist}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>




			<td class="form-left"><primo:label code="Driver" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="driver" id="driverId"
					style="min-width:154px; max-width:154px" disabled="true">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${drivers}" itemValue="id"
						itemLabel="fullName"></form:options> 
				</form:select> <br> <form:errors path="driver" cssClass="errorMessage" />
			</td>

		</tr>
		
		
		
		
		<tr>
			<td class="form-left"><primo:label code="Truck" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="vehicle" id="truckId" style="min-width:154px; max-width:154px" onchange="return checkVehicleEntry(); " disabled="true">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${trucks}" itemValue="id" itemLabel="unit" />
				</form:select> <br> <form:errors path="vehicle" cssClass="errorMessage" /><span id="spanId3" style="color:red; font-size:10px; font-weight:bold "> </span>
			</td>
			<td class="form-left"><primo:label code="Trailer" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" id="trailerId" path="trailer" style="min-width:154px; max-width:154px" onchange="return checkTrailerEntry();" disabled="true">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${trailers}" itemValue="id" itemLabel="unit" />
				</form:select> <br> <form:errors path="trailer" cssClass="errorMessage" /><span id="spanId4" style="color:red; font-size:10px; font-weight:bold "> </span>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Entered By" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="createdBy" id="createdBy" style="min-width:154px; max-width:154px" disabled="true">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<c:forEach var="item" items="${operators}">
						<c:set var="selected" value=""/>
						<c:if test="${item.id eq modelObject.createdBy}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${item.id}" ${selected}>${item.name }</option>
					</c:forEach>
				</form:select> <br> <form:errors path="createdBy" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Hold/UnHold" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="ticketStatus" style="min-width:154px; max-width:154px" disabled="true">
			<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${ticketStatus}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="ticketStatus" cssClass="errorMessage" />
			</td>
		</tr>
		<%-- <c:if test="${sessionScope.userInfo.role.id == 1 || sessionScope.userInfo.role.id == 8}"> --%>
		<tr>
		<td class="form-left"><primo:label code="Payroll Pending" /></td>
			<td><form:select cssClass="flat" path="payRollStatus" style="min-width:154px; max-width:154px" >
			<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${payrollPending}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="payRollStatus" cssClass="errorMessage" />
			</td>
			
			
			<td class="form-left"><primo:label code="Subcontractor" /></td>
			<td><form:select cssClass="flat" path="subcontractor" style="min-width:154px; max-width:154px" >
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${subcontractors}" itemValue="id"	itemLabel="name" />
				</form:select> <br> <form:errors path="subcontractor" cssClass="errorMessage" />
			</td>		
		</tr>
		<%-- </c:if> --%>
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Transfer Station/Origin " />
			</b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Origin" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="origin" id="origin" style="min-width:154px; max-width:154px" onchange="return checkLandfillTicket();" disabled="true">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${origins}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="origin" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Origin Ticket#" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="originTicket" id="originticket" style="min-width:150px; max-width:150px" onblur="return checkLandfillTicket();" 
					cssClass="flat" readonly="true"/> <br> <form:errors path="originTicket"
					cssClass="errorMessage" /><span id="spanId1" style="color:red; font-size:11px; font-weight:bold"> </span></td>	
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Load Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker" path="loadDate" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate();" onchange="return checkBatchDate();" disabled="true"/> <br> <form:errors
					path="loadDate" cssClass="errorMessage" /></td>
			<td class="form-left"></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Transfer Time In" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="transfertimein" path="transferTimeIn" style="min-width:150px; max-width:150px"
					cssClass="flat"  onkeypress="return onlyNumbers(event,false)" onblur="return getTime('transfertimein')" readonly="true"/> <br> <form:errors path="transferTimeIn"
					cssClass="errorMessage" /></td>
			<td class="form-left"><primo:label code="Transfer Time Out" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="transfertimeout"  path="transferTimeOut" style="min-width:150px; max-width:150px"
					cssClass="flat" onkeypress="return onlyNumbers(event,false)" onblur="return getTime('transfertimeout')" readonly="true"/> <br> <form:errors path="transferTimeOut"
					cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Transfer Gross" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="transferGross" id="transferGrossId" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="javascript:getTransferContents()" readonly="true"/> <br> <form:errors path="transferGross"
					cssClass="errorMessage" /></td>
			<td class="form-left"><primo:label code="Transfer Tare" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="transferTare" id="transferTareId" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="javascript:getTransferContents()" readonly="true"/> <br> <form:errors path="transferTare"
					cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Transfer Net" /><span
				class="errorMessage">*</span></td>
			<c:choose>
			<c:when test="${modelObject.id!=null && modelObject.transferGross!=null && modelObject.transferTare!=null}">
			<td align="${left}"><form:input path="transferNet" id="transferNetId" style="min-width:150px; max-width:150px"
					Class="flat" readonly="true" /> <br> <form:errors path="transferNet" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="transferNet" id="transferNetId" style="min-width:150px; max-width:150px"
					Class="flat"  readonly="true"  value="" /> <br> <form:errors path="transferNet" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose>
			<td class="form-left"><primo:label code="Transfer Tonns" /><span
				class="errorMessage">*</span></td>
			<td  align="${left}">
				<form:input path="transferTons" cssClass="flat" style="min-width:150px; max-width:150px" readonly="true"/> <br> 
				<form:errors path="transferTons" cssClass="errorMessage" />
			</td>
		</tr>
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Landfill station/Destination" />
			</b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Destination" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="destination" style="min-width:154px; max-width:154px" id="destination" onchange="return checkTransferTicket();" disabled="true">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${destinations}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="destination" cssClass="errorMessage" />
			</td>	
			<td class="form-left"><primo:label code="Destination Ticket#" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="destinationTicket" style="min-width:150px; max-width:150px" id="destinationticket" onblur="return checkTransferTicket();"
					cssClass="flat" readonly="true"/> <br> <form:errors path="destinationTicket"
					cssClass="errorMessage" /><span id="spanId2" style="color:red; font-size:11px; font-weight:bold "> </span></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="UnLoad Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker1"
					path="unloadDate" style="min-width:150px; max-width:150px"  cssClass="flat" onblur="return formatDate1();" onchange="return checkBatchDate();" disabled="true"/> <br> <form:errors
					path="unloadDate" cssClass="errorMessage" /></td>
			<td class="form-left"></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Land Fill Time In" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="landfilltimein" path="landfillTimeIn" style="min-width:150px; max-width:150px"
					cssClass="flat" onkeypress="return onlyNumbers(event,false)" onblur="return getTime('landfilltimein')" readonly="true"/> <br> <form:errors path="landfillTimeIn"
					cssClass="errorMessage" /></td>
			<td class="form-left"><primo:label code="Land Fill Time Out" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="landfilltimeout"  path="landfillTimeOut" style="min-width:150px; max-width:150px"
					cssClass="flat" onkeypress="return onlyNumbers(event,false)" onblur="return getTime('landfilltimeout')" readonly="true"/> <br> <form:errors path="landfillTimeOut"
					cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="LandFill Gross" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="landfillGross" id="landfillGrossId" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="javascript:getLandfillContents()" readonly="true"/> <br> <form:errors path="landfillGross"
					cssClass="errorMessage" /></td>
			<td class="form-left"><primo:label code="LandFill Tare" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="landfillTare" id="landfillTareId" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="javascript:getLandfillContents()" readonly="true"/> <br> <form:errors path="landfillTare"
					cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="LandFill Net" /><span
				class="errorMessage">*</span></td>
			<c:choose>
			<c:when test="${modelObject.id!=null && modelObject.landfillGross!=null && modelObject.landfillTare!=null}">
			<td align="${left}"><form:input path="landfillNet" id="LandFillNetId" style="min-width:150px; max-width:150px"
					Class="flat"  readonly="true" /> <br> <form:errors path="landfillNet" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="landfillNet" id="LandFillNetId" style="min-width:150px; max-width:150px"
					Class="flat"  readonly="true" /> <br> <form:errors path="landfillNet" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose>
			<td class="form-left"><primo:label code="LandFill Tonns" /><span
				class="errorMessage">*</span></td>
			<td  align="${left}">
				<form:input path="landfillTons" cssClass="flat" style="min-width:150px; max-width:150px" readonly="true"/> <br> 
				<form:errors path="landfillTons" cssClass="errorMessage"  />
			</td>
		</tr>
		
		<tr class="table-heading">
			<td colspan="4"><b>Notes</b></td>
		</tr>
		<tr>
			<td align="${left}" colspan="4">
				<form:textarea path="notes" rows="3" cols="90" readonly="true"/>    	
			</td>
		</tr>
		
		
		<tr><td colspan="2"></td> 		
		 </tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
			
				
				<input type="button"
				name="create" id="create" onclick="javascript:submitform('complete');"
				value="<primo:label code="Save"/>" class="flat" />
				
				<%-- <input type="button"
				name="incomplete" id="incomplete" onclick="javascript:submitform('incomplete');"
				value="<primo:label code="Save As Incomplete"/>" class="flat" />  --%>
				
				
				<input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>
<script language="javascript">
getTime('landfilltimein');
getTime('landfilltimeout');
getTime('transfertimein');
getTime('transfertimeout');
getDriver();
getTerminal();
</script>