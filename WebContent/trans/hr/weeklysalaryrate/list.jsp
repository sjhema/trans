<%@include file="/common/taglibs.jsp"%>


<h3>
	<primo:label code="Manage Weekly Salary Rate"/>
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Weekly Salary Rate" /></b></td>
		</tr>
			
		<tr>
		<td align="${left}" class="first"><primo:label code="Employee"/></td>
		<td align="${left}"><select id="driver" name="driver.fullName" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${employees}" var="driver">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['driver.fullName'] == driver}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${driver}"${selected}>${driver}</option>
					</c:forEach>
			</select>
			</td>
			<%-- <td align="${left}"><select id="empname" name="empname.id">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${employees}" var="empname">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['empname.id'] == empname.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${empname.id}" ${selected}>${empname.fullName}</option>
				</c:forEach>
			</select></td> --%>
		<td align="${left}" class="first"><primo:label code="Category"/></td>
		<td align="${left}"><select id="company" name="catagory.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${catagories}" var="catagory">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['catagory.id'] == catagory.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${catagory.id}" ${selected}>${catagory.name}</option>
					</c:forEach>
			</select></td>
		
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
			<td align="${left}" class="first">Valid From</td>
			<td align="${left}"><input name="ValidFrom" id="datepicker1" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate('datepicker1');"
				value='${sessionScope.searchCriteria.searchMap.ValidFrom}' />
			</td>

			<td align="${left}" class="first">Valid To</td>
			<td align="${left}"><input name="ValidTo" id="datepicker2" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate('datepicker2');"
				value='${sessionScope.searchCriteria.searchMap.ValidFrom}' />
			</td>

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
	<primo:datatable urlContext="hr/weeklysalaryrate" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"  exportPdf="true" exportCsv="true" exportXls="true"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Employee" dataField="driver.fullName" />
		<primo:textcolumn headerText="Staff Id" dataField="staffId" />
		<primo:textcolumn headerText="Company" dataField="company.name" />
		<primo:textcolumn headerText="Terminal" dataField="terminal.name" />
		<primo:textcolumn headerText="Category" dataField="catagory.name" />
		<primo:textcolumn headerText="Weekly Pay" dataField="weeklySalary" />
		<primo:textcolumn headerText="Daily Pay" dataField="dailySalary" />
		<primo:datecolumn headerText="Valid Date" dataField="validFrom"
			dataFormat="MM-dd-yyyy" />
		<primo:datecolumn headerText="Valid To" dataField="validTo"
			dataFormat="MM-dd-yyyy" />
		
		
		
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>