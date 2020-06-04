<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
var weeklySalaryRateUrl = "${ctx}/hr/weeklysalaryrate/";

function constructBaseUrl(action) {
	return (weeklySalaryRateUrl + action + "?fromAlertPage=true");
}

function confirmDelete(id) {
	if (!confirm("Are you sure you want to delete the selected record?")) {
		return;
	}
	
	var href = constructBaseUrl("delete.do") + "&id=" + id;
	document.location.href = href
}

function create() {
	var href = constructBaseUrl("create.do");
	document.location.href = href
}

function changeAlertStatus(id, rateStatus) {
	if (rateStatus == "CURRENT") {
		alert("Unable to process your request. Please check rate status");
		return;
	}
	
	var href = constructBaseUrl("changeAlertStatus.do") + "&status=0&id=" + id;
	document.location.href = href
}


</script>

<h3><primo:label code="Expired/Expiring Weekly Salary Rate(s)"/></h3>
<div>
	<form:form action="search.do" method="get" name="searchForm">
		<input type="hidden" id="type" name="type" value="weeklySalaryRate"/>
		<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
			<tr class="table-heading">
				<td align="${left}" colspan="9"><b><primo:label code="Search Weekly Salary Rate Alert"/></b></td>
			</tr>
			<tr>
			 	<td align="${left}" class="first"><primo:label code="Company"/></td>
				<td align="${left}">
					<select id="company" name="company.id" style="min-width:154px; max-width:154px">
						<option value="">------<primo:label code="Please Select"/>------</option>
						<c:forEach items="${companies}" var="company">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['company'] == company.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
							<option value="${company.id}" ${selected}>${company.name}</option>
						</c:forEach>
					</select>
				</td>
				<td align="${left}" class="first"><primo:label code="Terminal"/></td>
				<td align="${left}">
					<select id="terminal" name="terminal.id" style="min-width:154px; max-width:154px">
						<option value="">------<primo:label code="Please Select"/>------</option>
						<c:forEach items="${terminals}" var="terminal">
							<c:set var="selected" value=""/>
							<c:if test="${sessionScope.searchCriteria.searchMap['terminal'] == terminal.id}">
								<c:set var="selected" value="selected"/>
							</c:if>
								<option value="${terminal.id}" ${selected}>${terminal.name}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td align="${left}"></td>
				<td align="${left}">
					<input type="button" onclick="document.forms['searchForm'].submit();" value="<primo:label code="Search"/>" />
				</td>
			</tr>
		</table>
	</form:form>
</div>
<br/>
<table width="100%">
	<tbody>
		<tr>
			<td>
				<a href="javascript:;" onclick="javascript:create();">
					<img src="${ctx}/images/add.png" title="Add" class="toolbarButton" border="0">
				</a>&nbsp;
			</td>
			<td width="90"></td>
		</tr>
	</tbody>
</table>
<div style="width:100%; margin:0px auto;">
	<form:form name="serviceForm" id="serviceForm">
		<primo:datatable urlContext="hr/weeklysalaryrate" deletable="false" editable="false" insertable="false" 
			exportPdf="false" exportXls="false" exportCsv="false"
			baseObjects="${expiredWeeklySalaryRateList}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
			<primo:textcolumn headerText="Employee" dataField="driver.fullName" />
			<primo:textcolumn headerText="Staff Id" dataField="staffId" />
			<primo:textcolumn headerText="Company" dataField="company.name" />
			<primo:textcolumn headerText="Terminal" dataField="terminal.name" />
			<primo:textcolumn headerText="Category" dataField="catagory.name" />
			<primo:textcolumn headerText="Weekly Pay" dataField="weeklySalary" />
			<primo:textcolumn headerText="Daily Pay" dataField="dailySalary" />
			<primo:datecolumn headerText="Valid From" dataField="validFrom" dataFormat="MM-dd-yyyy" />
			<primo:datecolumn headerText="Valid To" dataField="validTo" dataFormat="MM-dd-yyyy" />
			<primo:anchorcolumn headerText="Manage Alert" dataField="alertStatus" linkUrl="javascript:changeAlertStatus('{id}', '{rateStatus}');" linkText="Off alert"/>	
			<primo:imagecolumn headerText="EDIT" linkUrl="${ctx}/hr/weeklysalaryrate/edit.do?id={id}&fromAlertPage=true" imageSrc="${ctx}/images/edit.png" HAlign="center" width="25px"/>
 			<primo:imagecolumn headerText="DEL" linkUrl="javascript:confirmDelete('{id}');" imageSrc="${ctx}/images/delete.png" HAlign="center" width="25px"/>
       	 </primo:datatable>
		 <%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
	</form:form>
</div>
		