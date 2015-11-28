<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">


function searchReport() {
	var d1 = document.getElementById('datepicker').value;
	
	if (d1!=null && d1!='' && !isValidDate(d1)) {
		alert("Invalid billBatch date");
	}
	var d6 = document.getElementById('datepicker1').value;
	if (d6!=null && d6!='' && !isValidDate(d6)) {
		alert("Invalid billBatch date");
	}
	
	var e;
	
	if(d1==""&&e!="yes"){
		alert("please select From Date");
		e=yes;
	}
	if(d6==""&&e!="yes"){
		alert("please select To Date");
		e=yes;
	}
	
	if(d1!=""&&d6!=""){	
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

</script>
<br/>
<h3>Auto Update Driver Inspection</h3>

<form:form action="autoupdatedriverinspection.do" method="POST"
	name="searchForm">
	<table id="form-table" width="75%" cellspacing="0" cellpadding="5" align="left">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Update Driver Inspection"/></b></td>
		</tr>
		
	<tr>
			<td align="${left}" class="first"><primo:label code="Employee Company"/></td>
				<td align="${left}"><select id="company" name="company.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${companies}" var="company">
					<c:set var="selected" value=""/>
					
						<option value="${company.id}" ${selected}>${company.name}</option>
					</c:forEach>
			</select></td>
			<td align="${left}" class="first"><primo:label code="Terminal"/></td>
			<td align="${left}"><select id="terminal" name="terminal.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${terminals}" var="terminal">
					<c:set var="selected" value=""/>
					
						<option value="${terminal.id}" ${selected}>${terminal.name}</option>
				</c:forEach>
			</select></td>
			
	   </tr>	
		
		
		
	<tr>			
			<td align="${left}" class="first"><primo:label code="Date From" />
			</td>
			<td align="${left}"><input id="datepicker" name="fromDate" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate();"
				value=""/>
			</td>
				
			
			<td align="${left}" class="first"><primo:label code="Date To" />
			</td>
			<td align="${left}"><input id="datepicker1" name="toDate" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate();"
				value=""/>
			</td>
					
		</tr>
		
		<tr>
		<td></td>
	<td colspan="2">
	<input type="button"
				onclick="javascript:searchReport()" value="Update"/>
	<input type="reset" value="Reset"/>
	</td>
	</tr>
	</table>
	
</form:form>
