<%@ include file="/common/taglibs.jsp"%>
<h3><primo:label code="Manage Trip Sheet Location"/></h3>
<form:form action="save.do" name="locationForm"
	commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="2"><b><primo:label code="Add/Update Trip Sheet Location" />
			</b>
			</td>
		</tr>
		
		
		<tr>
			<td class="form-left"><primo:label code="Company" /><span class="errorMessage">*</span>
			</td><td><form:select cssClass="flat" path="driverCompany" id="companyId"  style="min-width:154px; max-width:154px">
							<form:option value="">-----Please Select----</form:option>
							<form:options items="${companies}" itemValue="id" itemLabel="name" />
						</form:select> 
					<br><form:errors path="driverCompany" cssClass="errorMessage" />
			</td>	
		
		</tr>
		
		
		<tr>
			<td class="form-left"><primo:label code="Terminal" /><span
				class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="terminal"
					style="min-width:154px; max-width:154px" id="terminalId">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${terminals}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Name" /><span
				class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="name"
					style="min-width:154px; max-width:154px" id="nameId">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${locationNames}" />
				</form:select> <br> <form:errors path="name" cssClass="errorMessage" />
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
