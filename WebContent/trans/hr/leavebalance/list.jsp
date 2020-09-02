<%@include file="/common/taglibs.jsp"%>


<h3>
	<primo:label code="Manage Leave Balance"/>
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Leave Balance" /></b></td>
		</tr>
			
	
		<tr>
		<td align="${left}" class="first"><primo:label code="Employee"/></td>
			<td align="${left}"><select id="empname" name="empname.fullName" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${employees}" var="empname">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['empname.fullName'] == empname}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${empname}" ${selected}>${empname}</option>
				</c:forEach>
			</select></td>
			<td align="${left}" class="first"><primo:label code="SSN"/></td>
			<td>
				<input id="ssn" name="empname.ssn" type="text" value="${sessionScope.searchCriteria.searchMap.empname.ssn}"/>
			</td>
		</tr>
		
		<tr>
		<td align="${left}" class="first"><primo:label code="Category"/></td>
		<td align="${left}"><select id="empcategory" name="empcategory.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${catagories}" var="empcategory">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['empcategory.id'] == empcategory.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${empcategory.id}" ${selected}>${empcategory.name}</option>
				</c:forEach>
			</select>
		</td>
		<td align="${left}" class="first"><primo:label code="Leave Type"/></td>
				<td align="${left}"><select id="name" name="leavetype.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${leavetypes}" var="leavetype">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['leavetype.id'] == leavetype.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${leavetype.id}" ${selected}>${leavetype.name}</option>
					</c:forEach>
			</select>
		</td>						
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
	<primo:datatable urlContext="hr/leavebalance" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
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
		<primo:staticdatacolumn headerText="Status" dataField="status" dataType="STATUS" />
		<primo:anchorcolumn headerText="Check History" dataField="id" linkUrl="/hr/leavebalance/history.do?id={id}" linkText="History"/>
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>