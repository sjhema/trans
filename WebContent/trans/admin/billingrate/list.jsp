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
	<primo:label code="Manage Billing Rate" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table id="form-table" width="55%" align="left" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b>Search Billing Rate</b>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><label>Transfer Station</label>
			</td>
			<td align="${left}"><select id="transferStation" name="transferStation.id" style="min-width:154px; max-width:154px"  onchange="javascript:getDestinationLoad()">
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
			<td align="${left}"><select id="landfill" name="landfill.id" style="min-width:154px; max-width:154px"  onchange="javascript:getOriginLoad()">
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
		<td align="${left}" class="first"><label>Customer Name</label></td>
		<td align="${left}"><select id="customerid" name="customername.id" style="min-width:154px; max-width:154px"  onchange="javascript:getOriginLoad()">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${customernames}" var="customer">
					<c:set var="selected" value=""/>
					<%-- <c:if test="${sessionScope.searchCriteria.searchMap['customername.id'] == customer.id}">
						<c:set var="selected" value="selected"/>
					</c:if> --%>
						<option value="${customer.id}" ${selected}>${customer.name}</option>
				</c:forEach>
			</select>
		</td>		
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
	<%request.getSession().setAttribute("typ",null); %>
		<primo:datatable urlContext="admin/billingrate" deletable="true"
			editable="true" insertable="true" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false">
			<br/>
			<primo:textcolumn headerText="valid From" dataField="validFrom" dataFormat="MM-dd-yyyy" />
			<primo:textcolumn headerText="valid To" dataField="validTo" dataFormat="MM-dd-yyyy"/>
			<primo:textcolumn headerText="Company" dataField="companyLocation.name" />
			<primo:textcolumn headerText="Transfer Station" dataField="transferStation.name" />
			<primo:textcolumn headerText="Landfill" dataField="landfill.name" />
			<primo:textcolumn headerText="Customer Name" dataField="customername.name" />
			<primo:staticdatacolumn headerText="Bill Using" dataField="billUsing" dataType="BILL_USING"/>
			<primo:staticdatacolumn headerText="Sort By" dataField="sortBy" dataType="BILL_USING"/>
            
            <primo:textcolumn headerText="Tonnage Premium" dataField="tonnagePremium.code" />
			<primo:textcolumn headerText="Demmurage Charge" dataField="demmurageCharge.demurragecode"/>			
			<primo:staticdatacolumn headerText="Rate Type" dataField="rateType" dataType="RATE_TYPE"/>
			<primo:textcolumn headerText="Rate" dataField="value"/>
			<primo:textcolumn headerText="Padd" dataField="padd.code"/>
			<primo:textcolumn headerText="Peg" dataField="peg"/>
			<primo:textcolumn headerText="Miles" dataField="miles"/>
			<primo:staticdatacolumn headerText="Fuel Surcharge Type" dataField="fuelSurchargeType" dataType="FUEL_SURCHARGE_TYPE"/>
			<primo:staticdatacolumn headerText="Fuel Surcharge Weekly Rate" dataField="fuelsurchargeweeklyRate" dataType="YES_NO"/>
			<primo:textcolumn headerText="Surcharge Per Ton" dataField="surchargePerTon"/>
			<primo:textcolumn headerText="Surcharge Amount" dataField="surchargeAmount"/>
			
			<!--Peak rate 2nd Feb 2021-->
			<primo:textcolumn headerText="Peak Rate" dataField="peakRate"/>
			<primo:textcolumn headerText="Peak Rate Start Time" dataField="peakRateValidFrom"/>
			<primo:textcolumn headerText="Peak Rate End Time" dataField="peakRateValidTo"/>
			
			<primo:anchorcolumn headerText="Manage Alert" dataField="status" linkUrl="/admin/billingrate/changestatus.do?id={id}" linkText="On alert"/>			
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
	</form:form>
</div>
<style>
.datagrid td{
 text-align: center;
}
</style> 