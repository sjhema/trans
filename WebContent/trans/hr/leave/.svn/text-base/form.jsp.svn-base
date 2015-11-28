<%@include file="/common/taglibs.jsp"%>

<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="4"><b><primo:label code="Add/Update Leave Type"/></b></td>
		</tr>
		<tr>
		     <td class="form-left">Leave Type<span class="errorMessage">*</span></td>
		       <td align="${left}"><form:select cssClass="flat" path="leavetype">
					<form:option value="">-----Please Select----</form:option>
					<form:options items="${LeaveTypes}" itemValue="dataText"
						itemLabel="dataText" />
				</form:select> <br><form:errors path="leavetype" cssClass="errorMessage" /></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Leave Description"/><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="description" cssClass="flat" cssStyle="width:200px;"/>
				 <br><form:errors path="description" cssClass="errorMessage" />
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
	
