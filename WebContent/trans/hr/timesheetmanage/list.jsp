<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function confirmAddToPrev(id) {
	if (!confirm("Do you want to add to previous timesheet?")) {
		return;
	}
	
	document.location = "${ctx}/hr/timesheetmanage/edit.do?id=" + id + "&mode=ADD_TO_PREV";
}
</script>
<h3>
	<primo:label code="Manage TimeSheet" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search TimeSheet" /></b></td>
		</tr>
		
		<tr>
		
		<td align="${left}" class="first"><primo:label code="Employee"/></td>
				<td align="${left}"><select id="driver" name="driver.fullName" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${employees}" var="driver">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['driver.fullName'] == driver}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${driver}" ${selected}>${driver}</option>
					</c:forEach>
			</select></td>
			<td align="${left}" class="first"><primo:label code="SSN"/></td>
			<td>
				<input id="driver.ssn" name="driver.ssn" type="text" value="${sessionScope.searchCriteria.searchMap.driver.ssn}"/>
			</td>
	   </tr>
		<tr>
				<td align="${left}" class="first"><primo:label code="Category"/></td>
				<td align="${left}"><select id="catagory" name="catagory.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${catagories}" var="catagory">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['catagory.id'] == catagory.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${catagory.id}" ${selected}>${catagory.name}</option>
					</c:forEach>
			</select></td>	
		</tr>
		
        <tr>
				<td align="${left}" class="first"><primo:label code="Company"/></td>
				<td align="${left}"><select id="company" name="company.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${companies}" var="company">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['company.id'] == company.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${company.id}" ${selected}>${company.name}</option>
					</c:forEach>
			</select></td>
			
			
			<td align="${left}" class="first"><primo:label code="Terminal"/></td>
				<td align="${left}"><select id="terminal" name="terminal.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${terminals}" var="terminal">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['terminal.id'] == terminal.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${terminal.id}" ${selected}>${terminal.name}</option>
					</c:forEach>
			</select></td>		
			
	   </tr>
	  
		
	   
	 
	  <tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();"
				value="<primo:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="delete.do" id="serviceForm">
	<primo:datatable urlContext="hr/timesheetmanage" deletable="true"
		editable="true" insertable="true" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		
		
	    <primo:datecolumn headerText="Batch Date" dataField="batchdate" dataFormat="MM-dd-yyyy"/>
	   
        <primo:textcolumn headerText="Company" dataField="company.name" />
        <primo:textcolumn headerText="Terminal" dataField="terminal.name" />
		<primo:textcolumn headerText="Employee" dataField="driver.fullName" />
		<primo:textcolumn headerText="Regular Hrs" dataField="regularhours" />
		<primo:textcolumn headerText="OT Hrs" dataField="totalothoursinweek" />
		<primo:textcolumn headerText="Holiday Hrs" dataField="holidayhours" />
		<primo:textcolumn headerText="Total Hrs" dataField="hoursworkedInweekRoundedValue" />
		<primo:textcolumn headerText="Category" dataField="catagory.name" />
		 <primo:datecolumn headerText="Check Date" dataField="hourlypayrollinvoiceDate" dataFormat="MM-dd-yyyy"/>
		<primo:anchorcolumn headerText="Copy"  linkUrl="/hr/timesheetmanage/copy.do?id={id}" linkText="Copy"/>
		<primo:anchorcolumn headerText="Add To Prev" linkUrl="javascript:confirmAddToPrev('{id}')" linkText="Add To Prev"/>
		
		<%-- <primo:textcolumn headerText="Sign In" dataField="signintime" />
		<primo:textcolumn headerText="Sing Out" dataField="signouttime" /> --%>
		
		
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


