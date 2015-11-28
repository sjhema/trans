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
		}
	 }
   }
}
</script>
<h3>
	<primo:label code="Add/Update FuelSurchargeWeeklyRate" />
</h3>
<form:form action="save.do?typ=${type}" name="fuelsurchargeweeklyrateForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td align="${left}" colspan="2"><b><primo:label code="Add/Update FuelSurchargeWeeklyRate"/></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Transfer Station" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="transferStations" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${transferStations}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="transferStations" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Landfill  Station" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="landfillStations" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${landfillStations}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="landfillStations" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>			
			<td class="form-left">Rate Type<span class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:select cssClass="flat" style="min-width:154px; max-width:154px"  path="rateType">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${rateTypes}" itemValue="dataValue"
						itemLabel="dataText" />
				</form:select> <br><form:errors path="rateType" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Fuel Surcharge Rate" /><span
					class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="fuelSurchargeRate" style="min-width:150px; max-width:150px" cssClass="flat"/> 
			<br><form:errors path="fuelSurchargeRate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="From Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker" path="fromDate" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate();"/> <br> <form:errors
					path="fromDate" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="To Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker1" path="toDate" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate1();"/> <br> <form:errors
					path="toDate" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}"><input type="submit"
				name="create" id="create" onclick=""
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> 
				<%-- <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" /> --%>
				<c:if test="${type eq 'alert'}">
    <input type="button" id="cancelBtn" value="Cancel" class="flat"
   onClick="JavaScript:window.location='${ctx}/rate/expirationreport/list.do?type=weeklyrate';" />
   </c:if>
   <c:if test="${type ne 'alert'}">
   <input type="button" id="cancelBtn" value="Cancel" class="flat" 
   onClick="location.href='list.do'" />
   </c:if>
			</td>
		</tr>
	</table>
</form:form>
