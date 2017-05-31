<%@ include file="/common/taglibs.jsp"%>
<h3><primo:label code="Manage Location"/></h3>
<form:form action="save.do" name="locationForm"
	commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="2"><b><primo:label code="Add/Update Location" />
			</b>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Name" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="name" cssClass="flat"
					style="min-width:150px; max-width:150px" maxlength="20" /> <br> <form:errors
					path="name" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Lomg Name" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="longName" cssClass="flat"
					style="min-width:250px; max-width:250px" /> 
				<br><form:errors path="longName" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Code" />
			</td>
			<td align="${left}"><form:input path="code" cssClass="flat"
					style="min-width:150px; max-width:150px" maxlength="20" /> <br> <form:errors
					path="code" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Type" /><span
				class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="type" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${locationTypes}" itemValue="dataValue"
						itemLabel="dataText" />
				</form:select> <br> <form:errors path="type" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Status" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="status">
					<form:options items="${locationstatus}" itemValue="dataValue"
						itemLabel="dataText" />
				</form:select> <br> <form:errors path="status" cssClass="errorMessage" /></td>

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
