<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
</script>
<h3>
	<primo:label code="Manage Invoice" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b>Search Invoice</b></td>
		</tr>
		<tr>
			<td align="${left}" class="first">Invoice Date</td>
			<td align="${left}"><input name="InvoiceDate" id="datepicker" type="text" onblur="return formatDate('datepicker');"
				value='${sessionScope.searchCriteria.searchMap.InvoiceDate}' /></td>
			<td align="${left}" class="first">Invoice Number</td>
			<td align="${left}"><input name="invoiceNumber" type="text"
				value="${sessionScope.searchCriteria.searchMap.invoiceNumber}" /></td>
		</tr>
		<tr>
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
	<form:form name="delete.do" id="uploadinvoiceForm">
		<primo:datatable urlContext="admin/uploadinvoice" deletable="true"
			editable="false" insertable="false" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false">
			<primo:textcolumn headerText="Invoice Date" dataField="invoiceDate" dataFormat="MM-dd-yyyy"/>
			<primo:textcolumn headerText="Invoice Number" dataField="invoiceNumber" />
			<primo:textcolumn headerText="Transfer Station" dataField="transferStation.name" />
			<primo:textcolumn headerText="Landfill" dataField="landfill.name" />
			<primo:imagecolumn headerText="Download (CSV)" linkUrl="${ctx}/admin/uploadinvoice/downloadinvoice.do?id={id}&type=csv" imageSrc="${ctx}/images/csv.png" HAlign="center"/>
			<primo:imagecolumn headerText="Download (XLS)" linkUrl="${ctx}/admin/uploadinvoice/downloadinvoice.do?id={id}&type=xls" imageSrc="${ctx}/images/excel.png" HAlign="center"/>
			<primo:imagecolumn headerText="Download (PDF)" linkUrl="${ctx}/admin/uploadinvoice/downloadinvoice.do?id={id}&type=pdf" imageSrc="${ctx}/images/pdf.png" HAlign="center"/>
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
	</form:form>
</div>