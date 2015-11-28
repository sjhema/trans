<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function searchReport()
{
	/* document.forms[0].target="reportData"; */
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


</script>
<br/>
<form:form action="search.do" name="searchForm" method="post" commandName="modelObject">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Fuel Log Verification Report" />
			</b></td>
	    </tr>
	    
		 <tr>
		    <td class="form-left"><primo:label code="Driver Company" /><span class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="company">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td>
		   
		    <td class="form-left"><primo:label code="Terminal" /><span class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="terminal" >
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><primo:label code="Driver" /><span class="errorMessage"></span></td>
			  <td><form:select cssClass="flat" path="driver" id="driverId" >
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${drivers}" itemValue="fullName" itemLabel="fullName"></form:options>
				</form:select> <br> <form:errors path="driver" cssClass="errorMessage" />
			</td>
	   </tr>
		
		 <tr>
	        <td class="form-left"><primo:label code="Transaction Date From" /><span class="errorMessage"></span></td>
			<td  align="${left}">
				<form:input size="10" path="transactionDateFrom" cssClass="flat"  onblur="return formatDate1('transactionDateFrom');"/> 
				<script type="text/javascript">
					$(function() {
					$("#transactionDateFrom").datepicker({
						dateFormat:'mm-dd-yy',
		            	changeMonth: true,
		    			changeYear: true
		    		});
					});
				</script>
			<td class="form-left"><primo:label code="Transaction Date To" /><span class="errorMessage"></span></td>
			<td  align="${left}">
			<form:input size="10" path="transactionDateTo" cssClass="flat"  onblur="return formatDate1('transactionDateTo');"/>
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
		
		
		
			
	 
	   
	   
		 
		
		<tr><td colspan="2"></td></tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3"><input type="button"
				onclick="javascript:searchReport()" value="Preview" /></td>
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


