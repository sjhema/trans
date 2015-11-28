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
<h3><primo:label code="Manage Drivers"/></h3>
<div><form:form action="search.do" method="get"
	name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Search Driver"/></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Driver Name"/></td>
				<td align="${left}"><select id="driver" name="id" style="min-width:154px; max-width:154px">
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
			<td align="${left}"><input id="datepicker" name="Hireddate" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate();"
				value="${sessionScope.searchCriteria.searchMap.Hireddate}"/>
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
            <td align="${left}" class="first"><primo:label code="Status" />
                </td>
                <td align="${left}"><select id="status" name="status" style="min-width:154px; max-width:154px">
                        <option value="">------    <primo:label code="Please Select" />-----</option>
                        <c:forEach items="${driverstatus}" var="driverstatus">
                            <c:set var="selected" value="" />
                            <c:if test="${sessionScope.searchCriteria.searchMap['status'] == driverstatus.dataValue}">
                                <c:set var="selected" value="selected" />
                            </c:if>
                            <option value="${driverstatus.dataValue}"${selected}>${driverstatus.dataText}</option>
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
		<primo:datatable urlContext="admin/driver" deletable="true" editable="true" insertable="true" exportPdf="true" exportXls="true" exportCsv="true"
			baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false" >
			<primo:textcolumn headerText="Last Name" dataField="lastName" />
			<primo:textcolumn headerText="First Name" dataField="firstName" />
			<primo:datecolumn headerText="Date Hired" dataField="dateHired" dataFormat="MM-dd-yyyy"/>
			<primo:textcolumn headerText="Company" dataField="company.name"/>
			<primo:textcolumn headerText="Terminal" dataField="terminal.name"/>	
			<primo:datecolumn headerText="Date Terminated" dataField="dateTerminated" dataFormat="MM-dd-yyyy"/>	
			<primo:staticdatacolumn headerText="Status" dataField="status" dataType="STATUS" />
		</primo:datatable>
		<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList")); %>
	</form:form>
</div>