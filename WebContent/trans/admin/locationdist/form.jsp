<%@include file="/common/taglibs.jsp"%>

<h3>
	<primo:label code="Add/Update Load Miles" />
</h3>

<form:form action="save.do" name="modelForm" modelAttribute="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="5"><b>Load Miles</b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Origin" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="origin" id="origin" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${origins}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="origin" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Destination" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="destination" id="destination" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${destinations}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="destination" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Miles" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="miles" style="min-width:150px; max-width:150px" cssClass="flat"
					 maxlength="9" onkeypress="return onlyNumbers(event, true)"/>
				<br><form:errors path="miles" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" name="create" id="create" onclick="" value="Save" class="flat" /> 
				<input type="reset" id="resetBtn" value="Reset" class="flat" />
				<input type="button" id="cancelBtn" value="Cancel" class="flat" onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>


