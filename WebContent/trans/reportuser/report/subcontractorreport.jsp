<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function searchReport() {
	document.forms[0].submit();
}

function searchReport1() {
	document.getElementById('sum').value='true' ;
	document.forms[0].submit(); 
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
	<primo:label code="Subcontractor Report " />
</h3>
<form:form action="search.do" name="searchForm" method="post" commandName="modelObject">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Subcontractor Report" />
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
		</tr>
		<tr>
			
		    <td class="form-left"><primo:label code="Voucher Status" /><span
				class="errorMessage"></span></td>
			<td><form:select path="voucherStatus" multiple="true">
					<form:options items="${voucherStatus}" itemValue="dataValue"
						itemLabel="dataText" />
				</form:select> <br> <form:errors path="voucherStatus" cssClass="errorMessage" /></td>
		    
		
			<td class="form-left"><primo:label code="Terminals" /><span
				class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="terminal" multiple="true">
					<form:options items="${terminals}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" /></td>
			
		</tr>
	
		<tr>
		
			<td class="form-left"><primo:label code="Sub Contractor" /><span
				class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="subcontractor" multiple="true">
					<form:options items="${subcontractors}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <form:errors path="subcontractor" cssClass="errorMessage" /></td>
		
			<td class="form-left"><primo:label code="Companies" /><span
				class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="company" multiple="true">
					<form:options items="${companies}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" /></td>
				
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
			<td class="form-left" colspan="2">&nbsp;</td>
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
		
			<td class="form-left"><primo:label code="Total Amount" /><span
				class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="totAmtFrom" cssClass="flat"/> To:<form:input size="10" path="totAmtTo" cssClass="flat"/>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Voucher Date" /><span
				class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="fromVoucherDate" cssClass="flat"  onblur="return formatDate('fromIVoucherDate');"/> 
				<script type="text/javascript">
					$(function() {
					$("#fromVoucherDate").datepicker({
						dateFormat:'mm-dd-yy',
		            	changeMonth: true,
		    			changeYear: true
		    		});
					});
				</script>
				To:<form:input size="10" path="voucherDateTo" cssClass="flat"  onblur="return formatDate('voucherDateTo');"/>
				<script type="text/javascript">
					$(function() {
					$("#voucherDateTo").datepicker({
						dateFormat:'mm-dd-yy',
		            	changeMonth: true,
		    			changeYear: true
		    		});
					});
				</script>
			</td>
			<td class="form-left"><primo:label code="Voucher Number" /><span
				class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="voucherNumberFrom" cssClass="flat"/> To:<form:input size="10" path="voucherNumberTo" cssClass="flat"/>
               <input type="hidden" name="summary" id="sum">
			</td>
		</tr>
		<tr>
			
			<td class="form-left"><primo:label code="Miscellaneous Charges" /><span
				class="errorMessage"></span> </td>
				<td  align="${left}">
				<form:input size="10" path="miscelleneousCharges" cssClass="flat"/>
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
			onclick="javascript:searchReport1()" value="Summary" />
				</td>
			
		</tr>
	</table>
</form:form>

