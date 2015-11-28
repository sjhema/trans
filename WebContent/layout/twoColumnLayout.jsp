<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Trans - Reporting System</title>
<%@include file="/common/css.jsp"%>
<%@include file="/common/scripts.jsp"%>
<decorator:head/>
</head>
<body dir="${dir}">
<table width="100%" cellpadding="0" cellspacing="0" border="0" style="min-height:100%;height:100%">
	<tr>
		<td colspan="2" height="75px"><jsp:include page="header.jsp" /></td>
	</tr>
	<tr>
		<td colspan="2" height="25px" ><jsp:include page="topMenu.jsp" /></td>
	</tr>
	<tr>
		<td width="200" valign="top" class="left-col"><jsp:include page="leftMenu.jsp" /></td>
		<td  valign="top"><div style="padding:10px"><jsp:include page="/common/messages.jsp" /> <decorator:body /></div>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="footer"><jsp:include page="footer.jsp" /></td>
	</tr>
</table>
</body>
</html>