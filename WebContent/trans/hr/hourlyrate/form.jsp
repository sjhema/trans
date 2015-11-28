<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function getComTermCatagory() {
		
		
		var emp = document.getElementById("employeeId");
		var driver = emp.options[emp.selectedIndex].value;

		jQuery.ajax({
			url : '${ctx}/hr/hourlyrate/ajax.do?action=findDCompany&driver='
					+ driver,
			success : function(data) {
				var listData = jQuery.parseJSON(data);
				var options;

				if (driver == "") {
					options = '<option value="">Please Select</option>';
				}
				for ( var i = 0; i < listData.length; i++) {
					var company = listData[i];
					options += '<option value="'+company.id+'">' + company.name+ '</option>';
				}
				$("#company").html(options);
			}

		});

		jQuery.ajax({
			url : '${ctx}/hr/hourlyrate/ajax.do?action=findDTerminal&driver='
					+ driver,
			success : function(data) {
				var listData = jQuery.parseJSON(data);
				var options;

				if (driver == "") {
					options = '<option value="">Please Select</option>';
				}
				for ( var i = 0; i < listData.length; i++) {
					var terminal = listData[i];
					options += '<option value="'+terminal.id+'">'
							+ terminal.name + '</option>';
				}
				$("#terminalId").html(options);
			}

		});

		jQuery.ajax({
			url : '${ctx}/hr/hourlyrate/ajax.do?action=findDCategory&driver='
					+ driver,
			success : function(data) {
				var listData = jQuery.parseJSON(data);
				var options;

				if (driver == "") {
					options = '<option value="">Please Select</option>';
				}
				for ( var i = 0; i < listData.length; i++) {
					var category = listData[i];
					options += '<option value="'+category.id+'">'
							+ category.name + '</option>';
				}
				$("#categoryId").html(options);
			}

		});
		jQuery.ajax({
			url:'${ctx}/hr/hourlyrate/ajax.do?action=findDStaff&driver='+driver, 
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				
				var temp=listData[0];
				if(temp!="."){
					document.getElementById("staffId").value=listData[0];
				}
			}
				
			}); 
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
	<primo:label code="Add/Update Hourly Rate Information" />
</h3>




<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post">
   <form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">

<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Add/Update Hourly Rate"/></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Employee" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:select cssClass="flat" id="employeeId" style="min-width:154px; max-width:154px"
					path="driver" onchange="javascript:getComTermCatagory()">
					<form:option value="">------Please Select------</form:option>
					<form:options items="${employees}" itemValue="id"
						itemLabel="FullName" />
				</form:select> <br>
			<form:errors path="driver" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Employee Id" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input id="staffId" path="staffId" style="min-width:150px; max-width:150px"
					cssClass="flat" /> <br> <form:errors path="staffId"
					cssClass="errorMessage" />
			</td>
			
			
			<%-- <td align="${left}" ><select name="category" id="categoryId"  
				style="width: 160px">
					<option value="">------Please Select------</option>
					<c:forEach var="item" items="${catagories}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
			</select> <br>
			<form:errors path="category" cssClass="errorMessage" />
			</td> --%>
		</tr>

		<tr>
		<td class="form-left"><primo:label code="Category" /><span
				class="errorMessage">*</span>
			</td>
			
			<td align="${left}">
				<form:select cssClass="flat" path="catagory" style="min-width:154px; max-width:154px"  id="categoryId">
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${catagories}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="catagory" cssClass="errorMessage" />
			</td>
			

		</tr>
<tr>
		 <td class="form-left"><primo:label code="Company" /><span	class="errorMessage">*</span></td>
		 	<td><form:select cssClass="flat" path="company" id="company" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companyLocation}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td>
			<%-- <td align="${left}">				
				<select name="company" id="company"   style="width:160px">
					<option value="">------Please Select------</option>
					<c:forEach var="item" items="${companyLocation}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
				<br><form:errors path="company" cssClass="errorMessage" />
			</td> --%>
			 <td class="form-left"><primo:label code="Terminal" /><span	class="errorMessage">*</span></td>
		      <td><form:select cssClass="flat" path="terminal" id="terminalId" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>
		<%-- 	<td align="${left}">
				<select name="terminal" id="terminalId"   style="width:160px">
					<option value="">------Please Select------</option>
					<c:forEach var="item1" items="${terminals}">
						<option value="${item1.id}">${item1.name}</option>
					</c:forEach>
				</select>
			   <br><form:errors path="terminal" cssClass="errorMessage" />
			</td> --%>
	    </tr>
	
	<tr>
		<td class="form-left"><primo:label code="Weekly Regular Hours for OT" /><span class="errorMessage"></span></td>
			<td align="${left}"><form:input path="weeklyHours" id="weeklyHoursid" style="min-width:151px; max-width:151px"
					Class="flat"  readonly="false"/> 
			</td>
		
		<td class="form-left"><primo:label code="Daily Regular Hours for OT" /><span class="errorMessage"></span></td>
			<td align="${left}"><form:input path="dailyHours" id="dailyHoursid"
					Class="flat" style="min-width:151px; max-width:151px" readonly="false"/> 
			</td>
		  
	</tr>



		<tr>

			<td class="form-left"><primo:label code="Regular Hourly Rate" />
			</td>
			<td align="${left}"><form:input path="hourlyRegularRate" id="" cssClass="flat" style="min-width:150px; max-width:150px"
					cssStyle="width:150px;" maxlength="20" /> <br>
					 <form:errors path="hourlyRegularRate" cssClass="errorMessage" /></td>


		</tr>

		<tr>

			<td class="form-left"><primo:label code="OverTime Rate Factor" />
			</td>
			<td align="${left}"><form:input path="hourlyOvertimeRate" id="hourlyOvertimeRate" cssClass="flat" style="min-width:150px; max-width:150px"
					cssStyle="width:150px;" maxlength="20" /> <br> <form:errors
					path="hourlyOvertimeRate" cssClass="errorMessage" /></td>


		</tr>
		<tr>

			<td class="form-left"><primo:label code="Double Rate Factor" />
			</td>
			<td align="${left}"><form:input path="hourlyDoubleTimeRate" id="hourlyDoubleTimeRate" cssClass="flat" style="min-width:150px; max-width:150px"
					cssStyle="width:150px;" maxlength="20" /> <br> <form:errors
					path="hourlyDoubleTimeRate" cssClass="errorMessage" /></td>


		</tr>

		<tr>
			<td class="form-left"><primo:label code="Valid From" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="validFrom" cssClass="flat" style="min-width:150px; max-width:150px"
					id="datepicker1" onblur="return formatDate1();" /> <br> <form:errors
					path="validFrom" cssClass="errorMessage" /></td>

			<td class="form-left"><primo:label code="Valid To" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="validTo" cssClass="flat" style="min-width:150px; max-width:150px"
					id="datepicker" onblur="return formatDate();" /> <br> <form:errors
					path="validTo" cssClass="errorMessage" /></td>
		</tr>

		<tr><td>&nbsp;</td>
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