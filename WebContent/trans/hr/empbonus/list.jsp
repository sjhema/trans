<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function confirmRevert(id) {
	if (!confirm("Do you want to revert the selected Bonus record?")) {
		return;
	}
	
	document.location = "${ctx}/hr/empbonus/edit.do?id=" + id + "&mode=REVERT";
}
/**
 * Function for Editable Date
 */
 function next(){
	 document.nextForm.submit();
 }
function formatDate(){
	var date=document.getElementById("datepicker").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
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
						alert("invalid date format");
						document.getElementById("datepicker").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
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
<h3><primo:label code="Manage Employee Bonus"/></h3>
<div><form:form action="search.do" method="get" name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search Employee Bonus"/></b></td>
		</tr>
		
		<tr>
		
		<td align="${left}" class="first"><primo:label code="Employee"/></td>
				<td align="${left}"><select id="driver" name="driver.fullName" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${employees}" var="driver">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['driver.fullName'] == driver}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${driver}" ${selected}>${driver}</option>
					</c:forEach>
			</select></td>
			
		<td align="${left}" class="first"><primo:label code="Category"/></td>
				<td align="${left}"><select id="category" name="category.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${categories}" var="category">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['category.id'] == category.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${category.id}" ${selected}>${category.name}</option>
					</c:forEach>
			</select></td>
		<tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
				<td align="${left}"><select id="company" name="company.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${companies}" var="company">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['company.id'] == company.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${company.id}" ${selected}>${company.name}</option>
					</c:forEach>
			</select></td>
			<td align="${left}" class="first"><primo:label code="Terminal"/></td>
			<td align="${left}"><select id="terminal" name="terminal.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${terminals}" var="terminal">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['terminal.id'] == terminal.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${terminal.id}" ${selected}>${terminal.name}</option>
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
		<primo:datatable urlContext="hr/empbonus" deletable="true" editable="true" insertable="true" exportPdf="true" exportXls="true" exportCsv="true"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
			<primo:textcolumn headerText="Employee" dataField="driver.fullName" />
			<primo:textcolumn headerText="Category" dataField="category.name" />
			<primo:textcolumn headerText="Company" dataField="company.name" />			
			<primo:textcolumn headerText="Terminal" dataField="terminal.name" />
			<primo:textcolumn headerText="Batch Date From" dataField="batchFrom" dataFormat="MM-dd-yyyy"/>
			<primo:textcolumn headerText="Batch Date To" dataField="batchTo" dataFormat="MM-dd-yyyy"/>
			<primo:datecolumn headerText="Bonus Types" dataField="bonustypes"/>
			<primo:datecolumn headerText="Total amount" dataField="amounts"/>
			<primo:datecolumn headerText="Payroll Batch" dataField="payRollBatch"
			dataFormat="MM-dd-yyyy" />
			<primo:staticdatacolumn headerText="Payroll Pending" dataField="payRollStatus" dataType="Payroll_Pending" />
			<primo:anchorcolumn headerText="RVT PAY" linkUrl="javascript:confirmRevert('{id}')" linkText="RVT PAY"/>
			
			<%-- <primo:textcolumn headerText="Bonus Type" dataField="bonustype.typename" />
			<primo:datecolumn headerText="Amount" dataField="bonusamount" /> --%>
			                                                            
			</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
	</form:form>
</div>
<c:if test="${sessionScope.nextbonuses eq true}">
<form:form action="bonuscontinue.do" method="get" name="nextForm">
<input type="hidden" name="id" value="1"/>
<input type="button" value=" continue bonus"/ onclick="javascript:next();">
</form:form>
</c:if>