<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function checkFile() {
		var fileName = document.getElementById("dataFile").value;
		var fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
		fileExt = fileExt.toLowerCase();
		
		var validExtensions = "wmv, mp4";
		if (validExtensions.indexOf(fileExt) == -1) {    
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
			document.getElementById("uploadAccidentVideoErrorDiv").innerHTML="Please choose a file to upload!";
		} else {
			document.forms["uploadAccidentVideoForm"].submit();
		}
	}
</script>
<br/>
<form:form action="${ctx}/admin/accident/accidentmaint/uploadvideo/save.do" method="post" name="uploadAccidentVideoForm" enctype="multipart/form-data" modelAttribute="modelObject">
<form:hidden path="id" id="id" />
<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
	<tr class="table-heading">
		<td colspan="4"><b><primo:label code="Upload Accident Video" /></b></td>
	</tr>
	<tr>
		<td align="${left}" class="first"><primo:label code="Upload Accident Video" /><span class="errorMessage">*</span>
		<td align="${left}">
			<input type="file" id="dataFile" name="dataFile" onchange="return checkFile()"/>
		</td>
	</tr>
	<tr>
		<td>
			<input type="button" value="Upload" onclick="javascript:submitUploadform();"/>
			&nbsp;
			<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat"
				onClick="document.location.href='${ctx}/admin/accident/accidentmaint/list.do'" />
		</td>
	</tr>
</table>
</form:form>

<div id="uploadAccidentVideoErrorDiv" style="font-size:1.2em;font-weight: bold; color:red"></div>

<c:if test="${not empty errorList}">
	<b>Following errors occured while uploading accident video:</b><br/>
	<c:forEach var="item" items="${errorList}">
		<FONT color=#F2290F><STRONG>${item}</strong></FONT>
	</c:forEach>
</c:if>
