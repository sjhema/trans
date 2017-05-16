<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function checkfile() {
		var fileExt = document.getElementById("dataFile").value.lastIndexOf(".xls");
		if (fileExt == -1) {    
			alert("This File Is Not Allowed"); 
			document.getElementById("dataFile").value="";   
			return false;
		} else {
			return true;
		}
	}
	
	function submitUploadform() {	
		var dataFile = document.getElementById("dataFile");
		
		if (dataFile.value == "") {
			document.getElementById("employeeUploadErrorDiv").innerHTML="Please choose a file to upload!";
		} else {
			document.forms["employeeUploadForm"].submit();
		}
	}
</script>
<br/>
<form:form action="${ctx}/uploadData/employee/upload.do" method="post" name="employeeUploadForm" enctype="multipart/form-data" modelAttribute="modelObject">
<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
	<tr class="table-heading">
		<td colspan="4"><b><primo:label code="Upload Employee" /></b></td>
	</tr>
	<tr>
		<td align="${left}" class="first"><primo:label code="Upload Employee" /><span class="errorMessage">*</span>
		<td align="${left}">
			<input type="file" id="dataFile" name="dataFile" onchange="return checkfile()"/>
		</td>
	</tr>
	<tr>
		<td><input type="button" value="Upload" onclick="javascript:submitUploadform();"/></td>
	</tr>
</table>
</form:form>

<div id="employeeUploadErrorDiv" style="font-size:1.2em;font-weight: bold; color:red"></div>

<c:if test="${not empty errorList}">
	<b>Following Employee records are in Error, rest are loaded successfully</b><br/>
	<c:forEach var="item" items="${errorList}">
		<FONT color=#F2290F><STRONG>${item}</strong></FONT>
	</c:forEach>
</c:if>
