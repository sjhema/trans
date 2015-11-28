<%@include file="/common/taglibs.jsp"%>

<h3><primo:label code="Reset Password"/></h3>
 <form:form action="updatenewpassword.do" name="userForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id"/>
    <table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="2"><b><primo:label code="Reset Password"/></b></td>
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="User Name"/><span
				class="errorMessage">*</span></td>
			<td align="${left}">
			<c:if test="${not empty modelObject.id}"><form:input path="username" cssClass="flat" readonly="true" style="min-width:200px; max-width:200px" maxlength="20"/></c:if>
			<br><form:errors path="username" cssClass="errorMessage"/></td>
		</tr>
		
	
		<tr>
			<td class="form-left"><primo:label code="Password"/><span
				class="errorMessage">*</span></td>
			<td align="${left}">
				<form:password path="password" cssClass="flat" style="min-width:200px; max-width:200px" maxlength="20"/>
			<br><form:errors path="password" cssClass="errorMessage"/></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Confirm Password"/><span
				class="errorMessage">*</span></td>
			<td align="${left}">
				<form:password path="confirmPassword" cssClass="flat" style="min-width:200px; max-width:200px" maxlength="20"/>
			<br><form:errors path="confirmPassword" cssClass="errorMessage"/></td>
		</tr>

	
		<tr><td colspan="2"></td></tr>
		<tr>
			<td></td>
			<td align="${left}"><input type="submit" name="create" id="create" onclick="return emailValidate();"
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>" class="flat" />
				<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat" onClick="location.href='list.do'"/>
			</td>
		</tr>                 
     </table>
 </form:form>
             
