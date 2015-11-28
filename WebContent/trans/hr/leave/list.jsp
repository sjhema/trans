<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Leave" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Leave" /></b></td>
		</tr>
		<tr>
			
	 <td align="${left}" class="first"><primo:label code="Leave Type"/></td>
			<td align="${left}"><select id="leavetype" name="leavetype">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${LeaveTypes}" var="leavetype">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['id'] == leavetype.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${leavetype.dataText}" ${selected}>${leavetype.dataText}</option>
				</c:forEach>
			</select>
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
	<primo:datatable urlContext="hr/leave" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		exportPdf="true" exportXls="true"
			exportCsv="true"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		
		<primo:textcolumn headerText="Leave Type" dataField="leavetype" />
		<primo:textcolumn headerText="Leave Description" dataField="description" />
		
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


