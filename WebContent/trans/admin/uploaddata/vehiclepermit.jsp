<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function checkfile(){
		var FileExt = document.getElementById("uploadfile").value.lastIndexOf(".xls");
		if(
		   FileExt==-1) {    
			alert("This File Is Not Allowed"); 
			document.getElementById("uploadfile").value="";   
			return false;
		}else{
			return true;
		}
	}
</script>
<br/>
<form:form action="${ctx}/uploadData/vehiclepermit/upload.do" method="post" enctype="multipart/form-data" modelAttribute="modelObject">
<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
	<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Upload Permit" /></b></td>
	</tr>
	<tr>
		<td><primo:label code="Upload Vehicle Permits" />
		<input type="file" id="uploadfile" name="dataFile" onchange="return checkfile()"/></td>
	</tr>
	<tr>
		<td><input type="submit" value="Upload"/></td>
	</tr>
</table>
</form:form>
<c:if test="${not empty errorList}">
<br/>
	<b>Following <i>Permits</i> are in Error:</b><br/><br/>
	<c:forEach var="item" items="${errorList}">
		<font color="red"><b>${item}</b></font>
	</c:forEach>
</c:if>