<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function searchFuelPurchaseReport() {
	document.forms[0].elements["reportType"].value = 'FUEL_PURCHASE';
	
	/* document.forms[0].target="reportData"; */
    document.forms[0].submit();
}

function searchFuelTruckReport() {
	document.forms[0].elements["reportType"].value = 'FUEL_TRUCK';
	
	/* document.forms[0].target="reportData"; */
    document.forms[0].submit();
}

function searchJJKellerFuelCardReport() {
	document.forms[0].elements["reportType"].value = 'JJ_KELLER_FUEL_TXN';
	
	/* document.forms[0].target="reportData"; */
    document.forms[0].submit();
}

function getTime(timedata){
	var timein=document.getElementById(timedata).value;
	if(timein!=""){
		if(timein.length<4){
			alert("Invalidte time format");
			document.getElementById(timedata).value="";
			return true;
		}else{
			var str=new String(timein);
			if(!str.match(":")){
				var hour=str.substring(0,2);
				var min=str.substring(2,4);
				if(hour>=24 || min>=60){
					alert("Invalidte time format");
					document.getElementById(timedata).value="";
					return true;
				}
				var time=hour+":"+min;
				document.getElementById(timedata).value=time;
			}
		}
	}
}
function getTime1(){
	var timeout=document.getElementById("transfertimeout").value;
	if(timeout!=""){
		if(timeout.length<4){
			alert("Invalidte time format");
			document.getElementById("transfertimeout").value="";
			return true;
		}
		else{
			var str=new String(timeout);
			if(!str.match(":")){
				var hour=str.substring(0,2);
				var min=str.substring(2,4);
				if(hour>=24 || min>=60){
					alert("Invalidte time format");
					document.getElementById("transfertimeout").value="";
					return true;
				}
				var time=hour+":"+min;
				document.getElementById("transfertimeout").value=time;
			}
		}
	}
}
function getTime2(){
	var trtimein=document.getElementById("landfilltimein").value;
	if(trtimein!=""){
		if(trtimein.length<4){
			alert("Invalidte time format");
			document.getElementById("landfilltimein").value="";
			return true;
		}
		else{
			var str=new String(trtimein);
			if(!str.match(":")){
				var hour=str.substring(0,2);
				var min=str.substring(2,4);
				if(hour>=24 || min>=60){
					alert("Invalidte time format");
					document.getElementById("landfilltimein").value="";
					return true;
				}
				var time=hour+":"+min;
				document.getElementById("landfilltimein").value=time;
			}
		}
	}
}
function getTime3(){
	var trtimeout=document.getElementById("landfilltimeout").value;
	if(trtimeout!=""){
		if(trtimeout.length<4){
			alert("Invalidte time format");
			document.getElementById("landfilltimeout").value="";
			return true;
		}
		else{
			var str=new String(trtimeout);
			if(!str.match(":")){
				var hour=str.substring(0,2);
				var min=str.substring(2,4);
				if(hour>=24 || min>=60){
					alert("Invalidte time format");
					document.getElementById("landfilltimeout").value="";
					return true;
				}
				var time=hour+":"+min;
				document.getElementById("landfilltimeout").value=time;
			}
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

$(document).ready(function(){
	   $("select").multiselect();
});
</script>
<br/>
<%-- <h3><primo:label code="FuelLog Report" /></h3> --%>
<form:form action="search.do" name="searchForm" method="post" commandName="modelObject">
	<form:hidden path="reportType" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Fuel Log Report" />
			</b></td>
	    </tr>
	    
		 <tr>
		 	<td class="form-left"><primo:label code="Companies" /><span class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="company" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Terminals" /><span class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="terminal" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>
		<tr>
		<tr>
			<td class="form-left"><primo:label code="Transaction date" /><span class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="transactionDateFrom" cssClass="flat"  onblur="return formatDate1('transactionDateFrom');"/> 
				<script type="text/javascript">
					$(function() {
					$("#transactionDateFrom").datepicker({
						dateFormat:'mm-dd-yy',
		            	changeMonth: true,
		    			changeYear: true
		    		});
					});
				</script>
				To:<form:input size="10" path="transactionDateTo" cssClass="flat"  onblur="return formatDate1('transactionDateTo');"/>
				<script type="text/javascript">
					$(function() {
					$("#transactionDateTo").datepicker({
						dateFormat:'mm-dd-yy',
		            	changeMonth: true,
		    			changeYear: true
		    		});
					});
				</script>
			</td>
		</tr>
		<tr>
			<%--  <td class="form-left"><primo:label code="Fuel Vendor" /></td>
		    <td align="${left}"><form:input name="fuelVendor" path="fuelVendor" /> --%>
		    <td class="form-left"><primo:label code="Fuel Vendor" /><span class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="fuelVendor" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${fuelvendor}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="fuelVendor" cssClass="errorMessage" />
			</td>
			<%-- <td class="form-left"><primo:label code="Fuel Type" /></td>
		    <td align="${left}"><form:input path="fueltype" /></td>	 --%>
		    <td class="form-left"><primo:label code="Fuel Type" />
		    <td align="${left}"><form:select cssClass="flat" path="fueltype" multiple="true">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${fuelTypes}" itemValue="dataText"
						itemLabel="dataText" />
				</form:select> <br><form:errors path="fueltype" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Unit#" /><span
				class="errorMessage"></span></td>
			 <td><form:select cssClass="flat" path="unit" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${trucks}" itemValue="unit" itemLabel="unit" />
				</form:select>
			 </td>
			 <td class="form-left"><primo:label code="States" /><span class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="state" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${states}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="state" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Driver" /><span class="errorMessage"></span></td>
			  <td><form:select cssClass="flat" path="driver" id="driverId" multiple="true" onchange="javascript:getTerminal();">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${drivers}" itemValue="fullName" itemLabel="fullName"></form:options>
				</form:select> <br> <form:errors path="driver" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Gallons" /><span class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="gallonsFrom" cssClass="flat"/> 
				To:<form:input size="10" path="gallonsTo" cssClass="flat"/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Subcontractor" /><span class="errorMessage"></span></td>
			  <td><form:select cssClass="flat" path="subContractor" id="subContractor" multiple="true" >
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${subcontractors}" itemValue="id" itemLabel="name"></form:options>
				</form:select> <br> <form:errors path="subContractor" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Unit Price" /><span class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="unitPriceFrom" cssClass="flat"/> 
				To:<form:input size="10" path="unitPriceTo" cssClass="flat"/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Card#" /></td>
		    <td align="${left}"><form:input path="cardno" /></td>	
		    <td class="form-left"><primo:label code="Fees" /><span class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="feesFrom" cssClass="flat"/> 
				To:<form:input size="10" path="feesTo" cssClass="flat"/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Invoice#" /><span class="errorMessage"></span></td>
			 <td  align="${left}">
				From:<form:input size="10" path="fromInvoiceNo" cssClass="flat"/> 
				To:<form:input size="10" path="invoiceNoTo" cssClass="flat"/>
			 </td>
			 <td class="form-left"><primo:label code="Discounts" /><span class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="discountFrom" cssClass="flat"/> 
				To:<form:input size="10" path="discountTo" cssClass="flat"/>
			</td>
			
		</tr>
			
		 <tr>
	        <td class="form-left"><primo:label code="Invoice Date" /><span class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="fromInvoiceDate" cssClass="flat"  onblur="return formatDate('fromInvoiceDate');"/> 
				<script type="text/javascript">
					$(function() {
					$("#fromInvoiceDate").datepicker({
						dateFormat:'mm-dd-yy',
		            	changeMonth: true,
		    			changeYear: true
		    		});
					});
				</script>
				To:<form:input size="10" path="invoiceDateTo" cssClass="flat"  onblur="return formatDate('invoiceDateTo');"/>
				<script type="text/javascript">
					$(function() {
					$("#invoiceDateTo").datepicker({
						dateFormat:'mm-dd-yy',
		            	changeMonth: true,
		    			changeYear: true
		    		});
					});
				</script>
			</td>
			 <td class="form-left"><primo:label code="Amount" /><span class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="amountFrom" cssClass="flat"/> 
				To:<form:input size="10" path="amountTo" cssClass="flat"/>
			</td>
	        
		    <%-- <td class="form-left"><primo:label code="Transaction date" /><span class="errorMessage"></span></td>
			<td align="${left}">
			     From:<form:input size="10" path="transactionDateFrom" cssClass="flat" onblur="return formatDate1('transactionDateFrom');"/>
						<script type="text/javascript">
							$(function() {
							$("#transactionDateFrom").datepicker1({
								dateFormat:'mm-dd-yy',
				            	changeMonth: true,
				    			changeYear: true
				    		});
							});
						</script>
					To:<form:input size="10" path="transactionDateTo" cssClass="flat" onblur="return formatDate1('transactionDateTo');"/>
					<script type="text/javascript">
						$(function() {
						$("#transactionDateTo").datepicker1({
							dateFormat:'mm-dd-yy',
			            	changeMonth: true,
			    			changeYear: true
			    		});
						});
					</script>
			  </td> --%>
		</tr>
		 
		
		<tr><td colspan="2"></td></tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3"><input type="button"
				onclick="javascript:searchFuelPurchaseReport()" value="Fuel - Purchase report" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button"
			onclick="javascript:searchFuelTruckReport()" value="Fuel - Truck report" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button"
			onclick="javascript:searchJJKellerFuelCardReport()" value="Fuel - JJ Keller Report" />
			</td>
		</tr>
		
		
	</table>
</form:form>
 <%-- <table width="100%">
	<tr>
		<td align="${left}" width="100%" align="right">
			<a href="${ctx}/reportuser/report/fuellogbilling/export.do?type=pdf" target="reportData"><img src="${ctx}/images/pdf.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/reportuser/report/fuellogbillng/export.do?type=xls" target="reportData"><img src="${ctx}/images/excel.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/reportuser/report/fuellogbillng/save.do" target="reportData"><img src="${ctx}/images/save.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/reportuser/report/fuellogbillng/export.do?type=print" target="reportData"><img src="${ctx}/images/print.png" border="0" class="toolbarButton"/></a>
		</td>
	</tr>
	<tr><td align="${left}" width="100%" valign="top"><iframe src="${ctx}/trans/blank.jsp" width="100%" height="600" name="reportData" frameborder="0"></iframe></td></tr>
  </table> --%>


