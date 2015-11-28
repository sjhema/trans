<%@include file="/common/taglibs.jsp"%>
<div id="search"><form:form action="search.do" method="get"
	name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search Role"/></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><label><primo:label code="Role Name"/></label></td>
			<td align="${left}"><input name="name" type="text"
				value="${sessionScope.searchCriteria.searchMap.name}" /></td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();" value="<primo:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form></div>
<br/>
<div style="width="100%"><form:form name="delete.do" id="serviceForm">
		<primo:datatable urlContext="roleprivilege" deletable="true" editable="true" insertable="true"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="true" searcheable="false" >
			<primo:textcolumn headerText="Business Object" dataField="businessObject.displayTag" />
			<primo:staticdatacolumn headerText="Status" dataField="status" dataType="ENTITY_STATUS"/>
		</primo:datatable>
	</form:form>
</div>