<%@ include file="/common/taglibs.jsp"%>
<script language="javascript">
function searchReport() {
	/* var sum= */document.getElementById('sum').value='true' ;
	//alert(sum);
	document.forms[0].target="reportData";
	document.forms[0].submit();
}
function searchReport1() {
	 document.getElementById('sum').value='false' ;
	var emp=document.getElementById('employeeId').value;
	
	
	
	document.forms[0].target="reportData";
	document.forms[0].submit(); 
	 
}


function getEmpDetail(){
	var emp=document.getElementById("employeeId");
	var driver=emp.options[emp.selectedIndex].value;
	
	jQuery.ajax({
		url:'${ctx}/hr/timesheetmanage/ajax.do?action=findDCompany&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			
			if(driver==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var company=listData[i];
				options += '<option value="'+company.id+'">'+company.name+'</option>';
				}
			$("#company").html(options);
		}
			
		});
         
	jQuery.ajax({
		url:'${ctx}/hr/timesheetmanage/ajax.do?action=findDTerminal&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			
			if(driver==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var terminal=listData[i];
				options += '<option value="'+terminal.id+'">'+terminal.name+'</option>';
				}
			$("#terminalId").html(options);
		}
			
		});
	
	jQuery.ajax({
		url:'${ctx}/hr/timesheetmanage/ajax.do?action=findDCategory&driver='+driver, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			
			if(driver==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var category=listData[i];
				options += '<option value="'+category.id+'">'+category.name+'</option>';
				}
			$("#categoryId").html(options);
		}
			
		});
	  }



</script>

<form:form action="search.do" name="searchForm" method="post" commandName="modelObject">
	<input type="hidden" name="summary" id="sum" />
	<br/>
	<br/>
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="7"><b><primo:label code="Time sheet Report" />
			</b></td>
	    </tr>
		 

    <tr>
    
       <td class="form-left">Employee</td>
			<td align="${left}">
				<form:select cssClass="flat" id="employeeId" path="driver"  style="min-width:130px; max-width:130px" onchange="javascript:getEmpDetail()">
					<form:option value="">--Please Select--</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="FullName" />
				</form:select> 
			</td>
			
			
			<td class="form-left"><primo:label code="Employee No." /><span class="errorMessage"></span></td>
			<td  align="${left}">
				<form:input size="10" path="employeesNo" cssClass="flat" style="min-width:120px; max-width:120px"/> 
				
			</td>		 
	</tr>	
			<tr>
		       <td class="form-left">Company</td>
			<td align="${left}">
				<select name="company" id="company"   style="min-width:130px; max-width:130px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${companies}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
		     
		     <td class="form-left">Terminal</td>
			<td align="${left}">
				<select name="terminal" id="terminalId"   style="min-width:120px; max-width:120px">
					<option value="">Please Select</option>
					<c:forEach var="item1" items="${terminals}">
						<option value="${item1.id}">${item1.name}</option>
					</c:forEach>
				</select>
			</td>
		
			
			<td class="form-left">Category</td>
			<td align="${left}">
				 <select name="catagory" id="categoryId"   style="min-width:120px; max-width:120px">
					<option value="">Please Select</option>
					<!--   <option value=""></option> -->
					<c:forEach var="item" items="${categories}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
			 
	</tr>	
	
	   
	   <tr>
		 <td class="form-left"><primo:label code="Date From" /><span class="errorMessage"></span></td>
			<td  align="${left}">
				<form:input size="10"  path="weekstartDateFrom" cssClass="flat"  style="min-width:130px; max-width:130px" onblur="return formatDate1('weekstartDateFrom');"/> 
				<script type="text/javascript">
					$(function() {
					$("#weekstartDateFrom").datepicker({
						dateFormat:'mm-dd-yy',
		            	changeMonth: true,
		    			changeYear: true
		    		});
					});
				</script>
				</td>
			<td class="form-left"><primo:label code="Date To" /><span class="errorMessage"></span></td>
			<td  align="${left}">
				<form:input size="10" path="weekendDateTo" cssClass="flat" style="min-width:130px; max-width:130px"  onblur="return formatDate1('weekendDateTo');"/>
				<script type="text/javascript">
					$(function() {
					$("#weekendDateTo").datepicker({
						dateFormat:'mm-dd-yy',
		            	changeMonth: true,
		    			changeYear: true
		    		});
					});
				</script>
			</td>
		
		 </tr>
	   
	   
		<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3"><input type="button"
				onclick="javascript:searchReport()" value="Summary" />
			    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			    <input type="button"
				onclick="javascript:searchReport1()" value="Detail" />
				</td>
		</tr>	
		    
		</table>
</form:form>

<table width="100%">
	<tr>
		<td align="${left}" width="100%" align="right">
			<a href="${ctx}/hr/report/timesheet/export.do?type=pdf" target="reportData"><img src="${ctx}/images/pdf.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/hr/report/timesheet/export.do?type=xls" target="reportData"><img src="${ctx}/images/excel.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/hr/report/timesheet/export.do?type=csv" target="reportData"><img src="${ctx}/images/csv.png" border="0" class="toolbarButton"/></a>
			<%-- <a href="${ctx}/hr/report/timesheet/save.do" target="reportData"><img src="${ctx}/images/save.png" border="0" class="toolbarButton"/></a> --%>
			<%-- <a href="${ctx}/hr/report/timesheet/export.do?type=print" target="reportData"><img src="${ctx}/images/print.png" border="0" class="toolbarButton"/></a> --%>
		</td>
	</tr>
	<tr><td align="${left}" width="100%" valign="top"><iframe src="${ctx}/trans/blank.jsp" width="100%" height="600" name="reportData" frameborder="0"></iframe></td></tr>
</table>
				