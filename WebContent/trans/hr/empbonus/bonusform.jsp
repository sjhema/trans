<%@ include file="/common/taglibs.jsp"%>
<jsp:include page="/trans/hr/report/employeebonus/html1.jsp" />
<script language="javascript">
$(document).ready(function(){
	   $("select").multiselect();
});



function getTerminal()
{
	//var selecteddriver=document.getElementById("driverId").value;
	var selectedcompany = document.getElementById('companyID');
	var companyid = selectedcompany.options[selectedcompany.selectedIndex].value;
	var selectedterminal= document.getElementById('terminalID');
	var terminalId=selectedterminal.options[selectedterminal.selectedIndex].value;
	
	if(companyid!='')
	{
	jQuery.ajax({
		
		url:'${ctx}/admin/driverinspection/ajax.do?action=findTerminal&company='+companyid, 
		
			success: function( data ) 
			{
				var listData=jQuery.parseJSON(data);
				
				var options = '<option value="">------Please Select------</option>';
				for (var i = 0; i <listData.length; i++) {
					var dlst=listData[i];
					if(terminalId==dlst.id)
					options += '<option value="'+dlst.id+'" selected="selected">'+dlst.name+'</option>';
					else
						options += '<option value="'+dlst.id+'">'+dlst.name+'</option>';
					
				}
				$("#terminalID").html(options);
		}
	});	
	}
	else
	{
		jQuery.ajax({
			url:'${ctx}/admin/driverinspection/ajax.do?action=findAllTerminal', 
			
				success: function( data ) {
					var listData=jQuery.parseJSON(data);
					var options = '<option value="">------Please Select------</option>';
					for (var i = 0; i <listData.length; i++) {
						var dlst=listData[i];
						options += '<option value="'+dlst.id+'">'+dlst.name+'</option>';
					  }
					$("#terminalID").html(options);
			}
		});	
	}
}




function getDriver()
{
	//var selecteddriver=document.getElementById("driverId").value;
	
	var selectedcompany = document.getElementById('companyID');
	var companyid = selectedcompany.options[selectedcompany.selectedIndex].value;
	var selecteddriver= document.getElementById('driverID');
	var driverId=selecteddriver.options[selecteddriver.selectedIndex].value;	
	var selectedterminal= document.getElementById('terminalID');
	var terminalId=selectedterminal.options[selectedterminal.selectedIndex].value;
	
	
	if(terminalId!='')
	{
	jQuery.ajax({
		
		url:'${ctx}/admin/driverinspection/ajax.do?action=findDriver&company='+companyid, 
		
			success: function( data ) 
			{
				var listData=jQuery.parseJSON(data);				
				var options = '<option value="">------Please Select------</option>';
				for (var i = 0; i <listData.length; i++) {
					var dlst=listData[i];
					if(driverId==dlst.id)
					options += '<option value="'+dlst.id+'" selected="selected">'+dlst.fullName+'</option>';
					else
						options += '<option value="'+dlst.id+'">'+dlst.fullName+'</option>';
					
				}
				$("#driverID").html(options);
		}
	});	
	}
	else
	{
		jQuery.ajax({
			url:'${ctx}/admin/driverinspection/ajax.do?action=findAllDriver', 
			
				success: function( data ) {
					var listData=jQuery.parseJSON(data);
					var options = '<option value="">------Please Select------</option>';
					for (var i = 0; i <listData.length; i++) {
						var dlst=listData[i];
						options += '<option value="'+dlst.id+'">'+dlst.fullName+'</option>';
					  }
					$("#driverID").html(options);
			}
		});	
	}
}



function submitForm(){	
	var str = 

	$.ajax({
	    type:"POST",	    
	    url:'${ctx}/hr/empbonus/saveBonus.do',
	    data:$('#employeeForm').serialize(),	    
	    success: function(data){
	       alert(data);
	    }
	});
	
}


function getamount(){
	 var bo=document.getElementById("bo");
    var b = new Array();
    for (var i = 0; i < bo.options.length; i++) {
     if(bo.options[i].selected ==true){
    
          b[i]=bo.options[i].value;
         
        }
        }
  var b1= b.toString();
  
  var table="";
	jQuery.ajax({
		url:'${ctx}/hr/empbonus/ajax.do?action=findamount&bid='+b1,
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			/* var table=""; */
			table+='<tr><th>Bonus Type</th><th>Amount</th><th >Note</th></tr>'
			//var amount=listData[0];
			for (var i = 0; i <listData.length; i++) {
				var list=listData[i];
				table+='<tr><td><input name="bonustype" type="hidden" value="'+list.id+'"/>'+list.typename+'</td><td><input name="bonusAmount" type="text" value="'+list.amount+'"/></td><td ><input type="text" name="note" size="50px"/></td></tr>'
			}
			
		  //  document.getElementById("bonusamount"+num).value=amount;
			$("#divta").html(table);
			
		}
	}); 
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
</script>
<%-- <c:if test="${bon ne true}">
<input type="button" id="cancelBtn"
				value="<primo:label code="Previous"/>" class="flat"
				onClick="location.href='previous.do'" />
				<input type="button" id="cancelBtn"
				value="<primo:label code="Next"/>" class="flat"
				onClick="location.href='bonusnext.do'" style="width: 85px"/>
				</c:if> --%>
<h3>
	<primo:label code="Add Employee Bonus" />
</h3>
<form:form action="saveBonus.do" name="employeeForm" commandName="modelObject"
	method="post">
		<form:hidden path="id" id="id" />
		<table> 
		<tr><td><font color="#67636B">Employee:</font> <font color="black" size="2">${modelObject.driver.fullName}</font></td>
		<td><font color="#67636B" >Category:</font> <font color="black" size="2">${modelObject.category.name}</font></td>
		<td><font color="#67636B" >Company:</font> <font color="black" size="2">${modelObject.company.name}</font></td>
		<td><font color="#67636B" >Terminal:</font> <font color="black" size="2">${modelObject.terminal.name}</font></td>
		</tr><tr><td><font color="#67636B" >Date Hired:</font> <font color="black" size="2"><fmt:formatDate pattern="MM-dd-yyyy" value="${modelObject.driver.dateHired}" /></font>
		</td><td><font color="#67636B" >Experience(yr.month):</font> <font color="black" size="2">${modelObject.experience}</font></td>
		<td><font color="#67636B" >Batch Date:</font> <font color="black" size="2"><fmt:formatDate pattern="MM-dd-yyyy" value="${modelObject.batchFrom}" /></font>
		</td></tr>
		<tr style="visibility: hidden">
	<%-- 	<td class="form-left"><primo:label code="Employee" /><span class="errorMessage">*</span></td> --%>
		<td><form:select cssClass="flat" path="driver" id="empId" onchange="javascript:getComTermCat()">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="fullName" />
				</form:select> <br> <form:errors path="driver" cssClass="errorMessage" />
			</td>
		
		
		<%-- <td class="form-left"><primo:label code="Category" /><span
				class="errorMessage">*</span></td> --%>
		<td><form:select cssClass="flat" path="category" id="categoryid" >
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${categories}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="category" cssClass="errorMessage" />
			</td>
		<!-- </tr>
		<tr> -->
		<%-- <td class="form-left"><primo:label code="Company" /><span
				class="errorMessage">*</span></td> --%>
		<td><form:select cssClass="flat" path="company" id="companyid" >
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td>
		
		<%-- <td class="form-left"><primo:label code="Terminal" /><span
				class="errorMessage">*</span></td> --%>
		<td><form:select cssClass="flat" path="terminal" id="terminalid" >
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>
			</tr><tr style="visibility: hidden">
				
			<td align="${left}"><form:input path="dateHired"
					cssClass="flat" readonly="true"   onblur="return formatDate();"/> 
			<br> <form:errors path="dateHired" cssClass="errorMessage" /></td>
			
			
			<td align="${left}"><form:input id="experience" readonly="true"  path="experience" cssClass="flat"/> 
			<br> <form:errors path="experience" cssClass="errorMessage" /></td>
				<td align="${left}"><form:input path="batchFrom"
					cssClass="flat" readonly="true"  onblur="return formatDate();"/> 
			<br> <form:errors path="batchFrom" cssClass="errorMessage" /></td>
			
			
			<td align="${left}"><form:input path="batchTo"
					cssClass="flat" readonly="true"  onblur="return formatDate1();"/> 
			<br> <form:errors path="batchTo" cssClass="errorMessage" /></td>
		</tr>
		</table>
	<table width="100%" id="form-table" cellspacing="1" cellpadding="5" >
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Add/Update Employee Bonus"/></b></td>
		</tr>
		<%-- <tr style="visibility: hidden">
		<td class="form-left"><primo:label code="Employee" /><span class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="employee" id="empId" onchange="javascript:getComTermCat()">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="fullName" />
				</form:select> <br> <form:errors path="employee" cssClass="errorMessage" />
			</td>
		
		
		<td class="form-left"><primo:label code="Category" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="category" id="categoryid" >
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${categories}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="category" cssClass="errorMessage" />
			</td>
		<!-- </tr>
		<tr> -->
		<td class="form-left"><primo:label code="Company" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="company" id="companyid" >
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td>
		
		<td class="form-left"><primo:label code="Terminal" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="terminal" id="terminalid" >
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>
		</tr> --%>
		
		<%-- <tr>
			<td class="form-left"><primo:label code="Hired Date" /><span class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="dateHired"
					cssClass="flat" readonly="true"   onblur="return formatDate();"/> 
			<br> <form:errors path="dateHired" cssClass="errorMessage" /></td>
			
			<td class="form-left"><primo:label code="Experience(yr.month)" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="experience" readonly="true"  path="experience" cssClass="flat"/> 
			<br> <form:errors path="experience" cssClass="errorMessage" /></td>
			
		</tr> --%>
		<%-- 	<tr>
		<td class="form-left"><primo:label code="Batch Date From" /><span class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="batchFrom"
					cssClass="flat" readonly="true"  onblur="return formatDate();"/> 
			<br> <form:errors path="batchFrom" cssClass="errorMessage" /></td>
			
			<td class="form-left"><primo:label code="Batch Date To" /><span class="errorMessage">*</span></td>
			<td align="${left}"><form:input path="batchTo"
					cssClass="flat" readonly="true"  onblur="return formatDate1();"/> 
			<br> <form:errors path="batchTo" cssClass="errorMessage" /></td>
			
		</tr> --%>
		<tr>
		 <c:if test="${bon ne true}">
		<td><select id="bo" multiple>
		<c:forEach var="item" items="${bonustypes}" varStatus="counter">
		<option value="${item.id}">${item.typename}</option>
		</c:forEach>
		</select></td>
		</c:if>
		</tr>
		<tr>
		<c:if test="${bon ne true}">
		<td><input type="button" onclick="javascript:getamount()" value="Add" />
		</td>
		</c:if>
		</tr>
		<tr>
		<!-- <td></td> -->
		
		<%--  <td class="form-left"><primo:label code="Bonus Type" /><span class="errorMessage"></span></td>
		 <!-- <td></td> -->
		 <td class="form-left" ><primo:label code="Amount" /><span
				class="errorMessage"></span></td>
		<td class="form-left" ><primo:label code="Note" /><span
				class="errorMessage"></span></td> --%>
		
	 <%--   <c:if test="${bon ne true}">
	   <c:forEach var="item" items="${bonustypes}" varStatus="counter">
		<tr>
		<!-- <td></td> -->
		<td>
		<select name="bonustype" id="btype${counter.count}" onchange="javascript:getamount(this)">
		<option value="">--please select--</option>
		<option value="${item.id}">${item.typename}</option>
		</select>
		</td>
		<td></td>
		<td>
	    <input name="bonusAmount" id="bonusamount,${counter.count}" type="text"/>
		</td>
		</tr>
		</c:forEach>
	    </c:if> --%>
	   <!-- <div class="datagrid" style="border: none"> -->
	   <td colspan="4">
	   <c:if test="${bon eq true}">
	   <table class="datagrid" width="100%">
	   <tr><th>Bonus Type</th><th>Amount</th><th >Note</th></tr>
		 <c:forEach var="item" items="${bonustypese}">
	<tr>
	<td>${item.bonusType.typename}</td>
		<td>${item.bonusamount}</td>
		<td>${item.note}</td>
		</tr>
		</c:forEach>
		</table>
	   </c:if>
		</td>
		<!-- </div> -->
		<!-- </td> -->
		</tr>
	<tr><td><div id="divta" class="datagrid" style="border: none"></div></td></tr>
		
		<tr><td colspan="2"></td></tr>
		<tr>
			<!-- <td>&nbsp;</td> -->
			<td align="${left}" colspan="1"><input type="submit"
				name="create" id="create" 
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" />
			</td>
			<td align="left"><c:if test="${bon ne true}">
               <input type="button" id="cancelBtn"
				value="<primo:label code="Previous"/>" class="flat"
				onClick="location.href='addEmpBonusPrevious.do'" />
				<%-- <input type="button" id="cancelBtn"
				value="<primo:label code="Next"/>" class="flat"
				onClick="location.href='bonusnext.do'" style="width: 85px"/> --%>
				<c:choose>
				     <c:when test="${listEnd eq true}">
				        <input type="button" id="cancelBtn"
				          value="<primo:label code="Next"/>" class="flat"
				          onClick="location.href='addEmpBonusNext.do'" style="width: 85px" disabled="disabled"/>
				     </c:when>
                     <c:otherwise>
                            <input type="button" id="cancelBtn"
				              value="<primo:label code="Next"/>" class="flat"
				                    onClick="location.href='addEmpBonusNext.do'" style="width: 85px"/>
                     </c:otherwise>
			     </c:choose>
				</c:if></td>
			</tr>
		</table>
	</form:form>

<%-- <a href="${ctx}/hr/empbonus/bonusnext.do">Next</a> --%>
