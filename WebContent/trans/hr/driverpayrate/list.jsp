<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function getDestinationLoad(){
	var selectedtransfer= document.getElementById('transferStation');
	var origin=selectedtransfer.options[selectedtransfer.selectedIndex].value;
	var selectedlandfill= document.getElementById('landfill');
	var destination=selectedlandfill.options[selectedlandfill.selectedIndex].value;
	if(origin!="" && destination==""){
		jQuery.ajax({
			url:'${ctx}/hr/driverpayrate/ajax.do?action=findDestinationLoad&origin='+origin, 
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
			url:'${ctx}/hr/driverpayrate/ajax.do?action=findOriginLoad&destination='+destination, 
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

<h3><primo:label code="Manage Driver Pay Rate"/></h3>
<div><form:form action="search.do" method="get"
	name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search Driver Pay Rate"/></b></td>
		</tr>
		<tr>
		 	<td align="${left}" class="first"><primo:label code="Company"/></td>
				<td align="${left}"><select id="company" name="company.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${companies}" var="company">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['company.id'] == company.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${company.id}" ${selected}>${company.name}</option>
					</c:forEach>
			</select></td>
			<td align="${left}" class="first"><primo:label code="Terminal"/></td>
			<td align="${left}"><select id="terminal" name="terminal.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${terminals}" var="terminal">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['terminal.id'] == terminal.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${terminal.id}" ${selected}>${terminal.name}</option>
				</c:forEach>
			</select></td>
		 		
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
			<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();" value="<primo:label code="Search"/>" /></td>
		</tr>
		</tr>
		</table>
		</form:form>
		</div>
		<div style="width:100%; margin:0px auto;">
		<form:form name="delete.do" id="serviceForm">
		<primo:datatable urlContext="hr/driverpayrate" deletable="true" editable="true" insertable="true" exportPdf="true" exportXls="true" exportCsv="true"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
			<primo:textcolumn headerText="valid From" dataField="validFrom" dataFormat="MM-dd-yyyy" />
			<primo:textcolumn headerText="valid To" dataField="validTo" dataFormat="MM-dd-yyyy"/>
			<primo:textcolumn headerText="Company" dataField="company.name"/>
			<primo:textcolumn headerText="Terminal" dataField="terminal.name"/>	
			<primo:textcolumn headerText="Category" dataField="catagory.name" />
			<primo:textcolumn headerText="Transfer Station" dataField="transferStation.name" />
			<primo:textcolumn headerText="Landfill" dataField="landfill.name" />
			<primo:textcolumn headerText="Sunday Rate Factor" dataField="sundayRateFactor"/>
			<primo:textcolumn headerText="Day Rate" dataField="payRate"/>
			<primo:textcolumn headerText="Night Rate" dataField="nightPayRate"/>
			<primo:textcolumn headerText="Probation Rate" dataField="probationRate"/>
			<primo:staticdatacolumn headerText="Rate Type" dataField="rateType" dataType="RATE_TYPE"/>
			<primo:staticdatacolumn headerText="Rate Using" dataField="rateUsing" dataType="RATE_USING"/>
			<primo:anchorcolumn headerText="Manage Alert" dataField="alertStatus" linkUrl="/hr/driverpayrate/changeAlertStatus.do?id={id}" linkText="On alert"/>	
		</primo:datatable>
			<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>
		
			</div>
		