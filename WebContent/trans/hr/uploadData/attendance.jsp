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
	
	/* function checkfileoverride(){
		var FileExt = document.getElementById("uploadfile1").value.lastIndexOf(".xls");
		if(
		   FileExt==-1) {    
			alert("This File Is Not Allowed"); 
			document.getElementById("uploadfile1").value="";   
			return false;
		}else{
			return true;
		}
	} */
</script>

<form:form action="${ctx}/hr/uploadData/attendance/upload.do"  enctype="multipart/form-data" modelAttribute="modelObject">
<br/>
<br/>
<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
	<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Upload Attendance" /></b></td>
			
	</tr>
	<tr>
		<td><primo:label code="Upload Attendance" />
		<input type="file" id="uploadfile" name="dataFile" onchange="return checkfileoverride()"/></td>
	</tr>
	<tr>
		<td><input type="submit" value="Upload"/></td>
	</tr>
</table>
</form:form>
<c:if test="${not empty errorList}">
	<b>Following Attendance are in Error :</b><br/>
	<c:forEach var="item" items="${errorList}">
		<FONT color=#F2290F><STRONG>${item}</strong></FONT>
	</c:forEach>
	
 <%-- <form:form action="${ctx}/uploadData/fuellog/override.do"  enctype="multipart/form-data" modelAttribute="modelObject">
<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
	<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Above fuellog have error,if you want to Override,please select the file" /></b></td>
    </tr>
	<tr>
		<td><primo:label code="Upload Fuellog" />
		<input type="file" id="uploadfile1" name="dataFile" onchange="return checkfileoverride()"/></td>
	</tr>
	<tr>
		<td><input type="submit" value="Override"/></td>
	</tr>
</table>
</form:form> --%>

</c:if>

