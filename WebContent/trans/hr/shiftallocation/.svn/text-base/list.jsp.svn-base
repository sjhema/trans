<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Shift Allocation" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Shift Allocation" /></b></td>
		</tr>
		
		<tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
				<td align="${left}"><select id="company" name="company.id">
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
				<td align="${left}"><select id="terminal" name="terminal.id">
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
			<td align="${left}" class="first"><primo:label code="Employee"/></td>
				<td align="${left}"><select id="driver" name="driver.id">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${employees}" var="driver">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['driver.id'] == driver.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${driver.id}" ${selected}>${driver.fullName}</option>
					</c:forEach>
			</select></td>
			
			<td align="${left}" class="first"><primo:label code="Shift Name"/></td>
				<td align="${left}"><select id="shift_calendar" name="shift_calendar.id">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${shiftnames}" var="shift_calendar">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['shift_calendar.id'] == shift_calendar.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${shift_calendar.id}" ${selected}>${shift_calendar.name}</option>
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
	<primo:datatable urlContext="hr/shiftallocation" deletable="true"
		editable="true" insertable="true" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		
		
	    <primo:textcolumn headerText="Company" dataField="company.name" />
		<primo:textcolumn headerText="Terminal" dataField="terminal.name" />
		<primo:textcolumn headerText="shift" dataField="shift_calendar.name" />
		<primo:textcolumn headerText="Employee" dataField="driver.fullName" />
		<primo:datecolumn headerText="Effective Start Date" dataField="effectivestartdate" dataFormat="MM-dd-yyyy"/>
		<primo:datecolumn headerText="Effective End Date" dataField="effectiveenddate" dataFormat="MM-dd-yyyy"/>
		<primo:staticdatacolumn headerText="Status" dataField="status" dataType="STATUS" />
		
		
		
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


