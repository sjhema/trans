<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

	var submitting = false;
	
	function processSubmit() {
	  if(submitting) {
		    alert('Please wait a moment...');
		    this.disabled = true;
		    return false;
	   } else {
			submitting = true;
		    return true;
	   }	
	}

</script>
<script language="javascript">
	function searchReport() {
		submitting = false;
		
		var subcon= document.getElementById('subcontractorID').value;
		
		
		var company= document.getElementById('companyId').value;
		
		
		var d1 = document.getElementById('fromDate').value;
		if (d1!=null && d1!='' && !isValidDate(d1)) {
			alert("Invalid billBatch date");
		}
		var d6 = document.getElementById('toDate').value;
		if (d6!=null && d6!='' && !isValidDate(d6)) {
			alert("Invalid billBatch date");
		}
		var d2 = document.getElementById('fromloadDate').value;
		if (d2!=null && d2!='' && !isValidDate(d2)) {
			alert("Invalid from loaddate");
		}
		var d3 = document.getElementById('toloadDate').value;
		if (d3!=null && d3!='' && !isValidDate(d3)) {
			alert("Invalid to loaddate");
		}
		var d4 = document.getElementById('fromunloadDate').value;
		if (d4!=null && d4!='' && !isValidDate(d4)) {
			alert("Invalid from unloadDate");
		}
		var d5 = document.getElementById('tounloadDate').value;
		if (d5!=null && d5!='' && !isValidDate(d5)) {
			alert("Invalid to unloadDate");
		}
		var d7 = document.getElementById('invoiceDate').value;
		if (d7!=null && d7!='' && !isValidDate(d7)) {
			alert("Invalid invoice date");
		}		
		 
		var allNotes='';
		var allCharges='';
	   var noteSpans=document.getElementsByName("noteSpan");
	   var chargeSpans=document.getElementsByName("chargeSpan");
	   
	   for ( var i = 0; i < noteSpans.length; i++) {		
		   if(allNotes==''){
			   allNotes=noteSpans[i].innerHTML;  
		   }
		   else{
			   allNotes=allNotes+','+noteSpans[i].innerHTML+'';
		   }
	   }	   
	   
	   
	   
	   for ( var i = 0; i < chargeSpans.length; i++) {		
		   if(allCharges==''){
			   allCharges=chargeSpans[i].innerHTML;  
		   }
		   else{
			   allCharges=allCharges+','+chargeSpans[i].innerHTML+'';
		   }
	   }	   
	   
	   
	   document .getElementById("miscelleneousNote").value=allNotes;
	   document .getElementById("miscelleneousCharges").value=allCharges;
	   
	   if (company!=null && company!='' && subcon!=null && subcon!=''){
			document.forms[0].target="reportData";
			document.forms[0].submit();
	   }
	   else{
		   alert("Please Select Subcontractor Name and Company");
	   }
	}
	
	
	function removeMultipleNote(){
		var notes=document.getElementById("miscelleneousNote").value="";
		var charge=document.getElementById("miscelleneousCharges").value="";		
	}
	
	function addMultipleNote(){				
		var notes=document.getElementById("miscelleneousNote").value;
		var charge=document.getElementById("miscelleneousCharges").value;
		
		if(notes!='' && charge!=''){
		
		var newSpan = document.createElement('span');
		newSpan.setAttribute('name', 'noteSpan');		
		document.getElementById('noteTdId').appendChild(newSpan);
		newSpan.style.color='red';
		newSpan.innerHTML = notes;
		 var newbr = document.createElement('br');
		 document.getElementById('noteTdId').appendChild(newbr); 
		 
		var newSpan2 = document.createElement('span');
		newSpan2.setAttribute('name', 'chargeSpan');		
		document.getElementById('chargeTdId').appendChild(newSpan2); 
		newSpan2.style.color='red';
		newSpan2.innerHTML = charge;
		var newbr2 = document.createElement('br');
		 document.getElementById('chargeTdId').appendChild(newbr2); 	
		 
		 
		 document.getElementById("miscelleneousNote").value="";
		 document.getElementById("miscelleneousCharges").value="";
		 
		}
		else{
			alert("Please Enter Both Misc.Note and Misc.Charge values!");
		}
		 
	}
	
	
	
	function getNote(){
		document.getElementById("notes").value="";
		var selectedtransfer= document.getElementById('origin');
		var origin=selectedtransfer.options[selectedtransfer.selectedIndex].value;
		var selectedlandfill= document.getElementById('destination');
		var destination=selectedlandfill.options[selectedlandfill.selectedIndex].value;
		if(origin!="" && destination==""){
			getDestinationLoad();
		}
		if(destination!="" && origin=="" ){
			getOriginLoad();
		}
		jQuery.ajax({
			url:'${ctx}/reportuser/report/subcontractorbilling/ajax.do?action=findNote&origin='+origin+'&destination='+destination, 
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				document.getElementById("notes").innerHTML=listData;
			}
		});	
	}

	function getDestinationLoad(){
		var selectedtransfer= document.getElementById('origin');
		var origin=selectedtransfer.options[selectedtransfer.selectedIndex].value;
		jQuery.ajax({
			url:'${ctx}/reportuser/report/subcontractorbilling/ajax.do?action=findDestinationLoad&origin='+origin, 
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var options = '<option value="">Please Select</option>';
				for (var i = 0; i <listData.length; i++) {
					var destination=listData[i];
					options += '<option value="'+destination.id+'">'+destination.name+'</option>';
				  }
				$("#destination").html(options);
			}
		});	
	}
	function getOriginLoad(){
		var selectedlandfill= document.getElementById('destination');
		var destination=selectedlandfill.options[selectedlandfill.selectedIndex].value;
		jQuery.ajax({
			url:'${ctx}/reportuser/report/subcontractorbilling/ajax.do?action=findDOriginLoad&destination='+destination, 
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var options = '<option value="">Please Select</option>';
				for (var i = 0; i <listData.length; i++) {
					var origin=listData[i];
					options += '<option value="'+origin.id+'">'+origin.name+'</option>';
				  }
				$("#origin").html(options);
			}
		});	
	}
</script>
<br/>
<form:form action="search.do" method="post"
	name="searchForm">
	<table id="form-table" width="100%" cellspacing="0" cellpadding="5">
		<tr class="table-heading">
			<td align="${left}" colspan="10"><b>Subcontractor Voucher</b></td>
			<td ></td>
						
		</tr>
		<tr>
		<td class="form-left">Subcontractor Name<span 
				class="errorMessage">*</span></td>
			<td align="${left}">
				<select name="subcontractorName" id="subcontractorID" style="width:130px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${subContractor}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
	<td class="form-left">Company<span
				class="errorMessage">*</span></td>
			<td align="${left}">
				<select name="company" id="companyId" style="width:130px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${companyLocation}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		
		<tr>
			<td class="form-left">Origin</td>
			<td align="${left}">
				<select name="origin" id="origin"  onchange="javascript:getNote()" style="width:130px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${origins}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}"  class="first">Destination</td>
			<td align="${left}">
				<select name="destination" id="destination" onchange="javascript:getNote()"  style="width:130px">
					<option value="">--Please Select--</option>
					<c:forEach var="item" items="${destinations}">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</td>
			<td align="${left}"  class="first">Subcontractor Voucher No.</td>
			<td align="${left}">
				<input name="invoiceNumber" id="invoiceNumber" size="15" value=""/>
			</td>
			<td class="first"><label>Voucher Date</label></td><td><input name="invoiceDate" type="text" id="invoiceDate" size="15"
				value="${sessionScope.searchCriteria.searchMap.invoiceDate}" onblur="javascript:formatReportDate('invoiceDate');"/>
				<script type="text/javascript">
			$(function() {
			$("#invoiceDate").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>
		</tr>
		<tr>
			<td align="${left}" class="first"><label>From Batch Date</label></td>
			<td align="${left}"><input name="fromDate" type="text" id="fromDate" size="15"
				value="${sessionScope.searchCriteria.searchMap.fromDate}" onblur="javascript:formatReportDate('fromDate');"/>
				<script type="text/javascript">
			$(function() {
			$("#fromDate").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>
		   <td align="${left}" class="first"><label>To Batch Date</label></td>
			<td align="${left}"><input name="toDate" type="text" id="toDate" size="15"
				value="${sessionScope.searchCriteria.searchMap.toDate}" onblur="javascript:formatReportDate('toDate');"/>
				<script type="text/javascript">
			$(function() {
			$("#toDate").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>
		    <td align="${left}" class="first"><label>From Load Date</label></td>
			<td align="${left}"><input name="fromloadDate" type="text" id="fromloadDate"  size="15"
				value="${sessionScope.searchCriteria.searchMap.fromloadDate}" onblur="javascript:formatReportDate('fromloadDate');"/>
				<script type="text/javascript">
			$(function() {
			$("#fromloadDate").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>
		     <td align="${left}" class="first"><label>To Load Date</label></td>
			<td align="${left}"><input name="toloadDate" type="text" id="toloadDate"  size="15"
				value="${sessionScope.searchCriteria.searchMap.toloadDate}" onblur="javascript:formatReportDate('toloadDate');"/>
				<script type="text/javascript">
			$(function() {
			$("#toloadDate").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>
		 </tr>
		 <tr>
		    <td align="${left}" class="first"><label>From UnLoad Date</label></td>
			<td align="${left}"><input name="fromunloadDate" type="text" id="fromunloadDate"  size="15"
				value="${sessionScope.searchCriteria.searchMap.fromunloadDate}" onblur="javascript:formatReportDate('fromunloadDate');"/>
				<script type="text/javascript">
			$(function() {
			$("#fromunloadDate").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>
		     <td align="${left}" class="first"><label>To UnLoad Date</label></td>
			<td align="${left}"><input name="tounloadDate" type="text" id="tounloadDate"  size="15"
				value="${sessionScope.searchCriteria.searchMap.tounloadDate}" onblur="javascript:formatReportDate('tounloadDate');"/>
				<script type="text/javascript">
			$(function() {
			$("#tounloadDate").datepicker({
				dateFormat:'mm-dd-yy',
            	changeMonth: true,
    			changeYear: true
    		});
			});
		    </script></td>
		    <td align="${left}"  class="first">Miscellaneous Note</td>
			<td align="${left}">
				<input name="miscelleneousNote" id="miscelleneousNote" size="15" value=""/>
			</td>
		    <td align="${left}"  class="first">Miscellaneous Charges</td>
			<td align="${left}">
				<input name="miscelleneousCharges" id="miscelleneousCharges" size="15" value=""/>
			</td>
			<td>
			<a href="javascript:addMultipleNote()"><img src="${ctx}/images/addnotes.png" border="0" title="Add New Note"  width="20px" height="20px"/></a></td>
			<td><a href="javascript:removeMultipleNote()"><img src="${ctx}/images/removenote.png" border="0" title="Clear Field Values" width="20px" height="20px"/></a>
			</td>
		    
		</tr>
		<tr>
			<td></td>
			<td align="left" colspan="3"><input type="button"
				onclick="javascript:searchReport()" value="Preview" /></td>
				
				<td id="noteTdId" align="left" colspan="2">
				
				</td>
				
				<td id="chargeTdId" align="center" valign="middle" colspan="2">
				
				</td>
		</tr>
		<tr><td colspan="4" align="center"><strong><span id="notes" style="color:red"></span></strong></td></tr>
	</table>
</form:form>
<table width="100%">
	<tr>
		<td align="${left}" width="100%" align="right">
			<a href="${ctx}/reportuser/report/subcontractorbilling/export.do?type=pdf" target="reportData"><img src="${ctx}/images/pdf.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/reportuser/report/subcontractorbilling/export.do?type=xls" target="reportData"><img src="${ctx}/images/excel.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/reportuser/report/subcontractorbilling/export.do?type=csv" target="reportData"><img src="${ctx}/images/csv.png" border="0" class="toolbarButton"/></a>
			<a href="${ctx}/reportuser/report/subcontractorbilling/save.do" target="reportData"  name="myButton" 
			onclick="return processSubmit();"><img src="${ctx}/images/save.png" border="0" class="toolbarButton"/></a>
<a href="${ctx}/reportuser/report/subcontractorbilling/export.do?type=print" target="reportData"><img src="${ctx}/images/print.png" border="0" class="toolbarButton"/></a>
		</td>
	</tr>
	<tr><td align="${left}" width="100%" valign="top"><iframe src="${ctx}/trans/blank.jsp" width="100%" height="600" name="reportData" frameborder="0"></iframe></td></tr>
</table>
