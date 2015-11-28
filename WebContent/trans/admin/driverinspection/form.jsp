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
	document.forms["driverinspectionForm"].action='${ctx}/admin/driverinspection/savedriverinspection.do';
	
	var selecteddriver= document.getElementById('driverID');
	var driver=selecteddriver.options[selecteddriver.selectedIndex].value;
	
	var selectedcompany = document.getElementById('companyID');
	var company = selectedcompany.options[selectedcompany.selectedIndex].value;
	
	var selectedterminal = document.getElementById('terminalID');
	var terminal = selectedterminal.options[selectedterminal.selectedIndex].value;
	
	var weekOfDate = document.getElementById('datepicker1').value;
	
	var selectedenterBy = document.getElementById('enteredByID');
	var enteredBy = selectedenterBy.options[selectedenterBy.selectedIndex].value;
	
	var submittingError = false;
	if(company==''){
		submittingError = true;
		$('#companyError').show();		
	}
	else{
		$('#companyError').hide();
	}
	
	if(terminal==''){
		submittingError = true;
		$('#terminalError').show();
	}
	else{
		$('#terminalError').hide();
	}
	
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
	
	if(enteredBy==''){
		submittingError = true;
		$('#enteredByError').show();
	}
	else{
		$('#enteredByError').hide();
	}
	
	if(submittingError) {

    }
    else{        	
    	var mondaydate = document.getElementById('dayid1').value;
    	
    	if(weekOfDate==mondaydate){
    		$('#weekDateError').hide();
	 		 document.forms["driverinspectionForm"].submit();
    	}else{
    		$('#weekDateError').html("Week of Date must be Monday Date");
    	    $('#weekDateError').show();
    	}
    }
}

function checkUncheckModified(currObj) {
    var checked;
    if (currObj.checked==true){
        checked=true;
    }
    else {
        checked=false;
    }
    
    for(var i=0;i<7;i++){
    
    if(checked){
    	document.getElementById("dayID"+i).checked = checked;
    }
    else{
    	document.getElementById("dayID"+i).checked = checked;
    }
    	
    }    
}

function checkUncheckModified2(currObj) {
    var checked;
    if (currObj.checked==true){
        checked=true;
    }
    else {
        checked=false;
    }
    
    for(var i=0;i<6;i++){
    
    if(checked){
    	document.getElementById("dayID"+i).checked = checked;
    }
    else{
    	document.getElementById("dayID"+i).checked = checked;
    }
    	
    }    
}



function getTerminal()
{
	//var selecteddriver=document.getElementById("driverId").value;
	var selectedcompany = document.getElementById('companyID');
	var companyid = selectedcompany.options[selectedcompany.selectedIndex].value;
	var selectedterminal= document.getElementById('terminalID');
	var terminalId=selectedterminal.options[selectedterminal.selectedIndex].value;
	
	if(companyid!='')
	{
	jQuery.ajax({
		
		url:'${ctx}/admin/driverinspection/ajax.do?action=findTerminal&company='+companyid, 
		
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
				$("#terminalID").html(options);
		}
	});	
	}
	else
	{
		jQuery.ajax({
			url:'${ctx}/admin/driverinspection/ajax.do?action=findAllTerminal', 
			
				success: function( data ) {
					var listData=jQuery.parseJSON(data);
					var options = '<option value="">------Please Select------</option>';
					for (var i = 0; i <listData.length; i++) {
						var dlst=listData[i];
						options += '<option value="'+dlst.id+'">'+dlst.name+'</option>';
					  }
					$("#terminalID").html(options);
			}
		});	
	}
}




function getDriver()
{
	//var selecteddriver=document.getElementById("driverId").value;
	var selectedcompany = document.getElementById('companyID');
	var companyid = selectedcompany.options[selectedcompany.selectedIndex].value;
	var selecteddriver= document.getElementById('driverID');
	var driverId=selecteddriver.options[selecteddriver.selectedIndex].value;	
	var selectedterminal= document.getElementById('terminalID');
	var terminalId=selectedterminal.options[selectedterminal.selectedIndex].value;
	
	
	if(terminalId!='')
	{
	jQuery.ajax({
		
		url:'${ctx}/admin/driverinspection/ajax.do?action=findDriver&terminal='+terminalId+'&company='+companyid,  
		
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
				$("#driverID").html(options);
		}
	});	
	}
	else
	{
		jQuery.ajax({
			url:'${ctx}/admin/driverinspection/ajax.do?action=findAllDriver', 
			
				success: function( data ) {
					var listData=jQuery.parseJSON(data);
					var options = '<option value="">------Please Select------</option>';
					for (var i = 0; i <listData.length; i++) {
						var dlst=listData[i];
						options += '<option value="'+dlst.id+'">'+dlst.fullName+'</option>';
					  }
					$("#driverID").html(options);
			}
		});	
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
		document.getElementById("dayid1").value="";
		document.getElementById("dayid2").value="";
		document.getElementById("dayid3").value="";
		document.getElementById("dayid4").value="";
		document.getElementById("dayid5").value="";
		document.getElementById("dayid6").value="";
		document.getElementById("dayid7").value="";
	}
		
}


function getAllDaysOfWeek(date){
	jQuery.ajax({				
		url:'${ctx}/admin/driverinspection/ajax.do?action=allDaysOfWeek&batchDate='+date,
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			if(listData)
			document.getElementById("dayid1").value=listData[0];
			document.getElementById("dayid2").value=listData[1];
			document.getElementById("dayid3").value=listData[2];
			document.getElementById("dayid4").value=listData[3];
			document.getElementById("dayid5").value=listData[4];
			document.getElementById("dayid6").value=listData[5];
			document.getElementById("dayid7").value=listData[6];
		}
	});
}




</script>

<h3><primo:label code="Add/Update Driver Inspection Data"/></h3>

<form:form action="savedriverinspection.do" name="driverinspectionForm"
	commandName="modelObject" method="post" >
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="2"><b><primo:label code="Add/Update Driver Inspection Data" />
			</b>
			</td>
		</tr>	
		
		
		<tr>
		<td class="form-left"><primo:label code="Employee Company" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" id="companyID" onchange="javascript:getTerminal();" path="company" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> <br> <div id="companyError" class="errorMessage">This field is required</div>
			</td>
		</tr>
		
		
		<tr>
		<td class="form-left"><primo:label code="Terminals" /><span
				class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="terminal"
					style="min-width:154px; max-width:154px" id="terminalID"
					onchange="javascript:getDriver();">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${terminals}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <div id="terminalError" class="errorMessage">This field is required</div>
			</td>
		
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Driver" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="driver" id="driverID"
					style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${drivers}" itemValue="id"
						itemLabel="fullName"></form:options> 
				</form:select> <br> <div id="driverError" class="errorMessage">This field is required</div>
			</td>
		</tr>
		
		
		<tr>
		<td class="form-left"><primo:label code="Entered By" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="enteredBy" id="enteredByID" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<c:forEach var="item" items="${operators}">
						<c:set var="selected" value=""/>
						<c:if test="${item.id eq modelObject.enteredBy}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${item.id}" ${selected}>${item.name }</option>
					</c:forEach>
				</form:select> <br> <div id="enteredByError" class="errorMessage" >This field is required</div>
			</td>
		</tr>
		
		
		<tr>
		<td class="form-left"><primo:label code="Week of Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker1" path="weekOfDate" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate1();" onchange="return formatDate1();"/> <br> 
					<div id="weekDateError" class="errorMessage">This field is required</div>
			</td>
		</tr>
		 
		<tr class="table-heading">
			<td colspan="2"><b><primo:label code="Inspection Dates" />
			</b>
			</td>
		</tr>
		<tr>
		
		<td colspan="2">
		<table>
		<tr>
		<td width="70px" style="font-weight: bold;margin: 0px auto "><primo:label code="Select All" /></td>
		<td><input type="checkbox" id="checkId" onclick="javascript:checkUncheckModified(this);"></td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td width="120px" style="font-weight: bold;margin: 0px auto "><primo:label code="Select All Except Sunday" /></td>
		<td ><input type="checkbox" id="checkId2" onclick="javascript:checkUncheckModified2(this);"></td>
		</tr>
		</table>
		</td>
	
		</tr>
		
		
		
		<tr>
		<td class="form-left"><primo:label code="Monday" />			
		</td>
		<td>
		<form:checkbox id="dayID0" value="day1" path="daysList"></form:checkbox>
		<input type="text" id="dayid1" readonly="readonly" name="day1" style="min-width:85px; max-width:85px"/>
		</td>
		</tr>
		
		<tr>
		<td class="form-left"><primo:label code="Tuesday" />
		</td>
		<td>
		<form:checkbox id="dayID1"  value="day2" path="daysList"></form:checkbox>
		<input type="text" id="dayid2" readonly="readonly" name="day2" style="min-width:85px; max-width:85px" />
		</td>
		</tr>
		
		
		
		<tr>
		<td class="form-left"><primo:label code="Wednesday" />
		</td>
		<td>
		<form:checkbox id="dayID2"  value="day3" path="daysList"></form:checkbox>
		<input type="text" id="dayid3" readonly="readonly" name="day3" style="min-width:85px; max-width:85px"/>
		</td>
		</tr>
	
		<tr>		
		<td class="form-left"><primo:label code="Thursday" />
		</td>
		<td>
		<form:checkbox id="dayID3"  value="day4" path="daysList"></form:checkbox>
		<input type="text" id="dayid4" readonly="readonly" name="day4" style="min-width:85px; max-width:85px"/>
		</td>
		</tr>
		
		<tr>
		<td class="form-left"><primo:label code="Friday" />
		</td>
		<td>
		<form:checkbox id="dayID4"  value="day5" path="daysList"></form:checkbox>
		<input type="text" id="dayid5" readonly="readonly" name="day5" style="min-width:85px; max-width:85px"/>
		</td>
		</tr>
		
		<tr>
		<td class="form-left"><primo:label code="Saturday" />
		</td>
		<td>
		<form:checkbox id="dayID5" value="day6" path="daysList"></form:checkbox>
		<input type="text" id="dayid6" readonly="readonly" name="day6" style="min-width:85px; max-width:85px"/>
		</td>
		</tr>
		
		<tr>
		<td class="form-left"><primo:label code="Sunday" />
		</td>
		<td>
		<form:checkbox id="dayID6"  value="day7" path="daysList"></form:checkbox>
		<input type="text" id="dayid7" readonly="readonly" name="day7" style="min-width:85px; max-width:85px"/>
		
		</td>
		</tr>
		
		
		
		
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="button"
				name="create" id="create" onclick="javascript:submitform();"
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do?rst=1'" /></td>
		</tr>
	</table>
</form:form>