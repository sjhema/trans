<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function disp(){
	var selectedleaveType= document.getElementById('leaveType');
	var leaveType=selectedleaveType.options[selectedleaveType.selectedIndex].value;
	if(leaveType==3)
		holiday()
    if(leaveType==4)
    	vacation()
    if(leaveType!=3&&leaveType!=4)
    	all()
    if(leaveType==1)
    	sick();
}

 function holiday(){
	 document.getElementById('beforeafter').style.display="";
	 document.getElementById('workweek').style.display="";
	 document.getElementById('priornotice').style.display="none";
	 document.getElementById('novaction').style.display="none";
 }
 function vacation(){
	 document.getElementById('beforeafter').style.display="none";
	 document.getElementById('workweek').style.display="none";
	 document.getElementById('priornotice').style.display="";
	 document.getElementById('novaction').style.display="";
	 
 }
 function all(){
	 document.getElementById('beforeafter').style.display="";
	 document.getElementById('workweek').style.display="";
	 document.getElementById('priornotice').style.display="";
	 document.getElementById('novaction').style.display="";
 }
 function sick(){
	 document.getElementById('novaction').style.display="none";
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
<h3>
	<primo:label code="Add/Update Eligibility" />
</h3>
<form:form action="save.do" name="eligibilityForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Add/Update Eligibility"/></b></td>
		</tr>
			<tr>
		<td class="form-left"><primo:label code="Category" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="catagory" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${catagories}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="catagory" cssClass="errorMessage" />
			</td>
		</tr>
			<tr>
		<td class="form-left"><primo:label code="Company" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="company" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Terminal" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="terminal" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Leave Type" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="leaveType" id="leaveType" style="min-width:154px; max-width:154px" onchange="javascript:disp()">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${leavetypes}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="leaveType" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td><td></tr>
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Eligibility for Probation" />
			</b></td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Probation Period" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="probationDays" style="min-width:150px; max-width:150px"
					cssClass="flat" /><span style="color: black">Days</span> 
			<br> <form:errors path="probationDays" cssClass="errorMessage" /></td>
			<td class="form-left"><primo:label code="Years" /><span
				class="errorMessage"></span></td>
			<td align="${left}">
			<form:select cssClass="flat" path="probationYear" style="min-width:154px; max-width:154px">
				<form:option value="">-----Please Select----</form:option>
				    <form:option value="1">1 year</form:option>
	                <form:option value="2">2 year</form:option>
                    <form:option value="3">3 year</form:option>
                    <form:option value="4">4 year</form:option>
                    <form:option value="5">5 year</form:option>
                    <form:option value="6">6 year</form:option>
                   
            </form:select><span style="color: black"></span>
            <br> <form:errors path="probationYear" cssClass="errorMessage" />
            
            </td>
            </tr>
            <tr><td><td></tr>
            <tr class="table-heading">
			<td colspan="4"><b><primo:label code="Eligibility for Before days  and After days" />
			</b></td>
			</tr>
			<tr id="beforeafter">
			<td class="form-left" ><primo:label code="Before Days" /><span
				class="errorMessage"></span></td>
			<td align="${left}" >
			<form:select cssClass="flat" path="beforeDays" style="min-width:154px; max-width:154px">
				<form:option value="">-----Please Select----</form:option>
				    <form:option value="1">1 day</form:option>
	                <form:option value="2">2 day</form:option>
                    <form:option value="3">3 day</form:option>
                    <form:option value="4">4 day</form:option>
                    <form:option value="5">5 day</form:option>
                    <form:option value="6">6 day</form:option>
                    <form:option value="7">7 day</form:option>
                   
            </form:select><span style="color: black"></span>
            <br> <form:errors path="beforeDays" cssClass="errorMessage" />
            
            </td>
            <td class="form-left" ><primo:label code="After days" /><span
				class="errorMessage"></span></td>
			<td align="${left}" >
			<form:select cssClass="flat" path="afterDays" style="min-width:154px; max-width:154px">
				<form:option value="">-----Please Select----</form:option>
				    <form:option value="1">1 day</form:option>
	                <form:option value="2">2 day</form:option>
                    <form:option value="3">3 day</form:option>
                    <form:option value="4">4 day</form:option>
                    <form:option value="5">5 day</form:option>
                    <form:option value="6">6 day</form:option>
                    <form:option value="7">7 day</form:option>
                   
            </form:select><span style="color: black"></span>
            <br> <form:errors path="afterDays" cssClass="errorMessage" />
            
            </td>
			
			</tr>
			<tr><td><td></tr>
             <tr class="table-heading">
			<td colspan="4"><b><primo:label code="Eligibility for Work for Week" />
			</b></td>
			</tr>
			<tr id="workweek">
			 
			  <td class="form-left" ><primo:label code="Work for Week" /><span
				class="errorMessage"></span></td>
			<td align="${left}" >
			<form:select cssClass="flat" path="workForWeek" style="min-width:154px; max-width:154px">
				<form:option value="">-----Please Select----</form:option>
				    <form:option value="1">1 week</form:option>
	                <form:option value="2">2 week</form:option>
                    <form:option value="3">3 week</form:option>
                    <form:option value="4">4 week</form:option>
            </form:select><span style="color: black"></span>
            <br> <form:errors path="workForWeek" cssClass="errorMessage" />
            
            </td>
			
			</tr>
			<tr><td><td></tr>
			 <tr class="table-heading">
			<td colspan="4"><b><primo:label code="Prior Notice" />
			</b></td>
			</tr>
			<tr id="priornotice">
			<td class="form-left" ><primo:label code="Prior Notice" /><span
				class="errorMessage"></span></td>
			<td align="${left}" ><form:input path="priorNoticeDays" style="min-width:150px; max-width:150px"
					cssClass="flat" /><span style="color: black">Days</span> 
			<br> <form:errors path="priorNoticeDays" cssClass="errorMessage" /></td>
			<td class="form-left" ><primo:label code="Week" /><span
				class="errorMessage"></span></td>
			<td align="${left}" >
			<form:select cssClass="flat" path="priorNoticeWeeks" style="min-width:154px; max-width:154px">
				<form:option value="">-----Please Select----</form:option>
				    <form:option value="1">1 week</form:option>
	                <form:option value="2">2 week</form:option>
                    <form:option value="3">3 week</form:option>
                    <form:option value="4">4 week</form:option>
                    <form:option value="5">5 week</form:option>
                    <form:option value="6">6 week</form:option>
            </form:select><span style="color: black"></span>
            <br> <form:errors path="priorNoticeWeeks" cssClass="errorMessage" />
            
            </td>
			</tr>
			<tr><td><td></tr>
			 <tr class="table-heading">
			<td colspan="4"><b><primo:label code="No Vaction Between Dates" />
			</b></td>
			</tr>
			  <tr id="novaction">
			<td class="form-left"><primo:label code="Date From" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input id="datepicker" path="noVactionDateFrom" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate();"/> <br> <form:errors
					path="noVactionDateFrom" cssClass="errorMessage" /></td>
			
			
			<td class="form-left"><primo:label code="Date To" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input id="datepicker1" path="noVactionDateTo" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate1();"/> <br> <form:errors
					path="noVactionDateTo" cssClass="errorMessage" /></td>
			
		</tr>
				<tr><td colspan="2"></td></tr>
				<tr><td colspan="2">
				<script type="text/javascript">disp();</script>
				
			</td>
				</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick=""
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" />
			</td>
		</tr>
		</table>
		</form:form>
		