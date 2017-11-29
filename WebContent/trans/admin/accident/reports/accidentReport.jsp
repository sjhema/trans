<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function getForm() {
	var form = $('#accidentReportSearchForm');
	return form;
}

function processNotReportedReport() {
	var form = getForm();
	form.attr('action', 'notReportedSearch.do');
	form.submit();
}

function processReportedReport() {
	var form = getForm();
	form.attr('action', 'reportedSearch.do');
	form.submit();
}

function processAllReport() {
	var form = getForm();
	form.attr('action', 'allSearch.do');
	form.submit();
}

function formatDate(dateElemId) {
	var dateElem = document.getElementById(dateElemId);
	var date = dateElem.value;
	
	if (date != "") {
		if (date.length < 8) {
			alert("Invalidte date format");
			dateElem.value = "";
			return true;
		} else {
			var str = new String(date);
			if (!str.match("-")) {
				var mm = str.substring(0,2);
				var dd = str.substring(2,4);
				var yy = str.substring(4,8);
				var enddigit = str.substring(6,8);
				if (!enddigit == 00 && enddigit%4 == 0 ) {
					if (mm == 04 || mm == 06 || mm == 09 || mm == 11) {
						if (dd > 30) {
							alert("Invalid date format");
							dateElem.value="";
							return true;
						}
					} if (mm == 02 && dd > 29) {
						alert("Invalid date format");
						dateElem.value = "";
						return true;
					} else if (dd > 31) {
						alert("Invalid date format");
						dateElem.value="";
						return true;
					}
				} if (enddigit == 00 && yy%400 == 0) {
					if (mm == 04 || mm == 06 || mm == 09 || mm == 11) {
						if (dd > 30) {
							alert("Invalid date format");
							dateElem.value="";
							return true;
						}
					} if (mm == 02 && dd > 29) {
						alert("Invalid date format");
						dateElem.value="";
						return true;
					} else if (dd > 31) {
						alert("Invalid date format");
						dateElem.value="";
						return true;
					}					
				} else {
					if (mm == 04 || mm == 06 || mm == 09 || mm == 11) {
						if (dd > 30) {
							alert("Invalid date format");
							dateElem.value = "";
							return true;
						}
					} if (mm == 02 && dd > 28) {
						alert("Invalid date format");
						dateElem.value = "";
						return true;
					} else if (dd > 31) {
						alert("Invalid date format");
						dateElem.value = "";
						return true;
					}
				}
				date = mm+"-"+dd+"-"+yy;
				dateElem.value = date;
			}
	 	}
	}
}

</script>
<br/>

<form:form action="search.do" id="accidentReportSearchForm" name="accidentReportSearchForm" method="get">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Accident Report" /></b></td>
	    </tr>
	   <tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
			<td align="${left}">
				<select id="driverCompany" name="driverCompany" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${companies}" var="aCompany">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['driverCompany'] == aCompany.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aCompany.id}" ${selected}>${aCompany.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Employee"/></td>
			<td align="${left}">
				<select id="driver" name="driver" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${driverNames}" var="aDriverName">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['driver'] == aDriverName}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aDriverName}" ${selected}>${aDriverName}</option>
					</c:forEach>
				</select>
			</td>
	   	</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Insurance Company"/></td>
			<td align="${left}">
				<select id="insuranceCompany" name="insuranceCompany" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${insuranceCompanies}" var="anInsuranceCompany">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['insuranceCompany'] == anInsuranceCompany.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${anInsuranceCompany.id}" ${selected}>${anInsuranceCompany.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Claim No."/></td>
			<td align="${left}">
				<select id="claimNumber" name="claimNumber" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${accidents}" var="anAccident">
						<c:if test="${anAccident.claimNumber != null and anAccident.claimNumber != ''}">
							<c:set var="selected" value="" />
							<c:if test="${sessionScope.searchCriteria.searchMap['claimNumber'] == anAccident.claimNumber}">
								<c:set var="selected" value="selected" />
							</c:if>
							<option value="${anAccident.claimNumber}" ${selected}>${anAccident.claimNumber}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Recordable"/></td>
			<td align="${left}">
				<select id="recordable" name="recordable" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<option value="Yes">Yes</option>
					<option value="No">No</option>
				</select>
			</td>
		</tr>	
		<tr>
			<td align="${left}" class="first"><primo:label code="Incident Date From"/></td>
			<td align="${left}">
				<input id="datepicker" name="incidentDateFrom" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.incidentDateFrom}" /> 
			</td>
			<td align="${left}" class="first"><primo:label code="Incident Date To"/></td>
			<td align="${left}">
				<input id="datepicker1" name="incidentDateTo" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.incidentDateTo}" /> 
			</td>
		</tr>
		<!-- 
		<tr>
			<td align="${left}" class="first"><primo:label code="Paid From" /></td>
			<td>
				<input id="paidFrom" name="paidFrom" style="min-width:148px; max-width:148px" class="flat" 
					 maxlength="9" onkeypress="return onlyNumbers(event, true)" 
					 value="${sessionScope.searchCriteria.searchMap.paidFrom}"/>
			</td>
			<td align="${left}" class="first"><primo:label code="Paid To" /></td>
			<td>
				<input id="paidTo" name="paidTo" style="min-width:148px; max-width:148px" class="flat" 
					 maxlength="9" onkeypress="return onlyNumbers(event, true)" 
					 value="${sessionScope.searchCriteria.searchMap.paidTo}"/>
			</td>
		</tr>
		-->
		<tr>
			<td align="${left}" class="first"><primo:label code="Total Cost From" /></td>
			<td>
				<input id="totalCostFrom" name="totalCostFrom" style="min-width:148px; max-width:148px" class="flat" 
					 maxlength="9" onkeypress="return onlyNumbers(event, true)" 
					 value="${sessionScope.searchCriteria.searchMap.totalCostFrom}"/>
			</td>
			<td align="${left}" class="first"><primo:label code="Total Cost To" /></td>
			<td>
				<input id="totalCostTo" name="totalCostTo" style="min-width:148px; max-width:148px" class="flat" 
					 maxlength="9" onkeypress="return onlyNumbers(event, true)" 
					 value="${sessionScope.searchCriteria.searchMap.totalCostTo}"/>
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3">
				<input type="button"
					onclick="javascript:processAllReport()" value="     All     " />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button"
					onclick="javascript:processReportedReport()" value="Reported" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button"
					onclick="javascript:processNotReportedReport()" value="Not Reported" />
			</td>
		</tr>
	</table>
</form:form>

