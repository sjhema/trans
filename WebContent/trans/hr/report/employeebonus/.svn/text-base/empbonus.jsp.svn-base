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
			<td colspan="4"><b><primo:label code="Bonus Report" />
			</b></td>
	    </tr>
	    
	     <tr>
		    <td class="form-left"><primo:label code="Employee" /><span class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="driver" multiple="true">
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
			
			<td class="form-left"><primo:label code="Terminal" /><span class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="terminal" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select>
			</td>
	   </tr>
	   
	  
	  
	   <tr>
	      <td class="form-left"><primo:label code="Bonus Type" /><span class="errorMessage"></span></td>
			<td><form:select cssClass="flat" path="bonustype" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${bonustypes}" itemValue="id" itemLabel="typename" />
				</form:select>
			</td>
	      
	      
	      <td class="form-left"><primo:label code="Employee No." /><span class="errorMessage"></span></td>
			<td  align="${left}">
				<form:input size="10" path="employeeNo" cssClass="flat"/> 
				
			</td> 
	   </tr>
		
		 <tr>
	          <td class="form-left"><primo:label code="Batch Date From" /></td>
	          <td align="${left}"><input name="batchFrom" type="text" id="fromDate" size="15"
				value="${sessionScope.searchCriteria.searchMap.fromDate}" "/>
				<script type="text/javascript">
			$(function() {
			$("#fromDate").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script>
		    </td>
		  
	          <td class="form-left"><primo:label code="Batch Date To" /></td>
	          <td align="${left}"><input name="batchto" type="text" id="toDate" size="15"
				value="${sessionScope.searchCriteria.searchMap.toDate}" "/>
				<script type="text/javascript">
			$(function() {
			$("#toDate").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>
		    
	   </tr>
	   
		
		
		
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3"><input type="button"
				onclick="javascript:searchReport()" value="Preview" /></td>
		</tr>
		</table>
</form:form>
				