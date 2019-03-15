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

function editMultipleData() {
	var fuelVendor = document.getElementById('fuelvendor').value;
	if (fuelVendor == "") {
		alert("Please search by fuel vendor");
		return;
	}
	
	var inputs = document.getElementsByName("id");
	var submitForm = false;
	for (var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			submitForm = true;
		}
	}
	
	if (!submitForm) {
		alert("Please select at least one record");
		return;
	}
	
	if (!confirm("Do you want to bulk edit selected records?")) {
		return;
	}
	
	document.deleteForm.action = "bulkedit.do";
	document.deleteForm.submit();
}
</script>
<h3>
	<primo:label code="Manage Fuel Cards" />
</h3>
<form:form action="search.do" method="get" name="searchForm">
	<table width="100%" id="form-table">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Search Fuel Cards" /></b></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><primo:label code="Fule Vendor" /></td>
			<td align="${left}"><select id="fuelvendor" name="fuelvendor" style="min-width:154px; max-width:154px">
					<option value="">------
					<primo:label code="Please Select" />
					------
					</option>
					<c:forEach items="${fuelvendor}" var="fuelvendor">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['fuelvendor'] ==fuelvendor.id}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${fuelvendor.id}"${selected}>${fuelvendor.name}</option>
					</c:forEach>
			</select></td>
			
			
			<%-- <td align="${left}" class="first"><primo:label code="Name" /></td>
			<td align="${left}"><input name="name" type="text"
				value="${sessionScope.searchCriteria.searchMap.name}" /></td> --%>

			<td align="${left}" class="first"><primo:label code="Fuel Card Number" /></td>
			<td align="${left}"><select id="fuelcardnum" name="fuelcardNum" style="min-width:154px; max-width:154px">
					<option value="">
						------
						<primo:label code="Please Select" />
						------
					</option>
					<c:forEach items="${fuelcard}" var="fuelcard">
						<c:set var="selected" value="" />
						<c:if
							test="${sessionScope.searchCriteria.searchMap['fuelcardNum'] == fuelcard.fuelcardNum}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${fuelcard.fuelcardNum}"${selected}>${fuelcard.fuelcardNum}</option>
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
			<td align="${left}"></td>
			<td align="${left}"><input type="button"
				onclick="document.forms['searchForm'].submit();"
				value="<primo:label code="Search"/>" /></td>
		</tr>
	</table>
</form:form>

<table width="100%">
	<tr>
		<td align="left" colspan="3">		
		<img src="/trans/images/edit.png" border="0" title="BULK EDIT" class="toolbarButton"> 
			<a href="javascript:;" onclick="editMultipleData()">BULK EDIT</a>
		</td>
	
	</tr>
</table>

<br />
<form:form name="deleteForm" id="deleteForm">
	<primo:datatable urlContext="admin/fuelcard" deletable="true"
		editable="true" exportPdf="true" exportXls="true"
		exportCsv="true" insertable="true" baseObjects="${list}"
		searchCriteria="${sessionScope['searchCriteria']}" cellPadding="2"
		pagingLink="search.do" multipleDelete="true" searcheable="false">
		<primo:textcolumn width="100px" headerText="Fuel Vendor" dataField="fuelvendor.name" />
		<primo:textcolumn width="100px" headerText="Fuel Card Number" dataField="fuelcardNum" />
		<primo:textcolumn headerText="Valid From" dataField="validFrom" dataFormat="MM-dd-yyyy"/>
		<primo:textcolumn headerText="Valid To" dataField="validTo" dataFormat="MM-dd-yyyy"/>
		<primo:staticdatacolumn headerText="Status" dataField="status" dataType="STATUS" />
	</primo:datatable>
	<%session.setAttribute("columnPropertyList", pageContext.getAttribute("columnPropertyList"));%>
</form:form>
