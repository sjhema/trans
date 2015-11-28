<%@include file="/common/taglibs.jsp"%>
<script language="javascript">

function getComTerm(){
	var selectedemployee= document.getElementById('driver');
	var driver=selectedemployee.options[selectedemployee.selectedIndex].value;
	/* if(driver!=""){ */
	jQuery.ajax({
		url:'${ctx}/hr/leavebalance/ajax.do?action=findDCompany&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			if(driver==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var company=listData[i];
				options += '<option value="'+company.id+'">'+company.name+'</option>';
			  }
			$("#company").html(options);
		}
	});	
	jQuery.ajax({
		url:'${ctx}/hr/leavebalance/ajax.do?action=findDterminal&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options ;
			if(driver==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var terminal=listData[i];
				options += '<option value="'+terminal.id+'">'+terminal.name+'</option>';
			  }
			$("#terminal").html(options);
		}
	});
	jQuery.ajax({
		url:'${ctx}/hr/ptodapplication/ajax.do?action=findDCategory&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			if(driver==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var category=listData[i];
				options += '<option value="'+category.id+'">'+category.name+'</option>';
				}
			$("#categoryid").html(options);
		}
			
		}); 
	
	jQuery.ajax({
		url:'${ctx}/hr/leavebalance/ajax.do?action=findNextAnv&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			document.getElementById('datepicker2').value=listData;
		}
	});	
	
	/* } */
}

function FindLeave(obj){
	var id = obj.id;
	
	    var accrueddays=document.getElementById('daysaccrudeid').value;
	    var earneddays=document.getElementById('dayssbalanceid').value;
		var useddays=document.getElementById('daysusedid').value;
		var remaindays=document.getElementById('daysremainid').value;
		
		if(earneddays!="" && accrueddays!=""){
			var availabledays=parseInt(accrueddays)+parseInt(earneddays);
			document.getElementById('daysavailableid').value=availabledays;
			
			if(useddays!=""){
				var Rdays=parseInt(availabledays, 10) - parseInt(useddays, 10);
				document.getElementById('daysremainid').value=Rdays;
			}else{
				document.getElementById('daysremainid').value=availabledays;
				document.getElementById('daysusedid').value=0;
			}
		}
	 /* if(id=="dayssbalanceid"){
		    var earneddays=document.getElementById('dayssbalanceid').value;
			var useddays=document.getElementById('daysusedid').value;
			var remaindays=document.getElementById('daysremainid').value;
			if(useddays !=""){
				var Rdays=parseInt(earneddays, 10) - parseInt(useddays, 10);
				document.getElementById('daysremainid').value=Rdays;
			}
			else{
				if(remaindays!="" ){
					var Udays=parseInt(earneddays, 10) - parseInt(remaindays, 10);
					document.getElementById('daysusedid').value=Udays;
				}
			}
	 }
	 
	 if(id=="daysusedid"){
		    var earneddays=document.getElementById('dayssbalanceid').value;
			var useddays=document.getElementById('daysusedid').value;
			var remaindays=document.getElementById('daysremainid').value;
			
			if(earneddays !=""){
				var Rdays=parseInt(earneddays, 10) - parseInt(useddays, 10);
				document.getElementById('daysremainid').value=Rdays;
			}
			else{
				if(remaindays !=""){
					var Edays=parseInt(remaindays, 10) + parseInt(useddays, 10);
					document.getElementById('dayssbalanceid').value=Rdays;
				}
			}
	 }
	 
	 
	 if(id=="daysremainid"){
		    var earneddays=document.getElementById('dayssbalanceid').value;
			var useddays=document.getElementById('daysusedid').value;
			var remaindays=document.getElementById('daysremainid').value;
			if(earneddays !=""){
				//alert("ok");
				var Udays=parseInt(earneddays, 10) - parseInt(remaindays, 10);
				document.getElementById('daysusedid').value=Udays;
			}
			else{
				  if(useddays!=""){
					  var Edays=parseInt(remaindays, 10) + parseInt(useddays, 10);
						document.getElementById('dayssbalanceid').value=Edays; 
				  }
			}
	 } */
}

function findhours(){
	var houraccrued =document.getElementById("hoursaccrudeid").value;
	var hourearned =document.getElementById("hourearned").value;
	var hourused =document.getElementById("hourused").value;
	var hourem=document.getElementById("hourem").value;
	if(hourearned!="" && houraccrued!="" ){
		var houravailable=parseFloat(houraccrued)+parseFloat(hourearned);
		document.getElementById("hoursavailableid").value=houravailable;
		if(hourused!=""){
			var Rhours=parseFloat(houravailable) - parseFloat(hourused);
			document.getElementById('hourem').value=Rhours;
		}else{
			document.getElementById('hourem').value=houravailable;
			document.getElementById('hourused').value=0.0;
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

function formatDate2(){
	var date=document.getElementById("datepicker2").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker2").value="";
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
						document.getElementById("datepicker2").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker2").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker2").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker2").value=date;
		}
	 }
   }
}
</script>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<br/>
	<br/>
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="4"><b><primo:label code="Add/Update Employee Leave Balance"/></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Employee" /><span	class="errorMessage">*</span></td>
		<%-- 	<td><form:select cssClass="flat" path="empname" id="employee"  onchange="javascript:getComTerm()">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<c:forEach var="item" items="${employees}">
						<c:set var="selected" value=""/>
						<c:if test="${item.id eq modelObject.empname.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${item.id}" ${selected}>${item.fullName}--${item.catagory.name}</option>
					</c:forEach>
				</form:select> <br> <form:errors path="empname" cssClass="errorMessage" />
			</td> --%>
			<td align="${left}">
				<form:select cssClass="flat" path="empname" id="driver" style="min-width:154px; max-width:154px" onchange="javascript:getComTerm()" >
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="fullName"></form:options>
				</form:select> <br><form:errors path="empname" cssClass="errorMessage" />
			</td>
			
		</tr>
			<tr>
		<td class="form-left"><primo:label code="Company" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="company" style="min-width:154px; max-width:154px" id="company" >
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td>
		
	  <td class="form-left"><primo:label code="Category" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="empcategory" style="min-width:154px; max-width:154px" id="categoryid">
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${catagories}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="empcategory" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
		
			<td class="form-left"><primo:label code="Terminal" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="terminal" style="min-width:154px; max-width:154px" id="terminal" >
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>		
		  
			
		   <td class="form-left"><primo:label code="Leave Type" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="leavetype" style="min-width:154px; max-width:154px">
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${leavetypes}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="leavetype" cssClass="errorMessage" />
			</td>
			
		</tr>
		
		<tr>
		<td class="form-left"><primo:label code="Days Accrued" /><span
				class="errorMessage">*</span></td>
		<td align="${left}">
				<form:input path="daysaccrude" id="daysaccrudeid" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" onblur="javascript:FindLeave(this)"/>
				<br><form:errors path="daysaccrude" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><primo:label code="Hours Accrued" /><span
				class="errorMessage">*</span></td>
		   <td align="${left}">
				<form:input path="hoursaccrude" id="hoursaccrudeid" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" onblur="javascript:findhours()"/>
				<br><form:errors path="hoursaccrude" cssClass="errorMessage" />
			</td>			
		</tr>
		
		
		
		<tr>
		    <td class="form-left"><primo:label code="Days Earned" /></td>
			<td align="${left}">
				<form:input path="dayssbalance" id="dayssbalanceid" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" onblur="javascript:FindLeave(this)"/>
			</td>
	         
	         
	         <td class="form-left"><primo:label code="Hours Earned" /><!-- <span class="errorMessage">*</span> --></td>
			<td align="${left}">
				<form:input path="hoursbalance" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" id="hourearned" onblur="javascript:findhours()" /><!-- <b>Days</b> -->
			 	<br><form:errors path="hoursbalance" cssClass="errorMessage" />
			</td>
	         
	  </tr>
	  
	  
	  <tr>
	  <td class="form-left"><primo:label code="Days Available" />
		<td align="${left}">
				<form:input path="daysavailable" id="daysavailableid" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" onblur="javascript:FindLeave(this)"/>
			</td>
			
			<td class="form-left"><primo:label code="Hours Available" />
		   <td align="${left}">
				<form:input path="hoursavailable" id="hoursavailableid" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" onblur="javascript:findhours()"/>
			</td>
	  
	  </tr>
	  
	  
	  
	  <tr> 	         
	         <td class="form-left"><primo:label code="Days used" /></td>
			<td align="${left}">
				<form:input path="daysused"  id="daysusedid" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" onblur="javascript:FindLeave(this)" />
			 	
			</td>
			
			<td class="form-left"><primo:label code="Hours Used" /><!-- <span class="errorMessage">*</span> --></td>
			<td align="${left}">
				<form:input path="hoursused" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" id="hourused" onblur="javascript:findhours()"  /><!-- <b>Hrs/</b> -->
			 	<br><form:errors path="hoursused" cssClass="errorMessage" />
			</td>			
		</tr>
		
		
		<tr>
		      <td class="form-left"><primo:label code="Days Remaining" /></td>
			<td align="${left}">
				<form:input path="daysremain" id="daysremainid" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" onblur="javascript:FindLeave(this)" />
			 	
			</td>		
			<td class="form-left"><primo:label code="Hours Remaining" /><!-- <span class="errorMessage">*</span> --></td>
			<td align="${left}">
				<form:input path="hourremain" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" id="hourem" onblur="javascript:findhours()" /><!-- <b>Hrs/</b> -->
			 	<br><form:errors path="hourremain" cssClass="errorMessage" />
			</td>
			
		</tr>
		
		<tr>
		
			<td class="form-left"><primo:label code="Effective Date From" /><span class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="dateEffectiveFrom" id="datepicker" onblur="return formatDate();" style="min-width:150px; max-width:150px"
					Class="flat"  /> <br> <form:errors path="dateEffectiveFrom" cssClass="errorMessage" /></td>
					
			<td class="form-left"><primo:label code="Effective Date To" /><span class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="dateEffectiveTo" id="datepicker1"  onblur="return formatDate1();" style="min-width:150px; max-width:150px"
					Class="flat"  /> <br> <form:errors path="dateEffectiveTo" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
		<td class="form-left"><primo:label code="Status" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="status">
					<form:options items="${leavebalstatus}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="status" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Next Anniverssary Date" /><span class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="nextAllotmentDate" id="datepicker2"  onblur="return formatDate2();" style="min-width:150px; max-width:150px"
					Class="flat"  /> <br> <form:errors path="nextAllotmentDate" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Note" /></td>
			<td align="${left}" colspan="3">
				<%-- <form:input   path="note" cssClass="flat" cssStyle="width:300px;" maxlength="100"  size="30"/><!-- <b>Hrs/</b> --> --%>
			 	<form:textarea path="note" rows="2" cols="69"/>  
			 	<br><form:errors path="note" cssClass="errorMessage" />
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
		getComTerm();
</script>
		