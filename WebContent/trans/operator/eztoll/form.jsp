<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function getTime(field){
	var timein=document.getElementById(field).value;
	if(timein!=""){
		if(timein.length<4){
			alert("Invalidte time format");
			document.getElementById(field).value="";
			return true;
		}else{
			var str=new String(timein);
			if(!str.match(":")){
				var hour=str.substring(0,2);
				var min=str.substring(2,4);
				if(hour>=24 || min>=60){
					alert("Invalidte time format");
					document.getElementById(field).value="";
					return true;
				}
				var time=hour+":"+min;
				document.getElementById(field).value=time;
			}
		}
	}
}


function getTerminalAndComnpany(driverId){
	if(driverId != ''){
		jQuery.ajax({
			url:'${ctx}/operator/eztoll/ajax.do?action=findterminal&driverId='+driverId, 
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				/* $('#terminal option[value="' + listData.id + '"]').prop('selected', true); */
				var options = '';
				options += '<option value="'+listData.id+'">'+listData.name+'</option>';
				$("#terminal").html(options);
				
			}
		});
	
		jQuery.ajax({
			url:'${ctx}/operator/eztoll/ajax.do?action=findcompany&driverId='+driverId, 
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				/* $('#company option[value="' + listData.id + '"]').prop('selected', true); */
				var options = '';
				options += '<option value="'+listData.id+'">'+listData.name+'</option>';
				$("#company").html(options);
			}
		}); 
	}else{
		
		jQuery.ajax({
			url:'${ctx}/operator/eztoll/ajax.do?action=findallterminal', 
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var options = '<option value="">-----Please Select-----</option>';
				for (var i = 0; i <listData.length; i++) {
					var vehicle=listData[i];						
					options += '<option value="'+vehicle.id+'">'+vehicle.name+'</option>';
				  }
				$("#terminal").html(options);
			}
		});
		
		jQuery.ajax({
			url:'${ctx}/operator/eztoll/ajax.do?action=findallcompany', 
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var options = '<option value="">-----Please Select-----</option>';
				for (var i = 0; i <listData.length; i++) {
					var vehicle=listData[i];						
					options += '<option value="'+vehicle.id+'">'+vehicle.name+'</option>';
				  }
				$("#company").html(options);
			}
		});
	}
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


function findPlateNum(){
	
	if( document.getElementById("tolltagnum")!=""){
		
	/* 	var selectedfuelvendor= document.getElementById('fuelvendor');
		var fuelvendor=selectedfuelvendor.options[selectedfuelvendor.selectedIndex].value; */
		var selectedtolltag=document.getElementById("tolltagnum");
		var tolltag=selectedtolltag.options[selectedtolltag.selectedIndex].value;
		if( tolltag!=""){			
			jQuery.ajax({
				url:'${ctx}/operator/eztoll/ajax.do?action=findplatenum&tolltag='+tolltag, 
				success: function( data ) {
					var listData=jQuery.parseJSON(data);
					var options = '';
					for (var i = 0; i <listData.length; i++) {
						var tolltag=listData[i];						
						options += '<option value="'+tolltag.vehicle.id+'">'+tolltag.vehicle.plate+'</option>';
					  }
					$("#platenum").html(options);
				}
			});
		
				}
		else
		{		
		jQuery.ajax({
			url:'${ctx}/operator/eztoll/ajax.do?action=findall', 
			success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var options = '<option value="">-----Please Select-----</option>';
				for (var i = 0; i <listData.length; i++) {
					var vehicle=listData[i];						
					options += '<option value="'+vehicle.id+'">'+vehicle.plate+'</option>';
				  }
				$("#platenum").html(options);
			}
		});
		}
	}
	
}


</script>

<h3>
	<primo:label code="Add/Update Tolls" />
</h3>

<form:form action="save.do" name="modelForm" modelAttribute="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<form:hidden path="plates" name="plateNumber" value="${plateNum}"/>
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
		<tr class="table-heading">
			<td colspan="5"><b>Tolls</b>
			</td>
		</tr>
		
		
		<tr>
			<td class="form-left"><primo:label code="Driver" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="driver" id="driverId" style="min-width:154px; max-width:154px" onchange="getTerminalAndComnpany(this.value)">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${drivers}" itemValue="id" itemLabel="fullName"></form:options>
				</form:select> <br> <form:errors path="driver" cssClass="errorMessage" />
			</td>
			</tr>
		
		
		<tr>
		   
			<td class="form-left"><primo:label code="Toll Company" /><span class="errorMessage">*</span>
			</td><td><form:select cssClass="flat" path="toolcompany" style="min-width:154px; max-width:154px">
							<form:option value="">-----Please Select----</form:option>
							<form:options items="${tollcompanies}" itemValue="id" itemLabel="name" />
						</form:select> 
					<br><form:errors path="toolcompany" cssClass="errorMessage" />
				</td>
			<td class="form-left"><primo:label code="Company" /><span class="errorMessage">*</span>
			</td><td><form:select cssClass="flat" path="company" style="min-width:154px; max-width:154px">
							<form:option value="">-----Please Select----</form:option>
							<form:options items="${companyLocation}" itemValue="id" itemLabel="name" />
						</form:select> 
					<br><form:errors path="company" cssClass="errorMessage" />
				</td>
			
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Terminals" /><span class="errorMessage">*</span>
			</td><td><form:select cssClass="flat" path="terminal" style="min-width:154px; max-width:154px">
							<form:option value="">-----Please Select----</form:option>
							<form:options items="${terminals}" itemValue="id" itemLabel="name" />
						</form:select> 
					<br><form:errors path="terminal" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Tag#" /><span class="errorMessage">*</span>
			</td><td><form:select cssClass="flat" path="tollTagNumber" style="min-width:154px; max-width:154px" id="tolltagnum" onchange="findPlateNum();">
			<c:if test="${eztoll ne null}">
			        <c:if test="${eztoll.tollTagNumber ne null }">
					<form:option value="${eztoll.tollTagNumber.id}">${tolltag.tollTagNumber} - ${plateNum}</form:option>
					</c:if>
					</c:if>
					
					<c:if test="${tolltags ne null}">			        
					<form:option value="${tolltags.id}">${tolltags.tollTagNumber} - ${tolltags.vehicle.plate}</form:option>					
					</c:if>
					
							<form:option value="">-----Please Select----</form:option>
							<c:forEach var="tolltagnum" items="${tollTagNumbers}">
							<form:option value="${tolltagnum.id}">${tolltagnum.tollTagNumber} - ${tolltagnum.vehicle.plate}</form:option>
							</c:forEach>
						</form:select> 
					<br><form:errors path="tollTagNumber" cssClass="errorMessage" />
				</td>
		</tr>
		
		<tr>
		      <td class="form-left"><primo:label code="Plate#"  /><span class="errorMessage">*</span>
			  </td><td><form:select cssClass="flat" path="plateNumber" id="platenum" style="min-width:154px; max-width:154px">
							<form:option value="">-----Please Select----</form:option>
							<form:options items="${vehicleplates}" itemValue="id" itemLabel="plate" />
						</form:select> 
					<br><form:errors path="plateNumber" cssClass="errorMessage" />
				</td>
		      <td class="form-left"><primo:label code="Unit" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="unit" style="min-width:154px; max-width:154px">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${trucks}" itemValue="id" itemLabel="unit" />
				</form:select> <br> <form:errors path="unit" cssClass="errorMessage" />
			</td>
		      <!--   <td class="form-left">&nbsp;</td>
			   <td>&nbsp;</td> -->
			 
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Invoice Date" /><span
				class="errorMessage">*</span>
			</td>
			<td align="${left}"><form:input path="invoiceDate" style="min-width:150px; max-width:150px"
					cssClass="flat" id="datepicker" onblur="return formatDate();" /> <br>
			<form:errors path="invoiceDate" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
		      
			 
			<td class="form-left"><primo:label code="Transaction Date" /><span class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker1" style="min-width:150px; max-width:150px"
					path="transactiondate" cssClass="flat" onblur="return formatDate1();"/> <br> <form:errors
					path="transactiondate" cssClass="errorMessage" />
		    </td>
		    
		    <td class="form-left"><primo:label code="Transaction Time" /><span class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="transactiontime" path="transactiontime" style="min-width:150px; max-width:150px"
					cssClass="flat"  onkeypress="return onlyNumbers(event,false)" onblur="return getTime('transactiontime')"/> <br> <form:errors path="transactiontime"
					cssClass="errorMessage" />
			 </td>
			 
			 
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Agency " /><span class="errorMessage">*</span></td>			
			<td align="${left}"><form:input path="agency" style="min-width:150px; max-width:150px" /> 
			     <br><form:errors path="agency" cssClass="errorMessage" />
			 </td>
		
		     <td class="form-left"><primo:label code="Amount" /><span class="errorMessage">*</span></td>
			 <td align="${left}"><form:input path="amount"  style="min-width:150px; max-width:150px"/> 
			     <br><form:errors path="amount" cssClass="errorMessage" />
			 </td>
		     
		      
		</tr>
		<%-- <tr>
			<td class="form-left"><primo:label code="Driver" /><span
				class="errorMessage">*</span></td>
			<td><form:select cssClass="flat" path="driver" id="driverId" style="min-width:154px; max-width:154px" onchange="javascript:getTerminal();">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${drivers}" itemValue="id" itemLabel="fullName"></form:options>
				</form:select> <br> <form:errors path="driver" cssClass="errorMessage" />
			</td>
			</tr> --%>
			
			
			
			
		
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick="" value="Save" class="flat" /> <input
				type="reset" id="resetBtn" value="Reset" class="flat" /> <input
				type="button" id="cancelBtn" value="Cancel" class="flat"
				onClick="location.href='list.do'" /></td>
		</tr>
	</table>
</form:form>

<script language="javascript">
getTime('transactiontime');
</script>

