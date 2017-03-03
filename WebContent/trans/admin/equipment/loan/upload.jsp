<%@include file="/common/taglibs.jsp"%>

<style>
 .ui-datepicker-calendar {
     display: none;
  }
</style>

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
	
	function submitUploadForm() {	
		var dataFile = document.getElementById("dataFile");
		if (dataFile.value == "") {
			document.getElementById("vehicleLoanErrorDiv").innerHTML="Please choose a file to upload!";
		} else {
			document.forms["vehicleLoanUploadForm"].submit();
		}
	}
</script>

<br/>
<form:form action="${ctx}/admin/equipment/loan/upload.do" method="post" name="vehicleLoanUploadForm" enctype="multipart/form-data" modelAttribute="modelObject">
<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
	<tr class="table-heading">
		<td colspan="4"><b><primo:label code="Upload Vehicle Loan" /></b></td>
	</tr>
	<tr>
		<td align="${left}" class="first"><primo:label code="Upload Vehicle Loan" /><span class="errorMessage">*</span>
		<td align="${left}">
			<input type="file" id="dataFile" name="dataFile" onchange="return checkfile()"/>
		</td>
	</tr>
	<tr>
		<td><input type="button" value="Upload" onclick="javascript:submitUploadForm();"/></td>
	</tr>
</table>
</form:form>

<div id="vehicleLoanErrorDiv" style="font-size:1.2em;font-weight: bold; color:red"></div>

<c:if test="${not empty errorList}">
	<b>Following Vehicle Loan records are in Error:</b><br/>
	<c:forEach var="item" items="${errorList}">
		<FONT color=#F2290F><STRONG>${item}</strong></FONT>
	</c:forEach>
</c:if>
