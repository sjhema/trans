<%@include file="/common/taglibs.jsp"%>
<br/>
<h3>Manage Static Data</h3>
<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="2"><b><primo:label code="Add/Update Static Data"/></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Data Type"/><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="dataType" cssClass="flat" style="min-width:150px; max-width:150px"/><br><form:errors path="dataType" cssClass="errorMessage"/></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Data Text"/><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="dataText" cssClass="flat" style="min-width:150px; max-width:150px"/><br><form:errors path="dataText" cssClass="errorMessage"/></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Data Value"/><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="dataValue" cssClass="flat" style="min-width:150px; max-width:150px"/><br><form:errors path="dataValue" cssClass="errorMessage"/></td>
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
	
