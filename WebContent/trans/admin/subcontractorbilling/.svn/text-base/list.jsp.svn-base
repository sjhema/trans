<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">

</script>
<script type="text/javascript">
function deleteconfirm()                                                 
{                       
return confirm('do  you  want to delete this selected  recoed ?');

}                                                                                       
</script>
<h3>
	<primo:label code="Manage Subcontractor Voucher" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b>Search Voucher</b></td>
		</tr>
		<tr>
			<td align="${left}" class="first">Voucher Date</td>
			<td align="${left}"><input name="VoucherDate" id="datepicker" type="text" onblur="return formatDate('datepicker');"
				value='${sessionScope.searchCriteria.searchMap.VoucherDate}' /></td>
			<td align="${left}" class="first">Voucher Number</td>
			<td align="${left}"><input name="voucherNumber" type="text"
				value="${sessionScope.searchCriteria.searchMap.voucherNumber}" /></td>
		</tr>
		<tr>
				
				<td align="${left}" class="first"><primo:label code="Subcontractor"/></td>
			<td align="${left}"><select id="origin" name="subContractorId.id">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${subcontractors}" var="subContractors">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['subContractorId.id'] == subContractors.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${subContractors.id}"${selected}>${subContractors.name}</option>
					</c:forEach>
			</select></td>
			
				<td align="${left}" class="first"><primo:label code="Company"/></td>
			<td align="${left}"><select id="origin" name="companyLocationId.id">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${companyLocation}" var="companyLocation">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['companyLocationId.id'] == companyLocation.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${companyLocation.id}"${selected}>${companyLocation.name}</option>
					</c:forEach>
			</select></td>
			<tr>
		
			<td align="${left}" class="first"><primo:label code="Miscellaneous Charges"/></td>
			<td align="${left}"><input name="miscelleneousCharges" type="text"
				value="${sessionScope.searchCriteria.searchMap.miscelleneousCharges}" /></td>
			</tr>
				
		</tr>
	<%-- 	<tr>
			<td align="${left}" class="first"><primo:label code="Transfer Station"/></td>
			<td align="${left}"><select id="origin" name="transferStation.id">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${transferStations}" var="transferStation">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['transferStation.id'] == transferStation.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${transferStation.id}"${selected}>${transferStation.name}</option>
					</c:forEach>
			</select></td>
			<td align="${left}" class="first"><primo:label code="Landfill" /></td>
			<td align="${left}"><select id="landfill" name="landfill.id">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${landfills}" var="landfill">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['landfill.id'] == landfill.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${landfill.id}"${selected}>${landfill.name}</option>
					</c:forEach>
			</select></td>
		</tr> --%>
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
	<form:form name="delete.do" id="subcontractorbillingForm">
		<primo:datatable urlContext="admin/subcontractorbilling" deletable="true"
			editable="false" insertable="false" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false"  searcheable="false">
			<primo:textcolumn headerText="Subcontractor" dataField="subContractorId.name" />
			<primo:textcolumn headerText="Company" dataField="companyLocationId.name" />
			<primo:textcolumn headerText="Voucher Date" dataField="voucherDate" dataFormat="MM-dd-yyyy"/>
			<primo:textcolumn headerText="Voucher Number" dataField="voucherNumber" />
			<%-- <primo:textcolumn headerText="Transfer Station" dataField="transferStation.name" />
			<primo:textcolumn headerText="Landfill" dataField="landfill.name" /> --%>
			<primo:imagecolumn headerText="Download (CSV)" linkUrl="${ctx}/admin/subcontractorbilling/download.do?id={id}&type=csv" imageSrc="${ctx}/images/csv.png" HAlign="center"/>
			<primo:imagecolumn headerText="Download (XLS)" linkUrl="${ctx}/admin/subcontractorbilling/download.do?id={id}&type=xls" imageSrc="${ctx}/images/excel.png" HAlign="center"/>
			<primo:imagecolumn headerText="Download (PDF)" linkUrl="${ctx}/admin/subcontractorbilling/download.do?id={id}&type=pdf" imageSrc="${ctx}/images/pdf.png" HAlign="center"/>
			<%-- <primo:imagecolumn headerText="DELETE" linkUrl="${ctx}/admin/subcontractorbilling/delete.do?id={id} " imageSrc="${ctx}/images/delete.png" HAlign="center" /> --%>
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
	</form:form>
</div>