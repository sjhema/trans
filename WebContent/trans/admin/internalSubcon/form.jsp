<%@ include file="/common/taglibs.jsp"%>

<h3><primo:label code="Manage Internal Subcontractor"/></h3>

<form:form action="save.do" name="internalSubconMappingForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="2"><b><primo:label code="Add/Update Internal Subcontractor" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Driver Company" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="driverCompany" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select>
				<br> <form:errors path="driverCompany" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Billing Company" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="billingCompany" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select>
				<br> <form:errors path="billingCompany" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Origin" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="origin" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${origins}" itemValue="id" itemLabel="name" />
				</form:select>
				<br> <form:errors path="origin" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Destination" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="destination" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${destinations}" itemValue="id" itemLabel="name" />
				</form:select>
				<br> <form:errors path="destination" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Subcontractor" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="subcontractor" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${subcontractors}" itemValue="id" itemLabel="name" />
				</form:select>
				<br> <form:errors path="subcontractor" cssClass="errorMessage" />
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
