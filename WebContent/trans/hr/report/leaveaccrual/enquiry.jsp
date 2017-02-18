<%@ include file="/common/taglibs.jsp"%>
<script language="javascript">
function searchReport() {
	document.forms[0].submit();
}
</script>

<c:if test="${errors != null && errors != ''}">
	<FONT color=#F2290F><STRONG>${errors}</strong></FONT>
</c:if>
<form:form action="search.do" name="searchForm" method="post" commandName="modelObject">
	<br/>
	<br/>
	
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4">
				<b><primo:label code="Yearly Vacation Accrual Report" /></b>
			</td>
	    </tr>
	    <tr>
		    <td class="form-left"><primo:label code="Employee" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="employee" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="fullName" />
				</form:select> 
			</td>
		    <td class="form-left"><primo:label code="Category" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="category" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${categories}" itemValue="id" itemLabel="name" />
				</form:select>
			</td>		
		</tr>
		<tr>
		 	<td class="form-left"><primo:label code="Company" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="company" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> 
			</td>
			<td class="form-left"><primo:label code="Terminal" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="terminal" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select>
			</td>
		</tr>
	   	<tr>
			<td class="form-left"><primo:label code="Leave Type" /><span class="errorMessage"></span></td>
			<td>
				<form:select cssClass="flat" path="leaveType" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${leavetypes}" itemValue="id" itemLabel="name" />
				</form:select> 
			</td>
		</tr>
		<tr>
			<td align="${left}" class="form-left">Accrual Year</td>
			<td>
				<form:select cssClass="flat" path="accrualYear" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${years}"/>
				</form:select>
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3">
				<input type="button" onclick="javascript:searchReport()" value="Preview" />
			</td>
		</tr>
	</table>
</form:form>
	   