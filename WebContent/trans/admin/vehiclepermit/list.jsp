<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Vehicle Permit" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Vehicle Permit" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Vehicle#"/></td>
			<td align="${left}"><select id="vehicle" name="vehicle.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${vehicle}" var="vehicle">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['vehicle.id'] == vehicle.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${vehicle.id}" ${selected}>${vehicle.unit}</option>
					</c:forEach>
				</select>
			</td>
			
			<td align="${left}" class="first"><label>Company</label>
			</td>
				<td align="${left}"><select id="companyLocation" name="companyLocation.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${companies}" var="companylocation">
				<c:set var="selected" value=""/>
				<c:if test="${sessionScope.searchCriteria.searchMap['companyLocation.id'] ==companylocation.id}">
					<c:set var="selected" value="selected"/>
				</c:if>
					<option value="${companylocation.id}" ${selected}>${companylocation.name}</option>
				</c:forEach>
			</select></td>
			
			<tr>
		<td align="${left}" class="first"><primo:label code="Permit Type" /></td>
		
			<td align="${left}"><select id="permittype" name="permitType" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${permitType}" var="permittype">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['permitType'] == permittype.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${permittype.id}" ${selected}>${permittype.dataLabel}</option>
					</c:forEach>
				</select>
			</td>
		<td align="${left}" class="first"><primo:label code="Permit Number" /></td>
			<td align="${left}"><input name="permitNumber" type="text" style="min-width:150px; max-width:150px"
				value="${sessionScope.searchCriteria.searchMap.permitNumber}" /></td>
		</tr>
			
			
			
			
			
		</tr>
		
			<tr>
			<td align="${left}" class="first"><primo:label code="Effective Date"/></td>
			<td align="${left}"><input id="datepicker" name="issuedate" type="text" style="min-width:150px; max-width:150px"
				value="${sessionScope.searchCriteria.searchMap.issuedate}" /></td>
				
				
			<td align="${left}" class="first"><primo:label code="Expiration Date" /></td>
			<td align="${left}"><input id="datepicker1" name="expirydate" type="text" style="min-width:150px; max-width:150px"
				value="${sessionScope.searchCriteria.searchMap.expirydate}" /></td>
		</tr>
		
		
		
		
		
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();"
				value="<primo:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="delete.do" id="serviceForm">
	<primo:datatable urlContext="admin/vehiclepermit" deletable="true"
		editable="true" exportPdf="true" exportXls="true"
		exportCsv="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Vehicle#" dataField="vehicle.unitNum" />
		<primo:textcolumn headerText="Company" dataField="companyLocation.name" />
		<primo:textcolumn headerText="PERMIT TYPE" dataField="permitType.dataLabel" />
		<primo:textcolumn headerText="Permit Number" dataField="permitNumber" />
		<primo:textcolumn headerText="Effective Date" dataField="issueDate" dataFormat="MM-dd-yyyy"/>
		<primo:textcolumn headerText="Expiration Date" dataField="expirationDate" dataFormat="MM-dd-yyyy"/>
		
		<%-- <primo:anchorcolumn headerText="Copy"  linkUrl="/admin/vehiclepermit/copy.do?vpid={id}" linkText="Copy"/> --%>
		
		<primo:anchorcolumn headerText="Copy"  dataField="''"  linkUrl="/admin/vehiclepermit/copy.do?vpid={id}" linkText="Copy"/>
		
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


