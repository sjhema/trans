<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function getComTermCat(){
	var companyselected=document.getElementById("company");
	var staffId=document.getElementById("staffId").value;
	var company=companyselected.options[companyselected.selectedIndex].value;
  /* 	jQuery.ajax({
		url:'${ctx}/hr/employee/ajax.do?action=findDCompany&employee='+employee, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			if(employee==""){
				options = '<option value="">Please Select</option>';
			}
			for (var i = 0; i <listData.length; i++) {
				var company=listData[i];
				options += '<option value="'+company.id+'">'+company.name+'</option>';
				}
			$("#companyid").html(options);
		}
			
		}); */  
         
  	jQuery.ajax({
		url:'${ctx}/hr/employee/ajax.do?action=findDTerminal&company='+company+'&staffId='+staffId, 
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			if(listData[0]!="."){
				/* if(company==""){
					options = '<option value="">Please Select</option>';
				} 
			for (var i = 0; i <listData.length; i++) {
				var terminal=listData[i];
				options += '<option value="'+terminal.id+'">'+terminal.name+'</option>';
				} */
				var terminal=listData[0];
				$("#terminalid").val(terminal.id);
		}
		}
			
		}); 
	
 	jQuery.ajax({
		url:'${ctx}/hr/employee/ajax.do?action=findDCategory&company='+company+'&staffId='+staffId,
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			if(listData[0]!="."){
			/* if(company==""){
				options = '<option value="">Please Select</option>';
			} 
			for (var i = 0; i <listData.length; i++) {
				var category=listData[i];
				options += '<option value="'+category.id+'">'+category.name+'</option>';
			
			} */
			var category=listData[0];
			$("#categoryid").val(category.id);
			}
		}
			
		}); 
 	jQuery.ajax({
		url:'${ctx}/hr/employee/ajax.do?action=findDFLnames&company='+company+'&staffId='+staffId,
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var options;
			if(listData[0]!="."){
		document.getElementById("lastname").value=listData[0];
		document.getElementById("firstname").value=listData[1];
		document.getElementById("datepicker").value=listData[2];
		document.getElementById("datepicker1").value=listData[3];	
			}
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
function formatDate2(){
	var date=document.getElementById("datepicker2").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker2").value="";
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
						document.getElementById("datepicker2").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker2").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker2").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker2").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker2").value=date;
		}
	 }
   }
}
</script>
<script type="text/javascript">
function formatDate3(){
	var date=document.getElementById("datepicker3").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker3").value="";
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
						document.getElementById("datepicker3").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker3").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker3").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker3").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker3").value=date;
		}
	 }
   }
}

function formatDate4(){
	var date=document.getElementById("datepicker4").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker4").value="";
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
						document.getElementById("datepicker4").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker4").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker4").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker4").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker4").value=date;
		}
	 }
   }
}
       function checklastname(){

	  var str=document.getElementById("lastname").value;	 
	  var result = str.replace(/\s\s+/g, ' ');	 
	  document.getElementById("lastname").value=result;
	}

       function checkfirstname(){

    		  var str=document.getElementById("firstname").value;	 
    		  var result = str.replace(/\s\s+/g, ' ');	 
    		  document.getElementById("firstname").value=result;
    		}


       function checkstaffis(){
    	   var str=document.getElementById("OldStaffId").value;
    	  if(str==""){
    		   alert("Please enter Old Id");
    	   }
    	   else{
    		   jQuery.ajax({
    				url:'${ctx}/hr/employee/ajax.do?action=search&OldStaffId='+str, 
    				success: function( data ) {
    					var listData=jQuery.parseJSON(data);
    					document.getElementById("notes").innerHTML=listData;
    				}
    			});
    	   }
       } 
   
    	   
    function getEmpProbationDates(){    	
    	   var datejoined='';
    	   
    	   var probationTypeSelected=document.getElementById("probationTypeIds");
    	   var probationType=probationTypeSelected.options[probationTypeSelected.selectedIndex].value;
    	   
    	   var datehired=document.getElementById("datepicker").value;
    	   var dateRehired=document.getElementById("datepicker1").value;    	  
    	   if(datehired!=''&& dateRehired=='' ){
    		   
    		   datejoined=datehired;
    	   }
    	   else if(datehired==''&& dateRehired!=''){
    		   datejoined=dateRehired;
    	   }
    	   else if(datehired!=''&& dateRehired !=''){
    		   datejoined=dateRehired;
    	   }
    	   
    	   if(probationType!='' && datejoined!=''){
    		   
    		   jQuery.ajax({
   				url:'${ctx}/hr/employee/ajax.do?action=getProbationDates&probationTypeId='+probationType+'&joinedDate='+datejoined, 
   				success: function( data ) {   				
   					var listData=jQuery.parseJSON(data);
   					document.getElementById("dateProbationStartID").value=datejoined;
   					document.getElementById("dateProbationEndId").value=listData[0];
   					document.getElementById("probationDaysId").value=listData[1];
   				}
   			});
   	   }
       else {
    	   document.getElementById("probationDaysId").value=0;
    	   document.getElementById("dateProbationStartID").value='';
		   document.getElementById("dateProbationEndId").value='';
       }
    }   
    
    
    function getEmpProbationDates2(){    	
 	   var datejoined='';
 	   
 	   var probationTypeSelected=document.getElementById("leaveProbationTypeIds");
 	   var probationType=probationTypeSelected.options[probationTypeSelected.selectedIndex].value;
 	   
 	   var datehired=document.getElementById("datepicker").value;
 	   var dateRehired=document.getElementById("datepicker1").value;    	  
 	   if(datehired!=''&& dateRehired=='' ){
 		   
 		   datejoined=datehired;
 	   }
 	   else if(datehired==''&& dateRehired!=''){
 		   datejoined=dateRehired;
 	   }
 	   else if(datehired!=''&& dateRehired !=''){
 		   datejoined=dateRehired;
 	   }
 	   
 	   if(probationType!='' && datejoined!=''){
 		   
 		   jQuery.ajax({
				url:'${ctx}/hr/employee/ajax.do?action=getProbationDates&probationTypeId='+probationType+'&joinedDate='+datejoined, 
				success: function( data ) {   				
					var listData=jQuery.parseJSON(data);
					document.getElementById("dateLeaveProbationStartID").value=datejoined;
					document.getElementById("dateLeaveProbationEndId").value=listData[0];
					document.getElementById("leaveProbationDaysId").value=listData[1];
				}
			});
	   }
    else {
 	   document.getElementById("leaveProbationDaysId").value=0;
 	   document.getElementById("dateLeaveProbationStartID").value='';
	   document.getElementById("dateLeaveProbationEndId").value='';
    }
 }
    
    
    
    function getEmpProbationDates3(){    	
  	   var datejoined='';
  	   
  	   var probationTypeSelected=document.getElementById("generalProbationType");
  	   var probationType=probationTypeSelected.options[probationTypeSelected.selectedIndex].value;
  	   
  	   var datehired=document.getElementById("datepicker").value;
  	   var dateRehired=document.getElementById("datepicker1").value;    	  
  	   if(datehired!=''&& dateRehired=='' ){
  		   
  		   datejoined=datehired;
  	   }
  	   else if(datehired==''&& dateRehired!=''){
  		   datejoined=dateRehired;
  	   }
  	   else if(datehired!=''&& dateRehired !=''){
  		   datejoined=dateRehired;
  	   }
  	   
  	   if(probationType!='' && datejoined!=''){
  		   
  		   jQuery.ajax({
 				url:'${ctx}/hr/employee/ajax.do?action=getProbationDates&probationTypeId='+probationType+'&joinedDate='+datejoined, 
 				success: function( data ) {   				
 					var listData=jQuery.parseJSON(data);
 					document.getElementById("dateGeneralProbationStartID").value=datejoined;
 					document.getElementById("dateGeneralProbationEndId").value=listData[0];
 					document.getElementById("generalProbationDaysId").value=listData[1];
 				}
 			});
 	   }
     else {
  	   document.getElementById("generalProbationDaysId").value=0;
  	   document.getElementById("dateGeneralProbationStartID").value='';
 	   document.getElementById("dateGeneralProbationEndId").value='';
     }
  	   }
    
    function getEmpProbationTypes(){    	
  	   var datejoined=''; 	   
  	   
  	   var companyselected=document.getElementById("company"); 	
 	   var company=companyselected.options[companyselected.selectedIndex].value;
 	   
 	   var terminalselected=document.getElementById("terminalid"); 	
	   var terminal=terminalselected.options[terminalselected.selectedIndex].value;	  
	   
	   var categoryselected=document.getElementById("categoryid"); 	
	   var empCategory=categoryselected.options[categoryselected.selectedIndex].value;	
  	   
  	   var datehired=document.getElementById("datepicker").value;  	   
  	   var dateRehired=document.getElementById("datepicker1").value; 
  	   
  	   if(datehired!=''&& dateRehired=='' ){
  		   
  		   datejoined=datehired;
  	   }
  	   else if(datehired==''&& dateRehired!=''){
  		   datejoined=dateRehired;
  	   }
  	   else if(datehired!=''&& dateRehired !=''){
  		   datejoined=dateRehired;
  	   }
  	   
  	   if(company!='' && terminal!='' && datejoined!='' && empCategory!=''){
  		   
  		   jQuery.ajax({
 				url:'${ctx}/hr/employee/ajax.do?action=getPayrollProbationTypes&company='+company+'&joinedDate='+datejoined+'&terminal='+terminal+'&category='+empCategory, 
 				success: function( data ) {   				
 					var listData=jQuery.parseJSON(data);
 					var options = '<option value="">------Please Select------</option>';
					for ( var i = 0; i < listData.length; i++) {
						var probationType = listData[i];
						options += '<option value="'+probationType.id+'">'
								+ probationType.probationName								
								+ '</option>';
					}
					$("#probationTypeIds").html(options);
 				}
 			});
  		   
  		   
  		 jQuery.ajax({
				url:'${ctx}/hr/employee/ajax.do?action=getLeaveProbationTypes&company='+company+'&joinedDate='+datejoined+'&terminal='+terminal+'&category='+empCategory, 
				success: function( data ) {   				
					var listData=jQuery.parseJSON(data);
					var options = '<option value="">------Please Select------</option>';
					for ( var i = 0; i < listData.length; i++) {
						var probationType = listData[i];
						options += '<option value="'+probationType.id+'">'
								+ probationType.probationName								
								+ '</option>';
					}
					$("#leaveProbationTypeIds").html(options);
				}
			});
 	   }
     else {
  	   
     }
  }	   
       

</script>

<h3>
	<primo:label code="Add/Update Employee" />
</h3>
<form:form action="save.do" name="employeeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />
	<table width="65%" id="form-table" align="left">
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Add/Update Employee"/></b></td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Employee Id" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="staffId"  style="min-width:150px; max-width:150px"   path="staffId" cssClass="flat"
					  /> <br> <form:errors
					path="staffId" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
		<td class="form-left"><primo:label code="Company" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="company" id="company" style="min-width:154px; max-width:154px" onchange="javascript:getEmpProbationTypes()">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${companies}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="company" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Terminal" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="terminal" style="min-width:154px; max-width:154px" id="terminalid" onchange="javascript:getEmpProbationTypes()">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="terminal" cssClass="errorMessage" />
			</td>
		</tr>
			<tr>
		<td class="form-left"><primo:label code="Category" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="catagory" style="min-width:154px; max-width:154px" id="categoryid" onchange="javascript:getEmpProbationTypes()">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${catagories}" itemValue="id" itemLabel="name" />
				</form:select> <br> <form:errors path="catagory" cssClass="errorMessage" />
			</td>
		</tr>
		
		<tr>
			<td class="form-left"><primo:label code="Last Name" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="lastname" style="min-width:150px; max-width:150px"   onblur="return checklastname();"  path="lastName" cssClass="flat"
					  /> <br> <form:errors
					path="lastName" cssClass="errorMessage" /></td>
		<!-- </tr>
		<tr> -->
			<td class="form-left"><primo:label code="First Name" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="firstname"  style="min-width:150px; max-width:150px"  onblur="return checkfirstname();"  path="firstName" cssClass="flat"
					  /> <br> <form:errors
					path="firstName" cssClass="errorMessage" /></td>
		</tr>

		<tr>
			<td class="form-left"><primo:label code="Hired Date" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="dateHired" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="datepicker" onblur="return formatDate();" onchange="javascript:getEmpProbationTypes()"/> 
			<br> <form:errors path="dateHired" cssClass="errorMessage" /></td>
		</tr>
			<tr>
			<td class="form-left"><primo:label code="Re-Hired Date"/><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="dateReHired" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="datepicker1" onblur="return formatDate1();" onchange="javascript:getEmpProbationTypes()"/> 
			<br> <form:errors path="dateReHired" cssClass="errorMessage" /></td>
		</tr>
		<%-- <tr>
		<td class="form-left"><primo:label code="old Id" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input id="OldStaffId" path="OldStaffId" cssClass="flat"
					  /><a href="#" onclick="javascript:checkstaffis();"><span style="color: black">Search</span></a><br><span id="notes" style="color:Black"></span><br> <form:errors
					path="OldStaffId" cssClass="errorMessage" /></td>
		</tr> --%>
		<tr>
		
		<td class="form-left"><primo:label code="Payroll Probation" /><span
				class="errorMessage"></span></td>
				<td align="${left}">
				<form:select cssClass="flat" path="payrollProbation" id="probationTypeIds" style="min-width:154px; max-width:154px" onchange="javascript:getEmpProbationDates();">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${payrollProbationTypes}" itemValue="id" itemLabel="probationName" />
				</form:select>
				</td>
			
			
			<td class="form-left"><primo:label code="Days" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="probationDays" id="probationDaysId" style="min-width:150px; max-width:150px"
					cssClass="flat" readonly="true" />
			<br> <form:errors path="probationDays" cssClass="errorMessage" /></td>
			
		</tr>
		
		
		
		<tr>
			<td class="form-left"><primo:label code="PayRoll Probation Start Date" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="dateProbationStart" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="dateProbationStartID" onblur="return formatDate2();" readonly="true"/> 
			<br> <form:errors path="dateProbationStart" cssClass="errorMessage" /></td>
			
			<td class="form-left"><primo:label code="PayRoll Probation End Date" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="dateProbationEnd" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="dateProbationEndId" onblur="return formatDate3();" readonly="true"/> 
			<br> <form:errors path="dateProbationEnd" cssClass="errorMessage" /></td>
		</tr>
		
		
		
		<tr>
		
		<td class="form-left"><primo:label code="Leave Probation" /><span
				class="errorMessage"></span></td>
				<td align="${left}">
				<form:select cssClass="flat" path="leaveProbation" id="leaveProbationTypeIds" style="min-width:154px; max-width:154px" onchange="javascript:getEmpProbationDates2();">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${leaveProbationTypes}" itemValue="id" itemLabel="probationName" />
				</form:select>
				</td>
			
			
			<td class="form-left"><primo:label code="Days" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="leaveProbationDays" id="leaveProbationDaysId" style="min-width:150px; max-width:150px"
					cssClass="flat" readonly="true" />
			<br> <form:errors path="leaveProbationDays" cssClass="errorMessage" /></td>
			
		</tr>
		
		
		
		<tr>
			<td class="form-left"><primo:label code="Leave Probation Start Date" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="dateLeaveProbationStart" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="dateLeaveProbationStartID" onblur="return formatDate2();" readonly="true"/> 
			<br> <form:errors path="dateLeaveProbationStart" cssClass="errorMessage" /></td>
			
			<td class="form-left"><primo:label code="Leave Probation End Date" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="dateLeaveProbationEnd" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="dateLeaveProbationEndId" onblur="return formatDate3();" readonly="true"/> 
			<br> <form:errors path="dateLeaveProbationEnd" cssClass="errorMessage" /></td>
		</tr>
		
		
		
		
		
		<tr>
		
		<td class="form-left"><primo:label code="General Probation" /><span
				class="errorMessage"></span></td>
				<td align="${left}">
				<form:select cssClass="flat" path="generalProbation" id="generalProbationType" style="min-width:154px; max-width:154px" onchange="javascript:getEmpProbationDates3();">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<form:options items="${generalProbationTypes}" itemValue="id" itemLabel="probationName" />
				</form:select>
				</td>
			
			
			<td class="form-left"><primo:label code="Days" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="generalProbationDays" id="generalProbationDaysId" style="min-width:150px; max-width:150px"
					cssClass="flat" readonly="true" />
			<br> <form:errors path="generalProbationDays" cssClass="errorMessage" /></td>
			
		</tr>
		
		
		
		<tr>
			<td class="form-left"><primo:label code="General Probation Start Date" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="dateGeneralProbationStart" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="dateGeneralProbationStartID" onblur="return formatDate2();" readonly="true"/> 
			<br> <form:errors path="dateGeneralProbationStart" cssClass="errorMessage" /></td>
			
			<td class="form-left"><primo:label code="General Probation End Date" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input path="dateGeneralProbationEnd" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="dateGeneralProbationEndId" onblur="return formatDate3();" readonly="true"/> 
			<br> <form:errors path="dateGeneralProbationEnd" cssClass="errorMessage" /></td>
		</tr>
		
		
		
		
		
		
		
		
		
	
		<%-- <tr>
		<td class="form-left"><primo:label code="Weekly Salary" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input      path="weeklySalary" cssClass="flat"
					  /> <br> <form:errors
					path="weeklySalary" cssClass="errorMessage" /></td>
		</tr>
			<tr>
		<td class="form-left"><primo:label code="Hourly Regular Rate" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input      path="hourlyRegularRate" cssClass="flat"
					  /> <br> <form:errors
					path="hourlyRegularRate" cssClass="errorMessage" /></td>
		</tr>
		<tr>
		<td class="form-left"><primo:label code="Hourly Overtime Rate Factor" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input      path="hourlyOvertimeRate" cssClass="flat"
					  /> <br> <form:errors
					path="hourlyOvertimeRate" cssClass="errorMessage" /></td>
			<td class="form-left"><primo:label code="Hourly Doubletime Rate Factor" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input      path="hourlyDoubleTimeRate" cssClass="flat"
					  /> <br> <form:errors
					path="hourlyDoubleTimeRate" cssClass="errorMessage" /></td>
		</tr> --%>
		
		<tr>
		<td class="form-left"><primo:label code="Pay Term" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="payTerm">
					<form:options items="${payterm}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="payTerm" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><primo:label code="Shift" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="shift">
					<form:options items="${employeeshift}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="shift" cssClass="errorMessage" />
			</td>
		
		</tr>
		
			<tr>
			<td class="form-left"><primo:label code="Date Terminated" /></td>
			<td align="${left}"><form:input path="dateTerminated" style="min-width:150px; max-width:150px"
					cssClass="flat"  id="datepicker4" onblur="return formatDate4();"/> 
			<br> <form:errors path="dateTerminated" cssClass="errorMessage" /></td>
		</tr>
		
		<tr>
		<td class="form-left"><primo:label code="Status" /><span
				class="errorMessage">*</span></td>
		<td><form:select cssClass="flat" path="status">
					<form:options items="${employeestatus}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="status" cssClass="errorMessage" />
			</td>
					
		</tr>
			<tr>
		    <td class="form-left"><primo:label code="Notes" /><span
				class="errorMessage"></span></td>
			<td align="${left}" colspan="5">
				<form:textarea path="notes" rows="2" cols="75"/>    	
			</td>
		</tr>
		<tr><td colspan="2"></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}"><input type="submit"
				name="create" id="create" onclick=""
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" />
			</td>
		</tr>
	</table>
</form:form>

