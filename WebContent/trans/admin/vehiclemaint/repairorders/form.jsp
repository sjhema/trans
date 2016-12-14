<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
var submitting = false;
function submitForm(){
	if(submitting) {
    	alert('Please wait a moment...');
    } else{
         submitting = true;
         document.forms["repairOrderForm"].submit();
	}
}

function deleteLineItem(lineItemId) {
	if(confirm("Do you want to delete the selected Line Item?")) {
		var id = document.getElementById("id").value;
		document.location = "${ctx}/admin/vehiclemaint/repairorders/deleteLineItem.do?id=" + id + "&lineItemId=" + lineItemId;
	}
}

function printOrder() {
	var id = document.getElementById("id").value;
	
	var url = "${ctx}/admin/vehiclemaint/repairorders/print.do?id=" + id;
	//window.open(url, "Repair Order Print", "toolbar=0,location=0,scrollbars=yes,resizable=yes,top=500,left=500,width=800,height=500");
	document.location = url;
}

function saveLineItemType() {
	clearLineItemTypeDialogMsgs();
	
	var type = $('#lineItemTypeDialogType').val();
	var description = $('#lineItemTypeDialogDescription').val();
	var lineItemTypeSel = $('#lineItemType');
	
	var lineItemTypeDialogErrorMessageElem = $('#lineItemTypeDialogErrorMessage');
	
	$.ajax({
  		url: "${ctx}/admin/vehiclemaint/repairorders/lineitemtype/ajax.do?action=save&type=" + type + "&description=" + description,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData.indexOf("success") == -1) {
       			lineItemTypeDialogErrorMessageElem.html(responseData);
       		} else {
       			var id = responseData.substring(responseData.indexOf(":") + 1);
       			var option = "<option value='" + id + "'>" + type + "</option>";
       			lineItemTypeSel.append(option);
       			lineItemTypeSel.val(id);
       			
       			closeLineItemTypeDialog();
       		}
		}
	});
}

function clearLineItemTypeDialog() {
	$('#lineItemTypeDialogType').val("");
	$('#lineItemTypeDialogDescription').val("");
	
	clearLineItemTypeDialogMsgs();
}

function clearLineItemTypeDialogMsgs() {
	$('#lineItemTypeDialogErrorMessage').html("");
	$('#lineItemTypeDialogSuccessMessage').html("");
}

function openLineItemTypeDialog() {
	clearLineItemTypeDialog();
	$('#lineItemTypeDialog').dialog({ 
		 modal: true,
         bgiframe: true,
         width: 500,
         resizable: false
     });
}

function closeLineItemTypeDialog() {
	clearLineItemTypeDialog();
	$('#lineItemTypeDialog').dialog('close');
}

function saveComponent() {
	clearComponentDialogMsgs();
	
	var component = $('#componentDialogComponent').val();
	var description = $('#componentDialogDescription').val();
	var componentSel = $('#lineItemComponent');
	
	var componentDialogErrorMessageElem = $('#componentDialogErrorMessage');
	
	$.ajax({
  		url: "${ctx}/admin/vehiclemaint/repairorders/component/ajax.do?action=save&component=" + component + "&description=" + description,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData.indexOf("success") == -1) {
       			componentTypeDialogErrorMessageElem.html(responseData);
       		} else {
       			var id = responseData.substring(responseData.indexOf(":") + 1);
       			var option = "<option value='" + id + "'>" + component + "</option>";
       			componentSel.append(option);
       			componentSel.val(id);
       			
       			closeComponentDialog();
       		}
		}
	});
}

function clearComponentDialog() {
	$('#componentDialogComponent').val("");
	$('#componentDialogDescription').val("");
	
	clearComponentDialogMsgs();
}

function clearComponentDialogMsgs() {
	$('#componentDialogErrorMessage').html("");
	$('#componentSuccessMessage').html("");
}

function openComponentDialog() {
	clearComponentDialog();
	$('#componentDialog').dialog({ 
		 modal: true,
         bgiframe: true,
         width: 500,
         resizable: false
     });
}

function closeComponentDialog() {
	clearComponentDialog();
	$('#componentDialog').dialog('close');
}

function editLineItem(lineItemId) {
	$.ajax({
  		url: "ajax.do?action=retrieveLineItem" + "&lineItemId=" + lineItemId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		var lineItem = jQuery.parseJSON(responseData);
       		$("#lineItemId").val(lineItem.id);
       		$("#lineItemType").val(lineItem.lineItemType.id);
       		$("#lineItemComponent").val(lineItem.component.id);
       		$("#lineItemDescription").val(lineItem.description);
       		$("#lineItemNoOfHours").val(lineItem.noOfHours);
       		$("#lineItemLaborRate").val(lineItem.laborRate);
       		$("#lineItemTotalLaborCost").val(lineItem.totalLaborCost);
       		$("#lineItemTotalPartsCost").val(lineItem.totalPartsCost);
       		$("#lineItemTotalCost").val(lineItem.totalCost);
       		$("#originalLineItemTotalCost").val(lineItem.totalCost);
		}
	});
}

function processLineItemTypeChange() {
	populateSimilarLineItem();
}

function processLineItemComponentChange() {
	populateSimilarLineItem();
}

function populateSimilarLineItem() {
	/*var lineItemDescriptionElem = $("#lineItemDescription");
	var lineItemNoOfHoursElem = $("#lineItemNoOfHours");
	var lineItemLaborRateElem = $("#lineItemLaborRate");
	var lineItemTotalLaborCostElem = $("#lineItemTotalLaborCost");
	var lineItemTotalPartsCostElem = $("#lineItemTotalPartsCost");
	var lineItemTotalCostElem = $("#lineItemTotalCost");
	
	lineItemDescriptionElem.val("");
	lineItemNoOfHoursElem.val("");
	lineItemLaborRateElem.val("");
	lineItemTotalLaborCostElem.val("");
	lineItemTotalPartsCostElem.val("");
	lineItemTotalCostElem.val("");*/
	
	var typeId = document.getElementById("lineItemType").value;
	var componentId = document.getElementById("lineItemComponent").value;
	if (typeId == '' || componentId == '') {
		return;
	}
	
	$.ajax({
  		url: "ajax.do?action=retrieveSimilarLineItem" + "&typeId=" + typeId + "&componentId=" + componentId,
       	type: "GET",
       	success: function(responseData, textStatus, jqXHR) {
       		if (responseData == '') {
       			return;
       		}
       		
       		var simialrLineItem = jQuery.parseJSON(responseData);
       		$("#lineItemDescription").val(simialrLineItem.description);
       		$("#lineItemNoOfHours").val(simialrLineItem.noOfHours);
       		$("#lineItemLaborRate").val(simialrLineItem.laborRate);
       		$("#lineItemTotalLaborCost").val(simialrLineItem.totalLaborCost);
       		$("#lineItemTotalPartsCost").val(simialrLineItem.totalPartsCost);
       		$("#lineItemTotalCost").val(simialrLineItem.totalCost);
       		
       		populateCosts();
		}
	});
}

function populateCosts() {
	var originalTotalOrderCostElem = document.getElementById("originalTotalOrderCost");
	
	var lineItemIdElem = document.getElementById("lineItemId");
	var originalLineItemTotalCostElem = document.getElementById("originalLineItemTotalCost");
	
	var lineItemNoOfHoursElem = document.getElementById("lineItemNoOfHours");
	var lineItemLaborRateElem = document.getElementById("lineItemLaborRate");
	var lineItemTotalLaborCostElem = document.getElementById("lineItemTotalLaborCost");
	var lineItemTotalPartsCostElem = document.getElementById("lineItemTotalPartsCost");
	var lineItemTotalCostElem = document.getElementById("lineItemTotalCost");
	var orderTotalCostElem = document.getElementById("totalCost");
	
	var originalLineItemTotalCost = parseFloat(0.00);
	if (originalLineItemTotalCostElem.value != '') {
		originalLineItemTotalCost = parseFloat((parseFloat(originalLineItemTotalCostElem.value)).toFixed(2));
	}
	
	var originalTotalOrderCost = parseFloat(0.00);
	if (originalTotalOrderCostElem.value != '') {
		originalTotalOrderCost = parseFloat((parseFloat(originalTotalOrderCostElem.value)).toFixed(2));
	}
	if (lineItemIdElem.value != '') {
		originalTotalOrderCost = originalTotalOrderCost - originalLineItemTotalCost;
		originalTotalOrderCost = parseFloat((originalTotalOrderCost.toFixed(2)));
	}
	
	var lineItemNoOfHours = parseFloat(0.0);
	if (lineItemNoOfHoursElem.value != '') {
		lineItemNoOfHours = parseFloat((parseFloat(lineItemNoOfHoursElem.value)).toFixed(1));
	}
	var lineItemLaborRate = parseFloat(0.00);
	if (lineItemLaborRateElem.value != '') {
		lineItemLaborRate = parseFloat((parseFloat(lineItemLaborRateElem.value)).toFixed(2));
	}
	var lineItemTotalPartsCost = parseFloat(0.00);
	if (lineItemTotalPartsCostElem.value != '') {
		lineItemTotalPartsCost = parseFloat((parseFloat(lineItemTotalPartsCostElem.value)).toFixed(2));
	}
	
	var lineItemTotalLaborCost = parseFloat(0.00);
	var lineItemTotalCost = parseFloat(0.00);
	var orderTotalCost = parseFloat(0.00);
	
	lineItemTotalLaborCost = parseFloat((lineItemNoOfHours * lineItemLaborRate).toFixed(2));
	lineItemTotalCost = parseFloat((lineItemTotalLaborCost + lineItemTotalPartsCost).toFixed(2));
	orderTotalCost =  parseFloat((originalTotalOrderCost + lineItemTotalCost).toFixed(2));
	
	lineItemNoOfHoursElem.value = lineItemNoOfHours;
	lineItemLaborRateElem.value = lineItemLaborRate;
	lineItemTotalLaborCostElem.value = lineItemTotalLaborCost;
	lineItemTotalPartsCostElem.value = lineItemTotalPartsCost;
	lineItemTotalCostElem.value = lineItemTotalCost;
	orderTotalCostElem.value = orderTotalCost;
}
</script>
<h3>
	<primo:label code="Add/Update Repair Orders" />
</h3>
<form:form action="save.do" name="repairOrderForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="lineItemId" id="lineItemId" />
	<input type="hidden" name="originalLineItemTotalCost" id="originalLineItemTotalCost" value="" />
	<input type="hidden" name="originalTotalOrderCost" id="originalTotalOrderCost" value="${modelObject.totalCost}" />
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Order" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Order No" /></td>
			<td align="${left}">
				<form:input path="id" style="background-color: #eee; min-width:162px; max-width:162px" cssClass="flat" 
					 readonly="true" /> 
			</td>
			<td class="form-left"><primo:label code="Order Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input id="datepicker" path="repairOrderDate" style="min-width:158px; max-width:158px" cssClass="flat"  />
				<br> 
				<form:errors path="repairOrderDate" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Description" /></td>
			<td align="${left}" colspan="6">
				<form:input path="description" style="min-width:602px; max-width:602px" cssClass="flat"  />
				<br> <form:errors path="description" cssClass="errorMessage" />   	
			</td>
		<tr>
		</tr>
		<tr>
			<td class="form-left">
				<primo:label code="Company" /><span class="errorMessage">*</span>
			</td>
			<td>
				<form:select cssClass="flat" path="company" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="company" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Subcontractor" /></td>
			<td>
				<form:select cssClass="flat" path="subcontractor" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${subcontractors}" itemValue="id"	itemLabel="name" />
				</form:select> 
				<br> 
				<form:errors path="subcontractor" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Vehicle" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="vehicle" id="truckId" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${vehicles}" itemValue="id" itemLabel="unit" />
				</form:select> <br> <form:errors path="vehicle" cssClass="errorMessage" /><span id="spanId3" style="color:red; font-size:10px; font-weight:bold "> </span>
			</td>
			<td class="form-left"><primo:label code="Mechanic" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="mechanic" id="mechanic" style="min-width:166px; max-width:166px">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${mechanics}" itemValue="id" itemLabel="fullName"/>
				</form:select> 
				<br> <form:errors path="mechanic" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Total Order Cost" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="totalCost" style="background-color: #eee; min-width:162px; max-width:162px" cssClass="flat" 
					readonly="true" />
				<br> <form:errors path="totalCost" cssClass="errorMessage" />
			</td>
		</tr>
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Order Line Item" /></b></td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Type" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="lineItemType" id="lineItemType" style="min-width:166px; max-width:166px" 
					onchange="javascript:processLineItemTypeChange();">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${lineItemTypes}" itemValue="id" itemLabel="type" />
				</form:select>
				&nbsp;
				<a href="javascript:openLineItemTypeDialog();">
					<img class="toolbarButton" src="/trans/images/add.png" style="vertical-align: top" title="Add Line Item Type" border="0">
				</a>
				<br> <form:errors path="lineItemType" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Component" /><span class="errorMessage">*</span></td>
			<td>
				<form:select cssClass="flat" path="lineItemComponent" id="lineItemComponent" style="min-width:166px; max-width:166px" 
					onchange="javascript:processLineItemComponentChange();">
					<form:option value="">-----<primo:label code="Please Select" />-----</form:option>
					<form:options items="${components}" itemValue="id" itemLabel="component" />
				</form:select>
				&nbsp;
				<a href="javascript:openComponentDialog();">
					<img class="toolbarButton" src="/trans/images/add.png" style="vertical-align: top" title="Add Component" border="0">
				</a>
				<br> <form:errors path="lineItemComponent" cssClass="errorMessage" />
			</td>
		</tr>	
		<tr>
			<td class="form-left"><primo:label code="Description" /></td>
			<td align="${left}" colspan="6">
				<form:input path="lineItemDescription" style="min-width:602px; max-width:602px" cssClass="flat"  />
				<br> <form:errors path="lineItemDescription" cssClass="errorMessage" />   	
			</td>
		</tr>	
		<tr>
			<td class="form-left"><primo:label code="Labor Rate" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="lineItemLaborRate" style="min-width:158px; max-width:158px" cssClass="flat" 
					 maxlength="7" onkeypress="return onlyNumbers(event, true)" onchange="return populateCosts();"  /> 
				<br> <form:errors path="lineItemLaborRate" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="No Of Hours" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="lineItemNoOfHours" style="min-width:159px; max-width:159px" cssClass="flat"
					 maxlength="6" onkeypress="return onlyNumbers(event, true)" onchange="return populateCosts();"  />
				<br> <form:errors path="lineItemNoOfHours" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Total Labor Cost" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="lineItemTotalLaborCost" style="background-color: #eee; min-width:162px; max-width:162px" cssClass="flat" 
					readonly="true" /> 
				<br> <form:errors path="lineItemTotalLaborCost" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Total Parts Cost" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="lineItemTotalPartsCost" style="min-width:159px; max-width:159px" cssClass="flat" 
					maxlength="8" onkeypress="return onlyNumbers(event, true)" onchange="return populateCosts();" /> 
				<br> <form:errors path="lineItemTotalPartsCost" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Total Line Item Cost" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="lineItemTotalCost" style="background-color: #eee; min-width:162px; max-width:162px" cssClass="flat"
					readonly="true"  /> 
				<br> <form:errors path="lineItemTotalCost" cssClass="errorMessage" />
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="button" name="create" id="create" onclick="javascript:submitForm();"
					value="<primo:label code="Save"/>" class="flat" />
				&nbsp;
				<input type="reset" id="resetBtn"
					value="<primo:label code="Reset"/>" class="flat" />
				&nbsp;
				<input type="button" id="printBtn" onclick="javascript:printOrder();"
					value="<primo:label code="Print"/>" class="flat" /> 
				&nbsp;
				<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat"
					onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="repairOrderLineItemServiceForm" id="repairOrderLineItemServiceForm">
	<primo:datatable urlContext="admin/vehiclemaint/repairorders"
		baseObjects="${repairOrderLineItemList}"
		searchCriteria="<%=null%>" cellPadding="2">
		<primo:textcolumn headerText="Type" dataField="lineItemType.type" />
        <primo:textcolumn headerText="Component" dataField="component.component" />
        <primo:textcolumn headerText="Description" dataField="description" width="500px" />
       	<primo:textcolumn headerText="Labor Rate" dataField="laborRate" width="75px"/>
       	<primo:textcolumn headerText="No Of Hrs" dataField="noOfHours" width="75px"/>
        <primo:textcolumn headerText="Tot Labor Cost" dataField="totalLaborCost" width="75px"/>
        <primo:textcolumn headerText="Tot Parts Cost" dataField="totalPartsCost" width="75px"/>
       	<primo:textcolumn headerText="Tot Ln Itm Cost" dataField="totalCost" width="75px" />
       	<primo:imagecolumn headerText="EDIT" linkUrl="javascript:editLineItem('{id}');" imageSrc="${ctx}/images/edit.png" HAlign="center" width="25px"/>
       	<primo:imagecolumn headerText="DELETE" linkUrl="javascript:deleteLineItem('{id}');" imageSrc="${ctx}/images/delete.png" HAlign="center" width="25px"/>
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>

<div id="lineItemTypeDialog" title="Add Repair Order Line Item Type" style="display:none;">
	<div id="lineItemTypeDialogBody">
		<div id="lineItemTypeDialogMessage">
     		<div id="lineItemTypeDialogErrorMessage" style="color:red; font-size:14px; vertical-align:center;"></div>
     		<div id="lineItemTypeDialogSuccessMessage" style="color:green; font-size:14px; vertical-align:center;"></div>
     	</div>
		<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
			<tr>
				<td class="form-left"><primo:label code="Type" /><span class="errorMessage">*</span></td>
				<td align="${left}">
					<input type="text" id="lineItemTypeDialogType" style="min-width:300px; max-width:300px" class="flat">
				</td>
			</tr>
			<tr>
				<td class="form-left"><primo:label code="Description" /></td>
				<td align="${left}">
					<input type="text" id="lineItemTypeDialogDescription" style="min-width:300px; max-width:300px" class="flat">
				</td>
			</tr>
			<tr><td colspan="2"></td></tr>
			<tr>
				<td>&nbsp;</td>
				<td align="${left}" colspan="2">
					<input type="button" id="lineItemTypeDialogCreate" onclick="javascript:saveLineItemType();"
						value="<primo:label code="Save"/>" class="flat" />
					<input type="button" id="lineItemTypeDialogCancel" value="<primo:label code="Cancel"/>" class="flat"
						onClick="javascript:closeLineItemTypeDialog();" />
				</td>
			</tr>
		</table>
	</div>
</div>

<div id="componentDialog" title="Add Repair Order Component" style="display:none;">
	<div id="componentDialogBody">
		<div id="componentDialogMessage">
     		<div id="componentDialogErrorMessage" style="color:red; font-size:14px; vertical-align:center;"></div>
     		<div id="componentDialogSuccessMessage" style="color:green; font-size:14px; vertical-align:center;"></div>
     	</div>
		<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
			<tr>
				<td class="form-left"><primo:label code="Component" /><span class="errorMessage">*</span></td>
				<td align="${left}">
					<input type="text" id="componentDialogComponent" style="min-width:300px; max-width:300px" class="flat">
				</td>
			</tr>
			<tr>
				<td class="form-left"><primo:label code="Description" /></td>
				<td align="${left}">
					<input type="text" id="componentDialogDescription" style="min-width:300px; max-width:300px" class="flat">
				</td>
			</tr>
			<tr><td colspan="2"></td></tr>
			<tr>
				<td>&nbsp;</td>
				<td align="${left}" colspan="2">
					<input type="button" id="componentDialogCreate" onclick="javascript:saveComponent();"
						value="<primo:label code="Save"/>" class="flat" />
					<input type="button" id="componentDialogCancel" value="<primo:label code="Cancel"/>" class="flat"
						onClick="javascript:closeComponentDialog();" />
				</td>
			</tr>
		</table>
	</div>
</div>

