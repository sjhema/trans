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
	
	
	function checkfileoverride(){
		var FileExt = document.getElementById("uploadfile1").value.lastIndexOf(".xls");
		if(
		   FileExt==-1) {    
			alert("This File Is Not Allowed"); 
			document.getElementById("uploadfile1").value="";   
			return false;
		}else{
			return true;
		}
	}
	
	
	function submitform(){	
		var fileinfo=document.getElementById("uploadfile1");
		var tollCompany = document.getElementById("tollcompany");
		if(fileinfo.value==""){
			document.getElementById("div_id").innerHTML="Please choose a file to upload!";
		} else if(tollCompany.value == "") {
			document.getElementById("div_id").innerHTML="Please choose a Toll Company!";
		} else {
			document.forms["overrideForm"].action='${ctx}/uploadData/eztoll/override.do';
			document.forms["overrideForm"].submit();
		}
	}
	
	function submitUploadform() {	
		var fileinfo = document.getElementById("uploadfile");
		var tollCompany = document.getElementById("tollcompany");
		if(fileinfo.value=="") {
			document.getElementById("div_id").innerHTML="Please choose a file to upload!";
		} else if(tollCompany.value == "") {
			document.getElementById("div_id").innerHTML="Please choose a Toll Company!";
		} else {
			document.forms["uploadForm"].submit();
		}
	}
</script>
<br/>
<form:form action="${ctx}/uploadData/eztoll/upload.do" method="post" name="uploadForm" enctype="multipart/form-data" modelAttribute="modelObject">
<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
	<tr class="table-heading">
		<td colspan="4"><b><primo:label code="Upload Tolls" /></b></td>
	</tr>
	<tr>
		<td align="${left}" class="first">
			<primo:label code="Toll Company"/>
		</td>
		<td align="${left}">
			<select id="tollcompany" name="tollcompany" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${tollcompanies}" var="tollcompany">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['tollcompany'] == tollcompany.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
					<option value="${tollcompany.id}" ${selected}>${tollcompany.name}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td align="${left}" class="first"><primo:label code="Upload Tolls" />
		<td align="${left}">
			<input type="file" id="uploadfile" name="dataFile" onchange="return checkfile()"/>
		</td>
	</tr>
	<tr>
		<td><input type="button" value="Upload" onclick="javascript:submitUploadform();"/></td>
	</tr>
</table>
</form:form>

<div id="div_id" style="font-size:1.2em;font-weight: bold; color:red"></div>

<c:if test="${not empty errorList}">
	<b>Following Tolls are in Error :</b><br/>
	<c:forEach var="item" items="${errorList}">
		<%-- ${item} --%>
		<FONT color=#F2290F><STRONG>${item}</strong></FONT>
	</c:forEach>
	
<form:form action="${ctx}/uploadData/eztoll/override.do" name="overrideForm"  enctype="multipart/form-data" modelAttribute="modelObject">
<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
	<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Above Tolls have error,if you want to Override,please select the file" /></b></td>
    </tr>
	<tr>
		<td><primo:label code="Upload Tolls" />
		<input type="file" id="uploadfile1" name="dataFile" onchange="return checkfileoverride()"/></td>
	</tr>
	<tr>
		<td><input type="button" value="Override" onclick="javascript:submitform();"/></td>
	</tr>
</table>
</form:form>
</c:if>