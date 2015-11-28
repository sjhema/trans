<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
</script>
<br/>
<c:if test="${list3 eq null}">
<input type="button" value="Back to PTOD Application" onclick="javascript:location.href='${ctx}/hr/ptodapplication/redirect.do'"/>
</c:if>
<c:if test="${list3 ne null}">
<input type="button" value="Back to Current Leave Balance" onclick="javascript:location.href='${ctx}/hr/leavebalance/search.do'"/>
</c:if>

<input type="button" value="Enter PTOD Application" onclick="javascript:location.href='${ctx}/hr/ptodapplication/create.do'"/>
<br/>
<h3>
	<primo:label code="Leave Current" />
</h3>
<primo:datatable urlContext="" deletable="false" editable="false" insertable="false" exportPdf="false" exportXls="false" exportCsv="false"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
		<primo:textcolumn headerText="Company" dataField="company.name"/>
		<primo:textcolumn headerText="Terminal" dataField="terminal.name"/>		
		<primo:textcolumn headerText="Category" dataField="empcategory.name" />
		<primo:textcolumn headerText="Name" dataField="empname.fullName" />
		<primo:datecolumn headerText="Hire&nbsp;Date" dataField="empname.dateHired" dataFormat="MM-dd-yyyy"/>
		<primo:datecolumn headerText="Rehire&nbsp;Date" dataField="empname.dateReHired" dataFormat="MM-dd-yyyy"/>
		<primo:textcolumn headerText="Leave Type" dataField="leavetype.name" />
		<primo:textcolumn headerText="Days Accrued" dataField="daysaccrude" />
		<primo:textcolumn headerText="Days Earned" dataField="dayssbalance" />
		<primo:textcolumn headerText="Days Available" dataField="daysavailable" />
		<primo:textcolumn headerText="Days Used" dataField="daysused" />
		<primo:textcolumn headerText="Days Remaining" dataField="daysremain" />
		<primo:textcolumn headerText="Hours Accrued" dataField="hoursaccrude" />
		<primo:textcolumn headerText="Hours Earned" dataField="hoursbalance" />
		<primo:textcolumn headerText="Hours Available" dataField="hoursavailable" />
		<primo:textcolumn headerText="Hours Used" dataField="hoursused" />
		<primo:textcolumn headerText="Hours Remaining" dataField="hourremain" />
		<primo:textcolumn headerText="Effective Date From" dataField="dateEffectiveFrom" dataFormat="MM-dd-yyyy" />
		<primo:textcolumn headerText="Effective Date To" dataField="dateEffectiveTo" dataFormat="MM-dd-yyyy" />
		<primo:textcolumn headerText="Note" dataField="note" />
</primo:datatable>
<br/>
<h3>
	<primo:label code="PTOD Application History" />
</h3>
<c:if test="${sessionScope.userInfo.role.name eq 'HR'||sessionScope.userInfo.role.name eq 'ADMIN'}">
<primo:datatable urlContext="" deletable="false" editable="false" insertable="false" exportPdf="false" exportXls="false" exportCsv="false"
			baseObjects="${list1}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
		<%-- <primo:textcolumn headerText="Leave Type" dataField="leavetype.name" />
		<primo:textcolumn headerText="Submit&nbsp;Date" dataField="submitdate" dataFormat="MM-dd-yyyy" />
		<primo:textcolumn headerText="Days Requested" dataField="daysrequested" />
		<primo:textcolumn headerText="Leave&nbsp;Date From" dataField="leavedatefrom" dataFormat="MM-dd-yyyy" />
		<primo:textcolumn headerText="Leave&nbsp;Date To" dataField="leavedateto" dataFormat="MM-dd-yyyy" />
		<primo:textcolumn headerText="Days Paid" dataField="dayspaid" />
		<primo:textcolumn headerText="Days Unpaid" dataField="daysunpaid" />
		<primo:textcolumn headerText="Ptod Rate" dataField="ptodrates" />
		<primo:textcolumn headerText="Amount Paid" dataField="amountpaid" />
		<primo:textcolumn headerText="Hours Requested" dataField="hoursrequested" />
		<primo:textcolumn headerText="Hours Paid" dataField="hourspaid" />
		<primo:textcolumn headerText="Hourly Rate" dataField="ptodhourlyrate" />
		<primo:textcolumn headerText="Hourly Amount Paid" dataField="hourlyamountpaid" />
		<primo:textcolumn headerText="Check&nbsp;Date" dataField="checkdate" dataFormat="MM-dd-yyyy" />
		<primo:staticdatacolumn headerText="Approve Status" dataField="approvestatus" dataType="APPROVE_STATUS" />
		<primo:textcolumn headerText="Approved By" dataField="approveby.fullName" />
		<primo:textcolumn headerText="Note" dataField="notes" /> --%>
		
		<primo:textcolumn headerText="Company" dataField="company.name" />
		<primo:textcolumn headerText="Terminal" dataField="terminal.name" />		
		<primo:textcolumn headerText="Category" dataField="category.name" />
		<primo:textcolumn headerText="Employee" dataField="driver.fullName" />
		<primo:datecolumn headerText="Hire&nbsp;Date" dataField="driver.dateHired" dataFormat="MM-dd-yyyy"/>
		<primo:datecolumn headerText="Rehire&nbsp;Date" dataField="driver.dateReHired" dataFormat="MM-dd-yyyy"/>
		<primo:textcolumn headerText="Leave Type" dataField="leavetype.name" />		
		<primo:datecolumn headerText="Leave Date From" dataField="leavedatefrom" dataFormat="MM-dd-yyyy"/>
		<primo:datecolumn headerText="Leave Date To" dataField="leavedateto" dataFormat="MM-dd-yyyy"/>
		
		<primo:textcolumn headerText="Days Req" dataField="daysrequested" />
		<primo:textcolumn headerText="Days Paid" dataField="dayspaid" />
		<primo:textcolumn headerText="Days Unpaid" dataField="daysunpaid" />
		<primo:textcolumn headerText="Days Paidout" dataField="paidoutdays" />
		<primo:textcolumn headerText="Ptod Rate" dataField="ptodrates" />
		<primo:textcolumn headerText="Amount Paid" dataField="amountpaid" />
		
		
		<primo:textcolumn headerText="Hrs Req" dataField="hoursrequested" />
		<primo:textcolumn headerText="Hrs Paid" dataField="hourspaid" />
		<primo:textcolumn headerText="Hrs Unpaid" dataField="hoursunpaid" />
		<primo:textcolumn headerText="Hrs Paidout" dataField="paidouthours" />
		<primo:textcolumn headerText="Hourly Rate" dataField="ptodhourlyrate" />
		<primo:textcolumn headerText="Hourly Amount Paid" dataField="hourlyamountpaid" />		
				
				
		<primo:datecolumn headerText="Batch&nbsp;Date" dataField="batchdate" dataFormat="MM-dd-yyyy"/>		
		<primo:textcolumn headerText="Check&nbsp;Date" dataField="checkdate" dataFormat="MM-dd-yyyy" />
		<primo:staticdatacolumn headerText="Status" dataField="approvestatus" dataType="APPROVE_STATUS" />
		<primo:textcolumn headerText="Approved By" dataField="approveby.fullName" />
		<primo:textcolumn headerText="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Note&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" dataField="notes" />
		
		
		
		
</primo:datatable>
</c:if>
<c:if test="${sessionScope.userInfo.role.name ne 'HR'}">
<c:if test="${sessionScope.userInfo.role.name ne 'ADMIN'}">
<primo:datatable urlContext="" deletable="false" editable="false" insertable="false" exportPdf="false" exportXls="false" exportCsv="false"
			baseObjects="${list1}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
		<primo:textcolumn headerText="Leave Type" dataField="leavetype.name" />
		<primo:textcolumn headerText="Submit Date" dataField="submitdate" dataFormat="MM-dd-yyyy" />
		<primo:textcolumn headerText="Days Requested" dataField="daysrequested" />
		<primo:textcolumn headerText="Leave Date From" dataField="leavedatefrom" dataFormat="MM-dd-yyyy" />
		<primo:textcolumn headerText="Leave Date To" dataField="leavedateto" dataFormat="MM-dd-yyyy" />
		<primo:textcolumn headerText="Days Paid" dataField="dayspaid" />
		<primo:textcolumn headerText="Days Unpaid" dataField="daysunpaid" />
		<%-- <primo:textcolumn headerText="Ptod Rate" dataField="ptodrates" />
		<primo:textcolumn headerText="Amount Paid" dataField="amountpaid" /> --%>
		<primo:textcolumn headerText="Hours Requested" dataField="hoursrequested" />
		<primo:textcolumn headerText="Hours Paid" dataField="hourspaid" />
		<%-- <primo:textcolumn headerText="Hourly Rate" dataField="ptodhourlyrate" />
		<primo:textcolumn headerText="Hourly Amount Paid" dataField="hourlyamountpaid" /> --%>
		<primo:textcolumn headerText="Check Date" dataField="checkdate" dataFormat="MM-dd-yyyy" />
		<primo:staticdatacolumn headerText="Approve Status" dataField="approvestatus" dataType="APPROVE_STATUS" />
		<primo:textcolumn headerText="Approved By" dataField="approveby.fullName" />
		<primo:textcolumn headerText="Note" dataField="notes" />
</primo:datatable>
</c:if>
</c:if>
