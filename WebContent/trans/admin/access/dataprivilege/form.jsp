<%@ include file="/common/taglibs.jsp"%>

<h3><primo:label code="Manage Data Access Privilege"/></h3>
<form:form action="save.do" name="dataAccessPrivilegeForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="role.id" id="roleId" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="2"><b><primo:label code="Add/Update Data Access Privilege" /></b></td>
		</tr>
		<tr>
			<td class="form-left">
				<primo:label code="Role Name" />
			</td>
			<td style="font-weight: 900;">${modelObject.role.name}</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Screen Name"/><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select path="bo.id" style="min-width:153px; max-width:153px">
					<form:option value=""><primo:label code="Please Select"/></form:option>
					<c:forEach var="item" items="${boList}">
						<form:option value="${item.id}" label="${item.objectName}"/>
					</c:forEach>
				</form:select>
				<br><form:errors path="bo.id" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">
				<primo:label code="Employee Category" /><span class="errorMessage">*</span>
			</td>
			<td>
				<form:checkboxes items="${empCatNames}" path="privilegeArrEmpCat" />
				<br><form:errors path="privilegeArrEmpCat" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Has Editable"/></td>
			<td align="${left}">
				<form:select path="hasEditable" style="min-width:153px; max-width:153px">
					<form:option value=""><primo:label code="Please Select"/></form:option>
					<form:option value="Y"><primo:label code="Yes"/></form:option>
					<form:option value="N"><primo:label code="No"/></form:option>
				</form:select>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Has Deletable"/></td>
			<td align="${left}">
				<form:select path="hasDeletable" style="min-width:153px; max-width:153px">
					<form:option value=""><primo:label code="Please Select"/></form:option>
					<form:option value="Y"><primo:label code="Yes"/></form:option>
					<form:option value="N"><primo:label code="No"/></form:option>
				</form:select>
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" name="create" id="createBtn"
					value="<primo:label code="Save"/>" class="flat" /> 
				<input type="reset" id="resetBtn" value="<primo:label code="Reset"/>" class="flat" />
				<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat"
					onClick="location.href='list.do?role.id=${modelObject.role.id}'" />
			</td>
		</tr>
	</table>
</form:form>
