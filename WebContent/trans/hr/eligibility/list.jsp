<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
</script>
<h3><primo:label code="Manage Eligibility"/></h3>
<div><form:form action="search.do" method="get"
	name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search Eligibility"/></b></td>
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
			
			<td align="${left}" class="first"><primo:label code="Leave Type"/></td>
				<td align="${left}"><select id="name" name="leaveType.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${leavetypes}" var="leavetype">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['leaveType.id'] == leavetype.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${leavetype.id}" ${selected}>${leavetype.name}</option>
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
		
		<div style="width:100%; margin:0px auto;"><form:form name="delete.do" id="customerForm">
		<primo:datatable urlContext="hr/eligibility" deletable="true" editable="true" insertable="true" exportPdf="true" exportXls="true" exportCsv="true"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
				
				<primo:textcolumn headerText="Company" dataField="company.name"/>
				<primo:textcolumn headerText="Terminal" dataField="terminal.name"/>	
				<primo:textcolumn headerText="Category" dataField="catagory.name" />
				<primo:textcolumn headerText="Leave Type" dataField="leaveType.name"/>	
				</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
	</form:form>
</div>
		