<%@include file="/common/taglibs.jsp"%>

<form:form action="search.do" method="post" name="searchForm">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5" align="left">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Odometer Reset" /> </b>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Truck#"/></td>
			<td align="${left}">
				<select id="truck.unit" name="truck.unit" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${trucks}" var="item">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['truck.unit'] == item.unit}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.unit}"${selected}>${item.unit}</option>
					</c:forEach>
			</select></td>
		</tr>  
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="javascript:document.forms['searchForm'].submit();"
				value="<primo:label code="Search"/>" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<div style="width: 100%; margin: 0px auto;">
<form:form action="delete.do" id="deleteForm" name="deleteForm">
	<primo:datatable urlContext="admin/odometerreset" deletable="true"
		editable="true" exportPdf="true" exportXls="true"
		exportCsv="false" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" searcheable="false" >
		<primo:textcolumn headerText="Truck#" dataField="tempTruck"/>
		<primo:textcolumn headerText="Reset Date" dataField="resetDate" dataFormat="MM-dd-yyyy" />	
		<primo:textcolumn headerText="Reset reading" dataField="resetReading"/>
		<primo:textcolumn headerText="Reset by" dataField="enteredBy"/>
	</primo:datatable>
	<% session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
</form:form>
</div>