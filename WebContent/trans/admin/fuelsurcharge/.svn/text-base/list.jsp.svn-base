<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
</script>
<div class="catbody"><form:form action="search.do" method="get"
	name="searchForm">
	<table width="100%" cellspacing="10" cellpadding="5">
		<tr>
			<td valign="top" class="productbox" width="90%">
				<table width="100%">
					<tr>
							<td colspan="4">
								<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
									<tr class="table-heading" >
							            <td colspan="2"><b><primo:label code="Search Fuel Surcharge"/></b></td>
									</tr>
								</table>
							</td>
					</tr>
					<tr>
						<td align="${left}" class="first"><primo:label code="From Place"/></td>
						<td align="${left}"><select id="fromplace" name="fromPlace.id">
							<option value="">------<primo:label code="Please Select"/>------</option>
							<c:forEach items="${location}" var="location">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['location.id'] == location.id}">
								<c:set var="selected" value="selected"/>
							</c:if>
								<option value="${location.id}" ${selected}>${location.name}</option>
							</c:forEach>
						</select></td>
						<td align="${left}" class="first"><primo:label code="To Place"/></td>
						<td align="${left}"><select id="toplace" name="toPlace.id">
							<option value="">------<primo:label code="Please Select"/>------</option>
							<c:forEach items="${location}" var="location">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['location.id'] == location.id}">
								<c:set var="selected" value="selected"/>
							</c:if>
								<option value="${location.id}" ${selected}>${location.name}</option>
							</c:forEach>
						</select></td>
					</tr>
					<tr>
						<td align="${left}"></td>
						<td align="${left}"><input type="button"
							onclick="document.forms['searchForm'].submit();" value="Search" /></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</form:form></div>
<br/>
<div class="catbody">
<form:form name="delete.do" id="serviceForm">
		<primo:datatable urlContext="admin/fuelsurcharge" deletable="true" editable="true" insertable="true"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
			<primo:textcolumn headerText="From Place" dataField="fromPlace.name" />
			<primo:textcolumn headerText="To Place" dataField="toPlace.name" />
			<primo:textcolumn headerText="Rate" dataField="rate" dataFormat="#0.00" />
			<primo:textcolumn headerText="Distance" dataField="distance" />
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>
</div>


