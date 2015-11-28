<%@ include file="/common/taglibs.jsp"%>
<jsp:include page="/trans/hr/report/employeebonus/html1.jsp" />
<script language="javascript">
$(document).ready(function(){
	   $("select").multiselect();
});


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

<h3>
	<primo:label code="Update Employee Bonus" />
</h3>
<form action="/trans/hr/empbonus/updatebonus.do" method="post">
		
		<table> 
		<tr><td><font color="#67636B">Employee:</font> <font color="black" size="2">${modelObject.driver.fullName}</font></td>
		<td><font color="#67636B" >Category:</font> <font color="black" size="2">${modelObject.category.name}</font></td>
		<td><font color="#67636B" >Company:</font> <font color="black" size="2">${modelObject.company.name}</font></td>
		<td><font color="#67636B" >Terminal:</font> <font color="black" size="2">${modelObject.terminal.name}</font></td>
		</tr><tr><td><font color="#67636B" >Date Hired:</font> <font color="black" size="2"><fmt:formatDate pattern="MM-dd-yyyy" value="${modelObject.driver.dateHired}" /></font>
		</td><td><font color="#67636B" >Experience(yr.month):</font> <font color="black" size="2">${modelObject.experience}</font></td>
		<td><font color="#67636B" >Batch Date:</font> <font color="black" size="2"><fmt:formatDate pattern="MM-dd-yyyy" value="${modelObject.batchFrom}" /></font>
		</td></tr>
		
		
</table>
	<table width="100%" id="form-table" cellspacing="1" cellpadding="5" >
		<tr class="table-heading">
			<td colspan="4"><b><primo:label code="Update Employee Bonus"/></b></td>
		</tr>
		
		<tr>
		
	   <td colspan="3">
	  
	   <table class="datagrid" width="100%">
	   <tr><th>Bonus Type</th><th>Amount</th><th >Note</th></tr>
		 <c:forEach var="item" items="${bonustypese}">
		 <input type="hidden" name="bonuslistId" value="${item.id}"/>
	<tr>	
	<td>${item.bonusType.typename}</td>
		<td><input type="text" value="${item.bonusamount}" name="bonusAmt"/></td>
		<td><input type="text" value="${item.note}" style="min-width:220px; max-width:220px" name="bonusNtes"/></td>
		</tr>
		</c:forEach>
		</table>
	   
		</td>
		
		</tr>
	<tr><td><div id="divta" class="datagrid" style="border: none"></div></td></tr>
		
		<tr><td colspan="2"></td></tr>
		<tr>
			
			<td align="${left}" colspan="1"><input type="submit"
				name="create" id="create" value="<primo:label code="Save"/>" class="flat" /> 
				<input
				type="reset" id="resetBtn" value="<primo:label code="Reset"/>"
				class="flat" /> <input type="button" id="cancelBtn"
				value="<primo:label code="Cancel"/>" class="flat"
				onClick="location.href='list.do'" />
			</td>
			
			</tr>
		</table>
	</form>


