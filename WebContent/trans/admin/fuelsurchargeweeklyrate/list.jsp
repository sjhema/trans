<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
</script>
<h3><primo:label code="Manage Fuel Surcharge Weekly Rate"/></h3>
<div><form:form action="search.do" method="get"
	name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search Fuel Surcharge Weekly Rate"/></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><label>Transfer Station</label>
			</td>
			<td align="${left}"><select id="transferStation" name="transferStations.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${transferStations}" var="transferStation">
				<c:set var="selected" value=""/>
				<c:if test="${sessionScope.searchCriteria.searchMap['transferStation.id'] == transferStation.id}">
					<c:set var="selected" value="selected"/>
				</c:if>
					<option value="${transferStation.id}" ${selected}>${transferStation.name}</option>
				</c:forEach>
			</select></td>
			<td align="${left}" class="first"><label>Landfill Station</label>
			</td>
			<td align="${left}"><select id="landfill" name="landfillStations.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${landfillStations}" var="landfill">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['landfill.id'] == landfill.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${landfill.id}" ${selected}>${landfill.name}</option>
				</c:forEach>
			</select></td>
		</tr>
		<tr>
		<td align="${left}" class="first"><primo:label code="Rate Type" /></td>
			<td align="${left}"><select id="type" name="rateType" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${rateTypes}" var="locationType">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['rateType'] == locationType.dataValue}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${locationType.dataValue}"${selected}>${locationType.dataText}</option>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();" value="<primo:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form></div>
<br/>
<div style="width:100%; margin:0px auto;"><form:form name="delete.do" id="customerForm">
<%request.getSession().setAttribute("typ",null); %>
		<primo:datatable urlContext="admin/fuelsurchargeweeklyrate" 
		deletable="true" editable="true" insertable="true" exportPdf="true" exportXls="true" exportCsv="true"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
			<primo:textcolumn headerText="Transfer Station" dataField="transferStations.name" />
			<primo:textcolumn headerText="Landfill Station" dataField="landfillStations.name" />
			<primo:staticdatacolumn headerText="Rate Type" dataField="rateType" dataType="RATE_TYPE"/>
			<primo:textcolumn headerText="Fuel Surcharge Rate" dataField="fuelSurchargeRate" width="100px"/>
			<primo:datecolumn headerText="From Date" dataField="fromDate" dataFormat="MM-dd-yyyy"/>	
			<primo:datecolumn headerText="To Date" dataField="toDate" dataFormat="MM-dd-yyyy"/>
			<primo:anchorcolumn headerText="Manage Alert" dataField="status" linkUrl="/admin/fuelsurchargeweeklyrate/changestatus.do?id={id}" linkText="On alert"/>	
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
	</form:form>
</div>
<style>
.datagrid td{
 text-align: center;
}
</style> 