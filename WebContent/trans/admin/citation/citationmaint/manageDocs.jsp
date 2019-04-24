<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function checkfile() {
		var fileElem = document.getElementById("dataFile");
		var file = fileElem.value;
		var fileExt = file.lastIndexOf(".pdf");
		var commaIndx = file.indexOf(",");
		if (fileExt == -1) {    
			alert("Only pdf file is allowed"); 
			fileElem.value = "";   
			return false;
		} else if (commaIndx != -1) {    
			alert("File name cannot have commas"); 
			fileElem.value = "";   
			return false;
		} else {
			return true;
		}
	}
	
	function getForm() {
		return document.forms["manageDocsForm"];
	}
	
	function processUpload() {
		var fileElem = document.getElementById("dataFile");
		var noOfFiles = fileElem.files.length;
		if (noOfFiles == 0) {
			document.getElementById("manageDocsErrorDiv").innerHTML = "Please choose a file to upload!";
			return;
		}
		
		var file = fileElem.files.item(0).name;
		var id = document.getElementById("id").value;
		$.ajax({
	  		url: "../ajax.do?action=doesDocExist" + "&id=" + id + "&file=" + file,
	       	type: "GET",
	       	success: function(responseData, textStatus, jqXHR) {
	       		if (responseData == 'true') {
	       			if (confirm("Do you want to replace the pdf already uploaded?")) {
	       				uploadDoc(file);
	           		}
	       		} else {
	       			uploadDoc(file);
	       		}
			}
		});
	}
	
	function uploadDoc() {
		submitForm("uploaddoc.do");
	}
	
	function processDownload() {
		if (!validateCheckedDocs()) {
			return;
		}
		
		submitForm("downloaddoc.do");
	}
	
	function processDelete() {
		if (!validateCheckedDocs()) {
			return;
		}
		
		if (confirm("Do you want to Delete the selected pdf?")) {
			submitForm("deletedoc.do");
   		}
	}
	
	function validateCheckedDocs() {
		var checkedCount = 0;
		var checkedDocs = document.getElementsByName("fileList");
		for (var i = 0; i < checkedDocs.length; i++) {
			if (checkedDocs[i].checked) {
				checkedCount++;
			}
		}
		if (checkedCount == 0) {
			alert("Select a pdf");
			return false;
		}
		if (checkedCount > 1) {
			alert("Select only one pdf");
			return false;
		}
		
		return true;
	}
	
	function submitForm(action) {
		var form = getForm();
		form.action = action;
		form.submit();
	}
</script>
<br/>
<form:form action="${ctx}/admin/citation/citationmaint/managedocs/save.do" method="post" name="manageDocsForm" enctype="multipart/form-data" modelAttribute="modelObject">
<form:hidden path="id" id="id" />
<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
	<tr class="table-heading">
		<td colspan="4"><b><primo:label code="Manage Citation Pdf" /></b></td>
	</tr>
	<tr>
		<td align="${left}" class="first"><primo:label code="Uploaded Files" /><span class="errorMessage"></span>
		<td align="${left}"><form:checkboxes items="${fileList}" path="fileList" /></td>
		<td><form:errors path="fileList" cssClass="error" /></td>
	</tr>
	<tr>
		<td>
			<input type="button" id="downloadBtn" value="<primo:label code="Download"/>" class="flat"
				onclick="javascript:processDownload();"/>
			&nbsp;
			<input type="button" id="deleteBtn" value="<primo:label code="Delete"/>" class="flat"
				onclick="javascript:processDelete();" />
		</td>
	</tr>
	<tr class="table-heading">
		<td colspan="6"><b><primo:label code="Upload Citation Pdf" /></b></td>
	</tr>
	<tr>
		<td align="${left}" class="first"><primo:label code="Upload Citation Pdf" /><span class="errorMessage"></span>
		<td align="${left}">
			<input type="file" id="dataFile" name="dataFile" onchange="return checkfile()"/>
		</td>
	</tr>
	<tr>
		<td>
			<input type="button" value="Upload" onclick="javascript:processUpload();"/>
			&nbsp;
			<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat"
				onClick="document.location.href='${ctx}/admin/citation/citationmaint/list.do'" />
		</td>
	</tr>
</table>
</form:form>

<div id="manageDocsErrorDiv" style="font-size:1.2em;font-weight: bold; color:red"></div>

<c:if test="${not empty errorList}">
	<b>Following errors occured while managing citation docs:</b><br/>
	<c:forEach var="item" items="${errorList}">
		<FONT color=#F2290F><STRONG>${item}</strong></FONT>
	</c:forEach>
</c:if>
