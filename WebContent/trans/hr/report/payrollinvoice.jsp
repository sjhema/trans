<%@ include file="/common/taglibs.jsp"%>

<script language="javascript">
window.onload = function WindowLoad(event) {
	element=document.getElementById('saved');
	element.style.display = 'none';
}
</script>
<script type="text/javascript">

function searchReport2() {
	
	var company= document.getElementById('company').value;
	//var company=selecteddriver.options[selectedcompany.selectedIndex].value;
	
	
	
	var d1 = document.getElementById('fromDate').value;	
	if (d1!=null && d1!='' && !isValidDate(d1)) {
		alert("Invalid billBatch date");
	}
	var d6 = document.getElementById('toDate').value;
	if (d6!=null && d6!='' && !isValidDate(d6)) {
		alert("Invalid billBatch date");
	}
	
	var d8 = document.getElementById('payrollinvoicedate').value;
	if (d8!=null && d8!='' && !isValidDate(d8)) {
		alert("Invalid Check date");
	}
	
	var e;
	
	 if(company==""){
		alert("Please select Company");
		e=yes;
	} 
	if(d1==""&&e!="yes"){
		alert("Please select From Batch Date");
		e=yes;
	}
	if(d6==""&&e!="yes"){
		alert("Please select To Batch Date");
		e=yes;
	}
	
	if(d8==""&&e!="yes"){
		alert("Please select Check Date");
		e=yes;
	}
	
	
	if(d1!=""&& d6!="" && driver!="" && d8!="" && company!=""){
	document.forms[0].target="reportData";
	document.forms[0].submit();
	}
}




function searchRepo() {
	
	var d1 = document.getElementById('fromDate').value;
	if (d1!=null && d1!='' && !isValidDate(d1)) {
		alert("Invalid billBatch date");
	}
	var d6 = document.getElementById('toDate').value;
	if (d6!=null && d6!='' && !isValidDate(d6)) {
		alert("Invalid billBatch date");
	}
	var e;
	
	if(d1==""&&e!="yes"){
		alert("please select from batch date");
		e=yes;
	}
	if(d6==""&&e!="yes"){
		alert("please select to batch date");
		e=yes;
	}
	if(d1!=""&&d6!=""){
		document.forms[0].target="reportData";
		document.forms[0].submit();
	}
}
</script>

<form:form action="search.do" method="post" name="searchForm">
	<br/>
	<br/>
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="8"><b>Hourly Payroll Run</b></td>
		</tr>
		
		
		<tr>
		<td class="form-left">Employee</td>
			<td align="${left}">
				<select name="driver" id="driver" style="min-width:154px; max-width:154px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${employees}">
						<option value="${item.fullName}">${item.fullName}</option>
					</c:forEach>
				</select>
			</td>		 
		     
		     <td class="form-left">Terminal</td>
			<td align="${left}">
				<select name="terminal" id="terminal" style="min-width:154px; max-width:154px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${terminals}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
		     
		  
			
			<td class="form-left">Category</td>
			<td align="${left}">
				<select name="category" id="category" style="min-width:154px; max-width:154px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${categories}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
			
		</tr>
		
		
		
		<tr>			
			<td class="form-left">Company</td>
			<td align="${left}">
				<select name="company" id="company" style="min-width:154px; max-width:154px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${companies}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
			
			
			<td align="${left}" class="first"><label>From Batch Date</label>
			</td>
			<td align="${left}"><input name="fromDate" type="text" style="min-width:150px; max-width:150px" 
				id="fromDate" size="15"
				value="${sessionScope.searchCriteria.searchMap.fromDate}"
				onchange="javascript:getNote()"
				onblur="javascript:formatReportDate('fromDate');" /> <script
					type="text/javascript">
					$(function() {
						$("#fromDate").datepicker({
							dateFormat : 'mm-dd-yy',
							changeMonth : true,
							changeYear : true
						});
					});
				</script>
			</td>
			<td align="${left}" class="first"><label>To Batch Date</label>
			</td>
			<td align="${left}"><input name="toDate" type="text" id="toDate" style="min-width:150px; max-width:150px"
				size="15" value="${sessionScope.searchCriteria.searchMap.toDate}"
				onchange="javascript:getNote()"
				onblur="javascript:formatReportDate('toDate');" /> <script
					type="text/javascript">
					$(function() {
						$("#toDate").datepicker({
							dateFormat : 'mm-dd-yy',
							changeMonth : true,
							changeYear : true
						});
					});
				</script>
			</td>
			
			
			
	</tr>
	
	<tr>
	
	<td class="first"><label>Check Date</label></td><td><input name="payrollinvoicedate" type="text" id="payrollinvoicedate" style="min-width:150px; max-width:150px" size="15"
				value="${sessionScope.searchCriteria.searchMap.payrollinvoicedate}" onblur="javascript:formatReportDate('payrollinvoicedate');"/>
				<script type="text/javascript">
			$(function() {
			$("#payrollinvoicedate").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>
	
	
			
		
		 </tr>
	
	
		
	<tr>
			<td align="${left}"></td>
			<td align="${left}" colspan="3"><input type="button"
				onclick="javascript:searchReport2()" value="Preview" /></td>
	</tr>	
		
	</table>
</form:form>


<table width="100%">
	<tr>
		<td align="${left}" width="100%" align="right">
			<a href="${ctx}/hr/report/payrollinvoice/export.do?type=print" target="reportData"><img src="${ctx}/images/print.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/hr/report/payrollinvoice/export.do?type=pdf" target="reportData"><img src="${ctx}/images/pdf.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/hr/report/payrollinvoice/export.do?type=xls" target="reportData"><img src="${ctx}/images/excel.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/hr/report/payrollinvoice/export.do?type=csv" target="reportData"><img src="${ctx}/images/csv.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/hr/report/payrollinvoice/save.do" target="reportData" 
			onclick = "
				element=document.getElementById('save');
				element.style.display = 'none';
				element1=document.getElementById('saved');
				element1.style.display = '';">
			<img src="${ctx}/images/save.png" border="0" class="toolbarButton" id="save"/></a>
			<img src="${ctx}/images/saved.png" border="0" class="toolbarButton" id="saved"/>
			<%-- <a href="${ctx}/reportuser/report/billing/export.do?type=print" target="reportData"><img src="${ctx}/images/print.png" border="0" class="toolbarButton"/></a> --%>
		</td>
	</tr>
	<tr><td align="${left}" width="100%" valign="top"><iframe src="${ctx}/trans/blank.jsp" width="100%" height="600" name="reportData" frameborder="0"></iframe></td></tr>
</table>