<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function ValidateFileType(fileId)
{
	var file =document.getElementById(fileId);
		if(file.value!=''){
	    var checkimg = file.value.toLowerCase();
	    	if (!checkimg.match(/(\.xlsx)$/)){
		  		file.value = null;
				alert("Only MS Excel files are allowed !");
				return false;
				   }		
				 }
	return true;
}
</script>
<div class="catbody">
<form action="${ctx}/uploadData/fuelSurcharge/upload.do" name="fileUploadForm"  method="POST"	enctype="multipart/form-data" >
	<table cellpadding="0" cellspacing="10" width="100%">
		<tr>
			<td valign="top" class="productbox" width="50%">
			<table width="100%">
				<tr>
					<td colspan="4">
						<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
							<tr class="table-heading" >
							     <td colspan="2"><b>Fuel Surcharge-Data Upload</b></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr height="75px">
					<td width="150">Select a file to upload:</td>
					<td width="10">:</td>
					<td>
						<input type="file" name="dataFile" id="dataFile" onchange="return ValidateFileType(this.id);"/>
					</td>
				</tr>
				<tr height="50px">
					<td width="150">&nbsp;</td>
					<td></td>
					<td><input type="submit" value="Upload" /> </td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</form>
</div>