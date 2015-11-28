<%@ include file="/common/taglibs.jsp"%>

<style type="text/css">
body {
	
	color: black;
}

.form-left {
    font-size: 12px;
	font-weight: bold;
	width: 140px;
	margin: 0px auto;
	text-align: left;
	background: #eaeaea;
}

.flat {
font-size: 13px;
color: black;
}

.flat2 {
font-size: 12px;
color: black;
}
</style>

<script type="text/javascript">


function submitform(){
	
	
	var start=document.getElementById("startID").value;
	var finish=document.getElementById("endID").value;
	
	if(start!='' && finish!=''){
		jQuery.ajax({				
				url:'${ctx}/driver/odometer/ajax.do?action=calcMiles&startReading='+start+'&endReading='+finish,
				success: function( data ) {
				var listData=jQuery.parseJSON(data);
				if(listData)
					document.getElementById("milesId").value=listData[0];			
			}
			});
	}
	else{
		document.getElementById("milesId").value="";
	}
	
	
	document.forms["odometerForm"].action='${ctx}/driver/odometer/save.do';
	if(start!='' && finish!=''){
		var miles = document.getElementById("milesId").value;
		if(miles!='')
			document.forms["odometerForm"].submit();
	}
	else{
		document.forms["odometerForm"].submit();
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

function calculateMiles(){
	
	var start=document.getElementById("startID").value;
	var finish=document.getElementById("endID").value;
	
	if(start!='' && finish!=''){
		jQuery.ajax({				
				url:'${ctx}/driver/odometer/ajax.do?action=calcMiles&startReading='+start+'&endReading='+finish,
				success: function( data ) {
				var listData=jQuery.parseJSON(data);
				if(listData)
					document.getElementById("milesId").value=listData[0];			
			}
			});
	}
	else{
		document.getElementById("milesId").value="";
	}
}


function verifyTruck(){
	var truckNum = document.getElementById("truckId").value;
	if(truckNum!=''){
	jQuery.ajax({				
		url:'${ctx}/driver/tripsheet/ajax.do?action=verifytruck&truck='+truckNum,
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


</script>
<br/>
<form:form action="save.do" name="odometerForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table" cellspacing="1" cellpadding="5"">
		<tr class="table-heading">
			<td colspan="2"><b><primo:label code="Add/Update Odometer Reading" />
			</b>
			</td>
		</tr>	
		
		
		<tr>
		<td class="form-left"><primo:label code="Truck#" /><span class="errorMessage">*</span></td>
		<td><form:input  cssClass="flat" path="tempTruck" id="truckId" autocomplete="off" type="tel"  style="min-width:180px; max-width:180px;" maxlength="8" onkeypress="return onlyNumbers(event, false)" onblur="verifyTruck();" onchange="verifyTruck();"/>
				 <br> <form:errors path="tempTruck" cssClass="errorMessage" /> <span id="spanId1" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		
		</tr>	
		
		
		<tr>
			<td class="form-left"><primo:label code="Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker" path="recordDate" autocomplete="off" style="min-width:180px; max-width:180px"
					cssClass="flat" onblur="return formatDate();"/> 
			<br> <form:errors path="recordDate" cssClass="errorMessage" /></td>
		</tr>	
		
		
		
		<tr>
			<td class="form-left"><primo:label code="Start" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="startReading" cssClass="flat" id="startID"
					style="min-width:180px; max-width:180px" maxlength="7" autocomplete="off" type="tel" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles();" onchange="calculateMiles();"/> <br> <form:errors
					path="startReading" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Finish" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="endReading" cssClass="flat" id="endID"
					style="min-width:180px; max-width:180px" maxlength="7" autocomplete="off" type="tel" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles();" onchange="calculateMiles();"/> <br> <form:errors
					path="endReading" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Miles" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="miles" cssClass="flat" id="milesId"
					style="min-width:180px; max-width:180px" maxlength="20" type="tel" readonly="true" /> <br> <form:errors
					path="miles" cssClass="errorMessage" />
			</td>
		</tr>
		
		
		
		
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick="javascript:submitform();"
				value="<primo:label code="Save"/>" class="flat2" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat2" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat2"
				onClick="location.href='list.do'" /></td>
		</tr>
		
		<tr>			
			<td align="${left}" colspan="3">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" id="Btn1"	value="<primo:label code="&nbsp;Add Trip Sheet&nbsp;"/>" class="flat2"
				onClick="location.href='/trans/driver/tripsheet/create.do'" />	
				<input type="button" id="Btn2"	value="<primo:label code="&nbsp;&nbsp;Add Fuel Log&nbsp;&nbsp;"/>" class="flat2"
				onClick="location.href='/trans/driver/fuellog/create.do'" />				
			</td>
		</tr>
	</table>
</form:form>
