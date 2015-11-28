<%@ include file="/common/taglibs.jsp"%>


<script type="text/javascript">

$( document ).ready(function() {
	$('#companyError').hide();
	$('#terminalError').hide();
	$('#driverError').hide();
	$('#enteredByError').hide();
	$('#weekDateError').hide();
	
	var weekOfDate = document.getElementById('datepicker1').value;
	if(weekOfDate!=''){
		getAllDaysOfWeek(weekOfDate);
	}
	getTerminal();
	getDriver();
	
});



function submitform(){	
	document.forms["bulkodometerReviewForm"].action='${ctx}/admin/bulkodometerreview/save.do';
	
	var selecteddriver= document.getElementById('driverId');
	var driver=selecteddriver.options[selecteddriver.selectedIndex].value;
	
	var weekOfDate = document.getElementById('datepicker1').value;	

	var submittingError = false;
		
	
	
	if(driver==''){
		submittingError = true;
		$('#driverError').show();
	}
	else{
		$('#driverError').hide();
	}
	
	if(weekOfDate==''){
		submittingError = true;
		$('#weekDateError').show();
	}
	else{
		$('#weekDateError').hide();
	}
	
	
	
	if(submittingError) {

    }
    else{    	
	 	document.forms["bulkodometerReviewForm"].submit();    	
    }
}



function calculateMiles(startID, endID, milesId,startspanId,endspanId,truckId){
	
	var start=document.getElementById(startID).value;
	var finish=document.getElementById(endID).value;
	
	
	var truckNum = document.getElementById(truckId).value;
	if(truckNum!='' && start!=''){
	jQuery.ajax({				
		url:'${ctx}/admin/bulkodometerreview/ajax.do?action=verifytruckandreading&truck='+truckNum+'&startreading='+start,
		success: function( data ) {
			if(data!=''){
				document.getElementById(startspanId).innerHTML="Start value is less than previous end value";
				
			}
			else{
				document.getElementById(startspanId).innerHTML="";
			}			
		}
	});
	}
	
	
	if(start=='' && finish!=''){
		document.getElementById(startspanId).innerHTML="Start reading  is required";
		document.getElementById(milesId).value="";
	}	
	else if(start!='' && finish!=''){
		document.getElementById(startspanId).innerHTML="";
		if(parseInt(finish)<parseInt(start)){
			document.getElementById(endspanId).innerHTML="End reading is less than start";
			document.getElementById(milesId).value="";
		}
		else{
			document.getElementById(startspanId).innerHTML="";
			document.getElementById(endspanId).innerHTML="";
			jQuery.ajax({				
				url:'${ctx}/admin/bulkodometerreview/ajax.do?action=calcMiles&startReading='+start+'&endReading='+finish,
				success: function( data ) {
				var listData=jQuery.parseJSON(data);
				if(listData)
					document.getElementById(milesId).value=listData[0];			
			} 
			});
		}
	}
	else{
		document.getElementById(milesId).value="";
		document.getElementById(startspanId).innerHTML="";
		document.getElementById(endspanId).innerHTML="";
	}
}


function verifyTruck(truckId,spanId){
	var truckNum = document.getElementById(truckId).value;
	if(truckNum!=''){
	jQuery.ajax({				
		url:'${ctx}/admin/bulkodometerreview/ajax.do?action=verifytruck&truck='+truckNum,
		success: function( data ) {
			if(data!=''){
				document.getElementById(spanId).innerHTML="Invalid truck number";
				
			}
			else{
				document.getElementById(spanId).innerHTML="";
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
			url:'${ctx}/admin/bulkodometerreview/ajax.do?action=fetchcomterm&driver='+driver,
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


function formatDate1(){
	var date=document.getElementById("datepicker1").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalid date format");
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
			getAllDaysOfWeek(date);
		}		
		getAllDaysOfWeek(date);		
	 }
   }
	else{
		document.getElementById("dayId1").value="";
		document.getElementById("dayId2").value="";
		document.getElementById("dayId3").value="";
		document.getElementById("dayId4").value="";
		document.getElementById("dayId5").value="";
		document.getElementById("dayId6").value="";
		document.getElementById("dayId7").value="";
	}
		
}


function getAllDaysOfWeek(date){
	jQuery.ajax({				
		url:'${ctx}/admin/bulkodometerreview/ajax.do?action=allDaysOfWeek&batchDate='+date,
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			if(listData)
			document.getElementById("dayId1").value=listData[0];
			document.getElementById("dayId2").value=listData[1];
			document.getElementById("dayId3").value=listData[2];
			document.getElementById("dayId4").value=listData[3];
			document.getElementById("dayId5").value=listData[4];
			document.getElementById("dayId6").value=listData[5];
			document.getElementById("dayId7").value=listData[6];
		}
	});
}



</script>
<br/>
<form:form action="save.do" name="bulkodometerReviewForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table" cellspacing="1" cellpadding="5"">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Add Odometer Reading" />
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
			</form:select> <br> <div id="driverError" class="errorMessage">This field is required</div>
		</td>		
		
		<td class="form-left"><primo:label code="Company" /></td>	
		<td><form:input  cssClass="flat" path="companyName" id="companyId" style="min-width:180px; max-width:180px;" readonly="true"/>
				
		</td>
		
		<td class="form-left"><primo:label code="Terminal" /></td>	
		<td><form:input  cssClass="flat" path="terminalName" id="terminalId" style="min-width:180px; max-width:180px;" readonly="true"/>
				 
		</td>
				
		</tr>
		
		<%-- <tr>
		<td class="form-left"><primo:label code="Truck#" /><span class="errorMessage">*</span></td>
		<td><form:input  cssClass="flat" path="tempTruck" id="truckId" style="min-width:180px; max-width:180px;" maxlength="8" onkeypress="return onlyNumbers(event, false)" onblur="verifyTruck();" onchange="verifyTruck();"/>
				 <br> <form:errors path="tempTruck" cssClass="errorMessage" /> <span id="spanId1" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		
		</tr> --%>	
		
		
		<tr>
		<td class="form-left"><primo:label code="Week of Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker1" path="recordDate" style="min-width:180px; max-width:180px"
					cssClass="flat" onblur="return formatDate1();" onchange="return formatDate1();"/> <br> 
					<div id="weekDateError" class="errorMessage">This field is required</div>
			</td>
		</tr>
		
		<tr class="table-heading">
			<td colspan="16"><b><primo:label code="Daily Readings" /></b></td>
		</tr>
		
		<tr>
		<td class="form-left" width="75px"><b><primo:label code="Day" /></b></td>
		<td class="form-left" width="75px"><b><primo:label code="Date" /></b></td>
		<td class="form-left" width="75px"><b><primo:label code="Truck" /></b></td>
		<td class="form-left" width="75px"><b><primo:label code="Start" /></b></td>
		<td class="form-left" width="75px"><b><primo:label code="End" /></b></td>
		<td class="form-left" width="75px"><b><primo:label code="Miles" /></b></td>
		</tr>
		
		<tr>
		<td class="form-left"><primo:label code="Monday" />			
		</td>
		<td>
		<input type="text" id="dayId1" readonly="readonly" name="day1" style="min-width:185px; max-width:185px"/>
		</td>
		<td align="${left}"><input type="text" name="truck1" class="flat" id="truckID1"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="verifyTruck('truckID1','spanId1');" onchange="verifyTruck('truckID1','spanId1');"/> 
					<br><span id="spanId1" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}">
		<input type="text" name="startReading1" class="flat" id="startID1"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles('startID1','endID1','milesId1','startspanId1','endspanId1','truckID1');" onchange="calculateMiles('startID1','endID1','milesId1','startspanId1','endspanId1','truckID1');"/> 
					<br><span id="startspanId1" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}"><input type="text" name="endReading1" class="flat" id="endID1"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles('startID1','endID1','milesId1','startspanId1','endspanId1','truckID1');" onchange="calculateMiles('startID1','endID1','milesId1','startspanId1','endspanId1','truckID1');"/> 
					<br><span id="endspanId1" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}"><input type="text" name="miles1" class="flat" id="milesId1" readonly="readonly"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" /> 
		</td>
		<td>
		<div id="error1"></div>
		</td>
		
		</tr>
		
		
		
		<tr>
		<td class="form-left"><primo:label code="Tuesday" />			
		</td>
		<td>
		<input type="text" id="dayId2" readonly="readonly" name="day2" style="min-width:185px; max-width:185px"/>
		</td>
		<td align="${left}"><input type="text" name="truck2" class="flat" id="truckID2"
					style="min-width:185px; max-width:185px"  onkeypress="return onlyNumbers(event, false)" onblur="verifyTruck('truckID2','spanId2');" onchange="verifyTruck('truckID2','spanId2');"/> 
					<br><span id="spanId2" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}">
		<input type="text" name="startReading2" class="flat" id="startID2"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles('startID2','endID2','milesId2','startspanId2','endspanId2','truckID2');" onchange="calculateMiles('startID2','endID2','milesId2','startspanId2','endspanId2','truckID2');"/> 
					<br><span id="startspanId2" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}"><input type="text" name="endReading2" class="flat" id="endID2"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles('startID2','endID2','milesId2','startspanId2','endspanId2','truckID2');" onchange="calculateMiles('startID2','endID2','milesId2','startspanId2','endspanId2','truckID2');"/> 
					<br><span id="endspanId2" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}"><input type="text" name="miles2" class="flat" id="milesId2" readonly="readonly"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" /> 
		</td>
		<td>
		<div id="error2"></div>
		</td>
		
		</tr>
		
		
		
		<tr>
		<td class="form-left"><primo:label code="Wednesday" />			
		</td>
		<td>
		<input type="text" id="dayId3" readonly="readonly" name="day3" style="min-width:185px; max-width:185px"/>
		</td>
		<td align="${left}"><input type="text" name="truck3" class="flat" id="truckID3"
					style="min-width:185px; max-width:185px"  onkeypress="return onlyNumbers(event, false)" onblur="verifyTruck('truckID3','spanId3');" onchange="verifyTruck('truckID3','spanId3');"/> 
					<br><span id="spanId3" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}">
		<input type="text" name="startReading3" class="flat" id="startID3"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles('startID3','endID3','milesId3','startspanId3','endspanId3','truckID3');" onchange="calculateMiles('startID3','endID3','milesId3','startspanId3','endspanId3','truckID3');"/> 
					<br><span id="startspanId3" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}"><input type="text" name="endReading3" class="flat" id="endID3"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles('startID3','endID3','milesId3','startspanId3','endspanId3','truckID3');" onchange="calculateMiles('startID3','endID3','milesId3','startspanId3','endspanId3','truckID3');"/> 
					<br><span id="endspanId3" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}"><input type="text" name="mile3" class="flat" id="milesId3" readonly="readonly"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" /> 
		</td>
		<td>
		<div id="error3"></div>
		</td>
		
		</tr>
		
		
		<tr>
		<td class="form-left"><primo:label code="Thursday" />			
		</td>
		<td>
		<input type="text" id="dayId4" readonly="readonly" name="day4" style="min-width:185px; max-width:185px"/>
		</td>
		<td align="${left}"><input type="text" name="truck4" class="flat" id="truckID4"
					style="min-width:185px; max-width:185px"  onkeypress="return onlyNumbers(event, false)" onblur="verifyTruck('truckID4','spanId4');" onchange="verifyTruck('truckID4','spanId4');"/> 
					<br><span id="spanId4" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}">
		<input type="text" name="startReading4" class="flat" id="startID4"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles('startID4','endID4','milesId4','startspanId4','endspanId4','truckID4');" onchange="calculateMiles('startID74','endID4','milesId4','startspanId4','endspanId4','truckID4');"/> 
					<br><span id="startspanId4" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}"><input type="text" name="endReading4" class="flat" id="endID4"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles('startID4','endID4','milesId4','startspanId4','endspanId4','truckID4');" onchange="calculateMiles('startID4','endID4','milesId4','startspanId4','endspanId4','truckID4');"/> 
					<br><span id="endspanId4" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}"><input type="text" name="miles4" class="flat" id="milesId4" readonly="readonly"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" /> 
		</td>
		<td>
		<div id="error4"></div>
		</td>
		
		</tr>
		
		
		<tr>
		<td class="form-left"><primo:label code="Friday" />			
		</td>
		<td>
		<input type="text" id="dayId5" readonly="readonly" name="day5" style="min-width:185px; max-width:185px"/>
		</td>
		<td align="${left}"><input type="text" name="truck5" class="flat" id="truckID5"
					style="min-width:185px; max-width:185px"  onkeypress="return onlyNumbers(event, false)" onblur="verifyTruck('truckID5','spanId5');" onchange="verifyTruck('truckID5','spanId5');"/> 
					<br><span id="spanId5" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}">
		<input type="text" name="startReading5" class="flat" id="startID5"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles('startID5','endID5','milesId5','startspanId5','endspanId5','truckID5');" onchange="calculateMiles('startID5','endID5','milesId5','startspanId5','endspanId5','truckID5');"/> 
					<br><span id="startspanId5" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}"><input type="text" name="endReading5" class="flat" id="endID5"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles('startID5','endID5','milesId5','startspanId5','endspanId5','truckID5');" onchange="calculateMiles('startID5','endID5','milesId5','startspanId5','endspanId5','truckID5');"/> 
					<br><span id="endspanId5" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}"><input type="text" name="miles5" class="flat" id="milesId5" readonly="readonly"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" /> 
		</td>
		<td>
		<div id="error5"></div>
		</td>
		
		</tr>
		
		
		<tr>
		<td class="form-left"><primo:label code="Saturday" />			
		</td>
		<td>
		<input type="text" id="dayId6" readonly="readonly" name="day6" style="min-width:185px; max-width:185px"/>
		</td>
		<td align="${left}"><input type="text" name="truck6" class="flat" id="truckID6"
					style="min-width:185px; max-width:185px"  onkeypress="return onlyNumbers(event, false)" onblur="verifyTruck('truckID6','spanId6');" onchange="verifyTruck('truckID6','spanId6');"/> 
					<br><span id="spanId6" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}">
		<input type="text" name="startReading6" class="flat" id="startID6"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles('startID6','endID6','milesId6','startspanId6','endspanId6','truckID6');" onchange="calculateMiles('startID6','endID6','milesId6','startspanId6','endspanId6','truckID6');"/> 
					<br><span id="startspanId6" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}"><input type="text" name="endReading6" class="flat" id="endID6"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles('startID6','endID6','milesId6','startspanId6','endspanId6','truckID6');" onchange="calculateMiles('startID6','endID6','milesId6','startspanId6','endspanId6','truckID6');"/> 
					<br><span id="endspanId6" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}"><input type="text" name="miles6" class="flat" id="milesId6" readonly="readonly"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" /> 
		</td>
		<td>
		<div id="error6"></div>
		</td>
		
		</tr>
		
		
		<tr>
		<td class="form-left"><primo:label code="Sunday" />			
		</td>
		<td>
		<input type="text" id="dayId7" readonly="readonly" name="day7" style="min-width:185px; max-width:185px"/>
		</td>
		<td align="${left}"><input type="text" name="truck7" class="flat" id="truckID7"
					style="min-width:185px; max-width:185px"  onkeypress="return onlyNumbers(event, false)" onblur="verifyTruck('truckID7','spanId7');" onchange="verifyTruck('truckID7','spanId7');"/> 
					<br><span id="spanId7" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}">
		<input type="text" name="startReading7" class="flat" id="startID7"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles('startID7','endID7','milesId7','startspanId7','endspanId7','truckID7');" onchange="calculateMiles('startID7','endID7','milesId7','startspanId7','endspanId7','truckID7');"/> 
					<br><span id="startspanId7" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}"><input type="text" name="endReading7" class="flat" id="endID7"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles('startID7','endID7','milesId7','startspanId7','endspanId7','truckID7');" onchange="calculateMiles('startID7','endID7','milesId7','startspanId7','endspanId7','truckID7');"/> 
					<br><span id="endspanId7" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		<td align="${left}"><input type="text" name="miles7" class="flat" id="milesId7" readonly="readonly"
					style="min-width:185px; max-width:185px" maxlength="7" onkeypress="return onlyNumbers(event, false)" /> 
		</td>
		<td>
		<div id="error7"></div>
		</td>
		
		</tr>	
		
		
		
		
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="button"
				name="create" id="create" onclick="javascript:submitform();"
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> </td>
		</tr>
	</table>
		
		
	
</form:form>
