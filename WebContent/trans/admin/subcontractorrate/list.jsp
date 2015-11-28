<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function getDestinationLoad(){
	var selectedtransfer= document.getElementById('transferStation');
	var origin=selectedtransfer.options[selectedtransfer.selectedIndex].value;
	var selectedlandfill= document.getElementById('landfill');
	var destination=selectedlandfill.options[selectedlandfill.selectedIndex].value;
	if(origin!="" && destination==""){
		jQuery.ajax({
			url:'${ctx}/admin/subcontractorrate/ajax.do?action=findDestinationLoad&origin='+origin, 
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
			url:'${ctx}/admin/subcontractorrate/ajax.do?action=findOriginLoad&destination='+destination, 
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
	<primo:label code="Manage Subcontractor Rate" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table id="form-table" width="100%"  cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b>Search Subcontractor Rate</b>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><label>Transfer Station</label>
			</td>
			<td align="${left}"><select id="transferStation" name="transferStation.id" style="min-width:154px; max-width:154px" onchange="javascript:getDestinationLoad()">
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
			<td align="${left}"><select id="landfill" name="landfill.id" style="min-width:154px; max-width:154px" onchange="javascript:getOriginLoad()">
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
			<td align="${left}" class="first"><label>Subcontractor</label>
			</td>
				<td align="${left}"><select id="subcontractor" name="subcontractor.id" style="min-width:154px; max-width:154px" >
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${subContractor}" var="subContractor">
				<c:set var="selected" value=""/>
				<c:if test="${sessionScope.searchCriteria.searchMap['subcontractor.id'] ==subContractor.id}">
					<c:set var="selected" value="selected"/>
				</c:if>
					<option value="${subContractor.id}" ${selected}>${subContractor.name}</option>
				</c:forEach>
			</select></td>
			
			
			<td align="${left}" class="first"><label>Company</label>
			</td>
				<td align="${left}"><select id="companyLocation" name="companyLocation.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${companyLocation}" var="companylocation">
				<c:set var="selected" value=""/>
				<c:if test="${sessionScope.searchCriteria.searchMap['companyLocation.id'] ==companylocation.id}">
					<c:set var="selected" value="selected"/>
				</c:if>
					<option value="${companylocation.id}" ${selected}>${companylocation.name}</option>
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
	<%request.getSession().setAttribute("typ",null); %>
		<primo:datatable urlContext="admin/subcontractorrate" deletable="true"
			editable="true" insertable="true" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false">
			
			<primo:textcolumn headerText="valid From" dataField="validFrom" dataFormat="MM-dd-yyyy" />
			<primo:textcolumn headerText="valid To" dataField="validTo" dataFormat="MM-dd-yyyy"/>
			<primo:textcolumn headerText="Subcontractor" dataField="subcontractor.name" />
			<primo:textcolumn headerText="Company" dataField="companyLocation.name" />
			<primo:textcolumn headerText="Transfer Station" dataField="transferStation.name" />
			<primo:textcolumn headerText="Landfill" dataField="landfill.name" />
			<primo:staticdatacolumn headerText="Bill Using" dataField="billUsing" dataType="BILL_USING"/>
			<primo:staticdatacolumn headerText="Sort By" dataField="sortBy" dataType="BILL_USING"/>
			<primo:staticdatacolumn headerText="Rate Type" dataField="rateType" dataType="RATE_TYPE"/>
			<primo:textcolumn headerText="Rate" dataField="value"/>
			<primo:textcolumn headerText="Fuel Surcharge Amount" dataField="fuelSurchargeAmount"/>
			<primo:textcolumn headerText="Other Charges" dataField="otherCharges"/>
			<primo:anchorcolumn headerText="Manage Alert" dataField="status" linkUrl="/admin/subcontractorrate/changestatus.do?id={id}" linkText="On alert"/>			
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
	</form:form>
</div>

<style>
.datagrid td{
 text-align: center;
}
</style> 