<%@include file="/common/taglibs.jsp"%>
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

<form:form action="search.do" method="post" name="searchForm">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5" align="left">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Diver Mobile Entry" /> </b>
			</td>
		</tr>		
		
		<tr>
		<td align="${left}" class="first"><primo:label code="Employee Company"/></td>
			<td align="${left}"><select id="driverCompany" name="employeeCompanyName" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${companies}" var="item">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['employeeCompanyName'] == item.name}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.name}"${selected}>${item.name}</option>
					</c:forEach>
			</select>
			</td>
			
			
			
			<td align="${left}" class="first"><primo:label code="Terminal"/></td>
			<td align="${left}"><select id="terminalId" name="employeeTerminalName" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${terminals}" var="item">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['employeeTerminalName'] == item.name}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.name}"${selected}>${item.name}</option>
					</c:forEach>
			</select></td>
		
		</tr>
		
		
		
		<tr>
			<td align="${left}" class="first"><primo:label code="Driver"/></td>
			<td align="${left}"><select id="driverId" name="employeeName" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${drivers}" var="item">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['employeeName'] == item.fullName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.fullName}"${selected}>${item.fullName}</option>
					</c:forEach>
			</select></td>
			
		<td align="${left}" class="first"><primo:label code="Entry Status" /></td>
			<td align="${left}"><select id="entryStatusID" name="entryStatus" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					
						<c:set var="selected" value="" />
						<c:set var="nselected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['entryStatus'] == 'entered'}">
							<c:set var="selected" value="selected" />
						</c:if>
						<c:if
							test="${sessionScope.searchCriteria.searchMap['entryStatus'] == 'notentered'}">
							<c:set var="nselected" value="selected" />
						</c:if>
						<option value="entered" ${selected}>Entered</option>
						<option value="notentered" ${nselected}>Not Entered</option>
					
			</select></td>		 
			
		</tr>              
         
        <tr>
		<td align="${left}" class="first"><primo:label code="Entry Date From" />
			</td>
			<td align="${left}"><input id="datepicker" name="entryDateFrom" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate();"
				value="${sessionScope.searchCriteria.searchMap.entryDateFrom}"/>
			</td>
			
			<td align="${left}" class="first"><primo:label code="Entry Date To" />
			</td>
			<td align="${left}"><input id="datepicker1" name="entryDateTo" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate1();"
				value="${sessionScope.searchCriteria.searchMap.entryDateTo}"/>
			</td>
		
		</tr>
		
		<tr>
		
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="javascript:document.forms['searchForm'].submit();"
				value="<primo:label code="Search"/>" />
				&nbsp;&nbsp;&nbsp;
				
			</td>
			
			
		</tr>
	</table>
</form:form>

<table width="100%">
	<tr>
		<td align="right" colspan="3">	
<a href="/trans/admin/drivermobileentry/exportdata.do?type=pdf"><img src="/trans/images/pdf.png" border="0" class="toolbarButton"></a>
<a href="/trans/admin/drivermobileentry/exportdata.do?type=xls"><img src="/trans/images/excel.png" border="0" class="toolbarButton"></a>
<a href="/trans/admin/drivermobileentry/exportdata.do?type=csv"><img src="/trans/images/csv.png" border="0" class="toolbarButton"></a>
</td>
</tr>
</table>

<div style="width: 100%; margin: 0px auto;">
<form:form action="delete.do" id="deleteForm" name="deleteForm">
	<primo:datatable urlContext="admin/drivermobileentry" deletable="false"
		editable="flase" exportPdf="true" exportXls="true"
		exportCsv="true" insertable="false" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" searcheable="false">
		<br/>					
		<primo:textcolumn headerText="Company" dataField="employeeCompanyName" />
		<primo:textcolumn headerText="Terminal" dataField="employeeTerminalName" />
		<primo:textcolumn headerText="Driver" dataField="employeeName" />
		<primo:textcolumn headerText="Date" dataField="entryDate" dataFormat="MM-dd-yyyy" />	
		<primo:textcolumn HAlign="center" headerText="Trip Sheet" dataField="tripsheet_flag"/>	
		<primo:textcolumn HAlign="center" headerText="Fuel Log" dataField="fuellog_flag"/>		
		<primo:textcolumn HAlign="center" headerText="Odometer" dataField="odometer_flag"/>	
		<primo:textcolumn headerText="Entered By" dataField="enteredBy" />
	</primo:datatable>
	<% session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
</form:form>
</div>