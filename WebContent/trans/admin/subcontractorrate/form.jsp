<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">

var submitting = false;

function submitform(type){	
	
	document.forms["modelForm"].action='${ctx}/admin/subcontractorrate/save.do?typ='+type;
	 if(submitting) {
alert('please wait a moment...');
}
else{
     submitting = true;
	document.forms["modelForm"].submit();
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
	<primo:label code="Add/Update Subcontractor Rate" />
</h3>
<form:form action="save.do?typ=${type}" name="modelForm"
	modelAttribute="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" >
		<tr class="table-heading">
			<td colspan="4"><b>Subcontractor Rate</b>
			</td>
		</tr>
		<tr>
		<td class="form-left">Company<span class="errorMessage">*</span>
			</td><td><form:select cssClass="flat" path="companyLocation" style="min-width:154px; max-width:154px">
							<form:option value="">-----Please Select----</form:option>
							<form:options items="${companyLocation}" itemValue="id" itemLabel="name" />
						</form:select> 
					<br><form:errors path="companyLocation" cssClass="errorMessage" /></td>
					<td class="form-left">Subcontractor<span class="errorMessage">*</span>
			</td><td><form:select cssClass="flat" path="subcontractor" style="min-width:154px; max-width:154px">
							<form:option value="">-----Please Select----</form:option>
							<form:options items="${subContractor}" itemValue="id" itemLabel="name" />
						</form:select> 
					<br><form:errors path="subcontractor" cssClass="errorMessage" /></td>
		<td></td><td></td>
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
			<td align="${left}"><form:input path="value" style="min-width:150px; max-width:150px"/>
			<br><form:errors path="value" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left">Minimum Billable Gross Weight</td>
			<td align="${left}"><form:input path="minbillablegrossWeight" style="min-width:150px; max-width:150px" />
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
			<td align="${left}"><form:input id="datepicker" path="validFrom" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate('datepicker');"/> 
			<br><form:errors path="validFrom" cssClass="errorMessage" /></td>
			<td class="form-left">Valid To<span class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input id="datepicker1" path="validTo" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate1();"/> 
			<br><form:errors path="validTo" cssClass="errorMessage" /></td>
		</tr>
		<tr class="table-heading">
			<td colspan="4"><b>Fuel Surcharge</b></td>
		</tr>
		
		<tr>
			<%-- <td class="form-left">Subcontractor Rate<span class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="subcontractorRate" /> --%>
			<%-- <br><form:errors path="subcontractorRate" cssClass="errorMessage" /></td> --%>
			<td class="form-left">Fuel Surcharge Amount</td>
			<td align="${left}"><form:input path="fuelSurchargeAmount"  style="min-width:150px; max-width:150px"/>
			<br><form:errors path="otherCharges" cssClass="errorMessage" /></td>
				<td class="form-left">Other Charges</td>
			<td align="${left}"><form:input path="otherCharges" style="min-width:150px; max-width:150px" />
			<br><form:errors path="otherCharges" cssClass="errorMessage" /></td>
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
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
			
			<input type="button"
				name="create" id="create" onclick="javascript:submitform('${type}');"
				value="<primo:label code="Save"/>" class="flat" />
				
				 <input
				type="reset" id="resetBtn" value="Reset" class="flat" /> 
				<!-- <input
				type="button" id="cancelBtn" value="Cancel" class="flat"
				onClick="location.href='list.do'" /> -->
				<c:if test="${type eq 'alert'}">
    <input type="button" id="cancelBtn" value="Cancel" class="flat"
   onClick="JavaScript:window.location='${ctx}/rate/expirationreport/list.do?type=subcontractorrate';" />
   </c:if>
   <c:if test="${type ne 'alert'}">
   <input type="button" id="cancelBtn" value="Cancel" class="flat" 
   onClick="location.href='list.do'" />
   </c:if>
				</td>
		</tr>
	</table>
</form:form>

