<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function checkFile(dataFileId) {
		var dataFileElem = document.getElementById(dataFileId);
		var fileExt = dataFileElem.value.lastIndexOf(".xls");
		if (fileExt == -1) {    
			alert("This File Is Not Allowed"); 
			dataFileElem.value="";   
			return false;
		} else {
			return true;
		}
	}
	
	function upload(action, dataFileId) {	
		var dataFileElem = document.getElementById(dataFileId);
		if (dataFileElem.value == "") {
			document.getElementById("injuryUploadErrorDiv").innerHTML="Please choose a file to upload!";
			return;
		} 
		
		var form = $('#injuryUploadForm');
		var url = "${ctx}/uploadData/injury/" + action;
		form.attr('action', url);
		form.submit();
	}
</script>
<br/>
<form:form action="${ctx}/uploadData/injury/uploadMain.do" method="post" name="injuryUploadForm" id="injuryUploadForm" enctype="multipart/form-data" modelAttribute="modelObject">
<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
	<tr class="table-heading">
		<td colspan="4"><b><primo:label code="Upload Injury" /></b></td>
	</tr>
	<tr>
		<td align="${left}" class="first"><primo:label code="Upload Injury Main" /><span class="errorMessage">*</span></td>
		<td align="${left}">
			<input type="file" id="dataFileMain" name="dataFileMain" onchange="return checkFile('dataFileMain')"/>
		</td>
	</tr>
	<tr>
		<td align="${left}" class="first"><primo:label code="Upload Reported Injury" /><span class="errorMessage">*</span></td>
		<td align="${left}">
			<input type="file" id="dataFileReported" name="dataFileReported" onchange="return checkFile('dataFileReported')"/>
		</td>
	</tr>
	<tr>
		<td align="${left}" class="first"><primo:label code="Upload Not Reported Injury" /><span class="errorMessage">*</span></td>
		<td align="${left}">
			<input type="file" id="dataFileNotReported" name="dataFileNotReported" onchange="return checkFile('dataFileNotReported')"/>
		</td>
	</tr>
	<tr>
		<td colspan="3">
			<input type="button" value="Upload Main" onclick="javascript:upload('uploadMain.do', 'dataFileMain');"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" onclick="javascript:upload('uploadReported.do', 'dataFileReported')" value="Upload Reported" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" onclick="javascript:upload('uploadNotReported.do', 'dataFileNotReported')" value="Upload Not Reported" />
		</td>
	</tr>
</table>
</form:form>

<div id="injuryUploadErrorDiv" style="font-size:1.2em;font-weight: bold; color:red"></div>

<c:if test="${not empty errorList}">
	<b>Following Injury records are in Error, rest are loaded successfully</b><br/>
	<c:forEach var="item" items="${errorList}">
		<FONT color=#F2290F><STRONG>${item}</strong></FONT>
	</c:forEach>
</c:if>
