<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Tolls" />
</h3>
<form:form action="search.do" method="post" name="searchForm">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b>Search Tolls</b>
			</td>
		</tr>
		
	<tr>
			<td align="${left}" class="first"><primo:label code="Toll Company"/></td>
			<td align="${left}"><select id="tollcompany" name="toolcompany" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${tollcompanies}" var="tollcompany">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['toolcompany'] == tollcompany.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${tollcompany.id}" ${selected}>${tollcompany.name}</option>
				</c:forEach>
			</select></td>	
			
			<td align="${left}" class="first"><primo:label code="Unit#"/></td>
			<td align="${left}"><select id="unit" name="plateNumber.unit.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${trucks}" var="unit">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['plateNumber.unit.id'] == unit.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${unit.id}" ${selected}>${unit.unit}</option>
				</c:forEach>
			</select></td>
			
		</tr>				
		<tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
			<td align="${left}"><select id="company" name="company.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${companyLocation}" var="company">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['company.id'] == company.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${company.id}" ${selected}>${company.name}</option>
				</c:forEach>
			</select></td>
			
			<td align="${left}" class="first"><primo:label code="Tag Number"/></td>
			<td align="${left}"><select id="tagNumber" name="tollTagNumber.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${tollTagNumbers}" var="tagNumber">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['tollTagNumber.id'] == tagNumber.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${tagNumber.id}" ${selected}>${tagNumber.tollTagNumber}</option>
				</c:forEach>
			</select></td>
		
		</tr>
		
		
		<tr>
			
			
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
			
			
			
			<td align="${left}" class="first"><primo:label code="Plate Number"/></td>
			<td align="${left}"><select id="plateNumber" name="plateNumber.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${vehicleplates}" var="plateNumber">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['plateNumber.id'] == plateNumber.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${plateNumber.id}" ${selected}>${plateNumber.plate}</option>
				</c:forEach>
			</select></td>
		
		</tr>
		
		<tr>
		
			<td align="${left}" class="first"><primo:label code="Driver"/></td>
			<td align="${left}"><select id="driver" name="driver.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${drivers}" var="driver">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['driver.id'] == driver.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${driver.id}" ${selected}>${driver.fullName}</option>
				</c:forEach>
			</select></td>
		
		</tr>
		<tr>
			<%-- <td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();" value="Search" />
			</td> --%>
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
	
		
	<%-- <primo:datatable urlContext="operator/eztoll" deletable="true"
		editable="true" exportPdf="true" exportXls="true"
		exportCsv="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="true" searcheable="false">
			
			
			
			<primo:textcolumn headerText="Toll company" dataField="toolcompany.name"/>
			<primo:textcolumn headerText="company" dataField="company.name"/>
			<primo:textcolumn headerText="Terminal" dataField="terminal.name"/>
			<primo:textcolumn headerText="Unit Number" dataField="units"/>	
			<primo:textcolumn headerText="Tag Number" dataField="tollTagNumber.tollTagNumber"/>
			<primo:textcolumn headerText="Plate Number" dataField="plateNumber.plate"/>
			<primo:textcolumn headerText="Driver" dataField="driver.fullName"/>
			<primo:textcolumn headerText="Transaction Date" dataField="transactiondate" dataFormat="MM-dd-yyyy"/>
			<primo:textcolumn headerText="Agency" dataField="agency" />
			
			
			<primo:textcolumn headerText="Unit#" dataField="unit.unit" type="int"/>
	</primo:datatable> --%>
	
	<primo:datatable urlContext="operator/eztoll" deletable="true"
		editable="true" exportPdf="true" exportXls="true"
		exportCsv="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="true" searcheable="false">
			
			
			
			<primo:textcolumn headerText="Toll company" dataField="toolcompany.name"/>
			<primo:textcolumn headerText="company" dataField="company.name"/>
			<primo:textcolumn headerText="Terminal" dataField="terminal.name"/>
			<primo:textcolumn headerText="Unit Number" dataField="unit.unitNum" />
			<primo:textcolumn headerText="Tag Number" dataField="tollTagNumber.tollTagNumber"/>
			<primo:textcolumn headerText="Plate Number" dataField="plateNumber.plate"/>
			<primo:textcolumn headerText="Driver" dataField="driver.fullName"/>
			<primo:textcolumn headerText="Transaction Date" dataField="transactiondate" dataFormat="MM-dd-yyyy"/>
			<primo:textcolumn headerText="Agency" dataField="agency" />
			
			
			<%-- <primo:textcolumn headerText="Unit#" dataField="unit.unit" type="int"/> --%>
	</primo:datatable>
	
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
	</form:form>
</div>