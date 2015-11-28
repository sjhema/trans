<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Attendance" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Attendance" /></b></td>
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
				<td align="${left}"><select id="shift" name="shift.id">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${shiftnames}" var="shift">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['shift.id'] == shift.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${shift.id}" ${selected}>${shift.name}</option>
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
	<primo:datatable urlContext="hr/attendance" deletable="true"
		editable="true" insertable="true" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		
		
	    <primo:datecolumn headerText="Date" dataField="attendancedate" dataFormat="MM-dd-yyyy"/>
        <primo:textcolumn headerText="shift" dataField="shift.name" />
        <primo:textcolumn headerText="Company" dataField="company.name" />
        <primo:textcolumn headerText="Terminal" dataField="terminal.name" />
         <primo:textcolumn headerText="Category" dataField="catagory.name" />
		<primo:textcolumn headerText="Employee" dataField="driver.fullName" />
		<primo:textcolumn headerText="Sign In" dataField="signintime" />
		<primo:textcolumn headerText="Sing Out" dataField="signouttime" />
		
		
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


