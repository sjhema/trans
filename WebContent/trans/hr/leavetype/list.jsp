<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
</script>
<h3><primo:label code="Manage Leave Type"/></h3>
<div><form:form action="search.do" method="get"
	name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search Leave Type"/></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Name"/></td>
				<td align="${left}"><select id="name" name="id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${leavetypes}" var="leavetype">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['id'] == leavetype.id}">
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
		</form:form></div>
		<div style="width:100%; margin:0px auto;"><form:form name="delete.do" id="customerForm">
		<primo:datatable urlContext="hr/leavetype" deletable="true" editable="true" insertable="true" exportPdf="true" exportXls="true" exportCsv="true"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
			<primo:textcolumn headerText="Leave Type" dataField="name" />
			
			<primo:textcolumn headerText="Description" dataField="description" />
				</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
	</form:form>
</div>