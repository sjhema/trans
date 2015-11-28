<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function searchReport()
{
	
	document.forms["fuelAverageSearchForm"].action='${ctx}/reportuser/report/fuellogaverage/search.do';
	
	var noSubmitError = true;
	var company = document.getElementById("companyId").value;
	var datefrom = document.getElementById("datepicker").value;
	var dateto = document.getElementById("datepicker1").value;
	
	if(company==''){
		alert("Please select company");
		noSubmitError = false;
	}
	else if(datefrom==''){
		alert("Please enter From date");
		noSubmitError = false;
	}
	else if(dateto==''){
		alert("Please enter To date");
		noSubmitError = false;
	}	
	
	if(noSubmitError){
		document.forms["fuelAverageSearchForm"].submit();
	}
	
	
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


</script>
<br/>
<%-- <h3><primo:label code="FuelLog Report" /></h3> --%>
<form:form action="search.do" name="fuelAverageSearchForm" method="post" commandName="modelObject">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Fuel Average Report" />
			</b></td>
	    </tr>
	    
		 
		
		
		 <tr>	        
	        <td class="form-left"><primo:label code="Date From" /><span class="errorMessage">*</span></td>
			<td  align="${left}">
				<form:input id="datepicker" style="min-width:180px; max-width:180px" path="transactionDateFrom" cssClass="flat"  onblur="return formatDate();"/> 
			</td>
				
				 <td class="form-left"><primo:label code="Date To" /><span class="errorMessage">*</span></td>
				<td align="${left}">
				<form:input id="datepicker1" style="min-width:180px; max-width:180px" path="transactionDateTo" cssClass="flat"  onblur="return formatDate1();"/>
				
			</td>
		  
		</tr>
		
		
		<tr>
		    <td class="form-left"><primo:label code="Company" /><span class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" id="companyId" path="company" style="min-width:184px; max-width:184px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td>
		   
		     <td class="form-left"><primo:label code="Unit#" /><span
				class="errorMessage"></span></td>
			 <td><form:select cssClass="flat" path="unit" style="min-width:184px; max-width:184px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${trucks}" itemValue="unit" itemLabel="unit" />
				</form:select>
			 </td>
	   </tr>
			
	   
		 
		
		<tr><td colspan="2"></td></tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3"><input type="button"
				onclick="javascript:searchReport()" value="Preview" /></td>
		</tr>
		
		
	</table>
</form:form>
 

