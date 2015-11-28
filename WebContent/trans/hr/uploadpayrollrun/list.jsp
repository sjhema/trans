<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
</script>
<h3>
	<primo:label code="Manage Hourly Payroll" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b>Search Hourly Payroll</b></td>
		</tr>
		
		<tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
				<td align="${left}"><select id="company" name="companyLoc.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${companies}" var="company">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['companyLoc.id'] == company.id}">
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
		    
			<td align="${left}" class="first">Check Date</td>
			<td align="${left}"><input name="Payrollinvoicedate" id="datepicker" style="min-width:150px; max-width:150px" type="text" onblur="return formatDate('datepicker');"
				value='${sessionScope.searchCriteria.searchMap.Payrollinvoicedate}' /></td>
			
		</tr>
		
		
		<tr>
			<td align="${left}" class="first">Batch Date From</td>
			<td align="${left}"><input name="BillBatchFrom" id="datepicker1" style="min-width:150px; max-width:150px" type="text" onblur="return formatDate('datepicker1');"
				value='${sessionScope.searchCriteria.searchMap.BillBatchFrom}' /></td>
				<td align="${left}" class="first">Batch Date To</td>
			<td align="${left}"><input name="BillBatchTo" id="datepicker2" type="text" style="min-width:150px; max-width:150px" onblur="return formatDate('datepicker2');"
				value='${sessionScope.searchCriteria.searchMap.BillBatchTo}' /></td>
			
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
	<form:form name="delete.do" id="uploadpayrollrunForm">
		<primo:datatable urlContext="hr/uploadpayrollrun" deletable="true"
			editable="false" insertable="false" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false">
			<primo:textcolumn headerText="Company" dataField="companyLoc.name"/>
			<primo:textcolumn headerText="Terminal" dataField="terminal.name"/>
			<primo:textcolumn headerText="Check Date" dataField="payrollinvoicedate" dataFormat="MM-dd-yyyy"/>	
			<primo:textcolumn headerText="Bill Batch From" dataField="billBatchFrom" dataFormat="MM-dd-yyyy"/>
			<primo:textcolumn headerText="Bill Batch To" dataField="billBatchTo" dataFormat="MM-dd-yyyy"/>		
			<primo:imagecolumn headerText="Download (CSV)" linkUrl="${ctx}/hr/uploadpayrollrun/downloadrun.do?id={id}&type=csv" imageSrc="${ctx}/images/csv.png" HAlign="center"/>
			<primo:imagecolumn headerText="Download (XLS)" linkUrl="${ctx}/hr/uploadpayrollrun/downloadrun.do?id={id}&type=xls" imageSrc="${ctx}/images/excel.png" HAlign="center"/>
			<primo:imagecolumn headerText="Download (PDF)" linkUrl="${ctx}/hr/uploadpayrollrun/downloadrun.do?id={id}&type=pdf" imageSrc="${ctx}/images/pdf.png" HAlign="center"/>
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
	</form:form>
</div>