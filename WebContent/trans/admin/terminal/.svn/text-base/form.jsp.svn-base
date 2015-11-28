<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
</script>
<h3>
	<primo:label code="Add/Update Terminal" />
</h3>
<form:form action="save.do" name="eligibilityForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
	<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Add/Update Eligibility"/></b></td>
		</tr>
		
		<tr>
		<td class="form-left"><primo:label code="Home Branch" /><span
				class="errorMessage">*</span></td>
		<td><form:input cssClass="flat" path="homeBranch" style="min-width:150px; max-width:150px"/>
					<br> <form:errors path="homeBranch" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
		<td class="form-left"><primo:label code="Company" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="company" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Terminal" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="terminal" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}"><input type="submit"
				name="create" id="create" onclick=""
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
	</form:form>
	