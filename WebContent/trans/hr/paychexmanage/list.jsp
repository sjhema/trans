<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
</script>
<h3><primo:label code="Manage Pay Chex"/></h3>
<div><form:form action="search.do" method="get"
	name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search Pay Chex"/></b></td>
		</tr>
			<tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
				<td align="${left}"><select id="company" name="companyLocation.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${companies}" var="company">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['companyLocation.id'] == company.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${company.id}" ${selected}>${company.name}</option>
					</c:forEach>
			</select></td>
			<td align="${left}" class="first"><primo:label code="Terminal"/></td>
			<td align="${left}"><select id="terminal" name="terminalLocation.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${terminals}" var="terminal">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['terminalLocation.id'] == terminal.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${terminal.id}" ${selected}>${terminal.name}</option>
				</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td align="${left}" class="first">Check Date</td>
			<td align="${left}"><input name="checkDate" style="min-width:150px; max-width:150px" id="datepicker" type="text" onblur="return formatDate('datepicker');"
				value='${sessionScope.searchCriteria.searchMap.checkDate}' /></td>
			
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
		<primo:datatable urlContext="hr/paychexmanage" deletable="true"
			editable="false" insertable="false" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false">
			<primo:textcolumn headerText="Company" dataField="companyLocation.name" />
			<primo:textcolumn headerText="Terminal" dataField="terminalLocation.name" />
			<primo:textcolumn headerText="Check Date" dataField="checkDate" dataFormat="MM-dd-yyyy"/>			
			<primo:imagecolumn headerText="Download (CSV)" linkUrl="${ctx}/hr/paychexmanage/download.do?id={id}&type=csv" imageSrc="${ctx}/images/csv.png" HAlign="center"/>
			<primo:imagecolumn headerText="Download (XLS)" linkUrl="${ctx}/hr/paychexmanage/download.do?id={id}&type=xls" imageSrc="${ctx}/images/excel.png" HAlign="center"/>
			<primo:imagecolumn headerText="Download (PDF)" linkUrl="${ctx}/hr/paychexmanage/download.do?id={id}&type=pdf" imageSrc="${ctx}/images/pdf.png" HAlign="center"/>
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
	</form:form>
</div>