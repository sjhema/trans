<%@include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function formatDate(){
	var date=document.getElementById("datepicker").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalid date format");
		document.getElementById("datepicker").value="";
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
						document.getElementById("datepicker").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("Invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}
				else if(dd>31){
					alert("Invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("Invalid date format");
						document.getElementById("datepicker").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("Invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}else if(dd>31){
					alert("Invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("Invalid date format");
						document.getElementById("datepicker").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("Invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}else if(dd>31){
					alert("Invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker").value=date;
		}
	 }
   }
}
</script>

<h3><primo:label code="Manage Salary Override"/></h3>
<div>
<form:form action="search.do" method="get" name="salaryOverrideSearchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search Salary Override"/></b></td>
		</tr>
		<tr>
			<td class="form-left">Employee</td>
			<td align="${left}">
				<select id="driver" name="driver.fullName" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${employees}" var="driver">
						<option value="${driver.fullName}" ${selected}>${driver.fullName}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first">Batch Date</td>
			<td align="${left}">
				<input name="payrollBatch" id="datepicker" type="text" style="min-width:154px; max-width:154px" onblur="return formatDate();" />
			</td>
		</tr>
		<tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['salaryOverrideSearchForm'].submit();" value="Search" />
			</td>
		</tr>
	</table>
</form:form>
</div>
<br />
<div style="width:100%; margin:0px auto;">
	<form:form name="salaryOverrideEditForm" id="salaryOverrideEditForm">
		<primo:datatable urlContext="hr/salaryoverride" deletable="true" editable="true" insertable="true" 
			exportPdf="false" exportXls="false" exportCsv="false"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
			<primo:textcolumn headerText="Employee" dataField="driver.fullName" />
			<primo:textcolumn headerText="Batch Date" dataField="payrollBatch" dataFormat="MM-dd-yyyy"/>
			<primo:textcolumn headerText="Override Amount" dataField="amount" width="200 px"/>
			<primo:textcolumn headerText="Notes" dataField="notes" />                            
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
	</form:form>
</div>
