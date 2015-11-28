<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function getDestinationLoad(){
	var selectedtransfer= document.getElementById('transferStation');
	var origin=selectedtransfer.options[selectedtransfer.selectedIndex].value;
	var selectedlandfill= document.getElementById('landfill');
	var destination=selectedlandfill.options[selectedlandfill.selectedIndex].value;
	if(origin!="" && destination==""){
		jQuery.ajax({
			url:'${ctx}/admin/billingrate/ajax.do?action=findDestinationLoad&origin='+origin, 
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var options = '<option value="">Please Select</option>';
				for (var i = 0; i <listData.length; i++) {
					var destination=listData[i];
					options += '<option value="'+destination.id+'">'+destination.name+'</option>';
				  }
				$("#landfill").html(options);
			}
		});
	}	
}
function getOriginLoad(){
	var selectedtransfer= document.getElementById('transferStation');
	var origin=selectedtransfer.options[selectedtransfer.selectedIndex].value;
	var selectedlandfill= document.getElementById('landfill');
	var destination=selectedlandfill.options[selectedlandfill.selectedIndex].value;
	if(destination!="" && origin==""){
		jQuery.ajax({
			url:'${ctx}/admin/billingrate/ajax.do?action=findOriginLoad&destination='+destination, 
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var options = '<option value="">Please Select</option>';
				for (var i = 0; i <listData.length; i++) {
					var origin=listData[i];
					options += '<option value="'+origin.id+'">'+origin.name+'</option>';
				  }
				$("#transferStation").html(options);
			}
		});	
	}
}
</script>
<h3>
	<primo:label code="Expired/Expiring Billing Rate" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
  <input type="hidden" value="billingrate" name="type"/>
	<table id="form-table" width="100%"  cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b>Search Billing Rate</b>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><label>Transfer Station</label>
			</td>
			<td align="${left}"><select id="transferStation" name="transferStation.id" onchange="javascript:getDestinationLoad()">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${transferStation}" var="transferStation">
				<c:set var="selected" value=""/>
				<c:if test="${sessionScope.searchCriteria.searchMap['transferStation.id'] == transferStation.id}">
					<c:set var="selected" value="selected"/>
				</c:if>
					<option value="${transferStation.id}" ${selected}>${transferStation.name}</option>
				</c:forEach>
			</select></td>
			<td align="${left}" class="first"><label>Landfill</label>
			</td>
			<td align="${left}"><select id="landfill" name="landfill.id" onchange="javascript:getOriginLoad()">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${landfill}" var="landfill">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['landfill.id'] == landfill.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${landfill.id}" ${selected}>${landfill.name}</option>
				</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();" value="Search" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<div style="width: 100%; margin: 0px auto;">			
		<form:form name="delete.do" id="customerForm">	
		<c:set var="typ" value="${type}" scope="session"/>			
		<primo:datatable urlContext="admin/billingrate"  deletable="true"
			editable="true" insertable="true" exportPdf="false" exportXls="false"
			exportCsv="false" baseObjects="${billingrate}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="list.do" multipleDelete="false" searcheable="false">		
			<%request.setAttribute("type","alert"); %>
			<primo:textcolumn headerText="&nbsp;&nbsp;Valid&nbsp;From&nbsp;&nbsp;" dataField="validFrom" dataFormat="MM-dd-yyyy" HAlign="center"/>
			<primo:textcolumn headerText="&nbsp;&nbsp;&nbsp;Valid&nbsp;&nbsp;To&nbsp;&nbsp;&nbsp;" dataField="validTo" dataFormat="MM-dd-yyyy" HAlign="center"/>
			<primo:textcolumn headerText="Company" dataField="companyLocation.name" HAlign="center" />
			<primo:textcolumn headerText="Transfer Station" dataField="transferStation.name" HAlign="center"/>
			<primo:textcolumn headerText="Landfill" dataField="landfill.name"  HAlign="center"/>
			<primo:textcolumn headerText="Customer Name" dataField="customername.name" HAlign="center"/>
			<primo:staticdatacolumn headerText="Bill Using" dataField="billUsing" dataType="BILL_USING" HAlign="center"/>
			<primo:staticdatacolumn headerText="Sort By" dataField="sortBy" dataType="BILL_USING" HAlign="center"/>
            
            <primo:textcolumn headerText="Tonnage Premium" dataField="tonnagePremium.code" HAlign="center"/>
			<primo:textcolumn headerText="Demmurage Charge" dataField="demmurageCharge.demurragecode" HAlign="center"/>			
			<primo:staticdatacolumn headerText="Rate Type" dataField="rateType" dataType="RATE_TYPE" HAlign="center"/>
			<primo:textcolumn headerText="Rate" dataField="value" HAlign="center"/>
			<primo:textcolumn headerText="Padd" dataField="padd.code" HAlign="center"/>
			<primo:textcolumn headerText="Peg" dataField="peg" HAlign="center"/>
			<primo:textcolumn headerText="Miles" dataField="miles" HAlign="center"/>
			<primo:staticdatacolumn headerText="Fuel Surcharge Type" dataField="fuelSurchargeType" dataType="FUEL_SURCHARGE_TYPE" HAlign="center"/>
			<primo:staticdatacolumn headerText="Fuel Surcharge Weekly Rate" dataField="fuelsurchargeweeklyRate" dataType="YES_NO" HAlign="center"/>
			<primo:textcolumn headerText="Surcharge Per Ton" dataField="surchargePerTon" HAlign="center"/>
			<primo:textcolumn headerText="Surcharge Amount" dataField="surchargeAmount" HAlign="center"/>
		     <primo:staticdatacolumn headerText="Status"	dataField="ratestatus" HAlign="center"/>
		     <primo:anchorcolumn  headerText="Manage Alert" dataField="status" linkUrl="/admin/billingrate/changestatus.do?id={id}&status={ratestatus}" linkText="Off alert"/>	
			
		</primo:datatable>
	</form:form>
</div>
 <style>
.datagrid td{
 text-align: center;
}
</style> 