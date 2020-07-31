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
			<td colspan="4"><b><primo:label code="Employee Probation Report" />
			</b></td>
	    </tr>
	    <tr>
		    <td class="form-left"><primo:label code="Employee" /><span class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="driver" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${employees}" itemValue="fullName" itemLabel="fullName" />
				</form:select> 
			</td>
			<td align="${left}" class="first"><primo:label code="SSN"/></td>
			<td>
				<input id="ssn" name="ssn" type="text" value="${sessionScope.searchCriteria.searchMap.ssn}"/>
			</td>
			
		 </tr>
		 <tr>
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
		<td class="form-left"><primo:label code="Terminal" /><span class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="terminal" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select>
			</td>
		
		</tr>	
		
	 
		<tr>
				<td align="${left}" class="form-left">Probation Start Date</td>
			<td align="${left}"><form:input path="dateProbationStart"
					cssClass="flat"  id="datepicker" onblur="return formatDate();" /></td>
					<td align="${left}" class="form-left">Probation End Date</td>
				<td align="${left}"><form:input path="dateProbationEnd" cssClass="flat"  id="datepicker1" onblur="return formatDate();" /> </td>
		</tr>
		  <tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3"><input type="button"
				onclick="javascript:searchReport()" value="Preview" /></td>
		</tr>
	   </table>
	   
	   </form:form>
	   