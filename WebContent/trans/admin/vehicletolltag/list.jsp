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
<h3><primo:label code="Vehicle Toll Tag"/></h3>
<div><form:form action="search.do" method="get"
	name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="9"><b><primo:label code="Vehicle Toll Tag"/></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Unit#"/></td>
			<td align="${left}"><select id="vehicle" name="unit" style="min-width:154px; max-width:154px">
					<option value="">------<primo:label code="Please Select"/>------</option>
					<c:forEach items="${vehicleList}" var="vehicle">
					<c:set var="selected" value=""/>
					<c:if test="${sessionScope.searchCriteria.searchMap['unit'] == vehicle.unit}">
						<c:set var="selected" value="selected"/>
					</c:if>
					<option value="${vehicle.unit}" ${selected}>${vehicle.unit}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}" class="first"><primo:label code="Toll Tag No." /></td>
			<td align="${left}"><input name="tollTagNumber" type="text" style="min-width:150px; max-width:150px"
				value="${sessionScope.searchCriteria.searchMap.tollTagNumber}" />
			</td>			
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
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();" value="<primo:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form></div>
<br/>
<form:form name="delete.do" id="serviceForm">
	<primo:datatable urlContext="admin/vehicletolltag" deletable="true"
		editable="true" exportPdf="true" exportXls="true"
		exportCsv="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="false" searcheable="false">
		 <primo:textcolumn headerText="Unit#" dataField="unit"/> 
		<primo:textcolumn headerText="Vin#" dataField="vehicle.vinNumber" />
		<primo:staticdatacolumn headerText="Toll Company" dataField="tollCompany.name" />
		<primo:textcolumn headerText="Toll Tag No." dataField="tollTagNumber" />
		<primo:textcolumn headerText="Valid From" dataField="validFrom" dataFormat="MM-dd-yyyy"/>
        <primo:textcolumn headerText="Valid To" dataField="validTo" dataFormat="MM-dd-yyyy"/>
		<%--<primo:staticdatacolumn headerText="Toll Tag Status" dataField="tollTagStatus" dataType="STATUS" /> --%>
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>



