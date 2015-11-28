<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function disableOwnerRate(){
	if(document.getElementById("ownerRate").value == 1){
		document.getElementById("ownerRateAmount").disabled=false;
	}else{
		document.getElementById("ownerRateAmount").disabled=true;
	}
}
function disableRow(){
	if(document.getElementById("fuelSurchargeType").value == "A"){
		enablepaddpegmiles();
	}else if(document.getElementById("fuelSurchargeType").value == "M"){
		document.getElementById("automatic").style.display = "none";
		document.getElementById("automatic1").style.display = "none";
		document.getElementById("automatic2").style.display = "none";
		document.getElementById("automatic3").style.display = "none";
		document.getElementById("manual").style.display = "table-row";
		document.getElementById("manual1").style.display = "table-row";
	}
	else{
		document.getElementById("automatic").style.display = "none";
		document.getElementById("automatic1").style.display = "none";
		document.getElementById("automatic2").style.display = "none";
		document.getElementById("automatic3").style.display = "none";
		document.getElementById("manual").style.display = "none";
		document.getElementById("manual1").style.display = "none";
	}
}

function enablepaddpegmiles(){
	if(document.getElementById("fuelsurchargeweeklyRate").value == 0){
		document.getElementById("manual").style.display = "none";
		document.getElementById("manual1").style.display = "none";
		document.getElementById("automatic").style.display = "table-row";
		document.getElementById("automatic1").style.display = "table-row";
		document.getElementById("automatic2").style.display = "table-row";
  		document.getElementById("automatic3").style.display = "none"; 
	}else{
		document.getElementById("manual").style.display = "none";
		document.getElementById("manual1").style.display = "none";
		document.getElementById("automatic").style.display = "none";
		document.getElementById("automatic1").style.display = "none";
		document.getElementById("automatic2").style.display = "none";
		document.getElementById("automatic3").style.display = "table-row";
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
			if(mm>12){
				alert("invalid date format");
				document.getElementById("datepicker1").value="";
				return true;
			}
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
	<primo:label code="Manage Billing Rate" />
</h3>
<form:form action="save.do?typ=${type}" name="modelForm"
	modelAttribute="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" >
		<tr class="table-heading">
			<td colspan="4"><b>Billing Rate</b>
			</td>
		</tr>
		<tr>
			<td class="form-left">Company<span class="errorMessage">*</span>
			</td><td><form:select cssClass="flat" path="companyLocation" style="min-width:154px; max-width:154px">
							<form:option value="">-----Please Select----</form:option>
							<form:options items="${companyLocation}" itemValue="id" itemLabel="name" />
						</form:select> 
					<br><form:errors path="companyLocation" cssClass="errorMessage" />
				</td>
		     
		     <%-- <td class="form-left">Customer name<span class="errorMessage">*</span></td>			
			<td align="${left}"><form:input path="customername" size="40"/>
			<br><form:errors path="customername" cssClass="errorMessage" /></td> --%>	
			<td class="form-left">Customer name<span class="errorMessage">*</span>
			</td><td><form:select cssClass="flat" path="customername" style="min-width:154px; max-width:154px">
							<form:option value="">-----Please Select----</form:option>
							<form:options items="${customernames}" itemValue="id" itemLabel="name" />
						</form:select> 
					<br><form:errors path="customername" cssClass="errorMessage" />
				</td>
		</tr>
		<tr>
			<td class="form-left">Transfer Station<span class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="transferStation" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${transferStation}" itemValue="id" itemLabel="name" />
				</form:select> 
			<br><form:errors path="transferStation" cssClass="errorMessage" /></td>
			<td class="form-left">Landfill<span class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="landfill" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${landfill}" itemValue="id" itemLabel="name" />
				</form:select> 
			<br><form:errors path="landfill" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left">Bill Using<span class="errorMessage">*</span></td>
			<td align="${left}"><form:select cssClass="flat" path="billUsing" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${billUsing}" itemValue="dataValue"
						itemLabel="dataText" />
				</form:select> <br><form:errors path="billUsing" cssClass="errorMessage" /></td>
			<td class="form-left">Sort By<span class="errorMessage">*</span></td>
			<td align="${left}"><form:select cssClass="flat" path="sortBy" style="min-width:154px; max-width:154px">
				<form:option value="">-----Please Select----</form:option>
				<form:options items="${billUsing}" itemValue="dataValue"
					itemLabel="dataText" />
			</form:select> <br><form:errors path="sortBy" cssClass="errorMessage" /></td>
		</tr>
		<%-- <tr>
			<td class="form-left">Driver Rate<span class="errorMessage">*</span>
			</td>			
			<td align="${left}"><form:input path="driverRate" />
			<br><form:errors path="driverRate" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left">Owner Rate<span class="errorMessage">*</span>
			</td>			
			<td align="${left}"><form:select cssClass="flat" path="ownerRate" id="ownerRate" onchange="disableOwnerRate();">
					<form:options items="${yesNo}" itemValue="dataValue"
						itemLabel="dataText" />	
				</form:select>				
				 <br><form:errors path="ownerRate" cssClass="errorMessage" /></td>							
			<td class="form-left">Owner Rate Amount<span class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="ownerRateAmount" id="ownerRateAmount" disabled="true"/>
			<br><form:errors path="ownerRateAmount" cssClass="errorMessage" /></td>
		</tr>
 --%>		<tr>
			<td class="form-left">Tonnage Premium
			</td>
			<td align="${left}"><form:select cssClass="flat" path="tonnagePremium" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${tonnages}" itemValue="id"
						itemLabel="code" />
				</form:select> <br><form:errors path="tonnagePremium" cssClass="errorMessage" /></td>
			<td class="form-left">Demmurage Charge
			</td>
			<td align="${left}"><form:select cssClass="flat" path="demmurageCharge" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${demurrage}" itemValue="id"
						itemLabel="demurragecode" />
				</form:select> <br><form:errors path="demmurageCharge" cssClass="errorMessage" /></td>
			
			
		</tr>
		<TR>
		<td class="form-left">Print Using</td>
			<td align="${left}">
			<form:select cssClass="flat" path="billedby" style="min-width:154px; max-width:154px">
					<%-- <form:option value="">-----Please Select----</form:option>
				    <form:option value="bydestination">PrintBy_Destination</form:option>
	                <form:option value="byorigin">PrintBy_Origin</form:option>
                    <form:option value="bygallon">PrintBy_Gallon</form:option>
                    <form:option value="bydestinationWeight">PrintBy_DestinationWeight</form:option>
	                <form:option value="byoriginWeight">PrintBy_OriginWeight</form:option>
                    <form:option value="bynoFuelSurchage">PrintBy_NoFuelSurchage</form:option>
                    <form:option value="bynoTonnage">PrintBy_NoTonnage</form:option> --%>
                    
                    <form:option value="">-----Please Select----</form:option>
				    <form:option value="bydestination">Format1_Min_Bill_weights_by Destination</form:option>
	                <form:option value="byorigin">Format2_Min_Bill_weights_by Origin</form:option>
                    <form:option value="bygallon">Format3_Gallons</form:option>
                    <form:option value="bydestinationWeight">Format4_Per Load_by Destination</form:option>
	                <form:option value="byoriginWeight">Format5_Per Load_by Origin</form:option>
                    <form:option value="bynoFuelSurchage">Format6_No Fuel Surcharge_No Tonnage_No Demurrage</form:option>
                    <form:option value="bynoTonnage">Format7_No Tonnage_No Demurrage</form:option>
            </form:select>
            </td>
		</TR>
		<tr class="table-heading">
			<td colspan="4"><b>Rate Details</b>
			</td>
		</tr>
		<tr>			
			<td class="form-left">Rate Type<span class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:select cssClass="flat" path="rateType" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${rateTypes}" itemValue="dataValue"
						itemLabel="dataText" />
				</form:select> <br><form:errors path="rateType" cssClass="errorMessage" /></td>
			<td class="form-left">Rate (USD)<span class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="value" style="min-width:150px; max-width:150px" />
			<br><form:errors path="value" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left">Minimum Billable Gross Weight</td>
			<td align="${left}"><form:input path="minbillablegrossWeight" style="min-width:150px; max-width:150px"/>
			<br><form:errors path="minbillablegrossWeight" cssClass="errorMessage" /></td>
			<td class="form-left">Rate Using<span class="errorMessage">*</span></td>
			<td align="${left}"><form:select cssClass="flat" path="rateUsing" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${rateUsing}" itemValue="dataValue"
						itemLabel="dataText" />
			</form:select> <br><form:errors path="rateUsing" cssClass="errorMessage" /></td>

		</tr>
		<tr>
			<td class="form-left">Valid From<span class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input id="datepicker" path="validFrom"
					cssClass="flat" style="min-width:150px; max-width:150px" onblur="return formatDate('datepicker');"/> 
			<br><form:errors path="validFrom" cssClass="errorMessage" /></td>
			<td class="form-left">Valid To<span class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input id="datepicker1" style="min-width:150px; max-width:150px" path="validTo"
					cssClass="flat" onblur="return formatDate1();"/> 
			<br><form:errors path="validTo" cssClass="errorMessage" /></td>
		</tr>
		<tr class="table-heading">
			<td colspan="4"><b>Fuel Surcharge</b></td>
		</tr>
		<tr>
			<td class="form-left">Fuel Surcharge Type<span class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:select cssClass="flat" path="fuelSurchargeType" style="min-width:154px; max-width:154px" id="fuelSurchargeType" onchange="disableRow();">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${fuelSurchargeTypes}" itemValue="dataValue"
						itemLabel="dataText" />
				</form:select> <br><form:errors path="fuelSurchargeType" cssClass="errorMessage" /></td>
			<td class="form-left">Fuel Surcharge Weekly Rate<span class="errorMessage">*</span></td>			
			<td align="${left}">
				<form:select cssClass="flat" path="fuelsurchargeweeklyRate" style="min-width:154px; max-width:154px" id="fuelsurchargeweeklyRate" onchange="disableRow();">
					<form:options items="${yesNo}" itemValue="dataValue" itemLabel="dataText" />	
				</form:select>				
				 <br><form:errors path="fuelsurchargeweeklyRate" cssClass="errorMessage" />
			</td>			
		</tr>
		<tr class="table-heading" id="automatic">
			<td colspan="4"><b>Automatic</b></td>
		</tr>
		<tr id="automatic1">			
			<td class="form-left">Padd<span class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:select cssClass="flat" path="padd" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
				<form:options items="${padds}" itemValue="id" itemLabel="code" />
				</form:select> <br><form:errors path="padd" cssClass="errorMessage" /></td>
			<td class="form-left">Padd Using<span class="errorMessage">*</span></td>
			<td align="${left}"><form:select cssClass="flat" path="paddUsing" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${paddUsing}" itemValue="dataValue"
						itemLabel="dataText" />
			</form:select> <br><form:errors path="paddUsing" cssClass="errorMessage" /></td>
		</tr>
		<tr id="automatic2">
			<td class="form-left">Peg<span class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="peg" style="min-width:150px; max-width:150px"/>
			<br><form:errors path="peg" cssClass="errorMessage" /></td>
			<td class="form-left">Miles</td>			
			<td align="${left}"><form:input path="miles" style="min-width:150px; max-width:150px" />
			<br><form:errors path="miles" cssClass="errorMessage" /></td>
			<td></td><td></td>
		</tr>
		<tr id="automatic3">
			<td class="form-left">Weekly Rate Using<span class="errorMessage">*</span></td>
			<td align="${left}"><form:select cssClass="flat" path="weeklyRateUsing" style="min-width:154px; max-width:154px">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${weeklyRateUsing}" itemValue="dataValue"
						itemLabel="dataText" />
			</form:select> <br><form:errors path="weeklyRateUsing" cssClass="errorMessage" /></td>
			<td class="form-left"></td>			
			<td align="${left}"></td>
			<td></td><td></td>
		</tr>	
		<tr class="table-heading" id="manual">
			<td colspan="4"><b>Manual</b></td>
		</tr>	
		<tr id="manual1">			
			<td class="form-left">Surcharge per Ton<span class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="surchargePerTon" style="min-width:150px; max-width:150px" id="surchargePerTon"/> 
			<br><form:errors path="surchargePerTon" cssClass="errorMessage" /></td>
			<td class="form-left">Surcharge Amount<span class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="surchargeAmount" style="min-width:150px; max-width:150px" id="surchargeAmount"/> 
			<br><form:errors path="surchargeAmount" cssClass="errorMessage" /></td>
		</tr> 
			<tr class="table-heading">
			<td colspan="4"><b>Notes</b></td>
		</tr>
		<tr>
			<td align="${left}" colspan="5">
				<form:textarea path="notes" rows="3" cols="115"/>    	
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<script type="text/javascript">disableOwnerRate();</script>
				<script type="text/javascript">disableRow();</script>
				<script type="text/javascript">searchCharge();</script>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick="" value="Save" class="flat" /> <input
				type="reset" id="resetBtn" value="Reset" class="flat" /> 
				<!-- <input
				type="button" id="cancelBtn" value="Cancel" class="flat"
				onClick="location.href='list.do'" /> -->
				<c:if test="${type eq 'alert'}">
    <input type="button" id="cancelBtn" value="Cancel" class="flat"
   onClick="JavaScript:window.location='${ctx}/rate/expirationreport/list.do?type=billingrate';" />
   </c:if>
   <c:if test="${type ne 'alert'}">
   <input type="button" id="cancelBtn" value="Cancel" class="flat" 
   onClick="location.href='list.do'" />
   </c:if>
				</td>
		</tr>
	</table>
</form:form>

