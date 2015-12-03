<%@include file="/common/taglibs.jsp"%>
<script>
function emailValidate(){
	if(document.getElementById("notificationType").value=="Email"){
		if(document.getElementById("email").value==""){
		 alert("Please Enter your Email ID.");
		 return false;
		 }
		return true;
	}
	return true;
}
</script>
<h3><primo:label code="Add/Update User"/></h3>
 <form:form action="save.do" name="userForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id"/>
    <table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="2"><b><primo:label code="Add/Update User"/></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="First Name"/><span
				class="errorMessage">*</span></td>
			<td align="${left}">
			<!--<form:input path="firstName" cssClass="flat" style="min-width:200px; max-width:200px" maxlength="20"/>-->
			<form:select cssClass="flat form-control input-sm" style="min-width:200px; max-width:200px" maxlength="20" path="firstName" >
				<form:option value="">----Please Select----</form:option>
				<form:options items="${employeesFirstName}"/>
			</form:select>
			<br><form:errors path="firstName" cssClass="errorMessage"/></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Last Name"/><span
				class="errorMessage">*</span></td>
			<td align="${left}">
			<!--<form:input path="lastName" cssClass="flat" style="min-width:200px; max-width:200px" maxlength="20"/>-->
			<form:select cssClass="flat form-control input-sm" style="min-width:200px; max-width:200px" maxlength="20" path="lastName" >
				<form:option value="">----Please Select----</form:option>
				<form:options items="${employeesLastName}"/>
			</form:select>
			<br><form:errors path="lastName" cssClass="errorMessage"/></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="User Name"/><span
				class="errorMessage">*</span></td>
			<td align="${left}"><c:if test="${empty modelObject.id}"><form:input path="username" cssClass="flat" style="min-width:200px; max-width:200px" maxlength="20"/></c:if>
			<c:if test="${not empty modelObject.id}"><form:input path="username" cssClass="flat" readonly="true" style="min-width:200px; max-width:200px" maxlength="20"/></c:if>
			<br><form:errors path="username" cssClass="errorMessage"/></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Short Name"/><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="name" cssClass="flat" style="min-width:200px; max-width:200px" maxlength="20"/>
			<br><form:errors path="name" cssClass="errorMessage"/></td>
		</tr>
		<c:if test="${empty modelObject.id}">
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
		</c:if>
		<tr>
			<td class="form-left"><primo:label code="Contact Number"/><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="phoneNumber" cssClass="flat" style="min-width:200px; max-width:200px" maxlength="20"/>
			<br><form:errors path="phoneNumber" cssClass="errorMessage"/></td>
		</tr>
        <tr>
			<td class="form-left"><primo:label code="Email"/></td>
			<td align="${left}"><form:input path="email" cssClass="flat" style="min-width:200px; max-width:200px"/>
			<br><form:errors path="email" cssClass="errorMessage"/></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Mobile"/></td>
			<td align="${left}"><form:input path="mobileNo" id="mobileNo" cssClass="flat" maxlength="15" style="min-width:200px; max-width:200px"/>
			<br><form:errors path="mobileNo" cssClass="errorMessage"/></td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Role"/></td>
		<td align="${left}">
			<form:select path="role" style="min-width:204px; max-width:204px">
				<form:option value="" label="--Please Select--"/>
				<form:options items="${roles}" itemValue="id" itemLabel="name"/>
			</form:select>
		</td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Status"/></td>
		<td align="${left}">
			<form:select path="status">
				<c:forEach var="item" items="${statuses}">
					<form:option value="${item.dataValue}" label="${item.dataText}"/>
				</c:forEach>
			</form:select>
		</td>
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
             
