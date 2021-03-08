<%@ include file="/common/taglibs.jsp"%>

<h3><primo:label code="Manage Loading Fee"/></h3>
<form:form action="save.do" name="loadingFeeForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="2"><b><primo:label code="Add/Update Loading Fee" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Code" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="code" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="15" />
				<br><form:errors path="code" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Rate" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="rate" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" />
				<br><form:errors path="rate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Valid From" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input id="datepicker" path="validFrom" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate('datepicker');"/> 
				<br><form:errors path="validFrom" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Valid To" /><span
				class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input id="datepicker1" style="min-width:150px; max-width:150px"
					path="validTo" cssClass="flat" onblur="return formatDate('datepicker1');"/> 
			<br><form:errors path="validTo" cssClass="errorMessage" /></td>
		</tr>
		<!--
		<tr>
		    <td class="form-left"><primo:label code="Notes" /><span class="errorMessage"></span></td>
			<td align="${left}" colspan="5">
				<form:textarea path="notes" rows="2" cols="75"/>    	
			</td>
		</tr>
		-->
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" name="create" id="createBtn"
					value="<primo:label code="Save"/>" class="flat" /> 
				<input type="reset" id="resetBtn" value="<primo:label code="Reset"/>" class="flat" />
				<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat"
					onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>
