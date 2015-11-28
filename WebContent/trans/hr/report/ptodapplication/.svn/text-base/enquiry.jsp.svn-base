<%@ include file="/common/taglibs.jsp"%>
<script language="javascript">
function searchReport() {
	document.forms[0].submit();
}
$(document).ready(function(){
	   $("select").multiselect();
});
</script>

<form:form action="search.do" name="searchForm" method="post" commandName="modelObject">
	<br/>
	<br/>
	
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="PTOD Application Report" />
			</b></td>
	    </tr>
	    <tr>
	    <td class="form-left"><primo:label code="Employee" /><span class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="employees" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="fullName" />
				</form:select> 
			</td>
	    
	    <td class="form-left"><primo:label code="Category" /><span class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="category" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${categories}" itemValue="id" itemLabel="name" />
				</form:select>
			</td>		
		 </tr>
		<tr>
		 <td class="form-left"><primo:label code="Company" /><span class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="company" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> 
			</td>
		
			  <td class="form-left"><primo:label code="Leave Type" /><span
				class="errorMessage"></span></td>
		<td><form:select cssClass="flat" path="leaveType"  multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${leavetypes}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="leaveType" cssClass="errorMessage" />
			</td>			
			
		</tr>
		
	   <tr>
	 <td class="form-left"><primo:label code="Terminal" /><span class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="terminal" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select>
			</td>
		</tr>
		<tr>
				<td align="${left}" class="form-left">Pay Roll Batch Date From</td>
			<td align="${left}"><form:input path="payRollBatchFrom"
					cssClass="flat"  id="datepicker" onblur="return formatDate();" /></td>
					<td align="${left}" class="form-left">Pay Roll Batch Date To</td>
				<td align="${left}"><form:input path="payRollBatchto" cssClass="flat"  id="datepicker1" onblur="return formatDate();" /> </td>
		</tr>
		 <tr>
	   <td class="form-left"><primo:label code="Approve Status" /><span
				class="errorMessage"></span></td>
		<td><form:select cssClass="flat" path="approvestatus"  multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${approvestatuss}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="approvestatus" cssClass="errorMessage" />
			</td>
		</tr>
		  <tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3"><input type="button"
				onclick="javascript:searchReport()" value="Preview" /></td>
		</tr>
	   </table>
	   
	   </form:form>
	   