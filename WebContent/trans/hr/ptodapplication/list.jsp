<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
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
<h3>
	<primo:label code="Manage PTOD Application" />
</h3>

<form:form action="search.do" method="get" name="searchForm">
	<table align="left" width="60%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search PTOD Application" /></b></td>
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
			<td align="${left}" class="first"><primo:label code="SSN"/></td>
			<td>
				<input id="driver.ssn" name="driver.ssn" type="text" value="${sessionScope.searchCriteria.searchMap.driver.ssn}"/>
			</td>
	   </tr>
	   <tr>
	   		<td align="${left}" class="first"><primo:label code="Category"/></td>
			<td align="${left}"><select id="category" name="category.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${catagories}" var="category">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['category.id'] == category.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${category.id}" ${selected}>${category.name}</option>
				</c:forEach>
			</select></td>		
			
			<td align="${left}" class="first"><primo:label code="Leave Type" /></td>
			<td align="${left}"><select id="leavetype" name="leavetype.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${leavetypes}" var="leavetype">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['leavetype.id'] == leavetype.id}">
						<c:set var="selected" value="selected"/>
					</c:if>
						<option value="${leavetype.id}" ${selected}>${leavetype.name}</option>
				</c:forEach>
			</select></td>		
			
		</tr>	
			
			
			
	    <tr>	
	    	<td align="${left}" class="first"><primo:label code="Company"/></td>
				<td align="${left}"><select id="company" name="company.id" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${companyLocation}" var="company">
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
			
			<%-- <td align="${left}"><select id="leavetype" name="leavetype">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${LeaveTypes}" var="leaveTypes">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['leavetype'] == leaveTypes.dataValue}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${leaveTypes.dataText}"${selected}>${leaveTypes.dataText}</option>
					</c:forEach>
			</select></td> --%>
	   </tr>
	   <tr>
	  	 <td align="${left}" class="first"><primo:label code="Batch Date" />
			</td>
			<td align="${left}"><input id="datepicker" name="batchdate" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate();"
				value=""/>
			</td>
	   </tr>
	   
	 
	  <tr>
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();"
				value="<primo:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>
<br />
<form:form name="delete.do" id="serviceForm">
	<primo:datatable urlContext="hr/ptodapplication" deletable="true"
		editable="true" insertable="true" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		<br/>
		<%-- <primo:textcolumn headerText="Name" dataField="name" /> --%>
		<primo:textcolumn headerText="Company" dataField="company.name" />
		<primo:textcolumn headerText="Terminal" dataField="terminal.name" />		
		<primo:textcolumn headerText="Category" dataField="category.name" />
		<primo:textcolumn headerText="Employee" dataField="driver.fullName" />
		<primo:datecolumn headerText="Hire&nbsp;Date" dataField="driver.dateHired" dataFormat="MM-dd-yyyy"/>
		<primo:datecolumn headerText="Rehire&nbsp;Date" dataField="driver.dateReHired" dataFormat="MM-dd-yyyy"/>
		<primo:textcolumn headerText="Leave Type" dataField="leavetype.name" />		
		<primo:datecolumn headerText="Leave Date From" dataField="leavedatefrom" dataFormat="MM-dd-yyyy"/>
		<primo:datecolumn headerText="Leave Date To" dataField="leavedateto" dataFormat="MM-dd-yyyy"/>
		
		<primo:textcolumn headerText="Days Req" dataField="daysrequested" />
		<primo:textcolumn headerText="Days Paid" dataField="dayspaid" />
		<primo:textcolumn headerText="Days Unpaid" dataField="daysunpaid" />
		<primo:textcolumn headerText="Days Paidout" dataField="paidoutdays" />
		
		<primo:textcolumn headerText="Hrs Req" dataField="hoursrequested" />
		<primo:textcolumn headerText="Hrs Paid" dataField="hourspaid" />
		<primo:textcolumn headerText="Hrs Unpaid" dataField="hoursunpaid" />
		<primo:textcolumn headerText="Hrs Paidout" dataField="paidouthours" />
		
		<primo:textcolumn headerText="PTOD Rate" dataField="ptodrates" />
				
		<primo:datecolumn headerText="Batch&nbsp;Date" dataField="batchdate" dataFormat="MM-dd-yyyy"/>		
		<primo:textcolumn headerText="Check&nbsp;Date" dataField="checkdate" dataFormat="MM-dd-yyyy" />
		<primo:staticdatacolumn headerText="Status" dataField="approvestatus" dataType="APPROVE_STATUS" />
		<primo:textcolumn headerText="Approved By" dataField="approveby.fullName" />
		<primo:textcolumn headerText="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Note&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" dataField="notes" />
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>


