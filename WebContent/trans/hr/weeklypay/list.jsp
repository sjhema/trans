<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
</script>
<h3><primo:label code="Manage Salary Payroll"/></h3>
<div><form:form action="search.do" method="get"
	name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search Salary Payroll"/></b></td>
		</tr>
			<tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
				<td align="${left}"><select id="company" name="company.id" style="min-width:154px; max-width:154px" >
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
			<td align="${left}"><select id="terminal" name="terminal.id" style="min-width:154px; max-width:154px" >
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
			<td align="${left}" class="first">Batch Date</td>
			<td align="${left}"><input name="PayRollBatch" id="datepicker" type="text" style="min-width:154px; max-width:154px" onblur="return formatDate('datepicker');"
				value='${sessionScope.searchCriteria.searchMap.PayRollBatch}' /></td>
			<td align="${left}" class="first">Check Date</td>
			<td align="${left}"><input type="text" name="checkDate" id="datepicker1"  style="min-width:154px; max-width:154px" value='${sessionScope.searchCriteria.searchMap.checkDate}'  onblur="return formatDate('datepicker1');" /></td>
		</tr>
		
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();" value="Search" />
			</td>
		</tr>
		</table>
		</form:form>
		</div>
		



<br />
<div style="width: 100%; margin: 0px auto;">
	<form:form name="delete.do" id="uploadinvoiceForm">
		<primo:datatable urlContext="hr/weeklypay" deletable="true"
			editable="false" insertable="false" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false">
			<primo:textcolumn headerText="Company" dataField="company.name" />
			<primo:textcolumn headerText="Terminal" dataField="terminal.name" />
			<primo:textcolumn headerText="Check Date" dataField="checkDate" dataFormat="MM-dd-yyyy"/>
			<primo:textcolumn headerText="Batch Date" dataField="payRollBatch" dataFormat="MM-dd-yyyy"/>
			<primo:imagecolumn headerText="Download (CSV)" linkUrl="${ctx}/hr/weeklypay/download.do?id={id}&type=csv" imageSrc="${ctx}/images/csv.png" HAlign="center"/>
			<primo:imagecolumn headerText="Download (XLS)" linkUrl="${ctx}/hr/weeklypay/download.do?id={id}&type=xls" imageSrc="${ctx}/images/excel.png" HAlign="center"/>
			<primo:imagecolumn headerText="Download (PDF)" linkUrl="${ctx}/hr/weeklypay/download.do?id={id}&type=pdf" imageSrc="${ctx}/images/pdf.png" HAlign="center"/>
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
	</form:form>
</div>