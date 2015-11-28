<%@ include file="/common/taglibs.jsp"%>
<h3><primo:label code="Manage PayRollRate"/></h3>
<form:form action="save.do" name="payrollrateForm"
	commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="2"><b><primo:label code="Add/Update PayRollRate" />
			</b>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Origin" /><span
				class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="origin">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${origins}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <form:errors path="origin" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Destination" /><span
				class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="destination">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${destinations}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <form:errors path="destination" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Terminal" /><span
				class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="state">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${states}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <form:errors path="state" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Rate" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="rate" cssClass="flat"
					cssStyle="width:200px;" maxlength="20" /> <br> <form:errors
					path="rate" cssClass="errorMessage" />
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
