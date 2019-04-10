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
	
	function submitUploadform(source) {	
		var period = document.getElementById("period");
		var dataFile = document.getElementById("dataFile");
		var resetMiles = document.getElementById("resetMiles");
		
		var mileageErrorDiv = document.getElementById("mileageErrorDiv");
		if (period.value == "") {
			mileageErrorDiv.innerHTML="Please choose a period for upload!";
		} else if (dataFile.value == "") {
			mileageErrorDiv.innerHTML="Please choose a file to upload!";
		} else if (resetMiles.value == "") {
			mileageErrorDiv.innerHTML="Please enter reset miles!";
		} else {
			var sourceElem = document.getElementById("source");
			sourceElem.value = source;
			document.forms["mileageUploadForm"].submit();
		}
	}
	
	$(function() {
		$("#period").datepicker( {
            changeMonth: true,
            changeYear: true,
            showButtonPanel: true,
            dateFormat: 'MM yy',
            onClose: function(dateText, inst) { 
            	var iMonth = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                var iYear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                $(this).datepicker('setDate', new Date(iYear, iMonth, 1));
            },
            beforeShow : function(input, inst) {
            	if ((selDate = $(this).val()).length > 0) {
                   iYear = selDate.substring(selDate.length - 4, selDate.length);
                   iMonth = jQuery.inArray(selDate.substring(0, selDate.length - 5), $(this).datepicker('option', 'monthNames'));
                   $(this).datepicker('option', 'defaultDate', new Date(iYear, iMonth, 1));
                   $(this).datepicker('setDate', new Date(iYear, iMonth, 1));
                }
            }
        });
	  });
</script>

<br/>
<form:form action="${ctx}/uploadData/mileagelog/upload.do" method="post" name="mileageUploadForm" enctype="multipart/form-data" modelAttribute="modelObject">
<input type="hidden" id="source" name="source"/>
<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
	<tr class="table-heading">
		<td colspan="4"><b><primo:label code="Upload Mileage" /></b></td>
	</tr>
	<tr>
		<td class="form-left"><primo:label code="Period" /><span class="errorMessage">*</span></td>
		<td align="${left}">
			<input type="text" size="15" id="period" name="period" class="flat"/> 
		</td>
	</tr>
	<tr>
		<td class="form-left"><primo:label code="Reset miles" /><span class="errorMessage">*</span></td>
		<td align="${left}">
			<input type="text" id="resetMiles" name="resetMiles" value="5000" class="flat" size="15" />
		</td>
	</tr>
	<tr>
		<td align="${left}" class="first"><primo:label code="Upload Mileage" /><span class="errorMessage">*</span>
		<td align="${left}">
			<input type="file" id="dataFile" name="dataFile" onchange="return checkfile()"/>
		</td>
	</tr>
	<tr>
		<td colspan="2"><input type="button" value="Upload Old Mileage" onclick="javascript:submitUploadform('OLD_GPS');"/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" value="Upload New Mileage" onclick="javascript:submitUploadform('NEW_GPS');"/></td>
	</tr>
</table>
</form:form>

<div id="mileageErrorDiv" style="font-size:1.2em;font-weight: bold; color:red"></div>

<c:if test="${not empty errorList}">
	<b>Following Mileage records are in Error:</b><br/>
	<c:forEach var="item" items="${errorList}">
		<FONT color=#F2290F><STRONG>${item}</strong></FONT>
	</c:forEach>
</c:if>
