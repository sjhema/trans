<%@include file="/common/taglibs.jsp"%>
<h3><primo:label code="Manage Roles"/></h3>
<div id="search"><form:form action="search.do" method="get"
	name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search Role"/></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><label><primo:label code="Role Name"/></label></td>
			<td align="${left}"><input name="name" type="text"
				value="${sessionScope.searchCriteria.searchMap.name}"  style="min-width:149px; max-width:149px"/></td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();" value="<primo:label code="Search"/>"/></td>
		</tr>
	</table>
</form:form></div>
<br/>
<div style="width="100%"><form:form name="delete.do" id="serviceForm">
		<primo:datatable urlContext="admin/access/role" deletable="true" editable="true" insertable="true"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="true" searcheable="false" >
			<primo:textcolumn headerText="Role Name" dataField="name" />
			<primo:staticdatacolumn headerText="Status" dataField="status" dataType="STATUS"/>
			<primo:anchorcolumn headerText="Manage Permissions" linkUrl="/admin/access/roleprivilege/create.do?id={id}" linkText="Manage Permissions" dataField=""/>
			<primo:anchorcolumn headerText="Manage Data Access" linkUrl="/admin/access/dataprivilege/list.do?role.id={id}" linkText="Manage Data Access" dataField=""/>
		</primo:datatable>
	</form:form>
</div>