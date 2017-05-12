<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function showTruckDriverInfo(truckDriverInfo) {
	clearBillingHistorySummaryDetailsDialog();
	
	var truckDriverHtml = "<tr><td><b>TRUCK</b></td><td><b>DRIVER</b></td></tr>";
	var truckDriverTokens = truckDriverInfo.split("|");
	for (var i = 0; i < truckDriverTokens.length; i++) {
		truckDriverHtml += "<tr>";
		
	    var driverTokens = truckDriverTokens[i].split(":");
	    truckDriverHtml += "<td>" + driverTokens[0] + "</td>"
	    truckDriverHtml += "<td>" + driverTokens[1] + "</td>"
	    truckDriverHtml += "</tr>";
	}
	
	var billingHistorySummaryTruckDriverInfoElem = $('#billingHistorySummaryTruckDriverInfo');
	var existingContent = billingHistorySummaryTruckDriverInfoElem.html();
	billingHistorySummaryTruckDriverInfoElem.html(truckDriverHtml + existingContent);
	
	openBillingHistorySummaryDetailsDialog();
}

function openBillingHistorySummaryDetailsDialog() {
	$('#billingHistorySummaryDetailsDialog').dialog({ 
		 modal: true,
         bgiframe: true,
         width: 500,
         resizable: false
     });
}

function closeBillingHistorySummaryDetailsDialog() {
	clearBillingHistorySummaryDetailsDialog();
	$('#billingHistorySummaryDetailsDialog').dialog('close');
}

function clearBillingHistorySummaryDetailsDialog() {
	$('#billingHistorySummaryTruckDriverInfo').html("");
	clearBillingHistorySummaryDetailsMsgs();
}

function clearBillingHistorySummaryDetailsMsgs() {
	$('#billingHistorySummaryDetailsDialogErrorMessage').html("");
	$('#billingHistorySummaryDetailsDialogSuccessMessage').html("");
}
</script>

<div id="billingHistorySummaryDetailsDialog" title="Truck Driver Details" style="display:none;">
	<div id="billingHistorySummaryDetailsDialogBody">
		<div id="billingHistorySummaryDetailsDialogMessage">
     		<div id="billingHistorySummaryDetailsDialogErrorMessage" style="color:red; font-size:14px; vertical-align:center;"></div>
     		<div id="billingHistorySummaryDetailsDialogSuccessMessage" style="color:green; font-size:14px; vertical-align:center;"></div>
     	</div>
     	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
			<tbody id="billingHistorySummaryTruckDriverInfo">
			</tbody>
		</table>
		<table width="100%" cellspacing="1" cellpadding="5">
			<tbody>
				<tr>
					<td align="center">
						<input type="button" id="billingHistorySummaryDetailsDialog" value="<primo:label code="Close"/>" class="flat"
							onClick="javascript:closeBillingHistorySummaryDetailsDialog();" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>


