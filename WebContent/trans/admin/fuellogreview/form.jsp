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


function verifyTruck(){
	var truckNum = document.getElementById("truckId").value;
	if(truckNum!=''){
	jQuery.ajax({				
		url:'${ctx}/admin/fuellogreview/ajax.do?action=verifytruck&truck='+truckNum,
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

function findFuelCard() {
		var slecteddriver = document.getElementById("driverId");
		var driver = slecteddriver.options[slecteddriver.selectedIndex].value;
		if (driver != "") {
			jQuery
					.ajax({
						url : '${ctx}/admin/fuellogreview/ajax.do?action=findFuelCard&driver='
								+ driver,
						success : function(data) {
							var listData = jQuery.parseJSON(data);

							var options = '<option value="">------Please Select------</option>';
							for ( var i = 0; i < listData.length; i++) {
								var driverfuelcard = listData[i];
								options += '<option value="'+driverfuelcard.id+'">'
										+ driverfuelcard.fuelcard.fuelcardNum										
										+ '</option>';
							}
							$("#fuelCardId").html(options);							
						}
					});
			getComterminal();
		} else {
			var options = '<option value="">------Please Select------</option>';
			$("#fuelCardId").html(options);
			getComterminal();
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
<form:form action="save.do" name="driverFuelLogReviewForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table" cellspacing="1" cellpadding="5"">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Add/Update Fuel Log" />
			</b>
			</td>
		</tr>	
		
		<tr>
		<td class="form-left"><primo:label code="Driver" /><span class="errorMessage">*</span></td>		
		<td><form:select cssClass="flat" path="driver" id="driverId"
					style="min-width:184px; max-width:184px" onchange="findFuelCard();">
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
		<td><form:input  cssClass="flat" path="tempTruck" id="truckId" style="min-width:180px; max-width:180px;" maxlength="20" onkeypress="return onlyNumbers(event, false)" onblur="verifyTruck();" onchange="verifyTruck();"/>
				 <br> <form:errors path="tempTruck" cssClass="errorMessage" /><span id="spanId1" style="color:red; font-size:11px; font-weight:bold"> </span>
		</td>
		
		</tr>	
		
		
		<tr>
			<td class="form-left"><primo:label code="Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker" path="transactionDate" style="min-width:180px; max-width:180px"
					cssClass="flat" onblur="return formatDate();"/> 
			<br> <form:errors path="transactionDate" cssClass="errorMessage" /></td>
		</tr>
		
		
		
		
		
		
		<tr>
			<td class="form-left"><primo:label code="Gallons" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="gallons" cssClass="flat"
					style="min-width:180px; max-width:180px" maxlength="20" onkeypress="return onlyNumbers(event, true)"/> <br> <form:errors
					path="gallons" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
		<td class="form-left"><primo:label code="Fuel Card" /></td>
		<td><form:select  cssClass="flat" path="driverFuelCard" id="fuelCardId" style="min-width:184px; max-width:184px;" >
				<form:option value="">------<primo:label code="Please Select" />------</form:option>
				<form:options items="${fuelcards}" itemValue="id" itemLabel="fuelcard.fuelcardNum" />
				</form:select> <br> <form:errors path="driverFuelCard" cssClass="errorMessage" />
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
