<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function processMiscellaneousPayReport() {
	var batchDateFrom = $("[name='batchDateFrom']").val();
	var batchDateTo = $("[name='batchDateTo']").val();
	if ((batchDateFrom != '' && batchDateTo == '') ||
			(batchDateTo != '' && batchDateFrom == '')) {
		alert("Please enter both Batch Date From and To");
		return;
	}
	
	document.forms[0].submit();
}

function formatDate(datepickerId){
	var date=document.getElementById(datepickerId).value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById(datepickerId).value="";
		return true;
	}
	else{
		var str=new String(date);
		if(!str.match("-")){
			var mm=str.substring(0,2);
			var dd=str.substring(2,4);
			var yy=str.substring(4,8);
			var enddigit=str.substring(6,8);
			if(!enddigit==00 && enddigit%4==0 ){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("Invalid date format");
						document.getElementById(datepickerId).value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("Invalid date format");
					document.getElementById(datepickerId).value="";
					return true;
				}
				else if(dd>31){
					alert("Invalid date format");
					document.getElementById(datepickerId).value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("Invalid date format");
						document.getElementById(datepickerId).value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("Invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}else if(dd>31){
					alert("Invalid date format");
					document.getElementById(datepickerId).value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("Invalid date format");
						document.getElementById(datepickerId).value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("Invalid date format");
					document.getElementById(datepickerId).value="";
					return true;
				}else if(dd>31){
					alert("Invalid date format");
					document.getElementById(datepickerId).value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById(datepickerId).value=date;
		}
	 }
   }
}

$(document).ready(function() {
	$("#miscellaneousDesc").multiselect();
});
</script>
<br/>

<form:form action="search.do" name="searchForm" method="post" commandName="modelObject">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Miscellaneous Pay Report" /></b></td>
	    </tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
			<td align="${left}">
				<select id="company" name="company" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${companies}" var="aCompany">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['company'] == aCompany}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aCompany.id}" ${selected}>${aCompany.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Terminal"/></td>
			<td align="${left}">
				<select id="terminal" name="terminal" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${terminals}" var="aTerminal">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['terminal'] == aTerminal}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${aTerminal.id}" ${selected}>${aTerminal.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Employee"/></td>
			<td align="${left}">
				<select id="employee" name="employee" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
						<c:forEach items="${employees}" var="anEmployee">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['employee'] == anEmployee}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${anEmployee.fullName}" ${selected}>${anEmployee.fullName}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Misc. Description"/></td>
			<td align="${left}">
				<select id="miscellaneousDesc" name="miscellaneousDesc" multiple style="min-width:154px; max-width:154px">
					<c:forEach items="${miscellaneousDesc}" var="anMiscellaneousDesc">
						<option value="${anMiscellaneousDesc.dataLabel}">${anMiscellaneousDesc.dataLabel}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Batch Date From" /></td>
			<td align="${left}">
				<input id="datepicker1" name="batchDateFrom" style="min-width:150px; max-width:150px"
					type="text" onblur="return formatDate('datepicker1');"
					value="${sessionScope.searchCriteria.searchMap.batchDateFrom}"/>
		    </td>
		    <td align="${left}" class="first"><primo:label code="Batch Date To" /></td>
			<td align="${left}">
				<input id="datepicker2" name="batchDateTo" style="min-width:150px; max-width:150px"
					type="text" onblur="return formatDate('datepicker2');"
					value="${sessionScope.searchCriteria.searchMap.batchDateTo}"/>
		    </td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3">
				<input type="button"
					onclick="javascript:processMiscellaneousPayReport()" value="Preview" />
			</td>
		</tr>
	</table>
</form:form>

