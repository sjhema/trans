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
			document.getElementById("subcontractorrateUploadErrorDiv").innerHTML="Please choose a file to upload!";
		} else {
			document.forms["subcontractorrateUploadForm"].submit();
		}
	}
	
	function formatDate(datePickerId){
		var date=document.getElementById(datePickerId).value;
		if(date!=""){
		if(date.length<8){
			alert("Invalidte date format");
			document.getElementById(datePickerId).value="";
			return true;
		}
		else{
			var str=new String(date);
			if(!str.match("-")){
				var mm=str.substring(0,2);
				var dd=str.substring(2,4);
				var yy=str.substring(4,8);
				var enddigit=str.substring(6,8);
				if(mm>12){
					alert("invalid date format");
					document.getElementById(datePickerId).value="";
					return true;
				}
				if(!enddigit==00 && enddigit%4==0 ){
					if(mm==04 || mm==06 || mm==09 || mm==11){
						if(dd>30){
							alert("invalid date format");
							document.getElementById(datePickerId).value="";
							return true;
						}
					}if(mm==02 && dd>29){
						alert("invalid date format");
						document.getElementById(datePickerId).value="";
						return true;
					}
					else if(dd>31){
						alert("invalid date format");
						document.getElementById(datePickerId).value="";
						return true;
					}
				}if(enddigit==00 && yy%400==0){
					if(mm==04 || mm==06 || mm==09 || mm==11){
						if(dd>30){
							alert("invalid date format");
							document.getElementById(datePickerId).value="";
							return true;
						}
					}if(mm==02 && dd>29){
						alert("invalid date format");
						document.getElementById(datePickerId).value="";
						return true;
					}else if(dd>31){
						alert("invalid date format");
						document.getElementById(datePickerId).value="";
						return true;
					}					
				}else{
					if(mm==04 || mm==06 || mm==09 || mm==11){
						if(dd>30){
							alert("invalid date format");
							document.getElementById(datePickerId).value="";
							return true;
						}
					}if(mm==02 && dd>28){
						alert("invalid date format");
						document.getElementById(datePickerId).value="";
						return true;
					}else if(dd>31){
						alert("invalid date format");
						document.getElementById(datePickerId).value="";
						return true;
					}
				}
				var date=mm+"-"+dd+"-"+yy;
				document.getElementById(datePickerId).value=date;
			}
		 }
	   }
	}
</script>
<br/>
<form:form action="${ctx}/uploadData/subcontractorrate/upload.do" method="post" name="subcontractorrateUploadForm" enctype="multipart/form-data" modelAttribute="modelObject">
<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
	<tr class="table-heading">
		<td colspan="4"><b><primo:label code="Upload Subcontractor Rate" /></b></td>
	</tr>
	<tr>
		<td class="form-left"><primo:label code="Valid From" /><span class="errorMessage">*</span></td>
		<td align="${left}">
			<input id="datepicker1" name="validFrom" class="flat" style="min-width:150px; max-width:150px" onblur="return formatDate('datepicker1');" value="" type="text">
		</td>
	</tr>
	<tr>
		<td class="form-left"><primo:label code="Valid To" /><span class="errorMessage">*</span></td>
		<td align="${left}">
			<input id="datepicker2" name="validTo" class="flat" style="min-width:150px; max-width:150px" onblur="return formatDate('datepicker2');" value="" type="text">
		</td>
	</tr>
	<tr>
		<td align="${left}" class="first"><primo:label code="Upload Subcontractor Rate" /><span class="errorMessage">*</span>
		<td align="${left}">
			<input type="file" id="dataFile" name="dataFile" onchange="return checkfile()"/>
		</td>
	</tr>
	<tr>
		<td><input type="button" value="Upload" onclick="javascript:submitUploadform();"/></td>
	</tr>
</table>
</form:form>

<div id="subcontractorrateUploadErrorDiv" style="font-size:1.2em;font-weight: bold; color:red"></div>

<c:if test="${not empty errorList}">
	<b>Following Subcontractor Rate records are in Error, rest are loaded successfully</b><br/>
	<c:forEach var="item" items="${errorList}">
		<FONT color=#F2290F><STRONG>${item}</strong></FONT>
	</c:forEach>
</c:if>
