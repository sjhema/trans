<%@ include file="/common/taglibs.jsp"%>
<script language="javascript">
function getComTerm(){
	var selectedemployee= document.getElementById('driver');
	var driver=selectedemployee.options[selectedemployee.selectedIndex].value;
	
	/* if(driver!=""){ */
	jQuery.ajax({
		url:'${ctx}/hr/weeklysalaryrate/ajax.do?action=findDCompany&driver='+driver, 
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
		url:'${ctx}/hr/weeklysalaryrate/ajax.do?action=findDterminal&driver='+driver, 
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
		url:'${ctx}/hr/weeklysalaryrate/ajax.do?action=findDCategory&driver='+driver, 
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
		url:'${ctx}/hr/weeklysalaryrate/ajax.do?action=findDStaff&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			
			var temp=listData[0];
			if(temp!="."){
				document.getElementById("staffId").value=listData[0];
			}
		}
			
		}); 
	
	
}
	
function calDailyPay(){
	weekSalary = document.getElementById("weeklypay").value;	
	if(weekSalary != "")		
		document.getElementById("dailypay").value = (weekSalary / 5).toFixed(2);
	else
		document.getElementById("dailypay").value = "";
}

function formatDate() {
	var date = document.getElementById("datepicker").value;
	if (date != "") {
		if (date.length < 8) {
			alert("Invalidte date format");
			document.getElementById("datepicker").value = "";
			return true;
		} else {
			var str = new String(date);
			if (!str.match("-")) {
				var mm = str.substring(0, 2);
				var dd = str.substring(2, 4);
				var yy = str.substring(4, 8);
				var enddigit = str.substring(6, 8);
				if (!enddigit == 00 && enddigit % 4 == 0) {
					if (mm == 04 || mm == 06 || mm == 09 || mm == 11) {
						if (dd > 30) {
							alert("invalid date format");
							document.getElementById("datepicker").value = "";
							return true;
						}
					}
					if (mm == 02 && dd > 29) {
						alert("invalid date format");
						document.getElementById("datepicker").value = "";
						return true;
					} else if (dd > 31) {
						alert("invalid date format");
						document.getElementById("datepicker").value = "";
						return true;
					}
				}
				if (enddigit == 00 && yy % 400 == 0) {
					if (mm == 04 || mm == 06 || mm == 09 || mm == 11) {
						if (dd > 30) {
							alert("invalid date format");
							document.getElementById("datepicker").value = "";
							return true;
						}
					}
					if (mm == 02 && dd > 29) {
						alert("invalid date format");
						document.getElementById("datepicker").value = "";
						return true;
					} else if (dd > 31) {
						alert("invalid date format");
						document.getElementById("datepicker").value = "";
						return true;
					}
				} else {
					if (mm == 04 || mm == 06 || mm == 09 || mm == 11) {
						if (dd > 30) {
							alert("invalid date format");
							document.getElementById("datepicker").value = "";
							return true;
						}
					}
					if (mm == 02 && dd > 28) {
						alert("invalid date format");
						document.getElementById("datepicker").value = "";
						return true;
					} else if (dd > 31) {
						alert("invalid date format");
						document.getElementById("datepicker").value = "";
						return true;
					}
				}
				var date = mm + "-" + dd + "-" + yy;
				document.getElementById("datepicker").value = date;
			}
		}
	}
}
function formatDate1() {
	var date = document.getElementById("datepicker1").value;
	if (date != "") {
		if (date.length < 8) {
			alert("Invalidte date format");
			document.getElementById("datepicker1").value = "";
			return true;
		} else {
			var str = new String(date);
			if (!str.match("-")) {
				var mm = str.substring(0, 2);
				var dd = str.substring(2, 4);
				var yy = str.substring(4, 8);
				var enddigit = str.substring(6, 8);
				if (!enddigit == 00 && enddigit % 4 == 0) {
					if (mm == 04 || mm == 06 || mm == 09 || mm == 11) {
						if (dd > 30) {
							alert("invalid date format");
							document.getElementById("datepicker1").value = "";
							return true;
						}
					}
					if (mm == 02 && dd > 29) {
						alert("invalid date format");
						document.getElementById("datepicker1").value = "";
						return true;
					} else if (dd > 31) {
						alert("invalid date format");
						document.getElementById("datepicker1").value = "";
						return true;
					}
				}
				if (enddigit == 00 && yy % 400 == 0) {
					if (mm == 04 || mm == 06 || mm == 09 || mm == 11) {
						if (dd > 30) {
							alert("invalid date format");
							document.getElementById("datepicker1").value = "";
							return true;
						}
					}
					if (mm == 02 && dd > 29) {
						alert("invalid date format");
						document.getElementById("datepicker1").value = "";
						return true;
					} else if (dd > 31) {
						alert("invalid date format");
						document.getElementById("datepicker1").value = "";
						return true;
					}
				} else {
					if (mm == 04 || mm == 06 || mm == 09 || mm == 11) {
						if (dd > 30) {
							alert("invalid date format");
							document.getElementById("datepicker1").value = "";
							return true;
						}
					}
					if (mm == 02 && dd > 28) {
						alert("invalid date format");
						document.getElementById("datepicker1").value = "";
						return true;
					} else if (dd > 31) {
						alert("invalid date format");
						document.getElementById("datepicker1").value = "";
						return true;
					}
				}
				var date = mm + "-" + dd + "-" + yy;
				document.getElementById("datepicker1").value = date;
			}
		}
	}
}


</script>
<h3>
	<primo:label code="Add/Update Weekly Salary Rate" />
</h3>
<form:form action="save.do?fromAlertPage=${fromAlertPage}" name="employeeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Add/Update Weekly Salary Rate"/></b></td>
		</tr>
		<tr>
	
			<td class="form-left"><primo:label code="Employee" /><span	class="errorMessage">*</span></td>
		<td align="${left}">
				<form:select cssClass="flat" path="driver" id="driver" style="min-width:154px; max-width:154px" onchange="javascript:getComTerm()" >
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="fullName"></form:options>
				</form:select> <br><form:errors path="driver" cssClass="errorMessage" />
			</td>
			
			
			
			<td class="form-left"><primo:label code="Staff Id" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="staffId" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="staffId" /> 
			<br> <form:errors path="staffId" cssClass="errorMessage" /></td>
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
		
		<td class="form-left"><primo:label code="Terminal" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="terminal" style="min-width:154px; max-width:154px" id="terminal" >
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
		    <td class="form-left"><primo:label code="Category" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="catagory" style="min-width:154px; max-width:154px" id="categoryid">
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${catagories}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="catagory" cssClass="errorMessage" />
			</td>
			
			
			
		
		<tr>
		<td class="form-left"><primo:label code="Weekly Pay" /><span
				class="errorMessage"></span></td>
		<td align="${left}"><form:input  id="weeklypay"    path="weeklySalary" cssClass="flat" style="min-width:150px; max-width:150px" onblur="calDailyPay()"
					  /> <br> <form:errors
					path="weeklySalary" cssClass="errorMessage" /></td>
					
		<td class="form-left"><primo:label code="Daily Pay" /><span
				class="errorMessage"></span></td>
		<td align="${left}"><form:input  id="dailypay" path="dailySalary" cssClass="flat"   style="min-width:150px; max-width:150px"/> 
		</td>	
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Valid From" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="validFrom" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="datepicker" onblur="return formatDate();"/> 
			<br> <form:errors path="validFrom" cssClass="errorMessage" /></td>
	
		
			<td class="form-left"><primo:label code="Valid To" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="validTo" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="datepicker1" onblur="return formatDate1();"/> 
			<br> <form:errors path="validTo" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
		  	<td class="form-left"><primo:label code="Notes" /><span class="errorMessage"></span></td>
			<td align="${left}" colspan="5">
				<form:textarea path="notes" rows="2" cols="69"/>    	
			</td>
		</tr>
		
		
	
		<tr>
		
		</tr>
			
			
		
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}">
				<input type="submit" name="create" id="create" onclick="" value="<primo:label code="Save"/>" class="flat" /> 
				<input type="reset" id="resetBtn" value="<primo:label code="Reset"/>" class="flat" />
				<c:set var="cancelAction" value="location.href='list.do'"/>
				<c:if test="${fromAlertPage ne null and fromAlertPage eq 'true'}">
					<c:set var="cancelAction" value="JavaScript:window.location='${ctx}/hr/payrollratealert/list.do?type=weeklySalaryRate';"/>
				</c:if>
				<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat"
					onClick="${cancelAction}" />
			</td>
		</tr>
	</table>
</form:form>
