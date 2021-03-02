<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function searchReport() {
	document.forms[0].submit();
}
function searchReport1() {
	document.getElementById('sum').value='true' ;
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
<h3>
	<primo:label code="Billing History" />
</h3>
<form:form action="search.do" name="searchForm" method="post" commandName="modelObject">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Billing" />
			</b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><label>Bill Batch Date</label></td>
			<td align="${left}">From:<form:input size="10" path="batchDateFrom" onblur="javascript:formatReportDate('batchDate');"/>
				<script type="text/javascript">
			$(function() {
			$("#batchDateFrom").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script>
		    &nbsp;
		    To:<form:input size="10" path="batchDateTo" onblur="javascript:formatReportDate('batchDateTo');"/>
				<script type="text/javascript">
			$(function() {
			$("#batchDateTo").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>
		    <td class="form-left"><primo:label code="Process Status" /><span
				class="errorMessage"></span></td>
			<td><form:select path="ticketStatus" multiple="true">
					<form:options items="${ticketStatuss}" itemValue="dataValue"
						itemLabel="dataText" />
				</form:select> <br> <form:errors path="ticketStatus" cssClass="errorMessage" /></td>
		    
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Driver" /><span
				class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="driver" id="driverId" multiple="true" onchange="javascript:getTerminal();">
					<form:options items="${drivers}" itemValue="id" itemLabel="fullName"></form:options>
				</form:select> <br> <form:errors path="driver" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Terminals" /><span
				class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="terminal" multiple="true">
					<form:options items="${terminals}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" /></td>
			
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Truck" /><span
				class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="truck" multiple="true">
					<form:options items="${trucks}" itemValue="unit" itemLabel="unit" />
				</form:select>
			</td>
			<td class="form-left"><primo:label code="Trailer" /><span
				class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="trailer" multiple="true">
					<form:options items="${trailers}" itemValue="unit" itemLabel="unit" />
				</form:select>
			</td>
					</tr>
		<tr>
			<td class="form-left"><primo:label code="Entered By" /></td>
			<td><form:select cssClass="flat" path="createdBy" id="createdBy" multiple="true">
					<c:forEach var="item" items="${operators}">
						<c:set var="selected" value=""/>
						<c:if test="${item.id eq modelObject.createdBy}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${item.id}" ${selected}>${item.username}</option>
					</c:forEach>
				</form:select> <br> <form:errors path="createdBy" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Sub Contractor" /><span
				class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="subcontractor" multiple="true">
					<form:options items="${subcontractors}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <form:errors path="subcontractor" cssClass="errorMessage" /></td>
			
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Billing Companies" /><span
				class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="company" multiple="true">
					<form:options items="${companies}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" /></td>
			
			<td class="form-left"><primo:label code="Employee Companies" /><span
				class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="driverCompany" multiple="true">
					<form:options items="${companies}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <form:errors path="driverCompany" cssClass="errorMessage" /></td>	
				
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Not Billable" /></td>
			<td>
				<form:select cssClass="flat" path="notBillable" multiple="true">
					<form:options items="${notBillableOptions}" />
				</form:select> 
				<br> <form:errors path="notBillable" cssClass="errorMessage" />
			</td>
		</tr>
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Origin " />
			</b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Origin" /><span
				class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="origin" multiple="true">
					<form:options items="${origins}" itemValue="id" itemLabel="name" />
				</form:select>
			</td>
			<td class="form-left"><primo:label code="Origin Ticket#" /><span
				class="errorMessage"></span></td>
			<td align="${left}">From:<form:input size="10" path="originTicketFrom"
					cssClass="flat" />&nbsp;To:<form:input size="10" path="originTicketTo"
					cssClass="flat" /><br> <form:errors path="originTicketFrom"
					cssClass="errorMessage" /><form:errors path="originTicketTo"
					cssClass="errorMessage" /></td>	
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Load Date" /><span
				class="errorMessage"></span></td>
			<td align="${left}">From:<form:input size="10" path="loadedFrom"
					cssClass="flat" onblur="return formatDate('loadedFrom');"/>
						<script type="text/javascript">
							$(function() {
							$("#loadedFrom").datepicker({
								dateFormat:'mm-dd-yy',
				            	changeMonth: true,
				    			changeYear: true
				    		});
							});
						</script>
					To:<form:input size="10" path="loadedTo"
					cssClass="flat" onblur="return formatDate('loadedTo');"/>
					<script type="text/javascript">
						$(function() {
						$("#loadedTo").datepicker({
							dateFormat:'mm-dd-yy',
			            	changeMonth: true,
			    			changeYear: true
			    		});
						});
					</script>
					</td>
			<td class="form-left"></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Transfer Time In" /><span
				class="errorMessage"></span></td>
			<td align="${left}">From:<form:input size="10" path="transferTimeInFrom"
					cssClass="flat"  onkeypress="return onlyNumbers(event,false)" onblur="return getTime('transferTimeInFrom')"/>&nbsp;To:<form:input size="10" path="transferTimeInTo"
					cssClass="flat"  onkeypress="return onlyNumbers(event,false)" onblur="return getTime('transferTimeInTo')"/> </td>
			<td class="form-left"><primo:label code="Transfer Time Out" /><span
				class="errorMessage"></span></td>
			<td align="${left}">From:<form:input size="10" path="transferTimeOutFrom"
					cssClass="flat" onkeypress="return onlyNumbers(event,false)" onblur="return getTime('transferTimeOutFrom')"/> To:<form:input size="10" path="transferTimeOutTo"
					cssClass="flat" onkeypress="return onlyNumbers(event,false)" onblur="return getTime('transferTimeOutTo')"/> </td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Transfer Gross" /><span
				class="errorMessage"></span></td>
			<td align="${left}">From:<form:input size="10" path="originGrossWtFrom" 
					cssClass="flat" /> To:<form:input size="10" path="originGrossWtTo"
					cssClass="flat" /> </td>
			<td class="form-left"><primo:label code="Transfer Tare" /><span
				class="errorMessage"></span></td>
			<td align="${left}">From:<form:input size="10" path="originTareWtFrom" cssClass="flat" onblur="javascript:getTransferContents()"/>&nbsp;To:<form:input size="10" path="originTareWtTo" cssClass="flat" onblur="javascript:getTransferContents()"/></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Transfer Tons" /><span
				class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="originTonsWtFrom" cssClass="flat"/>&nbsp;To:<form:input size="10" path="originTonsWtTo" cssClass="flat"/>
			</td>
			<td colspan="2">
		</tr>
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Destination" />
			</b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Destination" /><span
				class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="destination" multiple="true">
					<form:options items="${destinations}" itemValue="id" itemLabel="name" />
				</form:select>
			</td>	
			<td class="form-left"><primo:label code="Destination Ticket#" /><span
				class="errorMessage"></span></td>
			<td align="${left}">From:<form:input size="10" path="destinationTicketFrom"
					cssClass="flat" />&nbsp;To:<form:input size="10" path="destinationTicketTo"
					cssClass="flat" /><br> <form:errors path="destinationTicketFrom"
					cssClass="errorMessage" /><form:errors path="destinationTicketTo"
					cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="UnLoad Date" /><span
				class="errorMessage"></span></td>
			<td align="${left}">From:<form:input size="10" path="unloadedFrom" cssClass="flat" onblur="return formatDate('unloadedFrom');"/>
					<script type="text/javascript">
						$(function() {
						$("#unloadedFrom").datepicker({
							dateFormat:'mm-dd-yy',
			            	changeMonth: true,
			    			changeYear: true
			    		});
						});
					</script>
					To:<form:input size="10" path="unloadedTo" cssClass="flat" onblur="return formatDate('unloadedTo');"/>
					<script type="text/javascript">
						$(function() {
						$("#unloadedTo").datepicker({
							dateFormat:'mm-dd-yy',
			            	changeMonth: true,
			    			changeYear: true
			    		});
						});
					</script>
					</td>
			<td class="form-left"></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Land Fill Time In" /><span
				class="errorMessage"></span></td>
			<td align="${left}">From:<form:input size="10" path="landfillTimeInFrom"
					cssClass="flat" onkeypress="return onlyNumbers(event,false)" onblur="return getTime('landfillTimeInFrom')"/> To:<form:input size="10" path="landfillTimeInTo"
					cssClass="flat" onkeypress="return onlyNumbers(event,false)" onblur="return getTime('landfillTimeInTo')"/><br> <form:errors path="landfillTimeInFrom"
					cssClass="errorMessage" /><form:errors path="landfillTimeInTo"
					cssClass="errorMessage" /></td>
			<td class="form-left"><primo:label code="Land Fill Time Out" /><span
				class="errorMessage"></span></td>
			<td align="${left}">From:<form:input size="10" path="landfillTimeOutFrom"
					cssClass="flat" onkeypress="return onlyNumbers(event,false)" onblur="return getTime('landfillTimeOutFrom')"/>&nbsp;To:<form:input size="10" path="landfillTimeOutTo"
					cssClass="flat" onkeypress="return onlyNumbers(event,false)" onblur="return getTime('landfillTimeOutTo')"/>  <br> <form:errors path="landfillTimeOutFrom"
					cssClass="errorMessage" /><form:errors path="landfillTimeOutTo"
					cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="LandFill Gross" /><span
				class="errorMessage"></span></td>
			<td align="${left}">From:<form:input size="10" path="landfillGrossWtFrom" cssClass="flat" />&nbsp;To:<form:input size="10" path="landfillGrossWtTo" cssClass="flat" /> 
			</td>
			<td class="form-left"><primo:label code="LandFill Tare" /><span
				class="errorMessage"></span></td>
			<td align="${left}">From:<form:input size="10" path="landfillTareWtFrom" cssClass="flat" onblur="javascript:getLandfillContents()"/>&nbsp;To:<form:input size="10" path="landfillTareWtTo"
					cssClass="flat" onblur="javascript:getLandfillContents()"/> </td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="LandFill Tonns" /><span
				class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="landfillTonsWtFrom" cssClass="flat"/> To:<form:input size="10" path="landfillTonsWtTo" cssClass="flat"/>
			</td>
			<!-- <td class="form-left" colspan="2">&nbsp;</td> -->
			<td ></td>
		</tr>
		
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Profit and Cost Analysis" />
			</b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Amount" /><span
				class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="amountFrom" cssClass="flat"/> To:<form:input size="10" path="amountTo" cssClass="flat"/>
			</td>
			<td class="form-left"><primo:label code="Rate" /><span
				class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="rateFrom" cssClass="flat"/> To:<form:input size="10" path="rateTo" cssClass="flat"/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Fuel Surcharge" /><span
				class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="fuelSurchargeFrom" cssClass="flat"/> To:<form:input size="10" path="fuelSurchargeTo" cssClass="flat"/>
			</td>
			<td class="form-left"><primo:label code="Tonnage Premium" /><span
				class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="tonnagePremiumFrom" cssClass="flat"/> To:<form:input size="10" path="tonnagePremiumTo" cssClass="flat"/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Demurrage Charge" /><span
				class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="demurrageChargeFrom" cssClass="flat"/> To:<form:input size="10" path="demurrageChargeTo" cssClass="flat"/>
			</td>
			<td class="form-left"><primo:label code="Total Amount" /><span
				class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="totalAmtFrom" cssClass="flat"/> To:<form:input size="10" path="totalAmtTo" cssClass="flat"/>
			</td>
	   </tr>
	   <tr>
			<td class="form-left"><primo:label code="Sum of Total Amount" /><span
				class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="totAmtFrom" cssClass="flat"/> To:<form:input size="10" path="totAmtTo" cssClass="flat"/>
			</td>
			<!-- <td class="form-left" colspan="2">&nbsp;</td> -->
			
			
			<td class="form-left"><primo:label code="Customers" /><span
				class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="customer" multiple="true" >
				<form:options items="${customers}" itemValue="id" 
				itemLabel="name" />
				</form:select><br> <form:errors path="customer" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Invoice Date" /><span
				class="errorMessage"></span></td>
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
			<td class="form-left"><primo:label code="Invoice Number" /><span
				class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="invoiceNumberFrom" cssClass="flat"/> To:<form:input size="10" path="invoiceNumberTo" cssClass="flat"/>
               <input type="hidden" name="summary" id="sum">
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3"><input type="button"
				onclick="javascript:searchReport()" value="Search" />&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button"
			onclick="javascript:searchReport1()" value="Summary" /></td>
			
			<%-- <td align="${left}" colspan="3"><input type="button"
			onclick="javascript:searchReport1()" value="Summary" /></td> --%>
		</tr>
	</table>
</form:form>

