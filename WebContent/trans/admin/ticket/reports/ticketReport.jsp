<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function getForm() {
	var form = $('#ticketReportSearchForm');
	return form;
}

function processReport() {
	var form = getForm();
	form.attr('action', 'search.do');
	form.submit();
}

function formatDate(dateElemId) {
	var dateElem = document.getElementById(dateElemId);
	var date = dateElem.value;
	
	if (date != "") {
		if (date.length < 8) {
			alert("Invalidte date format");
			dateElem.value = "";
			return true;
		} else {
			var str = new String(date);
			if (!str.match("-")) {
				var mm = str.substring(0,2);
				var dd = str.substring(2,4);
				var yy = str.substring(4,8);
				var enddigit = str.substring(6,8);
				if (!enddigit == 00 && enddigit%4 == 0 ) {
					if (mm == 04 || mm == 06 || mm == 09 || mm == 11) {
						if (dd > 30) {
							alert("Invalid date format");
							dateElem.value="";
							return true;
						}
					} if (mm == 02 && dd > 29) {
						alert("Invalid date format");
						dateElem.value = "";
						return true;
					} else if (dd > 31) {
						alert("Invalid date format");
						dateElem.value="";
						return true;
					}
				} if (enddigit == 00 && yy%400 == 0) {
					if (mm == 04 || mm == 06 || mm == 09 || mm == 11) {
						if (dd > 30) {
							alert("Invalid date format");
							dateElem.value="";
							return true;
						}
					} if (mm == 02 && dd > 29) {
						alert("Invalid date format");
						dateElem.value="";
						return true;
					} else if (dd > 31) {
						alert("Invalid date format");
						dateElem.value="";
						return true;
					}					
				} else {
					if (mm == 04 || mm == 06 || mm == 09 || mm == 11) {
						if (dd > 30) {
							alert("Invalid date format");
							dateElem.value = "";
							return true;
						}
					} if (mm == 02 && dd > 28) {
						alert("Invalid date format");
						dateElem.value = "";
						return true;
					} else if (dd > 31) {
						alert("Invalid date format");
						dateElem.value = "";
						return true;
					}
				}
				date = mm+"-"+dd+"-"+yy;
				dateElem.value = date;
			}
	 	}
	}
}
</script>
<br/>

<form:form action="search.do" id="ticketReportSearchForm" name="ticketReportSearchForm" method="get">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Ticket Report" /></b></td>
	    </tr>
	    <tr>
			<td align="${left}" class="first"><primo:label code="Driver Company"/></td>
			<td align="${left}">
				<select id="driverCompany" name="driverCompany" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${companies}" var="aCompany">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['driverCompany'] == aCompany.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aCompany.id}" ${selected}>${aCompany.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Terminal"/></td>
			<td align="${left}">
				<select id="terminal" name="terminal" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${terminals}" var="aTerminal">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['terminal'] == aTerminal.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aTerminal.id}" ${selected}>${aTerminal.name}</option>
					</c:forEach>
				</select>
			</td>
	   	</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Driver"/></td>
			<td align="${left}">
				<select id="driver" name="driver" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${driverNames}" var="aDriverName">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['driver'] == aDriverName}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aDriverName}" ${selected}>${aDriverName}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Subcontractor"/></td>
			<td align="${left}">
				<select id="subcontractor" name="subcontractor" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${subcontractors}" var="aSubcontractor">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['subcontractor'] == aSubcontractor.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aSubcontractor.id}" ${selected}>${aSubcontractor.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		 <tr>
			<td align="${left}" class="first"><primo:label code="Origin"/></td>
			<td align="${left}">
				<select id="origin" name="origin" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${origins}" var="aOrigin">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['origin'] == aOrigin.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aOrigin.id}" ${selected}>${aOrigin.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Destination"/></td>
			<td align="${left}">
				<select id="destination" name="destination" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${destinations}" var="aDestination">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['destination'] == aDestination.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aDestination.id}" ${selected}>${aDestination.name}</option>
					</c:forEach>
				</select>
			</td>
	   	</tr>
	   	<tr>	 
			<td align="${left}" class="first"><primo:label code="Origin Ticket#"/></td>
			<td align="${left}">
				<input name="originTicket" type="text" style="min-width:150px; max-width:150px"
					value="${sessionScope.searchCriteria.searchMap.originTicket}" />
			</td>
			<td align="${left}" class="first"><primo:label code="Destination Ticket#"/></td>
			<td align="${left}">
				<input name="destinationTicket" type="text" style="min-width:150px; max-width:150px"
					value="${sessionScope.searchCriteria.searchMap.destinationTicket}" />
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Bill Batch Date From"/></td>
			<td align="${left}">
				<input id="datepicker" name="billBatchDateFrom" style="min-width:148px; max-width:148px" class="flat" 
					 onblur="return formatDate('datepicker');" value="${sessionScope.searchCriteria.searchMap.billBatchDateFrom}" /> 
			</td>
			<td align="${left}" class="first"><primo:label code="Bill Batch Date To"/></td>
			<td align="${left}">
				<input id="datepicker1" name="billBatchDateTo" style="min-width:148px; max-width:148px" class="flat" 
					onblur="return formatDate('datepicker1');" value="${sessionScope.searchCriteria.searchMap.billBatchDateTo}" /> 
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Check Date From"/></td>
			<td align="${left}">
				<input id="datepicker2" name="payrollBatchDateFrom" style="min-width:148px; max-width:148px" class="flat" 
					onblur="return formatDate('datepicker2');" value="${sessionScope.searchCriteria.searchMap.payrollBatchDateFrom}" /> 
			</td>
			<td align="${left}" class="first"><primo:label code="Check Date To"/></td>
			<td align="${left}">
				<input id="datepicker3" name="payrollBatchDateTo" style="min-width:148px; max-width:148px" class="flat" 
					onblur="return formatDate('datepicker3');" value="${sessionScope.searchCriteria.searchMap.payrollBatchDateTo}" /> 
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Load Date From"/></td>
			<td align="${left}">
				<input id="datepicker4" name="loadDateFrom" style="min-width:148px; max-width:148px" class="flat" 
					onblur="return formatDate('datepicker4');" value="${sessionScope.searchCriteria.searchMap.loadDateFrom}" /> 
			</td>
			<td align="${left}" class="first"><primo:label code="Load Date To"/></td>
			<td align="${left}">
				<input id="datepicker5" name="loadDateTo" style="min-width:148px; max-width:148px" class="flat" 
					onblur="return formatDate('datepicker5');" value="${sessionScope.searchCriteria.searchMap.loadDateTo}" /> 
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Unload Date From"/></td>
			<td align="${left}">
				<input id="datepicker6" name="unloadDateFrom" style="min-width:148px; max-width:148px" class="flat" 
					onblur="return formatDate('datepicker6');" value="${sessionScope.searchCriteria.searchMap.unloadDateFrom}" /> 
			</td>
			<td align="${left}" class="first"><primo:label code="Unload Date To"/></td>
			<td align="${left}">
				<input id="datepicker7" name="unloadDateTo" style="min-width:148px; max-width:148px" class="flat" 
					onblur="return formatDate('datepicker7');" value="${sessionScope.searchCriteria.searchMap.unloadDateTo}" /> 
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Ticket Status"/></td>
			<td align="${left}">
				<select id="ticketStatus" name="ticketStatus" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
				    <option value="0">Inactive</option>
                    <option value="1">Active</option>
                    <option value="3">Incomplete</option>
		         </select>
            </td>
			<td align="${left}" class="first"><primo:label code="Process Status"/></td>
			<td align="${left}">
				<select id="processStatus" name="processStatus" style="min-width:154px; max-width:154px">
				    <option value="">------<primo:label code="Please Select" />------</option>
					<option value="0">On Hold</option>
	                <option value="1">Available</option>
                    <option value="2">Invoiced</option>
			    </select>
            </td>
         </tr>
         <tr>
         	<td align="${left}" class="first"><primo:label code="Payroll Pending"/></td>
			<td align="${left}">
				<select id="payrollPending" name="payrollPending" style="min-width:154px; max-width:154px">
			    	<option value="">------<primo:label code="Please Select" />------</option>
				  	<option value="0">Yes</option>
                    <option value="1">No</option>
                    <option value="2">Done</option>     
		        </select>
            </td>
         	<td align="${left}" class="first"><primo:label code="Entered By"/></td>
			<td align="${left}">
				<select id="enteredBy" name="enteredBy" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
					<c:forEach items="${operators}" var="anOperator">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap.enteredBy == anOperator.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOperator.id}"${selected}>${anOperator.name}</option>
					</c:forEach>
				</select>
			</td>
        </tr>
        <tr>
         	<td align="${left}" class="first"><primo:label code="Not Billable"/></td>
			<td align="${left}">
				<select id="notBillable" name="notBillable" style="min-width:154px; max-width:154px">
				    <option value="">------<primo:label code="Please Select" />------</option>
				    <c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap.notBillable == 'Y'}">
						<c:set var="selected" value="selected" />
					</c:if>
				    <option value="Y" ${selected}>Y</option> 
			    </select>
            </td>
        </tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3">
				<input type="button"
					onclick="javascript:processReport()" value="Search" />
			</td>
		</tr>
	</table>
</form:form>

