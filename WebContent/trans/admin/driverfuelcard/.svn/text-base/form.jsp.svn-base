<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function findFuelCard(){
	if(document.getElementById("fuelvendor").value == ""){
		
	}else{			
		var selectedfuelvendor= document.getElementById('fuelvendor');
		var fuelvendor=selectedfuelvendor.options[selectedfuelvendor.selectedIndex].value;
		if(fuelvendor!=""){
			jQuery.ajax({
				url:'${ctx}/admin/driverfuelcard/ajax.do?action=findFuelCard&fuelvendor='+fuelvendor, 
				success: function( data ) {
					var listData=jQuery.parseJSON(data);
					var options = '<option value="">-----Please Select-----</option>';
					for (var i = 0; i <listData.length; i++) {
						var fuelcard=listData[i];
						options += '<option value="'+fuelcard.id+'">'+fuelcard.fuelcardNum+'</option>';
					  }
					$("#fuelcards").html(options);
				}
			});
		
				}
		
	}
}
	</script>

<h3><primo:label code="Manage Driver Fuel Cards"/></h3>

<form:form action="save.do" name="fuelcardForm"
	commandName="modelObject" method="post" >
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="2"><b><primo:label code="Add/Update Driver Fuel Card" />
			</b>
			</td>
		</tr>
		
			<tr>
			<td class="form-left"><primo:label code="Driver" /><span
				class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="driver" style="min-width:184px; max-width:184px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${drivers}" itemValue="id"
						itemLabel="fullName" />
				</form:select> <br> <form:errors path="driver" cssClass="errorMessage" /></td>
			
		
		</tr>
		
		
		
		<tr>
			<td class="form-left"><primo:label code="Fuel Vendor" /><span
				class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="fuelvendor" style="min-width:184px; max-width:184px" id="fuelvendor" onchange="findFuelCard();">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${fuelvendor}" itemValue="id"
						itemLabel="name" />
				</form:select> <br> <form:errors path="fuelvendor" cssClass="errorMessage" /></td>
			
			</tr>
		 <tr >
			<td class="form-left"><primo:label code="Fuel Card" /><span
				class="errorMessage">*</span>
			</td>
			<td><form:select cssClass="flat" path="fuelcard" style="min-width:184px; max-width:184px" id="fuelcards">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${fuelcard}" itemValue="id"
						itemLabel="fuelcardNum" />
				</form:select> <br> <form:errors path="fuelcard" cssClass="errorMessage" /></td>
			
		
		</tr>	 
		
		<tr>
		<td class="form-left"><primo:label code="Status" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="status">
					<form:options items="${fuelcardstatus}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="status" cssClass="errorMessage" />
			</td>
		</tr>
		
		
		
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick=""
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" /></td>
		</tr>
	</table>
</form:form>

