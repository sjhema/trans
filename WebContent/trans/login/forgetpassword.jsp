<%@include file="/common/taglibs.jsp"%>
<table width="100%">
	<tr><td align="${left}" class="table-head"><primo:label code="Reset Password"/></td></tr>
</table>
<form action="${ctx}/login/resetpassword.do" method="post">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="2"><b><primo:label code="Reset Password"/></b></td>
		</tr>
		<tr><td align="${left}" colspan="2" class="errorMessage"><%if(session.getAttribute("error")!=null) {out.println(session.getAttribute("error"));session.removeAttribute("error"); }%></td></tr>
		<tr>
			<td class="form-left"><primo:label code="UserId" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><input type="text" cssClass="flat" name="userid"/> <br> </td>
		</tr>
		<tr>
			<td align="${left}" colspan="2"> <input
				type="submit" id="resetBtn" value="<primo:label code="Reset"/>" class="flat" />
			</td>
		</tr>                 
	</table>
</form>