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


function formatDate2(){
	var date=document.getElementById("datepicker2").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker2").value="";
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
						document.getElementById("datepicker2").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker2").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker2").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker2").value=date;
		}
	 }
   }
}


function formatDate3(){
	var date=document.getElementById("datepicker3").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker3").value="";
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
						document.getElementById("datepicker3").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker3").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker3").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker3").value=date;
		}
	 }
   }
}

function formatDate4(){
	var date=document.getElementById("datepicker4").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker4").value="";
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
						document.getElementById("datepicker4").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker4").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker4").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker4").value=date;
		}
	 }
   }
}

function formatDate5(){
	var date=document.getElementById("datepicker5").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker5").value="";
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
						document.getElementById("datepicker5").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker5").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker5").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker5").value=date;
		}
	 }
   }
}
</script>

<form:form action="search.do" method="post" name="searchForm">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5" align="left">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Trip Sheet" /> </b>
			</td>
		</tr>		
		
		<tr>
		<td align="${left}" class="first"><primo:label code="Batch Date From" />
			</td>
			<td align="${left}"><input id="datepicker" name="batchDateFrom" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate();"
				value="${sessionScope.searchCriteria.searchMap.batchDateFrom}"/>
			</td>
			
			<td align="${left}" class="first"><primo:label code="Batch Date To" />
			</td>
			<td align="${left}"><input id="datepicker1" name="batchDateTo" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate1();"
				value="${sessionScope.searchCriteria.searchMap.batchDateTo}"/>
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
			
		<td align="${left}" class="first"><primo:label code="Verification Status" /></td>
			<td align="${left}"><select id="veirifcationID" name="verificationStatus" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					
						<c:set var="selected" value="" />
						<c:set var="nselected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['verificationStatus'] == 'Verified'}">
							<c:set var="selected" value="selected" />
						</c:if>
						<c:if
							test="${sessionScope.searchCriteria.searchMap['verificationStatus'] == 'null'}">
							<c:set var="nselected" value="selected" />
						</c:if>
						<option value="null" ${nselected}>Not Verified</option>
						<option value="Verified" ${selected}>Verified</option>
					
			</select></td>		 
			
		</tr>              
         
         <tr>  		
			
			<td align="${left}" class="first"><primo:label code="Truck#"/></td>
			<td align="${left}"><select id="truckId" name="truck.unit" style="min-width:154px; max-width:154px">
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
			
			<td align="${left}" class="first"><primo:label code="Trailer#"/></td>
			<td align="${left}"><select id="trailerId" name="trailer.id" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${trailers}" var="item">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['trailer.id'] == item.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.id}"${selected}>${item.unit}</option>
					</c:forEach>
			</select></td>
			
			
         </tr>   
         
         <tr>
         	<td align="${left}" class="first"><primo:label code="Load Date From" />
			</td>
			<td align="${left}"><input id="datepicker2" name="lDateFrom" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate2();"
				value="${sessionScope.searchCriteria.searchMap.lDate}"/>
			</td>
			
			<td align="${left}" class="first"><primo:label code="Load Date To" />
			</td>
			<td align="${left}"><input id="datepicker3" name="lDateTo" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate3();"
				value="${sessionScope.searchCriteria.searchMap.lDate}"/>
			</td>
			
			</tr>
			
			<tr>
         
            <td align="${left}" class="first"><primo:label code="Unload Date From" />
			</td>
			<td align="${left}"><input id="datepicker4" name="ulDateFrom" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate4();"
				value="${sessionScope.searchCriteria.searchMap.ulDate}"/>
			</td>
			
			<td align="${left}" class="first"><primo:label code="Unload Date To" />
			</td>
			<td align="${left}"><input id="datepicker5" name="ulDateTo" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate5();"
				value="${sessionScope.searchCriteria.searchMap.ulDate}"/>
			</td>
         
         </tr>
         
         <tr>
			<td align="${left}" class="first"><primo:label code="Origin"/></td>
			<td align="${left}"><select id="origin" name="origin.id" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${origins}" var="origin">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['origin.id'] == origin.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${origin.id}"${selected}>${origin.name}</option>
					</c:forEach>
			</select></td>
			<td align="${left}" class="first"><primo:label code="Destination" /></td>
			<td align="${left}"><select id="destination" name="destination.id" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${destinations}" var="destination">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['destination.id'] == destination.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${destination.id}"${selected}>${destination.name}</option>
					</c:forEach>
			</select></td>
		</tr>
		
		<tr>	 
			<td align="${left}" class="first"><primo:label code="Origin Ticket#" /></td>
			<td align="${left}"><input name="originTicket" type="text" style="min-width:150px; max-width:150px"
				value="${sessionScope.searchCriteria.searchMap.originTicket}" />
			</td>
			
			 <td align="${left}" class="first"><primo:label code="Destination Ticket#" /></td>
			<td align="${left}"><input name="destinationTicket" type="text" style="min-width:150px; max-width:150px"
				value="${sessionScope.searchCriteria.searchMap.destinationTicket}" />
			</td> 
		
		</tr> 
		
		<tr>
		
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="javascript:document.forms['searchForm'].submit();"
				value="<primo:label code="Search"/>" />
				&nbsp;&nbsp;&nbsp;
				<input type="button"
				onclick="javascript:location.href = '${ctx}/admin/tripsheetticketverification/create.do';"
				value="<primo:label code="Ticket Verify"/>" />
			</td>
			
			
		</tr>
	</table>
</form:form>

<table width="100%">
	<tr>
		<td align="right" colspan="3">	
<a href="/trans/admin/tripsheetreview/exportdata.do?type=pdf"><img src="/trans/images/pdf.png" border="0" class="toolbarButton"></a>
<a href="/trans/admin/tripsheetreview/exportdata.do?type=xls"><img src="/trans/images/excel.png" border="0" class="toolbarButton"></a>
<a href="/trans/admin/tripsheetreview/exportdata.do?type=csv"><img src="/trans/images/csv.png" border="0" class="toolbarButton"></a>
<a href="/trans/admin/tripsheetreview/exportdata.do?type=pdf&audit=true">
	<img src="/trans/images/bluepdf.png" border="0" class="toolbarButton"></a>
</td>
</tr>
</table>



<div style="width: 100%; margin: 0px auto;">
<form:form action="delete.do" id="deleteForm" name="deleteForm">
	<primo:datatable urlContext="admin/tripsheetreview" deletable="true"
		editable="true" exportPdf="false" exportXls="false"
		exportCsv="false" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" searcheable="false">
		<br/>
		<primo:textcolumn headerText="Batch Date" dataField="batchDate" dataFormat="MM-dd-yyyy" />
		<primo:textcolumn headerText="Company" dataField="driverCompany.name" />
		<primo:textcolumn headerText="Terminal" dataField="terminal.name" />
		<primo:textcolumn headerText="Driver" dataField="driver.fullName" />
		<primo:textcolumn headerText="Truck#" dataField="tempTruck"/>
		<primo:textcolumn headerText="Trailer#" dataField="tempTrailer"/>
		<primo:textcolumn headerText="Load Date" dataField="loadDate" dataFormat="MM-dd-yyyy" />	
		<primo:textcolumn headerText="Origin" dataField="origin.name"/>
		<primo:textcolumn headerText="Org. Ticket#" dataField="originTicket"/>
		<primo:textcolumn headerText="Unload Date" dataField="unloadDate" dataFormat="MM-dd-yyyy" />	
		<primo:textcolumn headerText="Destination" dataField="destination.name"/>
		<primo:textcolumn headerText="Dest. Ticket#" dataField="destinationTicket"/>
		<primo:textcolumn headerText="Amount" dataField="payRate"/>
		<primo:textcolumn headerText="Status" dataField="verificationStatus"/>		
		<primo:anchorcolumn headerText="Verify" dataField="verificationStatus" linkUrl="/admin/tripsheetticketverification/create.do?tripsheetId={id}" linkText="Verify"/>
		
	</primo:datatable>
	<% session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
</form:form>
</div>