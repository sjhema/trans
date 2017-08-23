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
function formatDate1(){
	var date=document.getElementById("datepicker1").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker1").value="";
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
						document.getElementById("datepicker1").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker1").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker1").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker1").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker1").value=date;
		}
	 }
   }
}
</script>
<h3>
	<primo:label code="Manage Vehicle" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Vehicle" /></b>
			</td>
		</tr>
		<tr>
		<c:out value="${sessionScope['unitval']}"/>
		<c:set var="unitVal" value="${sessionScope['unitval']}"/>
			<td align="${left}" class="first"><primo:label code="Unit#"/></td>
			<td align="${left}"><select id="unitNo" name="unit.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${unitNo}" var="unitNo">
				<c:set var="selected" value=""/>	
				<c:set var="unitVal" value="${unitNo.unit}-${unitNo.type}"/>			
				<c:if test="${sessionScope['unitval'] ne null}">
				<c:if test="${sessionScope['unitval'] == unitVal}">
					<c:set var="selected" value="selected"/>
				</c:if>
				</c:if>
					<option value="${unitNo.unit}-${unitNo.type}" ${selected}>${unitNo.unit}</option>
					
				</c:forEach>
			</select></td>

			<td align="${left}" class="first"><primo:label code="Vin NO"/></td>
			<td align="${left}"><select id="vinNumber" name="vinNumber.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${vinNo}" var="vinNumber">
				<c:set var="selected" value=""/>
				<c:if test="${sessionScope.searchCriteria.searchMap['vinNumber.id'] ==vinNumber.id}">
					<c:set var="selected" value="selected"/>
				</c:if>
					<option value="${vinNumber.id}" ${selected}>${vinNumber.vinNumber}</option>
				</c:forEach>
			</select></td>
			
			<%-- <td align="${left}" class="first"><primo:label code="Unit" /></td>
			<td align="${left}"><input name="unit" type="text"
				value="${sessionScope.searchCriteria.searchMap.unit}" />
			</td> --%>
			
			<%-- <td align="${left}" class="first"><primo:label code="Vin No." /></td>
			<td align="${left}"><input name="vinNumber" type="text"
				value="${sessionScope.searchCriteria.searchMap.vinNumber}" />
			</td> --%>

		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Company"/></td>
			<td align="${left}"><select id="owner" name="owner.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${companies}" var="owner">
				<c:set var="selected" value=""/>
				<c:if test="${sessionScope.searchCriteria.searchMap['owner.id'] == owner.id}">
					<c:set var="selected" value="selected"/>
				</c:if>
					<option value="${owner.id}" ${selected}>${owner.name}</option>
				</c:forEach>
			</select></td>
				<td align="${left}" class="first"><primo:label code="Plate#"/></td>
			<td align="${left}"><select id="plateNum" name="plate.id" style="min-width:154px; max-width:154px">
				<option value="">------<primo:label code="Please Select"/>------</option>
				<c:forEach items="${plateNumber}" var="plateNum">
				<c:set var="selected" value=""/>
				<c:if test="${sessionScope.searchCriteria.searchMap['plate.id'] == plateNum.id}">
					<c:set var="selected" value="selected"/>
				</c:if>
					<option value="${plateNum.id}" ${selected}>${plateNum.plate}</option>
				</c:forEach>
			</select></td>
		</tr>
		
		<tr>
			<td align="${left}" class="first"><primo:label code="Valid From" />
			</td>
			<td align="${left}"><input id="datepicker" name="validFromDate" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate();"
				value="${sessionScope.searchCriteria.searchMap.validFromDate}"/>
			</td>
			<td align="${left}" class="first"><primo:label
					code="Valid To" /></td>
			<td align="${left}"><input id="datepicker1" name="validToDate" style="min-width:150px; max-width:150px"
				type="text" onblur="return formatDate1();"
				value="${sessionScope.searchCriteria.searchMap.validToDate}"/>
			</td>
		</tr>
		
		
		
		<tr>
			<td align="${left}" class="first"><primo:label code="Type" /></td>
			<td align="${left}"><select id="type" name="type" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${vehicleTypes}" var="vehicletype">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['type'] == vehicletype.dataValue}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${vehicletype.dataValue}"${selected}>${vehicletype.dataText}</option>
					</c:forEach>
			</select></td>
			<td align="${left}" class="first"><primo:label code="Feature" /></td>
			<td align="${left}">
				<select id="feature" name="feature" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select" />------</option>
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
					<option value="">------<primo:label code="Please Select" />------</option>
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
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();" value="Search" />
			</td>
		</tr>
	</table>
</form:form>
<br />
<div style="width: 100%; margin: 0px auto;">
	<form:form name="delete.do" id="customerForm">
		<primo:datatable urlContext="admin/vehicle" deletable="true"
			editable="true" insertable="true" exportPdf="true" exportXls="true"
			exportCsv="true" baseObjects="${list}"
			searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
			pagingLink="search.do" multipleDelete="false" searcheable="false">			
			<primo:textcolumn headerText="Unit#" dataField="unit" />
			<primo:textcolumn headerText="Company" dataField="owner.name" />
			<%-- <primo:textcolumn headerText="Location" dataField="location.name" /> --%>
			<primo:textcolumn headerText="Year" dataField="year" />
			<primo:textcolumn headerText="Make" dataField="make" />
			<primo:staticdatacolumn headerText="Model" dataField="model" dataType="MODEL_TYPE" />
			<primo:textcolumn headerText="Vin No." dataField="vinNumber" />
			<primo:textcolumn headerText="Plate" dataField="plate" />
			<primo:staticdatacolumn headerText="Type" dataField="type" dataType="VEHICLE_TYPE" />
			<primo:textcolumn headerText="Valid From" dataField="validFrom" dataFormat="MM-dd-yyyy"/>
        <primo:textcolumn headerText="Valid To" dataField="validTo" dataFormat="MM-dd-yyyy"/>
        	<primo:staticdatacolumn headerText="Feature" dataField="feature" dataType="VEHICLE_FEATURE" />
        	<primo:textcolumn headerText="Inactive Date" dataField="inactiveDate" dataFormat="MM-dd-yyyy"/>
        	<primo:staticdatacolumn headerText="Status" dataField="activeStatus" dataType="VEHICLE_STATUS" />
		</primo:datatable>
	</form:form>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</div>