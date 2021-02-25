<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Add/Update Data Access Privilege" />
</h3>
<form:form action="save.do" name="dataAccessPrivForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="role.id" id="roleId" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr>
			<td class="form-left">
				<primo:label code="Role Name" />
			</td>
			<td>
				${modelObject.role.name}
			</td>
		</tr>
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Employee Category"/></b></td>
		</tr>
		<tr>
			<td class="form-left">
				<primo:label code="Screen" />
			</td>
			<td>
				Payroll Report
			</td>
		</tr>
		<tr>
			<td class="form-left">
				<primo:label code="Employee Category" /><span class="errorMessage">*</span>
			</td>
			<td>
				<form:checkboxes items="${empCatNames}" path="privilegeArrPayrollReport" />
				<br><form:errors path="privilegeArrPayrollReport" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">
				<primo:label code="Screen" />
			</td>
			<td>
				Manage Employee
			</td>
		</tr>
		<tr>
			<td class="form-left">
				<primo:label code="Employee Category" /><span class="errorMessage">*</span>
			</td>
			<td>
				<form:checkboxes items="${empCatNames}" path="privilegeArrManageEmployee" />
				<br><form:errors path="privilegeArrManageEmployee" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
  			<td align="${left}" colspan="2">
				<input type="submit" value="<primo:label code="Save"/>" class="flat" />
				&nbsp;
				<input type="reset" value="<primo:label code="Reset"/>" class="flat" />
				&nbsp;
				<input type="button" value="<primo:label code="Cancel"/>" class="flat"
					onClick="document.location.href='../role/list.do'" />
			</td>
  		</tr>
  </table>
</form:form>

