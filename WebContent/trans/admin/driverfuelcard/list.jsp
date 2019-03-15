<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function editMultipleData() {
	var fuelVendor = document.getElementById('fuelvendor').value;
	if (fuelVendor == "") {
		alert("Please search by fuel vendor");
		return;
	}
	
	var inputs = document.getElementsByName("id");
	var submitForm = false;
	for (var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			submitForm = true;
		}
	}
	
	if (!submitForm) {
		alert("Please select at least one record");
		return;
	}
	
	if (!confirm("Do you want to bulk edit selected records?")) {
		return;
	}
	
	document.deleteForm.action = "bulkedit.do";
	document.deleteForm.submit();
}
</script>

<h3>
	<primo:label code="Manage Driver Fuel Cards" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Driver Fuel Cards" /></b></td>
		</tr>
		<tr>
		    <%-- <td align="${left}" class="first"><primo:label code="Driver"/></td>
			<td align="${left}"><select id="origin" name="driver.id" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${drivers}" var="item">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['driver.id'] == item.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.id}"${selected}>${item.fullName}</option>
					</c:forEach>
			</select></td> --%>
			
			
			<td align="${left}" class="first"><primo:label code="Driver"/></td>
			<td align="${left}"><select id="origin" name="driver.fullName" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${searchdriver}" var="item">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['driver.fullName'] == item.fullName}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.fullName}"${selected}>${item.fullName}</option>
					</c:forEach>
			</select></td>
		
			<td align="${left}" class="first"><primo:label code="Vehicle"/></td>
			<td align="${left}">
				<select id="vehicle.unit" name="vehicle.unit" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${vehicles}" var="item">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['vehicle.unit'] ne ''}">
							<c:if test="${sessionScope.searchCriteria.searchMap['vehicle.unit'] == item.unit}">
								<c:set var="selected" value="selected" />
							</c:if>
						</c:if>
						<option value="${item.unit}"${selected}>${item.unit}</option>
					</c:forEach>
				</select>
			</td>
			

</tr>
<tr>
	<td align="${left}" class="first"><primo:label code="Fule Vendor" /></td>
			<td align="${left}"><select id="fuelvendor" name="fuelvendor.id" style="min-width:154px; max-width:154px">
					<option value="">------
					<primo:label code="Please Select" />
					------
					</option>
					<c:forEach items="${fuelvendor}" var="fuelvendor">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['fuelvendor.id'] ==fuelvendor.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${fuelvendor.id}"${selected}>${fuelvendor.name}</option>
					</c:forEach>
			</select></td>
			
			
			<%-- <td align="${left}" class="first"><primo:label code="Name" /></td>
			<td align="${left}"><input name="name" type="text"
				value="${sessionScope.searchCriteria.searchMap.name}" /></td> --%>

			<td align="${left}" class="first"><primo:label code="Fuel Card Number" /></td>
			<td align="${left}"><select id="fuelcardnum" name="fuelcard.id" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${fuelcard}" var="fuelcard">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['fuelcard.id'] == fuelcard.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${fuelcard.id}"${selected}>${fuelcard.fuelcardNum}</option>
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

<table width="100%">
	<tr>
		<td align="left" colspan="3">		
		<img src="/trans/images/edit.png" border="0" title="BULK EDIT" class="toolbarButton"> 
			<a href="javascript:;" onclick="editMultipleData()">BULK EDIT</a>
		</td>
	
	</tr>
</table>

<br />
<form:form name="deleteForm" id="deleteForm">
	<primo:datatable urlContext="admin/driverfuelcard" deletable="true"
		editable="true" exportPdf="true" exportXls="true"
		exportCsv="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="true" searcheable="false">
		<primo:textcolumn headerText="Driver" dataField="driver.fullName" width="275px"/>
		<primo:textcolumn headerText="Unit" dataField="vehicle.unitNum"/>
		<primo:textcolumn headerText="Fuel Vendor" dataField="fuelvendor.name" width="250px"/>
		<primo:textcolumn width="200px" headerText="Fuel Card Number" dataField="fuelcard.fuelcardNum" />		
		<primo:staticdatacolumn headerText="Status" dataField="status" dataType="STATUS" />
		<primo:textcolumn headerText="Valid From" dataField="validFrom" dataFormat="MM-dd-yyyy"/>
        <primo:textcolumn headerText="Valid To" dataField="validTo" dataFormat="MM-dd-yyyy"/>
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>
