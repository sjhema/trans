<%@include file="/common/taglibs.jsp"%>

<table>
<tr>
<tr><td colspan="2"></td></tr>
<tr><td colspan="2"></td></tr>
<tr><td colspan="2"></td></tr>
</tr>
</table>
<h3>
	<primo:label code="Manage Employee Category" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Category" /></b></td>
		</tr>
		<tr>
		
		<td align="${left}" class="first"><primo:label code="Name"/></td>
			<td align="${left}"><select id="catagory" name="id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${catagory}" var="catagory">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['id'] == catagory.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${catagory.id}" ${selected}>${catagory.name}</option>
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
	<primo:datatable urlContext="hr/employeecatagory" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Name" dataField="name" />
		<primo:textcolumn headerText="Code" dataField="code" />
		
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>