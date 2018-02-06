<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function editMultipleData() {
	var inputs = document.getElementsByName("id");
	var submitForm = false;
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			submitForm = true;
		}
	}
	if (submitForm) {
		
			document.deleteForm.action = "bulkedit.do";
			document.deleteForm.submit();
		
	} else {
		alert("Please select at least one record");
	}
}

function processRevert() {
	var inputs = document.getElementsByName("id");
	var submitForm = false;
	for (var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			submitForm = true;
		}
	}
	if (!submitForm) {
		alert("Please select at least one record");
		return;
	} 
	
	if (!confirm("Do you want to revert the selected tickets?")) {
		return;
	}
	
	document.deleteForm.action = "bulkedit.do?mode=REVERT";
	document.deleteForm.submit();
}


function massHold() {
	var inputs = document.getElementsByName("id");
	var submitForm = false;
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			submitForm = true;
		}
	}
	if (submitForm) {
		
			document.deleteForm.action = "holdAllTickets.do";
			document.deleteForm.submit();
		
	} else {
		alert("Please select at least one record");
	}
}


function payrollPendingYes() {
	var inputs = document.getElementsByName("id");
	var submitForm = false;
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			submitForm = true;
		}
	}
	if (submitForm) {
		
			document.deleteForm.action = "prPendingYes.do";
			document.deleteForm.submit();
		
	} else {
		alert("Please select at least one record");
	}
}


function payrollPendingNo() {
	var inputs = document.getElementsByName("id");
	var submitForm = false;
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			submitForm = true;
		}
	}
	if (submitForm) {
		
			document.deleteForm.action = "prPendingNo.do";
			document.deleteForm.submit();
		
	} else {
		alert("Please select at least one record");
	}
}


function massUnhold() {
	var inputs = document.getElementsByName("id");
	var submitForm = false;
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			submitForm = true;
		}
	}
	if (submitForm) {
		
			document.deleteForm.action = "unholdAllTickets.do";
			document.deleteForm.submit();
		
	} else {
		alert("Please select at least one record");
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




</script>
<form:form action="search.do" method="post" name="searchForm">
	<table id="form-table" width="47%" cellspacing="1" cellpadding="5" align="left">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Tickets" /> </b>
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
			<td align="${left}" class="first"><primo:label code="Load Date" />
			</td>
			<td align="${left}"><input id="datepicker" name="lDate" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate();"
				value="${sessionScope.searchCriteria.searchMap.lDate}"/>
			</td>
			<td align="${left}" class="first"><primo:label
					code="UnLoad Date" /></td>
			<td align="${left}"><input id="datepicker1" name="uDate" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate1();"
				value="${sessionScope.searchCriteria.searchMap.uDate}"/>
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
					<c:forEach items="${terminals}" var="item">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['driverCompany.name'] == item.name}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.name}"${selected}>${item.name}</option>
					</c:forEach>
			</select></td>
			
			
			
			<td align="${left}" class="first"><primo:label code="Terminal"/></td>
			<td align="${left}"><select id="terminalId" name="terminal.name" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${terminalslist}" var="item">
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
			<td align="${left}"><select id="origin" name="driver.fullName" style="min-width:154px; max-width:154px">
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
			<td align="${left}" class="first"><primo:label code="Bill Batch Date" />
			</td>
			<td align="${left}"><input id="datepicker2" name="billBatchDate" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate2();"
				value="${sessionScope.searchCriteria.searchMap.billBatchDate}"/>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Subcontractor"/></td>
			<td align="${left}">
				<select id="subcontractor" name="subcontractor" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
					<c:forEach items="${subcontractors}" var="aSubcontractor">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['subcontractor'] == aSubcontractor.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aSubcontractor.id}"${selected}>${aSubcontractor.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		
		<tr>
			<td align="${left}" class="first"><primo:label code="Ticket Status"/></td>
			<td align="${left}"><select id="status.id" name="status" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
					    <option value="0">Inactive</option>
	                                    <option value="1">Active</option>
                                            <option value="3">Incomplete</option>
			                  </select>
                         </td>

			
			<td align="${left}" class="first"><primo:label code="Process Status"/></td>
			<td align="${left}"><select id="Processstatus" name="ticketStatus" style="min-width:154px; max-width:154px">
				    <option value="">------<primo:label code="Please Select" />------</option>
					  <option value="0">On Hold</option>
	                                  <option value="1">Available</option>
                                          <option value="2">Invoiced</option>
			           </select>
                        </td>
			
               </tr>
               
               
               
               
               
               <tr>
               		
               		<td align="${left}" class="first"><primo:label code="Entered By"/></td>
			<td align="${left}"><select id="createdBy" name="createdBy" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${operators}" var="item">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap.createdBy == item.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.id}"${selected}>${item.name}</option>
					</c:forEach>
			</select></td>
               			
               			<td align="${left}" class="first"><primo:label code="Payroll Pending"/></td>
			<td align="${left}"><select id="PayRollStatus" name="payRollStatus" style="min-width:154px; max-width:154px">
				    <option value="">------<primo:label code="Please Select" />------</option>
					  
	                                  <option value="0">Yes</option>
                                       <option value="1">No</option>
                                       <option value="2">Done</option>     
			           </select>
                        </td>
               			
               			
               </tr>
               
               
        <tr>
			<td align="${left}" class="first"><primo:label code="Check Date" />
			</td>
			<td align="${left}"><input id="datepicker3" name="pDate" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate3();"
				value="${sessionScope.searchCriteria.searchMap.pDate}"/>
			</td>
			<td align="${left}" class="first"></td>
			<td align="${left}"></td>
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
		<td align="left" colspan="3">		
		<c:if test="${sessionScope.userInfo.role.id == 1 || sessionScope.userInfo.role.id == 8 
			|| sessionScope.userInfo.role.id == 10 || sessionScope.userInfo.id == 295
			|| sessionScope.userInfo.id == 30}">
			<img src="/trans/images/edit.png" border="0" title="BULK EDIT" class="toolbarButton"> 
			<a href="javascript:;"  onclick="editMultipleData()">BULK EDIT</a>&nbsp;&nbsp;
			<a href="javascript:;"  onclick="processRevert()">DRIVER PAY ADJ.</a>&nbsp;&nbsp;
		    <a href="javascript:;"  onclick="payrollPendingYes()">PR Pend. - YES</a>&nbsp;&nbsp;
			 <a href="javascript:;"  onclick="payrollPendingNo()">PR Pend. - NO</a>&nbsp;&nbsp;
		</c:if>
			 <a href="javascript:;"  onclick="massHold()">HOLD</a>&nbsp;&nbsp;
			 <a href="javascript:;"  onclick="massUnhold()">UNHOLD</a>&nbsp;&nbsp;
			
			
		</td>
	
	</tr>
</table>

<div style="width: 100%; margin: 0px auto;">
<form:form action="delete.do" id="deleteForm" name="deleteForm">
	<primo:datatable urlContext="operator/ticket" deletable="true"
		editable="true" exportPdf="true" exportXls="true"
		exportCsv="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="true" searcheable="false">
		<br/>
		<%-- <primo:textcolumn headerText="Entered by" dataField="enteredBy.name" /> --%>
		<primo:textcolumn headerText="Bill Batch Date" dataField="billBatch" dataFormat="MM-dd-yyyy" />
		<primo:textcolumn headerText="Entered by" dataField="enteredBy" />
		<primo:textcolumn headerText="Employee Company" dataField="driverCompany.name" />
		<primo:textcolumn headerText="Check Date" dataField="payRollBatch" dataFormat="MM-dd-yyyy" />
		<primo:textcolumn headerText="Truck#" dataField="vehicle.unit" type="int"/>
		<primo:textcolumn headerText="Trailer#" dataField="trailer.unit"  type="int"/>
		<primo:textcolumn headerText="Driver" dataField="driver.fullName" />
		<primo:textcolumn headerText="Load Date" dataField="loadDate"
			dataFormat="MM-dd-yyyy" />
		<primo:textcolumn headerText="UnLoad Date" dataField="unloadDate"
			dataFormat="MM-dd-yyyy" />
		<primo:textcolumn headerText="Origin" dataField="origin.name" />
		<primo:textcolumn headerText="Origin Ticket" dataField="originTicket" />
		<primo:textcolumn headerText="transfer TimeIn" dataField="transferTimeIn" />
		<primo:textcolumn headerText="transfer TimeOut" dataField="transferTimeOut" />
		<primo:textcolumn headerText="transfer Gross" dataField="transferGross" />
		<primo:textcolumn headerText="transfer Tare" dataField="transferTare" />
		<primo:textcolumn headerText="transfer Net" dataField="transferNet" />
		<primo:textcolumn headerText="transfer Tons" dataField="transferTons" />
		<primo:textcolumn headerText="Destination" dataField="destination.name" />
		<primo:textcolumn headerText="Destination Ticket" dataField="destinationTicket" />
		<primo:textcolumn headerText="landfill TimeIn" dataField="landfillTimeIn" />
		<primo:textcolumn headerText="landfill TimeOut" dataField="landfillTimeOut" />
		<primo:textcolumn headerText="landfill Gross" dataField="landfillGross" />
		<primo:textcolumn headerText="landfill Tare" dataField="landfillTare" />
		<primo:textcolumn headerText="landfill Net" dataField="landfillNet" />
		<primo:textcolumn headerText="landfill Tons" dataField="landfillTons" />
                <primo:staticdatacolumn headerText="Payroll Pending" dataField="payRollStatus" dataType="Payroll_Pending" />
		<primo:staticdatacolumn headerText="Complete Status" dataField="status" dataType="STATUS" />
		<primo:staticdatacolumn headerText="Hold Status" dataField="ticketStatus" dataType="TICKET_STATUS" />
		<primo:anchorcolumn headerText="Hold/Unhold" dataField="ticketStatus" linkUrl="/operator/ticket/changestatus.do?id={id}" linkText="Hold/Unhold"/>
	</primo:datatable>
	<% session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
</form:form>
</div>
