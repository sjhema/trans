<%@include file="/common/taglibs.jsp"%>

<form:form action="search.do" method="get" name="vehicleReportSearchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Vehicle Report" /></b></td>
		</tr>
		<tr>
	   		<td align="${left}" class="first"><primo:label code="Unit#"/></td>
			<td align="${left}">
				<select id="vehicle" name="vehicle" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${vehicles}" var="item">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['vehicle'] ne ''}">
							<c:if test="${sessionScope.searchCriteria.searchMap['vehicle'] == item.unit}">
								<c:set var="selected" value="selected" />
							</c:if>
						</c:if>
						<option value="${item.unit}"${selected}>${item.unit}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
			<td align="${left}">
				<select id="company" name="company" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${companies}" var="aCompany">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['company'] == aCompany.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${aCompany.id}" ${selected}>${aCompany.name}</option>
					</c:forEach>
				</select>
			</td>
	   	</tr>
	   	<tr>
	   		<td align="${left}" class="first"><primo:label code="Type" /></td>
			<td align="${left}">
				<select id="type" name="type" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${vehicleTypes}" var="aVehicleType">
						<c:set var="selected" value="" />
						<c:if test="${sessionScope.searchCriteria.searchMap['type'] == aVehicleType.dataValue}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aVehicleType.dataValue}"${selected}>${aVehicleType.dataText}</option>
					</c:forEach>
				</select>
			</td>
	   		<td align="${left}" class="first"><primo:label code="Feature" /></td>
			<td align="${left}">
				<select id="feature" name="feature" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${features}" var="aFeature">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['feature'] == aFeature.dataValue}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aFeature.dataValue}"${selected}>${aFeature.dataText}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Status" /></td>
			<td align="${left}">
				<select id="activeStatus" name="activeStatus" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${activeStauses}" var="aStatus">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['activeStatus'] == aStatus.dataValue}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${aStatus.dataValue}"${selected}>${aStatus.dataText}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
	 	<tr>
			<td align="${left}"></td>
			<td align="${left}">
				<input type="button"
					onclick="document.forms['vehicleReportSearchForm'].submit();"
					value="<primo:label code="Preview"/>" />
			</td>
		</tr>
	</table>
</form:form>



