<%@include file="/common/taglibs.jsp"%>
<h3><primo:label code="Manage Probation Type"/></h3>
<div><form:form action="search.do" method="get"
	name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search Probation Type"/></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Probation Name"/></td>
				<td align="${left}"><select id="probationName" name="probationName" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${probationNames}" var="probationtype">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['probationName'] == probationtype.probationName}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${probationtype.probationName}" ${selected}>${probationtype.probationName}</option>
					</c:forEach>
				</select>
			</td>
		<td align="${left}" class="first"><primo:label code="Emp. Category"/></td>
				<td align="${left}"><select id="empCategory" name="empCategory.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${categories}" var="category">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['empCategory.id'] == category.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${category.id}" ${selected}>${category.name}</option>
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
		<td align="${left}" class="first"><primo:label code="Prob. Category"/></td>
				<td align="${left}"><select id="probationCategory" name="probationCategory" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${probationCategories}" var="probcategory">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['probationCategory'] == probcategory.dataValue}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${probcategory.dataValue}" ${selected}>${probcategory.dataLabel}</option>
					</c:forEach>
			</select></td>
		</tr>
			<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();" value="<primo:label code="Search"/>" /></td>
		</tr>
		</table>
		</form:form>
		</div>
		
		<div style="width:100%; margin:0px auto;"><form:form name="delete.do" id="probationTypeForm">
		<primo:datatable urlContext="hr/probationtype" deletable="true" editable="true" insertable="true" exportPdf="true" exportXls="true" exportCsv="true"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
				<primo:textcolumn headerText="Probation Name" dataField="probationName"/>
				<primo:textcolumn headerText="Prob. Category" dataField="probationCategory"/>	
				<primo:textcolumn headerText="Company" dataField="company.name"/>
				<primo:textcolumn headerText="Terminal" dataField="terminal.name"/>	
				<primo:textcolumn headerText="Emp. Category" dataField="empCategory.name" />
				<primo:textcolumn headerText="No. of Days" dataField="noOfDays"/>
				<primo:datecolumn headerText="Effective Batch Date From" dataField="dateFrom"
			dataFormat="MM-dd-yyyy" />
			<primo:datecolumn headerText="Effective Batch Date To" dataField="dateTo"
			dataFormat="MM-dd-yyyy" />
				</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
	</form:form>
</div>