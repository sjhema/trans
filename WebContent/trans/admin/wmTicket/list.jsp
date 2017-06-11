<%@include file="/common/taglibs.jsp"%>

<h3>
	<primo:label code="Manage WM Ticket" />
</h3>
<form:form action="search.do" method="post" name="wmTicketSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search WM Ticket" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Origin"/></td>
			<td align="${left}">
				<select id="origin" name="origin" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
					<c:forEach items="${origins}" var="anOrigin">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['origin'] == anOrigin.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${anOrigin.id}"${selected}>${anOrigin.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Destination" /></td>
			<td align="${left}">
				<select id="destination" name="destination" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
					<c:forEach items="${destinations}" var="aDestination">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['destination'] == aDestination.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aDestination.id}"${selected}>${aDestination.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>	 
			<td align="${left}" class="first"><primo:label code="Ticket#" /></td>
			<td align="${left}">
				<input name="ticket" type="text" style="min-width:148px; max-width:148px"
					value="${sessionScope.searchCriteria.searchMap.ticket}" />
			</td>
			<td align="${left}" class="first"><primo:label code="Processing Status"/></td>
			<td align="${left}">
				<select id="processingStatus" name="processingStatus" style="min-width:154px; max-width:154px">
				    <option value="">------<primo:label code="Please Select" />------</option>
					<c:forEach items="${processingStatuses}" var="aProcessingStatus">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['processingStatus'] == aProcessingStatus.dataValue}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aProcessingStatus.dataValue}"${selected}>${aProcessingStatus.dataText}</option>
					</c:forEach>
			     </select>
             </td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Date From"/></td>
			<td align="${left}">
				<input id="datepicker" name="dateFrom" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.dateFrom}" /> 
			</td>
			<td align="${left}" class="first"><primo:label code="Date To"/></td>
			<td align="${left}">
				<input id="datepicker1" name="dateTo" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.dateTo}" /> 
			</td>
		</tr>
	 	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['wmTicketSearchForm'].submit();"
					value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="wmTicketServiceForm" id="wmTicketServiceForm">
	<primo:datatable urlContext="admin/wmTicket" deletable="false"
		editable="false" insertable="false" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false"
		exportPdf="false" exportXls="false" exportCsv="false">
		<primo:datecolumn headerText="Date" dataField="txnDate" dataFormat="MM-dd-yyyy" width="95px"/>
		<primo:textcolumn headerText="Ticket" dataField="ticket" />
		<primo:textcolumn headerText="Hauling Ticket" dataField="haulingTicket" />
		<primo:textcolumn headerText="Ticket Type" dataField="ticketType" />
		<primo:textcolumn headerText="Origin" dataField="origin.name" width="250px"/>
		<primo:textcolumn headerText="Destination" dataField="destination.name" width="250px"/>
		<primo:textcolumn headerText="Time In" dataField="timeIn" />
		<primo:textcolumn headerText="Time Out" dataField="timeOut" />
		<primo:textcolumn headerText="Gross" dataField="gross"/>
		<primo:textcolumn headerText="Tare" dataField="tare"/>
		<primo:staticdatacolumn headerText="Processing Status" dataField="processingStatus" dataType="WMTICKET_PROCESSING_STATUS" />
    </primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


