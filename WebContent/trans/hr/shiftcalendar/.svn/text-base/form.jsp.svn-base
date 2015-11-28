<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">


function checkHours(field){
	var hours=document.getElementById(field).value;
	if(hours!=""){
		if(hours.length<2){
			alert("Invalidte hours,Format should be 0"+hours);
			document.getElementById(field).value="";
		}
		if(hours>=24){
			alert("Invalidte hours,Should be less than 24");
			document.getElementById(field).value="";
		}
	}
}

function checkMinutes(field){
	var minutes=document.getElementById(field).value;
	if(minutes!=""){
		if(minutes.length<2){
			alert("Invalidte minutes,format should be 0"+minutes);
			document.getElementById(field).value="";
		
		}
		if(minutes>=60){
			alert("Invalidte minutes,Should be less than 60");
			document.getElementById(field).value="";
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


<h3>
	<primo:label code="Add/Shift Calendar" />
</h3>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Manage Shift Calendar" />
			</b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="name" cssClass="flat" />
			 	<br><form:errors path="name" cssClass="errorMessage" />
			</td>
		
		
		
		
			<td class="form-left"><primo:label code="Company" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="company">
					<form:option value="">------Please Select------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="company" cssClass="errorMessage" />
			</td>
		</tr>
		
		
		
			
	  
	  <tr>
			<td class="form-left"><primo:label code="Effective Start Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker" path="effectivestartdate"
					cssClass="flat" onblur="return formatDate();"/> <br> <form:errors
					path="effectivestartdate" cssClass="errorMessage" /></td>
			
			
			<td class="form-left"><primo:label code="Effective End Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker1" path="effectiveenddate"
					cssClass="flat" onblur="return formatDate1();"/> <br> <form:errors
					path="effectiveenddate" cssClass="errorMessage" /></td>
			
		</tr>
		
		<tr>
		     <td class="form-left"><primo:label code="Hours Start" /><span class="errorMessage">*</span></td>
		     <td align="${left}"><form:input id="starttimehoursid" path="starttimehours" cssClass="flat" onblur="return checkHours('starttimehoursid')"/>
		     <br> <form:errors path="starttimehours" cssClass="errorMessage" /></td>
		     
		      <td class="form-left"><primo:label code="Minutes Start" /><span class="errorMessage">*</span></td>
		     <td align="${left}"><form:input id="starttimeminutesid" path="starttimeminutes" cssClass="flat" onblur="return checkMinutes('starttimeminutesid')"/> 
		     <br> <form:errors path="starttimeminutes" cssClass="errorMessage" /></td>
		     
		</tr>
		
		<tr>
		     <td class="form-left"><primo:label code="Hours End" /><span class="errorMessage">*</span></td>
		     <td align="${left}"><form:input id="endtimehoursid" path="endtimehours" cssClass="flat" onblur="return checkHours('endtimehoursid')"/>
		     <br> <form:errors path="endtimehours" cssClass="errorMessage" /></td>
		     
		      <td class="form-left"><primo:label code="Minutes End" /><span class="errorMessage">*</span></td>
		     <td align="${left}"><form:input id="endtimeminutesid" path="endtimeminutes" cssClass="flat" onblur="return checkMinutes('endtimeminutesid')"/>
		     <br> <form:errors path="endtimeminutes" cssClass="errorMessage" /></td>
		     
		</tr>
		
		<tr>
		     <td class="form-left"><primo:label code="Daily Regular Hours" /><span class="errorMessage">*</span></td>
		     <td align="${left}"><form:input path="dailyregularhours" cssClass="flat"/> 
		     <br> <form:errors path="dailyregularhours" cssClass="errorMessage" /></td>
		     
		      <td class="form-left"><primo:label code="Weekly Regular Hours" /><span class="errorMessage">*</span></td>
		     <td align="${left}"><form:input path="weeklyregularhours" cssClass="flat"/> 
		     <br> <form:errors path="weeklyregularhours" cssClass="errorMessage" /></td>
		     
		</tr>
		
		
		
		<tr>
		     <td class="form-left"><primo:label code="Status" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="status">
					<form:options items="${status}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="status" cssClass="errorMessage" />
			</td>
		
		
		</tr>
		
		
		
		
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Description" />
			</b></td>
		</tr>
		<tr>
		    <td class="form-left"><primo:label code="Shift Description" /><span
				class="errorMessage"></span></td>
			<td align="${left}" colspan="5">
				<form:textarea path="shiftdescription" rows="2" cols="75"/>    	
			</td>
		</tr>
		
		
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick=""
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" /></td>
		</tr>
	</table>
</form:form>
<script language="javascript">
checkHours('starttimehoursid');
checkMinutes('starttimeminutesid');
checkHours('endtimehoursid');
checkMinutes('endtimeminutesid')
</script>
	
