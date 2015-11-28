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
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Employee Report" />
			</b></td>
	    </tr>
	    
	     <tr>
		    <td class="form-left"><primo:label code="Employee" /></td>
			<td><form:select cssClass="flat" path="driver" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="fullName" />
				</form:select> 
			</td>
			
			<td class="form-left"><primo:label code="Category" /></td>
			<td><form:select cssClass="flat" path="category" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${categories}" itemValue="id" itemLabel="name" />
				</form:select>
			</td>
		  </tr>
		
		<tr>
		    <td class="form-left"><primo:label code="Company" /></td>
			<td><form:select cssClass="flat" path="company" multiple="true">
					<form:option value="-1">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> 
			</td>
			
			<td class="form-left"><primo:label code="Terminal" /></td>
			<td><form:select cssClass="flat" path="terminal" multiple="true">
					<form:option value="-1">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select>
			</td>
	   </tr>
	   
	  
	   <tr>
	          <td class="form-left"><primo:label code="Hired date From" /></td>
	          <td align="${left}"><input name="dateHiredfrom" type="text" id="dateHiredfrom" size="15"
				value="${sessionScope.searchCriteria.searchMap.dateHiredfrom}" "/>
				<script type="text/javascript">
			$(function() {
			$("#dateHiredfrom").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script>
		    </td>
		  
	          <td class="form-left"><primo:label code="Hired Date To" /></td>
	          <td align="${left}"><input name="dateHiredto" type="text" id="dateHiredto" size="15"
				value="${sessionScope.searchCriteria.searchMap.dateHiredto}" "/>
				<script type="text/javascript">
			$(function() {
			$("#dateHiredto").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>
		    
	   </tr>
	   <tr>
		<td class="form-left"><primo:label code="Status" /></td>
		<td><form:select cssClass="flat" path="status" multiple="true">
		            <form:option value="-1">--<primo:label code="Please Select" />--</form:option>
					<form:options items="${employeestatus}" itemValue="dataValue" itemLabel="dataText" />
				</form:select>
			</td>
		</tr>
		
		
		
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3"><input type="button"
				onclick="javascript:searchReport()" value="Preview" /></td>
		</tr>
		</table>
</form:form>
				