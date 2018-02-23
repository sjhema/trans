<%@ include file="/common/taglibs.jsp"%>

<h3><primo:label code="Manage Vehicle Without GPS"/></h3>

<form:form action="save.do" name="vehicleGPSForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="2"><b><primo:label code="Add/Update Vehicle Without GPS" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Unit" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="vehicle" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${vehicles}" itemValue="id" itemLabel="unit" />
				</form:select>
				<br> <form:errors path="vehicle" cssClass="errorMessage" />
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
