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
		var fuelvendor=document.getElementById("fuelvendor");	
		if(fileinfo.value=="") {
			document.getElementById("div_id").innerHTML="Please choose a file to upload!";
		}
		else if(fuelvendor.value == "") {
			alert(fuelvendor.value);
			document.getElementById("div_id").innerHTML="Please choose a fuel vendor!";
		}
		else
		{
		document.forms["overrideForm"].action='${ctx}/uploadData/fuellog/override.do';
		document.forms["overrideForm"].submit();
		}
	}
	
	
	
</script>
<br/>
<form:form action="${ctx}/uploadData/fuellog/upload.do"  enctype="multipart/form-data" modelAttribute="modelObject">
<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
	<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Upload Fuel Log" /></b></td>
			
	</tr>
	<tr>
		<td align="${left}" class="first"><primo:label code="Name"/></td>
		<td align="${left}">
			<select id="fuelvendor" name="fuelvendor" style="min-width:200px; max-width:200px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${fuelvendor}" var="fuelvendor">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['fuelvendor'] == fuelvendor.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${fuelvendor.id}" ${selected}>${fuelvendor.name}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td align="${left}" class="first"><primo:label code="Upload Fuel log" />
		<td align="${left}">
			<input type="file" id="uploadfile" name="dataFile" onchange="return checkfileoverride()"/>
		</td>
	</tr>
	<tr>
		<td><input type="submit" value="Upload"/></td>
	</tr>
</table>
</form:form>
<c:if test="${not empty errorList}">
	<b>Following Fuel Logs are in Error :</b><br/>
	<c:forEach var="item" items="${errorList}">
		<FONT color=#F2290F><STRONG>${item}</strong></FONT>
	</c:forEach>
	
 <form:form action="${ctx}/uploadData/fuellog/override.do" name="overrideForm"  enctype="multipart/form-data" modelAttribute="modelObject">
<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
	<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Above Fuel Log have error,if you want to Override,please select the file" /></b></td>
    </tr>
	<tr>
		<td><primo:label code="Upload Fuellog" />
		<input type="file" id="uploadfile1" name="dataFile" onchange="return checkfileoverride()"/></td>
	</tr>
	<tr>
		<td><input type="button" value="Override" onclick="javascript:submitform();"/></td>
	</tr>
</table>
</form:form>
<div id="div_id" style="font-size:1.2em;font-weight: bold; color:red"></div>
</c:if>

