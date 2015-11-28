<%@include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function searchre(){
	document.getElementById("bh").value='true';
	var leave=document.getElementById("leavetypeid").value;
	var employeselected=document.getElementById("empId");
	var driver=employeselected.options[employeselected.selectedIndex].value;
	if(leave==""){
		alert("Please Select Leave Type");
	}else if(driver==""){
		alert("Please Select Employee");
		
	}
	else{
	document.forms[0].submit();
	}
}
function getComTermCat(){
	var employeselected=document.getElementById("empId");
	var driver=employeselected.options[employeselected.selectedIndex].value;
  	jQuery.ajax({
		url:'${ctx}/hr/ptodapplication/ajax.do?action=findDCompany&driver='+driver, 
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
			$("#companyid").html(options);
		}
			
		});  
         
  	jQuery.ajax({
		url:'${ctx}/hr/ptodapplication/ajax.do?action=findDTerminal&driver='+driver, 
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
			$("#terminalid").html(options);
		}
			
		}); 
	
 	jQuery.ajax({
		url:'${ctx}/hr/ptodapplication/ajax.do?action=findDCategory&driver='+driver, 
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
			$("#categoryid").html(options);
		}
			
		}); 
	  

	  }
</script>
<script type="text/javascript">




function commonScript(){
	document.getElementById("trHdId1").style.display="table-row";
	document.getElementById("trHdId3").style.display="table-row";
	document.getElementById("trId4").style.display="table-row";
	document.getElementById("trId5").style.display="table-row";
	document.getElementById("trId6").style.display="table-row";		
	document.getElementById("trId8").style.display="table-row";
	document.getElementById("trId9").style.display="table-row";
	document.getElementById("trId10").style.display="table-row";
	document.getElementById("trId12").style.display="table-row";
	document.getElementById("trId13").style.display="table-row";
	document.getElementById("trId14").style.display="table-row";
    var usertype = document.getElementById("usertypeid").value;
	
	if(usertype!='HR' && usertype!='ADMIN'){	
		document.getElementById("trId16").style.display="none";		
	}else{	
	   document.getElementById("trId15").style.display="table-row";
	}
	
	document.getElementById("trId21").style.display="table-row";
	document.getElementById("trId22").style.display="table-row";
	document.getElementById("labelId1").innerHTML="Approve Status<span class=\"errorMessage\">*</span>";
	document.getElementById("labelId2").innerHTML="Approved By<span class=\"errorMessage\">*</span>";
	document.getElementById("labelId3").innerHTML="Amount Paid<span class=\"errorMessage\">*</span>";
	document.getElementById("labelId4").innerHTML="Hourly Amount Paid<span class=\"errorMessage\">*</span>";
}


function getExperience(){
	//alert("yes"); 
	var emp=document.getElementById("empId").value;
	var company=document.getElementById("companyid").value;
	var terminal=document.getElementById("terminalid").value; 
	var leavetype=document.getElementById("leavetypeid").value;
	var category=document.getElementById("categoryid").value;
	
	if(leavetype!=''){
		if(leavetype=='5' || leavetype=='6' || leavetype=='7' || leavetype=='8'){	
		document.getElementById("trHdId1").style.display="none";		
		document.getElementById("trId4").style.display="none";
		document.getElementById("trId5").style.display="none";
		document.getElementById("trId6").style.display="none";		
		/* document.getElementById("trId8").style.display="none";
		document.getElementById("trId9").style.display="none";
		document.getElementById("trId10").style.display="none"; */
		document.getElementById("trId12").style.display="none";
		document.getElementById("trId13").style.display="none";
		document.getElementById("trId14").style.display="none";
		/* document.getElementById("trId15").style.display="none";	 */
		document.getElementById("trId16").style.display="table-row";
		document.getElementById("trId21").style.display="none";
		document.getElementById("trId22").style.display="none";
		document.getElementById("labelId1").innerHTML="Approve Status";
		document.getElementById("labelId2").innerHTML="Approved By";
		document.getElementById("labelId3").innerHTML="Amount Paid";
		document.getElementById("labelId4").innerHTML="Hourly Amount Paid";
		}
		else{
		  	commonScript();
		}
	
	}
	else{
		commonScript();
	}
	
	if(emp==""){
	    document.getElementById("dayearnedId").value="";
		document.getElementById("datepicker").value="";
		document.getElementById("useddaysid").value=""; 
		document.getElementById("remainingdaysid").value="";
		document.getElementById("earnedhoursid").value="";
		document.getElementById("usedhoursid").value="";
		document.getElementById("remaininghoursid").value=""; 
	}
	if(company=="" && terminal=="" && leavetype=="" && category==""){
	    document.getElementById("ptodratesid").value="";
	    document.getElementById("ptodhourlyrateid").value="";
	}
	
	var selectedEmp= document.getElementById('empId');
	//alert(selectedEmp);
	var empId=selectedEmp.options[selectedEmp.selectedIndex].value;
	//alert(empId);
	if(emp!="" && company!="" && terminal!="" && leavetype!="" && category!=""){ 
	/* if(emp!="" && category!="" && leavetype!="" ){ */
	jQuery.ajax({
		 url:'${ctx}/hr/ptodapplication/ajax.do?action=findExp&driver='+empId+'&company='+company+'&terminal='+terminal+'&leavetype='+leavetype+'&category='+category,  
				/* url:'${ctx}/hr/ptodapplication/ajax.do?action=findExp&driver='+empId+'&leavetype='+leavetype+'&category='+category, */
		      success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var days=listData[0];
			var hireddate=listData[1];
			var usedday=listData[2];
			var remainday=listData[3];
			/* var ptodRate=listData[4];
			var ptodHourlyRate=listData[5]; */
			
			var earnedHrs=listData[6];
			var usedHrs=listData[7];
			var remainHrs=listData[8];
			var rehiredate=listData[9];	
			
			var nextanniversarydate=listData[10];
			var nextallotmentdays=listData[11];
			var nextallotmenthours=listData[12];
			
			if(hireddate!='null' && rehiredate=='null'){
			document.getElementById("hiredat").value=hireddate;
			}
			if(hireddate=='null' && rehiredate!='null'){
				document.getElementById("rehiredat").value=rehiredate;
			}			
			if(hireddate!='null' && rehiredate!='null'){
				document.getElementById("hiredat").value=hireddate;
				document.getElementById("rehiredat").value=rehiredate;
			}
			document.getElementById("dayearnedId").value=days;
			document.getElementById("useddaysid").value=usedday;
			document.getElementById("remainingdaysid").value=remainday;
			/* document.getElementById("ptodratesid").value=ptodRate;
			document.getElementById("ptodhourlyrateid").value=ptodHourlyRate; */
			
			document.getElementById("earnedhoursid").value=earnedHrs;
			document.getElementById("usedhoursid").value=usedHrs;
			document.getElementById("remaininghoursid").value=remainHrs;
			
			
			
			
			if(nextallotmentdays==null)
				nextallotmentdays=0.0;
			
				
			if(nextallotmenthours==null)
				nextallotmenthours=0.0;			
			
			document.getElementById("nextAllotmentDaysid").value=parseInt(nextallotmentdays);
			document.getElementById("nextAllotmenthoursid").value=nextallotmenthours;
			
			if(nextallotmentdays!=null)
			  document.getElementById("datepicker8").value=nextanniversarydate;
			
		    if(nextallotmenthours!=null)
			  document.getElementById("datepicker9").value=nextanniversarydate;
			
		}
	});	
	}
}
	
	
function getptodamount(){
	var emp=document.getElementById("empId").value;
	var company=document.getElementById("companyid").value;
	var terminal=document.getElementById("terminalid").value; 
	var leavetype=document.getElementById("leavetypeid").value;
	var category=document.getElementById("categoryid").value;
	var batchdate=document.getElementById("datepicker6").value;
	
	if(emp!="" && company!="" && terminal!="" && leavetype!="" && category!=""){ 
		/* if(emp!="" && category!="" && leavetype!="" ){ */
		jQuery.ajax({
			 url:'${ctx}/hr/ptodapplication/ajax.do?action=findAmount&driver='+emp+'&company='+company+'&terminal='+terminal+'&leavetype='+leavetype+'&category='+category+'&batch='+batchdate,  
					/* url:'${ctx}/hr/ptodapplication/ajax.do?action=findExp&driver='+empId+'&leavetype='+leavetype+'&category='+category, */
			      success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var ptodRate=listData[0];
				var ptodHourlyRate=listData[1];
				document.getElementById("ptodratesid").value=ptodRate;
				document.getElementById("ptodhourlyrateid").value=ptodHourlyRate;
				
			}
		});	
		}
}

function reCalculateAmount(){
	var dayspaid=document.getElementById("dayspaidid").value;            
	var dayspaidout=document.getElementById("paidoutdaysid").value;
	var ptodrate=document.getElementById("ptodratesid").value;
	
	if(dayspaid!='' && dayspaidout!='' && ptodrate!=''){
		jQuery.ajax({
			 url:'${ctx}/hr/ptodapplication/ajax.do?action=recalculateAmount&ndayspaid='+dayspaid+'&ndayspaidout='+dayspaidout+'&ptodrate='+ptodrate,  					
			      success: function( data ) {
				var listData=jQuery.parseJSON(data);
				var amount=listData[0];
				document.getElementById("amountpaidid").value=amount;
			      }
		});	
	}
	else if(dayspaid!='' && dayspaidout!=''){
		getPtodContents();
		
	}
	
	var usertype = document.getElementById("usertypeid").value;
	
	if(usertype!='HR' && usertype!='ADMIN'){
		var leavetype=document.getElementById("leavetypeid").value;
		if(leavetype!=''){
			if(leavetype=='5' || leavetype=='6' || leavetype=='7' || leavetype=='8'){
				document.getElementById("trId15").style.display="none";
			}else{
				document.getElementById("trId15").style.display="none";
				document.getElementById("trId16").style.display="none";
			}
		}
		else{
			document.getElementById("trId15").style.display="none";
			document.getElementById("trId16").style.display="none";
		}
	}
}


//FOR amountPaid and hourly amount paid
function getPtodContents(){
	//alert("yes"); 
	var company=document.getElementById("companyid").value;
	var terminal=document.getElementById("terminalid").value;
	var leavetype=document.getElementById("leavetypeid").value;
	var category=document.getElementById("categoryid").value;
	var emp=document.getElementById("empId").value;
	var dayspaid=document.getElementById("dayspaidid").value;            
	var dayspaidout=document.getElementById("paidoutdaysid").value;
	var batchdate=document.getElementById("datepicker6").value;
	var hourspaid=document.getElementById("hourspaidid").value;
	var hourspaidout=document.getElementById("paidouthoursid").value;
	
	if(company!="" && terminal!="" && leavetype!="" && category!="" && dayspaid!="" && dayspaidout!=""){
	if(leavetype!='5' && leavetype!='6' && leavetype!='7'){
		jQuery.ajax({
		url:'${ctx}/hr/ptodapplication/ajax.do?action=amountPaid&company='+company+'&terminal='+terminal+'&leavetype='+leavetype+'&category='+category+'&dayspaid='+dayspaid+'&dayspaidout='+dayspaidout+'&batch='+batchdate+'&driver='+emp,
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var amount=listData[0];
			
			document.getElementById("amountpaidid").value=amount;
		 }
	});
		getptodamount();
	}
	}
	
	
	//for hourly amount paid
	if(company!="" && terminal!="" && leavetype!="" && category!="" && hourspaid!="" && emp!=null){
		if(leavetype!='5' && leavetype!='6' && leavetype!='7'){
	jQuery.ajax({
		url:'${ctx}/hr/ptodapplication/ajax.do?action=hourlyamountPaid&company='+company+'&terminal='+terminal+'&leavetype='+leavetype+'&category='+category+'&hourspaid='+hourspaid+'&driver='+emp+'&batch='+batchdate+'&hourspaidout='+hourspaidout,
		success: function( data ) {
			var listData=jQuery.parseJSON(data);
			var amount=listData[0];
		    document.getElementById("hourlyamountpaidid").value=amount;
		}
	});
	getptodamount();
	}
	}
	
	
}


function disableFields(){	

	var aprvstatus=document.getElementById('apprvStatus').value;  
	var idvalue=document.getElementById('id').value;
	
	if(aprvstatus=='0'||aprvstatus=='2'){ 		
		$('#paidoutdaysid').attr('readonly', true);
		$('#dayspaidid').attr('readonly', true);
		$('#paidouthoursid').attr('readonly', true);
		$('#hourspaidid').attr('readonly', true);
		$('#amountpaidid').attr('readonly', true);
		$('#hourlyamountpaidid').attr('readonly', true);
		if(idvalue==''){
			$('#paidoutdaysid').attr('value','0');
			$('#dayspaidid').attr('value','0');
			$('#paidouthoursid').attr('value','0.0');
			$('#hourspaidid').attr('value','0.0');
			$('#amountpaidid').attr('value','0.00');
			$('#hourlyamountpaidid').attr('value','0.00');
		}
    }
	else {
		$('#paidoutdaysid').attr('readonly', false);
		$('#dayspaidid').attr('readonly', false);
		$('#paidouthoursid').attr('readonly', false);
		$('#hourspaidid').attr('readonly', false);
		$('#amountpaidid').attr('readonly', false);
		$('#hourlyamountpaidid').attr('readonly',false);
		if(idvalue==''){
			$('#amountpaidid').attr('value','');
			$('#hourlyamountpaidid').attr('value','');
		}
	}
}

$(document).ready(function(){
	disableFields();
	reCalculateAmount();
});


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



function formatDate8(){
	var date=document.getElementById("datepicker8").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker8").value="";
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
						document.getElementById("datepicker8").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker8").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker8").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker8").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker8").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker8").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker8").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker8").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker8").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker8").value=date;
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


function formatDate5(){
	var date=document.getElementById("datepicker5").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker5").value="";
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
						document.getElementById("datepicker5").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker5").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker5").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker5").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker5").value=date;
		}
	 }
   }
}
function formatDate6(){
	
	var date=document.getElementById("datepicker6").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalidte date format");
		document.getElementById("datepicker6").value="";
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
						document.getElementById("datepicker6").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker6").value="";
					return true;
				}
				else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker6").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker6").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("invalid date format");
					document.getElementById("datepicker6").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker6").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("invalid date format");
						document.getElementById("datepicker6").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("invalid date format");
					document.getElementById("datepicker6").value="";
					return true;
				}else if(dd>31){
					alert("invalid date format");
					document.getElementById("datepicker6").value="";
					return true;
				}
			}
			var date=mm+"-"+dd+"-"+yy;
			document.getElementById("datepicker6").value=date;
		}
	 }
   }
	
}
</script>



<h3>
	<primo:label code="Add/Update PTOD Application" />
</h3>


<form:form action="save.do" name="typeForm" commandName="modelObject"
	method="post">
	<form:hidden path="id" id="id" />	
	<input id="usertypeid" type="hidden" value="${sessionScope.userInfo.role.name}"/>	
	<table id="form-table" width="100%" cellspacing="1" cellpadding="5">
	<tr >
	<!-- <td></td>
	<td></td> -->
	<td><input type="button" value="Leave Balance History" onclick="javascript:searchre()" /></td>
	<td><input type="hidden" name="bh" id="bh"/></td>
	</tr>
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Manage PTOD Application" />
			</b></td>
		</tr>
		
		
		<tr id="trId1">
		
		        <td class="form-left"><primo:label code="Employee" /><span	class="errorMessage">*</span></td>
		     <%-- 	<td align="${left}"><form:select cssClass="flat" path="driver" id="empId"  onchange="javascript:getComTermCat()">
					<form:option value="">------<primo:label
							code="Please Select" />------</form:option>
					<c:forEach var="item" items="${employees}">
						<c:set var="selected" value=""/>
						<c:if test="${item.id eq modelObject.driver.id}">
							<c:set var="selected" value="selected"/>
						</c:if>
						<option value="${item.id}" ${selected}>${item.fullName}--${item.catagory.name}</option>
					</c:forEach>
				</form:select> <br> <form:errors path="driver" cssClass="errorMessage" />
			</td> --%>
			<td align="${left}">
				<form:select cssClass="flat" path="driver" style="min-width:154px; max-width:154px"  id="empId" onchange="javascript:getComTermCat()">
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="fullName"></form:options>
				</form:select> <br><form:errors path="driver" cssClass="errorMessage" />
			</td>
		
		
		
		  <td class="form-left"><primo:label code="Category" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="category" id="categoryid" style="min-width:154px; max-width:154px" onblur="javascript:getExperience()">
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${catagories}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="category" cssClass="errorMessage" />
			</td>	
			
		</tr>
		<tr id="trId2">		
		  	<td class="form-left"><primo:label code="Company" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="company" id="companyid" style="min-width:154px; max-width:154px" onblur="javascript:getExperience()">
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${companyLocation}" itemValue="id" itemLabel="name"/>
				</form:select> 
				<br><form:errors path="company" cssClass="errorMessage" />
			</td>		  
		  
			
		    <td class="form-left"><primo:label code="Leave Type" /><span class="errorMessage">*</span></td>
		       <td align="${left}">
		       <form:select cssClass="flat" path="leavetype" style="min-width:154px; max-width:154px" id="leavetypeid" onchange="javascript:getExperience()">
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${leavetypes}" itemValue="id"
						itemLabel="name" />
				</form:select> <br><form:errors path="leavetype" cssClass="errorMessage" /></td>
					
		</tr>
	  
		
		<tr id="trId3">
		
		<td class="form-left"><primo:label code="Terminal" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="terminal" style="min-width:154px; max-width:154px" id="terminalid" onblur="javascript:getExperience()">
					<form:option value="">-------Please Select------</form:option>
					<form:options items="${terminals}" itemValue="id" itemLabel="name" />
				</form:select> 
				<br><form:errors path="terminal" cssClass="errorMessage" />
			</td>	
		 
		 
		    <!-- <td class="form-left"></td>  -->
		    <td class="form-left"><primo:label code="Hired Date" /><span class="errorMessage">*</span></td>
		    <c:choose>
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}">
			<td align="${left}"><form:input  path="hireddate" id="hiredat" style="min-width:150px; max-width:150px"
					Class="flat" readonly="true"  /> <br> <form:errors path="hireddate" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="hireddate" id="hiredat" style="min-width:150px; max-width:150px"
					Class="flat"  readonly="true"  value=""/> <br> <form:errors path="hireddate" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose>
		</tr>
		<tr>
		<td class="form-left"></td><td></td>
		   <td class="form-left"><primo:label code="ReHired Date" /></td>
		   <td align="${left}"><form:input path="rehireddate" id="rehiredat" style="min-width:150px; max-width:150px"
					Class="flat"  readonly="true"  value=""/> <br> <form:errors path="rehireddate" cssClass="errorMessage" /></td>	
		</tr>
		
		<tr class="table-heading" id="trHdId1">
			<td colspan="4"><b><primo:label code="Leave Information" />
			</b></td>
		</tr>
		
		
		<tr id="trId4">
		        
		    <%-- <td class="form-left"><primo:label code="Hired Date" /><span class="errorMessage">*</span></td>
		    <c:choose>
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}">
			<td align="${left}"><form:input  path="hireddate" id="datepicker"
					Class="flat" readonly="true"  onblur="return formatDate();"/> <br> <form:errors path="hireddate" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="hireddate" id="datepicker"
					Class="flat"  readonly="true"  value=""/> <br> <form:errors path="hireddate" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose> --%>
		        
		        
				
				<%-- <td class="form-left"><primo:label code="Hired Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker" path="hireddate"
					cssClass="flat" onblur="return formatDate();"/> <br> <form:errors
					path="hireddate" cssClass="errorMessage" /></td> --%>
					
					
			<td class="form-left"><primo:label code="Days Earned" /><span
				class="errorMessage">*</span></td>
		    <c:choose>
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}">
			<td align="${left}"><form:input path="earneddays" id="dayearnedId" style="min-width:150px; max-width:150px"
					Class="flat" readonly="true"/> <br> <form:errors path="earneddays" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="earneddays" id="dayearnedId" style="min-width:150px; max-width:150px"
					Class="flat"  readonly="true"  value=""/> <br> <form:errors path="earneddays" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose>
				
			<td class="form-left"><primo:label code="Hours Earned" /><span
				class="errorMessage"></span></td>
		    <c:choose>
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}">
			<td align="${left}"><form:input path="earnedhours" id="earnedhoursid" style="min-width:150px; max-width:150px"
					Class="flat" readonly="true"/> <br> <form:errors path="earneddays" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="earnedhours" id="earnedhoursid" style="min-width:150px; max-width:150px"
					Class="flat"  readonly="true"  value=""/> <br> <form:errors path="earnedhours" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose>		
					
			<%-- <td class="form-left"><primo:label code="Hours Earned" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="earnedhours" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="earnedhours" cssClass="errorMessage" />
			</td> --%> 
				
		</tr>
		
		
		
		
		<tr id="trId5">
		<%-- <td class="form-left"><primo:label code="Used Days" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="useddays" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="useddays" cssClass="errorMessage" />
			</td> --%>	
			
			
			<td class="form-left"><primo:label code="Used Days" /><span
				class="errorMessage">*</span></td>
		   <%--  <c:choose>
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}"> --%>
			<td align="${left}"><form:input path="useddays" id="useddaysid" style="min-width:150px; max-width:150px"
					Class="flat" readonly="true"/> <br> <form:errors path="useddays" cssClass="errorMessage" /></td>
			<%-- </c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="useddays" id="useddaysid"
					Class="flat"  readonly="true"  value=""/> <br> <form:errors path="useddays" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose> --%>
			
			<%-- <td class="form-left"><primo:label code="Used Hours" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="usedhours" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="usedhours" cssClass="errorMessage" />
			</td>  --%>
			
			<td class="form-left"><primo:label code="Used Hours" /><span
				class="errorMessage"></span></td>
		   <%--  <c:choose>
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}"> --%>
			<td align="${left}"><form:input path="usedhours" id="usedhoursid" style="min-width:150px; max-width:150px"
					Class="flat" readonly="true"/> <br> <form:errors path="usedhours" cssClass="errorMessage" /></td>
			
	</tr>		
	<tr id="trId6">		
			
			
		   <%-- <td class="form-left"><primo:label code="Remaining Days" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="remainingdays" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="remainingdays" cssClass="errorMessage" />
			</td>	 --%>
			
			<td class="form-left"><primo:label code="Remaining Days" /><span
				class="errorMessage">*</span></td>
		    <c:choose>
		    
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}">
			<td align="${left}"><form:input path="remainingdays" id="remainingdaysid" style="min-width:150px; max-width:150px"
					Class="flat" readonly="true"/> <br> <form:errors path="remainingdays" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="remainingdays" id="remainingdaysid" style="min-width:150px; max-width:150px"
					Class="flat"  readonly="true"  value=""/> <br> <form:errors path="remainingdays" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose>
			
			<%-- <td class="form-left"><primo:label code="Remaining Hours" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="remaininghours" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="remaininghours" cssClass="errorMessage" />
			</td>  --%>
			
			<td class="form-left"><primo:label code="Remaining Hours" /><span
				class="errorMessage"></span></td>
		    <c:choose>
		    
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}">
			<td align="${left}"><form:input path="remaininghours" id="remaininghoursid" style="min-width:150px; max-width:150px"
					Class="flat" readonly="true"/> <br> <form:errors path="remaininghours" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="remaininghours" id="remaininghoursid" style="min-width:150px; max-width:150px"
					Class="flat"  readonly="true"  value=""/> <br> <form:errors path="remaininghours" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose>
			
			
			
	  </tr>
	  
	  <tr id="trId21">
	  <td class="form-left"><primo:label code="Next Allotment Date" /><span
				class="errorMessage"></span></td>
	  <td align="${left}"><form:input id="datepicker8" path="nextAnniversaryDate" style="min-width:150px; max-width:150px"
					Class="flat"  readonly="true"  value="" onblur="return formatDate8();"/> <br> <form:errors
					path="nextAnniversaryDate" cssClass="errorMessage" /></td>
					
	<td class="form-left"><%-- <primo:label code="Next Allotment Date" /> --%><span
				class="errorMessage"></span></td>
	  <td align="${left}"><%-- <form:input id="datepicker9" path="nextAnniversaryDate" style="min-width:150px; max-width:150px"
					Class="flat"  readonly="true"  value="" onblur="return formatDate9();"/> <br> <form:errors
					path="nextAnniversaryDate" cssClass="errorMessage" /> --%></td>				
					
	
					
	  </tr>
	  
	  
	  <tr id="trId22">
	  <td class="form-left"><primo:label code="Next Allotment Days" />
	  <td align="${left}"><form:input path="nextAllotmentDays" id="nextAllotmentDaysid" style="min-width:150px; max-width:150px"
					Class="flat"  readonly="true"  value=""/> <br> <form:errors path="nextAllotmentDays" cssClass="errorMessage" /></td>
	  
	  <td class="form-left"><primo:label code="Next Allotment Hours" />
	  <td align="${left}"><form:input path="nextAllotmenthours" id="nextAllotmenthoursid" style="min-width:150px; max-width:150px"
					Class="flat"  readonly="true"  value=""/> <br> <form:errors path="nextAllotmenthours" cssClass="errorMessage" /></td>
	  </tr>
	  
	  
	  <tr class="table-heading" id="trHdId2">
			<td colspan="4"><b><primo:label code="Dates" />
			</b></td>
		</tr>
		<tr id="trId7">
		   <td class="form-left"><primo:label code="Date from" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input id="datepicker1" path="leavedatefrom" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate1();"/> <br> <form:errors
					path="leavedatefrom" cssClass="errorMessage" /></td>
					
					
		   <td class="form-left"><primo:label code="Date To" /><span
				class="errorMessage"></span></td>
			<td align="${left}"><form:input id="datepicker2" path="leavedateto" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate2();"/> <br> <form:errors
					path="leavedateto" cssClass="errorMessage" /></td>			
		  
		</tr>
		
		<tr id="trId8">
		   
		   <td class="form-left"><primo:label code="Days Requested" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="daysrequested"  cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" />
			 	<br><form:errors path="daysrequested" cssClass="errorMessage" />
			</td>
			<td class="form-left"><primo:label code="Hours Requested" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="hoursrequested"  cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" />
			 	<br><form:errors path="hoursrequested" cssClass="errorMessage" />
			</td>
			
		</tr>
		<tr id="trId9">
		   <td class="form-left"><primo:label code="Submit Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker3" path="submitdate" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate3();"/> <br> <form:errors
					path="submitdate" cssClass="errorMessage" /></td>
					
				
		   <td class="form-left"><primo:label code="Batch Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker6" path="batchdate" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate6();"/> <br> <form:errors
					path="batchdate" cssClass="errorMessage" /></td>			
		  
		</tr>
		
		<tr id="trId10">
		
		<%-- <td class="form-left"><primo:label code="Days Requested" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="daysrequested" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="daysrequested" cssClass="errorMessage" />
			</td> --%>	
		     <td class="form-left"><primo:label code="Check Date" /></td>
			<td align="${left}"><form:input id="datepicker5" path="checkdate" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate5();"/> <br> <form:errors
					path="checkdate" cssClass="errorMessage" /></td>
		
		     <td class="form-left"><primo:label code="Approved Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker4" path="dateapproved" style="min-width:150px; max-width:150px"
					cssClass="flat" onblur="return formatDate4();"/> <br> <form:errors
					path="dateapproved" cssClass="errorMessage" /></td>	 
		      
		      
		</tr>
		
		<tr id="trId11">
		       <td class="form-left" id="labelId1"><primo:label code="Approve Status" /><span class="errorMessage">*</span></td>
		         <td><form:select id="apprvStatus" cssClass="flat" path="approvestatus" style="min-width:154px; max-width:154px" onchange="javascript:disableFields()">
		           <form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${approvestatuss}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="approvestatus" cssClass="errorMessage" />
			</td>
			
			<td class="form-left" id="labelId2"><primo:label code="Approved By" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="approveby" style="min-width:154px; max-width:154px" id="empId">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="fullName"></form:options>
				</form:select> <br><form:errors path="approveby" cssClass="errorMessage" />
			</td>
			
	   </tr>
	  
	  
	  
	  
	  
	  
	  
	  
	  <tr class="table-heading" id="trHdId3">
			<td colspan="4"><b><primo:label code="Rates and Amount" />
			</b></td>
		</tr>
		<tr id="trId12">
		   
			
			<td class="form-left"><primo:label code="Paid Out Days" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="paidoutdays"  id="paidoutdaysid" cssClass="flat" style="min-width:150px; max-width:150px" onblur="javascript:getPtodContents()"/> 
			 	<br><form:errors path="paidoutdays" cssClass="errorMessage" />
			</td>	
			<td class="form-left"><primo:label code="Paid Out Hours" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="paidouthours"  id="paidouthoursid" cssClass="flat" style="min-width:150px; max-width:150px" onblur="javascript:getPtodContents()"/> 
			 	<br><form:errors path="paidouthours" cssClass="errorMessage" />
			</td>
			
		</tr>
		
		
		<tr id="trId13">
		   <td class="form-left"><primo:label code="Days Paid" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="dayspaid" id="dayspaidid" cssClass="flat" style="min-width:150px; max-width:150px" onblur="javascript:getPtodContents()"/>
			 	<br><form:errors path="dayspaid" cssClass="errorMessage" />
			</td>	
			
			<%-- <td class="form-left"><primo:label code="Days UnPaid" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="daysunpaid"  id="daysunpaidid" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="daysunpaid" cssClass="errorMessage" />
			</td> --%>	
			<td class="form-left"><primo:label code="Hours Paid" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="hourspaid"  id="hourspaidid" style="min-width:150px; max-width:150px" onblur="javascript:getPtodContents()"/>
			 	<br><form:errors path="hourspaid" cssClass="errorMessage" />
			</td>	
			
		</tr>
		
		<tr id="trId14">
		   <%-- <td class="form-left"><primo:label code="Hours Paid" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="hourspaid"  id="hourspaidid" onblur="javascript:getPtodContents()"/>
			 	<br><form:errors path="hourspaid" cssClass="errorMessage" />
			</td>	 --%>
			<td class="form-left"><primo:label code="Days UnPaid" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="daysunpaid"  id="daysunpaidid" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" />
			 	<br><form:errors path="daysunpaid" cssClass="errorMessage" />
			</td>
			
			
			<td class="form-left"><primo:label code="Hours UnPaid" /><span class="errorMessage"></span></td>
			<td align="${left}">
				<form:input path="hoursunpaid" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" />
			 	<br><form:errors path="hoursunpaid" cssClass="errorMessage" />
			</td>	
			
			
		</tr>
		
		
		
		
		
		<tr id="trId15">
		    <td class="form-left"><primo:label code="PTOD Rate" /><span
				class="errorMessage">*</span></td>
		    <c:choose>
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}">
			<td align="${left}"><form:input path="ptodrates" id="ptodratesid" style="min-width:150px; max-width:150px"
					Class="flat" onblur="javascript:reCalculateAmount()"/> <br> <form:errors path="ptodrates" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="ptodrates" id="ptodratesid" style="min-width:150px; max-width:150px"
					Class="flat"    value="" onblur="javascript:reCalculateAmount()"/> 
					<br> <form:errors path="ptodrates" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose>
			<%-- <td class="form-left"><primo:label code="PTOD Rate" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="ptodrates" cssClass="flat" cssStyle="width:150px;" maxlength="20" /><!-- <b>Days</b> -->
			 	<br><form:errors path="ptodrates" cssClass="errorMessage" />
			</td> --%>
		   <%-- <td class="form-left"><primo:label code="PTOD Rate" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="ptodrates" id="ptodratesid" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="ptodrates" cssClass="errorMessage" />
			</td> --%>	
			
			
			
			
			
			<td class="form-left"><primo:label code="PTOD Hourly Rate" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="ptodhourlyrate" id="ptodhourlyrateid" cssClass="flat" style="min-width:150px; max-width:150px" maxlength="20" />
			 	<br><form:errors path="ptodhourlyrate" cssClass="errorMessage" />
			</td>	
	   </tr>
	  
	   
	   <tr id="trId16">
	        <%-- <td class="form-left"><primo:label code="Amount Paid" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="amountpaid" id="amountpaidid" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="amountpaid" cssClass="errorMessage" />
			</td> --%>	
	       <td class="form-left" id="labelId3"><primo:label code="Amount Paid" /><span
				class="errorMessage">*</span></td>
		    <c:choose>
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}">
			<td align="${left}"><form:input path="amountpaid" id="amountpaidid" style="min-width:150px; max-width:150px"
					Class="flat" /> <br> <form:errors path="amountpaid" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="amountpaid" id="amountpaidid" style="min-width:150px; max-width:150px"
					Class="flat" value=""/> <br> <form:errors path="amountpaid" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose>
	       
	        
	        
		   <%-- <td class="form-left"><primo:label code="Hourly Amount Paid" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="hourlyamountpaid" id="hourlyamountpaidid" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="hourlyamountpaid" cssClass="errorMessage" />
			</td> --%>	
			<td class="form-left" id="labelId4"><primo:label code="Hourly Amount Paid" /><span
				class="errorMessage">*</span></td>
		    <c:choose>
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}">
			<td align="${left}"><form:input path="hourlyamountpaid" id="hourlyamountpaidid" style="min-width:150px; max-width:150px"
					Class="flat"/> <br> <form:errors path="hourlyamountpaid" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="hourlyamountpaid" id="hourlyamountpaidid" style="min-width:150px; max-width:150px"
					Class="flat" /> <br> <form:errors path="hourlyamountpaid" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose>
	   </tr>
	  
	   
	   <tr class="table-heading" >
			<td colspan="4"><b><primo:label code="Pay Chex Related" />
			</b></td>
		</tr>
	  
	  <tr>
	  <td class="form-left" ><primo:label code="Sequence#" /></td>
	  <td align="${left}" colspan="3">
				<form:input path="sequenceNum1"  cssClass="flat" style="min-width:75px; max-width:75px" maxlength="20" />
			 	
			 	
			 	<form:input path="sequenceNum2"  cssClass="flat" style="min-width:75px; max-width:75px" maxlength="20" />
			 	
			 	<form:input path="sequenceNum3"  cssClass="flat" style="min-width:75px; max-width:75px" maxlength="20" />
			 	
			 	
			 	<form:input path="sequenceNum4"  cssClass="flat" style="min-width:75px; max-width:75px" maxlength="20" />
			 	
	  </td>	   
	   
	  </tr> 
	  
	  <tr>
	  <td class="form-left" ><primo:label code="Amount" /></td>
	  <td align="${left}" colspan="3">
				<form:input path="sequenceAmt1"  cssClass="flat" style="min-width:75px; max-width:75px" maxlength="20" />
			 	
			 	
			 	<form:input path="sequenceAmt2"  cssClass="flat" style="min-width:75px; max-width:75px" maxlength="20" />
			 	
			 	<form:input path="sequenceAmt3"  cssClass="flat" style="min-width:75px; max-width:75px" maxlength="20" />
			 	
			 	
			 	<form:input path="sequenceAmt4"  cssClass="flat" style="min-width:75px; max-width:75px" maxlength="20" />
			 	
	  </td>	 
	  </tr>
	  
		<%-- <tr class="table-heading">
			<td colspan="4"><b><primo:label code="Dates" />
			</b></td>
		</tr>
		<tr>
		   <td class="form-left"><primo:label code="Date from" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker1" path="leavedatefrom"
					cssClass="flat" onblur="return formatDate1();"/> <br> <form:errors
					path="leavedatefrom" cssClass="errorMessage" /></td>
					
					
		   <td class="form-left"><primo:label code="Date To" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker2" path="leavedateto"
					cssClass="flat" onblur="return formatDate2();"/> <br> <form:errors
					path="leavedateto" cssClass="errorMessage" /></td>			
		  
		</tr>
		
		<tr>
		   
		   <td class="form-left"><primo:label code="Days Requested" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="daysrequested"  cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="daysrequested" cssClass="errorMessage" />
			</td>
		   
		   <td class="form-left"><primo:label code="Submit Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker3" path="submitdate"
					cssClass="flat" onblur="return formatDate()3;"/> <br> <form:errors
					path="submitdate" cssClass="errorMessage" /></td>
					
					
		   <td class="form-left"><primo:label code="Approved Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker4" path="dateapproved"
					cssClass="flat" onblur="return formatDate()4;"/> <br> <form:errors
					path="dateapproved" cssClass="errorMessage" /></td>			
		  
		</tr>
		
		<tr>
		
		<td class="form-left"><primo:label code="Days Requested" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="daysrequested" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="daysrequested" cssClass="errorMessage" />
			</td>	
		     <td class="form-left"><primo:label code="Check Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker5" path="checkdate"
					cssClass="flat" onblur="return formatDate5();"/> <br> <form:errors
					path="checkdate" cssClass="errorMessage" /></td>
		
		     <td class="form-left"><primo:label code="Approved Date" /><span
				class="errorMessage">*</span></td>
			<td align="${left}"><form:input id="datepicker4" path="dateapproved"
					cssClass="flat" onblur="return formatDate()4;"/> <br> <form:errors
					path="dateapproved" cssClass="errorMessage" /></td>	 
		      
		      
		</tr>
		
		<tr>
		       <td class="form-left"><primo:label code="Approve Status" /><span class="errorMessage">*</span></td>
		         <td><form:select cssClass="flat" path="approvestatus">
					<form:options items="${approvestatuss}" itemValue="dataValue" itemLabel="dataText" />
				</form:select> <br> <form:errors path="approvestatus" cssClass="errorMessage" />
			</td>
			
			<td class="form-left"><primo:label code="Approved By" /><span	class="errorMessage">*</span></td>
			<td align="${left}">
				<form:select cssClass="flat" path="approveby" id="empId">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="fullName"></form:options>
				</form:select> <br><form:errors path="approveby" cssClass="errorMessage" />
			</td>
			
	   </tr> --%>
		<%-- <tr class="table-heading">
			<td colspan="4"><b><primo:label code="Rates and Amount" />
			</b></td>
		</tr>
		<tr>
		   
			
			<td class="form-left"><primo:label code="Paid Out Days" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="paidoutdays"  id="paidoutdaysid" cssClass="flat" cssStyle="width:150px;" onblur="javascript:getPtodContents()"/> 
			 	<br><form:errors path="paidoutdays" cssClass="errorMessage" />
			</td>	
			<td class="form-left"></td>
			
		</tr>
		
		
		<tr>
		   <td class="form-left"><primo:label code="Days Paid" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="dayspaid" id="dayspaidid" cssClass="flat" cssStyle="width:150px;" onblur="javascript:getPtodContents()"/>
			 	<br><form:errors path="dayspaid" cssClass="errorMessage" />
			</td>	
			
			<td class="form-left"><primo:label code="Days UnPaid" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="daysunpaid"  id="daysunpaidid" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="daysunpaid" cssClass="errorMessage" />
			</td>	
			
			
		</tr>
		
		<tr>
		   <td class="form-left"><primo:label code="Hours Paid" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="hourspaid"  id="hourspaidid" onblur="javascript:getPtodContents()"/>
			 	<br><form:errors path="hourspaid" cssClass="errorMessage" />
			</td>	
			
			<td class="form-left"><primo:label code="Hours UnPaid" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="hoursunpaid" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="hoursunpaid" cssClass="errorMessage" />
			</td>	
			
			
		</tr>
		
		<tr>
		    <td class="form-left"><primo:label code="PTOD Rate" /><span
				class="errorMessage">*</span></td>
		    <c:choose>
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}">
			<td align="${left}"><form:input path="ptodrates" id="ptodratesid"
					Class="flat" readonly="true"/> <br> <form:errors path="ptodrates" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="ptodrates" id="ptodratesid"
					Class="flat"  readonly="true"  value=""/> 
					<br> <form:errors path="ptodrates" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose>
			<td class="form-left"><primo:label code="PTOD Rate" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="ptodrates" cssClass="flat" cssStyle="width:150px;" maxlength="20" /><!-- <b>Days</b> -->
			 	<br><form:errors path="ptodrates" cssClass="errorMessage" />
			</td>
		   <td class="form-left"><primo:label code="PTOD Rate" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="ptodrates" id="ptodratesid" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="ptodrates" cssClass="errorMessage" />
			</td>	
			
			
			
			
			
			<td class="form-left"><primo:label code="PTOD Hourly Rate" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="ptodhourlyrate" id="ptodhourlyrateid" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="ptodhourlyrate" cssClass="errorMessage" />
			</td>	
	   </tr>
	   
	   <tr>
	        <td class="form-left"><primo:label code="Amount Paid" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="amountpaid" id="amountpaidid" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="amountpaid" cssClass="errorMessage" />
			</td>	
	       <td class="form-left"><primo:label code="Amount Paid" /><span
				class="errorMessage">*</span></td>
		    <c:choose>
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}">
			<td align="${left}"><form:input path="amountpaid" id="amountpaidid"
					Class="flat" readonly="true"/> <br> <form:errors path="amountpaid" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="amountpaid" id="amountpaidid"
					Class="flat"  readonly="true"  value=""/> <br> <form:errors path="amountpaid" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose>
	       
	        
	        
		   <td class="form-left"><primo:label code="Hourly Amount Paid" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input path="hourlyamountpaid" id="hourlyamountpaidid" cssClass="flat" cssStyle="width:150px;" maxlength="20" />
			 	<br><form:errors path="hourlyamountpaid" cssClass="errorMessage" />
			</td>	
			<td class="form-left"><primo:label code="Hourly Amount Paid" /><span
				class="errorMessage">*</span></td>
		    <c:choose>
			<c:when test="${modelObject.id!=null && modelObject.driver!=null}">
			<td align="${left}"><form:input path="hourlyamountpaid" id="hourlyamountpaidid"
					Class="flat" readonly="true"/> <br> <form:errors path="hourlyamountpaid" cssClass="errorMessage" /></td>
			</c:when>
			<c:otherwise>
			<td align="${left}"><form:input path="hourlyamountpaid" id="hourlyamountpaidid"
					Class="flat"  readonly="true"  value=""/> <br> <form:errors path="hourlyamountpaid" cssClass="errorMessage" /></td>
			</c:otherwise>
			</c:choose>
	   </tr> --%>
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Notes" />
			</b></td>
		</tr>
		<tr>
		    <td class="form-left"><primo:label code="Notes" /><span
				class="errorMessage"></span></td>
			<td align="${left}" colspan="5">
				<form:textarea path="notes" rows="2" cols="75"/>    	
			</td>
		</tr>
		
	    <tr><td colspan="2"><!-- <script type="text/javascript">getPtodContents();</script> --></td></tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2"><input type="submit"
				name="create" id="create" onclick=""
				value="<primo:label code="Save"/>" class="flat" /> <input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" /></td>
		</tr>
		
	</table>
</form:form>
<script language="javascript">
getExperience();
</script>
	
