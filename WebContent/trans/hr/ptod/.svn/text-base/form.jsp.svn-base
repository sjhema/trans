<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function diselall(){
	document.getElementById('eligibilty_p').style.display="none";
	document.getElementById('eligibilty_Bf').style.display="none";
	document.getElementById('eligibilty_ww').style.display="none";
	document.getElementById('eligibilty').style.display='none';
	document.getElementById('eligibilty_nt').style.display='none';
	document.getElementById('eligibilty_pn').style.display='none';
}

function checkelgi(){
	var selectedcompany= document.getElementById('company');
	var company=selectedcompany.options[selectedcompany.selectedIndex].value;
	var selectedterminal= document.getElementById('terminal');
	var terminal=selectedterminal.options[selectedterminal.selectedIndex].value;
	var selectedcategory= document.getElementById('category');
	var category=selectedcategory.options[selectedcategory.selectedIndex].value;
	var selectedleavetype= document.getElementById('leavetype');
	var leavetype=selectedleavetype.options[selectedleavetype.selectedIndex].value;
	if(company!=""&&terminal!=""&&category!=""&&leavetype!=""){
		
		jQuery.ajax({
			url:'${ctx}/hr/ptod/ajax.do?action=checkelgi&company='+company+'&terminal='+terminal+'&category='+
			category+'&leavetype='+leavetype, 
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var day=listData[0];
				var year=listData[1];
				var beday=listData[2];
				var afday=listData[3];
				var ww=listData[5];
				var pnday=listData[6];
				var week=listData[7];
				if(day=="null"){
				document.getElementById('eligibilty_nt').style.display="";
				document.getElementById('eligibilty_p').style.display="none";
				document.getElementById('eligibilty_Bf').style.display="none";
				document.getElementById('eligibilty_p').style.display="none";
				document.getElementById('eligibilty').style.display='none';
				document.getElementById('eligibilty_ww').style.display='none';
				document.getElementById('eligibilty_pn').style.display='none';
				
				}
				else{
					document.getElementById('eligibilty_nt').style.display='none';
				document.getElementById('eligibilty').style.display="";
				document.getElementById("pday").value=day;
				document.getElementById("pyear").value=year;
				document.getElementById('eligibilty_p').style.display="";
				document.getElementById("bday").value=beday;
				document.getElementById("aday").value=afday;
				document.getElementById('eligibilty_Bf').style.display="";
				document.getElementById("ww").value=ww;
				document.getElementById('eligibilty_ww').style.display="";
				document.getElementById("pnday").value=pnday;
				document.getElementById("week").value=week;
				document.getElementById('eligibilty_pn').style.display="";
				if(day=="."&&week=="."){
					document.getElementById('eligibilty_p').style.display="none";
				}
				if(beday=="."&&afday=="."){
					document.getElementById('eligibilty_Bf').style.display="none";
				}
				if(ww=="."){
					document.getElementById('eligibilty_ww').style.display="none";
				}
				if(pnday=="."&&week=="."){
					document.getElementById('eligibilty_pn').style.display="none";
				}
				
				}
			}
		});
	}
}

function getExperience(){
	//alert("yes"); 
	var emp=document.getElementById("empId").value;
	
	if(emp==""){
	document.getElementById("experianceId").value=""; 
	document.getElementById("experianceIdyear").value="";
	document.getElementById("dayearnedId").value="";
	}
	var selectedEmp= document.getElementById('empId');
	//alert(selectedEmp);
	var empId=selectedEmp.options[selectedEmp.selectedIndex].value;
	//alert(empId);
	if(emp!=""){
	jQuery.ajax({
		url:'${ctx}/hr/ptod/ajax.do?action=findExp&driver='+empId, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var days=listData[0];
			var years=listData[1];
			var dayearned=listData[2];
			/* document.getElementById("transferNetId").value=transferNet;
			document.getElementById("transferTons").value=transferTon; */
			document.getElementById("experianceId").value=days;
			document.getElementById("experianceIdyear").value=years;
			document.getElementById("dayearnedId").value=dayearned;
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
	<primo:label code="Add/Update PTOD" />
</h3>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Manage PTOD" />
			</b></td>
		</tr>
		<%-- <tr>
			<td class="form-left"><primo:label code="Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="name" cssClass="flat" cssStyle="width:200px;" maxlength="20" />
			 	<br><form:errors path="name" cssClass="errorMessage" />
			</td>
		</tr> --%>
		
		
		<tr>
			<td class="form-left"><primo:label code="Company" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="company" style="min-width:154px; max-width:154px" onchange="javascript:checkelgi()" id="company">
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${companyLocation}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="company" cssClass="errorMessage" />
			</td>
			
			
			  <td class="form-left"><primo:label code="Category" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="category" style="min-width:154px; max-width:154px" onchange="javascript:checkelgi()" id="category">
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${catagories}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="category" cssClass="errorMessage" />
			</td>		
			
		</tr>
		
		<tr>
		  <td class="form-left"><primo:label code="Terminal" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="terminal" style="min-width:154px; max-width:154px" onchange="javascript:checkelgi()" id="terminal">
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="terminal" cssClass="errorMessage" />
			</td>
			
		   <td class="form-left"><primo:label code="Leave Type" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="leavetype" style="min-width:154px; max-width:154px" onchange="javascript:checkelgi()" id="leavetype">
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${leavetypes}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="leavetype" cssClass="errorMessage" />
			</td>
			
		</tr>
		
		
		
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Eligibility" />
			</b></td>
		</tr>
		<tr>
		    
		     <%-- <td class="form-left">Leave Type<span class="errorMessage">*</span></td>
		       <td align="${left}"><form:select cssClass="flat" path="leavetype">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${LeaveTypes}" itemValue="dataText"
						itemLabel="dataText" />
				</form:select> <br><form:errors path="leavetype" cssClass="errorMessage" /></td> --%>
		</tr>
		
		<tr>
			<%-- <td class="form-left"><primo:label code="Experience In days" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="experienceindays" cssClass="flat" cssStyle="width:150px;" maxlength="20" /><!-- <b>Days</b> -->
			 	<br><form:errors path="experienceindays" cssClass="errorMessage" />--%>
			 
			<%--  <td class="form-left"><primo:label code="Experience In days" /><span
				class="errorMessage">*</span></td> --%>
				<td class="form-left"><primo:label code="Experience In days" /><!-- <span class="errorMessage">*</span> --></td>
			<td align="${left}">
				<form:input path="experienceindays" cssClass="flat" cssStyle="width:150px;" maxlength="20" /><!-- <b>Days</b> -->
			 	<br><form:errors path="experienceindays" cssClass="errorMessage" />
			 <%-- <td>
			<c:if test="${empty modelObject.id}">
				<input type="text" name="experienceindays" path="" id="experianceId" disabled="disabled" value="${sessionScope.experienceindays}">
				</c:if>
				<c:if test="${not empty modelObject.id}">
				<input type="text" disabled="disabled" value="${modelObject.experienceindays}">
				</c:if>
			</td> --%>
			
			<%-- <c:choose>
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}">
			<td align="${left}"><form:input path="experienceindays" id="experianceId"
					Class="flat" readonly="true"/> <br> <form:errors path="experienceindays" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="experienceindays" id="experianceId"
					Class="flat"  readonly="true"  value=""/> <br> <form:errors path="experienceindays" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose> --%>
			</td>
			<td class="form-left"><primo:label code="Leave Qualifier" /><span
				class="errorMessage">*</span></td>
				<td><form:select cssClass="flat" path="leaveQualifier">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${leavequalifier}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="leaveQualifier" cssClass="errorMessage" />
			</td>
			
			</tr>
			<tr>
			<td class="form-left"><primo:label code="Experience In Years" /><!-- <span
				class="errorMessage">*</span> --></td>
			<td align="${left}">
			<form:select cssClass="flat" path="experienceinyears" style="min-width:154px; max-width:154px">
				<form:option value="">-----Please Select----</form:option>
				  <form:options items="${experienceyears}" itemValue="dataValue" itemLabel="dataLabel"/> 
                   
            </form:select><!-- <span style="color: black">Years</span> -->
            <br> <form:errors path="experienceinyears" cssClass="errorMessage" />
			
			</td>
			<td class="form-left"><primo:label code="Up To Experience In Years" /><!-- <span
				class="errorMessage">*</span> --></td>
			<td align="${left}">
			<form:select cssClass="flat" path="experienceinyearsTo" style="min-width:154px; max-width:154px">
				<form:option value="">-----Please Select----</form:option>
				   <form:options items="${experienceyears}" itemValue="dataValue" itemLabel="dataLabel"/> 
                   
            </form:select>
            <br> <form:errors path="experienceinyearsTo" cssClass="errorMessage" />
			
			</td>
			 
		</tr>
		
		<tr>
		    <%-- <td class="form-left"><primo:label code="Experience In Years" /><span
				class="errorMessage">*</span></td> --%>
		    <%-- <c:choose>
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}">
			<td align="${left}"><form:input path="experienceinyears" id="experianceIdyear"
					Class="flat" readonly="true"/> <br> <form:errors path="experienceinyears" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="experienceinyears" id="experianceIdyear"
					Class="flat"  readonly="true"  value=""/> <br> <form:errors path="experienceinyears" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose> --%>
		    <%-- <td class="form-left"><primo:label code="Experience In Years" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="experienceinyears" cssClass="flat" cssStyle="width:150px;" maxlength="20" /><!-- <b>Days</b> -->
			 	<br><form:errors path="experienceinyears" cssClass="errorMessage" />
			</td>  --%>
			<%-- <td class="form-left"><primo:label code="Experience In Years" /><span
				class="errorMessage">*</span></td>
			<td align="${left}">
			<form:select cssClass="flat" path="experienceinyears">
				<form:option value="">-----Please Select----</form:option>
				    <form:option value="0">0 year</form:option>
				    <form:option value="1">1 year</form:option>
	                <form:option value="2">2 year</form:option>
                    <form:option value="3">3 year</form:option>
                    <form:option value="4">4 year</form:option>
                    <form:option value="5">5 year</form:option>
                    <form:option value="6">6 year</form:option>
                    <form:option value="7">7 year</form:option>
                    <form:option value="8">8 year</form:option>
                    <form:option value="9">9 year</form:option>
                    <form:option value="10">10 year</form:option>
                   
            </form:select><!-- <span style="color: black">Years</span> -->
            <br> <form:errors path="experienceinyears" cssClass="errorMessage" /> --%>
			
			
		</tr>
		<tr>
		
		    <%-- <td class="form-left"><primo:label code="Days Earned" /><span
				class="errorMessage">*</span></td> --%>
		   <%--  <c:choose>
			<c:when test="${modelObject.id!=null && modelObject.employee!=null}">
			<td align="${left}"><form:input path="dayearned" id="dayearnedId"
					Class="flat" readonly="true"/> <br> <form:errors path="dayearned" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="dayearned" id="dayearnedId"
					Class="flat"  readonly="true"  value=""/> <br> <form:errors path="dayearned" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose> --%>
			<td class="form-left"><primo:label code="Days Earned" /><!-- <span class="errorMessage">*</span> --></td>
			<td align="${left}">
				<form:input path="dayearned" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" /><!-- <b>Days</b> -->
			 	<br><form:errors path="dayearned" cssClass="errorMessage" />
			</td>
	  
			<td class="form-left"><primo:label code="Hours Earned" /><!-- <span class="errorMessage">*</span> --></td>
			<td align="${left}">
				<form:input path="hoursearned" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" /><!-- <b>Hrs/</b> -->
			 	<br><form:errors path="hoursearned" cssClass="errorMessage" />
			</td>
	  </tr>
	  <tr class="table-heading">
			<td colspan="4"><b><primo:label code="PTOD RATE" />
			</b></td>
		</tr>
	   <tr>
			<td class="form-left"><primo:label code="PTOD Rates" /><!-- <span class="errorMessage">*</span> --></td>
			<td align="${left}">
				<form:input path="rate" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" /><!-- <b>/</b> -->
			 	<br><form:errors path="rate" cssClass="errorMessage" />
			</td>
	  
	        <td class="form-left"><primo:label code="Calculate" />
	        <td><form:select  cssClass="flat" path="calculateFlag" >
					<form:options items="${calculate}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="calculateFlag" cssClass="errorMessage" />
			</td>
			<%-- <td class="form-left"><primo:label code="Hourly Rates" /><!-- <span class="errorMessage">*</span> --></td>
			<td align="${left}">
				<form:input path="hourlyrate" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="hourlyrate" cssClass="errorMessage" />
			</td> --%>
	  </tr>
	  
	  <tr>
			<td class="form-left"><primo:label code="Effective Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker" path="effectiveDate" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate();"/> <br> <form:errors
					path="effectiveDate" cssClass="errorMessage" /></td>
			
			
			<td class="form-left"><primo:label code="End Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker1" path="endDate" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate1();"/> <br> <form:errors
					path="endDate" cssClass="errorMessage" /></td>
			
		</tr>
		
		<tr>
		     <td class="form-left"><primo:label code="Status" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="status" style="min-width:154px; max-width:154px">
					<form:options items="${ptodstatus}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="status" cssClass="errorMessage" />
			</td>
		<!-- </tr>
		
		<tr> -->
		 <td class="form-left"><primo:label code="Annual Accrual" /><span
				class="errorMessage">*</span></td>
				<td><form:select cssClass="flat" path="annualoraccrual">
					<form:options items="${annualaccrual}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="annualoraccrual" cssClass="errorMessage" />
			</td>
		</tr>
		
		
		
		
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Notes" />
			</b></td>
		</tr>
		<tr>
		    <td class="form-left"><primo:label code="Notes" /><span
				class="errorMessage"></span></td>
			<td align="${left}" colspan="5">
				<form:textarea path="notes" rows="2" cols="75"/>    	
			</td>
		</tr>
		<tr class="table-heading" id="eligibilty">
			<td colspan="4"><b><primo:label code="Eligibility Criteria" />
			</b></td>
		</tr >
		<tr id="eligibilty_p">
		<td class="form-left"><primo:label code="Probation Days" /></td>
		<td align="${left}"><input type="text"  disabled="disabled" name="pday" id="pday"/><span style="color: black">Days</span>
		</td>
		<td class="form-left"><primo:label code="Probation Years" /></td>
		<td align="${left}"><input type="text"  disabled="disabled" name="pyear" id="pyear"/><span style="color: black">Years</span>
		</td>
		</tr>
		<tr id="eligibilty_Bf">
		<td class="form-left"><primo:label code="Before Days" /></td>
		<td align="${left}"><input type="text"  disabled="disabled" name="bday" id="bday"/><span style="color: black">Days</span>
		</td>
		<td class="form-left"><primo:label code="After days" /></td>
		<td align="${left}"><input type="text"  disabled="disabled" name="aday" id="aday"/><span style="color: black">Days</span>
		</td>
		</tr>
		<tr id="eligibilty_ww">
		<td class="form-left"><primo:label code="Work for Week" /></td>
		<td align="${left}"><input type="text"  disabled="disabled" name="ww" id="ww"/><span style="color: black">Weeks</span>
		</td>
		</tr>
			<tr id="eligibilty_pn">
		<td class="form-left"><primo:label code="Prior Notice" /></td>
		<td align="${left}"><input type="text"  disabled="disabled" name="pnday" id="pnday"/><span style="color: black">Days</span>
		</td>
		<td class="form-left"><primo:label code="Week" /></td>
		<td align="${left}"><input type="text"  disabled="disabled" name="week" id="week"/><span style="color: black">Weeks</span>
		</td>
		</tr>
		<tr id="eligibilty_nt">
		<td><span style="color: red">Eligibility Criteria is Not Defined </span></td>
		</tr>
		<tr><td colspan="2"></td><td><script type="text/javascript">checkelgi();</script></td>
		<td><script type="text/javascript">diselall();</script></td></tr>
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
getExperience();
</script>
	
