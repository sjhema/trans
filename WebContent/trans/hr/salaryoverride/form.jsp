<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function formatDate() {
	var date=document.getElementById("datepicker").value;
	if(date!=""){
	if(date.length<8){
		alert("Invalid date format");
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
						alert("Invalid date format");
						document.getElementById("datepicker").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("Invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}
				else if(dd>31){
					alert("Invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}
			}if(enddigit==00 && yy%400==0){
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("Invalid date format");
						document.getElementById("datepicker").value="";
						return true;
					}
				}if(mm==02 && dd>29){
					alert("Invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}else if(dd>31){
					alert("Invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}					
			}else{
				if(mm==04 || mm==06 || mm==09 || mm==11){
					if(dd>30){
						alert("Invalid date format");
						document.getElementById("datepicker").value="";
						return true;
					}
				}if(mm==02 && dd>28){
					alert("Invalid date format");
					document.getElementById("datepicker").value="";
					return true;
				}else if(dd>31){
					alert("Invalid date format");
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

</script>
<br/>
<form:form action="save.do" name="salaryOverrideForm" commandName="modelObject" method="post">
	<form:hidden path="id" id="id" />
	<table width="100%" id="form-table" cellspacing="1" cellpadding="5"">
		<tr class="table-heading">
			<td colspan="6"><b><primo:label code="Add/Update Salary Override" /></b></td>
		</tr>	
		<tr>
			<td class="form-left"><primo:label code="Employee" /><span class="errorMessage">*</span></td>		
			<td>
				<form:select cssClass="flat" path="driver" id="driverId" style="min-width:184px; max-width:184px">
					<form:option value="">------<primo:label code="Please Select" />------</form:option>
					<form:options items="${employees}" itemValue="id" itemLabel="fullName"></form:options> 
				</form:select>
				<br> 
				<form:errors path="driver" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left"><primo:label code="Batch Date" /><span class="errorMessage">*</span></td>
			<td align="${left}">
				<form:input id="datepicker" path="payrollBatch" style="min-width:180px; max-width:180px"
					cssClass="flat" onblur="return formatDate();"/> 
				<br>
				<form:errors path="payrollBatch" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left" >
				<primo:label code="Override Amount" /><span class="errorMessage">*</span>
			</td>
			<td align="${left}">
				<form:input id="amount" style="min-width:150px; max-width:150px" path="amount" cssClass="flat"/> 
				<br> 
				<form:errors path="amount" cssClass="errorMessage" />
			</td>
		</tr>
		<tr>
			<td class="form-left">
			  	<primo:label code="Notes" />
			</td>
			<td align="${left}" colspan="5">
				<form:textarea path="notes" rows="2" cols="69"/>    	
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="${left}" colspan="2">
				<input type="submit" name="create" id="create" value="<primo:label code="Save"/>" class="flat2" /> 
				<input type="reset" id="resetBtn" value="<primo:label code="Reset"/>" class="flat2" /> 
				<input type="button" id="cancelBtn" value="<primo:label code="Cancel"/>" class="flat2" onClick="location.href='list.do'" />
			</td>
		</tr>		
	</table>
</form:form>
