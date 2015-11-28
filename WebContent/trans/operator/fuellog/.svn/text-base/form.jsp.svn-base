<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function getTime(field) {
		var timein = document.getElementById(field).value;
		if (timein != "") {
			if (timein.length < 4) {
				alert("Invalidte time format");
				document.getElementById(field).value = "";
				return true;
			} else {
				var str = new String(timein);
				if (!str.match(":")) {
					var hour = str.substring(0, 2);
					//var min=str.substring(2,4);
					var min = str.substring(2);
					if (hour >= 24 || min >= 60) {
						alert("Invalidte time format");
						document.getElementById(field).value = "";
						return true;
					}
					var time = hour + ":" + min;
					document.getElementById(field).value = time;
				}
			}
		}
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

	function findFuelCard() {
		if (document.getElementById("driver") != "") {
			/* 	var selectedfuelvendor= document.getElementById('fuelvendor');
				var fuelvendor=selectedfuelvendor.options[selectedfuelvendor.selectedIndex].value; */
			var slecteddriver = document.getElementById("driver");
			var driver = slecteddriver.options[slecteddriver.selectedIndex].value;
			if (driver != "") {
				jQuery
						.ajax({
							url : '${ctx}/operator/fuellog/ajax.do?action=findFuelCard&driver='
									+ driver,
							success : function(data) {
								var listData = jQuery.parseJSON(data);

								var options = '<option value="">------Please Select------</option>';
								for ( var i = 0; i < listData.length; i++) {
									var driverfuelcard = listData[i];
									options += '<option value="'+driverfuelcard.fuelcard.id+'">'
											+ driverfuelcard.fuelcard.fuelcardNum
											+ ' - '
											+ driverfuelcard.fuelvendor.name
											+ '</option>';
								}
								$("#fuelcards").html(options);
								if (listData.length <= 0) {
									options = '';
									$("#fuelvendor").html(options);
								}
							}
						});

			} else {
				var options = '<option value="">------Please Select------</option>';
				$("#fuelcards").html(options);
			}

		}
	}

	$(document).ready(function (){
		var fuellogId=document.getElementById("id").value;
		var fuelvendor=document.getElementById("fuelvendor").value;
		var driver=document.getElementById("driver").value;
		var fulecard=document.getElementById("fuelcards").value;		
		if(fuellogId!=''){
			if(driver!='' && fuelvendor!=''){
				if(fulecard==''){					
					jQuery.ajax({
						url : '${ctx}/operator/fuellog/ajax.do?action=findMissingFuelCard&driver='
								+ driver+'&fuelvendor='+fuelvendor,
						success : function(data) {
							var listData = jQuery.parseJSON(data);

							var options = '<option value="">------Please Select------</option>';
							for ( var i = 0; i < listData.length; i++) {
								var driverfuelcard = listData[i];
								options += '<option value="'+driverfuelcard.fuelcard.id+'">'
										+ driverfuelcard.fuelcard.fuelcardNum
										+ ' - '
										+ driverfuelcard.fuelvendor.name
										+ '</option>';
							}
							$("#fuelcards").html(options);
						
						}
					});
				}				
			}		
		}		
	});
	
	
	
	function findFuelVendor() {
		if (document.getElementById("fuelcards") != "") {
			/* 	var selectedfuelvendor= document.getElementById('fuelvendor');
				var fuelvendor=selectedfuelvendor.options[selectedfuelvendor.selectedIndex].value; */
			var selectedfuelcard = document.getElementById("fuelcards");
			var fuelcard = selectedfuelcard.options[selectedfuelcard.selectedIndex].value;
			if (fuelcard != "") {
				jQuery
						.ajax({
							url : '${ctx}/operator/fuellog/ajax.do?action=findFuelVendor&fuelcard='
									+ fuelcard,
							success : function(data) {
								var listData = jQuery.parseJSON(data);
								var options = '';
								for ( var i = 0; i < listData.length; i++) {
									var fuelcard = listData[i];
									options += '<option value="'+fuelcard.fuelvendor.id+'">'
											+ fuelcard.fuelvendor.name
											+ '</option>';
								}
								$("#fuelvendor").html(options);
							}
						});

			} else {
				var options = '';
				$("#fuelvendor").html(options);
			}

		}
	}

	function disableFuelVendor() {
		$('#fuelvendor').attr("disabled", true);
	}

	jQuery(function($) {
		$('form').bind('submit', function() {
			$(this).find(':input').removeAttr('disabled');
		});
	});
</script>

<br/>
<form:form action="save.do" name="modelForm"
	modelAttribute="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="5"><b>Add/Update Fuel Log</b></td>
		</tr>



		<tr>
			<%--  <td class="form-left"><primo:label code="Fuel Vender" /><span class="errorMessage">*</span></td>
		    <td align="${left}"><form:input path="fuelvendor" /> 
			<br><form:errors path="fuelvendor" cssClass="errorMessage" /></td> --%>


			<td class="form-left"><primo:label code="Driver Name" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="driversid" id="driver" style="min-width:154px; max-width:154px"
					onchange="findFuelCard();">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${drivernames}" itemValue="id"
						itemLabel="fullName" />
				</form:select> <br>
			<form:errors path="driversid" cssClass="errorMessage" /></td>


			<td class="form-left"><primo:label code="Fuel Card" /></td>
			<td><form:select cssClass="flat" path="fuelcard" id="fuelcards" style="min-width:154px; max-width:154px"
					onchange="findFuelVendor();">
					<c:if test="${fuelcard ne null}">
						<form:option value="${fuelcard.id}">${fuelcard.fuelcardNum} - ${fuelcard.fuelvendor.name}</form:option>
					</c:if>
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<c:forEach var="driverfuelcard" items="${driverfuelcard}">
						<form:option value="${driverfuelcard.fuelcard.id}">${driverfuelcard.fuelcard.fuelcardNum} - ${driverfuelcard.fuelvendor.name } </form:option>
					</c:forEach>
				</form:select> <br> <form:errors path="fuelcard" cssClass="errorMessage" />
			</td>



			<%-- <td class="form-left"><primo:label code="Driver First Name" /><span class="errorMessage">*</span>
			</td><td><form:select cssClass="flat" path="driverFname">
							<form:option value="">-----Please Select----</form:option>
							<form:options items="${drivers}" itemValue="id" itemLabel="firstName" />
						</form:select> 
					<br><form:errors path="driverFname" cssClass="errorMessage" />
				</td> --%>

		</tr>

		<tr>
			<td class="form-left"><primo:label code="Company" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="company" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${companyLocation}" itemValue="id"
						itemLabel="name" />
				</form:select> <br>
			<form:errors path="company" cssClass="errorMessage" /></td>


			<td class="form-left"><primo:label code="Fuel Vendor" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="fuelvendor" style="min-width:154px; max-width:154px"
					id="fuelvendor" onchange="findFuelCard();">
					<c:if test="${fuelvendor ne null}">
						<form:option value="${fuelvendor.id}">${fuelvendor.name}</form:option>
					</c:if>
					<form:option value="">&nbsp;&nbsp;&nbsp;</form:option>

				</form:select> <br>
			<form:errors path="fuelvendor" cssClass="errorMessage" /></td>



		</tr>

		<tr>
			<%-- <td class="form-left"><primo:label code="Driver Last Name" /><span class="errorMessage">*</span>
			  </td><td><form:select cssClass="flat" path="driverLname">
							<form:option value="">-----Please Select----</form:option>
							<form:options items="${drivers}" itemValue="id" itemLabel="lastName" />
						</form:select> 
					<br><form:errors path="driverLname" cssClass="errorMessage" />
				</td> --%>

			<td class="form-left"><primo:label code="Terminal" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="terminal" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> <br>
			<form:errors path="terminal" cssClass="errorMessage" /></td>



			<td class="form-left"><primo:label code="City" />
				<!-- <span class="errorMessage">*</span> -->
			</td>
			<td align="${left}"><form:input path="city"  style="min-width:150px; max-width:150px"/> <br>
			<form:errors path="city" cssClass="errorMessage" />
			</td>


		</tr>

		<tr>

			<td class="form-left"><primo:label code="State" />
				<!-- <span class="errorMessage">*</span> -->
			</td>
			<td><form:select cssClass="flat" path="state" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${state}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="state" cssClass="errorMessage" /></td>

			<td class="form-left"><primo:label code="Unit#" /><span
				class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="unit" style="min-width:154px; max-width:154px">
					<form:option value="">-----<primo:label
							code="Please Select" />-----</form:option>
					<form:options items="${vehicles}" itemValue="id" itemLabel="unit" />
				</form:select> <br> <form:errors path="unit" cssClass="errorMessage" /></td>

		</tr>

		<tr>

			<td class="form-left"><primo:label code="Invoice Date" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="invoiceDate" style="min-width:150px; max-width:150px"
					cssClass="flat" id="datepicker" onblur="return formatDate();" /> <br>
			<form:errors path="invoiceDate" cssClass="errorMessage" /></td>


			<td class="form-left"><primo:label code="Invoice No." /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="invoiceNo" style="min-width:150px; max-width:150px" /> <br>
			<form:errors path="invoiceNo" cssClass="errorMessage" />
			</td>

			<%-- <td class="form-left"><primo:label code="Transaction Date" /><span class="errorMessage">*</span></td>
			  <td align="${left}"><form:input path="transactiondate"
					cssClass="flat"  id="datepicker" onblur="return formatDate();"/>
					<br><form:errors path="transactiondate" cssClass="errorMessage" />
			 </td> --%>





		</tr>

		<tr>

			<td class="form-left"><primo:label code="Transaction Date" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input id="datepicker1" style="min-width:150px; max-width:150px"
					path="transactiondate" cssClass="flat"
					onblur="return formatDate1();" /> <br> <form:errors
					path="transactiondate" cssClass="errorMessage" />
			</td>


			<td class="form-left"><primo:label code="Transaction Time" />
				<!-- <span class="errorMessage">*</span> -->
			</td>
			<td align="${left}"><form:input id="transactiontime" style="min-width:150px; max-width:150px"
					path="transactiontime" cssClass="flat"
					onkeypress="return onlyNumbers(event,false)"
					onblur="return getTime('transactiontime')" /> <br> <form:errors
					path="transactiontime" cssClass="errorMessage" /></td>
			<%-- <td class="form-left"><primo:label code="Fuel Card Number " /><span class="errorMessage">*</span></td>			
			<td align="${left}"><form:input path="fuelCardNumber" /> 
			<br><form:errors path="fuelCardNumber" cssClass="errorMessage" /></td> --%>





		</tr>

		<tr>
			<%-- <td class="form-left"><primo:label code="Fuel Type" /><span class="errorMessage">*</span></td>
		    <td align="${left}"><form:input path="fueltype" /> 
			 <br><form:errors path="fueltype" cssClass="errorMessage" /></td> --%>
			<td class="form-left">Fuel Type<span class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:select cssClass="flat" path="fueltype" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${fuelTypes}" itemValue="dataText"
						itemLabel="dataText" />
				</form:select> <br>
			<form:errors path="fueltype" cssClass="errorMessage" />
			</td>


			<td class="form-left">&nbsp;</td>
			<td>&nbsp;</td>
			<%-- <td class="form-left"><primo:label code="City" /><span class="errorMessage">*</span></td>
			  <td align="${left}"><form:input path="city" /> 
			  <br><form:errors path="city" cssClass="errorMessage" /></td> --%>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Gallons" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="gallons" style="min-width:150px; max-width:150px"/> <br>
			<form:errors path="gallons" cssClass="errorMessage" />
			</td>

			<td class="form-left"><primo:label code="Unit Price" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="unitprice" style="min-width:150px; max-width:150px"/> <br>
			<form:errors path="unitprice" cssClass="errorMessage" />
			</td>
		</tr>

		<tr>
			<td class="form-left"><primo:label code="Gross Cost" /></td>
			<td align="${left}"><form:input path="grosscost" style="min-width:150px; max-width:150px" /> <br></td>

			<td class="form-left"><primo:label code="Fees" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="fees" style="min-width:150px; max-width:150px"/> <br>
			<form:errors path="fees" cssClass="errorMessage" />
			</td>
			
	 </tr>
	 <tr>		
			

			<td class="form-left"><primo:label code="Discounts" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="discounts" style="min-width:150px; max-width:150px" /> <br>
			<form:errors path="discounts" cssClass="errorMessage" />
			</td>
		
			<td class="form-left"><primo:label code="Amount" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="amount" style="min-width:150px; max-width:150px"/> <br>
			<form:errors path="amount" cssClass="errorMessage" />
			</td>

			
		</tr>

		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick="" value="Save" class="flat" /> <input
				type="reset" id="resetBtn" value="Reset" class="flat" /> <input
				type="button" id="cancelBtn" value="Cancel" class="flat"
				onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>
<script language="javascript">
	getTime('transactiontime');
</script>
