<%@ include file="/common/taglibs.jsp"%>
<h3><primo:label code="Manage Trip Rate"/></h3>
<form:form action="save.do" name="triprateForm"
	commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="2"><b><primo:label code="Add/Update Trip Rate" />
			</b>
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Transfer Station" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="transferStation">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${transferStations}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="transferStation" cssClass="errorMessage" />
			</td>		
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Landfill" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="landfill">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${landfills}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="landfill" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Rate" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="rate"
					cssClass="flat" /> <br> 
			<form:errors path="rate" cssClass="errorMessage" /></td>
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
