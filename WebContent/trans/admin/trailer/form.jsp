<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Trailers" />
</h3>
<form:form action="save.do" name="modelForm"
	modelAttribute="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="2"><b>Add/Update Trailer</b>
			</td>
		</tr>
		<tr>
			<td class="form-left">Number<span class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="number"
					onkeypress="return onlyNumbers(event, false)" id="number" /> <form:errors
					path="number" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left">Type<span class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="type" id="type"/> <form:errors path="type"
					cssClass="errorMessage" /></td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick="" value="Save" class="flat" /> <input
				type="reset" id="resetBtn" value="Reset" class="flat" /> <input
				type="button" id="cancelBtn" value="Cancel" class="flat"
				onClick="location.href='list.do'" /></td>
		</tr>
	</table>
</form:form>