<%@ include file="/common/taglibs.jsp"%>


<script type="text/javascript">


function submitform(){
	
	
	var start=document.getElementById("startID").value;
	var finish=document.getElementById("endID").value;
	
	if(start!='' && finish!=''){
		jQuery.ajax({				
				url:'${ctx}/admin/odometerreview/ajax.do?action=calcMiles&startReading='+start+'&endReading='+finish,
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
	
	
	document.forms["odometerReviewForm"].action='${ctx}/admin/odometerreview/save.do';
	if(start!='' && finish!=''){
		var miles = document.getElementById("milesId").value;
		if(miles!='')
			document.forms["odometerReviewForm"].submit();
	}
	else{
		document.forms["odometerReviewForm"].submit();
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
				url:'${ctx}/admin/odometerreview/ajax.do?action=calcMiles&startReading='+start+'&endReading='+finish,
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
		url:'${ctx}/admin/odometerreview/ajax.do?action=verifytruck&truck='+truckNum,
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
<form:form action="save.do" name="odometerReviewForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table" cellspacing="1" cellpadding="5"">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Add/Update Odometer Reading" />
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
				 <br> <form:errors path="tempTruck" cssClass="errorMessage" /> <span id="spanId1" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		
		</tr>	
		
		
		<tr>
			<td class="form-left"><primo:label code="Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker" path="recordDate" style="min-width:180px; max-width:180px"
					cssClass="flat" onblur="return formatDate();"/> 
			<br> <form:errors path="recordDate" cssClass="errorMessage" /></td>
		</tr>	
		
		
		
		<tr>
			<td class="form-left"><primo:label code="Start" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="startReading" cssClass="flat" id="startID"
					style="min-width:180px; max-width:180px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles();" onchange="calculateMiles();"/> <br> <form:errors
					path="startReading" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Finish" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="endReading" cssClass="flat" id="endID"
					style="min-width:180px; max-width:180px" maxlength="7" onkeypress="return onlyNumbers(event, false)" onblur="calculateMiles();" onchange="calculateMiles();"/> <br> <form:errors
					path="endReading" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Miles" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="miles" cssClass="flat" id="milesId"
					style="min-width:180px; max-width:180px" maxlength="20" readonly="true" /> <br> <form:errors
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
	</table>
</form:form>
