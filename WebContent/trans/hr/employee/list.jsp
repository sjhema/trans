<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">

/**
 * Function for Editable Date
 */
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
<h3><primo:label code="Manage Employee"/></h3>
<div><form:form action="search.do" method="get"
	name="searchForm">
	<table align="left" id="form-table" width="65%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search Employee"/></b></td>
		</tr>
		
		<tr>
			<td align="${left}" class="first"><primo:label code="Employee Id"/></td>
			<td>
			<input id="staffId" name="staffId"
					type="text" 
					value="${sessionScope.searchCriteria.searchMap.staffId}"/>
			</td>
			<td align="${left}" class="first"><primo:label code="SSN"/></td>
			<td>
				<input id="ssn" name="ssn" type="text" value="${sessionScope.searchCriteria.searchMap.ssn}"/>
			</td>
		</tr>
		<%-- <tr>
			<td align="${left}" class="first"><primo:label code="Driver Name"/></td>
				<td align="${left}"><select id="driver" name="id">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${driver}" var="item">
					<c:set var="selected" value=""/>
					<c:if
							test="${sessionScope.searchCriteria.searchMap['id'] == item.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${item.id}"${selected}>${item.fullName}</option>
					</c:forEach>
			</select></td>

			<td align="${left}" class="first"><primo:label code="Date Hired" />
			</td>
			<td align="${left}"><input id="datepicker" name="Hireddate"
				type="text" onblur="return formatDate();"
				value="${sessionScope.searchCriteria.searchMap.Hireddate}"/>
			</td>
			
		</tr> --%>
		<tr>
		
		<td align="${left}" class="first"><primo:label code="Name"/></td>
				<td align="${left}"><select id="company" name="fullName" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${employees}" var="employee">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['fullName'] == employee}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${employee}" ${selected}>${employee}</option>
					</c:forEach>
			</select></td>
			
		<td align="${left}" class="first"><primo:label code="Category"/></td>
				<td align="${left}"><select id="company" name="catagory.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${catagories}" var="catagory">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['catagory.id'] == catagory.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${catagory.id}" ${selected}>${catagory.name}</option>
					</c:forEach>
			</select></td>
		<tr>
			<td align="${left}" class="first"><primo:label code="Status"/></td>
			<td>	
			<select id="status" name="status" style="min-width:154px; max-width:154px">
						<option value="">------<primo:label code="Please Select"/>------</option>
						<c:forEach items="${employeestatus}" var="employeestatus">
						<c:set var="selected" value=""/>
						<c:if test="${sessionScope.searchCriteria.searchMap['status'] == employeestatus.dataValue}">
							<c:set var="selected" value="selected"/>
						</c:if>
							<option value="${employeestatus.dataValue}" ${selected}>${employeestatus.dataText}</option>
						</c:forEach>
				</select>
			
			</td>
		</tr>
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
		<primo:datatable urlContext="hr/employee" deletable="true" editable="true" insertable="true" exportPdf="true" exportXls="true" exportCsv="true"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
			<primo:textcolumn headerText="Employee Id" dataField="staffId" />
			<primo:textcolumn headerText="SSN" dataField="ssn" />
			<primo:textcolumn headerText="Last Name" dataField="lastName" />
			<primo:textcolumn headerText="First Name" dataField="firstName" />
			<primo:textcolumn headerText="Category" dataField="catagory.name" />
			<primo:datecolumn headerText="Date&nbspHired" dataField="dateHired" dataFormat="MM-dd-yyyy"/>
			<primo:datecolumn headerText="ReHired&nbspDate" dataField="dateReHired" dataFormat="MM-dd-yyyy"/>
			<primo:datecolumn headerText="Probation Start Date" dataField="dateProbationStart" dataFormat="MM-dd-yyyy"/>
			<primo:datecolumn headerText="Probation End Date" dataField="dateProbationEnd" dataFormat="MM-dd-yyyy"/>
			<primo:datecolumn headerText="Anniversary Date" dataField="anniversaryDate" dataFormat="MM-dd-yyyy"/>
			<primo:datecolumn headerText="Next Anniversary Date" dataField="nextAnniversaryDate" dataFormat="MM-dd-yyyy"/>
			
			<primo:textcolumn headerText="Probation Period" dataField="probationDays" />			                                                             
			<primo:textcolumn headerText="Company" dataField="company.name"/>
			<primo:textcolumn headerText="Terminal" dataField="terminal.name"/>	
			<primo:datecolumn headerText="Date Terminated" dataField="dateTerminated" dataFormat="MM-dd-yyyy"/>	
			<primo:staticdatacolumn headerText="Pay Term" dataField="payTerm" dataType="Pay Term" />
			<primo:staticdatacolumn headerText="Shift" dataField="shift" dataType="SHIFT" />
			
			<primo:staticdatacolumn headerText="Status" dataField="status" dataType="STATUS" />
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
	</form:form>
</div>