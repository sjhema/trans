<%@include file="/common/taglibs.jsp"%>

<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="4"><b><primo:label code="Add/Update Time"/></b></td>
		</tr>
		
		<tr>
		      <td class="form-left" ><primo:label code="Hours Format" /><span class="errorMessage">*</span></td>
		     <td align="${left}"><form:input id="hoursformat" path="hoursformat" cssClass="flat"  style="width:100px"/> <br> 
			    <form:errors path="hoursformat" cssClass="errorMessage" />
	     </td>
		</tr>
		
		<tr>
		      <td class="form-left" ><primo:label code="HoursValue" /><span class="errorMessage">*</span></td>
		     <td align="${left}"><form:input id="hoursvalues" path="hoursvalues" cssClass="flat"/> <br> 
			    <form:errors path="hoursvalues" cssClass="errorMessage" />
	     </td>
		</tr>
		<tr>
		      <td class="form-left" ><primo:label code="Minutes" /><span class="errorMessage"></span></td>
		     <td align="${left}"><form:input id="minutes" path="minutes" cssClass="flat" style="width:100px"/> <br> 
			    <form:errors path="minutes" cssClass="errorMessage" />
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
	
