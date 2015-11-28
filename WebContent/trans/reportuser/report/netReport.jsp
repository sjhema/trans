<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function validateInvoice(){
	var ud1=document.getElementById("unloadedFrom").value;
	var ud2=document.getElementById("unloadedTo").value;
	
	if(ud1==""&&ud2==""){
		alert("Please Enter Date Range");
		return false;
	}
	
	if(ud1==""&&ud2!=""){
		alert("Please Enter From Date");
		return false;
	}
	if(ud1!=""&&ud2==""){
		alert("Please Enter To Date");
		return false;
	}
	
	return true;
}
function searchReport(obj){
	//alert(obj);
	var id = obj.id;
	var c;
    if(id=="Truckac") {
		document.forms[0].action="../netreport/search.do?Truck=Truckac";
		 c=validateInvoice();
		 if(c) 
		 document.forms[0].submit();
	}else if(id=="Truckcc") {
		document.forms[0].action="../netreport/search.do?Truck=Truckcc";
		c=validateInvoice();
		 if(c)
		document.forms[0].submit();
	}else if(id=="Truckca") {
		document.forms[0].action="../netreport/search.do?Truck=Truckca";
		c=validateInvoice();
		 if(c)
		document.forms[0].submit();
	}else if(id=="Trailerac") {
		document.forms[0].action="../netreport/search.do?Trailer=Trailerac";
		c=validateInvoice();
		 if(c)
		document.forms[0].submit();
	}else if(id=="Trailercc") {
		document.forms[0].action="../netreport/search.do?Trailer=Trailercc";
		c=validateInvoice();
		 if(c)
		document.forms[0].submit();
	}else if(id=="Trailerca") {
		document.forms[0].action="../netreport/search.do?Trailer=Trailerca";
		c=validateInvoice();
		 if(c)
		document.forms[0].submit();
	}
    else if(id=="Terminalac") { 
		document.forms[0].action="../netreport/search.do?Terminal=Terminalac";
		c=validateInvoice();
		 if(c)
		document.forms[0].submit();
	}else if(id=="Terminalcc") { 
		document.forms[0].action="../netreport/search.do?Terminal=Terminalcc";
		c=validateInvoice();
		 if(c)
		document.forms[0].submit();
	}else if(id=="Terminalca") { 
		document.forms[0].action="../netreport/search.do?Terminal=Terminalca";
		c=validateInvoice();
		 if(c)
		document.forms[0].submit();
	} 
    else if(id=="Driverac"){
		document.forms[0].action="../netreport/search.do?Driver=Driverac";
		c=validateInvoice();
		 if(c)
	    document.forms[0].submit();
	}else if(id=="Drivercc"){
		document.forms[0].action="../netreport/search.do?Driver=Drivercc";
		c=validateInvoice();
		 if(c)
	    document.forms[0].submit();
	}else if(id=="Driverca"){
		document.forms[0].action="../netreport/search.do?Driver=Driverca";
		c=validateInvoice();
		 if(c)
	    document.forms[0].submit();
	} 
	else if(id=="Company"){
		document.forms[0].action="../netreport/search.do?Company=Company";
		c=validateInvoice();
		 if(c)
	    document.forms[0].submit();
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
<form:form action="search" name="searchForm" method="post" commandName="modelObject">
	<table id="form-table" width="850px" cellspacing="1" cellpadding="5" >
		<tr class="table-heading">
			<td colspan="4" align="center"><b><primo:label code="Net Report" />
			</b></td>
	    </tr>
	    <tr>
	        <td class="form-left"><primo:label code="Date Range" /><span class="errorMessage"></span></td>
			<td  align="${left}">
				From:<form:input size="10" path="unloadedFrom" cssClass="flat"  onblur="return formatDate1('unloadedFrom');"/> 
				<script type="text/javascript">
					$(function() {
					$("#unloadedFrom").datepicker({
						dateFormat:'mm-dd-yy',
		            	changeMonth: true,
		    			changeYear: true
		    		});
					});
				</script>
				To:<form:input size="10" path="unloadedTo" cssClass="flat"  onblur="return formatDate1('unloadedTo');"/>
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
			</tr>
			<tr>
			<td></td>
			</tr>
	<tr class="table-heading">
	<td colspan="4">Company Report</td>
	</tr>
			
		<tr>
	          <td class="form-left"><primo:label code="Company" /><span class="errorMessage"></span></td>
			  <td><form:select cssClass="flat" path="company" id="id" multiple="true" >
					<%-- <form:option value="-1">------<primo:label
							code="Please Select" />------</form:option> --%>
					<form:options items="${companies}" itemValue="id" itemLabel="name"></form:options>
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td>
		</tr>
		
		
		
			<tr>
				<td align="${left}" class="form-left"></td>
			<td align="${left}" >
			<input type="button"
				onclick="javascript:searchReport(this)" value="Company Report" id="Company"/>			
				</td>
		</tr>
		
		
		<tr><td></td></tr>
		<tr class="table-heading">
	<td colspan="4">Driver Report</td>
	</tr>
			
		
		<tr>
		<td class="form-left"><primo:label code="Company" /><span class="errorMessage"></span></td>
			  <td><form:select cssClass="flat" path="driverCompany" id="drvid" multiple="true" >
					<%-- <form:option value="-1">------<primo:label
							code="Please Select" />------</form:option> --%>
					<form:options items="${companies}" itemValue="id" itemLabel="name"></form:options>
				</form:select> <br> <form:errors path="driverCompany" cssClass="errorMessage" />
			</td>
	          <td class="form-left"><primo:label code="Driver" /><span class="errorMessage"></span></td>
			  <td><form:select cssClass="flat" path="driver" id="driverId" multiple="true">
					<%-- <form:option value="-1">------<primo:label
							code="Please Select" />------</form:option> --%>
					<form:options items="${drivers}" itemValue="fullName" itemLabel="fullName"></form:options>
				</form:select> <br> <form:errors path="driver" cssClass="errorMessage" />
			</td>
		</tr>
		
			<tr>
			<td align="${left}" class="form-left"></td>
			<td align="${left}"><input type="button"
				onclick="javascript:searchReport(this)" value="Driver Report" id="Driverac"/>
				</td>
		</tr>
		
		
		<tr><td></td></tr>
		<tr class="table-heading">
	<td colspan="4">Truck Report</td>
	</tr>
		<tr>
		<td class="form-left"><primo:label code="Company" /><span class="errorMessage"></span></td>
			  <td><form:select cssClass="flat" path="truckCompany" id="trkid" multiple="true" >
					<%-- <form:option value="-1">------<primo:label
							code="Please Select" />------</form:option> --%>
					<form:options items="${companies}" itemValue="id" itemLabel="name"></form:options>
				</form:select> <br> <form:errors path="truckCompany" cssClass="errorMessage" />
			</td>
		 <td class="form-left"><primo:label code="Trucks" /><span
				class="errorMessage"></span></td>
			 <td><form:select cssClass="flat" path="unit" multiple="true">
					<%-- <form:option value="-1">------<primo:label
							code="Please Select" />------</form:option> --%>
					<form:options items="${trucks}" itemValue="unit" itemLabel="unit" />
				</form:select>
			 </td>
	   </tr>
	   
	   <tr>
	   <td align="${left}" class="form-left"></td>
		<td align="${left}">
		        <input type="button"
				onclick="javascript:searchReport(this)" value=" Truck Report" id="Truckac" />
				
		</td>
	   </tr>
	   <tr><td></td></tr>
		<tr class="table-heading">
	<td colspan="4">Trailer Report</td>
	</tr>
	   <tr>
	   <td class="form-left"><primo:label code="Company" /><span class="errorMessage"></span></td>
			  <td><form:select cssClass="flat" path="trailerCompany" id="traid" multiple="true" >
					<%-- <form:option value="-1">------<primo:label
							code="Please Select" />------</form:option> --%>
					<form:options items="${companies}" itemValue="id" itemLabel="name"></form:options>
				</form:select> <br> <form:errors path="trailerCompany" cssClass="errorMessage" />
		</td>
	   <td class="form-left"><primo:label code="Trailer"/></td>
	   <td><form:select cssClass="flat" path="trailer" multiple="true">
	   <%-- <form:option value="-1">------<primo:label
							code="Please Select" />------</form:option> --%>
					<form:options items="${trailers}" itemValue="unit" itemLabel="unit" />
	   </form:select></td>
	   </tr>
		
		<tr>
		<td align="${left}" class="form-left"></td>
		<td align="${left}">
		        <input type="button"
				onclick="javascript:searchReport(this)" value="Trailer Report" id="Trailerac" />				
		</td>
		</tr>
	
			
		<%-- <tr><td>
		<table>
          <tr>
			<td align="${left}"></td>
			<td align="${left}" ><input type="button"
				onclick="javascript:searchReport(this)" value="Driver" id="Driver"/></td>	
			<td align="${left}" ><input type="button"
				onclick="javascript:searchReport(this)" value="Terminal" id="Terminal"/></td>
			<td align="${left}" ><input type="button"
				onclick="javascript:searchReport(this)" value="Truck" id="Truck" /></td>
				
					
				
				
		</tr></table>
		</td></tr> --%>

		
		
	</table>
</form:form>

 


