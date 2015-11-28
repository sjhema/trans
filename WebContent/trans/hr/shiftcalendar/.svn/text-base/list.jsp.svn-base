<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Shift Calendar" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Shift Calendar" /></b></td>
		</tr>
		<%-- <tr>
			<td align="${left}" class="first"><primo:label code="Name"/></td>
			<td align="${left}"><select id="name" name="id">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${ptods}" var="name">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['id'] == name.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${name.id}" ${selected}>${name.name}</option>
				</c:forEach>
			</select>
			</td>
	    </tr> --%>
        <tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
				<td align="${left}"><select id="company" name="company.id">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${companies}" var="company">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['company.id'] == company.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${company.id}" ${selected}>${company.name}</option>
					</c:forEach>
			</select></td>
			
			<td align="${left}" class="first"><primo:label code="Name"/></td>
				<td align="${left}"><select id="name" name="name.id">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${shiftnames}" var="name">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['name.id'] == name.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${name.id}" ${selected}>${name.name}</option>
					</c:forEach>
			</select></td>
			
			
			
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
	<primo:datatable urlContext="hr/shiftcalendar" deletable="true"
		editable="true" insertable="true" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		
		
	    <primo:textcolumn headerText="Name" dataField="name" />
		<primo:textcolumn headerText="Company" dataField="company.name" />
		<primo:datecolumn headerText="Effective Start Date" dataField="effectivestartdate" dataFormat="MM-dd-yyyy"/>
		<primo:datecolumn headerText="Effective End Date" dataField="effectiveenddate" dataFormat="MM-dd-yyyy"/>
		
		<%-- <primo:textcolumn headerText="Start Time Hours" dataField="starttimehours" />
		<primo:textcolumn headerText="Start Time Minutes" dataField="starttimeminutes" /> --%>
		<primo:textcolumn headerText="Start Time" dataField="starttime" />
		
		
		<%-- <primo:textcolumn headerText="End Time Hours" dataField="endtimehours" />
		<primo:textcolumn headerText="End Time Minutes" dataField="endtimeminutes" /> --%>
		<primo:textcolumn headerText="End Time" dataField="endtime" />
		<primo:staticdatacolumn headerText="Status" dataField="status" dataType="STATUS" />
		
		
		
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


