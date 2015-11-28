<%@include file="/common/taglibs.jsp"%>
<h3><primo:label code="Change Password"/></h3>
<form:form action="${ctx}/operator/login/updatepassword.do" name="passwordForm" commandName="modelObject" method="post">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="2"><b><primo:label code="Change Password"/></b></td>
		</tr>
		<tr><td align="${left}" colspan="2" class="errorMessage"><%if(session.getAttribute("error")!=null) {out.println(session.getAttribute("error"));session.removeAttribute("error"); }%></td></tr>
		<tr>
			<td class="form-left"><primo:label code="Old Password"/><span
				class="errorMessage">*</span></td>
			<td align="${left}"><input type="password" name="oldpassword" Class="flat" Style="width:200px;"/>
			<br></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="New Password"/><span
				class="errorMessage">*</span></td>
			<td align="${left}"><input type="password" name="newpassword" Class="flat" Style="width:200px;"/>
			<br></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Confirm Password"/><span
				class="errorMessage">*</span></td>
			<td align="${left}"><input  type="password" name="confirmpassword" Class="flat" Style="width:200px;"/>
			<br></td>
		</tr>
		<tr>
			<td align="${left}" colspan="2"><input type="submit" name="create" id="create" 
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>" class="flat" />
			</td>
		</tr>                 
	</table>
</form:form>