<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function processReport() {
	var form = $('#driverPayAdjustReportSearchForm');
	form.attr('action', 'search.do');
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

<form:form action="search.do" id="driverPayAdjustReportSearchForm" name="driverPayAdjustReportSearchForm" method="get">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Driver Pay Adjustmnet Report" /></b></td>
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
			<td align="${left}" class="first"><primo:label code="Terminal"/></td>
			<td align="${left}">
				<select id="terminal" name="terminal" style="min-width:154px; max-width:154px">
					<option value="">-----<primo:label code="Please Select" />-----</option>
					<c:forEach items="${terminals}" var="aTerminal">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['terminal'] == aTerminal.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aTerminal.id}" ${selected}>${aTerminal.name}</option>
					</c:forEach>
				</select>
			</td>
			
	   	</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Driver"/></td>
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
			<td align="${left}" class="first"><primo:label code="SSN"/></td>
			<td>
				<input id="ssn" name="ssn" type="text" value="${sessionScope.searchCriteria.searchMap.ssn}"/>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Check Date From"/></td>
			<td align="${left}">
				<input id="datepicker" name="checkDateFrom" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.checkDateFrom}" /> 
			</td>
			<td align="${left}" class="first"><primo:label code="Check Date To"/></td>
			<td align="${left}">
				<input id="datepicker1" name="checkDateTo" style="min-width:148px; max-width:148px" class="flat" 
					 value="${sessionScope.searchCriteria.searchMap.checkDateTo}" /> 
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3">
				<input type="button" onclick="javascript:processReport()" value="Preview" />
			</td>
		</tr>
	</table>
</form:form>

