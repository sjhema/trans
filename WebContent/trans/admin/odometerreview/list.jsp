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
			<td colspan="4"><b><primo:label code="Search Odometer Reading" /> </b>
			</td>
		</tr>		
		
		<tr>
		<td align="${left}" class="first"><primo:label code="Employee Company"/></td>
			<td align="${left}"><select id="driverCompany" name="driverCompany.name" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${companies}" var="item">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['driverCompany.name'] == item.name}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.name}"${selected}>${item.name}</option>
					</c:forEach>
			</select>
			</td>
			
			
			
			<td align="${left}" class="first"><primo:label code="Terminal"/></td>
			<td align="${left}"><select id="terminalId" name="terminal.name" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${terminals}" var="item">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['terminal.name'] == item.name}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.name}"${selected}>${item.name}</option>
					</c:forEach>
			</select></td>
		
		</tr>
		
		
		
		<tr>
			<td align="${left}" class="first"><primo:label code="Driver"/></td>
			<td align="${left}"><select id="driverId" name="driver.fullName" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${drivers}" var="item">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['driver.fullName'] == item.fullName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.fullName}"${selected}>${item.fullName}</option>
					</c:forEach>
			</select></td>
			
			
			<td align="${left}" class="first"><primo:label code="Truck#"/></td>
			<td align="${left}"><select id="driverId" name="truck.unit" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${trucks}" var="item">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['truck.unit'] == item.unit}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.unit}"${selected}>${item.unit}</option>
					</c:forEach>
			</select></td>
			
		</tr>       
		
		<tr> 		
			<td align="${left}" class="first"><primo:label code="Date From" />
			</td>
			<td align="${left}"><input id="datepicker" name="dateFrom" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate();"
				value="${sessionScope.searchCriteria.searchMap.dateFrom}"/>
			</td>
		
		
         <td align="${left}" class="first"><primo:label code="Date To" />
			</td>
			<td align="${left}"><input id="datepicker1" name="dateTo" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate1();"
				value="${sessionScope.searchCriteria.searchMap.dateTo}"/>
			</td>
         </tr>   
         
         <tr>	 
			<td align="${left}" class="first"><primo:label code="Start Miles From" /></td>
			<td align="${left}"><input name="startMilesFrom" type="text" style="min-width:150px; max-width:150px"
				value="${sessionScope.searchCriteria.searchMap.startMilesFrom}" />
			</td>
			
			 <td align="${left}" class="first"><primo:label code="Start Miles To" /></td>
			<td align="${left}"><input name="startMilesTo" type="text" style="min-width:150px; max-width:150px"
				value="${sessionScope.searchCriteria.searchMap.startMilesTo}" />
			</td> 
		
		</tr>     
		
		  <tr>	 
			<td align="${left}" class="first"><primo:label code="End Miles From" /></td>
			<td align="${left}"><input name="endMilesFrom" type="text" style="min-width:150px; max-width:150px"
				value="${sessionScope.searchCriteria.searchMap.endMilesFrom}" />
			</td>
			
			 <td align="${left}" class="first"><primo:label code="End Miles To" /></td>
			<td align="${left}"><input name="endMilesTo" type="text" style="min-width:150px; max-width:150px"
				value="${sessionScope.searchCriteria.searchMap.endMilesTo}" />
			</td> 
		
		</tr> 
		
		
		  <tr>	 
			<td align="${left}" class="first"><primo:label code="Miles From" /></td>
			<td align="${left}"><input name="milesFrom" type="text" style="min-width:150px; max-width:150px"
				value="${sessionScope.searchCriteria.searchMap.milesFrom}" />
			</td>
			
			 <td align="${left}" class="first"><primo:label code="Miles To" /></td>
			<td align="${left}"><input name="milesTo" type="text" style="min-width:150px; max-width:150px"
				value="${sessionScope.searchCriteria.searchMap.milesTo}" />
			</td> 
		
		</tr> 
               

		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="javascript:document.forms['searchForm'].submit();"
				value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<table width="100%">
	<tr>
		<td align="right" colspan="3">	
<a href="/trans/admin/odometerreview/exportdata.do?type=pdf"><img src="/trans/images/pdf.png" border="0" class="toolbarButton"></a>
<a href="/trans/admin/odometerreview/exportdata.do?type=xls"><img src="/trans/images/excel.png" border="0" class="toolbarButton"></a>
<a href="/trans/admin/odometerreview/exportdata.do?type=csv"><img src="/trans/images/csv.png" border="0" class="toolbarButton"></a>

</td>
</tr>
</table>


<div style="width: 100%; margin: 0px auto;">
<form:form action="delete.do" id="deleteForm" name="deleteForm">
	<primo:datatable urlContext="admin/odometerreview" deletable="true"
		editable="true" exportPdf="false" exportXls="false"
		exportCsv="false" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" searcheable="false" >
		<br/>
		<primo:textcolumn headerText="Company" dataField="driverCompany.name" />
		<primo:textcolumn headerText="Terminal" dataField="terminal.name" />
		<primo:textcolumn headerText="Driver" dataField="driver.fullName" />
		<primo:textcolumn headerText="Truck#" dataField="tempTruck"/>
		<primo:textcolumn headerText="Date" dataField="recordDate" dataFormat="MM-dd-yyyy" />	
		<primo:textcolumn headerText="Start Miles" dataField="startReading"/>
		<primo:textcolumn headerText="End Miles" dataField="endReading"/>
		<primo:textcolumn headerText="Miles" dataField="miles"/>
	</primo:datatable>
	<% session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
</form:form>
</div>