<%@ include file="/common/taglibs.jsp"%>

<h3><primo:label code="Manage WM Location"/></h3>

<form:form action="save.do" name="wmLocationForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="2"><b><primo:label code="Add/Update WM Location" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Location" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="location" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${locations}" itemValue="id" itemLabel="name" />
				</form:select>
				<br> <form:errors path="location" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="WM Location Name" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="wmLocationName" cssClass="flat" style="min-width:400px; max-width:400px" /> 
				<br> <form:errors path="wmLocationName" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" name="create" id="create" onclick=""
					value="<primo:label code="Save"/>" class="flat" /> 
				<input type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
					class="flat" /> 
				<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat"
					onClick="document.location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>
