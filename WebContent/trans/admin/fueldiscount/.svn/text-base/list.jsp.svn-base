<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
</script>
<h3><primo:label code="Manage FuelDiscount"/></h3>
<div><form:form action="search.do" method="get"
	name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search FuelDiscount"/></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><label>Fuel Vendor</label>
			</td>
			<td align="${left}"><select id="fuelvendor" name="fuelvendor.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${fuelvendors}" var="fuelvendor">
				<c:set var="selected" value=""/>
				<c:if test="${sessionScope.searchCriteria.searchMap['fuelvendor.id'] == fuelvendor.id}">
					<c:set var="selected" value="selected"/>
				</c:if>
					<option value="${fuelvendor.id}" ${selected}>${fuelvendor.name}</option>
				</c:forEach>
			</select></td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();" value="<primo:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form></div>
<br/>
<div style="width:100%; margin:0px auto;"><form:form name="delete.do" id="customerForm">
<%request.getSession().setAttribute("typ",null); %>
		<primo:datatable urlContext="admin/fueldiscount" 
		deletable="true" editable="true" insertable="true" exportPdf="true" exportXls="true" exportCsv="true"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
			<primo:textcolumn headerText="Fuel Vendor" dataField="fuelvendor.name" />
			<primo:textcolumn headerText="Discount" dataField="fuelDiscountPercentage"/>
			<primo:datecolumn headerText="From Date" dataField="fromDate" dataFormat="MM-dd-yyyy"/>	
			<primo:datecolumn headerText="To Date" dataField="toDate" dataFormat="MM-dd-yyyy"/>
			<%-- <primo:anchorcolumn headerText="Manage Alert" dataField="status" linkUrl="/admin/fuelsurchargeweeklyrate/changestatus.do?id={id}" linkText="On alert"/>	 --%>
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
	</form:form>
</div>