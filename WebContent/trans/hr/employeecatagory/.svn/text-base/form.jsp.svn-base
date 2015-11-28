<%@include file="/common/taglibs.jsp"%>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<br/>
	<br/>
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="4"><b><primo:label code="Add/Update Employee Category"/></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="name" cssClass="flat" />
			 	<br><form:errors path="name" cssClass="errorMessage" />
			</td>
		</tr>
			<tr>
			<td class="form-left"><primo:label code="Code" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="code" cssClass="flat" />
			 	<br><form:errors path="code" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick=""
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" /></td>
		</tr>
		</table>
		</form:form>
		