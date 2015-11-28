<%@include file="/common/taglibs.jsp"%>
<h3>
	<primo:label code="Manage Customer" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Customer" /></b></td>
		</tr>
		<tr>
			<%-- <td align="${left}" class="first"><primo:label code="First Name" /></td>
			<td align="${left}"><input name="firstName" type="text"
				value="${sessionScope.searchCriteria.searchMap.firstName}" /></td>
			<td align="${left}" class="first"><primo:label code="Last Name" /></td>
			<td align="${left}"><input name="lastName" type="text"
				value="${sessionScope.searchCriteria.searchMap.lastName}" /></td>
	 --%>	
	 <td align="${left}" class="first"><primo:label code="Name"/></td>
			<td align="${left}"><select id="customer" name="id" style="min-width:350px; max-width:350px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${customer}" var="customer">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['id'] == customer.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${customer.id}" ${selected}>${customer.name}</option>
				</c:forEach>
			</select>
			</td>
			
			<td align="${left}" class="first"><primo:label code="Customer ID"/></td>
			<td align="${left}"><select id="customerId" name="customerNameID" style="min-width:200px; max-width:200px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${customerIds}" var="customerId">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['customerNameID'] == customerId.customerNameID}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${customerId.customerNameID}" ${selected}>${customerId.customerNameID}</option>
				</c:forEach>
			</select>
			</td>
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
	<primo:datatable urlContext="admin/customer" deletable="true"
		editable="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<primo:textcolumn headerText="Name" dataField="name" />
		<primo:textcolumn headerText="Customer ID" dataField="customerNameID" />
		<primo:textcolumn headerText="Address Line1" dataField="address" />
		<primo:textcolumn headerText="Address Line2" dataField="address2" />
		<primo:textcolumn headerText="City" dataField="city" />
		<primo:textcolumn headerText="State" dataField="state.name" />
		<primo:textcolumn headerText="Zipcode" dataField="zipcode" />
		<primo:textcolumn headerText="Phone" dataField="phone" />
		<primo:textcolumn headerText="Fax" dataField="fax" />
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


